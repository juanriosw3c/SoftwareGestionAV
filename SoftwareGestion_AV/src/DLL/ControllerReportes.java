package DLL;

import java.sql.*;
import javax.swing.*;

public class ControllerReportes {

    private static void cerrar(AutoCloseable... r) {
        for (AutoCloseable x : r) { if (x != null) try { x.close(); } catch (Exception ignored) {} }
    }

    /**
     * Lista vendedores, cantidad de ventas y monto total de todas sus ventas.
     * Cuenta una "venta" por cada factura tipo 'Venta' (COUNT DISTINCT de facturas),
     * y suma el total con SUM de los subtotales de detalle_factura.
     */
    public static void reporteVentasPorVendedor() {
        PreparedStatement st = null;
        ResultSet rs = null;

        final String sql = """
            SELECT
                u.id_usuario,
                u.nombre_usuario,
                COUNT(DISTINCT f.id_factura) AS ventas,
                COALESCE(SUM(d.subtotal), 0)  AS total
            FROM usuarios u
            LEFT JOIN factura f
                   ON f.id_usuario = u.id_usuario
                  AND f.tipo = 'Venta'
            LEFT JOIN detalle_factura d
                   ON d.id_factura = f.id_factura
            WHERE UPPER(u.rol) = 'VENDEDOR'
            GROUP BY u.id_usuario, u.nombre_usuario
            ORDER BY total DESC, ventas DESC, u.nombre_usuario ASC
        """;

        try {
            Connection con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement(sql);
            rs = st.executeQuery();

            StringBuilder sb = new StringBuilder("Reporte de Ventas por Vendedor\n\n");
            sb.append(String.format("%-5s %-18s %10s %16s%n", "ID", "Vendedor", "Ventas", "Total ($)"));
            sb.append("-----------------------------------------------------------\n");

            boolean hayFilas = false;
            while (rs.next()) {
                hayFilas = true;
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre_usuario");
                int ventas = rs.getInt("ventas");
                double total = rs.getDouble("total");
                sb.append(String.format("%-5d %-18s %10d %16.2f%n", id, nombre, ventas, total));
            }

            if (!hayFilas) {
                JOptionPane.showMessageDialog(null, "No hay vendedores con ventas registradas.");
                return;
            }

            JOptionPane.showMessageDialog(null, sb.toString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar reporte: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrar(rs, st);
        }
    }

    /**
     * Variante con filtro de fechas (inclusive) en la fecha de la factura.
     * Formato esperado: 'YYYY-MM-DD' (coincide con DATE de la tabla factura).
     */
    public static void reporteVentasPorVendedor(String fechaDesde, String fechaHasta) {
        PreparedStatement st = null;
        ResultSet rs = null;

        final String sql = """
            SELECT
                u.id_usuario,
                u.nombre_usuario,
                COUNT(DISTINCT f.id_factura) AS ventas,
                COALESCE(SUM(d.subtotal), 0)  AS total
            FROM usuarios u
            LEFT JOIN factura f
                   ON f.id_usuario = u.id_usuario
                  AND f.tipo = 'Venta'
                  AND f.fecha BETWEEN ? AND ?
            LEFT JOIN detalle_factura d
                   ON d.id_factura = f.id_factura
            WHERE UPPER(u.rol) = 'VENDEDOR'
            GROUP BY u.id_usuario, u.nombre_usuario
            ORDER BY total DESC, ventas DESC, u.nombre_usuario ASC
        """;

        try {
            Connection con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement(sql);
            st.setDate(1, java.sql.Date.valueOf(fechaDesde));
            st.setDate(2, java.sql.Date.valueOf(fechaHasta));
            rs = st.executeQuery();

            String titulo = "Reporte de Ventas por Vendedor\nRango: " + fechaDesde + " a " + fechaHasta + "\n\n";
            StringBuilder sb = new StringBuilder(titulo);
            sb.append(String.format("%-5s %-18s %10s %16s%n", "ID", "Vendedor", "Ventas", "Total ($)"));
            sb.append("-----------------------------------------------------------\n");

            boolean hayFilas = false;
            while (rs.next()) {
                hayFilas = true;
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre_usuario");
                int ventas = rs.getInt("ventas");
                double total = rs.getDouble("total");
                sb.append(String.format("%-5d %-18s %10d %16.2f%n", id, nombre, ventas, total));
            }

            if (!hayFilas) {
                JOptionPane.showMessageDialog(null, "No hay ventas en el rango seleccionado.");
                return;
            }

            JOptionPane.showMessageDialog(null, sb.toString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al generar reporte filtrado: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cerrar(rs, st);
        }
    }
}
