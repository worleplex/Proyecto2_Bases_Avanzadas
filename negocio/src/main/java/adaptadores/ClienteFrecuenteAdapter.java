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

    public static ClienteFrecuente dtoAEntidad(ClienteFrecuenteDTO dto) {
        if (dto == null) return null;
        
        ClienteFrecuente entidad = new ClienteFrecuente(
                dto.getNombres(), 
                dto.getApellidoPaterno(), 
                dto.getApellidoMaterno(), 
                dto.getTelefono(), 
                dto.getCorreo(), 
                dto.getFechaRegistro(), 
                dto.getPuntos()
        );
        
        entidad.setId(dto.getId());
        
        return entidad;
    }
    
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