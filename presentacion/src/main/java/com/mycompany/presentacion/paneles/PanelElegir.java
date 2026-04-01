/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.presentacion.paneles;

import com.mycompany.presentacion.controlador.Coordinador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author julian izaguirre
 */
public class PanelElegir extends JPanel{
    private final Coordinador coordinador;
    private Image imagen;

    private void cambiarPanel(JPanel panel) {
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
        v.setContentPane(panel);
        v.revalidate();
    }

    public PanelElegir(Coordinador coordinador) {
        setSize(1080, 720);
        this.coordinador = coordinador;
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        } else {
            System.err.println("Error: No se encontro FondoInicio.png");
        }
        mostrar();
    }

    public void mostrar() {
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

        // Panel central transparente para agrupar los botones
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 0, 25, 0); // Espacio entre botones
        gbc.gridx = 0; 
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        BotonDegradado btnMesero = new BotonDegradado("SOY MESERO");
        btnMesero.setPreferredSize(new Dimension(350, 70));

        BotonDegradado btnAdmin = new BotonDegradado("SOY ADMINISTRADOR");
        btnAdmin.setPreferredSize(new Dimension(350, 70));

        btnMesero.addActionListener((ActionEvent e) -> {
            cambiarPanel(new PanelMenuMesero());
        });

        btnAdmin.addActionListener((ActionEvent e) -> {
            cambiarPanel(new PanelMenuAdmin());
        });

        // Agregamos los botones directamente al panel transparente (sin la caja blanca)
        gbc.gridy = 0;
        panelCentro.add(btnMesero, gbc);
        
        gbc.gridy = 1;
        panelCentro.add(btnAdmin, gbc);

        panelFondo.add(panelCentro, BorderLayout.CENTER);
        add(panelFondo, BorderLayout.CENTER);

        setVisible(true);
    }

    class BotonDegradado extends JButton {
        public BotonDegradado(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 28)); 
            setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int ancho = getWidth();
            int alto = getHeight();
            
            // Colores oscuros y elegantes (Grafito / Carbón)
            Color colorClaro = new Color(70, 70, 70);   // Gris oscuro
            Color colorOscuro = new Color(15, 15, 15);  // Casi negro
            
            // Degradado de arriba hacia abajo para dar efecto de relieve o 3D
            GradientPaint gp = new GradientPaint(0, 0, colorClaro, 0, alto, colorOscuro);
            g2.setPaint(gp);
            
            g2.fillRoundRect(0, 0, ancho, alto, 60, 60);
            
            // Borde sutil gris claro para que el botón no se pierda en el fondo
            g2.setPaint(new Color(120, 120, 120)); 
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, ancho - 2, alto - 2, 60, 60);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
