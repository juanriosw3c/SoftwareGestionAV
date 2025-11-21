package views;

import javax.swing.*;
import java.awt.*;
import helpers.UIHelper;
import models.UsuarioBase;
import DLL.ControllerProveedor;

public class ProveedoresView extends JFrame {

    private JTextArea txtArea;
    private UsuarioBase usuario;

    public ProveedoresView(UsuarioBase usuario) {
        this.usuario = usuario;

        setTitle("Proveedores - " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // =============================
        // TOP PANEL (volver + título)
        // =============================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIHelper.GRAY_BG);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botón volver con icono
        JButton btnBack = UIHelper.crearBotonVolver(() -> {
            dispose();
            new MenuAdminView(usuario).setVisible(true);
        });

        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(UIHelper.crearTitulo("Gestión de Proveedores"), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // =============================
        // CENTRO → AREA DE TEXTO
        // =============================
        txtArea = new JTextArea();
        txtArea.setEditable(false);
        txtArea.setFont(UIHelper.FONT_LIST);

        JScrollPane scroll = new JScrollPane(txtArea);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        actualizarLista();

        // =============================
        // PANEL DE BOTONES INFERIOR
        // =============================
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(UIHelper.GRAY_BG);

        JButton btnAgregar = UIHelper.crearBoton("Agregar proveedor", UIHelper.GREEN);
        JButton btnEditar = UIHelper.crearBoton("Editar proveedor", UIHelper.BLUE);
        JButton btnEliminar = UIHelper.crearBoton("Eliminar proveedor", UIHelper.RED);
        JButton btnActualizar = UIHelper.crearBoton("Actualizar lista", UIHelper.TEAL);

        bottomPanel.add(btnAgregar);
        bottomPanel.add(btnEditar);
        bottomPanel.add(btnEliminar);
        bottomPanel.add(btnActualizar);

        add(bottomPanel, BorderLayout.SOUTH);

        // =============================
        // EVENTOS
        // =============================
        btnAgregar.addActionListener(e -> {

            JTextField txtNombre = new JTextField();
            JTextField txtContacto = new JTextField();
            JTextField txtTelefono = new JTextField();
            JTextField txtEmail = new JTextField();

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Nombre:"));
            panel.add(txtNombre);
            panel.add(new JLabel("Contacto:"));
            panel.add(txtContacto);
            panel.add(new JLabel("Teléfono:"));
            panel.add(txtTelefono);
            panel.add(new JLabel("Email:"));
            panel.add(txtEmail);

            int res = JOptionPane.showConfirmDialog(null, panel,
                    "Nuevo proveedor", JOptionPane.OK_CANCEL_OPTION);

            if (res == JOptionPane.OK_OPTION) {
                try {
                    models.Proveedor p = new models.Proveedor(
                            0,
                            txtNombre.getText(),
                            txtContacto.getText(),
                            txtTelefono.getText(),
                            txtEmail.getText()
                    );
                    ControllerProveedor.agregarProveedor(p);
                    actualizarLista();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Datos inválidos.");
                }
            }
        });

        btnEditar.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Ingrese ID del proveedor a editar:");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(input);
                    ControllerProveedor.editarProveedor(id);
                    actualizarLista();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID inválido.");
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Ingrese ID del proveedor a eliminar:");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(input);
                    ControllerProveedor.eliminarPorID(id);
                    actualizarLista();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID inválido.");
                }
            }
        });

        btnActualizar.addActionListener(e -> actualizarLista());
    }


    // ============================================
    // ACTUALIZAR LA LISTA USANDO TU CONTROLADOR
    // ============================================
    private void actualizarLista() {
        txtArea.setText("");

        StringBuilder sb = new StringBuilder("Listado de proveedores:\n\n");

        try {
            var con = DLL.ConexionDB.getInstance().getConnection();
            var stmt = con.prepareStatement("SELECT * FROM proveedor");
            var rs = stmt.executeQuery();

            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id_proveedor"))
                  .append(" | Nombre: ").append(rs.getString("nombre"))
                  .append(" | Contacto: ").append(rs.getString("contacto"))
                  .append(" | Tel: ").append(rs.getString("telefono"))
                  .append(" | Email: ").append(rs.getString("email"))
                  .append("\n");
            }

        } catch (Exception e) {
            sb.append("Error cargando proveedores.\n");
        }

        txtArea.setText(sb.toString());
    }
}
