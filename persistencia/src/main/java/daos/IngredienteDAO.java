/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import com.mycompany.persistencia.Persistencia.ConexionBD;
import entidades.Ingrediente;
import entidades.UnidadMedida;
import excepciones.PersistenciaException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Clase de DAO para la entidad Ingrediente. 
 * Implementa el patron Singleton para gestionar la persistencia de ingredientes.
 * @author Gael Galaviz
 */
public class IngredienteDAO {

    // singleton
    private static IngredienteDAO instancia;

    private IngredienteDAO() {
    }

    public static IngredienteDAO getInstance() {
        if (instancia == null) {
            instancia = new IngredienteDAO();
        }
        return instancia;
    }

    /**
     * Guardar un ingrediente a la base de datos.
     *
     * @param ingrediente Ingrediente a guardar.
     * @throws PersistenciaException
     */
    public void guardar(Ingrediente ingrediente) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(ingrediente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al guardar el ingrediente:" + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Actualizar un ingrediente
     *
     * @param ingrediente Ingrediente con datos actualizado(Esto es para el stock)
     * @throws PersistenciaException
     */
    public void actualizar(Ingrediente ingrediente) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.merge(ingrediente);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar ingrediente:" + e.getMessage());
        } finally {
            em.close();
        }
    }
    /**
     * Elimina un ingrediente de la base de datos.
     * @param id
     * @throws PersistenciaException si ocurre un error al eliminar
     */
    public void eliminar(Long id) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Ingrediente ingrediente = em.find(Ingrediente.class, id);
            if (ingrediente != null) {
                em.remove(ingrediente);

            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al eliminar el ingrediente:" + e.getMessage());
        } finally {
            em.close();

        }
    }

    // puse solo lo necesario demietras
    /**
     *
     * @param id
     * @return
     * @throws PersistenciaException
     */
    public Ingrediente obtenerPorId(Long id) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            return em.find(Ingrediente.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar ingrediente por ID: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Valida si ya existe un ingrediente con el mismo nombre y unidad de medida
     *
     * @param nombre Nombre del ingrediente a validar.
     * @param unidad Unidad de medida a validar.
     * @return true si ya existe, false en caso contrario.
     * @throws PersistenciaException Si ocurre un error en la consulta.
     */
    public boolean existeIngredientes(String nombre, UnidadMedida unidad) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT COUNT(i) FROM Ingrediente i WHERE LOWER(i.nombre) = "
                    + "LOWER(:nombre) AND i.unidadMedida = :unidad";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("nombre", nombre);
            query.setParameter("unidad", unidad);
            Long conteo = query.getSingleResult();
            return conteo != null && conteo > 0;

        } catch (Exception e) {
            throw new PersistenciaException("Error al validar existencias:" + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }

        }
    }
    /**
     * Verifica si un ingrediente esta siendo utilizado por algun producto.
     * @param idIngrediente id de ingrediente a verificar.
     * @return true si el ingrediente si esta enlazado con un producto, false en
     * caso contrario.
     */
    public boolean estaEnlazadoAProducto(Long idIngrediente) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT COUNT(pi) FROM ProductoIngrediente pi WHERE pi.ingrediente.id = :id";
            Long conteo = em.createQuery(jpql, Long.class).setParameter("id", idIngrediente).getSingleResult();
            return conteo > 0;

        } finally {
            em.close();
        }
    }

    /**
     *
     * @param nombreParcial
     * @return
     * @throws PersistenciaException
     */
    public List<Ingrediente> buscarPorNombre(String nombreParcial, String unidad) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "select i from Ingrediente i where lower(i.nombre) like lower(:nombre)"
                    + " and (:unidad is null or i.unidadMedida = :unidad)";
            
            TypedQuery<Ingrediente> query = em.createQuery(jpql, Ingrediente.class);
            query.setParameter("nombre", "%" + nombreParcial + "%");
            
            if (unidad == null || unidad.trim().isEmpty()) {
                query.setParameter("unidad", null); 
            } else {
                query.setParameter("unidad", entidades.UnidadMedida.valueOf(unidad.toUpperCase()));
            }
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar ingredientes por nombre: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
