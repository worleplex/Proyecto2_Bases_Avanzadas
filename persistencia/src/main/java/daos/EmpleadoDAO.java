/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import com.mycompany.persistencia.Persistencia.ConexionBD;
import entidades.Empleado;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * DAO para operaciones de acceso a datos de la entidad Empleado
 * Implementa el patrón Singleton para reutilizar la instancia
 * @author julian izaguirre
 */
public class EmpleadoDAO {

    private static final Logger LOG = Logger.getLogger(EmpleadoDAO.class.getName());
    private static EmpleadoDAO instancia;

    /**
     * Constructor privado para garantizar el patrón Singleton
     */
    private EmpleadoDAO() {
    }

    /**
     * Retorna la única instancia de EmpleadoDAO.
     * Si no existe, la crea.
     *
     * @return instancia única de EmpleadoDAO
     */
    public static EmpleadoDAO getInstance() {
        if (instancia == null) {
            instancia = new EmpleadoDAO();
        }
        return instancia;
    }

    /**
     * Busca un empleado por su nombre de usuario y contraseña.
     * Utilizado principalmente para el proceso de inicio de sesión.
     *
     * @param username nombre de usuario del empleado
     * @param password contraseña del empleado
     * @return el Empleado encontrado, o @code null si no existe
     */
    public Empleado buscarPorContras(String username, String password) {
        LOG.log(Level.INFO, "Buscando empleado con username: {0}", username);
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "select e from Empleado e where e.username = :user and e.password = :pass";
            TypedQuery<Empleado> query = em.createQuery(jpql, Empleado.class);
            query.setParameter("user", username);
            query.setParameter("pass", password);
            List<Empleado> resultados = query.getResultList();

            if (resultados.isEmpty()) {
                LOG.log(Level.WARNING, "No se encontró empleado con username: {0}", username);
                return null;
            }

            LOG.log(Level.INFO, "Empleado encontrado: {0}", username);
            return resultados.get(0);

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar empleado por credenciales: {0}", ex.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    /**
     * Recupera todos los empleados registrados en la base de datos.
     *
     * @return lista de todos los empleados, o lista vacía si no hay registros
     */
    public List<Empleado> buscarTodos() {
        LOG.info("Consultando todos los empleados");
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "select e from Empleado e";
            TypedQuery<Empleado> query = em.createQuery(jpql, Empleado.class);
            List<Empleado> resultado = query.getResultList();
            LOG.log(Level.INFO, "Se encontraron {0} empleados", resultado.size());
            return resultado;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar todos los empleados: {0}", ex.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }
}