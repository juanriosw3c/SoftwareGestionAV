package models;


import Enums.TipoMovimiento;
import java.time.LocalDateTime;

public class MovimientoStock {
    private int id_movimiento;
    private TipoMovimiento tipo;   // ENTRADA | SALIDA | AJUSTE
    private LocalDateTime fecha;
    private int cantidad;
    private int id_producto;
    private Integer id_usuario;    // puede venir null si se quisiera (FK)

    public MovimientoStock(int id_movimiento, TipoMovimiento tipo, LocalDateTime fecha,
                           int cantidad, int id_producto, Integer id_usuario) {
        this.id_movimiento = id_movimiento;
        this.tipo = tipo;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.id_producto = id_producto;
        this.id_usuario = id_usuario;
    }

    public int getId_movimiento() {
        return id_movimiento;
    }

    public void setId_movimiento(int id_movimiento) {
        this.id_movimiento = id_movimiento;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    }

    