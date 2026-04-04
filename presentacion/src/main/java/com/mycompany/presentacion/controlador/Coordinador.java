package com.mycompany.presentacion.controlador;

import com.mycompany.presentacion.paneles.*;
import entidades.Cliente;
import entidades.ClienteFrecuente;

import javax.swing.*;

/**
 *  @author Luis
 *  clase auxiliar que se encarga de la navegacion de las pantallas
 **/
public class Coordinador {
    FramePrincipal framePrincipal;
    PanelLogin panelLogin;
    PanelElegir panelElegir;
    PanelMenuMesero panelMenuMesero;
    PanelMenuAdmin panelMenuAdmin;
    PanelConsultarClientes panelConsultarClientes;
    PanelMenuClientes panelMenuClientes;
    PanelMenuReportes panelMenuReportes;
    PanelRegistrarCliente panelRegistrarCliente;
    PanelSeleccionarID panelSeleccionarID;
    PanelEditarCliente panelEditarCliente;
    PanelEliminarClienteFrecuente panelEliminarClienteFrecuente;

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

    public void cambiarTituloIniciarSistema(){
        framePrincipal.setTitle("Bienvenido al sistema");
    }

    public void cambiarPanel(JPanel panel) {
        framePrincipal.setContentPane(panel);
        framePrincipal.revalidate();
        framePrincipal.repaint();
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

    public void mostrarPanelMenuAdmin() {
        if (panelMenuAdmin == null) {
            panelMenuAdmin = new PanelMenuAdmin(this);
            cambiarPanel(panelMenuAdmin);
        }
    }

    public void mostrarPanelConsultarCliente(){
        if(panelConsultarClientes == null){
            panelConsultarClientes = new PanelConsultarClientes(this);
        }
            cambiarPanel(panelConsultarClientes);
    }

    public void mostrarPanelMenuMesero(){
        if(panelMenuMesero == null){
            panelMenuMesero = new PanelMenuMesero(this);
        }
        cambiarPanel(panelMenuMesero);
    }

    public void mostrarPanelMenuClientes(){
        if(panelMenuClientes == null){
            panelMenuClientes = new PanelMenuClientes(this);
        }
        cambiarPanel(panelMenuClientes);
    }

    public void mostrarPanelMenuReportes(){
        if(panelMenuReportes == null){
            panelMenuReportes = new PanelMenuReportes(this);
        }
        cambiarPanel(panelMenuReportes);
    }

    public void mostrarPanelRegistrarCliente(){
        if(panelRegistrarCliente == null){
            panelRegistrarCliente = new PanelRegistrarCliente(this);
        }
        cambiarPanel(panelRegistrarCliente);
    }

    public void mostrarPanelSeleccionarID(String accion){
        if(panelSeleccionarID == null){
            panelSeleccionarID = new PanelSeleccionarID(this, accion);
        }
        cambiarPanel(panelSeleccionarID);
    }

    public void mostrarPanelEditarClienteFrecuente(ClienteFrecuente cliente){
        if(panelEditarCliente == null){
            panelEditarCliente = new PanelEditarCliente(this, cliente);
        }
        cambiarPanel(panelEditarCliente);
    }

    public void mostrarPanelEliminarClienteFrecuente(ClienteFrecuente clienteFrecuente){
        if(panelEliminarClienteFrecuente == null){
            panelEliminarClienteFrecuente = new PanelEliminarClienteFrecuente(this, clienteFrecuente);
        }
        cambiarPanel(panelEliminarClienteFrecuente);
    }

}