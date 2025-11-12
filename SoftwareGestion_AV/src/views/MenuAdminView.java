package views;

import javax.swing.*;
import java.awt.*;
import models.UsuarioBase;
import DLL.ControllerUsuario;
import DLL.ControllerProducto;
import DLL.ControllerProveedor;
import DLL.ControllerMovimientoStock;
import DLL.ControllerReportes;

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

        // === Panel principal ===
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // === Título ===
        JLabel lblTitulo = new JLabel("Panel del Administrador", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        // === Panel de botones (2 columnas, 3 filas) ===
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        gridPanel.setOpaque(false);

        // === Botones coloridos ===
        JButton btnUsuarios = crearBoton("Gestionar Usuarios", "img/icons/users.png", new Color(46, 204, 113));
        JButton btnProductos = crearBoton("Gestionar Productos", "img/icons/products.png", new Color(52, 152, 219));
        JButton btnProveedores = crearBoton("Gestionar Proveedores", "img/icons/providers.png", new Color(231, 76, 60));
        JButton btnMovimientos = crearBoton("Movimientos de Stock", "img/icons/stock.png", new Color(243, 156, 18));
        JButton btnReportes = crearBoton("Reportes", "img/icons/reports.png", new Color(111, 66, 33));
        JButton btnSalir = crearBoton("Cerrar Sesión", "img/icons/logout.png", new Color(26, 188, 156));
        
        // === Acciones reales (controladores) ===
        btnUsuarios.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abriendo gestión de usuarios...");
            controllerUsuario.listarUsuarios(); // ejemplo, podés reemplazar por tu método real
        });

        btnProductos.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abriendo gestión de productos...");
            controllerProducto.listarProductos();
        });

        btnProveedores.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abriendo gestión de proveedores...");
            controllerProveedor.listarProveedores();
        });

        btnMovimientos.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abriendo movimientos de stock...");
            controllerMovimientoStock.listarMovimientos();
        });

        btnReportes.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Generando reportes...");
            controllerReportes.generarReporte();
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

        // === Panel inferior (vacío o futuro pie de página) ===
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 245));

        // === Estructura ===
        mainPanel.add(lblTitulo, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // === Mostrar mensaje flotante ===
        SwingUtilities.invokeLater(() -> mostrarMensajeBienvenida("Bienvenido, " + usuario.getNombre() + " 👋"));
    }

    // === Crear botón personalizado ===
    private JButton crearBoton(String texto, String iconPath, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setBackground(colorFondo);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setIconTextGap(15);
        boton.setMargin(new Insets(10, 20, 10, 20));

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo);
            }
        });

        return boton;
    }

    // === Mensaje tipo “toast” ===
    private void mostrarMensajeBienvenida(String texto) {
        JWindow toast = new JWindow();
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(0, 0, 0, 180));
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        toast.add(lbl);
        toast.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - toast.getWidth()) / 2;
        int y = (screenSize.height - toast.getHeight()) / 2;
        toast.setLocation(x, y);
        toast.setVisible(true);

        new Timer(2500, e -> toast.dispose()).start();
    }
}

