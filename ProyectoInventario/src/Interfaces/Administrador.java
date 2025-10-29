package Interfaces;

import DLL.ControllerMovimientoStock;
import DLL.ControllerProducto;
import DLL.ControllerProveedor;
import DLL.ControllerReportes;
import DLL.ControllerUsuario;
import Enums.Rol;
import javax.swing.JOptionPane;
import models.Producto;
import models.Proveedor;
import models.UsuarioBase;

public interface Administrador {

    // ----------- GESTIÓN DE USUARIOS -----------
    default void gestionarUsuarios() {
        String[] opciones = {"Listar usuarios", "Buscar por ID", "Crear nuevo", "Editar", "Eliminar", "Volver"};
        int opcion;

        do {
            opcion = JOptionPane.showOptionDialog(
                    null,
                    "Gestión de Usuarios",
                    "Administrador",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            switch (opcion) {
                case 0 -> ControllerUsuario.mostrarTodosUsuarios();
                case 1 -> {
                    try {
                        int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del usuario:"));
                        UsuarioBase u = ControllerUsuario.buscarPorID(id);
                        if (u != null)
                            JOptionPane.showMessageDialog(null, u.toString());
                        else
                            JOptionPane.showMessageDialog(null, "Usuario no encontrado");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "ID inválido");
                    }
                }
                case 2 -> {
                    String nombre = JOptionPane.showInputDialog("Nombre:");
                    String email = JOptionPane.showInputDialog("Email:");
                    String pass = JOptionPane.showInputDialog("Contraseña:");
                    String[] roles = {"ADMIN", "VENDEDOR", "DEPOSITO"};
                    String rolStr = (String) JOptionPane.showInputDialog(
                            null, "Selecciona rol:", "Rol",
                            JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]
                    );

                    if (nombre != null && email != null && pass != null && rolStr != null) {
                        try {
                            UsuarioBase nuevo = new UsuarioBase(0, nombre, email, pass, Rol.valueOf(rolStr));
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

    // ----------- GESTIÓN DE PRODUCTOS -----------
    default void gestionarProductos() {
        String[] opciones = {"Listar productos", "Buscar por ID", "Agregar nuevo", "Editar", "Eliminar", "Volver"};
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

    // ----------- GESTIÓN DE PROVEEDORES -----------
    // ----------- GESTIÓN DE PROVEEDORES -----------
default void gestionarProveedores() {
    String[] opciones = {"Listar", "Agregar nuevo", "Editar", "Eliminar", "Volver"};
    int opcion;

    do {
        opcion = JOptionPane.showOptionDialog(
                null, "Gestión de Proveedores", "Administrador - Proveedores",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, opciones, opciones[0]
        );

        switch (opcion) {
            case 0 -> ControllerProveedor.listarProveedores();
            case 1 -> {
                // Crear nuevo proveedor con inputs
                String nombre = JOptionPane.showInputDialog("Nombre del proveedor:");
                String contacto = JOptionPane.showInputDialog("Nombre del contacto:");
                String telefono = JOptionPane.showInputDialog("Teléfono:");
                String email = JOptionPane.showInputDialog("Email:");

                if (nombre != null && contacto != null && telefono != null && email != null) {
                    Proveedor nuevo = new Proveedor(0, nombre, contacto, telefono, email);
                    ControllerProveedor.agregarProveedor(nuevo);
                } else {
                    JOptionPane.showMessageDialog(null, "Operación cancelada o datos incompletos.");
                }
            }
            case 2 -> {
                try {
                    int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del proveedor a editar:"));
                    ControllerProveedor.editarProveedor(id);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "ID inválido");
                }
            }
            case 3 -> {
                try {
                    int id = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el ID del proveedor a eliminar:"));
                    ControllerProveedor.eliminarPorID(id);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "ID inválido");
                }
            }
        }
    } while (opcion != 4 && opcion != JOptionPane.CLOSED_OPTION);
}


    // ----------- MOVIMIENTOS -----------
    default void verMovimientos() {
        ControllerMovimientoStock.listarMovimientos();
    }

    // ----------- REPORTES -----------
// ----------- REPORTES -----------
default void generarReportes() {
    try {
        ControllerReportes.reporteVentasPorVendedor();
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(null, "No se pudo generar el reporte: " + e.getMessage());
    }
}


    void mostrarMenuPrincipal();
}
