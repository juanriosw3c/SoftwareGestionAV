package Interfaces;

import DLL.ControllerFactura;
import DLL.ControllerProducto;
import javax.swing.*;
import models.UsuarioBase;

public interface Vendedor {

    default void realizarVenta() {
        try {
            int idUser = ((UsuarioBase) this).getIdUsuario();
            ControllerFactura.crearVentaInteractiva(idUser);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al iniciar la venta: " + e.getMessage());
        }
    }

    default void consultarStock() {
        ControllerProducto.mostrarTodosProductos();
    }
}
