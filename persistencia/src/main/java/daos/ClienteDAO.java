/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import com.mycompany.persistencia.Persistencia.ConexionBD;
import entidades.Cliente;
import entidades.ClienteFrecuente;
import excepciones.PersistenciaException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Gael Galaviz
 * Clase encargada de la persistencia base de clientes.
 * 
 */
public class ClienteDAO {
    private static final Logger LOG = Logger.getLogger(ClienteDAO.class.getName());
    // singleton
    private static ClienteDAO instancia;

    public ClienteDAO() {
    }

    public static ClienteDAO getInstance() {
        if (instancia == null) {
            instancia = new ClienteDAO();
        }
        return instancia;
    }
    
    /**
     * Guardar un cliente a la base de datos
     * @param cliente Cliente a guardar.
     */
    public void guardar(Cliente cliente){
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;

        } finally {
            em.close();
        }
        
        
    }
    /**
     * Elimina un cliente por su id.
     *
     * @param id Id del cliente a eliminar.
     * @return true si se elimino, false si no existe.
     */
    public boolean eliminar(Long id) {
        EntityManager em = ConexionBD.crearConexion();

        try {
            Cliente cliente = em.find(Cliente.class, id);

            if (cliente == null) {
                return false;
            }

            em.getTransaction().begin();
            em.remove(cliente);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
        
    }
    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param cliente Cliente con datos actualizados.
     * @return Cliente actualizado.
     */
    public Cliente editar(Cliente cliente) {
        EntityManager em = ConexionBD.crearConexion();

        try {
            em.getTransaction().begin();
            Cliente clienteActualizado = em.merge(cliente);
            em.getTransaction().commit();
            return clienteActualizado;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * metodo para buscar al cliente por ID
     * @param id
     * @return 
     */
    public Cliente buscarPorId(Long id) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }
    
    /**
     * metodo dinamico de busqueda
     * @param nombre
     * @param telefonoEncriptado
     * @param correo
     * @return
     * @throws PersistenciaException 
     */
    public List<Cliente> buscarClientesFiltrados(String nombre, String telefonoEncriptado, String correo) throws PersistenciaException {
        LOG.log(Level.INFO, "Buscando clientes con filtros dinámicos");
        EntityManager em = ConexionBD.crearConexion();
        try {
            StringBuilder jpql = new StringBuilder("SELECT c FROM Cliente c WHERE 1=1 ");

            if (nombre != null && !nombre.trim().isEmpty()) {
                jpql.append("AND LOWER(c.nombres) LIKE LOWER(:nombre) ");
            }
            if (correo != null && !correo.trim().isEmpty()) {
                jpql.append("AND LOWER(c.correo) LIKE LOWER(:correo) ");
            }
            if (telefonoEncriptado != null && !telefonoEncriptado.trim().isEmpty()) {
                jpql.append("AND c.telefono = :telefono "); // Búsqueda exacta del Hash
            }
            
            jpql.append("ORDER BY c.nombres ASC");

            TypedQuery<Cliente> query = em.createQuery(jpql.toString(), Cliente.class);

            if (nombre != null && !nombre.trim().isEmpty()) {
                query.setParameter("nombre", "%" + nombre.trim() + "%");
            }
            if (correo != null && !correo.trim().isEmpty()) {
                query.setParameter("correo", "%" + correo.trim() + "%");
            }
            if (telefonoEncriptado != null && !telefonoEncriptado.trim().isEmpty()) {
                query.setParameter("telefono", telefonoEncriptado);
            }

            List<Cliente> resultados = query.getResultList();
            LOG.log(Level.INFO, "DAO: Se encontraron {0} clientes.", resultados.size());
            return resultados;
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al buscar clientes filtrados: {0}", e.getMessage());
            throw new PersistenciaException("Error al buscar clientes: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    
}
