package com.mycompany.presentacion.controlador;

import com.mycompany.presentacion.paneles.PanelLogin;
import com.mycompany.presentacion.paneles.PanelMenuClientes;

/**
 *  @author Luis
 **/
public class Coordinador {
    PanelMenuClientes panelMenuClientes;
    PanelLogin panelLogin;

    public Coordinador(){

    }

    public void iniciarSistema() {
        if (panelLogin == null) {
            panelLogin = new PanelLogin(this);
        }
        panelLogin.setVisible(true);
    }


}
