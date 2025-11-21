package views;

import javax.swing.*;
import java.awt.*;
import models.UsuarioBase;
import DLL.ControllerReportes;
import helpers.UIHelper;

public class ReportesView extends JFrame {

    private UsuarioBase usuario;

    public ReportesView(UsuarioBase usuario) {
        this.usuario = usuario;

        setTitle("Reportes - " + usuario.getNombre());
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel main = UIHelper.crearPanelPrincipal();
        setContentPane(main);

        // TOP
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UIHelper.GRAY_BG);

        JButton btnBack = UIHelper.crearBotonVolver(() -> {
            dispose();
            new MenuAdminView(usuario).setVisible(true);
        });

        JLabel titulo = UIHelper.crearTitulo("Reportes del Sistema");

        top.add(btnBack, BorderLayout.WEST);
        top.add(titulo, BorderLayout.CENTER);

        main.add(top, BorderLayout.NORTH);

        // BOTONES
        JPanel actions = new JPanel(new GridLayout(2, 1, 20, 20));
        actions.setBackground(UIHelper.GRAY_BG);
        actions.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JButton btnVendedores = UIHelper.crearBoton("Ventas por Vendedor", UIHelper.BLUE);
        JButton btnVendedoresFechas = UIHelper.crearBoton("Ventas por Fecha", UIHelper.TEAL);

        actions.add(btnVendedores);
        actions.add(btnVendedoresFechas);

        main.add(actions, BorderLayout.CENTER);

        // EVENTOS
        btnVendedores.addActionListener(e -> ControllerReportes.reporteVentasPorVendedor());

        btnVendedoresFechas.addActionListener(e -> {
            String desde = JOptionPane.showInputDialog("Fecha desde (YYYY-MM-DD):");
            if (desde == null) return;

            String hasta = JOptionPane.showInputDialog("Fecha hasta (YYYY-MM-DD):");
            if (hasta == null) return;

            ControllerReportes.reporteVentasPorVendedor(desde, hasta);
        });
    }
}

