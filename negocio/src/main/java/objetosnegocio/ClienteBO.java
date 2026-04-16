/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosnegocio;

import adaptadores.ClienteAdapter;
import daos.ClienteDAO;
import dtos.ClienteDTO;
import entidades.Cliente;
import excepciones.NegocioException;
import excepciones.PersistenciaException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Capa de negocio para la gestión de clientes generales.
 * Se encarga de las validaciones, la seguridad (encriptación de teléfonos) 
 * y de conectar las peticiones de las pantallas con la base de datos (DAO).
 *
 * @author julian izaguirre
 */
public class ClienteBO {
    private static final String LLAVE_SECRETA = "MiLlaveSuperSecreta123";
    private static final Logger LOG = Logger.getLogger(ClienteBO.class.getName());
    
    private static ClienteBO instancia;
    private final ClienteDAO clienteDAO;

    /**
     * Constructor privado (Patrón Singleton).
     * Inicializa el trabajador de la base de datos (DAO).
     */
    private ClienteBO() {
        this.clienteDAO = ClienteDAO.getInstance();
    }

    /**
     * Retorna la única instancia de ClienteBO disponible en todo el sistema.
     * Si no existe ninguna, crea una nueva.
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
     * Valida y guarda un nuevo cliente en la base de datos.
     * Intercepta el teléfono del cliente para guardarlo encriptado por seguridad.
     *
     * @param clienteDTO Los datos del cliente capturados en la pantalla.
     * @throws NegocioException Si faltan datos obligatorios o la BD falla.
     */
    public void guardarCliente(ClienteDTO clienteDTO) throws NegocioException {
        LOG.log(Level.INFO, "Iniciando proceso de guardado para el cliente: {0}", clienteDTO.getNombres());
        try {
            if (clienteDTO.getTelefono() == null || clienteDTO.getTelefono().trim().isEmpty()) {
                throw new NegocioException("El teléfono es obligatorio para registrar al cliente");
            }
            LOG.info("Encriptando el número de teléfono del nuevo cliente");
            String telefonoSeguro = encriptarTelefono(clienteDTO.getTelefono().trim());
            clienteDTO.setTelefono(telefonoSeguro);
            Cliente entidad = ClienteAdapter.dtoAEntidad(clienteDTO);
            clienteDAO.guardar(entidad);
            LOG.log(Level.INFO, "Cliente guardado correctamente en la BD");
            
        } catch (NegocioException e) {
            throw e; 
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Excepción crítica al guardar cliente: {0}", e.getMessage());
            throw new NegocioException("Error interno al guardar el cliente: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza los datos de un cliente que ya existe.
     * Vuelve a encriptar el teléfono por si el usuario lo modificó en la pantalla.
     *
     * @param clienteDTO Datos actualizados del cliente.
     * @throws NegocioException Si ocurre un error al actualizar.
     */
    public void editarCliente(ClienteDTO clienteDTO) throws NegocioException {
        LOG.log(Level.INFO, "Iniciando edición para el cliente con ID: {0}", clienteDTO.getId());
        try {
            if (clienteDTO.getTelefono() != null && !clienteDTO.getTelefono().trim().isEmpty()) {
                clienteDTO.setTelefono(encriptarTelefono(clienteDTO.getTelefono().trim()));
            }
            
            Cliente entidad = ClienteAdapter.dtoAEntidad(clienteDTO);
            clienteDAO.editar(entidad);
            LOG.log(Level.INFO, "¡Éxito! Cliente editado correctamente");
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Fallo al editar el cliente: {0}", e.getMessage());
            throw new NegocioException("No se pudo actualizar la información del cliente", e);
        }
    }

    /**
     * Busca un cliente específico mediante su número de ID.
     * Al encontrarlo, abre la "caja fuerte" y desencripta su teléfono para que se lea normal.
     *
     * @param id El identificador único del cliente a buscar.
     * @return ClienteDTO con los datos listos para mostrar en pantalla.
     * @throws NegocioException Si el cliente no existe o la BD falla.
     */
    public ClienteDTO buscarClientePorId(Long id) throws NegocioException {
        LOG.log(Level.INFO, "Buscando cliente exacto por ID: {0}", id);
        try {
            Cliente entidad = clienteDAO.buscarPorId(id);
            if (entidad == null) {
                throw new NegocioException("No se encontró ningún cliente con ese ID");
            }
            
            ClienteDTO dto = ClienteAdapter.entidadADTO(entidad);
            dto.setTelefono(desencriptarTelefono(dto.getTelefono()));
            
            LOG.info("Cliente encontrado y teléfono desencriptado con éxito");
            return dto;
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Fallo al buscar cliente por ID: {0}", e.getMessage());
            throw new NegocioException("Error al consultar el cliente.", e);
        }
    }

    /**
     * Elimina permanentemente a un cliente del sistema.
     *
     * @param id El identificador único del cliente a borrar.
     * @throws NegocioException Si el cliente no existe o tiene relaciones que impiden borrarlo.
     */
    public void eliminarCliente(Long id) throws NegocioException {
        LOG.log(Level.INFO, "Petición para eliminar cliente con ID: {0}", id);
        try {
            boolean eliminado = clienteDAO.eliminar(id);
            if (!eliminado) {
                LOG.log(Level.WARNING, "Intento de eliminar un cliente que no existe (ID: {0})", id);
                throw new NegocioException("No se encontró el cliente tal vez ya fue eliminado");
            }
            LOG.info("Cliente eliminado de la base de datos de forma permanente.");
            
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error crítico de BD al intentar eliminar: {0}", e.getMessage());
            throw new NegocioException("Error al intentar eliminar al cliente", e);
        }
    }

    /**
     * Reemplaza a 'obtenerTodos' y a 'buscarPorNombre'.
     * Si mandas nulls, te regresa todos. Si mandas parámetros, filtra en la BD.
     * * @param nombre El nombre parcial a buscar (ej. "Juan"). Null para ignorar.
     * @param telefonoPlano El teléfono normal (ej. "6441234567"). Null para ignorar.
     * @param correo El correo parcial a buscar. Null para ignorar.
     * @return Lista de clientes DTO ya desencriptados y listos para la tabla.
     * @throws NegocioException Si falla la consulta.
     */
    public List<ClienteDTO> buscarClientesFiltrados(String nombre, String telefonoPlano, String correo) throws NegocioException {
        LOG.info("Solicitando lista de clientes (con o sin filtros)");
        try {
            String telEncriptado = null;
            if (telefonoPlano != null && !telefonoPlano.trim().isEmpty()) {
                telEncriptado = encriptarTelefono(telefonoPlano.trim());
                LOG.info("El usuario busca por teléfono, se ha encriptado el filtro de búsqueda");
            }

            List<Cliente> entidades = clienteDAO.buscarClientesFiltrados(nombre, telEncriptado, correo);
            List<ClienteDTO> dtos = ClienteAdapter.listaEntidadADTO(entidades);

            LOG.info("Desencriptando la lista de teléfonos devuelta por la BD");
            for (ClienteDTO dto : dtos) {
                dto.setTelefono(desencriptarTelefono(dto.getTelefono()));
            }
            
            LOG.log(Level.INFO, "Se van a enviar {0} clientes a la interfaz gráfica", dtos.size());
            return dtos;
            
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "El DAO reportó un error: {0}", e.getMessage());
            throw new NegocioException("Error al obtener la lista de clientes", e);
        }
    }

    /**
     * Encripta una cadena de texto (como un teléfono) usando AES
     * @param telefonoPlano
     * @return el dato puede ser recuperado después con la misma llave
     */
    private String encriptarTelefono(String telefonoPlano) {
        if (telefonoPlano == null || telefonoPlano.isEmpty()) return telefonoPlano;
        try {
            java.security.Key aesKey = new javax.crypto.spec.SecretKeySpec(java.util.Arrays.copyOf(LLAVE_SECRETA.getBytes("UTF-8"), 16), "AES");
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, aesKey);
            return java.util.Base64.getEncoder().encodeToString(cipher.doFinal(telefonoPlano.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException("Error fatal de seguridad al encriptar", e);
        }
    }
    
    /**
     * Toma un código AES de la base de datos y lo devuelve a su estado de texto legible.
     * @param telefonoEncriptado
     * @return evuelve la misma cadena para no romper el sistema
     */
    private String desencriptarTelefono(String telefonoEncriptado) {
        if (telefonoEncriptado == null || telefonoEncriptado.isEmpty()) return telefonoEncriptado;
        try {
            java.security.Key aesKey = new javax.crypto.spec.SecretKeySpec(java.util.Arrays.copyOf(LLAVE_SECRETA.getBytes("UTF-8"), 16), "AES");
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, aesKey);
            return new String(cipher.doFinal(java.util.Base64.getDecoder().decode(telefonoEncriptado)), "UTF-8");
        } catch (Exception e) {
            LOG.warning("Aviso: Se leyó un teléfono que no estaba encriptado en la BD");
            return telefonoEncriptado; 
        }
    }
    
}