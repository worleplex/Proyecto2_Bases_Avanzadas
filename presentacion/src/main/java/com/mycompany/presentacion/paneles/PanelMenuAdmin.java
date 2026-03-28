package com.mycompany.presentacion.paneles;

import com.mycompany.presentacion.controlador.Coordinador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelMenuAdmin extends JPanel{
    private Image imagen;

    public PanelMenuAdmin() {
        imagen = new ImageIcon("presentacion\\src\\main\\java\\com\\mycompany\\presentacion\\fondos\\FondoInicio.png").getImage();

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
        JPanel panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);

        JLabel labelTitulo = new JLabel("Menu principal");
        labelTitulo.setFont(new Font("Arial",Font.BOLD, 32));
        labelTitulo.setForeground(Color.WHITE);

        JButton buttonComandas = new JButton("Comandas");
        JButton buttonProductos = new JButton("Productos");
        JButton buttonIngredientes = new JButton("Ingredientes");
        JButton buttonCliente = new JButton("Clientes");
        JButton buttonReportes = new JButton("Reportes");

        GridBagConstraints gbc = new GridBagConstraints();
        //arriba, izquierda, abajo, derecha en ese orden
        gbc.insets = new Insets(10,10, 10, 10);

        //fila 1
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panelCentro.add(buttonComandas, gbc);

        //fila 2
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panelCentro.add(buttonProductos, gbc);

        //fila 3
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelCentro.add(buttonIngredientes, gbc);

        //fila 4
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panelCentro.add(buttonCliente, gbc);

        //fila 5
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panelCentro.add(buttonReportes, gbc);

        panelNorte.add(labelTitulo, BorderLayout.NORTH);
        panelFondo.add(panelNorte, BorderLayout.NORTH);
        panelFondo.add(panelCentro, BorderLayout.CENTER);
        add(panelFondo, BorderLayout.CENTER);

        buttonCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Coordinador coordinador = new Coordinador();
                PanelLogin panel = new PanelLogin(coordinador);
                panel.cambiarPanel(new PanelMenuClientes());

            }
        });

    }
}
