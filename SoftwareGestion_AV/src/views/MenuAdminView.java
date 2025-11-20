package views;

import javax.swing.*;
import java.awt.*;
import models.UsuarioBase;
import DLL.ControllerUsuario;
import DLL.ControllerProducto;
import DLL.ControllerProveedor;
import DLL.ControllerMovimientoStock;
import DLL.ControllerReportes;
import helpers.UIHelper;

public class MenuAdminView extends JFrame {

    // Controladores
    private ControllerUsuario controllerUsuario;
    private ControllerProducto controllerProducto;
    private ControllerProveedor controllerProveedor;
    private ControllerMovimientoStock controllerMovimiento;
    private ControllerReportes controllerReportes;

    public MenuAdminView(UsuarioBase usuario) {

        // === Inicializar controladores ===
        controllerUsuario = new ControllerUsuario();
        controllerProducto = new ControllerProducto();
        controllerProveedor = new ControllerProveedor();
        controllerMovimiento = new ControllerMovimientoStock();
        controllerReportes = new ControllerReportes();

        // === Configuración principal ===
        setTitle("Software Gestión - " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        add(mainPanel);

        // === Título ===
        JLabel lblTitulo = new JLabel("Panel del Administrador", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        mainPanel.add(lblTitulo, BorderLayout.NORTH);

        // === Panel de botones ===
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        gridPanel.setOpaque(false);

        JButton btnUsuarios = UIHelper.crearBotonMenu(
                "Gestionar Usuarios",
                "/img/clientes.png",
                new Color(46, 204, 113)
        );

        JButton btnProductos = UIHelper.crearBotonMenu(
                "Gestionar Productos",
                "/img/agregar-producto.png",
                new Color(52, 152, 219)
        );

        JButton btnProveedores = UIHelper.crearBotonMenu(
                "Gestionar Proveedores",
                "/img/gestion.png",
                new Color(231, 76, 60)
        );

        JButton btnMovimientos = UIHelper.crearBotonMenu(
                "Movimientos de Stock",
                "/img/factura.png",
                new Color(243, 156, 18)
        );

        JButton btnReportes = UIHelper.crearBotonMenu(
                "Reportes",
                "/img/ventas.png",
                new Color(111, 66, 33)
        );

        JButton btnSalir = UIHelper.crearBotonMenu(
                "Cerrar Sesión",
                "/img/apagar.png",
                new Color(26, 188, 156)
        );

        // === Acciones ===
        btnUsuarios.addActionListener(e -> {
            dispose();
            new UsuariosView(usuario).setVisible(true);
        });

        btnProductos.addActionListener(e -> {
            dispose();
            new ProductosView(usuario).setVisible(true);
        });

        btnProveedores.addActionListener(e -> {
            dispose();
            new ProveedoresView(usuario).setVisible(true);
        });

        btnMovimientos.addActionListener(e -> {
            dispose();
            new MovimientosView(usuario).setVisible(true);
        });

        btnReportes.addActionListener(e -> {
            dispose();
            new ReportesView(usuario).setVisible(true);
        });

        btnSalir.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });

        // === Añadir botones al panel ===
        gridPanel.add(btnUsuarios);
        gridPanel.add(btnProveedores);
        gridPanel.add(btnProductos);
        gridPanel.add(btnMovimientos);
        gridPanel.add(btnReportes);
        gridPanel.add(btnSalir);

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        // === Mensaje bienvenida ===
        SwingUtilities.invokeLater(() -> UIHelper.mostrarToast("Bienvenido, " + usuario.getNombre() + " 👋"));
    }
}

