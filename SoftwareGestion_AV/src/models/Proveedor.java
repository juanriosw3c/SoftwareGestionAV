package models;

public class Proveedor {
    private int id_proveedor;
    private String nombre;
    private String contacto;
    private String telefono;
    private String email;

    public Proveedor(int id_proveedor, String nombre, String contacto, String telefono, String email) {
        this.id_proveedor = id_proveedor;
        this.nombre = nombre;
        this.contacto = contacto;
        this.telefono = telefono;
        this.email = email;
    }

    


    public int getId_proveedor() {
        return id_proveedor;
    }




    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }




    public String getNombre() {
        return nombre;
    }




    public void setNombre(String nombre) {
        this.nombre = nombre;
    }




    public String getContacto() {
        return contacto;
    }




    public void setContacto(String contacto) {
        this.contacto = contacto;
    }




    public String getTelefono() {
        return telefono;
    }




    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }




    public String getEmail() {
        return email;
    }




    public void setEmail(String email) {
        this.email = email;
    }




    @Override
    public String toString() {
        return "Proveedor id=" + id_proveedor  + '\n' +
               "Nombre= " + nombre + '\n' +
               "Contacto= " + contacto + '\n' +
               "Teléfono= " + telefono + '\n' +
               "Email='" + email + '\n'
               ;
    }
}
