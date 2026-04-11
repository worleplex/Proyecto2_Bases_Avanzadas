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
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author julian izaguirre
 */
public class ProductoBO {
    
    private static ProductoBO instancia;
    private ProductoDAO productoDAO;
    private IngredienteDAO ingredienteDAO; 

    private ProductoBO() {
        this.productoDAO = new ProductoDAO();
        this.ingredienteDAO = IngredienteDAO.getInstance();
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
            return aDTO(productoGuardado);

        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la base de datos: " + e.getMessage());
        }
    }
    
    public ProductoDTO editarProducto(ProductoDTO productoDTO) throws NegocioException {
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
                throw new NegocioException("No se encontró el producto a editar en la BD");
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
                    throw new NegocioException("Error: Un ingrediente seleccionado ya no existe en el sistema");
                }

                ProductoIngrediente pi = new ProductoIngrediente();
                pi.setIngrediente(ingredienteReal);
                pi.setCantidadRequerida(piDTO.getCantidadRequerida());
                
                entidadBD.anadirIngredienteRequerido(pi);
            }

            Producto productoActualizado = productoDAO.actualizar(entidadBD);
            return ProductoAdapter.aDTO(productoActualizado);

        } catch (PersistenciaException e) {
            throw new NegocioException("Error en la base de datos al editar: " + e.getMessage());
        }
    }

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

    public List<ProductoDTO> buscarProductos(String filtro) throws NegocioException {
        try {
            List<Producto> listaEntidades = productoDAO.buscarProductos(filtro);
            List<ProductoDTO> listaDTOs = new ArrayList<>();
            
            for (Producto p : listaEntidades) {
                listaDTOs.add(aDTO(p));
            }
            return listaDTOs;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al buscar productos: " + e.getMessage());
        }
    }
}