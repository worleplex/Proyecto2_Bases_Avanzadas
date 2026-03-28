/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package daos;

import entidades.ClienteFrecuente;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author julian izaguirre
 */
public class ClienteFrecuenteDAOTest {
    
    public ClienteFrecuenteDAOTest() {
    }
    
    @Test
    public void testGetInstance_Bueno() {
        ClienteFrecuenteDAO instancia1 = ClienteFrecuenteDAO.getInstance();
        assertNotNull(instancia1, "La instancia no debe ser nula");
    }

    @Test
    public void testGetInstance_Malo() {
        ClienteFrecuenteDAO instancia1 = ClienteFrecuenteDAO.getInstance();
        ClienteFrecuenteDAO instancia2 = ClienteFrecuenteDAO.getInstance();
        assertSame(instancia1, instancia2, "Fallo del Singleton: Se están creando instancias múltiples");
    }
    

    @Test
    public void testGuardar_Bueno() {
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        ClienteFrecuente cf = new ClienteFrecuente();
        cf.setNombres("Frecuente");
        cf.setApellido_paterno("Test");
        cf.setTelefono("6441112233");
        cf.setFechaRegistro(LocalDate.now());
        
        dao.guardar(cf);
        
        assertNotNull(cf.getId());
        assertTrue(cf.getId() > 0);
    }

    @Test
    public void testGuardar_Malo() {
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        ClienteFrecuente cfMalo = new ClienteFrecuente(); 
        
        assertThrows(Exception.class, () -> {
            dao.guardar(cfMalo);
        });
    }
    
    @Test
    public void testEliminar_Bueno() {
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        ClienteFrecuente cf = new ClienteFrecuente();
        cf.setNombres("Para Eliminar");
        cf.setApellido_paterno("Test");
        cf.setTelefono("0000000000");
        cf.setFechaRegistro(LocalDate.now());
        dao.guardar(cf);
        
        boolean resultado = dao.eliminar(cf.getId());
        assertTrue(resultado, "Debería retornar true al eliminar un registro que sí existe");
    }
    
    @Test
    public void testEliminar_Malo() {
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        
        // Enviar un ID nulo rompe el método em.find() internamente
        assertThrows(IllegalArgumentException.class, () -> {
            dao.eliminar(null);
        }, "Debería lanzar IllegalArgumentException al intentar eliminar con un ID nulo.");
    }

    @Test
    public void testEditar_Bueno() {
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        ClienteFrecuente cf = new ClienteFrecuente();
        cf.setNombres("Original");
        cf.setApellido_paterno("Test");
        cf.setTelefono("123");
        cf.setFechaRegistro(LocalDate.now());
        dao.guardar(cf);
        
        cf.setNombres("Editado");
        ClienteFrecuente actualizado = dao.editar(cf);
        
        assertEquals("Editado", actualizado.getNombres(), "El nombre debió actualizarse");
    }

    @Test
    public void testEditar_Malo() {
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        
        assertThrows(IllegalArgumentException.class, () -> {
            dao.editar(null);
        }, "Debería lanzar IllegalArgumentException al intentar editar un objeto nulo");
    }

    @Test
    public void testBuscarPorId_Bueno() {
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        ClienteFrecuente cf = new ClienteFrecuente();
        cf.setNombres("Buscado");
        cf.setApellido_paterno("Test");
        cf.setTelefono("123");
        cf.setFechaRegistro(LocalDate.now());
        dao.guardar(cf);
        
        ClienteFrecuente encontrado = dao.buscarPorId(cf.getId());
        assertNotNull(encontrado, "Debería encontrar al cliente recién guardado");
        assertEquals("Buscado", encontrado.getNombres());
    }

    @Test
    public void testBuscarPorId_Malo() {
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        
        assertThrows(IllegalArgumentException.class, () -> {
            dao.buscarPorId(null);
        }, "Debería lanzar IllegalArgumentException si el ID a buscar es nulo");
    }

    @Test
    public void testBuscarTodos_Bueno() {
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        // guardamos uno para asegurar q el tolano no venga vacio
        ClienteFrecuente cf = new ClienteFrecuente();
        cf.setNombres("Lista");
        cf.setApellido_paterno("Test");
        cf.setTelefono("123");
        cf.setFechaRegistro(LocalDate.now());
        dao.guardar(cf);
        
        List<ClienteFrecuente> lista = dao.buscarTodos();
        assertNotNull(lista, "La lista retornada no debe ser nula");
        assertFalse(lista.isEmpty(), "La lista debe contener al menos un elemento");
    }

    @Test
    public void testBuscarTodos_Malo() {
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        List<ClienteFrecuente> lista = dao.buscarTodos();
        
        assertNotEquals(null, lista, "La base de datos nunca debería retornar null, sino una lista vacía");
    }
    
    @Test
    public void testActualizarPuntos_Bueno() {
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        ClienteFrecuente cf = new ClienteFrecuente();
        cf.setNombres("Puntos");
        cf.setApellido_paterno("Test");
        cf.setTelefono("123");
        cf.setFechaRegistro(LocalDate.now());
        dao.guardar(cf);
        
        // Actualizamos
        dao.actualizarPuntos(cf.getId(), 500.0);
        ClienteFrecuente cfActualizado = dao.buscarPorId(cf.getId());
        assertEquals(500.0, cfActualizado.getPuntos());
    }

    @Test
    public void testActualizarPuntos_Malo() {
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        
        assertThrows(IllegalArgumentException.class, () -> {
            dao.actualizarPuntos(null, 100.0);
        });
    }
}
