/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import com.mycompany.persistencia.Persistencia.ConexionBD;
import entidades.Producto;
import entidades.TipoProducto;
import excepciones.PersistenciaException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * DAO para operaciones de acceso a datos de la entidad Producto.
 * Incluye búsqueda por nombre con Criteria API y carga eager de ingredientes.
 *
 * @author julian izaguirre
 */
public class ProductoDAO {

    private static final Logger LOG = Logger.getLogger(ProductoDAO.class.getName());
    public static ProductoDAO instancia;

    /**
     * Constructor público requerido por el BO que lo instancia directamente.
     */
    public ProductoDAO() {
    }

    /**
     * Retorna la única instancia de ProductoDAO.
     * Si no existe, la crea.
     *
     * @return instancia única de ProductoDAO
     */
    public static ProductoDAO getInstance() {
        if (instancia == null) {
            instancia = new ProductoDAO();
        }
        return instancia;
    }

    /**
     * Persiste un nuevo producto en la base de datos junto con
     * sus ingredientes requeridos gracias al cascade configurado.
     *
     * @param producto entidad Producto a guardar
     * @return el producto guardado con su ID generado
     * @throws PersistenciaException si ocurre un error al persistir
     */
    public Producto guardar(Producto producto) throws PersistenciaException {
        LOG.log(Level.INFO, "Guardando producto: {0}", producto.getNombre());
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
            LOG.log(Level.INFO, "Producto guardado con ID: {0}", producto.getId());
            return producto;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al guardar producto: {0}", e.getMessage());
            em.getTransaction().rollback();
            throw new PersistenciaException("Error al guardar el producto en la BD: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza un producto existente en la base de datos.
     * También actualiza su lista de ingredientes requeridos por cascade.
     *
     * @param producto entidad Producto con los datos actualizados
     * @return el producto actualizado y sincronizado con la BD
     * @throws PersistenciaException si ocurre un error al actualizar
     */
    public Producto actualizar(Producto producto) throws PersistenciaException {
        LOG.log(Level.INFO, "Actualizando producto ID: {0}", producto.getId());
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Producto actualizado = em.merge(producto);
            em.getTransaction().commit();
            LOG.log(Level.INFO, "Producto actualizado correctamente: {0}", producto.getNombre());
            return actualizado;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al actualizar producto: {0}", e.getMessage());
            em.getTransaction().rollback();
            throw new PersistenciaException("Error al actualizar el producto: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Busca un producto por nombre exacto usando Criteria API.
     * Utilizado para validar nombres duplicados antes de registrar o editar.
     *
     * @param nombre nombre exacto del producto a buscar
     * @return el Producto encontrado, o {@code null} si no existe
     * @throws PersistenciaException si ocurre un error en la consulta
     */
    public Producto obtenerPorNombre(String nombre) throws PersistenciaException {
        LOG.log(Level.INFO, "Buscando producto por nombre: {0}", nombre);
        EntityManager em = ConexionBD.crearConexion();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Producto> query = cb.createQuery(Producto.class);
            Root<Producto> root = query.from(Producto.class);
            query.select(root).where(cb.equal(
                cb.lower(root.get("nombre")),
                nombre.toLowerCase()
            ));
            Producto resultado = em.createQuery(query).getSingleResult();
            LOG.log(Level.INFO, "Producto encontrado: {0}", nombre);
            return resultado;
        } catch (NoResultException e) {
            LOG.log(Level.INFO, "No existe producto con nombre: {0}", nombre);
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al buscar producto por nombre: {0}", e.getMessage());
            throw new PersistenciaException("Error al buscar producto por nombre: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Busca productos por nombre parcial usando Criteria API.
     * Carga los ingredientes de cada producto para evitar LazyInitializationException.
     *
     * @param filtro texto parcial para buscar en el nombre del producto
     * @return lista de productos cuyo nombre contiene el filtro
     * @throws PersistenciaException si ocurre un error en la consulta
     */
    public List<Producto> buscarProductos(String filtro) throws PersistenciaException {
        LOG.log(Level.INFO, "Buscando productos con filtro: {0}", filtro);
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
            LOG.log(Level.INFO, "Se encontraron {0} productos con filtro '{1}'",
                new Object[]{resultado.size(), filtro});
            return resultado;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al buscar productos: {0}", e.getMessage());
            throw new PersistenciaException("Error al buscar productos: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene un producto por su ID con sus ingredientes cargados eagerly
     * mediante JPQL con LEFT JOIN FETCH.
     *
     * @param id identificador único del producto
     * @return el Producto encontrado con ingredientes cargados, o {@code null} si no existe
     * @throws PersistenciaException si ocurre un error en la consulta
     */
    public Producto obtenerPorId(Long id) throws PersistenciaException {
        LOG.log(Level.INFO, "Buscando producto por ID: {0}", id);
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "select p from Producto p "
                    + "left join fetch p.ingredientesRequeridos pi "
                    + "left join fetch pi.ingrediente "
                    + "where p.id = :id";
            TypedQuery<Producto> query = em.createQuery(jpql, Producto.class);
            query.setParameter("id", id);
            Producto resultado = query.getSingleResult();
            LOG.log(Level.INFO, "Producto encontrado con ID: {0}", id);
            return resultado;
        } catch (NoResultException e) {
            LOG.log(Level.WARNING, "No existe producto con ID: {0}", id);
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al buscar producto por ID: {0}", e.getMessage());
            throw new PersistenciaException("Error al buscar producto por ID: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Elimina todos los ProductoIngrediente asociados a un producto específico.
     * Útil para limpiar relaciones antes de reinsertarlas en una edición.
     *
     * @param idProducto ID del producto cuyos ingredientes se eliminarán
     * @throws PersistenciaException si ocurre un error al ejecutar el delete
     */
    public void limpiarIngredientesDeProducto(Long idProducto) throws PersistenciaException {
        LOG.log(Level.INFO, "Limpiando ingredientes del producto ID: {0}", idProducto);
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            int eliminados = em.createQuery(
                "delete from ProductoIngrediente pi where pi.producto.id = :id")
                .setParameter("id", idProducto)
                .executeUpdate();
            em.getTransaction().commit();
            LOG.log(Level.INFO, "Se eliminaron {0} ingredientes del producto ID: {1}",
                new Object[]{eliminados, idProducto});
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al limpiar ingredientes: {0}", e.getMessage());
            em.getTransaction().rollback();
            throw new PersistenciaException("Error al limpiar los ingredientes viejos: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    // esto 
    /**
     * Obtiene todos los productos registrados en la BD.
     *
     * @return lista completa de productos
     * @throws PersistenciaException si ocurre un error en la consulta
     */
    public List<Producto> obtenerTodos() throws PersistenciaException {
        LOG.info("Obteniendo todos los productos");
        EntityManager em = ConexionBD.crearConexion();
        try {
            List<Producto> productos = em.createQuery(
                "select p from Producto p", Producto.class).getResultList();
            LOG.log(Level.INFO, "Se obtuvieron {0} productos", productos.size());
            return productos;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener todos los productos: {0}", e.getMessage());
            throw new PersistenciaException("Error al obtener todos los productos: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todos los productos filtrados por tipo.
     *
     * @param tipo tipo de producto a filtrar (PLATILLO, BEBIDA, POSTRE)
     * @return lista de productos del tipo indicado
     * @throws PersistenciaException si ocurre un error en la consulta
     */
    public List<Producto> obtenerPorTipo(TipoProducto tipo) throws PersistenciaException {
        LOG.log(Level.INFO, "Obteniendo productos por tipo: {0}", tipo);
        EntityManager em = ConexionBD.crearConexion();
        try {
            List<Producto> productos = em.createQuery(
                "select p from Producto p where p.tipo = :tipo", Producto.class)
                .setParameter("tipo", tipo)
                .getResultList();
            LOG.log(Level.INFO, "Se encontraron {0} productos de tipo {1}",
                new Object[]{productos.size(), tipo});
            return productos;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener productos por tipo: {0}", e.getMessage());
            throw new PersistenciaException("Error al obtener productos por tipo: " + e.getMessage());
        } finally {
            em.close();
        }
    }

}