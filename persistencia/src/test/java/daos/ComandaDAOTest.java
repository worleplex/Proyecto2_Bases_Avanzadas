//package daos;
//
//import dtos.ComandaDTO;
//import entidades.Comanda;
//import entidades.EstadoComanda;
//import excepciones.PersistenciaException;
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//
//public class ComandaDAOTest {
//    private ComandaDAO comandaDAO = new ComandaDAO();
//    @org.junit.Test
//
//
//    @Test
//    public void testObtenerComandasParaReporteRetornaLista() throws PersistenciaException {
//        List<Comanda> resultado = comandaDAO.obtenerComandasParaReporte();
//        assertNotNull("La lista no debe ser nula", resultado);
//    }
//
//    @Test
//    public void testObtenerComandasParaReporteListaNoVacia() throws PersistenciaException {
//        List<Comanda> resultado = comandaDAO.obtenerComandasParaReporte();
//        assertFalse(resultado.isEmpty(), "La lista no debe estar vacía");
//    }
//
//    @Test
//    public void testObtenerComandasFiltradasSinFiltrosRetornaLista() throws PersistenciaException {
//        List<Comanda> resultado = comandaDAO.obtenerComandasFiltradas(null, null, null, null, null);
//        assertNotNull("La lista no debe ser nula", resultado);
//    }
//
//    @Test
//    public void testObtenerComandasFiltradasPorFechas() throws PersistenciaException {
//        LocalDate inicio = LocalDate.of(2024, 1, 1);
//        LocalDate fin = LocalDate.of(2024, 12, 31);
//        List<Comanda> resultado = comandaDAO.obtenerComandasFiltradas(inicio, fin, null, null, null);
//        assertNotNull("La lista filtrada por fechas no debe ser nula", resultado);
//    }
//
//    @Test
//    public void testCrearComandaRetornaComandaCreada() throws PersistenciaException {
//        ComandaDTO comanda = new ComandaDTO(null, LocalDateTime.now(), "OB-20260416-001",
//                EstadoComanda.ABIERTA, 0.0, 1L, 1L, null);
//        ComandaDTO resultado = comandaDAO.crearComanda(comanda);
//        assertNotNull("La comanda creada no debe ser nula", resultado);
//    }
//
//    @Test
//    public void testCrearComandaFolioEsCorrecto() throws PersistenciaException {
//        ComandaDTO comanda = new ComandaDTO(null, LocalDateTime.now(), "OB-20260416-002",
//                EstadoComanda.ABIERTA, 0.0, 1L, 1L, null);
//        ComandaDTO resultado = comandaDAO.crearComanda(comanda);
//        assertEquals("El folio debe coincidir", "OB-20260416-002", resultado.getFolio());
//    }
//
//    @Test
//    public void testModificarComandaRetornaComandaModificada() throws PersistenciaException {
//        ComandaDTO comanda = new ComandaDTO(1L, LocalDateTime.now(), "OB-20260416-001",
//                EstadoComanda.ENTREGADA, 150.0, 1L, 1L, null);
//        ComandaDTO resultado = comandaDAO.modificarComanda(comanda);
//        assertNotNull("La comanda modificada no debe ser nula", resultado);
//    }
//
//    @Test
//    public void testModificarComandaEstadoActualizado() throws PersistenciaException {
//        ComandaDTO comanda = new ComandaDTO(1L, LocalDateTime.now(), "OB-20260416-001",
//                EstadoComanda.CANCELADA, 150.0, 1L, 1L, null);
//        ComandaDTO resultado = comandaDAO.modificarComanda(comanda);
//        assertEquals("El estado debe ser CERRADA", EstadoComanda.CANCELADA, resultado.getEstadoComanda());
//    }
//
//    @Test
//    public void testBuscarPorFolioRetornaComanda() throws PersistenciaException {
//        ComandaDTO resultado = comandaDAO.buscarPorFolio("OB-20260416-001");
//        assertNotNull("La comanda no debe ser nula", resultado);
//    }
//
//    @Test
//    public void testBuscarPorFolioFolioEsCorrecto() throws PersistenciaException {
//        ComandaDTO resultado = comandaDAO.buscarPorFolio("OB-20260416-001");
//        assertEquals("El folio debe coincidir", "OB-20260416-001", resultado.getFolio());
//    }
//
//}
