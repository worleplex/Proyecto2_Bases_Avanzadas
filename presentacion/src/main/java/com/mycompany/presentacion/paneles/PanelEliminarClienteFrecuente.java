package com.mycompany.presentacion.paneles;

import javax.swing.*;
import java.awt.*;

public class PanelEliminarClienteFrecuente extends JPanel {
    private Image imagen;

    public PanelEliminarClienteFrecuente() {
        imagen = new ImageIcon("presentacion\\src\\main\\java\\com\\mycompany\\presentacion\\fondos\\FondoInicio.png").getImage();

    }


    public void mostrar() {
        setSize(1080, 720);
        setLayout(new BorderLayout(20, 20));

        JPanel panelFondo = new JPanel(new BorderLayout()) {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                // Habilitar alta calidad
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dibujar imagen con interpolación suave
                g2.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
            }
        };


        setVisible(true);
    }
}
