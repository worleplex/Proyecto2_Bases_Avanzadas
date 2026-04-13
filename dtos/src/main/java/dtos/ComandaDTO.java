package dtos;

import entidades.EstadoComanda;
import java.time.LocalDateTime;

/***
 * @author Luis Alonso
 */

public class ComandaDTO {
    private Long id;
    private String folio;
    private LocalDateTime fechaHora;
    private EstadoComanda estadoComanda;
    private Double total;
    private Long idMesa;
    private Long idMesero;
    private Long idCliente;

    public ComandaDTO() {

    }

    public ComandaDTO(Long id, LocalDateTime fechaHora, String folio, EstadoComanda estadoComanda, Double total, Long idMesa, Long idMesero, Long idCliente) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.folio = folio;
        this.estadoComanda = estadoComanda;
        this.total = total;
        this.idMesa = idMesa;
        this.idMesero = idMesero;
        this.idCliente = idCliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public EstadoComanda getEstadoComanda() {
        return estadoComanda;
    }

    public void setEstadoComanda(EstadoComanda estadoComanda) {
        this.estadoComanda = estadoComanda;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(Long idMesa) {
        this.idMesa = idMesa;
    }

    public Long getIdMesero() {
        return idMesero;
    }

    public void setIdMesero(Long idMesero) {
        this.idMesero = idMesero;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
}