/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import dtos.EmpleadoDTO;
import entidades.Empleado;
import entidades.Mesero;

/**
 *
 * @author julian izaguirre
 */
public class EmpleadoAdapter {
    
    /**
     * 
     * @param entidad
     * @return 
     */
    public static EmpleadoDTO entidadADTO(Empleado entidad) {
        if (entidad == null) {
            return null;
        }
        
        EmpleadoDTO dto = new EmpleadoDTO(entidad.getNombreCompleto(), entidad.getPassword(), entidad.getUsername());
        dto.setId(entidad.getId());
        
        if (entidad instanceof Mesero) {
            dto.setRol("MESERO"); 
        } else {
            dto.setRol("ADMIN");
        }
        return dto;
    }
}
