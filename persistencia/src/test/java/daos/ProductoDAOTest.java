/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package daos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import entidades.Producto;
import entidades.TipoProducto;
import excepciones.PersistenciaException;
import java.util.List;
import org.junit.jupiter.api.Disabled;

/**
 *
 * @author julian izaguirre
 */
@Disabled("Quitar el Disabled cuando la BD este limpia y lista para pruebas")
public class ProductoDAOTest {
    
    public ProductoDAOTest() {
    }

    @Test
    public void testGetInstance_Bueno() {
        ProductoDAO instancia1 = ProductoDAO.getInstance();
        ProductoDAO instancia2 = ProductoDAO.getInstance();
        
        assertNotNull(instancia1, "La instancia no debe ser nula");
        assertSame(instancia1, instancia2, "El patrón Singleton falló, son instancias diferentes");
    }

    @Test
    public void testGuardar_Bueno() throws PersistenciaException {
        ProductoDAO dao = ProductoDAO.getInstance();
        Producto producto = new Producto("Pizza Test", 150.0, "Prueba JUnit", TipoProducto.PLATILLO, true, "ruta.png");
        
        dao.guardar(producto);
        
        assertNotNull(producto.getId(), "El ID no debería ser nulo después de guardar");
        assertTrue(producto.getId() > 0, "El ID debe ser mayor a cero");
    }

    @Test
    public void testGuardar_Malo() {
        ProductoDAO dao = ProductoDAO.getInstance();
        // Creamos un producto vacío sin los campos obligatorios (nombre, precio, etc.)
        Producto productoMalo = new Producto(); 
        
        // Esperamos que tu DAO atrape el error de SQL y lance tu PersistenciaException
        assertThrows(PersistenciaException.class, () -> {
            dao.guardar(productoMalo);
        }, "Debería lanzar PersistenciaException al intentar guardar un producto inválido");
    }

    @Test
    public void testActualizar_Bueno() throws PersistenciaException {
        ProductoDAO dao = ProductoDAO.getInstance();
        Producto producto = new Producto("Burger Test", 100.0, "JUnit", TipoProducto.PLATILLO, true, "");
        dao.guardar(producto);
        
        // Cambiamos el precio y el estado
        producto.setPrecio(120.0);
        producto.setEstado(false);
        Producto actualizado = dao.actualizar(producto);
        
        assertEquals(120.0, actualizado.getPrecio(), "El precio debió actualizarse a 120.0");
        assertFalse(actualizado.getEstado(), "El estado debió cambiar a Inactivo (false)");
    }

    @Test
    public void testActualizar_Malo() {
        ProductoDAO dao = ProductoDAO.getInstance();
        
        // Mandar un producto nulo a actualizar debería hacer que truene JPA
        assertThrows(Exception.class, () -> {
            dao.actualizar(null);
        }, "Debería lanzar excepción al intentar actualizar un objeto nulo");
    }

    @Test
    public void testObtenerPorNombre_Bueno() throws PersistenciaException {
        ProductoDAO dao = ProductoDAO.getInstance();
        String nombreTest = "Soda Test " + System.currentTimeMillis(); // Nombre único
        Producto producto = new Producto(nombreTest, 25.0, "JUnit", TipoProducto.BEBIDA, true, "");
        dao.guardar(producto);
        
        Producto encontrado = dao.obtenerPorNombre(nombreTest);
        
        assertNotNull(encontrado, "El producto encontrado no debe ser nulo");
        assertEquals(nombreTest, encontrado.getNombre(), "Los nombres deben coincidir");
    }

    @Test
    public void testObtenerPorNombre_Malo() throws PersistenciaException {
        ProductoDAO dao = ProductoDAO.getInstance();
        
        // Buscamos algo que sabemos que no existe
        Producto noEncontrado = dao.obtenerPorNombre("ProductoFantasmaQueNoExiste123");
        
        // Tu método captura el NoResultException y regresa null, así que probamos eso
        assertNull(noEncontrado, "Debería retornar null al buscar un nombre que no existe");
    }

    @Test
    public void testBuscarProductos_Bueno() throws PersistenciaException {
        ProductoDAO dao = ProductoDAO.getInstance();
        dao.guardar(new Producto("Helado Fresa Test", 40.0, "", TipoProducto.POSTRE, true, ""));
        dao.guardar(new Producto("Helado Vainilla Test", 40.0, "", TipoProducto.POSTRE, true, ""));
        
        // Buscamos con una palabra parcial
        List<Producto> resultados = dao.buscarProductos("Helado");
        
        assertNotNull(resultados, "La lista no debe ser nula");
        assertTrue(resultados.size() >= 2, "Debe encontrar al menos los 2 helados de prueba");
    }

    @Test
    public void testObtenerPorId_Bueno() throws PersistenciaException {
        ProductoDAO dao = ProductoDAO.getInstance();
        Producto producto = new Producto("ID Test", 50.0, "JUnit", TipoProducto.PLATILLO, true, "");
        dao.guardar(producto);
        
        Producto encontrado = dao.obtenerPorId(producto.getId());
        
        assertNotNull(encontrado, "No debe regresar null si el ID existe");
        assertEquals(producto.getId(), encontrado.getId(), "Los IDs deben ser exactamente iguales");
    }

    @Test
    public void testObtenerPorId_Malo() throws PersistenciaException {
        ProductoDAO dao = ProductoDAO.getInstance();
        
        Producto noEncontrado = dao.obtenerPorId(999999L); // ID súper alto que no existe
        assertNull(noEncontrado, "Debería regresar null si el ID no existe en la BD");
        
        assertThrows(PersistenciaException.class, () -> {
            dao.obtenerPorId(null);
        }, "Debería lanzar excepción si mandamos un ID nulo");
    }

    @Test
    public void testObtenerTodos_Bueno() throws PersistenciaException {
        ProductoDAO dao = ProductoDAO.getInstance();
        dao.guardar(new Producto("Agua Test", 15.0, "", TipoProducto.BEBIDA, true, ""));
        
        List<Producto> lista = dao.obtenerTodos();
        
        assertNotNull(lista, "La lista general no debe ser nula");
        assertTrue(lista.size() > 0, "La lista debe traer al menos un registro");
    }
    
}
