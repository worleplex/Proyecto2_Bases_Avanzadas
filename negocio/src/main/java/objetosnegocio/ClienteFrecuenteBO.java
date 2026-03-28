package objetosnegocio;

import adaptadores.ClienteFrecuenteAdapter;
import daos.ClienteFrecuenteDAO;
import dtos.ClienteFrecuenteDTO;
import entidades.ClienteFrecuente;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author julian izaguirre
 */
public class ClienteFrecuenteBO {

    private static ClienteFrecuenteBO instancia;
    private final ClienteFrecuenteDAO clienteDAO;

    private ClienteFrecuenteBO() {
        this.clienteDAO = ClienteFrecuenteDAO.getInstance(); 
    }

    public static ClienteFrecuenteBO getInstance() {
        if (instancia == null) {
            instancia = new ClienteFrecuenteBO();
        }
        return instancia;
    }

    private void validarDatosCliente(ClienteFrecuenteDTO dto) throws NegocioException {
        if (dto.getNombres() == null || dto.getNombres().trim().isEmpty()) {
            throw new NegocioException("El nombre del cliente no puede estar vacio");
        }
        if (dto.getApellidoPaterno() == null || dto.getApellidoPaterno().trim().isEmpty()) {
            throw new NegocioException("El apellido paterno es obligatorio");
        }
        if (dto.getTelefono() == null || !dto.getTelefono().matches("\\d{10}")) {
            throw new NegocioException("El teléfono debe contener exactamente 10 numeros");
        }

        String reglaCorreo = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (dto.getCorreo() == null || dto.getCorreo().trim().isEmpty()) {
            throw new NegocioException("El correo electrónico es obligatorio para los clientes frecuentes");
        } else if (!dto.getCorreo().matches(reglaCorreo)) {
            throw new NegocioException("El formato del correo es inválido, asegurate de incluir el @ y el dominio");
        }
        
        if (dto.getPuntos() != null && dto.getPuntos() < 0) {
            throw new NegocioException("Los puntos de fidelidad no pueden ser negativos");
        }
    }


    public void guardarCliente(ClienteFrecuenteDTO clienteDTO) throws NegocioException {
        try {
            validarDatosCliente(clienteDTO);
            
            ClienteFrecuente entidad = ClienteFrecuenteAdapter.dtoAEntidad(clienteDTO);
            clienteDAO.guardar(entidad);
            
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("Error al guardar el cliente frecuente: " + e.getMessage(), e);
        }
    }

    public void editarCliente(ClienteFrecuenteDTO clienteDTO) throws NegocioException {
        try {
            validarDatosCliente(clienteDTO);
            
            ClienteFrecuente entidad = ClienteFrecuenteAdapter.dtoAEntidad(clienteDTO);
            clienteDAO.editar(entidad); 
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("Error al actualizar el cliente frecuente: " + e.getMessage(), e);
        }
    }

    public ClienteFrecuenteDTO buscarClientePorId(Long id) throws NegocioException {
        try {
            ClienteFrecuente entidad = clienteDAO.buscarPorId(id);
            return ClienteFrecuenteAdapter.entidadADTO(entidad); 
        } catch (Exception e) {
            throw new NegocioException("Error al buscar el cliente", e);
        }
    }

    public List<ClienteFrecuenteDTO> obtenerTodosLosClientes() throws NegocioException {
        try {
            List<ClienteFrecuente> entidades = clienteDAO.buscarTodos();
            return ClienteFrecuenteAdapter.listaEntidadADTO(entidades); 
        } catch (Exception e) {
            throw new NegocioException("Error al obtener la lista de clientes", e);
        }
    }

    public void actualizarPuntos(Long idCliente, Double nuevosPuntos) throws NegocioException {
        try {
            if (nuevosPuntos < 0) {
                throw new NegocioException("Los puntos no pueden ser negativos");
            }
            clienteDAO.actualizarPuntos(idCliente, nuevosPuntos);
        } catch (Exception e) {
            throw new NegocioException("Error al actualizar los puntos del cliente", e);
        }
    }

    public void eliminarCliente(Long id) throws NegocioException {
        try {
            boolean eliminado = clienteDAO.eliminar(id);
            if (!eliminado) {
                throw new NegocioException("No se encontro el cliente frecuente con ID: " + id);
            }
        } catch (Exception e) {
            throw new NegocioException("Error al eliminar el cliente frecuente: " + e.getMessage(), e);
        }
    }
}