/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package daos;

import entidades.Ingrediente;
import entidades.UnidadMedida;
import excepciones.PersistenciaException;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

/**
 * Pruebas unitarias para IngredienteDAO
 * @author Gael Galaviz
 */

@Disabled("Quitar el Disabled cuando la BD este limpia y lista para pruebas")
public class IngredienteDAOTest {
    public IngredienteDAOTest() {
    }

    // Singleton 
    @Test
    public void testGetInstance_Bueno() {
        IngredienteDAO instancia1 = IngredienteDAO.getInstance();
        IngredienteDAO instancia2 = IngredienteDAO.getInstance();
        assertNotNull(instancia1);
        assertSame(instancia1, instancia2, "El patron Singleton fallo");
    }

    // guardar()
    @Test
    public void testGuardar_Bueno() throws PersistenciaException {
        IngredienteDAO dao = IngredienteDAO.getInstance();
        Ingrediente ing = new Ingrediente("Sal de grano " , UnidadMedida.GRAMO, 500.0, "sal.png");

        dao.guardar(ing);

        assertNotNull(ing.getId(), "El ID deberia generarse automaticamente");
        assertTrue(ing.getId() > 0);
    }

    @Test
    public void testGuardar_Malo() {
        IngredienteDAO dao = IngredienteDAO.getInstance();
        Ingrediente ingInvalido = new Ingrediente();
        ingInvalido.setUnidadMedida(UnidadMedida.PIEZA);
        ingInvalido.setStock(10.0);

        assertThrows(PersistenciaException.class, () -> {
            dao.guardar(ingInvalido);
        }, "Deberia lanzar excepcion por falta de campo obligatorio");
    }

    // actualizar() 
    @Test
    public void testActualizar_Bueno() throws PersistenciaException {
        IngredienteDAO dao = IngredienteDAO.getInstance();
        Ingrediente ing = new Ingrediente("Pimienta", UnidadMedida.GRAMO, 100.0, null);
        dao.guardar(ing);

        ing.setStock(250.0);
        ing.setImagen("pimienta_nueva.png");
        dao.actualizar(ing);

        Ingrediente actualizado = dao.obtenerPorId(ing.getId());
        assertEquals(250.0, actualizado.getStock());
        assertEquals("pimienta_nueva.png", actualizado.getImagen());
    }

    @Test
    public void testActualizar_Malo() {
        IngredienteDAO dao = IngredienteDAO.getInstance();
        assertThrows(Exception.class, () -> {
            dao.actualizar(null);
        }, "No se puede actualizar un objeto nulo");
    }

    // eliminar() 
    @Test
    public void testEliminar_Bueno() throws PersistenciaException {
        IngredienteDAO dao = IngredienteDAO.getInstance();
        Ingrediente ing = new Ingrediente("Huevo", UnidadMedida.PIEZA, 1.0, null);
        dao.guardar(ing);

        Long id = ing.getId();
        dao.eliminar(id);

        Ingrediente resultado = dao.obtenerPorId(id);
        assertNull(resultado, "El registro no fue eliminado de la base de datos");
    }

    // obtenerPorId() 
    @Test
    public void testObtenerPorId_Bueno() throws PersistenciaException {
        IngredienteDAO dao = IngredienteDAO.getInstance();
        Ingrediente ing = new Ingrediente("Vino", UnidadMedida.MILILITRO, 10.0, null);
        dao.guardar(ing);

        Ingrediente encontrado = dao.obtenerPorId(ing.getId());
        assertNotNull(encontrado);
        assertEquals(ing.getId(), encontrado.getId());
    }

    @Test
    public void testObtenerPorId_Malo() throws PersistenciaException {
        IngredienteDAO dao = IngredienteDAO.getInstance();
        Ingrediente noExiste = dao.obtenerPorId(-1L); // ID que no existe
        assertNull(noExiste);
    }

    // existeIngredientes()
    @Test
    public void testExisteIngredientes_Bueno() throws PersistenciaException {
        IngredienteDAO dao = IngredienteDAO.getInstance();
        String nombre = "Unico ";
        dao.guardar(new Ingrediente(nombre, UnidadMedida.GRAMO, 100.0, null));

        boolean existe = dao.existeIngredientes(nombre, UnidadMedida.GRAMO);
        assertTrue(existe, "Deberia detectar que el ingrediente ya existe");
    }

    @Test
    public void testExisteIngredientes_Malo() throws PersistenciaException {
        IngredienteDAO dao = IngredienteDAO.getInstance();
        boolean existe = dao.existeIngredientes("Ingrediente que no existe", UnidadMedida.PIEZA);
        assertFalse(existe);
    }

    // estaEnlazadoAProducto() 
    @Test
    public void testEstaEnlazadoAProducto_Bueno() {
        IngredienteDAO dao = IngredienteDAO.getInstance();
        boolean resultado = dao.estaEnlazadoAProducto(1L);
        assertFalse(resultado, "En una base limpia, el ID 1 no deberia estar enlazado");
    }

    // buscarIngredientesFiltrados() 
    @Test
    public void testBuscarIngredientesFiltrados_Bueno() throws PersistenciaException {
        IngredienteDAO dao = IngredienteDAO.getInstance();
        dao.guardar(new Ingrediente("Leche Entera", UnidadMedida.MILILITRO, 1000.0, null));
        dao.guardar(new Ingrediente("Leche Deslactosada", UnidadMedida.MILILITRO, 500.0, null));

        // Filtro por nombre 
        List<Ingrediente> leche = dao.buscarIngredientesFiltrados("Leche", null);
        assertTrue(leche.size() >= 2);
        // Filtro por unidad de medida
        List<Ingrediente> ml = dao.buscarIngredientesFiltrados(null, UnidadMedida.MILILITRO);
        assertNotNull(ml);
        assertTrue(ml.stream().allMatch(i -> i.getUnidadMedida() == UnidadMedida.MILILITRO));
    }

    @Test
    public void testBuscarIngredientesFiltrados_SinResultados() throws PersistenciaException {
        IngredienteDAO dao = IngredienteDAO.getInstance();
        List<Ingrediente> lista = dao.buscarIngredientesFiltrados("Ajo", UnidadMedida.PIEZA);
        assertTrue(lista.isEmpty(), "La lista deberia estar vacia si no hay coincidencias");
    }
}
