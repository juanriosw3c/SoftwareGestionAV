package views;

import javax.swing.*;
import models.UsuarioBase;
import Enums.Rol;

public class UsuarioInputDialog {

    public static UsuarioBase crearUsuario() {

        String nombre = JOptionPane.showInputDialog("Nombre del usuario:");
        if (nombre == null) return null;

        String email = JOptionPane.showInputDialog("Email del usuario:");
        if (email == null) return null;

        String pass = JOptionPane.showInputDialog("Contraseña:");
        if (pass == null) return null;

        Rol rol = (Rol) JOptionPane.showInputDialog(
                null,
                "Seleccione rol:",
                "Rol",
                JOptionPane.QUESTION_MESSAGE,
                null,
                Rol.values(),
                Rol.ADMIN
        );

        if (rol == null) return null;

        return new UsuarioBase(0, nombre, email, pass, rol);
    }
}
