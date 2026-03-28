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

    public void guardarCliente(ClienteFrecuenteDTO clienteDTO) throws NegocioException {
        try {
            if (clienteDTO.getTelefono() == null || clienteDTO.getTelefono().trim().isEmpty()) {
                throw new NegocioException("El teléfono es obligatorio para registrar al cliente.");
            }
            
            ClienteFrecuente entidad = ClienteFrecuenteAdapter.dtoAEntidad(clienteDTO);
            clienteDAO.guardar(entidad);
            
        } catch (Exception e) {
            throw new NegocioException("Error al guardar el cliente frecuente: " + e.getMessage(), e);
        }
    }

    public ClienteFrecuenteDTO buscarClientePorId(Long id) throws NegocioException {
        try {
            ClienteFrecuente entidad = clienteDAO.buscarPorId(id);
            return ClienteFrecuenteAdapter.entidadADTO(entidad); 
        } catch (Exception e) {
            throw new NegocioException("Error al buscar el cliente.", e);
        }
    }

    public List<ClienteFrecuenteDTO> obtenerTodosLosClientes() throws NegocioException {
        try {
            List<ClienteFrecuente> entidades = clienteDAO.buscarTodos();
            return ClienteFrecuenteAdapter.listaEntidadADTO(entidades); 
        } catch (Exception e) {
            throw new NegocioException("Error al obtener la lista de clientes.", e);
        }
    }

    public void actualizarPuntos(Long idCliente, Double nuevosPuntos) throws NegocioException {
        try {
            if (nuevosPuntos < 0) {
                throw new NegocioException("Los puntos no pueden ser negativos.");
            }
            clienteDAO.actualizarPuntos(idCliente, nuevosPuntos);
        } catch (Exception e) {
            throw new NegocioException("Error al actualizar los puntos del cliente.", e);
        }
    }

    public void editarCliente(ClienteFrecuenteDTO clienteDTO) throws NegocioException {
        try {
            ClienteFrecuente entidad = ClienteFrecuenteAdapter.dtoAEntidad(clienteDTO);
            clienteDAO.editar(entidad); 
        } catch (Exception e) {
            throw new NegocioException("Error al actualizar el cliente frecuente: " + e.getMessage(), e);
        }
    }

    public void eliminarCliente(Long id) throws NegocioException {
        try {
            boolean eliminado = clienteDAO.eliminar(id);
            if (!eliminado) {
                throw new NegocioException("No se encontró el cliente frecuente con ID: " + id);
            }
        } catch (Exception e) {
            throw new NegocioException("Error al eliminar el cliente frecuente: " + e.getMessage(), e);
        }
    }
}