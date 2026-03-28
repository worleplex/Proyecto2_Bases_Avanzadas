/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import dtos.ClienteFrecuenteDTO;
import entidades.ClienteFrecuente;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author julian izaguirre
 */
public class ClienteFrecuenteAdapter {

    public static ClienteFrecuenteDTO entidadADTO(ClienteFrecuente entidad) {
        if (entidad == null) return null;
        
        return new ClienteFrecuenteDTO(
                entidad.getId(), 
                entidad.getNombres(), 
                entidad.getApellido_paterno(),
                entidad.getApellido_materno(), 
                entidad.getTelefono(), 
                entidad.getCorreo(), 
                entidad.getFechaRegistro(), 
                entidad.getPuntos()
        );
    }

    // Este lo necesitamos para cuando el usuario le dé clic en "Guardar"
    public static ClienteFrecuente dtoAEntidad(ClienteFrecuenteDTO dto) {
        if (dto == null) return null;
        
        return new ClienteFrecuente(
                dto.getNombres(), 
                dto.getApellidoPaterno(), 
                dto.getApellidoMaterno(), 
                dto.getTelefono(), 
                dto.getCorreo(), 
                dto.getFechaRegistro(), 
                dto.getPuntos()
        );
    }
    
    // Equivalente al de tu profe: Convierte listas
    public static List<ClienteFrecuenteDTO> listaEntidadADTO(List<ClienteFrecuente> entidades) {
        List<ClienteFrecuenteDTO> dtos = new ArrayList<>();
        if (entidades != null) {
            for (ClienteFrecuente c : entidades) {
                dtos.add(entidadADTO(c));
            }
        }
        return dtos;
    }
}
