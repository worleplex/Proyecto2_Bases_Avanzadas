/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosnegocio;

import adaptadores.ProductoAdapter;
import daos.ProductoDAO;
import dtos.ProductoDTO;
import entidades.Producto;
import excepciones.NegocioException;
import excepciones.PersistenciaException;

/**
 *
 * @author julian izaguirre
 */
public class ProductoBO {
    
    private static ProductoBO instancia;
    private ProductoDAO productoDAO;

    private ProductoBO() {
        this.productoDAO = new ProductoDAO();
    }

    public static ProductoBO getInstance() {
        if (instancia == null) {
            instancia = new ProductoBO();
        }
        return instancia;
    }

    public ProductoDTO registrarProducto(ProductoDTO productoDTO) throws NegocioException, PersistenciaException {
        try {
            Producto productoExistente = productoDAO.obtenerPorNombre(productoDTO.getNombre());
            // aqui la validacion que platique con la profe maye
            if (productoExistente != null) {
                throw new NegocioException("Ya existe un producto registrado con el nombre: " + productoDTO.getNombre());
            }
            // obligar a tener al menos 1 ingrediente
            if (productoDTO.getIngredientes() == null || productoDTO.getIngredientes().isEmpty()) {
                throw new NegocioException("El producto debe contener por lo menos un ingrediente asociado");
            }
            // validaciones basicas
            if (productoDTO.getPrecio() == null || productoDTO.getPrecio() <= 0) {
                throw new NegocioException("El precio debe ser mayor a cero");
            }
            
            Producto entidad = ProductoAdapter.aEntidad(productoDTO);
            java.util.List<entidades.ProductoIngrediente> listaIngredientes = new java.util.ArrayList<>();

            for (dtos.ProductoIngredienteDTO piDTO : productoDTO.getIngredientes()) {
                entidades.ProductoIngrediente pi = new entidades.ProductoIngrediente();
                entidades.Ingrediente ingrediente = new entidades.Ingrediente();
                ingrediente.setId(piDTO.getIdIngrediente());
                
                pi.setIngrediente(ingrediente);
                pi.setCantidadRequerida(piDTO.getCantidadRequerida());
                // el dato andaba mal hai
                pi.setProducto(entidad);
                listaIngredientes.add(pi);
            }

            entidad.setIngredientesRequeridos(listaIngredientes);
            Producto productoGuardado = productoDAO.guardar(entidad);

            return ProductoAdapter.aDTO(productoGuardado);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la base de datos: " + e.getMessage());
        }
    }
    
    /**
     * 
     * @param productoDTO
     * @return
     * @throws NegocioException 
     */
    public ProductoDTO editarProducto(ProductoDTO productoDTO) throws NegocioException {
        try {
            if (productoDTO.getId() == null) {
                throw new NegocioException("El ID del producto no puede ser nulo para editar");
            }

            Producto productoExistente = productoDAO.obtenerPorNombre(productoDTO.getNombre());
            if (productoExistente != null && !productoExistente.getId().equals(productoDTO.getId())) {
                throw new NegocioException("Ya existe OTRO producto registrado con el nombre: " + productoDTO.getNombre());
            }
            // validar lo mismo del precio e ingredientes
            if (productoDTO.getPrecio() == null || productoDTO.getPrecio() <= 0) {
                throw new NegocioException("El precio debe ser mayor a cero");
            }
            if (productoDTO.getIngredientes() == null || productoDTO.getIngredientes().isEmpty()) {
                throw new NegocioException("El producto debe contener por lo menos un ingrediente asociado");
            }

            Producto entidad = ProductoAdapter.aEntidad(productoDTO);
            Producto productoActualizado = productoDAO.actualizar(entidad);
            
            return ProductoAdapter.aDTO(productoActualizado);

        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la base de datos al editar: " + e.getMessage());
        }
    }

    /**
     * 
     * @param idProducto
     * @param nuevoEstado
     * @throws NegocioException 
     */
    public void cambiarEstado(Long idProducto, boolean nuevoEstado) throws NegocioException {
        try {
            Producto producto = productoDAO.obtenerPorId(idProducto);
            if (producto == null) {
                throw new NegocioException("Producto no encontrado con el ID: " + idProducto);
            }
            producto.setEstado(nuevoEstado);
            productoDAO.actualizar(producto);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la base de datos al cambiar el estado: " + e.getMessage());
        } catch (Exception e) {
            throw new NegocioException("Error al cambiar el estado del producto: " + e.getMessage());
        }
    }

    /**
     * 
     * @param filtro
     * @return
     * @throws NegocioException 
     */
    public java.util.List<ProductoDTO> buscarProductos(String filtro) throws NegocioException {
        try {
            java.util.List<Producto> listaEntidades = productoDAO.buscarProductos(filtro);
            java.util.List<ProductoDTO> listaDTOs = new java.util.ArrayList<>();
            
            for (Producto p : listaEntidades) {
                listaDTOs.add(ProductoAdapter.aDTO(p));
            }
            return listaDTOs;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar productos: " + e.getMessage());
        }
    }
    
}
