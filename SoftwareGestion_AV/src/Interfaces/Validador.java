package Interfaces;

public interface  Validador {
    
     default  boolean validarEmail(String email) {
        if (email == null) return false;
        // patrón robusto: usuario@dominio.extension
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    //  Valida contraseña con reglas fuertes
    default boolean validarContrasena(String contrasena) {
        if (contrasena == null) return false;
        // al menos 8 caracteres, 1 mayúscula, 1 minúscula, 1 número
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return contrasena.matches(regex);
    }

    default boolean validarNumero(String valor) {
        if (valor == null) return false;
        try {
            Integer.parseInt(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    default boolean validarTexto(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
}
