/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.presentacion.paneles;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Gael Galaviz
 * Clase que muestra el menu principal de opciones para
 * Clientes.
 */
 
public class PanelMenuClientes extends JPanel {

    public PanelMenuClientes() {
        construir();
    }
    /**
     * Configura el diseño visual y los botones del menu.
     */
    private void construir() {
        setLayout(new BorderLayout());
        JPanel fondo = crearFondo();
        fondo.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Clientes", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

        JButton btnRegistrar = crearBoton("Registrar Cliente");
        JButton btnEditar = crearBoton("Editar Cliente");
        JButton btnEliminar = crearBoton("Eliminar Cliente");
        JButton btnBuscador = crearBoton("Consultar Clientes");

        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 0, 15));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 150, 80, 150));

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscador);

        fondo.add(titulo, BorderLayout.NORTH);
        fondo.add(panelBotones, BorderLayout.CENTER);
        add(fondo, BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> abrir(new PanelRegistrarCliente()));
        btnEditar.addActionListener(e -> abrir(new PanelSeleccionarID("editar")));
        btnEliminar.addActionListener(e -> abrir(new PanelSeleccionarID("eliminar")));
        btnBuscador.addActionListener(e -> abrir(new PanelConsultarClientes()));
    }
 /**
  * Cambia el panel actual de la ventana por uno nuevo.
  */
    private void abrir(JPanel panel) {
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
        v.setContentPane(panel);
        v.revalidate();
    }
    
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 45));
        return btn;
    }
    /**
     * Crea el fondo con la imagen
     */
    private JPanel crearFondo() {
        Image imagen = new ImageIcon("presentacion\\src\\main\\java\\com\\mycompany\\presentacion\\fondos\\FondoInicio.png").getImage();
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
            }
        };
        p.setOpaque(false);
        return p;
    }
}
