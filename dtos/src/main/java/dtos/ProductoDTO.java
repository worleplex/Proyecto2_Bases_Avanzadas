/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

import entidades.TipoProducto;
import java.util.List;

/**
 *
 * @author julian izaguirre
 */
public class ProductoDTO {
    private Long id;
    private String nombre;
    private Double precio;
    private String descripcion;
    private TipoProducto tipo;
    private Boolean estado;
    private String imagen;
    private List<ProductoIngredienteDTO> ingredientes;

    public ProductoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoProducto getTipo() {
        return tipo;
    }

    public void setTipo(TipoProducto tipo) {
        this.tipo = tipo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<ProductoIngredienteDTO> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<ProductoIngredienteDTO> ingredientes) {
        this.ingredientes = ingredientes;
    }
    
    
}
