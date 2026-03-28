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

    @Override
    public String toString() {
        return "ClienteFrecuenteDTO{" + "ID=" + getId() + ", Nombres=" + getNombres() + ", Telefono=" + getTelefono() + ", Puntos=" + puntos + '}';
    }
}
