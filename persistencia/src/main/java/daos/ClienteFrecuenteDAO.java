/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import conexion.ConexionBD;
import entidades.ClienteFrecuente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Gael Galaviz
 * Clase encargada de la persistencia de clientes
 * con beneficios de lealtad.
 */
public class ClienteFrecuenteDAO {
    
    private static ClienteFrecuenteDAO instancia;
    
    private ClienteFrecuenteDAO() {
    }
    
    public static ClienteFrecuenteDAO getInstance() {
        if (instancia == null) {
            instancia = new ClienteFrecuenteDAO();
        }
        return instancia;
    }
    

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
            throw e;
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
            em.getTransaction().begin();
            ClienteFrecuente cf = em.find(ClienteFrecuente.class, id);
            
            if (cf == null) {
                em.getTransaction().rollback(); 
                return false;
            }
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
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * Busca clientes frecuentes filtrando dinámicamente por nombre, teléfono
     * y mínimo de visitas. Los parámetros nulos o vacíos se ignoran.
     * La BD hace el filtrado, no Java.
     *
     * @param nombre nombre parcial del cliente (puede ser null o vacío)
     * @param telefono teléfono parcial (puede ser null o vacío)
     * @param correo correo parcial (puede ser null o vacío)
     * @return lista de clientes frecuentes que cumplen los filtros
     */
    public List<ClienteFrecuente> buscarFiltrados(String nombre, String telefono, String correo) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            StringBuilder jpql = new StringBuilder(
                "select cf from ClienteFrecuente cf where 1=1 ");

            if (nombre != null && !nombre.trim().isEmpty()) {
                jpql.append("and lower(cf.nombres) like lower(:nombre) ");
            }
            if (telefono != null && !telefono.trim().isEmpty()) {
                jpql.append("and cf.telefono like :telefono ");
            }
            if (correo != null && !correo.trim().isEmpty()) {
                jpql.append("and lower(cf.correo) like lower(:correo) ");
            }
            jpql.append("order by cf.nombres asc");

            TypedQuery<ClienteFrecuente> query = em.createQuery(jpql.toString(), ClienteFrecuente.class);

            if (nombre != null && !nombre.trim().isEmpty()) {
                query.setParameter("nombre", "%" + nombre.trim() + "%");
            }
            if (telefono != null && !telefono.trim().isEmpty()) {
                query.setParameter("telefono", "%" + telefono.trim() + "%");
            }
            if (correo != null && !correo.trim().isEmpty()) {
                query.setParameter("correo", "%" + correo.trim() + "%");
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    
    /**
     * Busca clientes frecuentes filtrando también por número mínimo de visitas.
     * Las visitas se calculan contando las comandas asociadas al cliente en BD.
     *
     * @param nombre nombre parcial a filtrar
     * @param minimoVisitas número mínimo de visitas
     * @return lista de clientes que cumplen los filtros
     */
    public List<ClienteFrecuente> buscarFiltradosConVisitas(String nombre, int minimoVisitas) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            StringBuilder jpql = new StringBuilder(
                "select cf from ClienteFrecuente cf where 1=1 ");

            if (nombre != null && !nombre.trim().isEmpty()) {
                jpql.append("and lower(cf.nombres) like lower(:nombre) ");
            }
            if (minimoVisitas > 0) {
                jpql.append("and (select count(c) from Comanda c where c.cliente.id = cf.id) >= :visitas ");
            }
            jpql.append("order by cf.nombres asc");

            TypedQuery<ClienteFrecuente> query = em.createQuery(jpql.toString(), ClienteFrecuente.class);

            if (nombre != null && !nombre.trim().isEmpty()) {
                query.setParameter("nombre", "%" + nombre.trim() + "%");
            }
            if (minimoVisitas > 0) {
                query.setParameter("visitas", (long) minimoVisitas);
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    
    /**
     * Obtiene datos de reporte de clientes frecuentes con visitas,
     * total gastado y fecha de última comanda calculados en BD.
     *
     * @param nombre filtro de nombre
     * @param minimoVisitas mínimo de visitas
     * @return lista de Object[] con [ClienteFrecuente, visitas, totalGastado, fechaUltima]
     */
    public List<Object[]> buscarDatosReporteClientes(String nombre, int minimoVisitas) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            StringBuilder jpql = new StringBuilder(
                "select cf, "
                + "(select count(c) from Comanda c where c.cliente.id = cf.id), "
                + "(select coalesce(sum(c2.total), 0) from Comanda c2 where c2.cliente.id = cf.id), "
                + "(select max(c3.fechaHora) from Comanda c3 where c3.cliente.id = cf.id) "
                + "from ClienteFrecuente cf where 1=1 ");

            if (nombre != null && !nombre.trim().isEmpty()) {
                jpql.append("and lower(cf.nombres) like lower(:nombre) ");
            }
            if (minimoVisitas > 0) {
                jpql.append("having (select count(c) from Comanda c where c.cliente.id = cf.id) >= :visitas ");
            }
            jpql.append("order by cf.nombres asc");

            TypedQuery<Object[]> query = em.createQuery(jpql.toString(), Object[].class);
            if (nombre != null && !nombre.trim().isEmpty()) {
                query.setParameter("nombre", "%" + nombre.trim() + "%");
            }
            if (minimoVisitas > 0) {
                query.setParameter("visitas", (long) minimoVisitas);
            }
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
}   