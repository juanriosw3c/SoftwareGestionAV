package models;

import Enums.TipoFactura; // si lo usás; si no, podés manejar el String 'Venta'

import java.time.LocalDateTime;

public class Factura {
    private int id_factura;
    private LocalDateTime fecha; // si en DB usás DATE, guardá LocalDate.now() y casteás
    private String tipo;         // "Venta" | "Compra" (acá usaremos "Venta")
    private int id_usuario;

    public Factura(int id_factura, LocalDateTime fecha, String tipo, int id_usuario) {
        this.id_factura = id_factura;
        this.fecha = fecha;
        this.tipo = tipo;
        this.id_usuario = id_usuario;
    }

    public int getId_factura() {
        return id_factura;
    }

    public void setId_factura(int id_factura) {
        this.id_factura = id_factura;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    
     }