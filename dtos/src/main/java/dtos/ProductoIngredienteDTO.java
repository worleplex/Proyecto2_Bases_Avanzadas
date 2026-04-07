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
    private Double cantidadRequerida;
    private String unidadMedida;
    private Double stock;

    public ProductoIngredienteDTO() {}

    public ProductoIngredienteDTO(Long idIngrediente, String nombreIngrediente, float cantidadRequerida) {
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
        this.cantidadRequerida = (double) cantidadRequerida;
    }

    public ProductoIngredienteDTO(Long idIngrediente, String nombreIngrediente, float cantidadRequerida, String unidadMedida, Double stock) {
        this.idIngrediente = idIngrediente;
        this.nombreIngrediente = nombreIngrediente;
        this.cantidadRequerida = (double) cantidadRequerida;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
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

    public Double getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(Double cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }
}
