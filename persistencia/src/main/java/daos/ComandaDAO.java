/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import com.mycompany.persistencia.Persistencia.ConexionBD;
import entidades.Comanda;
import excepciones.PersistenciaException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * DAO para operaciones de acceso a datos de la entidad Comanda.
 *
 * @author Luis Alonso
 */
public class ComandaDAO {

    private static final Logger LOG = Logger.getLogger(ComandaDAO.class.getName());
    private static ComandaDAO instancia;

    private ComandaDAO() {}

    /**
     * Retorna la única instancia de ComandaDAO.
     *
     * @return instancia única de ComandaDAO
     */
    public static ComandaDAO getInstance() {
        if (instancia == null) {
            instancia = new ComandaDAO();
        }
        return instancia;
    }

    /**
     * Obtiene todas las comandas con sus relaciones cargadas eagerly.
     * Incluye cliente, mesa y mesero para el reporte.
     *
     * @return lista de todas las comandas
     * @throws PersistenciaException si ocurre un error en la consulta
     */
    public List<Comanda> obtenerComandasParaReporte() throws PersistenciaException {
        LOG.info("Obteniendo todas las comandas para reporte");
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "select c from Comanda c "
                    + "left join fetch c.cliente "
                    + "left join fetch c.mesa "
                    + "left join fetch c.mesero "
                    + "order by c.fechaHora desc";
            List<Comanda> resultado = em.createQuery(jpql, Comanda.class).getResultList();
            LOG.log(Level.INFO, "Se obtuvieron {0} comandas", resultado.size());
            return resultado;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener comandas: {0}", e.getMessage());
            throw new PersistenciaException("Error al obtener comandas: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene comandas filtradas por rango de fechas, mesa, estado y cliente.
     * Los parámetros nulos o vacíos se ignoran en el filtro.
     *
     * @param fechaInicio fecha de inicio del rango (puede ser null)
     * @param fechaFin fecha fin del rango (puede ser null)
     * @param numeroMesa número de mesa a filtrar (puede ser null o vacío)
     * @param estado estado de la comanda a filtrar (puede ser null o vacío)
     * @param nombreCliente nombre parcial del cliente (puede ser null o vacío)
     * @return lista de comandas que cumplen los filtros
     * @throws PersistenciaException si ocurre un error en la consulta
     */
    public List<Comanda> obtenerComandasFiltradas(
            LocalDate fechaInicio, LocalDate fechaFin,
            String numeroMesa, String estado, String nombreCliente)
            throws PersistenciaException {

        LOG.log(Level.INFO, "Buscando comandas filtradas: fechaInicio={0}, fechaFin={1}, mesa={2}, estado={3}, cliente={4}",
                new Object[]{fechaInicio, fechaFin, numeroMesa, estado, nombreCliente});
        EntityManager em = ConexionBD.crearConexion();
        try {
            StringBuilder jpql = new StringBuilder(
                "select c from Comanda c "
                + "left join fetch c.cliente "
                + "left join fetch c.mesa "
                + "left join fetch c.mesero "
                + "where 1=1 ");

            if (fechaInicio != null) {
                jpql.append("and c.fechaHora >= :fechaInicio ");
            }
            if (fechaFin != null) {
                jpql.append("and c.fechaHora < :fechaFin ");
            }
            if (numeroMesa != null && !numeroMesa.trim().isEmpty()) {
                jpql.append("and c.mesa.numero = :mesa ");
            }
            if (estado != null && !estado.trim().isEmpty()) {
                jpql.append("and c.estadoComanda = :estado ");
            }
            if (nombreCliente != null && !nombreCliente.trim().isEmpty()) {
                jpql.append("and lower(c.cliente.nombres) like lower(:cliente) ");
            }
            jpql.append("order by c.fechaHora desc");

            TypedQuery<Comanda> query = em.createQuery(jpql.toString(), Comanda.class);

            if (fechaInicio != null) {
                query.setParameter("fechaInicio", fechaInicio.atStartOfDay());
            }
            if (fechaFin != null) {
                query.setParameter("fechaFin", fechaFin.plusDays(1).atStartOfDay());
            }
            if (numeroMesa != null && !numeroMesa.trim().isEmpty()) {
                query.setParameter("mesa", numeroMesa.trim());
            }
            if (estado != null && !estado.trim().isEmpty()) {
                query.setParameter("estado", entidades.EstadoComanda.valueOf(estado.toUpperCase()));
            }
            if (nombreCliente != null && !nombreCliente.trim().isEmpty()) {
                query.setParameter("cliente", "%" + nombreCliente.trim() + "%");
            }

            List<Comanda> resultado = query.getResultList();
            LOG.log(Level.INFO, "Se encontraron {0} comandas con los filtros aplicados", resultado.size());
            return resultado;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al filtrar comandas: {0}", e.getMessage());
            throw new PersistenciaException("Error al filtrar comandas: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}