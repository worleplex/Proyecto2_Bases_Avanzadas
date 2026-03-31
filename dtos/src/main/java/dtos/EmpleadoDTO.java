/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 * aver este dao es para lo de iniciar sesion
 * @author julian izaguirre
 */
public class EmpleadoDTO {
    
    private Long id;
    private String nombreCompleto;
    private String password;
    private String username;
    private String rol;
    
    /*
     * constructor vacio necesario para frameworks y serializacion
     */
    public EmpleadoDTO() {
    }

    /*
     * constructor para crear un empleado sin id
     * el id lo asigna la base de datos automaticamente
     */
    public EmpleadoDTO(String nombreCompleto, String password, String username) {
        this.nombreCompleto = nombreCompleto;
        this.password = password;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "EmpleadoDTO{" + "id=" + id + ", nombreCompleto=" + nombreCompleto + ", password=" + password + ", username=" + username + ", rol=" + rol + '}';
    }

    
}
