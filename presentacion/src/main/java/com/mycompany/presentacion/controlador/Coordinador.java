package com.mycompany.presentacion.controlador;

import com.mycompany.presentacion.paneles.FramePrincipal;
import com.mycompany.presentacion.paneles.PanelElegir;
import com.mycompany.presentacion.paneles.PanelLogin;
import com.mycompany.presentacion.paneles.PanelMenuClientes;

/**
 *  @author Luis
 **/
public class Coordinador {
    FramePrincipal framePrincipal;

    public Coordinador(){

    }

    public void iniciarSistema() {
        if (framePrincipal == null) {
            framePrincipal = new FramePrincipal(this);
        }
        framePrincipal.setVisible(true);
    }

}
