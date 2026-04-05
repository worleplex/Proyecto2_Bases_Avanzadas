/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import dtos.ProductoDTO;
import entidades.Producto;
import entidades.TipoProducto;

/**
 *
 * @author julian izaguirre
 */
public class ProductoAdapter {
    
    public static ProductoDTO aDTO(Producto entidad) {
        if (entidad == null) {
            return null;
        }
        ProductoDTO dto = new ProductoDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setPrecio(entidad.getPrecio());
        dto.setDescripcion(entidad.getDescripcion());
        dto.setTipo(entidad.getTipo()); 
        dto.setEstado(entidad.getEstado());
        dto.setImagen(entidad.getImagen());
        
        return dto;
    }

    public static Producto aEntidad(ProductoDTO dto) {
        if (dto == null) {
            return null;
        }
        Producto entidad = new Producto();
        entidad.setId(dto.getId());
        entidad.setNombre(dto.getNombre());
        entidad.setPrecio(dto.getPrecio());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setTipo(dto.getTipo());
        entidad.setEstado(dto.getEstado());
        entidad.setImagen(dto.getImagen());
        
        return entidad;
    }
    
}
