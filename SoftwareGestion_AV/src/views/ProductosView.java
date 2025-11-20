package views;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import DLL.ConexionDB;
import DLL.ControllerProducto;
import Enums.TipoMovimiento;
import helpers.UIHelper;
import models.Producto;
import models.UsuarioBase;

public class ProductosView extends JFrame {

    private UsuarioBase usuarioActual;

    private JList<String> listaProductos;
    private DefaultListModel<String> modeloLista;

    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnVender;
    private JButton btnReponer;
    private JButton btnAjuste;
    private JButton btnVolver;

    public ProductosView(UsuarioBase usuario) {
        this.usuarioActual = usuario;

        configurarVentana();
        inicializarComponentes();
        cargarProductos();

        setVisible(true);
    }

    private void configurarVentana() {
        setTitle("Gestión de Productos");
        setSize(650, 550);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {

        // ===== Barra superior =====
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UIHelper.GRAY_BG);
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnVolver = UIHelper.crearBotonVolver(() -> {
            dispose();
            new MenuAdminView(usuarioActual).setVisible(true);
        });

        JLabel titulo = UIHelper.crearTitulo("Gestión de Productos");

        top.add(btnVolver, BorderLayout.WEST);
        top.add(titulo, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);

        // ===== Lista de productos =====
        modeloLista = new DefaultListModel<>();
        listaProductos = new JList<>(modeloLista);
        listaProductos.setFont(UIHelper.FONT_LIST);

        JScrollPane scroll = new JScrollPane(listaProductos);
        scroll.setBorder(BorderFactory.createTitledBorder("Productos registrados"));

        add(scroll, BorderLayout.CENTER);

        // ===== Botonera inferior =====
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        botones.setBackground(UIHelper.GRAY_BG);

        btnAgregar  = UIHelper.crearBoton("Agregar", UIHelper.BLUE);
        btnEditar   = UIHelper.crearBoton("Editar", UIHelper.GREEN);
        btnEliminar = UIHelper.crearBoton("Eliminar", UIHelper.RED);
        btnVender   = UIHelper.crearBoton("Registrar Venta", UIHelper.PURPLE);
        btnReponer  = UIHelper.crearBoton("Reponer Stock", UIHelper.TEAL);
        btnAjuste   = UIHelper.crearBoton("Ajuste", UIHelper.ORANGE);

        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnVender);
        botones.add(btnReponer);
        botones.add(btnAjuste);

        add(botones, BorderLayout.SOUTH);

        // ===== Eventos =====
        btnAgregar.addActionListener(e -> agregarProducto());
        btnEditar.addActionListener(e -> editarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnVender.addActionListener(e -> moverStock(TipoMovimiento.SALIDA));
        btnReponer.addActionListener(e -> moverStock(TipoMovimiento.ENTRADA));
        btnAjuste.addActionListener(e -> moverStock(TipoMovimiento.AJUSTE));
    }

    // ======================
    // CARGAR PRODUCTOS
    // ======================
    private void cargarProductos() {
        modeloLista.clear();

        try {
            Connection con = ConexionDB.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT * FROM producto");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                modeloLista.addElement(
                    "ID: " + rs.getInt("id_producto") +
                    " | " + rs.getString("nombre") +
                    " | $" + rs.getDouble("precio") +
                    " | Stock: " + rs.getInt("stock_actual")
                );
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage());
        }
    }

    private int obtenerID() {
        String s = listaProductos.getSelectedValue();
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            return -1;
        }
        return Integer.parseInt(s.split("\\|")[0].replace("ID:", "").trim());
    }

    private void agregarProducto() {
        try {
            String nombre = JOptionPane.showInputDialog("Nombre:");
            String desc   = JOptionPane.showInputDialog("Descripción:");
            double precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));
            int stockA    = Integer.parseInt(JOptionPane.showInputDialog("Stock actual:"));
            int stockM    = Integer.parseInt(JOptionPane.showInputDialog("Stock mínimo:"));

            Producto p = new Producto(desc, nombre, precio, stockA, stockM);
            ControllerProducto.agregarProducto(p);

            cargarProductos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos.");
        }
    }

    private void editarProducto() {
        int id = obtenerID();
        if (id == -1) return;
        ControllerProducto.editarProducto(id);
        cargarProductos();
    }

    private void eliminarProducto() {
        int id = obtenerID();
        if (id == -1) return;

        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            ControllerProducto.eliminarPorID(id);
            cargarProductos();
        }
    }

    private void moverStock(TipoMovimiento tipo) {
        int id = obtenerID();
        if (id == -1) return;

        try {
            int cant = Integer.parseInt(JOptionPane.showInputDialog("Cantidad:"));
            ControllerProducto.aplicarMovimiento(id, cant, tipo, usuarioActual.getIdUsuario());
            cargarProductos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
        }
    }
}

