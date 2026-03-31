/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosnegocio;

import adaptadores.EmpleadoAdapter;
import daos.EmpleadoDAO;
import dtos.EmpleadoDTO;
import entidades.Empleado;
import excepciones.NegocioException;

/**
 *
 * @author julian izaguirre
 */
public class EmpleadoBO {
    
    private static EmpleadoBO instancia;
    private final EmpleadoDAO empleadoDAO;
    
    private EmpleadoBO() {
        this.empleadoDAO = EmpleadoDAO.getInstance();
    }

    public static EmpleadoBO getInstance() {
        if (instancia == null) {
            instancia = new EmpleadoBO();
        }
        return instancia;
    }
    
    public EmpleadoDTO iniciarSesion(String username, String password) throws NegocioException {
        if (username == null || username.trim().isEmpty()) {
            throw new NegocioException("error el name de usuario no puede estar vacio");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new NegocioException("error no has ingresado tu contra");
        }
        
        Empleado entidad = empleadoDAO.buscarPorContras(username, password);
        
        if (entidad == null) {
            throw new NegocioException("usuario o contra incorrectos intentale de nuevo");
        }
        
        return EmpleadoAdapter.entidadADTO(entidad);
    }
}
