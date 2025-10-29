package Interfaces;

import Enums.Rol;

public interface Usuario {
    boolean autenticar(String usuario, String contrasena);
    Rol getRol();
    void mostrarMenuPrincipal();
}

