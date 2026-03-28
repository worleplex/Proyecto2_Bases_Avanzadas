package objetosnegocio;

import adaptadores.ClienteAdapter;
import daos.ClienteDAO;
import dtos.ClienteDTO;
import entidades.Cliente;
import excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author julian izaguirre
 */
public class ClienteBO {
    
    private static ClienteBO instancia;
    private final ClienteDAO clienteDAO;

    private ClienteBO() {
        this.clienteDAO = ClienteDAO.getInstance(); 
    }

    public static ClienteBO getInstance() {
        if (instancia == null) {
            instancia = new ClienteBO();
        }
        return instancia;
    }

    public void guardarCliente(ClienteDTO clienteDTO) throws NegocioException {
        try {
            if (clienteDTO.getTelefono() == null || clienteDTO.getTelefono().trim().isEmpty()) {
                throw new NegocioException("El teléfono es obligatorio para registrar al cliente");
            }
            
            Cliente entidad = ClienteAdapter.dtoAEntidad(clienteDTO);
            clienteDAO.guardar(entidad);
            
        } catch (Exception e) {
            throw new NegocioException("Error al guardar el cliente general: " + e.getMessage(), e);
        }
    }

    public ClienteDTO buscarClientePorId(Long id) throws NegocioException {
        try {
            Cliente entidad = clienteDAO.buscarPorId(id);
            return ClienteAdapter.entidadADTO(entidad); 
        } catch (Exception e) {
            throw new NegocioException("Error al buscar el cliente general", e);
        }
    }

    public List<ClienteDTO> obtenerTodosLosClientes() throws NegocioException {
        try {
            List<Cliente> entidades = clienteDAO.buscarTodos();
            return ClienteAdapter.listaEntidadADTO(entidades); 
        } catch (Exception e) {
            throw new NegocioException("Error al obtener la lista de clientes generales", e);
        }
    }

    public void editarCliente(ClienteDTO clienteDTO) throws NegocioException {
        try {
            Cliente entidad = ClienteAdapter.dtoAEntidad(clienteDTO);
            clienteDAO.editar(entidad); 
        } catch (Exception e) {
            throw new NegocioException("Error al actualizar el cliente general: " + e.getMessage(), e);
        }
    }

    public void eliminarCliente(Long id) throws NegocioException {
        try {
            boolean eliminado = clienteDAO.eliminar(id);
            if (!eliminado) {
                throw new NegocioException("No se encontró el cliente con ID: " + id);
            }
        } catch (Exception e) {
            throw new NegocioException("Error al eliminar el cliente general: " + e.getMessage(), e);
        }
    }
}