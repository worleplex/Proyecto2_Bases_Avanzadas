/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.presentacion.paneles.panelesClientes;

import com.mycompany.presentacion.controlador.Coordinador;
import utilidades.UIUtils;
import javax.swing.*;
import java.awt.*;

/**
 * @author Gael Galaviz
 * Clase que muestra el menu principal de opciones para Clientes.
 */
public class PanelMenuClientes extends JPanel {
    private final Coordinador coordinador;
    
    public PanelMenuClientes(Coordinador coordinador) {
        this.coordinador = coordinador;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        
        JPanel fondo = UIUtils.crearPanelFondo();
        fondo.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Clientes", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

        JButton btnRegistrar = UIUtils.crearBotonAccion("Registrar Cliente", Color.WHITE);
        btnRegistrar.setForeground(Color.BLACK);
        
        JButton btnEditar = UIUtils.crearBotonAccion("Editar Cliente", Color.WHITE);
        btnEditar.setForeground(Color.BLACK);
        
        JButton btnEliminar = UIUtils.crearBotonAccion("Eliminar Cliente", Color.WHITE);
        btnEliminar.setForeground(Color.BLACK);
        
        JButton btnBuscador = UIUtils.crearBotonAccion("Consultar Clientes", Color.WHITE);
        btnBuscador.setForeground(Color.BLACK);
        
        JButton buttonRegresar = UIUtils.crearBotonAccion("Regresar", new Color(105, 105, 105));
        buttonRegresar.setPreferredSize(new Dimension(200, 45));

        JPanel panelSur = new JPanel();
        panelSur.setOpaque(false);

        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 0, 15));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 150, 80, 150));

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnBuscador);
        
        panelSur.add(buttonRegresar);

        fondo.add(titulo, BorderLayout.NORTH);
        fondo.add(panelBotones, BorderLayout.CENTER);
        fondo.add(panelSur, BorderLayout.SOUTH);
        add(fondo, BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> coordinador.mostrarPanelRegistrarCliente());
        btnEditar.addActionListener(e -> coordinador.mostrarPanelSeleccionarID("editar"));
        btnEliminar.addActionListener(e -> coordinador.mostrarPanelSeleccionarID("eliminar"));
        btnBuscador.addActionListener(e -> coordinador.mostrarPanelConsultarCliente());
        buttonRegresar.addActionListener(e -> coordinador.mostrarPanelMenuAdmin());
    }
}