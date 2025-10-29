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
public static void crearVentaInteractiva(int idUsuarioVendedor) {
    record ItemVenta(int idProducto, int cantidad) {}
    java.util.List<ItemVenta> carrito = new java.util.ArrayList<>();

    // --- Carga de ítems con confirmaciones claras ---
    boolean seguir = true;
    while (seguir) {
        String inId = javax.swing.JOptionPane.showInputDialog("ID de producto (Cancelar para salir):");
        if (inId == null) { // Cancelado
            if (carrito.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null, "Venta cancelada.");
                return;
            } else {
                break; // hay items cargados; pasamos a confirmar venta
            }
        }
        inId = inId.trim();
        if (inId.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Debes ingresar un ID de producto.");
            continue;
        }

        String inCant = javax.swing.JOptionPane.showInputDialog("Cantidad (Cancelar para salir):");
        if (inCant == null) {
            if (carrito.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null, "Venta cancelada.");
                return;
            } else {
                break;
            }
        }
        inCant = inCant.trim();
        if (inCant.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Debes ingresar una cantidad.");
            continue;
        }

        try {
            int idProd = Integer.parseInt(inId);
            int cant   = Integer.parseInt(inCant);
            if (cant <= 0) {
                javax.swing.JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a cero.");
                continue;
            }

            // Prevalidación inmediata por ítem (UX): existencia + stock
            models.Producto p = DLL.ControllerProducto.buscarPorID(idProd);
            if (p == null) {
                javax.swing.JOptionPane.showMessageDialog(null, "Producto inexistente (ID " + idProd + ").");
                continue;
            }
            if (cant > p.getStock_actual()) {
                javax.swing.JOptionPane.showMessageDialog(null, "Stock insuficiente para " + p.getNombre()
                        + ". Disponible: " + p.getStock_actual());
                continue;
            }

            // Agregar al carrito
            carrito.add(new ItemVenta(idProd, cant));
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Agregado: " + p.getNombre() + "  x " + cant + " @ $" + String.format("%.2f", p.getPrecio()));

            // ¿Desea agregar otro ítem?
            int resp = javax.swing.JOptionPane.showConfirmDialog(
                    null, "¿Agregar otro producto?", "Carrito",
                    javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE
            );
            if (resp != javax.swing.JOptionPane.YES_OPTION) {
                break; // pasar a resumen
            }

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Valores inválidos (ID/Cantidad).");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    if (carrito.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(null, "No se agregaron productos.");
        return;
    }

    // --- Resumen previo a confirmar venta ---
    double totalFactura = 0.0;
    StringBuilder resumen = new StringBuilder("Resumen de venta (pre-visualización)\n\n");
    for (ItemVenta it : carrito) {
        models.Producto p = DLL.ControllerProducto.buscarPorID(it.idProducto());
        if (p == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "Producto no encontrado en resumen (ID " + it.idProducto() + ").");
            return;
        }
        double subtotal = p.getPrecio() * it.cantidad();
        totalFactura += subtotal;
        resumen.append("• ").append(p.getNombre())
               .append("  x ").append(it.cantidad())
               .append("  @ $").append(String.format("%.2f", p.getPrecio()))
               .append("\n");
    }
    resumen.append("\nTOTAL: $").append(String.format("%.2f", totalFactura));

    int confirmar = javax.swing.JOptionPane.showConfirmDialog(
            null, resumen.toString() + "\n\n¿Confirmar venta?",
            "Confirmación de venta", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.INFORMATION_MESSAGE
    );
    if (confirmar != javax.swing.JOptionPane.YES_OPTION) {
        javax.swing.JOptionPane.showMessageDialog(null, "Venta cancelada.");
        return;
    }

    // --- Transacción: factura + detalles + movimientos de stock ---
    java.sql.Connection con = null;
    java.sql.PreparedStatement stFactura = null;
    java.sql.PreparedStatement stDetalle = null;
    java.sql.ResultSet rsKeys = null;

    try {
        con = DLL.ConexionDB.getInstance().getConnection();
        boolean oldAuto = con.getAutoCommit();
        con.setAutoCommit(false); // BEGIN

        // 1) Insertar factura (tipo = 'Venta', fecha=DATE hoy según tu schema)
        stFactura = con.prepareStatement(
                "INSERT INTO factura (fecha, tipo, id_usuario) VALUES (?, ?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS
        );
        stFactura.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now())); // tu columna es DATE
        stFactura.setString(2, "Venta");
        stFactura.setInt(3, idUsuarioVendedor);
        stFactura.executeUpdate();
        rsKeys = stFactura.getGeneratedKeys();
        if (!rsKeys.next()) throw new java.sql.SQLException("No se pudo obtener ID de factura.");
        int idFactura = rsKeys.getInt(1);

        // 2) Insertar detalles
        stDetalle = con.prepareStatement(
                "INSERT INTO detalle_factura (id_factura, id_producto, cantidad, precio_compra, precio_venta, subtotal) " +
                "VALUES (?, ?, ?, ?, ?, ?)"
        );

        for (ItemVenta it : carrito) {
            models.Producto p = DLL.ControllerProducto.buscarPorID(it.idProducto());
            if (p == null) throw new java.sql.SQLException("Producto no encontrado en ejecución: " + it.idProducto());

            double precioVenta = p.getPrecio();
            double costo = obtenerCostoProducto(p.getId_producto(), con); // lee producto.costo si aplicaste el ALTER
            double subtotal = precioVenta * it.cantidad();

            stDetalle.setInt(1, idFactura);
            stDetalle.setInt(2, p.getId_producto());
            stDetalle.setInt(3, it.cantidad());
            stDetalle.setBigDecimal(4, java.math.BigDecimal.valueOf(costo));        // precio_compra
            stDetalle.setBigDecimal(5, java.math.BigDecimal.valueOf(precioVenta));  // precio_venta
            stDetalle.setBigDecimal(6, java.math.BigDecimal.valueOf(subtotal));     // subtotal (aunque no lo muestres)
            stDetalle.executeUpdate();
        }

        // 3) Descontar stock por cada ítem (movimiento SALIDA) usando unificado
        for (ItemVenta it : carrito) {
            boolean ok = DLL.ControllerProducto.aplicarMovimiento(
                    it.idProducto(), it.cantidad(), Enums.TipoMovimiento.SALIDA, idUsuarioVendedor
            );
            if (!ok) throw new java.sql.SQLException("Fallo al aplicar movimiento para producto " + it.idProducto());
        }

        con.commit(); // COMMIT
        con.setAutoCommit(oldAuto);

        // 4) Ticket final (confirmación)
        StringBuilder ticket = new StringBuilder();
        ticket.append("Factura de Venta #").append(idFactura).append("\n")
              .append("Fecha: ").append(java.time.LocalDateTime.now()).append("\n\n");
        for (ItemVenta it : carrito) {
            models.Producto p = DLL.ControllerProducto.buscarPorID(it.idProducto());
            ticket.append("• ").append(p.getNombre())
                  .append("  x ").append(it.cantidad())
                  .append("  @ $").append(String.format("%.2f", p.getPrecio()))
                  .append("\n");
        }
        ticket.append("\nTOTAL: $").append(String.format("%.2f", totalFactura));
        javax.swing.JOptionPane.showMessageDialog(null, ticket.toString());

    } catch (Exception e) {
        try { if (con != null) con.rollback(); } catch (Exception ignored) {}
        javax.swing.JOptionPane.showMessageDialog(null, "Error en la venta: " + e.getMessage());
        e.printStackTrace();
    } finally {
        if (rsKeys != null) try { rsKeys.close(); } catch (Exception ignored) {}
        if (stDetalle != null) try { stDetalle.close(); } catch (Exception ignored) {}
        if (stFactura != null) try { stFactura.close(); } catch (Exception ignored) {}
        // NO cerramos la Connection (singleton)
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
