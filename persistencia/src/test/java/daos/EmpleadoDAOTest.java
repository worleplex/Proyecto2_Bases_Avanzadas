package daos;

import entidades.Empleado;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmpleadoDAOTest {
    EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    @Test
    public void testBuscarPorContrasRetornaEmpleado() {
        Empleado resultado = empleadoDAO.buscarPorContras("admin", "1234");
        assertNotNull("El empleado no debe ser nulo", resultado);
    }

    @Test
    public void testBuscarPorContrasCredencialesInvalidas() {
        Empleado resultado = empleadoDAO.buscarPorContras("noexiste", "wrongpass");
        assertNull("Debe retornar null con credenciales inválidas", resultado);
    }

    @Test
    public void testBuscarTodosRetornaLista() {
        List<Empleado> resultado = empleadoDAO.buscarTodos();
        assertNotNull("La lista no debe ser nula", resultado);
    }

    @Test
    public void testBuscarTodosListaNoVacia() {
        List<Empleado> resultado = empleadoDAO.buscarTodos();
        assertTrue(resultado.isEmpty(), "La lista no debe estar vacía");
    }
}
