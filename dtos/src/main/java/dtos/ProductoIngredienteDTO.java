/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 *
 * @author julian izaguirre
 */
public class ProductoIngredienteDTO {
    private Long idIngrediente;
    private String nombreIngrediente;
    private double cantidadRequerida;
    
    public ProductoIngredienteDTO(Long idIngrediente, String nombreIngrediente, Float cantidadRequerida) {
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
        this.cantidadRequerida = cantidadRequerida;
    }

    public Long getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(Long idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    public String getNombreIngrediente() {
        return nombreIngrediente;
    }

    public void setNombreIngrediente(String nombreIngrediente) {
        this.nombreIngrediente = nombreIngrediente;
    }

    public double getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(double cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }
}
