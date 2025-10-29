package models;

import Enums.Rol;
import Interfaces.Usuario;
import javax.swing.JOptionPane;

public class  UsuarioBase implements Usuario {
    protected int idUsuario;
    protected String nombre;
    protected String email;
    protected String contrasena;
    protected Rol rol;

    public UsuarioBase(int idUsuario, String nombre, String email, String contrasena, Rol rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    

    public int getIdUsuario() {
        return idUsuario;
    }



    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }



    public String getNombre() {
        return nombre;
    }



    public void setNombre(String nombre) {
        this.nombre = nombre;
    }



    public String getEmail() {
        return email;
    }



    public void setEmail(String email) {
        this.email = email;
    }



    public String getContrasena() {
        return contrasena;
    }



    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }



    public void setRol(Rol rol) {
        this.rol = rol;
    }



    public boolean autenticar(String usuario, String contrasena) {
        return this.email.equals(usuario) && this.contrasena.equals(contrasena);
    }

    public Rol getRol() {
        return rol;
    }

    public void mostrarMenuPrincipal() {
        JOptionPane.showMessageDialog(null, "Menú base (sin implementación)");
 
 
    }


    @Override
public String toString() {
    return "ID: " + idUsuario + "\nNombre: " + nombre + "\nEmail: " + email + "\nRol: " + rol;
}
}
