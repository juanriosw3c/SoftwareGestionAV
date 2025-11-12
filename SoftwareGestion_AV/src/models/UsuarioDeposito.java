package models;

import Enums.Rol;
import Interfaces.Deposito;
import javax.swing.JOptionPane;

public class UsuarioDeposito extends UsuarioBase implements Deposito {

    public UsuarioDeposito(int id, String nombre, String email, String pass) {
        super(id, nombre, email, pass, Rol.DEPOSITO);
    }

    @Override
    public void mostrarMenuPrincipal() {
        String[] opciones = {
            "Ingresar mercadería",        // ENTRADA
            "Restar (pedido interno)",    // AJUSTE
            "Ver productos",              // SELECT * FROM producto
            "Salir"
        };
        int opcion;
        do {
            opcion = JOptionPane.showOptionDialog(
                null,
                "Menú Depósito",
                "Depósito",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );

            switch (opcion) {
                case 0 -> ingresarMercaderia();       // ENTRADA
                case 1 -> restarPorPedidoInterno();   // AJUSTE
                case 2 -> verProductos();             // Listado
                default -> { /* salir o ventana cerrada */ }
            }
        } while (opcion != 3 && opcion != JOptionPane.CLOSED_OPTION);
    }
}
