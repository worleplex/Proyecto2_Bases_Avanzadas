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
import javax.persistence.EntityManager;

/**
 *
 * @author Gael Galaviz
 * Clase encargada de la persistencia base de clientes.
 * 
 */
public class ClienteDAO {
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
     * Busca un cliente por su id.
     *
     * @param id Id del cliente.
     * @return Cliente encontrado o null si no existe.
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
     * Recupera todos los clientes de la base de datos.
     */
    public List<Cliente> buscarTodos() {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT c FROM Cliente c";
            return em.createQuery(jpql, Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }

    public ClienteFrecuente buscarPorNombre(String nombre) throws PersistenciaException{
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT c FROM Cliente c WHERE c.nombres = :nombre";
            return em.createQuery(jpql, ClienteFrecuente.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar el cliente: " + e.getMessage());
        }
        finally {
            em.close();
        }
    }
    
    
}
