// File: DLL/ControllerUsuario.java
package DLL;

import Enums.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import models.UsuarioAdmin;
import models.UsuarioBase;
import models.UsuarioDeposito;
import models.UsuarioVendedor;

public class ControllerUsuario {

    // Login
   // Login - ✅ ELIMINADO MENSAJE DUPLICADO DE BIENVENIDA
public static UsuarioBase login(String email, String contrasena) {
    UsuarioBase usuario = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        con = ConexionDB.getInstance().getConnection();

        String sql = "SELECT * FROM usuarios WHERE email = ? AND contrasena = ?";
        stmt = con.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, contrasena);
        rs = stmt.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("id_usuario");
            String nombre = rs.getString("nombre_usuario");
            String rolStr = rs.getString("rol");
            Rol rol = Rol.valueOf(rolStr.toUpperCase());

            // ✅ INSTANCIAS CORRECTAS SEGÚN ROL
            switch (rol) {
                case ADMIN:
                    usuario = new UsuarioAdmin(id, nombre, email, contrasena);
                    break;
                case VENDEDOR:
                    usuario = new UsuarioVendedor(id, nombre, email, contrasena);
                    break;
                case DEPOSITO:
                    usuario = new UsuarioDeposito(id, nombre, email, contrasena);
                    break;
                default:
                    usuario = new UsuarioBase(id, nombre, email, contrasena, rol);
                    break;
            }
            // ✅ ELIMINADO: JOptionPane.showMessageDialog(null, "Bienvenido " + rolStr + " " + nombre);
        }

    } catch (Exception e) {
        System.out.println("Error en login: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            // ✅ NO CERRAR CONEXIÓN SINGLETON
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return usuario;
}

// Agregar usuario
public static void agregarUsuario(UsuarioBase usuario) {
    Connection con = null;
    PreparedStatement statement = null;
    try {
        con = ConexionDB.getInstance().getConnection();
        statement = con.prepareStatement(
                "INSERT INTO usuarios (nombre_usuario, email, contrasena, rol) VALUES (?, ?, ?, ?)"
        );
        statement.setString(1, usuario.getNombre());
        statement.setString(2, usuario.getEmail());
        statement.setString(3, usuario.getContrasena());
        statement.setString(4, usuario.getRol().name());

        int filas = statement.executeUpdate();
        if (filas > 0) {
            JOptionPane.showMessageDialog(null, "✅ Usuario agregado correctamente");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "❌ Error al agregar usuario: " + e.getMessage());
    } finally {
        try {
            if (statement != null) statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Buscar por ID - ✅ CORREGIDO: NO CERRAR CONEXIÓN EN FINALLY
public static UsuarioBase buscarPorID(int idUsuario) {
    UsuarioBase usuario = null;
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        con = ConexionDB.getInstance().getConnection();
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        stmt = con.prepareStatement(sql);
        stmt.setInt(1, idUsuario);
        rs = stmt.executeQuery();

        if (rs.next()) {
            String nombre = rs.getString("nombre_usuario");
            String email = rs.getString("email");
            String contrasena = rs.getString("contrasena");
            String rolStr = rs.getString("rol");
            Rol rol = Rol.valueOf(rolStr.toUpperCase());
            usuario = new UsuarioBase(idUsuario, nombre, email, contrasena, rol);
        }

    } catch (Exception e) {
        System.out.println("Error al buscar usuario por ID: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return usuario;
}

// Eliminar por ID - ✅ CORREGIDO: NO CERRAR CONEXIÓN SINGLETON
public static UsuarioBase eliminarPorID(int id) {
    UsuarioBase usuario = buscarPorID(id);

    if (usuario == null) {
        JOptionPane.showMessageDialog(null, "❌ No se encontró ningún usuario con ID: " + id);
        return null;
    }

    Connection con = null;
    PreparedStatement stmt = null;

    try {
        con = ConexionDB.getInstance().getConnection();
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);

        int filas = stmt.executeUpdate();
        if (filas > 0) {
            JOptionPane.showMessageDialog(null, "✅ Usuario eliminado correctamente.");
            return usuario;
        } else {
            JOptionPane.showMessageDialog(null, "❌ No se pudo eliminar el usuario.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Error al eliminar usuario: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (stmt != null) stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return null;
}

// Mostrar todos los usuarios - ✅ CORREGIDO: NO CERRAR CONEXIÓN SINGLETON
public static void mostrarTodosUsuarios() {
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        con = ConexionDB.getInstance().getConnection();
        String sql = "SELECT * FROM usuarios";
        stmt = con.prepareStatement(sql);
        rs = stmt.executeQuery();

        StringBuilder sb = new StringBuilder();
        sb.append("Lista de usuarios:\n\n");

        while (rs.next()) {
            int id = rs.getInt("id_usuario");
            String nombre = rs.getString("nombre_usuario");
            String email = rs.getString("email");
            String rol = rs.getString("rol");

            sb.append("ID: ").append(id)
              .append(" | Nombre: ").append(nombre)
              .append(" | Email: ").append(email)
              .append(" | Rol: ").append(rol)
              .append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al mostrar usuarios\n"+ e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public static void editarUsuario(int id) {
    UsuarioBase usuario = buscarPorID(id);

    if (usuario == null) {
        JOptionPane.showMessageDialog(null, "❌ No se encontró ningún usuario con ID: " + id);
        return;
    }

    String[] opciones = {"Nombre", "Email", "Contraseña", "Rol", "Cancelar"};
    int opcion = JOptionPane.showOptionDialog(
            null,
            "¿Qué campo deseas editar para el usuario " + usuario.getNombre() + "?",
            "Editar usuario",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
    );

    if (opcion == 4 || opcion == JOptionPane.CLOSED_OPTION) {
        return;
    }

    String campo = "";
    String nuevoValor = "";

    switch (opcion) {
        case 0 -> { 
            campo = "nombre_usuario"; 
            nuevoValor = JOptionPane.showInputDialog("Nuevo nombre:", usuario.getNombre());
        }
        case 1 -> { 
            campo = "email"; 
            nuevoValor = JOptionPane.showInputDialog("Nuevo email:", usuario.getEmail());
        }
        case 2 -> { 
            campo = "contrasena"; 
            nuevoValor = JOptionPane.showInputDialog("Nueva contraseña:");
        }
        case 3 -> {
            campo = "rol";
            String[] roles = {"ADMIN", "VENDEDOR", "DEPOSITO"};
            nuevoValor = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecciona nuevo rol:",
                    "Editar rol",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    roles,
                    usuario.getRol().name()
            );
        }
    }

    // Validación básica para evitar valores nulos o vacíos
    if (nuevoValor == null || nuevoValor.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "❌ Valor inválido. No se realizaron cambios.");
        return;
    }

    Connection con = null;
    PreparedStatement stmt = null;

    try {
        con = ConexionDB.getInstance().getConnection();

        // 🔧 Corrección: no concatenamos el campo directamente, pero mantenemos compatibilidad
        String sql = switch (campo) {
            case "nombre_usuario" -> "UPDATE usuarios SET nombre_usuario = ? WHERE id_usuario = ?";
            case "email" -> "UPDATE usuarios SET email = ? WHERE id_usuario = ?";
            case "contrasena" -> "UPDATE usuarios SET contrasena = ? WHERE id_usuario = ?";
            case "rol" -> "UPDATE usuarios SET rol = ? WHERE id_usuario = ?";
            default -> throw new IllegalArgumentException("Campo no válido: " + campo);
        };

        stmt = con.prepareStatement(sql);
        stmt.setString(1, nuevoValor);
        stmt.setInt(2, id);

        int filas = stmt.executeUpdate();

        if (filas > 0) {
            JOptionPane.showMessageDialog(null, "✅ Usuario actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(null, "❌ No se pudo actualizar el usuario.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "❌ Error al editar usuario: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (stmt != null) stmt.close(); // 🔹 Solo cerramos el statement, no la conexión
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

}



