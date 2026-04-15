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
     * hasheala contraseña en formato SHA-256 de un solo sentido.
     * @param passwordPlana
     * @return 
     */
    public String hashearContrasena(String passwordPlana) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(passwordPlana.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error al hashear la contraseña", ex);
        }
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
    public EmpleadoDTO iniciarSesion(String usuario, String contrasenaPlana) throws NegocioException {
        String contrasenaHasheada = hashearContrasena(contrasenaPlana);
        Empleado empleadoBD = empleadoDAO.buscarPorContras(usuario, contrasenaHasheada);
        
        if (empleadoBD == null) {
            throw new NegocioException("Usuario o contraseña incorrectos");
        }
        return EmpleadoAdapter.entidadADTO(empleadoBD);
    }
}
