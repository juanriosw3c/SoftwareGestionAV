package views;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import DLL.ConexionDB;
import DLL.ControllerProveedor;
import models.Proveedor;
import models.UsuarioBase;

public class ProveedoresView extends JFrame {

    private UsuarioBase usuarioActual;

    private DefaultListModel<String> modeloLista;
    private JList<String> listaProveedores;

    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnDetalles;
    private JButton btnActualizar;
    private JButton btnVolver;

    public ProveedoresView(UsuarioBase usuario) {
        this.usuarioActual = usuario;

        configurarVentana();
        inicializarComponentes();
        cargarProveedores();

        setVisible(true);
    }

    // ============================
    // CONFIGURAR FRAME
    // ============================
    private void configurarVentana() {
        setTitle("Gestión de Proveedores");
        setSize(650, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    // ============================
    // COMPONENTES
    // ============================
    private void inicializarComponentes() {

        // ===== TOP BAR =====
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        top.setBackground(new Color(245, 245, 245));

        btnVolver = new JButton("⬅ Volver");
        btnVolver.addActionListener(e -> {
            dispose();
            new MenuAdminView(usuarioActual).setVisible(true);
        });

        JLabel titulo = new JLabel("Gestión de Proveedores", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        top.add(btnVolver, BorderLayout.WEST);
        top.add(titulo, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);

        // ===== LISTA =====
        modeloLista = new DefaultListModel<>();
        listaProveedores = new JList<>(modeloLista);
        listaProveedores.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JScrollPane scroll = new JScrollPane(listaProveedores);
        scroll.setBorder(BorderFactory.createTitledBorder("Proveedores registrados"));
        add(scroll, BorderLayout.CENTER);

        // ===== BOTONERA =====
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        acciones.setBackground(new Color(245, 245, 245));

        btnAgregar    = crearBoton("Agregar",    new Color(52, 152, 219));
        btnEditar     = crearBoton("Editar",     new Color(46, 204, 113));
        btnEliminar   = crearBoton("Eliminar",   new Color(231, 76, 60));
        btnDetalles   = crearBoton("Detalles",   new Color(155, 89, 182));
        btnActualizar = crearBoton("Actualizar", new Color(26, 188, 156));

        acciones.add(btnAgregar);
        acciones.add(btnEditar);
        acciones.add(btnEliminar);
        acciones.add(btnDetalles);
        acciones.add(btnActualizar);

        add(acciones, BorderLayout.SOUTH);

        // ===== Eventos =====
        btnAgregar.addActionListener(e -> agregarProveedor());
        btnEditar.addActionListener(e -> editarProveedor());
        btnEliminar.addActionListener(e -> eliminarProveedor());
        btnDetalles.addActionListener(e -> verDetalles());
        btnActualizar.addActionListener(e -> cargarProveedores());
    }

    // ============================
    // CREAR BOTÓN
    // ============================
    private JButton crearBoton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
        b.setForeground(Color.WHITE);
        b.setBackground(color);
        b.setFocusPainted(false);

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { b.setBackground(color.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt)  { b.setBackground(color); }
        });

        return b;
    }

    // ============================
    // CARGAR PROVEEDORES
    // ============================
    private void cargarProveedores() {
        modeloLista.clear();
        try {
            Connection con = ConexionDB.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT * FROM proveedor");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                modeloLista.addElement(
                    "ID: " + rs.getInt("id_proveedor") +
                    " | " + rs.getString("nombre") +
                    " | Contacto: " + rs.getString("contacto")
                );
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + e.getMessage());
        }
    }

    // ============================
    // OBTENER ID SELECCIONADO
    // ============================
    private int obtenerID() {
        String item = listaProveedores.getSelectedValue();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un proveedor.");
            return -1;
        }

        try {
            return Integer.parseInt(item.split("\\|")[0].replace("ID:", "").trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener ID.");
            return -1;
        }
    }

    // ============================
    // AGREGAR
    // ============================
    private void agregarProveedor() {
        try {
            String nombre   = JOptionPane.showInputDialog("Nombre:");
            String contacto = JOptionPane.showInputDialog("Contacto:");
            String telefono = JOptionPane.showInputDialog("Teléfono:");
            String email    = JOptionPane.showInputDialog("Email:");

            if (nombre == null || nombre.isEmpty()) return;

            Proveedor p = new Proveedor(
                0, nombre, contacto, telefono, email
            );

            ControllerProveedor.agregarProveedor(p);
            cargarProveedores();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }

    // ============================
    // EDITAR
    // ============================
    private void editarProveedor() {
        int id = obtenerID();
        if (id == -1) return;

        ControllerProveedor.editarProveedor(id);
        cargarProveedores();
    }

    // ============================
    // ELIMINAR
    // ============================
    private void eliminarProveedor() {
        int id = obtenerID();
        if (id == -1) return;

        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar proveedor?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            ControllerProveedor.eliminarPorID(id);
            cargarProveedores();
        }
    }

    // ============================
    // DETALLES
    // ============================
    private void verDetalles() {
        int id = obtenerID();
        if (id == -1) return;

        Proveedor p = ControllerProveedor.buscarPorID(id);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Proveedor no encontrado.");
            return;
        }

        JOptionPane.showMessageDialog(this,
                "ID: " + p.getId_proveedor() + "\n" +
                "Nombre: " + p.getNombre() + "\n" +
                "Contacto: " + p.getContacto() + "\n" +
                "Teléfono: " + p.getTelefono() + "\n" +
                "Email: " + p.getEmail(),
                "Detalles del proveedor",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
