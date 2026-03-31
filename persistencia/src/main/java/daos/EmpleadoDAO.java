/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daos;

import com.mycompany.persistencia.Persistencia.ConexionBD;
import entidades.Empleado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author julian izaguirre
 */
public class EmpleadoDAO {
    
    private static EmpleadoDAO instancia;
    
    private EmpleadoDAO() {    
    }
    
    public static EmpleadoDAO getInstance() {
        if (instancia == null) {
            instancia = new EmpleadoDAO();
        }
        return instancia;
    }
    
    public Empleado buscarPorContras(String username, String password) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String JPQL = "select e from Empleado e where e.username = :user and e.password = :pass";
            TypedQuery<Empleado> query = em.createQuery(JPQL, Empleado.class);
            query.setParameter("user", username);
            query.setParameter("pass", password);
            List<Empleado> resultados = query.getResultList();
            
            if (resultados.isEmpty()) {
                return null; 
            } else {
                return resultados.get(0); 
            }
            
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }
    
    public List<Empleado> buscarTodos() {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String JPQL = "select e from Empleado e";
            TypedQuery<Empleado> query = em.createQuery(JPQL, Empleado.class);
            return query.getResultList();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }
    
}
