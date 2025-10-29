package Enums;

public enum TipoMovimiento {
    ENTRADA,   // Reposición / Entrada de mercadería
    SALIDA,    // Venta
    AJUSTE;    // Pedido interno / Merma / Ajuste

    public String toDbValue() {
        return switch (this) {
            case ENTRADA -> "Entrada";
            case SALIDA  -> "Salida";
            case AJUSTE  -> "Ajuste";
        };
    }

    public static TipoMovimiento fromDbValue(String dbValue) {
        return switch (dbValue) {
            case "Entrada" -> ENTRADA;
            case "Salida"  -> SALIDA;
            case "Ajuste"  -> AJUSTE;
            default -> throw new IllegalArgumentException("TipoMovimiento inválido: " + dbValue);
        };
    }
}
