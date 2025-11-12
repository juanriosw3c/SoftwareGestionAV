package views;

import javax.swing.*;
import java.awt.*;
import DLL.ControllerUsuario;
import models.UsuarioBase;

public class LoginView extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPass;
    private JCheckBox chkRecordar;
    private JButton btnLogin;
    private JLabel lblLogo, lblEmail, lblPass, lblTitulo;

    private ControllerUsuario controller;

    public LoginView() {
        controller = new ControllerUsuario();
        iniciarComponentes();
        setVisible(true);
    }

    private void iniciarComponentes() {
        setTitle("Software Gestión");
        setSize(380, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // ======== LOGO ========
        lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon icon = new ImageIcon("img/Logo SG.png");
        Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        lblLogo.setIcon(new ImageIcon(scaled));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // ======== TÍTULO ========
        lblTitulo = new JLabel("Iniciar Sesión");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(5, 0, 20, 0));

        // ======== PANEL FORMULARIO ========
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        lblEmail = new JLabel("Correo electrónico:");
        txtEmail = new JTextField(18);

        lblPass = new JLabel("Contraseña:");
        txtPass = new JPasswordField(18);

        chkRecordar = new JCheckBox("Recordar usuario");
        btnLogin = new JButton("Iniciar sesión");

        // ======== FILAS ========
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblEmail, gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(txtEmail, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(lblPass, gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(txtPass, gbc);

        gbc.gridy++;
        formPanel.add(chkRecordar, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnLogin, gbc);

        // ======== EVENTO BOTÓN LOGIN ========
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();

            if (email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UsuarioBase usuario = controller.login(email, pass);

            if (usuario != null) {
                JOptionPane.showMessageDialog(this,
                        "Bienvenido " + usuario.getRol() + " " + usuario.getNombre(),
                        "Acceso concedido", JOptionPane.INFORMATION_MESSAGE);

                switch (usuario.getRol()) {
                case ADMIN -> {
                    new MenuAdminView(usuario).setVisible(true);
                    dispose();
                }
                case VENDEDOR -> {
                  
                }
                case DEPOSITO -> {
                    
                }
                default -> {
                    JOptionPane.showMessageDialog(this, "Rol no reconocido.");
                }
            }

                dispose(); // cerrar login
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ======== ENSAMBLADO FINAL ========
        panel.add(lblLogo);
        panel.add(lblTitulo);
        panel.add(formPanel);
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
