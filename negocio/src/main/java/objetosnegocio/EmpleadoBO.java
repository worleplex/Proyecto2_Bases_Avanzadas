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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Capa de negocio para la gestión de empleados.
 * Maneja la autenticación y validaciones de acceso al sistema.
 *
 * @author julian izaguirre
 */
public class EmpleadoBO {

    private static final Logger LOG = Logger.getLogger(EmpleadoBO.class.getName());
    private static EmpleadoBO instancia;
    private final EmpleadoDAO empleadoDAO;

    /**
     * Constructor privado para garantizar el patrón Singleton.
     * Inicializa el DAO de empleado.
     */
    private EmpleadoBO() {
        this.empleadoDAO = EmpleadoDAO.getInstance();
    }

    /**
     * Retorna la única instancia de EmpleadoBO.
     * Si no existe, la crea.
     *
     * @return instancia única de EmpleadoBO
     */
    public static EmpleadoBO getInstance() {
        if (instancia == null) {
            instancia = new EmpleadoBO();
        }
        return instancia;
    }

    /**
     * Autentica a un empleado verificando su username y contraseña.
     * Valida que ningún campo esté vacío antes de consultar la BD.
     *
     * @param username nombre de usuario del empleado
     * @param password contraseña del empleado
     * @return EmpleadoDTO con los datos del empleado autenticado
     * @throws NegocioException si los campos están vacíos o las credenciales son incorrectas
     */
    public EmpleadoDTO iniciarSesion(String username, String password) throws NegocioException {
        LOG.log(Level.INFO, "Intento de inicio de sesión para username: {0}", username);
        if (username == null || username.trim().isEmpty()) {
            throw new NegocioException("error el name de usuario no puede estar vacio");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new NegocioException("error no has ingresado tu contra");
        }
        Empleado entidad = empleadoDAO.buscarPorContras(username, password);
        if (entidad == null) {
            LOG.log(Level.WARNING, "Credenciales incorrectas para username: {0}", username);
            throw new NegocioException("usuario o contra incorrectos intentale de nuevo");
        }
        LOG.log(Level.INFO, "Inicio de sesión exitoso para: {0}", username);
        return EmpleadoAdapter.entidadADTO(entidad);
    }
}
