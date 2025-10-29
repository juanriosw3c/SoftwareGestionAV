package Interfaces;


import DLL.ControllerProducto;
import DLL.ControllerUsuario;
import javax.swing.JOptionPane;
import models.Producto;
import models.UsuarioBase;


public interface Administrador {


// Usamos métodos default para que la implementación quede disponible
// y las clases que implementen la interface puedan sobreescribir si lo desean.


default void gestionarUsuarios() {
String[] opciones = {"Listar usuarios", "Buscar por ID", "Crear nuevo", "Editar", "Eliminar", "Volver"};
int opcion;


do {
opcion = JOptionPane.showOptionDialog(null, "Gestión de Usuarios", "Administrador",
JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
null, opciones, opciones[0]);


switch (opcion) {
case 0 -> ControllerUsuario.mostrarTodosUsuarios();
case 1 -> {
try {
int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del usuario:"));
UsuarioBase u = ControllerUsuario.buscarPorID(id);
if (u != null) JOptionPane.showMessageDialog(null, u.toString());
else JOptionPane.showMessageDialog(null, "Usuario no encontrado");
} catch (NumberFormatException e) {
JOptionPane.showMessageDialog(null, "ID inválido");
}
}
case 2 -> {
// Pedimos datos para crear un nuevo usuario
String nombre = JOptionPane.showInputDialog("Nombre:");
String email = JOptionPane.showInputDialog("Email:");
String pass = JOptionPane.showInputDialog("Contraseña:");
String[] roles = {"ADMIN", "VENDEDOR", "DEPOSITO"};
String rolStr = (String) JOptionPane.showInputDialog(null, "Selecciona rol:", "Rol",
JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);


if (nombre != null && email != null && pass != null && rolStr != null) {
try {
UsuarioBase nuevo = new UsuarioBase(0, nombre, email, pass, Enums.Rol.valueOf(rolStr));
ControllerUsuario.agregarUsuario(nuevo);
} catch (Exception ex) {
JOptionPane.showMessageDialog(null, "Error al crear usuario: " + ex.getMessage());
}
} else {
JOptionPane.showMessageDialog(null, "Operación cancelada o datos incompletos.");
}
}
case 3 -> {
try {
int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del usuario a editar:"));
ControllerUsuario.editarUsuario(id);
} catch (NumberFormatException e) {
JOptionPane.showMessageDialog(null, "ID inválido");
}
}
case 4 -> {
try {
int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del usuario a eliminar:"));
ControllerUsuario.eliminarPorID(id);
} catch (NumberFormatException e) {
JOptionPane.showMessageDialog(null, "ID inválido");
}
}
}


} while (opcion != 5 && opcion != JOptionPane.CLOSED_OPTION);
}


// Otros métodos del administrador quedan como default (pueden llamarse igual desde aquí y
// delegar a sus respectivo controllers cuando estén implementados)
 default void gestionarProductos() {
        String[] opciones = {
            "Listar productos",
            "Buscar por ID",
            "Agregar nuevo",
            "Editar",
            "Eliminar",
            "Volver"
        };
        int opcion;

        do {
            opcion = JOptionPane.showOptionDialog(
                    null,
                    "Gestión de Productos",
                    "Administrador - Productos",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            switch (opcion) {
                case 0 -> ControllerProducto.mostrarTodosProductos();

                case 1 -> {
                    try {
                        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del producto:"));
                        Producto p = ControllerProducto.buscarPorID(id);
                        if (p != null)
                            JOptionPane.showMessageDialog(null, p.toString());
                        else
                            JOptionPane.showMessageDialog(null, "Producto no encontrado");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "ID inválido");
                    }
                }

                case 2 -> {
                    try {
                        String nombre = JOptionPane.showInputDialog("Nombre:");
                        String descripcion = JOptionPane.showInputDialog("Descripción:");
                        double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));
                        int stockActual = Integer.parseInt(JOptionPane.showInputDialog("Stock actual:"));
                        int stockMinimo = Integer.parseInt(JOptionPane.showInputDialog("Stock mínimo:"));

                        Producto nuevo = new Producto(0, nombre, descripcion, precio, stockActual, stockMinimo);
                        ControllerProducto.agregarProducto(nuevo);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Error al agregar producto: " + e.getMessage());
                    }
                }

                case 3 -> {
                    try {
                        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del producto a editar:"));
                        ControllerProducto.editarProducto(id);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "ID inválido");
                    }
                }

                case 4 -> {
                    try {
                        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del producto a eliminar:"));
                        ControllerProducto.eliminarPorID(id);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "ID inválido");
                    }
                }
            }

        } while (opcion != 5 && opcion != JOptionPane.CLOSED_OPTION);
    }


default void gestionarProveedores() {
JOptionPane.showMessageDialog(null, "Gestión de proveedores (pendiente)");
}


default void verMovimientos() {
JOptionPane.showMessageDialog(null, "Visualización de movimientos (pendiente)");
}


default void generarReportes() {
JOptionPane.showMessageDialog(null, "Generación de reportes (pendiente)");
}


void mostrarMenuPrincipal();
}