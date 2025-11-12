package models;

import Enums.Rol;
import Interfaces.Vendedor;
import javax.swing.JOptionPane;

public class UsuarioVendedor extends UsuarioBase implements Vendedor {

    public UsuarioVendedor(int id, String nombre, String email, String pass) {
        super(id, nombre, email, pass, Rol.VENDEDOR);
    }

    @Override
    public void mostrarMenuPrincipal() {
        String[] opciones = {"Realizar venta", "Consultar stock", "Salir"};
        int opcion;

        do {
            opcion = JOptionPane.showOptionDialog(
                    null,
                    "Menú Vendedor",
                    "Gestión de Ventas",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            switch (opcion) {
                case 0 -> realizarVenta();
                case 1 -> consultarStock();
            }

        } while (opcion != 2);
    }
}
