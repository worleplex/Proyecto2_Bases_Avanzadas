/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package daos;

import entidades.Cliente;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

/**
 *
 * @author julian izaguirre
 */
@Disabled("No correr hasta que los inserts masivos esten listos")
public class ClienteDAOTest {
    
    public ClienteDAOTest() {
    }

    @Test
    public void testGetInstance_Bueno() {
        ClienteDAO instancia1 = ClienteDAO.getInstance();
        ClienteDAO instancia2 = ClienteDAO.getInstance();

        assertNotNull(instancia1, "La instancia no debe ser nula");
        assertSame(instancia1, instancia2, "Ambas variables deben apuntar exactamente a la misma instancia en memoria");
    }

    @Test
    public void testGuardar_Bueno() {
        ClienteDAO dao = ClienteDAO.getInstance();
        Cliente cliente = new Cliente("Test", "JUnit", "Bueno", "6440000000", "test@test.com", LocalDate.now());
        
        dao.guardar(cliente);
        
        assertNotNull(cliente.getId(), "El ID no debería ser nulo después de guardar, la BD debió generar uno");
        assertTrue(cliente.getId() > 0, "El ID generado debe ser mayor a cero");
    }

    @Test
    public void testGuardar_Malo() {
        ClienteDAO dao = ClienteDAO.getInstance();
        Cliente clienteMalo = new Cliente(null, null, null, null, null, null);
        
        assertThrows(Exception.class, () -> {
            dao.guardar(clienteMalo);
        }, "Debería lanzar una excepción al intentar guardar un cliente con datos nulos obligatorios");
    }

    @Test
    public void testBuscarPorId_Bueno() {
        ClienteDAO dao = ClienteDAO.getInstance();
        Cliente cliente = new Cliente("Busqueda", "Exitosa", "JUnit", "6441112222", "busqueda@test.com", LocalDate.now());
        dao.guardar(cliente); 
        
        Cliente encontrado = dao.buscarPorId(cliente.getId());
        
        assertNotNull(encontrado, "El cliente encontrado no debe ser nulo");
        assertEquals("Busqueda", encontrado.getNombres(), "El nombre del cliente extraído debe coincidir con el guardado");
    }

    @Test
    public void testBuscarPorId_Malo() {
        ClienteDAO dao = ClienteDAO.getInstance();

        Cliente noEncontrado = dao.buscarPorId(999999L);
        assertNull(noEncontrado, "Debería retornar null al buscar un ID que no existe en la base de datos");

        assertThrows(IllegalArgumentException.class, () -> {
            dao.buscarPorId(null);
        }, "Buscar un ID nulo debe lanzar IllegalArgumentException");
    }

    @Test
    public void testEditar_Bueno() {
        ClienteDAO dao = ClienteDAO.getInstance();
        Cliente cliente = new Cliente("Editar", "Prueba", "JUnit", "6440000000", "editar@test.com", LocalDate.now());
        dao.guardar(cliente);

        cliente.setTelefono("6449999999");
        Cliente actualizado = dao.editar(cliente);
        
        assertEquals("6449999999", actualizado.getTelefono(), "El teléfono en la base de datos debió actualizarse correctamente");
    }

    @Test
    public void testEditar_Malo() {
        ClienteDAO dao = ClienteDAO.getInstance();

        assertThrows(IllegalArgumentException.class, () -> {
            dao.editar(null);
        }, "Intentar editar un cliente nulo debe lanzar IllegalArgumentException");
    }

    @Test
    public void testEliminar_Bueno() {
        ClienteDAO dao = ClienteDAO.getInstance();
        Cliente cliente = new Cliente("Eliminar", "Prueba", "JUnit", "6440000000", "eliminar@test.com", LocalDate.now());
        dao.guardar(cliente);
        
        Long id = cliente.getId();
        boolean resultado = dao.eliminar(id);

        assertTrue(resultado, "Debería retornar true al eliminar exitosamente");
        assertNull(dao.buscarPorId(id), "Si lo busco de nuevo, el cliente ya no debería existir en la BD");
    }

    @Test
    public void testEliminar_Malo() {
        ClienteDAO dao = ClienteDAO.getInstance();

        boolean resultado = dao.eliminar(999999L);
        assertFalse(resultado, "Debería retornar false al intentar eliminar un ID inexistente");

        assertThrows(IllegalArgumentException.class, () -> {
            dao.eliminar(null);
        }, "Eliminar con ID nulo debe lanzar IllegalArgumentException");
    }

    @Test
    public void testBuscarFiltrados_Bueno() throws Exception {
        ClienteDAO dao = ClienteDAO.getInstance();
        dao.guardar(new Cliente("Julian", "Izaguirre", "Test", "HashSecreto123", "julian@test.com", LocalDate.now()));

        List<Cliente> todos = dao.buscarClientesFiltrados(null, null, null);
        assertNotNull(todos, "La lista no debe ser nula al mandar nulls");
        assertTrue(todos.size() > 0, "La lista debe contener al menos un cliente");

        List<Cliente> filtrados = dao.buscarClientesFiltrados("Juli", null, null);
        assertNotNull(filtrados);
        assertTrue(filtrados.stream().anyMatch(c -> c.getNombres().equals("Julian")), "Debe encontrar a Julian buscando 'Juli'");
    }

    @Test
    public void testBuscarFiltrados_Malo() throws Exception {
        ClienteDAO dao = ClienteDAO.getInstance();
        List<Cliente> resultados = dao.buscarClientesFiltrados("NombreQueNoExisteXYZ", null, "correo@falso.xyz");

        assertNotNull(resultados, "La lista devuelta no debe ser null, debe ser una lista vacía");
        assertEquals(0, resultados.size(), "El tamaño de la lista debe ser 0 al no encontrar coincidencias");
    }
}
