package Interfaces;

import DLL.ControllerProducto;
import Enums.TipoMovimiento;
import javax.swing.JOptionPane;
import models.UsuarioBase;

public interface Deposito {

    // ENTRADA: ingresar mercadería al stock
    default void ingresarMercaderia() {
        try {
            int idProd = Integer.parseInt(JOptionPane.showInputDialog("ID del producto a ingresar:"));
            int cant   = Integer.parseInt(JOptionPane.showInputDialog("Cantidad a ingresar:"));
            int idUser = ((UsuarioBase) this).getIdUsuario(); // FK a usuarios
            ControllerProducto.aplicarMovimiento(idProd, cant, TipoMovimiento.ENTRADA, idUser);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al ingresar mercadería: " + e.getMessage());
        }
    }

    // AJUSTE: restar mercadería por pedido interno / merma / fallado
    default void restarPorPedidoInterno() {
        try {
            int idProd = Integer.parseInt(JOptionPane.showInputDialog("ID del producto a ajustar:"));
            int cant   = Integer.parseInt(JOptionPane.showInputDialog("Cantidad a descontar (pedido interno/merma):"));
            int idUser = ((UsuarioBase) this).getIdUsuario();
            ControllerProducto.aplicarMovimiento(idProd, cant, TipoMovimiento.AJUSTE, idUser);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al realizar el ajuste: " + e.getMessage());
        }
    }

    // Ver catálogo/stock
    default void verProductos() {
        ControllerProducto.mostrarTodosProductos();
    }
}
