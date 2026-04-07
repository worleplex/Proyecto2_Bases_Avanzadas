/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import dtos.ProductoDTO;
import dtos.ProductoIngredienteDTO;
import entidades.Producto;
import entidades.ProductoIngrediente;
import entidades.TipoProducto;
import java.util.ArrayList;
import java.util.List;

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
        if (entidad.getIngredientesRequeridos() != null) {
            List<ProductoIngredienteDTO> ingredientesDTO = new ArrayList<>();
            for (ProductoIngrediente pi : entidad.getIngredientesRequeridos()) {
                ProductoIngredienteDTO piDTO = new ProductoIngredienteDTO(
                    pi.getIngrediente().getId(),
                    pi.getIngrediente().getNombre(),
                    pi.getCantidadRequerida().floatValue(),
                    pi.getIngrediente().getUnidadMedida() != null 
                        ? pi.getIngrediente().getUnidadMedida().toString() : "-",
                    pi.getIngrediente().getStock()
                );
                ingredientesDTO.add(piDTO);
            }
            dto.setIngredientes(ingredientesDTO);
        }

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
