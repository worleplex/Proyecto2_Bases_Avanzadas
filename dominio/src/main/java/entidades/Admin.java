/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author julian izaguirre
 */
@Entity
@Table(name = "administradores")
@PrimaryKeyJoinColumn(name = "id_empleado")
@DiscriminatorValue("Admin")
public class Admin extends Empleado implements Serializable {

    public Admin() {
        super();
    }
    
    public Admin(String nombreCompleto, String username, String password) {
        super(nombreCompleto, username, password);
    }
}