package models;

public class Producto {
    protected int id_producto;
    protected String nombre;
    protected String descripcion;
    protected double precio;
    protected int stock_actual;
    protected int stock_minimo;

    public Producto(int id_producto, String descripcion, String nombre, double precio, int stock_actual, int stock_minimo) {
        this.descripcion = descripcion;
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock_actual = stock_actual;
        this.stock_minimo = stock_minimo;
    }

     public Producto(String descripcion, String nombre, double precio, int stock_actual, int stock_minimo) {
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.precio = precio;
        this.stock_actual = stock_actual;
        this.stock_minimo = stock_minimo;
    }

     public int getId_producto() {
         return id_producto;
     }

     public void setId_producto(int id_producto) {
         this.id_producto = id_producto;
     }

     public String getNombre() {
         return nombre;
     }

     public void setNombre(String nombre) {
         this.nombre = nombre;
     }

     public String getDescripcion() {
         return descripcion;
     }

     public void setDescripcion(String descripcion) {
         this.descripcion = descripcion;
     }

     public double getPrecio() {
         return precio;
     }

     public void setPrecio(double precio) {
         this.precio = precio;
     }

     public int getStock_actual() {
         return stock_actual;
     }

     public void setStock_actual(int stock_actual) {
         this.stock_actual = stock_actual;
     }

     public int getStock_minimo() {
         return stock_minimo;
     }

     public void setStock_minimo(int stock_minimo) {
         this.stock_minimo = stock_minimo;
     }

     @Override
     public String toString() {
        return "Productos \n Id producto: " + id_producto + "\nNombre:" + nombre + "\nDescripcion: " + descripcion
                + "\nPrecio:" + precio + "\nStock actual: " + stock_actual + "\nStock mínimo: " + stock_minimo;
     }

   

    
}


