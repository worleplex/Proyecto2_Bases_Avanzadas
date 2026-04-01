package com.mycompany.presentacion.paneles;

import javax.swing.*;
import java.awt.*;

public class PanelMenuMesero extends JPanel {
    private Image imagen;

    public PanelMenuMesero() {
        // 1. Cargamos la imagen
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        }
        setLayout(new BorderLayout(20, 20));

        // 2. Todo lo que tenías en mostrar() ahora se ejecuta automáticamente aquí al crear el panel
        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagen != null) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
                }
            }
        };

        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        
        JLabel tituloMesero = new JLabel("Bienvenido al Menú de Mesero");
        tituloMesero.setFont(new Font("Arial", Font.BOLD, 40));
        tituloMesero.setForeground(Color.WHITE);
        panelCentro.add(tituloMesero);

        panelFondo.add(panelCentro, BorderLayout.CENTER);
        add(panelFondo, BorderLayout.CENTER);
    }
}