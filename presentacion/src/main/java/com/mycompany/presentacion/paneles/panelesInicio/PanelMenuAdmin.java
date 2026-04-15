package com.mycompany.presentacion.paneles.panelesInicio;

import com.mycompany.presentacion.controlador.Coordinador;
import utilidades.UIUtils; 
import javax.swing.*;
import java.awt.*;

public class PanelMenuAdmin extends JPanel {
    
    private final Coordinador coordinador;

    public PanelMenuAdmin(Coordinador coordinador) {
        this.coordinador = coordinador;
        setLayout(new BorderLayout());

        JPanel panelFondo = UIUtils.crearPanelFondo();
        panelFondo.setLayout(new BorderLayout());

        JLabel labelTitulo = new JLabel("Menú principal", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 45));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        JButton buttonComandas = UIUtils.crearBotonAccion("Comandas", Color.WHITE);
        buttonComandas.setForeground(Color.BLACK);
        
        JButton buttonProductos = UIUtils.crearBotonAccion("Productos", Color.WHITE);
        buttonProductos.setForeground(Color.BLACK);
        
        JButton buttonIngredientes = UIUtils.crearBotonAccion("Ingredientes", Color.WHITE);
        buttonIngredientes.setForeground(Color.BLACK);
        
        JButton buttonCliente = UIUtils.crearBotonAccion("Clientes", Color.WHITE);
        buttonCliente.setForeground(Color.BLACK);
        
        JButton buttonReportes = UIUtils.crearBotonAccion("Reportes", Color.WHITE);
        buttonReportes.setForeground(Color.BLACK);

        JButton buttonCerrarSesion = UIUtils.crearBotonAccion("Cerrar sesión", new Color(105, 105, 105));
        buttonCerrarSesion.setPreferredSize(new Dimension(250, 50));

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

        buttonComandas.addActionListener(e -> coordinador.mostrarPanelElegirMesa());
        buttonCliente.addActionListener(e -> coordinador.mostrarPanelMenuClientes());
        buttonReportes.addActionListener(e -> coordinador.mostrarPanelMenuReportes());
        buttonProductos.addActionListener(e -> coordinador.mostrarPanelOpcionProducto());
        buttonIngredientes.addActionListener(e -> coordinador.mostrarPanelCatalogoIngredientes());
        
        buttonCerrarSesion.addActionListener(e -> {
            coordinador.cambiarTitulo("Bienvenido al sistema");
            coordinador.mostrarPanelLogin();
        });

        panelFondo.add(labelTitulo, BorderLayout.NORTH);
        panelFondo.add(panelBotones, BorderLayout.CENTER);
        panelFondo.add(panelSur, BorderLayout.SOUTH);
        
        add(panelFondo, BorderLayout.CENTER);
    }
}