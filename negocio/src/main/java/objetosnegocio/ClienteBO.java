/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosnegocio;

import adaptadores.ClienteAdapter;
import adaptadores.ClienteFrecuenteAdapter;
import daos.ClienteDAO;
import dtos.ClienteDTO;
import dtos.ClienteFrecuenteDTO;
import entidades.Cliente;
import entidades.ClienteFrecuente;
import excepciones.NegocioException;
import excepciones.PersistenciaException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Capa de negocio para la gestión de clientes generales.
 * Aplica validaciones básicas antes de delegar al DAO.
 *
 * @author julian izaguirre
 */
public class ClienteBO {

    private static final Logger LOG = Logger.getLogger(ClienteBO.class.getName());
    private static ClienteBO instancia;
    private final ClienteDAO clienteDAO;

    /**
     * Constructor privado para garantizar el patrón Singleton.
     * Inicializa el DAO de cliente.
     */
    private ClienteBO() {
        this.clienteDAO = ClienteDAO.getInstance();
    }

    /**
     * Retorna la única instancia de ClienteBO.
     * Si no existe, la crea.
     *
     * @return instancia única de ClienteBO
     */
    public static ClienteBO getInstance() {
        if (instancia == null) {
            instancia = new ClienteBO();
        }
        return instancia;
    }

    /**
     * Valida y guarda un nuevo cliente general en la base de datos.
     * Requiere que el teléfono no sea nulo ni vacío.
     *
     * @param clienteDTO datos del cliente a guardar
     * @throws NegocioException si el teléfono es inválido o ocurre un error al guardar
     */
    public void guardarCliente(ClienteDTO clienteDTO) throws NegocioException {
        LOG.log(Level.INFO, "Intentando guardar cliente general: {0}", clienteDTO.getNombres());
        try {
            if (clienteDTO.getTelefono() == null || clienteDTO.getTelefono().trim().isEmpty()) {
                throw new NegocioException("El teléfono es obligatorio para registrar al cliente");
            }
            Cliente entidad = ClienteAdapter.dtoAEntidad(clienteDTO);
            clienteDAO.guardar(entidad);
            LOG.log(Level.INFO, "Cliente general guardado correctamente: {0}", clienteDTO.getNombres());
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al guardar cliente general: {0}", e.getMessage());
            throw new NegocioException("Error al guardar el cliente general: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un cliente general por su ID.
     *
     * @param id identificador único del cliente
     * @return ClienteDTO con los datos del cliente encontrado
     * @throws NegocioException si no se encuentra el cliente o ocurre un error
     */
    public ClienteDTO buscarClientePorId(Long id) throws NegocioException {
        LOG.log(Level.INFO, "Buscando cliente general con ID: {0}", id);
        try {
            Cliente entidad = clienteDAO.buscarPorId(id);
            LOG.log(Level.INFO, "Cliente general encontrado con ID: {0}", id);
            return ClienteAdapter.entidadADTO(entidad);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al buscar cliente general con ID {0}: {1}",
                new Object[]{id, e.getMessage()});
            throw new NegocioException("Error al buscar el cliente general", e);
        }
    }

    /**
     * Obtiene la lista completa de clientes generales registrados.
     *
     * @return lista de ClienteDTO con todos los clientes
     * @throws NegocioException si ocurre un error al consultar la BD
     */
    public List<ClienteDTO> obtenerTodosLosClientes() throws NegocioException {
        LOG.info("Obteniendo todos los clientes generales");
        try {
            List<Cliente> entidades = clienteDAO.buscarTodos();
            LOG.log(Level.INFO, "Se obtuvieron {0} clientes generales", entidades.size());
            return ClienteAdapter.listaEntidadADTO(entidades);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener lista de clientes generales: {0}", e.getMessage());
            throw new NegocioException("Error al obtener la lista de clientes generales", e);
        }
    }

    /**
     * Actualiza los datos de un cliente general existente.
     *
     * @param clienteDTO datos actualizados del cliente
     * @throws NegocioException si ocurre un error al actualizar
     */
    public void editarCliente(ClienteDTO clienteDTO) throws NegocioException {
        LOG.log(Level.INFO, "Editando cliente general con ID: {0}", clienteDTO.getId());
        try {
            Cliente entidad = ClienteAdapter.dtoAEntidad(clienteDTO);
            clienteDAO.editar(entidad);
            LOG.log(Level.INFO, "Cliente general editado correctamente, ID: {0}", clienteDTO.getId());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al editar cliente general: {0}", e.getMessage());
            throw new NegocioException("Error al actualizar el cliente general: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un cliente general por su ID.
     *
     * @param id identificador único del cliente a eliminar
     * @throws NegocioException si no se encuentra el cliente o ocurre un error al eliminar
     */
    public void eliminarCliente(Long id) throws NegocioException {
        LOG.log(Level.INFO, "Eliminando cliente general con ID: {0}", id);
        try {
            boolean eliminado = clienteDAO.eliminar(id);
            if (!eliminado) {
                LOG.log(Level.WARNING, "No se encontró cliente general con ID: {0}", id);
                throw new NegocioException("No se encontró el cliente con ID: " + id);
            }
            LOG.log(Level.INFO, "Cliente general eliminado correctamente, ID: {0}", id);
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al eliminar cliente general: {0}", e.getMessage());
            throw new NegocioException("Error al eliminar el cliente general: " + e.getMessage(), e);
        }
    }

    public ClienteFrecuenteDTO buscarCliente(String nombre) throws PersistenciaException {
        LOG.log(Level.INFO, "Buscando al cliente con el nombre: {0}", nombre);
        try {
            if(nombre.isEmpty()){
                LOG.log(Level.SEVERE, "Para buscar a un cliente debe escribir un nombre");
            }

            ClienteFrecuente clienteEncontrar = clienteDAO.buscarPorNombre(nombre);
            ClienteFrecuenteDTO clienteEncontrado = ClienteFrecuenteAdapter.entidadADTO(clienteEncontrar);

            return clienteEncontrado;
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Error al buscar cliente general: {0}", e.getMessage());
            throw new PersistenciaException("Error al buscar al cliente" + e.getMessage());
        }


    }
}