/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosnegocio;

import adaptadores.IngredienteAdapter;
import daos.IngredienteDAO;
import dtos.IngredienteDTO;
import entidades.Ingrediente;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase de Objeto de Negocio (BO) para gestionar la logica de los ingredientes.
 * @author Gael Galaviz
 */
public class IngredienteBO {
    private static final Logger LOG = Logger.getLogger(IngredienteBO.class.getName());
    // solo lo necesario
    private static IngredienteBO instancia;
    private IngredienteDAO ingredienteDAO;

    private IngredienteBO() {
        this.ingredienteDAO = IngredienteDAO.getInstance();
    }

    public static IngredienteBO getInstance() {
        if (instancia == null) {
            instancia = new IngredienteBO();
        }
        return instancia;
    }
    /**
     * Registra un nuevo ingrediente validando que no exista un duplicado con el
     * mismo nombre y unidad de medida.
     * @param ingredienteDTO con la informacion del ingrediente.
     * @return DTO de ingrediente registrado con su id generado.
     * @throws NegocioException si ya existe o hay error en la base.
     */
    public IngredienteDTO registrarIngrediente(IngredienteDTO ingredienteDTO) throws NegocioException {
        try {
            if (ingredienteDAO.existeIngredientes(ingredienteDTO.getNombre(), ingredienteDTO.getUnidadMedida())) {
                throw new NegocioException("Ya existe un ingrediente registrado con el nombre: "
                        + ingredienteDTO.getNombre() + " y la unidad: " + ingredienteDTO.getUnidadMedida());
            }
            
            Ingrediente entidad = IngredienteAdapter.aEntidad(ingredienteDTO);
            ingredienteDAO.guardar(entidad);
            return IngredienteAdapter.aDTO(entidad);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la base de datos al registrar: " + e.getMessage());
        }
    }
    

    /**
     * Elimina un ingrediente.
     * @param id id de ingrediente a eliminar.
     * @throws NegocioException Si el ingrediente esta enlazado con un producto.
     */
    public void eliminarIngrediente(Long id) throws NegocioException {
        try {
            if (ingredienteDAO.estaEnlazadoAProducto(id)) {
                throw new NegocioException("No se puede eliminar: Este ingrediente es parte de la receta de un producto");

            }
            ingredienteDAO.eliminar(id);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se puede eliminar: El ingrediente está en uso o no existe.");
        }
    }
    /**
     * Actualiza el nivel stock de un ingrediente sumando o restando una cantidad.
     * @param id del ingrediente.
     * @param cantidadCambio Cantidad a sumar o restar.
     * @throws NegocioException Si el ingrediente no existe o el stock es negativo.
     */
    public void actualizarStock(Long id, Double cantidadCambio) throws NegocioException{
        try {
            Ingrediente i = ingredienteDAO.obtenerPorId(id);
            if (i == null) {
                throw new NegocioException("Ingrediente no encontrado");
                
            }
            Double nuevoStock = i.getStock() + cantidadCambio;
            if (nuevoStock < 0) {
                throw new NegocioException("El stock no puede ser menor a cero");
                
            }
            i.setStock(nuevoStock);
            ingredienteDAO.actualizar(i);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al actualizar stock");
        }
    }

    public IngredienteDTO obtenerPorID(Long id) throws NegocioException {
        try {
            if (id == null) {
                throw new NegocioException("El ID proporcionado no es valido");
            }
            Ingrediente ingrediente = ingredienteDAO.obtenerPorId(id);
            if (ingrediente == null) {
                throw new NegocioException("No se encontro el ingrediente");
            }
            return IngredienteAdapter.aDTO(ingrediente);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener el ingrediente:" + e.getMessage());
        }
    }
    
    /**
     * Valida y solicita la búsqueda dinámica de ingredientes.
     * Transforma el texto del ComboBox a su Enum correspondiente.
     */
    public List<IngredienteDTO> buscarIngredientesFiltrados(String nombre, String unidadStr) throws NegocioException {
        LOG.log(Level.INFO, "Solicitando ingredientes filtrados");
        try {
            entidades.UnidadMedida unidadEnum = null;
            if (unidadStr != null && !unidadStr.trim().isEmpty() && !unidadStr.equalsIgnoreCase("Todas")) {
                unidadEnum = entidades.UnidadMedida.valueOf(unidadStr.toUpperCase());
            }

            List<Ingrediente> ingredientesBD = ingredienteDAO.buscarIngredientesFiltrados(nombre, unidadEnum);
            List<IngredienteDTO> listaDTOs = new ArrayList<>();

            for (Ingrediente i : ingredientesBD) {
                listaDTOs.add(IngredienteAdapter.aDTO(i));
            }

            return listaDTOs;
            
        } catch (IllegalArgumentException e) {
            LOG.log(Level.SEVERE, "La unidad de medida seleccionada no es válida: {0}", unidadStr);
            throw new NegocioException("Unidad de medida inválida");
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Capa BO Error: {0}", e.getMessage());
            throw new NegocioException("Error al consultar los ingredientes: " + e.getMessage());
        }
    }
}
