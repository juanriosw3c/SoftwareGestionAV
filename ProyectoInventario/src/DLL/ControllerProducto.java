package DLL;

import Enums.TipoMovimiento;
import java.sql.*;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;
import models.Producto;

public class ControllerProducto {

    // -------- Helpers --------
    private static void cerrar(AutoCloseable... r) {
        for (AutoCloseable x : r) { if (x != null) try { x.close(); } catch (Exception ignored) {} }
    }

    private static void logMovimiento(int idProducto, int cantidad, TipoMovimiento tipo, int idUsuario) throws Exception {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = ConexionDB.getInstance().getConnection();
            stmt = con.prepareStatement(
                "INSERT INTO movimiento_stock (tipo, fecha, cantidad, id_producto, id_usuario) " +
                "VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, tipo.toDbValue()); // 'Entrada' | 'Salida' | 'Ajuste'
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, cantidad);
            stmt.setInt(4, idProducto);
            if (idUsuario > 0) stmt.setInt(5, idUsuario); else stmt.setNull(5, Types.INTEGER);
            stmt.executeUpdate();
        } finally {
            cerrar(stmt);
        }
    }

    // -------- CRUD PRODUCTO (ADMIN) --------
    public static void agregarProducto(Producto p) {
        Connection con = null; PreparedStatement st = null;
        try {
            con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement(
                "INSERT INTO producto (nombre, descripcion, precio, stock_actual, stock_minimo) VALUES (?, ?, ?, ?, ?)"
            );
            st.setString(1, p.getNombre());
            st.setString(2, p.getDescripcion());
            st.setDouble(3, p.getPrecio());
            st.setInt(4, p.getStock_actual());
            st.setInt(5, p.getStock_minimo());  // ✅ antes estaba duplicado el índice 4

            int n = st.executeUpdate();
            if (n > 0) JOptionPane.showMessageDialog(null, "Producto agregado correctamente");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al agregar: " + e.getMessage());
        } finally { cerrar(st); }
    }

    public static Producto buscarPorID(int id) {
        Producto p = null;
        Connection con = null; PreparedStatement st = null; ResultSet rs = null;
        try {
            con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement("SELECT * FROM producto WHERE id_producto = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                p = new Producto(
                    id,
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock_actual"),
                    rs.getInt("stock_minimo")
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + e.getMessage());
        } finally { cerrar(rs, st); }
        return p;
    }

    public static Producto eliminarPorID(int id) {
        Producto p = buscarPorID(id);
        if (p == null) { JOptionPane.showMessageDialog(null, "No existe el producto"); return null; }

        Connection con = null; PreparedStatement st = null;
        try {
            con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement("DELETE FROM producto WHERE id_producto = ?");
            st.setInt(1, id);
            int n = st.executeUpdate();
            if (n > 0) { JOptionPane.showMessageDialog(null, "Producto eliminado"); return p; }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
        } finally { cerrar(st); }
        return null;
    }

    public static void mostrarTodosProductos() {
        Connection con = null; PreparedStatement st = null; ResultSet rs = null;
        try {
            con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement("SELECT * FROM producto");
            rs = st.executeQuery();
            StringBuilder sb = new StringBuilder("Lista de productos:\n\n");
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id_producto"))
                  .append(" | Nombre: ").append(rs.getString("nombre"))
                  .append(" | Descripción: ").append(rs.getString("descripcion"))
                  .append(" | Precio: ").append(rs.getDouble("precio"))
                  .append(" | Stock actual: ").append(rs.getInt("stock_actual"))
                  .append(" | Stock mínimo: ").append(rs.getInt("stock_minimo"))
                  .append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar: " + e.getMessage());
        } finally { cerrar(rs, st); }
    }

    public static void editarProducto(int id) {
        Producto p = buscarPorID(id);
        if (p == null) { JOptionPane.showMessageDialog(null, "No existe el producto"); return; }

        String[] opts = {"Nombre", "Descripción", "Precio", "Stock actual", "Stock mínimo", "Cancelar"};
        int op = JOptionPane.showOptionDialog(null,
            "¿Qué campo deseas editar para: " + p.getNombre() + "?",
            "Editar producto", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, opts, opts[0]);

        if (op == 5 || op == JOptionPane.CLOSED_OPTION) return;

        String campo = null; Object nuevoValor = null;
        try {
            switch (op) {
                case 0 -> { campo = "nombre"; nuevoValor = JOptionPane.showInputDialog("Nuevo nombre:", p.getNombre()); }
                case 1 -> { campo = "descripcion"; nuevoValor = JOptionPane.showInputDialog("Nueva descripción:", p.getDescripcion()); }
                case 2 -> { campo = "precio"; nuevoValor = Double.parseDouble(JOptionPane.showInputDialog("Nuevo precio:", p.getPrecio())); }
                case 3 -> { campo = "stock_actual"; nuevoValor = Integer.parseInt(JOptionPane.showInputDialog("Nuevo stock actual:", p.getStock_actual())); }
                case 4 -> { campo = "stock_minimo"; nuevoValor = Integer.parseInt(JOptionPane.showInputDialog("Nuevo stock mínimo:", p.getStock_minimo())); }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Entrada inválida: " + e.getMessage());
            return;
        }

        PreparedStatement st = null;
        try {
            Connection con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement("UPDATE producto SET " + campo + " = ? WHERE id_producto = ?");
            if (nuevoValor instanceof String s) st.setString(1, s);
            if (nuevoValor instanceof Double d) st.setDouble(1, d);
            if (nuevoValor instanceof Integer i) st.setInt(1, i);
            st.setInt(2, id);
            int n = st.executeUpdate();
            JOptionPane.showMessageDialog(null, n > 0 ? "Producto actualizado." : "No se pudo actualizar.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
        } finally { cerrar(st); }
    }

    // -------- UN SOLO MÉTODO PARA TODOS LOS MOVIMIENTOS --------
    public static boolean aplicarMovimiento(int idProducto, int cantidad, TipoMovimiento tipo, int idUsuario) {
        if (cantidad <= 0) { JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a cero."); return false; }

        Producto p = buscarPorID(idProducto);
        if (p == null) { JOptionPane.showMessageDialog(null, "Producto inexistente"); return false; }

        int signo = switch (tipo) {
            case ENTRADA -> +1;            // Entrada de mercadería
            case SALIDA, AJUSTE -> -1;     // Venta o Pedido interno/merma
        };

        int nuevoStock = p.getStock_actual() + (signo * cantidad);
        if (nuevoStock < 0) {
            JOptionPane.showMessageDialog(null, "Stock insuficiente. Disponible: " + p.getStock_actual());
            return false;
        }

        PreparedStatement st = null;
        try {
            Connection con = ConexionDB.getInstance().getConnection();
            st = con.prepareStatement("UPDATE producto SET stock_actual = ? WHERE id_producto = ?");
            st.setInt(1, nuevoStock);
            st.setInt(2, idProducto);
            int n = st.executeUpdate();
            if (n > 0) {
                logMovimiento(idProducto, cantidad, tipo, idUsuario);
                JOptionPane.showMessageDialog(null,
                    tipo.toDbValue() + " registrada. Stock actual: " + nuevoStock);
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al aplicar movimiento: " + e.getMessage());
        } finally { cerrar(st); }
        return false;
    }

    // -------- Wrappers interactivos (si te sirven en menús) --------
    public static void venderInteractivo(int idProducto, int idUsuario) {
        try {
            int cant = Integer.parseInt(JOptionPane.showInputDialog(null, "Cantidad vendida:"));
            aplicarMovimiento(idProducto, cant, TipoMovimiento.SALIDA, idUsuario);
        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Entrada inválida: " + e.getMessage()); }
    }

    public static void reponerInteractivo(int idProducto, int idUsuario) {
        try {
            int cant = Integer.parseInt(JOptionPane.showInputDialog(null, "Cantidad a reponer:"));
            aplicarMovimiento(idProducto, cant, TipoMovimiento.ENTRADA, idUsuario);
        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Entrada inválida: " + e.getMessage()); }
    }

    public static void ajusteInteractivo(int idProducto, int idUsuario) {
        try {
            int cant = Integer.parseInt(JOptionPane.showInputDialog(null, "Cantidad a descontar (pedido interno/merma):"));
            aplicarMovimiento(idProducto, cant, TipoMovimiento.AJUSTE, idUsuario);
        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Entrada inválida: " + e.getMessage()); }
    }
}
