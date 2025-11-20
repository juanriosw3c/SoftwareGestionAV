package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import DLL.ControllerUsuario;
import Enums.Rol;
import models.UsuarioBase;

import java.sql.*;

public class UsuariosView extends JFrame {

    private JPanel cardsPanel; 
    private UsuarioBase usuario;

    public UsuariosView(UsuarioBase usuario) {
        this.usuario = usuario;

        setTitle("Usuarios - " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // =============================
        // TOP PANEL (volver + título)
        // =============================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnBack = crearBotonBack();
        topPanel.add(btnBack, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        topPanel.add(lblTitulo, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // =============================
        // PANEL CENTRAL (cards)
        // =============================
        cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBackground(new Color(245, 245, 245));

        JScrollPane scroll = new JScrollPane(cardsPanel);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        // =============================
        // PANEL INFERIOR (acciones)
        // =============================
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(new Color(245, 245, 245));

        JButton btnAgregar = crearBoton("Agregar usuario");
        JButton btnEditar = crearBoton("Editar usuario");
        JButton btnEliminar = crearBoton("Eliminar usuario");
        JButton btnActualizar = crearBoton("Actualizar lista");

        bottomPanel.add(btnAgregar);
        bottomPanel.add(btnEditar);
        bottomPanel.add(btnEliminar);
        bottomPanel.add(btnActualizar);

        add(bottomPanel, BorderLayout.SOUTH);

        // =============================
        // PERMISOS POR ROL
        // =============================
        if (usuario.getRol() != Rol.ADMIN) {
            btnAgregar.setEnabled(false);
            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);

            btnAgregar.setToolTipText("Solo administradores");
            btnEditar.setToolTipText("Solo administradores");
            btnEliminar.setToolTipText("Solo administradores");
        }

        // =============================
        // EVENTOS
        // =============================
        btnAgregar.addActionListener(e -> {
            UsuarioBase nuevo = UsuarioInputDialog.crearUsuario();
            if (nuevo != null) {
                ControllerUsuario.agregarUsuario(nuevo);
                cargarUsuarios();
            }
        });

        btnEditar.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("ID del usuario a editar:");
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    ControllerUsuario.editarUsuario(id);
                    cargarUsuarios();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ID inválido");
                }
            }
        });

        btnEliminar.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("ID del usuario a eliminar:");
            if (idStr != null) {
                try {
                    int id = Integer.parseInt(idStr);
                    ControllerUsuario.eliminarPorID(id);
                    cargarUsuarios();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ID inválido");
                }
            }
        });

        btnActualizar.addActionListener(e -> cargarUsuarios());

        // =============================
        // Cargar lista inicial
        // =============================
        cargarUsuarios();
    }

    // ============================================================
    // Cargar usuarios y crear tarjetas visuales
    // ============================================================
    private void cargarUsuarios() {
        cardsPanel.removeAll();

        try {
            Connection con = DLL.ConexionDB.getInstance().getConnection();
            PreparedStatement st = con.prepareStatement("SELECT * FROM usuarios");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                JPanel card = crearCardUsuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre_usuario"),
                        rs.getString("email"),
                        rs.getString("rol")
                );
                cardsPanel.add(card);
                cardsPanel.add(Box.createVerticalStrut(10));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios");
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    // ============================================================
    // Crear tarjeta individual con icono + datos
    // ============================================================
    private JPanel crearCardUsuario(int id, String nombre, String email, String rol) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // ICONO
        JLabel lblIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/usuario.png"));
            Image scaled = icon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
            lblIcon.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            lblIcon.setText("👤");
            lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        }

        // TEXTO
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        textPanel.add(new JLabel("ID: " + id));
        textPanel.add(new JLabel("Nombre: " + nombre));
        textPanel.add(new JLabel("Email: " + email));
        textPanel.add(new JLabel("Rol: " + rol));

        panel.add(lblIcon, BorderLayout.WEST);
        panel.add(textPanel, BorderLayout.CENTER);

        return panel;
    }

    // ============================================================
    // Botón volver
    // ============================================================
    private JButton crearBotonBack() {
        JButton btn = new JButton();

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/atras.png"));
            Image scaled = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            btn.setText("⬅");
        }

        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(40, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            dispose();
            new MenuAdminView(usuario).setVisible(true);
        });

        return btn;
    }

    // ============================================================
    // Botón inferior estándar
    // ============================================================
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 35));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
