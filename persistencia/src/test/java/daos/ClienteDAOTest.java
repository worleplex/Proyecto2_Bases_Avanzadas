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

/**
 *
 * @author julian izaguirre
 */
public class ClienteDAOTest {
    
    public ClienteDAOTest() {
    }

    @Test
    public void testGetInstance_Bueno() {
        ClienteDAO instancia1 = ClienteDAO.getInstance();
        ClienteDAO instancia2 = ClienteDAO.getInstance();
        
        // Verifica que no sea nulo y que el Singleton funcione 
        assertNotNull(instancia1);
        assertSame(instancia1, instancia2);
    }

    @Test
    public void testGuardar_Bueno() {
        ClienteDAO dao = ClienteDAO.getInstance();
        Cliente cliente = new Cliente("Test", "JUnit", "Bueno", "6440000000", "test@test.com", LocalDate.now());
        
        dao.guardar(cliente);
        
        assertNotNull(cliente.getId(), "El ID no debería ser nulo después de guardar");
        assertTrue(cliente.getId() > 0);
    }

    @Test
    public void testGuardar_Malo() {
        ClienteDAO dao = ClienteDAO.getInstance();
        // creamos un cliente con datos nulos
        Cliente clienteMalo = new Cliente(null, null, null, null, null, null);
        assertThrows(Exception.class, () -> {
            dao.guardar(clienteMalo);
        }, "Debería lanzar una excepción al guardar un cliente con datos nulos obligatorios");
    }

    @Test
    public void testBuscarPorId_Bueno() {
        ClienteDAO dao = ClienteDAO.getInstance();
        Cliente cliente = new Cliente("Busqueda", "Exitosa", "JUnit", "6441112222", "busqueda@test.com", LocalDate.now());
        dao.guardar(cliente); // Lo guardamos primero
        
        Cliente encontrado = dao.buscarPorId(cliente.getId());
        
        assertNotNull(encontrado, "El cliente encontrado no debe ser nulo");
        assertEquals("Busqueda", encontrado.getNombres());
    }

    @Test
    public void testBuscarPorId_Malo() {
        ClienteDAO dao = ClienteDAO.getInstance();
        
        Cliente noEncontrado = dao.buscarPorId(999999L);
        assertNull(noEncontrado, "Debería retornar null al buscar un ID que no existe");
        assertThrows(IllegalArgumentException.class, () -> {
            dao.buscarPorId(null);
        });
    }

    @Test
    public void testEditar_Bueno() {
        ClienteDAO dao = ClienteDAO.getInstance();
        Cliente cliente = new Cliente("Editar", "Prueba", "JUnit", "6440000000", "editar@test.com", LocalDate.now());
        dao.guardar(cliente);
        // Cambiamos el teléfono
        cliente.setTelefono("6449999999");
        Cliente actualizado = dao.editar(cliente);
        
        assertEquals("6449999999", actualizado.getTelefono(), "El teléfono debió actualizarse");
    }

    @Test
    public void testEditar_Malo() {
        ClienteDAO dao = ClienteDAO.getInstance();
        
        // Intentar editar mandando un objeto nulo debería hacer que truene JPA 
        assertThrows(IllegalArgumentException.class, () -> {
            dao.editar(null);
        });
    }

    @Test
    public void testEliminar_Bueno() {
        ClienteDAO dao = ClienteDAO.getInstance();
        Cliente cliente = new Cliente("Eliminar", "Prueba", "JUnit", "6440000000", "eliminar@test.com", LocalDate.now());
        dao.guardar(cliente);
        
        Long id = cliente.getId();
        boolean resultado = dao.eliminar(id);
        
        // Verificamos que devuelva true y que ya no exista
        assertTrue(resultado, "Debería retornar true al eliminar exitosamente");
        assertNull(dao.buscarPorId(id), "El cliente ya no debería existir en la BD");
    }

    @Test
    public void testEliminar_Malo() {
        ClienteDAO dao = ClienteDAO.getInstance();
        boolean resultado = dao.eliminar(999999L);
        assertFalse(resultado, "Debería retornar false al intentar eliminar un ID inexistente");
        // intentar eliminar un ID nulo hace que truene el EntityManager
        assertThrows(IllegalArgumentException.class, () -> {
            dao.eliminar(null);
        });
    }

    @Test
    public void testBuscarTodos_Bueno() {
        ClienteDAO dao = ClienteDAO.getInstance();
        dao.guardar(new Cliente("Lista", "Test", "", "123", "lista@test.com", LocalDate.now()));
        
        List<Cliente> lista = dao.buscarTodos();
        
        assertNotNull(lista, "La lista no debe ser nula");
        assertTrue(lista.size() > 0, "La lista debe contener al menos un cliente");
    }
}
