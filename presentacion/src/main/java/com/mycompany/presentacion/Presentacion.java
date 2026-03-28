/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.presentacion;

import com.mycompany.presentacion.controlador.Coordinador;
import com.mycompany.presentacion.paneles.PanelLogin;
import com.mycompany.presentacion.paneles.PanelMenuMesero;

import java.awt.*;

/**
 *
 * @author Gael Galaviz
 */
public class Presentacion {

    public static void main(String[] args) {
        Coordinador coordinador = new Coordinador();
        coordinador.iniciarSistema();

    }
}
