/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package correrinicio;

import com.mycompany.persistencia.Persistencia.ConexionBD;
import entidades.Admin;
import entidades.Cliente;
import entidades.Ingrediente;
import entidades.Mesero;
import entidades.UnidadMedida;
import java.time.LocalDate;
import java.util.Random;
import javax.persistence.EntityManager;

/**
 *
 * @author julian izaguirre
 */
public class insertsMasivos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        EntityManager em = ConexionBD.crearConexion();
        System.out.println("INSERCIÓN DE DATOS INICIALES");

        try {
            em.getTransaction().begin();
            // los empleados
            Admin admin = new Admin("Maye Tolano", "Maye", "1234");
            em.persist(admin);

            Mesero mesero1 = new Mesero("Luis Ortiz", "luis", "4321", "Vespertino", "M-001");
            em.persist(mesero1);
            Mesero mesero2 = new Mesero("Julian Izaguirre", "julian", "0000", "Matutino", "M-002");
            em.persist(mesero2);
            // los ingredientes
            Ingrediente agua = new Ingrediente("Vaso de Agua", UnidadMedida.MILILITRO, 5000.0, null);
            em.persist(agua);

            Ingrediente tortillaMaiz = new Ingrediente("Tortilla de Maíz", UnidadMedida.PIEZA, 200.0, null);
            em.persist(tortillaMaiz);

            Ingrediente tortillaHarina = new Ingrediente("Tortilla de Harina", UnidadMedida.PIEZA, 150.0, null);
            em.persist(tortillaHarina);

            Ingrediente carneAsada = new Ingrediente("Carne Asada", UnidadMedida.GRAMO, 3000.0, null);
            em.persist(carneAsada);

            Ingrediente pollo = new Ingrediente("Pollo", UnidadMedida.GRAMO, 2500.0, null);
            em.persist(pollo);

            em.getTransaction().commit();
            System.out.println("¡Listo! Empleados e ingredientes agregados con éxito.");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error durante la inserción:");
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
}