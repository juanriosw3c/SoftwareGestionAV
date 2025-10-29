package Enums;

public enum TipoFactura {
    COMPRA, VENTA;

    public String toDbValue() {
        return switch (this) {
            case COMPRA -> "Compra";
            case VENTA  -> "Venta";
        };
    }

    public static TipoFactura fromDbValue(String dbValue) {
        return switch (dbValue) {
            case "Compra" -> COMPRA;
            case "Venta"  -> VENTA;
            default -> throw new IllegalArgumentException("TipoFactura inválido: " + dbValue);
        };
    }
}
