package views;

import javax.swing.*;
import java.awt.*;
import DLL.ControllerUsuario;
import models.UsuarioBase;
import Enums.Rol;
import views.MenuAdminView;

public class UsuariosView extends JFrame {

    private UsuarioBase usuarioActual;

    public UsuariosView(UsuarioBase usuario) {
        this.usuarioActual = usuario;

        // === Configuración ===
        setTitle("Gestión de Usuarios - " + usuario.getNombre());
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // === Panel principal ===
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // === Barra superior (Volver + Título) ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));

        JButton btnBack = new JButton("⬅ Volver");
        btnBack.setFocusPainted(false);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setBackground(new Color(230, 230, 230));
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> {
            dispose();
            new MenuAdminView(usuarioActual).setVisible(true);
        });

        JLabel lblTitulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(lblTitulo, BorderLayout.CENTER);

        // === Panel de botones ===
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JButton btnCrear = crearBoton("➕ Crear Usuario", new Color(46, 204, 113));
        JButton btnEditar = crearBoton("📝 Editar Usuario", new Color(52, 152, 219));
        JButton btnEliminar = crearBoton("❌ Eliminar Usuario", new Color(231, 76, 60));
        JButton btnListar = crearBoton("📋 Listar Usuarios", new Color(243, 156, 18));

        // === Acciones ===

        // ➕ Crear
        btnCrear.addActionListener(e -> {
            try {
                String nombre = JOptionPane.showInputDialog("Ingrese nombre del usuario:");
                String email = JOptionPane.showInputDialog("Ingrese email del usuario:");
                String contrasena = JOptionPane.showInputDialog("Ingrese contraseña:");
                String[] roles = {"ADMIN", "VENDEDOR", "DEPOSITO"};
                String rolSeleccionado = (String) JOptionPane.showInputDialog(
                        null,
                        "Seleccione un rol:",
                        "Rol de usuario",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        roles,
                        roles[0]
                );

                if (nombre == null || email == null || contrasena == null || rolSeleccionado == null ||
                    nombre.trim().isEmpty() || email.trim().isEmpty() || contrasena.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "❌ Todos los campos son obligatorios.");
                    return;
                }

                Rol rol = Rol.valueOf(rolSeleccionado.toUpperCase());
                UsuarioBase nuevoUsuario = new UsuarioBase(0, nombre, email, contrasena, rol);
                ControllerUsuario.agregarUsuario(nuevoUsuario);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "❌ Error al crear usuario: " + ex.getMessage());
            }
        });

        // 📝 Editar
        btnEditar.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog("Ingrese el ID del usuario a editar:");
                if (idStr != null && !idStr.trim().isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    ControllerUsuario.editarUsuario(id);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "❌ Error al editar usuario: " + ex.getMessage());
            }
        });

        // ❌ Eliminar
        btnEliminar.addActionListener(e -> {
            try {
                String idStr = JOptionPane.showInputDialog("Ingrese el ID del usuario a eliminar:");
                if (idStr != null && !idStr.trim().isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    ControllerUsuario.eliminarPorID(id);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "❌ Error al eliminar usuario: " + ex.getMessage());
            }
        });

        // 📋 Listar
        btnListar.addActionListener(e -> {
            try {
                ControllerUsuario.mostrarTodosUsuarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "❌ Error al mostrar usuarios: " + ex.getMessage());
            }
        });

        // === Añadir botones al panel ===
        gridPanel.add(btnCrear);
        gridPanel.add(btnEditar);
        gridPanel.add(btnEliminar);
        gridPanel.add(btnListar);

        // === Estructura final ===
        mainPanel.add(topPanel);
        mainPanel.add(gridPanel);

        add(mainPanel);
    }

    // === Método para crear botones uniformes con hover ===
    private JButton crearBoton(String texto, Color colorFondo) {
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
}
