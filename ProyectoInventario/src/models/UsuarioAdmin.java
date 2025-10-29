package models;

import Enums.Rol;
import Interfaces.Administrador;
import javax.swing.JOptionPane;

public class UsuarioAdmin extends UsuarioBase implements Administrador {

    public UsuarioAdmin(int id, String nombre, String email, String pass) {
        super(id, nombre, email, pass, Rol.ADMIN);
    }

    @Override
    public void mostrarMenuPrincipal() {
        String[] opciones = {"Usuarios", "Productos", "Proveedores", "Movimientos", "Reportes", "Salir"};
        int opcion;

        do {
            opcion = JOptionPane.showOptionDialog(
                    null,
                    "Menú del Administrador",
                    "Administrador",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            switch (opcion) {
                case 0 -> gestionarUsuarios();
                case 1 -> gestionarProductos();
                case 2 -> JOptionPane.showMessageDialog(null, "Gestión de proveedores (pendiente)");
                case 3 -> JOptionPane.showMessageDialog(null, "Gestión de movimientos (pendiente)");
                case 4 -> JOptionPane.showMessageDialog(null, "Generación de reportes (pendiente)");
            }

        } while (opcion != 5 && opcion != JOptionPane.CLOSED_OPTION);
    }
}
