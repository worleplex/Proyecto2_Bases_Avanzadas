/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package daos;

import entidades.Empleado;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

/**
 * Pruebas unitarias para EmpleadoDAO
 * @author Gael Galaviz
 */
@Disabled("Quitar el Disabled cuando la BD este limpia y lista para pruebas")
public class EmpleadoDAOTest {
    // Singleton 
    @Test
    public void testGetInstance_Bueno() {
        EmpleadoDAO instancia1 = EmpleadoDAO.getInstance();
        EmpleadoDAO instancia2 = EmpleadoDAO.getInstance();
        assertNotNull(instancia1, "La instancia no debe ser nula");
        assertSame(instancia1, instancia2, "Ambas variables deben apuntar exactamente a la misma instancia (Singleton)");
    }

    // buscarPorContras()
    @Test
    public void testBuscarPorContras_Bueno() {
        EmpleadoDAO dao = EmpleadoDAO.getInstance();

        String user = "admin";
        String pass = "1234";

        Empleado resultado = dao.buscarPorContras(user, pass);

        assertNotNull(resultado, "El empleado deberia ser encontrado con credenciales correctas");
        assertEquals(user, resultado.getUsername(), "El username del empleado encontrado debe coincidir");
    }

    @Test
    public void testBuscarPorContras_Malo() {
        EmpleadoDAO dao = EmpleadoDAO.getInstance();

        Empleado resultado = dao.buscarPorContras("inexistente", "error123");
        assertNull(resultado, "Deberia retornar null cuando las credenciales son incorrectas");

        assertThrows(Exception.class, () -> {
            dao.buscarPorContras(null, null);
        }, "Deberia lanzar una excepcion de persistencia");
    }

    // buscarTodos()
    @Test
    public void testBuscarTodos_Bueno() {
        EmpleadoDAO dao = EmpleadoDAO.getInstance();

        List<Empleado> todos = dao.buscarTodos();

        assertNotNull(todos, "La lista devuelta no debe ser nula");
        assertTrue(todos.size() >= 0, "Debe poder retornar una lista de empleados");
    }

}
