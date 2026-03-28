/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adaptadores;

import dtos.ClienteDTO;
import entidades.Cliente;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author julian izaguirre
 */
public class ClienteAdapter {
    
    public static ClienteDTO entidadADTO(Cliente entidad) {
        if (entidad == null) return null;
        return new ClienteDTO(
                entidad.getId(), 
                entidad.getNombres(), 
                entidad.getApellido_paterno(), // <-- Ojo aquí si no lleva guion bajo
                entidad.getApellido_materno(), // <-- Ojo aquí si no lleva guion bajo
                entidad.getTelefono(), 
                entidad.getCorreo(), 
                entidad.getFechaRegistro()
        );
    }

    public static Cliente dtoAEntidad(ClienteDTO dto) {
        if (dto == null) return null;
        return new Cliente(
                dto.getNombres(), 
                dto.getApellidoPaterno(), 
                dto.getApellidoMaterno(), 
                dto.getTelefono(), 
                dto.getCorreo(), 
                dto.getFechaRegistro()
        );
    }
    
    public static List<ClienteDTO> listaEntidadADTO(List<Cliente> entidades) {
        List<ClienteDTO> dtos = new ArrayList<>();
        if (entidades != null) {
            for (Cliente c : entidades) {
                dtos.add(entidadADTO(c));
            }
        }
        return dtos;
    }
    
    
}
