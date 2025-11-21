package views;

import javax.swing.*;
import java.awt.*;
import models.UsuarioBase;
import DLL.ControllerMovimientoStock;
import DLL.ControllerProducto;
import Enums.TipoMovimiento;
import helpers.UIHelper;

public class MovimientosView extends JFrame {

    private UsuarioBase usuario;
    private JTextArea txtArea;

    public MovimientosView(UsuarioBase usuario) {
        this.usuario = usuario;

        setTitle("Movimientos de Stock - " + usuario.getNombre());
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel main = UIHelper.crearPanelPrincipal();
        setContentPane(main);

        // === TOP ===
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UIHelper.GRAY_BG);

        JButton btnBack = UIHelper.crearBotonVolver(() -> {
            dispose();
            new MenuAdminView(usuario).setVisible(true);
        });

        JLabel titulo = UIHelper.crearTitulo("Movimientos de Stock");

        top.add(btnBack, BorderLayout.WEST);
        top.add(titulo, BorderLayout.CENTER);

        main.add(top, BorderLayout.NORTH);

        // === CENTRO - LISTA ===
        txtArea = new JTextArea();
        txtArea.setEditable(false);
        txtArea.setFont(UIHelper.FONT_LIST);

        JScrollPane scroll = new JScrollPane(txtArea);
        main.add(scroll, BorderLayout.CENTER);

        actualizarLista();

        // === BOTONES ===
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottom.setBackground(UIHelper.GRAY_BG);

        JButton btnEntrada = UIHelper.crearBoton("Entrada", UIHelper.GREEN);
        JButton btnSalida = UIHelper.crearBoton("Venta / Salida", UIHelper.RED);
        JButton btnAjuste = UIHelper.crearBoton("Ajuste", UIHelper.ORANGE);
        JButton btnRefresh = UIHelper.crearBoton("Actualizar", UIHelper.BLUE);

        bottom.add(btnEntrada);
        bottom.add(btnSalida);
        bottom.add(btnAjuste);
        bottom.add(btnRefresh);

        main.add(bottom, BorderLayout.SOUTH);

        // ============================
        // EVENTOS
        // ============================

        btnEntrada.addActionListener(e -> realizarMovimiento(TipoMovimiento.ENTRADA));
        btnSalida.addActionListener(e -> realizarMovimiento(TipoMovimiento.SALIDA));
        btnAjuste.addActionListener(e -> realizarMovimiento(TipoMovimiento.AJUSTE));
        btnRefresh.addActionListener(e -> actualizarLista());
    }

    // ------------------------------
    // FUNCIONES DE MOVIMIENTO
    // ------------------------------
    private void realizarMovimiento(TipoMovimiento tipo) {
        try {
            String idStr = JOptionPane.showInputDialog("ID del producto:");
            if (idStr == null) return;

            int id = Integer.parseInt(idStr);

            String cantStr = JOptionPane.showInputDialog("Cantidad:");
            if (cantStr == null) return;

            int cant = Integer.parseInt(cantStr);

            ControllerProducto.aplicarMovimiento(id, cant, tipo, usuario.getIdUsuario());

            actualizarLista();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
        }
    }

    // ------------------------------
    // LISTAR MOVIMIENTOS
    // ------------------------------
    private void actualizarLista() {
        txtArea.setText("");

        try {
            var con = DLL.ConexionDB.getInstance().getConnection();
            var st = con.prepareStatement(
                    "SELECT * FROM movimiento_stock ORDER BY fecha DESC"
            );
            var rs = st.executeQuery();

            txtArea.append("Historial de Movimientos:\n\n");

            while (rs.next()) {
                txtArea.append(
                        "ID: " + rs.getInt("id_movimiento") +
                        " | Tipo: " + rs.getString("tipo") +
                        " | Fecha: " + rs.getTimestamp("fecha") +
                        " | Cant: " + rs.getInt("cantidad") +
                        " | Prod: " + rs.getInt("id_producto") +
                        "\n"
                );
            }

        } catch (Exception e) {
            txtArea.append("Error al cargar movimientos.");
        }
    }
}
