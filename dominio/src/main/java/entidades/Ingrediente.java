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
@Table(name = "ingredientes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nombre", "unidad_medida"})
})
public class Ingrediente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingrediente")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", nullable = false)
    private UnidadMedida unidadMedida;

    @Column(name = "stock", nullable = false)
    private Double stock;

    @Column(name = "imagen", length = 255)
    private String imagen;

    @OneToMany(mappedBy = "ingrediente")
    private List<ProductoIngrediente> productosQueLoUsan;

    // constructores
    public Ingrediente() {
    }

    public Ingrediente(String nombre, UnidadMedida unidadMedida, Double stock, String imagen) {
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
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

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<ProductoIngrediente> getProductosQueLoUsan() {
        return productosQueLoUsan;
    }

    public void setProductosQueLoUsan(List<ProductoIngrediente> productosQueLoUsan) {
        this.productosQueLoUsan = productosQueLoUsan;
    }
    
    
}
