package com.mycompany.presentacion.paneles.panelesInicio;

import com.mycompany.presentacion.controlador.Coordinador;

import javax.swing.*;
import java.awt.*;

public class FramePrincipal extends JFrame {
    public final Coordinador coordinador;
    private Image imagen;

    public void cambiarPanel(JPanel panel){
        getContentPane().removeAll();
        getContentPane().add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
        panel.requestFocusInWindow();
    }

    public FramePrincipal(Coordinador coordinador) {
        this.coordinador = coordinador;
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        } else {
            System.err.println("Error: No se encontro FondoInicio.png");
        }
        mostrar();
        setVisible(true);
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
