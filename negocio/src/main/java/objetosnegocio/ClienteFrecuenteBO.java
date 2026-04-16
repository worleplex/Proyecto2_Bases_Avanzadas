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
import java.util.ArrayList;
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
    private static final String LLAVE_SECRETA = "MiLlaveSuperSecreta123";

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
     * Intercepta el teléfono para encriptarlo antes de guardarlo.
     *
     * @param clienteDTO datos del cliente frecuente a guardar
     * @throws NegocioException si la validación falla o ocurre un error al guardar
     */
    public void guardarCliente(ClienteFrecuenteDTO clienteDTO) throws NegocioException {
        LOG.log(Level.INFO, "Intentando guardar cliente frecuente: {0}", clienteDTO.getNombres());
        try {
            validarDatosCliente(clienteDTO);
            LOG.log(Level.INFO, "Encriptando el teléfono de {0} por seguridad antes de guardarlo en BD.", clienteDTO.getNombres());
            clienteDTO.setTelefono(encriptarTelefono(clienteDTO.getTelefono()));
            
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
     * Intercepta el teléfono para encriptarlo antes de actualizarlo.
     *
     * @param clienteDTO datos actualizados del cliente frecuente
     * @throws NegocioException si la validación falla o ocurre un error al editar
     */
    public void editarCliente(ClienteFrecuenteDTO clienteDTO) throws NegocioException {
        LOG.log(Level.INFO, "Editando cliente frecuente con ID: {0}", clienteDTO.getId());
        try {
            validarDatosCliente(clienteDTO);
            LOG.log(Level.INFO, "Encriptando el teléfono actualizado del cliente ID: {0}", clienteDTO.getId());
            clienteDTO.setTelefono(encriptarTelefono(clienteDTO.getTelefono()));
            
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
     * Intercepta el teléfono encriptado de la BD y lo desencripta para la interfaz.
     *
     * @param id identificador único del cliente frecuente
     * @return ClienteFrecuenteDTO con los datos del cliente encontrado
     * @throws NegocioException si ocurre un error al buscar
     */
    public ClienteFrecuenteDTO buscarClientePorId(Long id) throws NegocioException {
        LOG.log(Level.INFO, "Buscando cliente frecuente con ID: {0}", id);
        try {
            ClienteFrecuente entidad = clienteDAO.buscarPorId(id);
            ClienteFrecuenteDTO dto = ClienteFrecuenteAdapter.entidadADTO(entidad);
            LOG.log(Level.FINE, "Desencriptando teléfono del cliente ID: {0} para la interfaz.", id);
            dto.setTelefono(desencriptarTelefono(dto.getTelefono()));
            
            LOG.log(Level.INFO, "Cliente frecuente encontrado con ID: {0}", id);
            return dto;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al buscar cliente frecuente con ID {0}: {1}", new Object[]{id, e.getMessage()});
            throw new NegocioException("Error al buscar el cliente", e);
        }
    }

    /**
     * Obtiene la lista completa de clientes frecuentes registrados.
     * Desencripta los teléfonos de todos los clientes antes de enviarlos a la tabla.
     *
     * @return lista de ClienteFrecuenteDTO con todos los clientes frecuentes
     * @throws NegocioException si ocurre un error al consultar la BD
     */
    public List<ClienteFrecuenteDTO> obtenerTodosLosClientes() throws NegocioException {
        LOG.info("Obteniendo todos los clientes frecuentes");
        try {
            List<ClienteFrecuente> entidades = clienteDAO.buscarTodos();
            List<ClienteFrecuenteDTO> listaDTOs = ClienteFrecuenteAdapter.listaEntidadADTO(entidades);

            LOG.log(Level.FINE, "Desencriptando la lista de teléfonos de clientes frecuentes...");
            for (ClienteFrecuenteDTO dto : listaDTOs) {
                dto.setTelefono(desencriptarTelefono(dto.getTelefono()));
            }
            
            LOG.log(Level.INFO, "Se obtuvieron {0} clientes frecuentes", entidades.size());
            return listaDTOs;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener lista de clientes frecuentes: {0}", e.getMessage());
            throw new NegocioException("Error al obtener la lista de clientes", e);
        }
    }

    /**
     * Actualiza los puntos de fidelidad de un cliente frecuente.
     */
    public void actualizarPuntos(Long idCliente, Double nuevosPuntos) throws NegocioException {
        LOG.log(Level.INFO, "Actualizando puntos del cliente ID {0} a {1}", new Object[]{idCliente, nuevosPuntos});
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
    
    /**
     * Encripta un número de teléfono en texto plano utilizando el algoritmo AES.
     * Esto permite que el número se guarde de forma segura en la base de datos pero pueda ser recuperado después.
     *
     * @param telefonoPlano El número de teléfono normal 
     * @return El número de teléfono encriptado en Base64, o el mismo texto si es nulo
     */
    private String encriptarTelefono(String telefonoPlano) {
        if (telefonoPlano == null || telefonoPlano.isEmpty()) return telefonoPlano;
        try {
            LOG.log(Level.FINER, "Aplicando algoritmo AES para encriptar teléfono");
            java.security.Key aesKey = new javax.crypto.spec.SecretKeySpec(java.util.Arrays.copyOf(LLAVE_SECRETA.getBytes("UTF-8"), 16), "AES");
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, aesKey);
            byte[] encriptado = cipher.doFinal(telefonoPlano.getBytes("UTF-8"));
            return java.util.Base64.getEncoder().encodeToString(encriptado);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error de seguridad al encriptar teléfono: {0}", e.getMessage());
            throw new RuntimeException("Error al encriptar el teléfono", e);
        }
    }
    
    /**
     * Desencripta un número de teléfono previamente encriptado con AES devolviéndolo a su estado original legible.
     * Se usa cuando la interfaz necesita mostrar los datos al usuario
     *
     * @param telefonoEncriptado La cadena encriptada en Base64 obtenida de la BD
     * @return El número de teléfono original, o el mismo texto si falla la desencriptación (compatibilidad hacia atrás)
     */
    private String desencriptarTelefono(String telefonoEncriptado) {
        if (telefonoEncriptado == null || telefonoEncriptado.isEmpty()) return telefonoEncriptado;
        try {
            java.security.Key aesKey = new javax.crypto.spec.SecretKeySpec(java.util.Arrays.copyOf(LLAVE_SECRETA.getBytes("UTF-8"), 16), "AES");
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, aesKey);
            byte[] desencriptado = cipher.doFinal(java.util.Base64.getDecoder().decode(telefonoEncriptado));
            return new String(desencriptado, "UTF-8");
        } catch (Exception e) {
            LOG.log(Level.WARNING, "No se pudo desencriptar el teléfono (Puede que no estuviera encriptado) devolviendo original");
            return telefonoEncriptado; 
        }
    }
    
    /**
     * Busca clientes frecuentes con filtros dinámicos aplicados en BD.
     * Desencripta los teléfonos antes de devolver la lista.
     *
     * @param nombre nombre parcial a filtrar
     * @param telefono teléfono parcial a filtrar  
     * @param correo correo parcial a filtrar
     * @return lista filtrada de ClienteFrecuenteDTO
     * @throws NegocioException si ocurre un error
     */
    public List<ClienteFrecuenteDTO> buscarFiltrados(String nombre, String telefono, String correo) throws NegocioException {
        LOG.info("Buscando clientes frecuentes con filtros en BD");
        try {
            List<ClienteFrecuente> entidades = clienteDAO.buscarFiltrados(nombre, telefono, correo);
            List<ClienteFrecuenteDTO> listaDTOs = ClienteFrecuenteAdapter.listaEntidadADTO(entidades);
            for (ClienteFrecuenteDTO dto : listaDTOs) {
                dto.setTelefono(desencriptarTelefono(dto.getTelefono()));
            }
            LOG.log(Level.INFO, "Se encontraron {0} clientes con los filtros aplicados", listaDTOs.size());
            return listaDTOs;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al buscar clientes filtrados: {0}", e.getMessage());
            throw new NegocioException("Error al buscar clientes: " + e.getMessage());
        }
    }
    
    /**
     * Busca clientes frecuentes con filtro de nombre y mínimo de visitas.
     * El conteo de visitas se hace en BD con subquery COUNT.
     *
     * @param nombre nombre parcial a filtrar
     * @param minimoVisitas número mínimo de visitas (0 = ignorar)
     * @return lista filtrada con visitas calculadas en BD
     * @throws NegocioException si ocurre un error
     */
    public List<ClienteFrecuenteDTO> buscarFiltradosConVisitas(String nombre, int minimoVisitas) throws NegocioException {
        LOG.log(Level.INFO, "Buscando clientes con nombre={0} y minimoVisitas={1}",
            new Object[]{nombre, minimoVisitas});
        try {
            List<ClienteFrecuente> entidades = clienteDAO.buscarFiltradosConVisitas(nombre, minimoVisitas);
            List<ClienteFrecuenteDTO> listaDTOs = ClienteFrecuenteAdapter.listaEntidadADTO(entidades);
            for (ClienteFrecuenteDTO dto : listaDTOs) {
                dto.setTelefono(desencriptarTelefono(dto.getTelefono()));
            }
            return listaDTOs;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al buscar clientes con visitas: {0}", e.getMessage());
            throw new NegocioException("Error al buscar clientes: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene datos completos para el reporte de clientes con cálculos en BD.
     *
     * @param nombre filtro de nombre
     * @param minimoVisitas mínimo de visitas
     * @return lista de ClienteFrecuenteReporteDTO con todos los datos calculados
     * @throws NegocioException si ocurre un error
     */
    public List<ClienteFrecuenteDTO> obtenerDatosReporteClientes(String nombre, int minimoVisitas) throws NegocioException {
        try {
            List<Object[]> filas = clienteDAO.buscarDatosReporteClientes(nombre, minimoVisitas);
            List<ClienteFrecuenteDTO> resultado = new ArrayList<>();

            for (Object[] fila : filas) {
                ClienteFrecuente cf = (ClienteFrecuente) fila[0];
                Long visitas = fila[1] != null ? (Long) fila[1] : 0L;
                Double totalGastado = fila[2] != null ? (Double) fila[2] : 0.0;
                java.time.LocalDateTime fechaUltima = fila[3] != null ? (java.time.LocalDateTime) fila[3] : null;

                ClienteFrecuenteDTO dto = ClienteFrecuenteAdapter.entidadADTO(cf);
                dto.setTelefono(desencriptarTelefono(dto.getTelefono()));
                dto.setVisitas(visitas);
                dto.setTotalGastado(totalGastado);
                dto.setFechaUltimaComanda(fechaUltima != null
                    ? fechaUltima.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    : "Sin comandas");
                resultado.add(dto);
            }
            return resultado;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener reporte de clientes: {0}", e.getMessage());
            throw new NegocioException("Error al generar reporte de clientes: " + e.getMessage());
        }
    }
}