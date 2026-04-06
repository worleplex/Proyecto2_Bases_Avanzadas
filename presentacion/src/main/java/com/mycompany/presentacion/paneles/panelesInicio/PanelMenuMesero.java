package com.mycompany.presentacion.paneles.panelesInicio;

import com.mycompany.presentacion.controlador.Coordinador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelMenuMesero extends JPanel {
    public final Coordinador coordinador;
    private Image imagen;

    private void regresar(JPanel panel) {
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
        v.setContentPane(panel);
        v.revalidate();
    }

    public PanelMenuMesero(Coordinador coordinador) {
        this.coordinador = coordinador;
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

        JPanel panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        JPanel panelSur = new JPanel();
        panelSur.setOpaque(false);
        
        JLabel tituloMesero = new JLabel("Bienvenido al Menú de Mesero");
        tituloMesero.setFont(new Font("Arial", Font.BOLD, 40));
        tituloMesero.setForeground(Color.WHITE);
        panelNorte.add(tituloMesero);

        JButton buttonComandas = new JButton("Comandas");
        JButton buttonRegresar = new JButton("Regresar");
        panelCentro.add(buttonComandas);
        panelSur.add(buttonRegresar);

        buttonRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                regresar(new PanelElegir(coordinador));
            }
        });

        panelFondo.add(panelNorte, BorderLayout.NORTH);
        panelFondo.add(panelCentro, BorderLayout.CENTER);
        panelFondo.add(panelSur, BorderLayout.SOUTH);

        add(panelFondo);
    }
}