package com.mycompany.presentacion.controlador;

import com.mycompany.presentacion.paneles.panelesInicio.*;
import com.mycompany.presentacion.paneles.panelesClientes.*;
import com.mycompany.presentacion.paneles.PanelesReporte.*;
import com.mycompany.presentacion.paneles.PanelesComandas.*;
import dtos.ProductoDTO;
import entidades.ClienteFrecuente;
import excepciones.NegocioException;
import panelesIngredientes.PanelCatalogoIngredientes;
import panelesProductos.PanelBuscarProducto;
import panelesProductos.PanelFormularioProducto;
import panelesProductos.PanelOpcionProducto;
import javax.swing.*;

/**
 * Clase auxiliar que se encarga de la navegación entre pantallas.
 * Centraliza la creación y cambio de paneles del sistema.
 *
 * @author Luis
 */
public class Coordinador {

    private String rolActivo;
    FramePrincipal framePrincipal;
    PanelLogin panelLogin;
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
    PanelElegirMesa panelElegirMesa;

    public Coordinador() {
    }

    public Coordinador(String rol) {
        this.rolActivo = rol;
    }

    public void iniciarSistema() {
        if (framePrincipal == null) {
            framePrincipal = new FramePrincipal(this);
        }
        framePrincipal.setVisible(true);
    }

    public void tituloPersonaLogeada(String nombre) {
        framePrincipal.setTitle("Bienvenido al sistema " + nombre);
    }

    public void cambiarTitulo(String titulo) {
        framePrincipal.setTitle(titulo);
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
        panelLogin.mostrar();
        cambiarPanel(panelLogin);
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

    public void mostrarPanelOpcionProducto() {
        if (panelOpcionProducto == null) {
            panelOpcionProducto = new PanelOpcionProducto(this);
        }
        cambiarPanel(panelOpcionProducto);
    }

    public void mostrarPanelFormularioProducto(boolean modoEdicion, ProductoDTO productoDTO) {
        panelFormularioProducto = new PanelFormularioProducto(this, modoEdicion, productoDTO);
        cambiarPanel(panelFormularioProducto);
    }

    public void mostrarPanelBuscarProducto() {
        panelBuscarProducto = new PanelBuscarProducto(this);
        cambiarPanel(panelBuscarProducto);
    }

    public void mostrarPanelEditarClienteFrecuente(ClienteFrecuente cliente) {
        panelEditarCliente = new PanelEditarCliente(this, cliente);
        cambiarPanel(panelEditarCliente);
    }

    public void mostrarPanelEliminarClienteFrecuente(ClienteFrecuente clienteFrecuente) {
        panelEliminarClienteFrecuente = new PanelEliminarClienteFrecuente(this, clienteFrecuente);
        cambiarPanel(panelEliminarClienteFrecuente);
    }

    public void mostrarPanelSeleccionarID(String accion) {
        panelSeleccionarID = new PanelSeleccionarID(this, accion);
        cambiarPanel(panelSeleccionarID);
    }

    public void mostrarPanelCatalogoIngredientes() {
        cambiarPanel(new PanelCatalogoIngredientes(this,null));
    }

    public void mostrarPanelElegirMesa() {
        if (panelElegirMesa == null) {
            panelElegirMesa = new PanelElegirMesa(this);
        }
        cambiarPanel(panelElegirMesa);
    }

    public void mostrarPanelCrearComanda(CoordinadorNegocio coordinadorNegocio) throws NegocioException {
        cambiarPanel(new PanelCrearComanda(this, coordinadorNegocio));
    }

    public String getRolActivo() {
        return rolActivo;
    }

    public void setRolActivo(String rolActivo) {
        this.rolActivo = rolActivo;
    }
}