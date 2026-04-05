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

    public Producto obtenerPorNombre(String nombre) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            TypedQuery<Producto> query = em.createQuery("select p from Producto p where lower(p.nombre) = lower(:nombre)", Producto.class);
            query.setParameter("nombre", nombre);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar producto por nombre: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public List<Producto> buscarProductos(String filtro) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "select p from Producto p where lower(p.nombre) like lower(:filtro)";
            TypedQuery<Producto> query = em.createQuery(jpql, Producto.class);
            query.setParameter("filtro", "%" + filtro + "%");
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar productos: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    public Producto obtenerPorId(Long id) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            return em.find(Producto.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar producto por ID: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
