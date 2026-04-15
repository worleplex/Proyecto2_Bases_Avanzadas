package com.mycompany.presentacion.paneles.panelesInicio;

import com.mycompany.presentacion.controlador.Coordinador;
import utilidades.UIUtils;
import javax.swing.*;
import java.awt.*;

public class PanelMenuMesero extends JPanel {
    
    public final Coordinador coordinador;

    public PanelMenuMesero(Coordinador coordinador) {
        this.coordinador = coordinador;
        setLayout(new BorderLayout());

        JPanel panelFondo = UIUtils.crearPanelFondo();
        panelFondo.setLayout(new BorderLayout(20, 20));

        JPanel panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        JPanel panelSur = new JPanel();
        panelSur.setOpaque(false);
        
        JLabel tituloMesero = new JLabel("Bienvenido al Menú de Mesero", SwingConstants.CENTER);
        tituloMesero.setFont(new Font("Segoe UI", Font.BOLD, 40));
        tituloMesero.setForeground(Color.WHITE);
        tituloMesero.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        panelNorte.add(tituloMesero);

        JButton buttonComandas = UIUtils.crearBotonAccion("Comandas", Color.WHITE);
        buttonComandas.setForeground(Color.BLACK);
        buttonComandas.setPreferredSize(new Dimension(250, 60));
        
        JButton buttonRegresar = UIUtils.crearBotonAccion("Regresar", new Color(105, 105, 105));
        buttonRegresar.setPreferredSize(new Dimension(200, 50));
        
        panelCentro.add(buttonComandas);
        panelSur.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0)); // Espaciado abajo
        panelSur.add(buttonRegresar);

        buttonRegresar.addActionListener(e -> coordinador.mostrarPanelLogin());
        buttonComandas.addActionListener(e -> coordinador.mostrarPanelElegirMesa());

        panelFondo.add(panelNorte, BorderLayout.NORTH);
        panelFondo.add(panelCentro, BorderLayout.CENTER);
        panelFondo.add(panelSur, BorderLayout.SOUTH);

        add(panelFondo, BorderLayout.CENTER);
    }
}