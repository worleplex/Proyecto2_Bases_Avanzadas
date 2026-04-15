/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosnegocio;

import adaptadores.ComandaAdapter;
import daos.ComandaDAO;
import dtos.ReporteComandaDTO;
import entidades.Comanda;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Capa de negocio para la gestión de comandas.
 *
 * @author Luis Alonso
 */
public class ComandaBO {

    private static final Logger LOG = Logger.getLogger(ComandaBO.class.getName());
    private static ComandaBO instancia;
    private final ComandaDAO comandaDAO;

    private ComandaBO() {
        this.comandaDAO = ComandaDAO.getInstance();
    }

    /**
     * Retorna la única instancia de ComandaBO.
     *
     * @return instancia única de ComandaBO
     */
    public static ComandaBO getInstance() {
        if (instancia == null) {
            instancia = new ComandaBO();
        }
        return instancia;
    }

    /**
     * Genera la lista de DTOs de todas las comandas para JasperReports.
     *
     * @return lista de ReporteComandaDTO
     * @throws NegocioException si ocurre un error al obtener los datos
     */
    public List<ReporteComandaDTO> obtenerDatosReporteComandas() throws NegocioException {
        LOG.info("Generando reporte de todas las comandas");
        try {
            List<Comanda> comandasBD = comandaDAO.obtenerComandasParaReporte();
            List<ReporteComandaDTO> listaReporte = new ArrayList<>();
            for (Comanda c : comandasBD) {
                listaReporte.add(ComandaAdapter.aReporteDTO(c));
            }
            LOG.log(Level.INFO, "Reporte generado con {0} registros", listaReporte.size());
            return listaReporte;
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error de BD al generar reporte: {0}", ex.getMessage());
            throw new NegocioException("Error de base de datos al generar el reporte: " + ex.getMessage());
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error inesperado al generar reporte: {0}", ex.getMessage());
            throw new NegocioException("Error inesperado al generar el reporte: " + ex.getMessage());
        }
    }

    /**
     * Genera la lista de DTOs de comandas filtradas por criterios.
     * Los parámetros nulos o vacíos se ignoran.
     *
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin fecha fin del rango
     * @param numeroMesa número de mesa
     * @param estado estado de la comanda
     * @param nombreCliente nombre parcial del cliente
     * @return lista filtrada de ReporteComandaDTO
     * @throws NegocioException si ocurre un error
     */
    public List<ReporteComandaDTO> obtenerComandasFiltradas(LocalDate fechaInicio, LocalDate fechaFin,
            String numeroMesa, String estado, String nombreCliente)
            throws NegocioException {
        LOG.info("Generando reporte de comandas con filtros");
        try {
            List<Comanda> comandasBD = comandaDAO.obtenerComandasFiltradas(
                fechaInicio, fechaFin, numeroMesa, estado, nombreCliente);
            List<ReporteComandaDTO> listaReporte = new ArrayList<>();
            for (Comanda c : comandasBD) {
                listaReporte.add(ComandaAdapter.aReporteDTO(c));
            }
            LOG.log(Level.INFO, "Reporte filtrado con {0} registros", listaReporte.size());
            return listaReporte;
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error de BD al filtrar comandas: {0}", ex.getMessage());
            throw new NegocioException("Error al filtrar comandas: " + ex.getMessage());
        }
    }
}
