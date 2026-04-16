/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.presentacion;

import com.mycompany.presentacion.controlador.Coordinador;
import daos.EmpleadoDAO;
import entidades.Empleado;
import java.util.List;

/**
 *
 * @author Gael Galaviz
 */
public class Presentacion {

    public static void main(String[] args) {
        // para iniciar los inserts
        EmpleadoDAO dao = EmpleadoDAO.getInstance();
        List<Empleado> empleados = dao.buscarTodos();

        if (empleados == null || empleados.isEmpty()) {
            System.out.println("Base de datos vacia corriendo los inserts iniciales");
            correrinicio.insertsMasivos.main(null); 
        }
        
        Coordinador coordinador = new Coordinador();
        coordinador.iniciarSistema();

    }
}
