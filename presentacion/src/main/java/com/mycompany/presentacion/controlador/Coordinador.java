package com.mycompany.presentacion.controlador;

import com.mycompany.presentacion.paneles.*;
import dtos.ProductoDTO;
import entidades.Cliente;
import entidades.ClienteFrecuente;
import javax.swing.*;
import panelesProductos.PanelBuscarProducto;
import panelesProductos.PanelFormularioProducto;
import panelesProductos.PanelOpcionProducto;

/**
 * @author Luis
 * clase auxiliar que se encarga de la navegacion de las pantallas
 */
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
    PanelOpcionProducto panelOpcionProducto;
    PanelBuscarProducto panelBuscarProducto;
    PanelFormularioProducto panelFormularioProducto;

    public Coordinador() {
    }

    public void iniciarSistema() {
        if (framePrincipal == null) {
            framePrincipal = new FramePrincipal(this);
        }
        framePrincipal.setVisible(true);
    }

    public void cambiarTituloFrame(String nombre) {
        framePrincipal.setTitle("Bienvenido al sistema " + nombre);
    }

    public void cambiarTituloIniciarSistema() {
        framePrincipal.setTitle("Bienvenido al sistema");
    }

    public void cambiarPanel(JPanel panel) {
        framePrincipal.setContentPane(panel);
        framePrincipal.revalidate();
        framePrincipal.repaint();
    }

    public void mostrarPanelLogin() {
        if (panelLogin == null) {
            panelLogin = new PanelLogin(this);
        }
        panelLogin.mostrar(); // FIX: limpia campos y pide foco siempre
        cambiarPanel(panelLogin);
    }

    public void mostrarPanelElegir() {
        if (panelElegir == null) {
            panelElegir = new PanelElegir(this);
        }
        cambiarPanel(panelElegir);
    }

    public void mostrarPanelMenuAdmin() {
        if (panelMenuAdmin == null) {
            panelMenuAdmin = new PanelMenuAdmin(this);
        }
        cambiarPanel(panelMenuAdmin);
    }

    public void mostrarPanelMenuMesero() {
        if (panelMenuMesero == null) {
            panelMenuMesero = new PanelMenuMesero(this);
        }
        cambiarPanel(panelMenuMesero);
    }

    public void mostrarPanelConsultarCliente() {
        if (panelConsultarClientes == null) {
            panelConsultarClientes = new PanelConsultarClientes(this);
        }
        cambiarPanel(panelConsultarClientes);
    }

    public void mostrarPanelMenuClientes() {
        if (panelMenuClientes == null) {
            panelMenuClientes = new PanelMenuClientes(this);
        }
        cambiarPanel(panelMenuClientes);
    }

    public void mostrarPanelMenuReportes() {
        if (panelMenuReportes == null) {
            panelMenuReportes = new PanelMenuReportes(this);
        }
        cambiarPanel(panelMenuReportes);
    }

    public void mostrarPanelRegistrarCliente() {
        if (panelRegistrarCliente == null) {
            panelRegistrarCliente = new PanelRegistrarCliente(this);
        }
        cambiarPanel(panelRegistrarCliente);
    }

    public void mostrarPanelSeleccionarID(String accion) {
        if (panelSeleccionarID == null) {
            panelSeleccionarID = new PanelSeleccionarID(this, accion);
        }
        cambiarPanel(panelSeleccionarID);
    }

    public void mostrarPanelEditarClienteFrecuente(ClienteFrecuente cliente) {
        if (panelEditarCliente == null) {
            panelEditarCliente = new PanelEditarCliente(this, cliente);
        }
        cambiarPanel(panelEditarCliente);
    }

    public void mostrarPanelEliminarClienteFrecuente(ClienteFrecuente clienteFrecuente) {
        if (panelEliminarClienteFrecuente == null) {
            panelEliminarClienteFrecuente = new PanelEliminarClienteFrecuente(this, clienteFrecuente);
        }
        cambiarPanel(panelEliminarClienteFrecuente);
    }
    // intento de julian
    public void mostrarPanelOpcionProducto() {
        if (panelOpcionProducto == null) {
            panelOpcionProducto = new PanelOpcionProducto(this);
        }
        cambiarPanel(panelOpcionProducto);
    }
    
    public void mostrarPanelBuscarProducto() {
        if (panelBuscarProducto == null) {
            panelBuscarProducto = new PanelBuscarProducto(this);
        }
        cambiarPanel(panelBuscarProducto);
    }
    
    public void mostrarPanelFormularioProducto(boolean modoEdicion, ProductoDTO productoDTO) {
        if (panelFormularioProducto == null) {
            panelFormularioProducto = new PanelFormularioProducto(this, modoEdicion, productoDTO);
        }
        cambiarPanel(panelFormularioProducto);
    }
}