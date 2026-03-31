/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package correrinicio;

import com.mycompany.persistencia.Persistencia.ConexionBD;
import entidades.Admin;
import entidades.Cliente;
import entidades.Mesero;
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
        System.out.println("INSERCIÓN DE EMPLEADOS PARA PRUEBAS DE LOGIN");

        try {
            em.getTransaction().begin();

            // 1. Creamos al Administrador (La profe Maye)
            Admin admin = new Admin("Maye Tolano", "Maye", "1234");
            em.persist(admin);

            // 2. Creamos al primer Mesero (Luis)
            Mesero mesero1 = new Mesero("Luis Ortiz", "luis", "4321", "Vespertino", "M-001");
            em.persist(mesero1);

            // 3. Creamos a un segundo Mesero (Tú)
            Mesero mesero2 = new Mesero("Julian Izaguirre", "julian", "0000", "Matutino", "M-002");
            em.persist(mesero2);

            em.getTransaction().commit();
            System.out.println("¡Listo! Empleados agregados con éxito a la base de datos.");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Ocurrió un error durante la inserción:");
            e.printStackTrace();
            
        } finally {
            em.close();
        }
        
        
        
//        Random random = new Random();
//
//        System.out.println("INSERCIÓN DE 5 CLIENTES");
//
//        try {
//            em.getTransaction().begin();
//
//            String[] nombres = {"Carlos", "Ana", "Luis", "Elena", "Guillermo"};
//            String[] apellidos = {"Slim", "Gabriel", "Miguel", "Poniatowska", "Del Toro"};
//
//            for (int i = 1; i <= 5; i++) {
//                
//                String nombreGen = nombres[random.nextInt(nombres.length)];
//                String apellidoPatGen = apellidos[random.nextInt(apellidos.length)];
//                String apellidoMatGen = apellidos[random.nextInt(apellidos.length)];
//                
//                if(apellidoPatGen.equals(apellidoMatGen)){
//                    apellidoMatGen = "Soto"; 
//                }
//                
//                String telefono = "644" + (1000000 + random.nextInt(9000000));
//                String correo = nombreGen.toLowerCase() + i + "@itson.edu.mx";
//                LocalDate fechaReg = LocalDate.now();
//
//                Cliente cli = new Cliente(nombreGen, apellidoPatGen, apellidoMatGen, telefono, correo, fechaReg);
//                
//                em.persist(cli);
//            }
//
//            em.getTransaction().commit();
//            System.out.println("¡Listo! 5 clientes agregados con éxito.");
//
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//            System.err.println("Ocurrió un error durante la inserción:");
//            e.printStackTrace();
//            
//        } finally {
//            em.close();
//        }
    }
    
}