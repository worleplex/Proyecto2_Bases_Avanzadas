/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import com.mycompany.persistencia.Persistencia.ConexionBD;
import entidades.Producto;
import excepciones.PersistenciaException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author julian izaguirre
 */
public class ProductoDAO {
    // el singleton
    public static ProductoDAO instancia;
    
    public ProductoDAO() {
    }
    
    public static ProductoDAO getInstance() {
        if (instancia == null) {
            instancia = new ProductoDAO();
        }
        return instancia;
    }
    
    public Producto guardar(Producto producto) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
            return producto;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new PersistenciaException("Error al guardar el producto en la BD: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public Producto actualizar(Producto producto) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Producto actualizado = em.merge(producto);
            em.getTransaction().commit();
            return actualizado;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new PersistenciaException("Error al actualizar el producto: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    /**
     * cumpliendo
     * @param nombre
     * @return
     * @throws PersistenciaException 
     */
    public Producto obtenerPorNombre(String nombre) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> root = query.from(Producto.class);

            query.select(root)
                 .where(cb.equal(
                     cb.lower(root.get("nombre")),
                     nombre.toLowerCase()
                 ));

            return em.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar producto por nombre: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * @param filtro
     * @return
     * @throws PersistenciaException 
     */
    public List<Producto> buscarProductos(String filtro) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> root = query.from(Producto.class);

            query.select(root).where(cb.like(
                    cb.lower(root.get("nombre")),
                    "%" + filtro.toLowerCase() + "%"
                 ));

            query.distinct(true);

            List<Producto> resultado = em.createQuery(query).getResultList();

            for (Producto p : resultado) {
                p.getIngredientesRequeridos().size();
            }

            return resultado;
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar productos: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    /**
     * 
     * @param id
     * @return
     * @throws PersistenciaException 
     */
    public Producto obtenerPorId(Long id) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT p FROM Producto p " +
                          "LEFT JOIN FETCH p.ingredientesRequeridos pi " +
                          "LEFT JOIN FETCH pi.ingrediente " +
                          "WHERE p.id = :id";
            TypedQuery<Producto> query = em.createQuery(jpql, Producto.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar producto por ID: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void limpiarIngredientesDeProducto(Long idProducto) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.createQuery("delete from ProductoIngrediente pi where pi.producto.id = :id")
              .setParameter("id", idProducto)
              .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new PersistenciaException("Error al limpiar los ingredientes viejos: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}