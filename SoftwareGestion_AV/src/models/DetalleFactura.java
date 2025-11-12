package models;

public class DetalleFactura {
    private int id_detalle;
    private int id_factura;
    private int id_producto;
    private int cantidad;
    private Double precio_compra; // nullable
    private Double precio_venta;  // nullable
    private double subtotal;      // cantidad * precio_venta

    public DetalleFactura(int id_detalle, int id_factura, int id_producto, int cantidad,
                          Double precio_compra, Double precio_venta, double subtotal) {
        this.id_detalle = id_detalle;
        this.id_factura = id_factura;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
        this.precio_compra = precio_compra;
        this.precio_venta = precio_venta;
        this.subtotal = subtotal;
    }
    
}
