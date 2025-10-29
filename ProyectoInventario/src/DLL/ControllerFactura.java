package DLL;

import java.sql.*;

public class ControllerFactura {

    private static void cerrar(AutoCloseable... r) {
        for (AutoCloseable x : r) { if (x != null) try { x.close(); } catch (Exception ignored) {} }
    }

    // Item transitorio para armar el carrito
    private static class ItemVenta {
        int idProducto;
        int cantidad;
        ItemVenta(int idProducto, int cantidad) { this.idProducto = idProducto; this.cantidad = cantidad; }
    }

    /**
     * Flujo interactivo de venta:
     * - Permite agregar múltiples ítems (idProducto, cantidad)
     * - Prevalida stock por cada ítem
     * - Abre transacción: inserta factura (Venta), inserta detalles, descuenta stock (movimientos SALIDA)
     * - Muestra resumen
     */
    // DLL/ControllerFactura.java  (reemplazar crearVentaInteractiva)
// DLL/ControllerFactura.java
public static void crearVentaInteractiva(int idUsuarioVendedor) {
    record ItemVenta(int idProducto, int cantidad) {}
    java.util.List<ItemVenta> carrito = new java.util.ArrayList<>();

    boolean seguir = true;
    while (seguir) {
        String inId = javax.swing.JOptionPane.showInputDialog("ID de producto (Cancelar para salir):");
        if (inId == null) {
            if (carrito.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null, "Venta cancelada.");
                return;
            } else break;
        }

        inId = inId.trim();
        if (inId.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Debe ingresar un ID de producto.");
            continue;
        }

        String inCant = javax.swing.JOptionPane.showInputDialog("Cantidad:");
        if (inCant == null) {
            if (carrito.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null, "Venta cancelada.");
                return;
            } else break;
        }

        inCant = inCant.trim();
        if (inCant.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad.");
            continue;
        }

        try {
            int idProd = Integer.parseInt(inId);
            int cant = Integer.parseInt(inCant);
            if (cant <= 0) {
                javax.swing.JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a cero.");
                continue;
            }

            models.Producto p = DLL.ControllerProducto.buscarPorID(idProd);
            if (p == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Producto no encontrado.");
                continue;
            }

            if (cant > p.getStock_actual()) {
                javax.swing.JOptionPane.showMessageDialog(null, "Stock insuficiente. Disponible: " + p.getStock_actual());
                continue;
            }

            carrito.add(new ItemVenta(idProd, cant));
            javax.swing.JOptionPane.showMessageDialog(null, "Producto agregado: " + p.getNombre() + " x" + cant);

            int resp = javax.swing.JOptionPane.showConfirmDialog(
                    null, "¿Agregar otro producto?", "Carrito",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );
            if (resp != javax.swing.JOptionPane.YES_OPTION) seguir = false;

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Datos inválidos.");
        }
    }

    if (carrito.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(null, "No se agregaron productos.");
        return;
    }

    // --- Resumen ---
    double totalFactura = 0.0;
    StringBuilder resumen = new StringBuilder("Resumen de venta\n\n");
    for (ItemVenta it : carrito) {
        models.Producto p = DLL.ControllerProducto.buscarPorID(it.idProducto());
        double subtotal = p.getPrecio() * it.cantidad();
        totalFactura += subtotal;
        resumen.append("• ").append(p.getNombre())
               .append("  x").append(it.cantidad())
               .append("  $").append(String.format("%.2f", p.getPrecio()))
               .append("\n");
    }
    resumen.append("\nTOTAL: $").append(String.format("%.2f", totalFactura));

    int confirmar = javax.swing.JOptionPane.showConfirmDialog(
            null, resumen.toString() + "\n\n¿Confirmar venta?",
            "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION
    );
    if (confirmar != javax.swing.JOptionPane.YES_OPTION) {
        javax.swing.JOptionPane.showMessageDialog(null, "Venta cancelada.");
        return;
    }

    // --- Transacción ---
    java.sql.Connection con = null;
    java.sql.PreparedStatement stFactura = null;
    java.sql.PreparedStatement stDetalle = null;
    java.sql.ResultSet rsKeys = null;

    try {
        con = DLL.ConexionDB.getInstance().getConnection();
        boolean oldAuto = con.getAutoCommit();
        con.setAutoCommit(false);

        stFactura = con.prepareStatement(
                "INSERT INTO factura (fecha, tipo, id_usuario) VALUES (?, ?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS
        );
        stFactura.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
        stFactura.setString(2, "Venta");
        stFactura.setInt(3, idUsuarioVendedor);
        stFactura.executeUpdate();

        rsKeys = stFactura.getGeneratedKeys();
        if (!rsKeys.next()) throw new java.sql.SQLException("No se pudo obtener ID de factura.");
        int idFactura = rsKeys.getInt(1);

        stDetalle = con.prepareStatement(
                "INSERT INTO detalle_factura (id_factura, id_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)"
        );

        for (ItemVenta it : carrito) {
            models.Producto p = DLL.ControllerProducto.buscarPorID(it.idProducto());
            double subtotal = p.getPrecio() * it.cantidad();

            stDetalle.setInt(1, idFactura);
            stDetalle.setInt(2, p.getId_producto());
            stDetalle.setInt(3, it.cantidad());
            stDetalle.setBigDecimal(4, java.math.BigDecimal.valueOf(subtotal));
            stDetalle.executeUpdate();
        }

        for (ItemVenta it : carrito) {
            DLL.ControllerProducto.aplicarMovimiento(
                    it.idProducto(), it.cantidad(), Enums.TipoMovimiento.SALIDA, idUsuarioVendedor
            );
        }

        con.commit();
        con.setAutoCommit(oldAuto);

        StringBuilder ticket = new StringBuilder();
        ticket.append("Factura de Venta #").append(idFactura).append("\n")
              .append("Fecha: ").append(java.time.LocalDateTime.now()).append("\n\n");
        for (ItemVenta it : carrito) {
            models.Producto p = DLL.ControllerProducto.buscarPorID(it.idProducto());
            ticket.append("• ").append(p.getNombre())
                  .append(" x").append(it.cantidad())
                  .append(" @ $").append(String.format("%.2f", p.getPrecio()))
                  .append("\n");
        }
        ticket.append("\nTOTAL: $").append(String.format("%.2f", totalFactura));
        javax.swing.JOptionPane.showMessageDialog(null, ticket.toString());

    } catch (Exception e) {
        try { if (con != null) con.rollback(); } catch (Exception ignored) {}
        javax.swing.JOptionPane.showMessageDialog(null, "Error en la venta: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try { if (rsKeys != null) rsKeys.close(); } catch (Exception ignored) {}
        try { if (stDetalle != null) stDetalle.close(); } catch (Exception ignored) {}
        try { if (stFactura != null) stFactura.close(); } catch (Exception ignored) {}
    }
}



    // Lee producto.costo (precio de compra) usando la misma conexión transaccional
    private static double obtenerCostoProducto(int idProducto, Connection con) {
        PreparedStatement st = null; ResultSet rs = null;
        try {
            st = con.prepareStatement("SELECT costo FROM producto WHERE id_producto = ?");
            st.setInt(1, idProducto);
            rs = st.executeQuery();
            if (rs.next()) return rs.getDouble("costo");
        } catch (Exception ignored) {
        } finally {
            cerrar(rs, st);
        }
        return 0.0;
    }
}
