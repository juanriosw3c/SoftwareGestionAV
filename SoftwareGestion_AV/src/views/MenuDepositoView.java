package views;

import javax.swing.*;
import java.awt.*;
import helpers.UIHelper;
import models.UsuarioBase;

public class MenuDepositoView extends JFrame {

    public MenuDepositoView(UsuarioBase usuario) {

        setTitle("Panel de Depósito - " + usuario.getNombre());
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIHelper.GRAY_BG);
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(main);

        JLabel titulo = UIHelper.crearTitulo("Panel de Depósito");
        main.add(titulo, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 2, 20, 20));
        grid.setOpaque(false);

        // ==== BOTONES ====
        JButton btnUsuarios = UIHelper.crearBotonMenu("Gestionar Usuarios", "/img/clientes.png", UIHelper.GREEN);
        JButton btnProductos = UIHelper.crearBotonMenu("Gestionar Productos", "/img/agregar-producto.png", UIHelper.BLUE);
        JButton btnProveedores = UIHelper.crearBotonMenu("Gestionar Proveedores", "/img/gestion.png", UIHelper.RED);
        JButton btnMov = UIHelper.crearBotonMenu("Movimientos de Stock", "/img/factura.png", UIHelper.ORANGE);
        JButton btnReportes = UIHelper.crearBotonMenu("Reportes", "/img/ventas.png", UIHelper.BROWN);
        JButton btnSalir = UIHelper.crearBotonMenu("Cerrar Sesión", "/img/apagar.png", UIHelper.TEAL);

        // ==== RESTRICCIONES DEL DEPÓSITO ====

        btnUsuarios.setEnabled(false);
        btnProveedores.setEnabled(false);
        btnReportes.setEnabled(false);

        btnUsuarios.setToolTipText("Solo administradores");
        btnProveedores.setToolTipText("Solo administradores");
        btnReportes.setToolTipText("Solo administradores");

        // Acciones válidas
        btnProductos.addActionListener(e -> {
            dispose();
            new ProductosView(usuario).setVisible(true);
        });

        btnMov.addActionListener(e -> {
            dispose();
            new MovimientosView(usuario).setVisible(true);
        });

        btnSalir.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });

        // ==== Agregar botones ====
        grid.add(btnUsuarios);
        grid.add(btnProveedores);
        grid.add(btnProductos);
        grid.add(btnMov);
        grid.add(btnReportes);
        grid.add(btnSalir);

        main.add(grid, BorderLayout.CENTER);
    }
}

