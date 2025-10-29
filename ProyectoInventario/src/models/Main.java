package models;

import DLL.ControllerUsuario;
import Enums.Rol;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {

        JOptionPane.showMessageDialog(null, "Bienvenido al sistema de gestión");

        String mail = JOptionPane.showInputDialog("Ingrese su correo:");
        String contrasena = JOptionPane.showInputDialog("Ingrese su contraseña:");

        // Intentar login
        UsuarioBase usuario = ControllerUsuario.login(mail, contrasena);

        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "Credenciales incorrectas o usuario no encontrado.");
            return;
        }

        // Mostrar mensaje de bienvenida
        Rol rol = usuario.getRol();
        JOptionPane.showMessageDialog(null, "Bienvenido " + usuario.getNombre() + " (" + rol + ")");

        // Según el rol, mostrar el menú correspondiente
        switch (rol) {
            case ADMIN -> {
                UsuarioAdmin admin = (UsuarioAdmin) usuario;
                admin.mostrarMenuPrincipal();
            }
            case VENDEDOR -> {
                UsuarioVendedor vendedor = (UsuarioVendedor) usuario;
                vendedor.mostrarMenuPrincipal();
            }
            case DEPOSITO -> {
                UsuarioDeposito deposito = (UsuarioDeposito) usuario;
                deposito.mostrarMenuPrincipal();
            }
            default -> JOptionPane.showMessageDialog(null, "Rol no reconocido. Contacte al administrador.");
        }

        JOptionPane.showMessageDialog(null, "Sesión finalizada. Gracias por usar el sistema.");
    }
}
