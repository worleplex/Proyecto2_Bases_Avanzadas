/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import conexion.ConexionBD;
import entidades.ClienteFrecuente;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Gael Galaviz
 * Clase encargada de la persistencia de clientes
 * con beneficios de lealtad.
 */
public class ClienteFrecuenteDAO {
    /**
     * Guarda un nuevo cliente frecuente en la base de datos.
     * @param cliente Guardar.
     */
    public void guardar(ClienteFrecuente cliente){
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        
        
    }
    /**
     * Elimina a un cliente frecuente del sistema de lealtad.
     *
     * @param id unico del cliente frecuente.
     * @return true si se eliminó, false si no se encontro.
     */
    public boolean eliminar(Long id) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            ClienteFrecuente cf = em.find(ClienteFrecuente.class, id);
            if (cf == null) {
                return false;
            }

            em.getTransaction().begin();
            em.remove(cf);
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
     * Actualiza la informacion de un ClienteFrecuente, incluyendo sus puntos.
     *
     * @param cliente Objeto con los datos modificados.
     * @return El socio actualizado.
     */
    public ClienteFrecuente editar(ClienteFrecuente cliente) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            ClienteFrecuente actualizado = em.merge(cliente);
            em.getTransaction().commit();
            return actualizado;
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
     * Busca un cliente frecuente por su ID.
     * @param id dentificador unico del cliente frecuente.
     * @return cliente frecuente encontrado o null si no existe.
     */
    public ClienteFrecuente buscarPorId(Long id) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            return em.find(ClienteFrecuente.class, id);
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * Obtiene la lista completa de clientes frecuentes. Sirve para mostrar
     * todos los socios en la tabla del modulo.
     *
     * @return Lista de objetos ClienteFrecuente.
     */
    public List<ClienteFrecuente> buscarTodos() {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT cf FROM ClienteFrecuente cf";
            return em.createQuery(jpql, ClienteFrecuente.class).getResultList();

        } finally {
            em.close();
        }
    }



    /**
     * 
     * @param idCliente Identificador unico del cliente a buscar
     * @param nuevosPuntos Nuava cantidad de puntos asignar
     */
    public void actualizarPuntos(Long idCliente, Double nuevosPuntos){
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            ClienteFrecuente cf = em.find(ClienteFrecuente.class, idCliente);
            if(cf != null){
                cf.setPuntos(nuevosPuntos);
                em.merge(cf);
            }
            em.getTransaction().commit();
            } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
        
    }
}   