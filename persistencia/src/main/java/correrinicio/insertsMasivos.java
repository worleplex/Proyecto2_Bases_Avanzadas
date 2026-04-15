/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package correrinicio;

import com.mycompany.persistencia.Persistencia.ConexionBD;
import entidades.*;

import java.time.LocalDate;
import java.util.Random;
import javax.persistence.EntityManager;

/**
 *
 * @author julian izaguirre
 */
public class insertsMasivos {
    
    /**
     * 
     * @param nombreArchivo
     * @return 
     */
    private static String getRuta(String nombreArchivo) {
        java.net.URL url = insertsMasivos.class.getResource("/" + nombreArchivo);
        return url != null ? "/" + nombreArchivo : null;
    }
    
    
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

            // Para Vaso de Agua
            Ingrediente agua = new Ingrediente("Agua", UnidadMedida.MILILITRO, 10000.0, getRuta("agua.png"));
            em.persist(agua);

            Ingrediente hielo = new Ingrediente("Hielo", UnidadMedida.PIEZA, 200.0, getRuta("hielo.png"));
            em.persist(hielo);

            // Para Tacos de Carne Asada
            Ingrediente tortilla = new Ingrediente("Tortilla de Maíz", UnidadMedida.PIEZA, 300.0, getRuta("tortilla_maiz.png"));
            em.persist(tortilla);

            Ingrediente carneAsada = new Ingrediente("Carne Asada", UnidadMedida.GRAMO, 5000.0, getRuta("carne_asada.png"));
            em.persist(carneAsada);

            Ingrediente cebolla = new Ingrediente("Cebolla", UnidadMedida.PIEZA, 50.0, getRuta("cebolla.png"));
            em.persist(cebolla);

            Ingrediente cilantro = new Ingrediente("Cilantro", UnidadMedida.GRAMO, 500.0, getRuta("cilantro.png"));
            em.persist(cilantro);

            // Para Quesadilla
            Ingrediente tortillaHarina = new Ingrediente("Tortilla de Harina", UnidadMedida.PIEZA, 200.0, getRuta("tortilla_harina.png"));
            em.persist(tortillaHarina);

            Ingrediente queso = new Ingrediente("Queso Oaxaca", UnidadMedida.GRAMO, 3000.0, getRuta("queso.png"));
            em.persist(queso);

            // Vaso de Agua
            Producto vasoAgua = new Producto();
            vasoAgua.setNombre("Vaso de Agua");
            vasoAgua.setPrecio(15.0);
            vasoAgua.setDescripcion("Vaso de agua fría con hielo");
            vasoAgua.setTipo(TipoProducto.BEBIDA);
            vasoAgua.setEstado(true);
            vasoAgua.setImagen(getRuta("vaso_agua.png"));
            em.persist(vasoAgua);

            ProductoIngrediente piAgua = new ProductoIngrediente(250.0, vasoAgua, agua);
            em.persist(piAgua);

            ProductoIngrediente piHielo = new ProductoIngrediente(3.0, vasoAgua, hielo);
            em.persist(piHielo);

            // Taco de Carne Asada
            Producto tacos = new Producto();
            tacos.setNombre("Taco de Carne Asada");
            tacos.setPrecio(35.0);
            tacos.setDescripcion("taco de carne asada con cebolla y cilantro");
            tacos.setTipo(TipoProducto.PLATILLO);
            tacos.setEstado(true);
            tacos.setImagen(getRuta("tacos.png"));
            em.persist(tacos);

            em.persist(new ProductoIngrediente(3.0,   tacos, tortilla));
            em.persist(new ProductoIngrediente(150.0, tacos, carneAsada));
            em.persist(new ProductoIngrediente(1.0,   tacos, cebolla));
            em.persist(new ProductoIngrediente(10.0,  tacos, cilantro));

            // Quesadilla
            Producto quesadilla = new Producto();
            quesadilla.setNombre("Quesadilla");
            quesadilla.setPrecio(50.0);
            quesadilla.setDescripcion("Quesadilla de harina con queso Oaxaca");
            quesadilla.setTipo(TipoProducto.PLATILLO);
            quesadilla.setEstado(true);
            quesadilla.setImagen(getRuta("quesadilla.png"));
            em.persist(quesadilla);

            em.persist(new ProductoIngrediente(2.0,   quesadilla, tortillaHarina));
            em.persist(new ProductoIngrediente(100.0, quesadilla, queso));

            //Insert de las 20 mesas mediante un ciclo por que me dio weba hacerlo manual
            em.persist(new Mesa("1", true));
            em.persist(new Mesa("2", true));
            em.persist(new Mesa("3", true));
            em.persist(new Mesa("4", true));
            em.persist(new Mesa("5", true));
            em.persist(new Mesa("6", true));
            em.persist(new Mesa("7", true));
            em.persist(new Mesa("8", true));
            em.persist(new Mesa("9", true));
            em.persist(new Mesa("10", true));
            em.persist(new Mesa("12", true));
            em.persist(new Mesa("13", true));
            em.persist(new Mesa("14", true));
            em.persist(new Mesa("15", true));
            em.persist(new Mesa("16", true));
            em.persist(new Mesa("17", true));
            em.persist(new Mesa("18", true));
            em.persist(new Mesa("19", true));
            em.persist(new Mesa("20", true));




            em.getTransaction().commit();
            System.out.println("¡Listo! Datos ingresados bien");

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