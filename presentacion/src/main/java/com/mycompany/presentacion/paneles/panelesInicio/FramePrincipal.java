package com.mycompany.presentacion.paneles.panelesInicio;

import com.mycompany.presentacion.controlador.Coordinador;

import javax.swing.*;
import java.awt.*;

public class FramePrincipal extends JFrame {
    public final Coordinador coordinador;

    public FramePrincipal(Coordinador coordinador) {
        this.coordinador = coordinador;
        mostrar();
        setVisible(true);
    }

    public void cambiarPanel(JPanel panel){
        getContentPane().removeAll();
        getContentPane().add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
        panel.requestFocusInWindow();
    }

    public void mostrar(){
        setSize(1080,720);
        setTitle("Pantalla de Eleccion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        PanelLogin panelLogin = new PanelLogin(coordinador);
        add(panelLogin, BorderLayout.CENTER);
    }
}
