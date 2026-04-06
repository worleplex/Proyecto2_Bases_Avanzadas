/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.presentacion.paneles.PanelesReporte;
import com.mycompany.presentacion.controlador.Coordinador;
import com.mycompany.presentacion.paneles.panelesInicio.PanelMenuAdmin;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author julian izaguirre
 */
public class PanelMenuReportes extends JPanel {
    private final Coordinador coordinador;
    private Image imagen;

    private void cambiarPanel(JPanel panel) {
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (v != null) {
            v.setContentPane(panel);
            v.revalidate();
            v.repaint();
        }
    }

    public PanelMenuReportes(Coordinador coordinador) {
        this.coordinador = coordinador;
        java.net.URL url = getClass().getResource("/FondoInicio.png"); 
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        } else {
            System.err.println("Error: No se encontro FondoInicio.png en resources");
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

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(20, 0, 20, 0); 
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel labelTitulo = new JLabel("Reportes");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 45));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0)); 
        
        gbc.gridy = 0;
        panelCentro.add(labelTitulo, gbc);

        JButton btnComandas = crearBotonBlanco("Reporte de Comandas");
        JButton btnClientes = crearBotonBlanco("Reporte de clientes frecuentes");

        JButton btnRegresar = crearBotonAccion("Regresar", new Color(105, 105, 105));
        btnRegresar.setPreferredSize(new Dimension(300, 60));

        btnRegresar.addActionListener(e -> {
            cambiarPanel(new PanelMenuAdmin(coordinador));
        });

        btnComandas.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abriendo Reporte de Comandas");
        });

        btnClientes.addActionListener(e -> {
            cambiarPanel(new PanelReporteClientes(coordinador));
        });

        gbc.gridy = 1;
        panelCentro.add(btnComandas, gbc);

        gbc.gridy = 2;
        panelCentro.add(btnClientes, gbc);

        gbc.gridy = 3; 
        gbc.insets = new Insets(60, 0, 20, 0); 
        panelCentro.add(btnRegresar, gbc);

        panelFondo.add(panelCentro, BorderLayout.CENTER);
        add(panelFondo, BorderLayout.CENTER);
    }

    private JButton crearBotonBlanco(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(400, 70));
        boton.setBackground(Color.WHITE);
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("Arial", Font.PLAIN, 20));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JButton crearBotonAccion(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(250, 50));
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}