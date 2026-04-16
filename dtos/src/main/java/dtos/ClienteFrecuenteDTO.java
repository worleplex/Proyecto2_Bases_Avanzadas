/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;
import java.time.LocalDate;

/**
 *
 * @author julian izaguirre
 */
public class ClienteFrecuenteDTO extends ClienteDTO {
    
    private Double puntos;
    private Long visitas;
    private Double totalGastado;
    private String fechaUltimaComanda;

    public ClienteFrecuenteDTO() {
        super();
    }

    public ClienteFrecuenteDTO(Long id, String nombres, String apellidoPaterno, String apellidoMaterno, String telefono, String correo, LocalDate fechaRegistro, Double puntos) {
        super(id, nombres, apellidoPaterno, apellidoMaterno, telefono, correo, fechaRegistro);
        this.puntos = puntos;
    }

    public Double getPuntos() {
        return puntos;
    }

    public void setPuntos(Double puntos) {
        this.puntos = puntos;
    }

    public Long getVisitas() {
        return visitas;
    }

    public void setVisitas(Long visitas) {
        this.visitas = visitas;
    }

    public Double getTotalGastado() {
        return totalGastado;
    }

    public void setTotalGastado(Double totalGastado) {
        this.totalGastado = totalGastado;
    }

    public String getFechaUltimaComanda() {
        return fechaUltimaComanda;
    }

    public void setFechaUltimaComanda(String fechaUltimaComanda) {
        this.fechaUltimaComanda = fechaUltimaComanda;
    }

    
    
    
}
