/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author julian izaguirre
 */
@Entity
@Table(name = "meseros")
@PrimaryKeyJoinColumn(name = "id_empleado")
public class Mesero extends Empleado implements Serializable {

    @Column(name = "turno", length = 50)
    private String turno;

    @Column(name = "numero_empleado", length = 20)
    private String numeroEmpleado;

    @OneToMany(mappedBy = "mesero", cascade = CascadeType.ALL)
    private List<Comanda> comandas;

    public Mesero() {
        super();
    }

    public Mesero(String nombreCompleto, String username, String password, String turno, String numeroEmpleado) {
        super(nombreCompleto, username, password);
        this.turno = turno;
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(String numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public List<Comanda> getComandas() {
        return comandas;
    }

    public void setComandas(List<Comanda> comandas) {
        this.comandas = comandas;
    }
}