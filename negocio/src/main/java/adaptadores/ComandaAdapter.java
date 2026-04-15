/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import dtos.ReporteComandaDTO;
import entidades.Comanda;
import java.time.format.DateTimeFormatter;

/**
 * Adaptador para convertir entidades Comanda a DTOs de reporte.
 *
 * @author Luis Alonso
 */
public class ComandaAdapter {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Convierte una entidad Comanda a un ReporteComandaDTO con datos en texto plano.
     *
     * @param c entidad Comanda a convertir
     * @return ReporteComandaDTO con los datos formateados para el reporte
     */
    public static ReporteComandaDTO aReporteDTO(Comanda c) {
        ReporteComandaDTO dto = new ReporteComandaDTO();
        dto.setFolio(c.getFolio());
        dto.setFechaHora(c.getFechaHora() != null ? c.getFechaHora().format(FORMATO) : "-");
        dto.setEstado(c.getEstadoComanda() != null ? c.getEstadoComanda().toString() : "-");
        dto.setTotal(c.getTotal());

        if (c.getCliente() != null && c.getCliente().getNombres() != null) {
            dto.setNombreCliente(c.getCliente().getNombres() + " " +
                (c.getCliente().getApellido_paterno() != null ? c.getCliente().getApellido_paterno() : ""));
        } else {
            dto.setNombreCliente("Cliente General");
        }

        dto.setNumeroMesa(c.getMesa() != null ? c.getMesa().getNumero() : "-");
        
        if (c.getMesero() != null) {
            dto.setNombreMesero(c.getMesero().getNombreCompleto());
        } else {
            dto.setNombreMesero("-");
        }

        return dto;
    }
}