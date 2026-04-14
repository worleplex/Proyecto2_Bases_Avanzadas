/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosnegocio;

import adaptadores.ProductoAdapter;
import static adaptadores.ProductoAdapter.*;

import daos.IngredienteDAO;
import daos.ProductoDAO;
import dtos.ProductoDTO;
import dtos.ProductoIngredienteDTO;
import entidades.Ingrediente;
import entidades.Producto;
import entidades.ProductoIngrediente;
import entidades.TipoProducto;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Capa de negocio para la gestión de productos del restaurante.
 * Aplica validaciones de negocio y delega operaciones al DAO.
 *
 * @author julian izaguirre
 */
public class ProductoBO {

    private static final Logger LOG = Logger.getLogger(ProductoBO.class.getName());
    private static ProductoBO instancia;
    private ProductoDAO productoDAO;
    private IngredienteDAO ingredienteDAO;

    /**
     * Constructor privado para garantizar el patrón Singleton.
     * Inicializa los DAOs de producto e ingrediente.
     */
    private ProductoBO() {
        this.productoDAO = new ProductoDAO();
        this.ingredienteDAO = IngredienteDAO.getInstance();
    }

    /**
     * Retorna la única instancia de ProductoBO.
     * Si no existe, la crea.
     *
     * @return instancia única de ProductoBO
     */
    public static ProductoBO getInstance() {
        if (instancia == null) {
            instancia = new ProductoBO();
        }
        return instancia;
    }

    /**
     * Valida y registra un nuevo producto con sus ingredientes requeridos.
     * Verifica nombre único, precio mayor a cero y al menos un ingrediente.
     *
     * @param productoDTO datos del producto a registrar
     * @return ProductoDTO con el producto guardado e ID generado
     * @throws NegocioException si la validación falla o el nombre ya existe
     * @throws PersistenciaException si ocurre un error al persistir en la BD
     */
    public ProductoDTO registrarProducto(ProductoDTO productoDTO) throws NegocioException, PersistenciaException {
        LOG.log(Level.INFO, "Registrando producto: {0}", productoDTO.getNombre());
        try {
            Producto productoExistente = productoDAO.obtenerPorNombre(productoDTO.getNombre());
            if (productoExistente != null) {
                throw new NegocioException("Ya existe un producto registrado con el nombre: " + productoDTO.getNombre());
            }
            if (productoDTO.getIngredientes() == null || productoDTO.getIngredientes().isEmpty()) {
                throw new NegocioException("El producto debe contener por lo menos un ingrediente asociado");
            }
            if (productoDTO.getPrecio() == null || productoDTO.getPrecio() <= 0) {
                throw new NegocioException("El precio debe ser mayor a cero");
            }

            Producto entidad = aEntidad(productoDTO);

            for (ProductoIngredienteDTO piDTO : productoDTO.getIngredientes()) {
                Ingrediente ingredienteReal = ingredienteDAO.obtenerPorId(piDTO.getIdIngrediente());
                if (ingredienteReal == null) {
                    throw new NegocioException("Error: Un ingrediente seleccionado ya no existe en el sistema");
                }
                ProductoIngrediente pi = new ProductoIngrediente();
                pi.setIngrediente(ingredienteReal);
                pi.setCantidadRequerida(piDTO.getCantidadRequerida());
                entidad.anadirIngredienteRequerido(pi);
            }

            Producto productoGuardado = productoDAO.guardar(entidad);
            LOG.log(Level.INFO, "Producto registrado con ID: {0}", productoGuardado.getId());
            return aDTO(productoGuardado);

        } catch (NegocioException e) {
            throw e;
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Error de persistencia al registrar producto: {0}", e.getMessage());
            throw new NegocioException("Error en la base de datos: " + e.getMessage());
        }
    }

    /**
     * Valida y actualiza los datos de un producto existente incluyendo sus ingredientes.
     * Limpia los ingredientes anteriores y los reemplaza con los del formulario.
     *
     * @param productoDTO datos actualizados del producto
     * @return ProductoDTO con el producto actualizado
     * @throws NegocioException si la validación falla o el producto no existe
     */
    public ProductoDTO editarProducto(ProductoDTO productoDTO) throws NegocioException {
        LOG.log(Level.INFO, "Editando producto con ID: {0}", productoDTO.getId());
        try {
            if (productoDTO.getId() == null) {
                throw new NegocioException("El ID del producto no puede ser nulo para editar");
            }
            Producto productoExistente = productoDAO.obtenerPorNombre(productoDTO.getNombre());
            if (productoExistente != null && !productoExistente.getId().equals(productoDTO.getId())) {
                throw new NegocioException("Ya existe OTRO producto registrado con el nombre: " + productoDTO.getNombre());
            }
            if (productoDTO.getPrecio() == null || productoDTO.getPrecio() <= 0) {
                throw new NegocioException("El precio debe ser mayor a cero");
            }
            if (productoDTO.getIngredientes() == null || productoDTO.getIngredientes().isEmpty()) {
                throw new NegocioException("El producto debe contener por lo menos un ingrediente asociado");
            }

            Producto entidadBD = productoDAO.obtenerPorId(productoDTO.getId());
            if (entidadBD == null) {
                throw new NegocioException("No se encontro el producto a editar en la BD");
            }

            entidadBD.setNombre(productoDTO.getNombre());
            entidadBD.setPrecio(productoDTO.getPrecio());
            entidadBD.setDescripcion(productoDTO.getDescripcion());
            entidadBD.setTipo(productoDTO.getTipo());
            entidadBD.setEstado(productoDTO.getEstado());
            entidadBD.setImagen(productoDTO.getImagen());

            List<ProductoIngrediente> listaBD = entidadBD.getIngredientesRequeridos();
            if (listaBD == null) {
                listaBD = new ArrayList<>();
                entidadBD.setIngredientesRequeridos(listaBD);
            }
            listaBD.clear();

            for (ProductoIngredienteDTO piDTO : productoDTO.getIngredientes()) {
                Ingrediente ingredienteReal = ingredienteDAO.obtenerPorId(piDTO.getIdIngrediente());
                if (ingredienteReal == null) {
                    throw new NegocioException("error un ingrediente seleccionado ya no existe en el sistema");
                }
                ProductoIngrediente pi = new ProductoIngrediente();
                pi.setIngrediente(ingredienteReal);
                pi.setCantidadRequerida(piDTO.getCantidadRequerida());
                entidadBD.anadirIngredienteRequerido(pi);
            }

            Producto productoActualizado = productoDAO.actualizar(entidadBD);
            LOG.log(Level.INFO, "Producto editado correctamente, ID: {0}", productoDTO.getId());
            return ProductoAdapter.aDTO(productoActualizado);

        } catch (NegocioException e) {
            throw e;
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Error de persistencia al editar producto: {0}", e.getMessage());
            throw new NegocioException("Error en la base de datos al editar: " + e.getMessage());
        }
    }

    /**
     * Cambia el estado activo/inactivo de un producto.
     * Útil para dar de baja productos sin eliminarlos.
     *
     * @param idProducto ID del producto a modificar
     * @param nuevoEstado {@code true} para activar, {@code false} para desactivar
     * @throws NegocioException si el producto no existe o ocurre un error
     */
    public void cambiarEstado(Long idProducto, boolean nuevoEstado) throws NegocioException {
        LOG.log(Level.INFO, "Cambiando estado del producto ID {0} a {1}",
            new Object[]{idProducto, nuevoEstado});
        try {
            Producto producto = productoDAO.obtenerPorId(idProducto);
            if (producto == null) {
                throw new NegocioException("Producto no encontrado con el ID: " + idProducto);
            }
            producto.setEstado(nuevoEstado);
            productoDAO.actualizar(producto);
            LOG.log(Level.INFO, "Estado del producto ID {0} actualizado a {1}",
                new Object[]{idProducto, nuevoEstado});
        } catch (NegocioException e) {
            throw e;
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Error al cambiar estado del producto: {0}", e.getMessage());
            throw new NegocioException("Error en la base de datos al cambiar el estado: " + e.getMessage());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error inesperado al cambiar estado: {0}", e.getMessage());
            throw new NegocioException("Error al cambiar el estado del producto: " + e.getMessage());
        }
    }

    /**
     * Busca productos cuyo nombre contenga el filtro proporcionado.
     * Utilizado desde el buscador de productos en la presentación.
     *
     * @param filtro texto parcial para buscar en el nombre del producto
     * @return lista de ProductoDTO que coinciden con el filtro
     * @throws NegocioException si ocurre un error al consultar la BD
     */
    public List<ProductoDTO> buscarProductos(String filtro) throws NegocioException {
        LOG.log(Level.INFO, "Buscando productos con filtro: {0}", filtro);
        try {
            List<Producto> listaEntidades = productoDAO.buscarProductos(filtro);
            List<ProductoDTO> listaDTOs = new ArrayList<>();
            for (Producto p : listaEntidades) {
                listaDTOs.add(aDTO(p));
            }
            LOG.log(Level.INFO, "Se encontraron {0} productos con filtro '{1}'",
                new Object[]{listaDTOs.size(), filtro});
            return listaDTOs;
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Error al buscar productos: {0}", e.getMessage());
            throw new NegocioException("Error al buscar productos: " + e.getMessage());
        }
    }

    public List<ProductoDTO> obtenerTodos() throws NegocioException{
        try{
            List<Producto> listaEntidades = productoDAO.obtenerTodos();
            List<ProductoDTO> listaDTOs = new ArrayList<>();
            for (Producto p : listaEntidades) {
                listaDTOs.add(aDTO(p));
            }

            return listaDTOs;
        }
        catch(PersistenciaException e){
            LOG.log(Level.SEVERE, "Error al buscar productos: ", e.getMessage());
            throw new NegocioException("Error al buscar productos: " + e.getMessage());
        }
    }

    public List<ProductoDTO> buscarPorTipo(TipoProducto tipo) throws NegocioException{
        try{
            List<Producto> listaProductos = productoDAO.obtenerPorTipo(tipo);
            List<ProductoDTO> listaDTOs = new ArrayList<>();

            for(Producto p : listaProductos){
                listaDTOs.add(aDTO(p));
            }

            return listaDTOs;
        }
        catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Error al buscar productos: ", e.getMessage());
            throw new NegocioException("Error al buscar productos: " + e.getMessage());
        }
    }

}