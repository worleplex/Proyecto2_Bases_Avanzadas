/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosnegocio;

import adaptadores.ClienteFrecuenteAdapter;
import daos.ClienteFrecuenteDAO;
import dtos.ClienteFrecuenteDTO;
import entidades.ClienteFrecuente;
import excepciones.NegocioException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Capa de negocio para la gestión de clientes frecuentes.
 * Aplica validaciones de formato y delega operaciones al DAO.
 *
 * @author julian izaguirre
 */
public class ClienteFrecuenteBO {

    private static final Logger LOG = Logger.getLogger(ClienteFrecuenteBO.class.getName());
    private static ClienteFrecuenteBO instancia;
    private final ClienteFrecuenteDAO clienteDAO;

    /**
     * Constructor privado para garantizar el patrón Singleton.
     * Inicializa el DAO de cliente frecuente.
     */
    private ClienteFrecuenteBO() {
        this.clienteDAO = ClienteFrecuenteDAO.getInstance();
    }

    /**
     * Retorna la única instancia de ClienteFrecuenteBO.
     * Si no existe, la crea.
     *
     * @return instancia única de ClienteFrecuenteBO
     */
    public static ClienteFrecuenteBO getInstance() {
        if (instancia == null) {
            instancia = new ClienteFrecuenteBO();
        }
        return instancia;
    }

    /**
     * Valida los datos del cliente frecuente.
     * Verifica nombre, apellido paterno, teléfono, correo y puntos.
     *
     * @param dto datos del cliente a validar
     * @throws NegocioException si algún campo es inválido
     */
    private void validarDatosCliente(ClienteFrecuenteDTO dto) throws NegocioException {
        LOG.log(Level.FINE, "Validando datos del cliente: {0}", dto.getNombres());
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
            throw new NegocioException("El formato del correo es invalido, asegurate de incluir el @ y el dominio");
        }
        if (dto.getPuntos() != null && dto.getPuntos() < 0) {
            throw new NegocioException("Los puntos de fidelidad no pueden ser negativos");
        }
    }

    /**
     * Valida y guarda un nuevo cliente frecuente en la base de datos.
     *
     * @param clienteDTO datos del cliente frecuente a guardar
     * @throws NegocioException si la validación falla o ocurre un error al guardar
     */
    public void guardarCliente(ClienteFrecuenteDTO clienteDTO) throws NegocioException {
        LOG.log(Level.INFO, "Intentando guardar cliente frecuente: {0}", clienteDTO.getNombres());
        try {
            validarDatosCliente(clienteDTO);
            ClienteFrecuente entidad = ClienteFrecuenteAdapter.dtoAEntidad(clienteDTO);
            clienteDAO.guardar(entidad);
            LOG.log(Level.INFO, "Cliente frecuente guardado correctamente: {0}", clienteDTO.getNombres());
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al guardar cliente frecuente: {0}", e.getMessage());
            throw new NegocioException("Error al guardar el cliente frecuente: " + e.getMessage(), e);
        }
    }

    /**
     * Valida y actualiza los datos de un cliente frecuente existente.
     *
     * @param clienteDTO datos actualizados del cliente frecuente
     * @throws NegocioException si la validación falla o ocurre un error al editar
     */
    public void editarCliente(ClienteFrecuenteDTO clienteDTO) throws NegocioException {
        LOG.log(Level.INFO, "Editando cliente frecuente con ID: {0}", clienteDTO.getId());
        try {
            validarDatosCliente(clienteDTO);
            ClienteFrecuente entidad = ClienteFrecuenteAdapter.dtoAEntidad(clienteDTO);
            clienteDAO.editar(entidad);
            LOG.log(Level.INFO, "Cliente frecuente editado correctamente, ID: {0}", clienteDTO.getId());
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al editar cliente frecuente: {0}", e.getMessage());
            throw new NegocioException("Error al actualizar el cliente frecuente: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un cliente frecuente por su ID.
     *
     * @param id identificador único del cliente frecuente
     * @return ClienteFrecuenteDTO con los datos del cliente encontrado
     * @throws NegocioException si ocurre un error al buscar
     */
    public ClienteFrecuenteDTO buscarClientePorId(Long id) throws NegocioException {
        LOG.log(Level.INFO, "Buscando cliente frecuente con ID: {0}", id);
        try {
            ClienteFrecuente entidad = clienteDAO.buscarPorId(id);
            LOG.log(Level.INFO, "Cliente frecuente encontrado con ID: {0}", id);
            return ClienteFrecuenteAdapter.entidadADTO(entidad);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al buscar cliente frecuente con ID {0}: {1}",
                new Object[]{id, e.getMessage()});
            throw new NegocioException("Error al buscar el cliente", e);
        }
    }

    /**
     * Obtiene la lista completa de clientes frecuentes registrados.
     *
     * @return lista de ClienteFrecuenteDTO con todos los clientes frecuentes
     * @throws NegocioException si ocurre un error al consultar la BD
     */
    public List<ClienteFrecuenteDTO> obtenerTodosLosClientes() throws NegocioException {
        LOG.info("Obteniendo todos los clientes frecuentes");
        try {
            List<ClienteFrecuente> entidades = clienteDAO.buscarTodos();
            LOG.log(Level.INFO, "Se obtuvieron {0} clientes frecuentes", entidades.size());
            return ClienteFrecuenteAdapter.listaEntidadADTO(entidades);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener lista de clientes frecuentes: {0}", e.getMessage());
            throw new NegocioException("Error al obtener la lista de clientes", e);
        }
    }

    /**
     * Actualiza los puntos de fidelidad de un cliente frecuente.
     * Los puntos no pueden ser negativos.
     *
     * @param idCliente ID del cliente a actualizar
     * @param nuevosPuntos nuevo valor de puntos de fidelidad
     * @throws NegocioException si los puntos son negativos o ocurre un error
     */
    public void actualizarPuntos(Long idCliente, Double nuevosPuntos) throws NegocioException {
        LOG.log(Level.INFO, "Actualizando puntos del cliente ID {0} a {1}",
            new Object[]{idCliente, nuevosPuntos});
        try {
            if (nuevosPuntos < 0) {
                throw new NegocioException("Los puntos no pueden ser negativos");
            }
            clienteDAO.actualizarPuntos(idCliente, nuevosPuntos);
            LOG.log(Level.INFO, "Puntos actualizados correctamente para cliente ID: {0}", idCliente);
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al actualizar puntos del cliente: {0}", e.getMessage());
            throw new NegocioException("Error al actualizar los puntos del cliente", e);
        }
    }

    /**
     * Elimina un cliente frecuente por su ID.
     *
     * @param id identificador único del cliente a eliminar
     * @throws NegocioException si no se encuentra el cliente o ocurre un error al eliminar
     */
    public void eliminarCliente(Long id) throws NegocioException {
        LOG.log(Level.INFO, "Eliminando cliente frecuente con ID: {0}", id);
        try {
            boolean eliminado = clienteDAO.eliminar(id);
            if (!eliminado) {
                LOG.log(Level.WARNING, "No se encontro cliente frecuente con ID: {0}", id);
                throw new NegocioException("No se encontro el cliente frecuente con ID: " + id);
            }
            LOG.log(Level.INFO, "Cliente frecuente eliminado correctamente, ID: {0}", id);
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al eliminar cliente frecuente: {0}", e.getMessage());
            throw new NegocioException("Error al eliminar el cliente frecuente: " + e.getMessage(), e);
        }
    }
}