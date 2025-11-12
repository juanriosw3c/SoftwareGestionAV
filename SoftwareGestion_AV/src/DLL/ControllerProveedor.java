package DLL;

import java.sql.*;
import javax.swing.JOptionPane;
import models.Proveedor;

public class ControllerProveedor {

    private static void cerrar(AutoCloseable... r) {
        for (AutoCloseable x : r) { if (x != null) try { x.close(); } catch (Exception ignored) {} }
    }

    public static void agregarProveedor(Proveedor p) {
        PreparedStatement st = null;
        try {
            Connection con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement(
                "INSERT INTO proveedor (nombre, contacto, telefono, email) VALUES (?, ?, ?, ?)"
            );
            st.setString(1, p.getNombre());
            st.setString(2, p.getContacto());
            st.setString(3, p.getTelefono());
            st.setString(4, p.getEmail());
            int n = st.executeUpdate();
            JOptionPane.showMessageDialog(null, n > 0 ? "Proveedor agregado" : "No se pudo agregar");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar proveedor: " + e.getMessage());
        } finally { cerrar(st); }
    }

    public static Proveedor buscarPorID(int id) {
        PreparedStatement st = null; ResultSet rs = null;
        try {
            Connection con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement("SELECT * FROM proveedor WHERE id_proveedor = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return new Proveedor(
                    rs.getInt("id_proveedor"),
                    rs.getString("nombre"),
                    rs.getString("contacto"),
                    rs.getString("telefono"),
                    rs.getString("email")
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar proveedor: " + e.getMessage());
        } finally { cerrar(rs, st); }
        return null;
    }

    public static void editarProveedor(int id) {
        Proveedor p = buscarPorID(id);
        if (p == null) { JOptionPane.showMessageDialog(null, "Proveedor no encontrado"); return; }

        String[] ops = {"Nombre", "Contacto", "Teléfono", "Email", "Cancelar"};
        int op = JOptionPane.showOptionDialog(null, "Editar Proveedor: " + p.getNombre(),
                "Editar proveedor", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, ops, ops[0]);
        if (op == 4 || op == JOptionPane.CLOSED_OPTION) return;

        String campo = null; String nuevo = null;
        try {
            switch (op) {
                case 0 -> { campo = "nombre";   nuevo = JOptionPane.showInputDialog("Nuevo nombre:", p.getNombre()); }
                case 1 -> { campo = "contacto"; nuevo = JOptionPane.showInputDialog("Nuevo contacto:", p.getContacto()); }
                case 2 -> { campo = "telefono"; nuevo = JOptionPane.showInputDialog("Nuevo teléfono:", p.getTelefono()); }
                case 3 -> { campo = "email";    nuevo = JOptionPane.showInputDialog("Nuevo email:", p.getEmail()); }
            }
            if (nuevo == null) return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida: " + e.getMessage());
            return;
        }

        PreparedStatement st = null;
        try {
            Connection con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement("UPDATE proveedor SET " + campo + " = ? WHERE id_proveedor = ?");
            st.setString(1, nuevo);
            st.setInt(2, id);
            int n = st.executeUpdate();
            JOptionPane.showMessageDialog(null, n > 0 ? "Proveedor actualizado" : "No se pudo actualizar");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
        } finally { cerrar(st); }
    }

    public static Proveedor eliminarPorID(int id) {
        Proveedor p = buscarPorID(id);
        if (p == null) { JOptionPane.showMessageDialog(null, "Proveedor no encontrado"); return null; }

        PreparedStatement st = null;
        try {
            Connection con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement("DELETE FROM proveedor WHERE id_proveedor = ?");
            st.setInt(1, id);
            int n = st.executeUpdate();
            JOptionPane.showMessageDialog(null, n > 0 ? "Proveedor eliminado" : "No se pudo eliminar");
            return (n > 0) ? p : null;
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "No se puede eliminar: está referido por productos o facturas.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        } finally { cerrar(st); }
        return null;
    }

    public static void listarProveedores() {
        PreparedStatement st = null; ResultSet rs = null;
        try {
            Connection con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement("SELECT * FROM proveedor");
            rs = st.executeQuery();
            StringBuilder sb = new StringBuilder("Proveedores:\n\n");
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id_proveedor"))
                  .append(" | Nombre: ").append(rs.getString("nombre"))
                  .append(" | Contacto: ").append(rs.getString("contacto"))
                  .append(" | Tel: ").append(rs.getString("telefono"))
                  .append(" | Email: ").append(rs.getString("email")).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar proveedores: " + e.getMessage());
        } finally { cerrar(rs, st); }
    }
}
