package com.mycompany.presentacion.paneles.PanelesReporte;

import com.mycompany.presentacion.controlador.Coordinador;
import utilidades.UIUtils;
import javax.swing.*;
import java.awt.*;

/**
 * @author julian izaguirre
 */
public class PanelMenuReportes extends JPanel {
    private final Coordinador coordinador;

    public PanelMenuReportes(Coordinador coordinador) {
        this.coordinador = coordinador;
        setLayout(new BorderLayout());
        JPanel panelFondo = UIUtils.crearPanelFondo();
        panelFondo.setLayout(new BorderLayout());

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(20, 0, 20, 0); 
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel labelTitulo = new JLabel("Reportes");
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 45));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0)); 
        
        gbc.gridy = 0;
        panelCentro.add(labelTitulo, gbc);
        JButton btnComandas = UIUtils.crearBotonAccion("Reporte de Comandas", Color.WHITE);
        btnComandas.setForeground(Color.BLACK);
        btnComandas.setPreferredSize(new Dimension(400, 70));
        
        JButton btnClientes = UIUtils.crearBotonAccion("Reporte de clientes frecuentes", Color.WHITE);
        btnClientes.setForeground(Color.BLACK);
        btnClientes.setPreferredSize(new Dimension(400, 70));

        JButton btnRegresar = UIUtils.crearBotonAccion("Regresar", new Color(105, 105, 105));
        btnRegresar.setPreferredSize(new Dimension(300, 60));
        btnRegresar.addActionListener(e -> coordinador.mostrarPanelMenuAdmin());
        btnComandas.addActionListener(e -> coordinador.cambiarPanel(new PanelReporteComandas(coordinador)));
        btnClientes.addActionListener(e -> coordinador.cambiarPanel(new PanelReporteClientes(coordinador)));

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
}