/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import com.mycompany.persistencia.Persistencia.ConexionBD;
import entidades.Ingrediente;
import excepciones.PersistenciaException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Gael Galaviz
 */
public class IngredienteDAO {
    // singleton
    private static IngredienteDAO instancia;

    private IngredienteDAO() {}

    public static IngredienteDAO getInstance() {
        if (instancia == null) {
            instancia = new IngredienteDAO();
        }
        return instancia;
    }
    // puse solo lo necesario demietras
    /**
     * 
     * @param id
     * @return
     * @throws PersistenciaException 
     */
    public Ingrediente obtenerPorId(Long id) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            return em.find(Ingrediente.class, id);
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar ingrediente por ID: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * 
     * @param nombreParcial
     * @return
     * @throws PersistenciaException 
     */
    public List<Ingrediente> buscarPorNombre(String nombreParcial) throws PersistenciaException {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "select i from Ingrediente i where lower(i.nombre) like lower(:nombre)";
            TypedQuery<Ingrediente> query = em.createQuery(jpql, Ingrediente.class);
            query.setParameter("nombre", "%" + nombreParcial + "%");
            
            return query.getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar ingredientes por nombre: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
    
    
}
