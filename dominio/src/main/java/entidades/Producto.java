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
@Table(name = "productos")
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoProducto tipo;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @Column(name = "imagen", length = 255)
    private String imagen;
    
    // cambiar lo del all para despues
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoIngrediente> ingredientesRequeridos;

    @OneToMany(mappedBy = "producto")
    private List<DetalleComanda> detallesComanda;

    public Producto() {
    }

    public Producto(String nombre, Double precio, String descripcion, TipoProducto tipo, Boolean estado, String imagen) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.estado = estado;
        this.imagen = imagen;
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

    public List<ProductoIngrediente> getIngredientesRequeridos() {
        return ingredientesRequeridos;
    }

    public void setIngredientesRequeridos(List<ProductoIngrediente> ingredientesRequeridos) {
        this.ingredientesRequeridos = ingredientesRequeridos;
    }

    public List<DetalleComanda> getDetallesComanda() {
        return detallesComanda;
    }

    public void setDetallesComanda(List<DetalleComanda> detallesComanda) {
        this.detallesComanda = detallesComanda;
    }
    
    
    
}