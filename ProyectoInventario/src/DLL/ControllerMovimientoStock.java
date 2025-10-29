package DLL;

import java.sql.*;
import javax.swing.*;

public class ControllerMovimientoStock {

    public static void listarMovimientos() {
        PreparedStatement st = null;
        ResultSet rs = null;

        final String sql = """
            SELECT m.id_movimiento, m.tipo, m.fecha, m.cantidad,
                   p.nombre AS producto, u.nombre_usuario AS usuario
            FROM movimiento_stock m
            LEFT JOIN producto p ON m.id_producto = p.id_producto
            LEFT JOIN usuarios u ON m.id_usuario = u.id_usuario
            ORDER BY m.fecha DESC
        """;

        try {
            Connection con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement(sql);
            rs = st.executeQuery();

            StringBuilder sb = new StringBuilder("Historial de Movimientos de Stock\n\n");
            sb.append(String.format("%-5s %-10s %-20s %-10s %-20s %-15s%n",
                    "ID", "Tipo", "Fecha", "Cant.", "Producto", "Usuario"));
            sb.append("--------------------------------------------------------------------------\n");

            boolean hay = false;
            while (rs.next()) {
                hay = true;
                sb.append(String.format("%-5d %-10s %-20s %-10d %-20s %-15s%n",
                        rs.getInt("id_movimiento"),
                        rs.getString("tipo"),
                        rs.getTimestamp("fecha"),
                        rs.getInt("cantidad"),
                        rs.getString("producto"),
                        rs.getString("usuario")));
            }

            if (!hay) {
                JOptionPane.showMessageDialog(null, "No hay movimientos registrados.");
                return;
            }

            JOptionPane.showMessageDialog(null, sb.toString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar movimientos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (Exception ignored) {}
        }
    }
}
