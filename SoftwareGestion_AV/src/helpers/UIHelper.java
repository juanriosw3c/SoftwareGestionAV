package helpers;

import javax.swing.*;
import java.awt.*;

public class UIHelper {

    // ========================================
    // 🎨 PALETA DE COLORES PRINCIPAL
    // ========================================
    public static final Color BLUE   = new Color(52, 152, 219);
    public static final Color GREEN  = new Color(46, 204, 113);
    public static final Color RED    = new Color(231, 76, 60);
    public static final Color ORANGE = new Color(243, 156, 18);
    public static final Color PURPLE = new Color(155, 89, 182);
    public static final Color TEAL   = new Color(26, 188, 156);
    public static final Color BROWN  = new Color(111, 66, 33);
    public static final Color GRAY_BG = new Color(245, 245, 245);

    // ========================================
    // 🔤 FUENTES DEL SISTEMA
    // ========================================
    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_LIST   = new Font("Segoe UI", Font.PLAIN, 15);

    // ========================================
    // 🔘 BOTÓN ESTÁNDAR PERSONALIZADO
    // ========================================
    public static JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    // ========================================
    // ⬅ BOTÓN VOLVER
    // ========================================
    public static JButton crearBotonVolver(Runnable accion) {
        JButton btn = new JButton("⬅ Volver");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> accion.run());

        return btn;
    }

    // ========================================
    // 🏷 TÍTULO CENTRALIZADO
    // ========================================
    public static JLabel crearTitulo(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(FONT_TITLE);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        return lbl;
    }

    // ========================================
    // 📦 PANEL PRINCIPAL BASE
    // ========================================
    public static JPanel crearPanelPrincipal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(GRAY_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

}
