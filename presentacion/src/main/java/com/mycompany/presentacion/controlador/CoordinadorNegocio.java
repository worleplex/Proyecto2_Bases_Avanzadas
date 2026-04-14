package com.mycompany.presentacion.controlador;

import daos.MesaDAO;

public class CoordinadorNegocio {
    String mesaActual;
    MesaDAO mesaDAO = MesaDAO.getInstancia();

    public CoordinadorNegocio(){

    }

    public CoordinadorNegocio(String mesaActual){
        this.mesaActual = mesaActual;
    }

    public boolean mesaOcupada(Long numero){
        return mesaDAO.mesaOcupada(numero).isEstado();
    }

//    public List<Producto> obtenerProductos() {
//        return mesaDAO.obtenerTodos();
//    }
//
//    public List<Producto> obtenerProductosTipo(TipoProducto tipo){
//        return mesaDAO.obtenerPorTipo(tipo);
//    }
//
//    public List<Producto> buscarProductoNombre(String nombre){
//        return mesaDAO.buscarProductoNombre(nombre);
//    }

    public String getMesaActual() {
        return mesaActual;
    }

    public void setMesaActual(String mesaActual) {
        this.mesaActual = mesaActual;
    }

}
