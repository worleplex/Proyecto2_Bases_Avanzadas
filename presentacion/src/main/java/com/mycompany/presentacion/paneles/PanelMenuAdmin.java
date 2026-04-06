package com.mycompany.presentacion.paneles;

import com.mycompany.presentacion.controlador.Coordinador;
import javax.swing.*;
import java.awt.*;

public class PanelMenuAdmin extends JPanel {
    private final Coordinador coordinador;
    private Image imagen;

    public void cambiarPanel(JPanel panel){
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (v != null) {
            v.setContentPane(panel);
            v.revalidate();
            v.repaint();
        }
    }

    public PanelMenuAdmin(Coordinador coordinador) {
        this.coordinador = coordinador;

        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        }

        setLayout(new BorderLayout());

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

        JLabel labelTitulo = new JLabel("Menú principal", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 45));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        JButton buttonComandas = crearBoton("Comandas");
        JButton buttonProductos = crearBoton("Productos");
        JButton buttonIngredientes = crearBoton("Ingredientes");
        JButton buttonCliente = crearBoton("Clientes");
        JButton buttonReportes = crearBoton("Reportes");

        JButton buttonCerrarSesion = new JButton("Cerrar sesión");
        buttonCerrarSesion.setPreferredSize(new Dimension(250, 50));
        buttonCerrarSesion.setBackground(new Color(105, 105, 105)); // Gris
        buttonCerrarSesion.setForeground(Color.WHITE);
        buttonCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 18));
        buttonCerrarSesion.setFocusPainted(false);
        buttonCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 0, 15));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 150, 60, 150));

        panelBotones.add(buttonComandas);
        panelBotones.add(buttonProductos);
        panelBotones.add(buttonIngredientes);
        panelBotones.add(buttonCliente);
        panelBotones.add(buttonReportes);

        JPanel panelSur = new JPanel();
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panelSur.add(buttonCerrarSesion);

        buttonCliente.addActionListener(e -> {
            cambiarPanel(new PanelMenuClientes(coordinador));
        });
        
        buttonReportes.addActionListener(e -> {
            cambiarPanel(new PanelMenuReportes(coordinador));
        });

        buttonCerrarSesion.addActionListener(e -> {
            coordinador.mostrarPanelElegir();
        });
        
        buttonProductos.addActionListener(e -> {
            coordinador.mostrarPanelOpcionProducto();
        });

        panelFondo.add(labelTitulo, BorderLayout.NORTH);
        panelFondo.add(panelBotones, BorderLayout.CENTER);
        panelFondo.add(panelSur, BorderLayout.SOUTH);
        
        add(panelFondo, BorderLayout.CENTER);
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 60)); // Altura fija
        return btn;
    }
}