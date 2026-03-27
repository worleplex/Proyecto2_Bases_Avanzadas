/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 *
 * @author Gael Galaviz
 */
@Entity
@Table(name = "clientes_frecuentes")
@PrimaryKeyJoinColumn(name = "id_cliente")
public class ClienteFrecuente extends Cliente implements Serializable{
    @Column(name = "puntos", nullable = false)
    private double puntos;

    public ClienteFrecuente() {
        super();
    }

    public ClienteFrecuente(String nombres, String apellido_paterno, String apellido_materno,
            String telefono, String correo, LocalDate fechaRegistro, double puntos) {
        super(nombres, apellido_paterno, apellido_materno, telefono, correo, fechaRegistro);
        this.puntos = puntos;
    }

    public double getPuntos() {
        return puntos;
    }

    public void setPuntos(double puntos) {
        this.puntos = puntos;
    }   
}
