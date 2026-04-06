/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import dtos.IngredienteDTO;
import entidades.Ingrediente;

/**
 *
 * @author Gael Galaviz
 */
public class IngredienteAdapter {
    
    public static IngredienteDTO aDTO(Ingrediente entidad) {
        if (entidad == null) return null;
        
        IngredienteDTO dto = new IngredienteDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setUnidadMedida(entidad.getUnidadMedida());
        dto.setStock(entidad.getStock());
        
        return dto;
    }
    
}
