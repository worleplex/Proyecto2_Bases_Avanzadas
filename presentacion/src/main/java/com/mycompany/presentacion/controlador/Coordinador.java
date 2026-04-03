package com.mycompany.presentacion.controlador;

import com.mycompany.presentacion.paneles.*;

import javax.swing.*;

/**
 *  @author Luis
 **/
public class Coordinador {
    FramePrincipal framePrincipal;
    PanelLogin panelLogin;
    PanelElegir panelElegir;
    PanelMenuMesero panelMenuMesero;
    PanelMenuAdmin panelMenuAdmin;

    public Coordinador(){

    }
    public void iniciarSistema() {
        if (framePrincipal == null) {
            framePrincipal = new FramePrincipal(this);
        }
        framePrincipal.setVisible(true);
    }

    public void cambiarTituloFrame(String nombre){
        framePrincipal.setTitle("Bienvenido al sistema " + nombre);
    }


    public void cambiarPanel(JPanel panel) {
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(framePrincipal);
        v.setContentPane(panel);
        v.revalidate();
    }

    public void mostrarPanelLogin(){
        if (panelLogin == null) {
            panelLogin = new PanelLogin(this);
        }
        cambiarPanel(panelLogin);
    }

    public void mostrarPanelElegir(){
        if(panelElegir == null){
            panelElegir = new PanelElegir(this);
        }
        cambiarPanel(panelElegir);
    }

    public void mostrarPanelMenuAdmin(){
        if(panelMenuAdmin == null){
            panelMenuAdmin = new PanelMenuAdmin(this);
        }
    }

}
