package com.mycompany.presentacion.paneles.panelesInicio;

import com.mycompany.presentacion.controlador.Coordinador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author julian izaguirre
 */
public class PanelElegir extends JPanel {
    private final Coordinador coordinador;
    private Image imagen;

    public PanelElegir(Coordinador coordinador) {
        this.coordinador = coordinador;
        setLayout(new BorderLayout());
        
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


        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 0, 60));
        panelCentro.setOpaque(false);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(200, 150, 200, 150));

        BotonDegradado btnMesero = new BotonDegradado("SOY MESERO");
        BotonDegradado btnAdmin = new BotonDegradado("SOY ADMINISTRADOR");

        btnMesero.addActionListener((ActionEvent e) -> {
            coordinador.cambiarTitulo("Bienvenido mesero");
            coordinador.setRolActivo("mesero");
            coordinador.mostrarPanelMenuMesero();
        });

        btnAdmin.addActionListener((ActionEvent e) -> {
            coordinador.cambiarTitulo("Bienvenido al sistema");
            coordinador.mostrarPanelLogin();
        });

        panelCentro.add(btnMesero);
        panelCentro.add(btnAdmin);

        panelFondo.add(panelCentro, BorderLayout.CENTER);
        add(panelFondo, BorderLayout.CENTER);
    }

    class BotonDegradado extends JButton {
        public BotonDegradado(String texto) {
            super(texto);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 36));
            setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int ancho = getWidth();
            int alto = getHeight();
            
            Color colorClaro = new Color(70, 70, 70);   
            Color colorOscuro = new Color(15, 15, 15);  
            
            GradientPaint gp = new GradientPaint(0, 0, colorClaro, 0, alto, colorOscuro);
            g2.setPaint(gp);
            
            g2.fillRoundRect(0, 0, ancho, alto, 30, 30); // Le bajé un poco la curva (de 60 a 30) para que se vea como en tu imagen
            
            g2.setPaint(new Color(120, 120, 120)); 
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(1, 1, ancho - 2, alto - 2, 30, 30);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }
}