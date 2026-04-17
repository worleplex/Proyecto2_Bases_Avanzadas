/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package correrinicio;

import com.mycompany.persistencia.Persistencia.ConexionBD;
import entidades.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;

/**
 *
 * @author julian izaguirre
 */
public class insertsMasivos {
    
    private static final String LLAVE_SECRETA = "MiLlaveSuperSecreta123";
    
    /**
     * * @param nombreArchivo
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
            
            // --- 1. USUARIOS ---
            Admin admin = new Admin("Maye Tolano", "Maye", hashear("1234"));
            em.persist(admin);

            Mesero mesero1 = new Mesero("Luis Ortiz", "luis", hashear("4321"), "Vespertino", "M-001");
            em.persist(mesero1);

            Mesero mesero2 = new Mesero("Julian Izaguirre", "julian", hashear("0000"), "Matutino", "M-002");
            em.persist(mesero2);

            // --- 2. INGREDIENTES ---
            Ingrediente agua = new Ingrediente("Agua", UnidadMedida.MILILITRO, 10000.0, getRuta("agua.png"));
            em.persist(agua);

            Ingrediente hielo = new Ingrediente("Hielo", UnidadMedida.PIEZA, 200.0, getRuta("hielo.png"));
            em.persist(hielo);

            Ingrediente tortilla = new Ingrediente("Tortilla de Maíz", UnidadMedida.PIEZA, 300.0, getRuta("tortilla_maiz.png"));
            em.persist(tortilla);

            Ingrediente carneAsada = new Ingrediente("Carne Asada", UnidadMedida.GRAMO, 5000.0, getRuta("carne_asada.png"));
            em.persist(carneAsada);

            Ingrediente cebolla = new Ingrediente("Cebolla", UnidadMedida.PIEZA, 50.0, getRuta("cebolla.png"));
            em.persist(cebolla);

            Ingrediente cilantro = new Ingrediente("Cilantro", UnidadMedida.GRAMO, 500.0, getRuta("cilantro.png"));
            em.persist(cilantro);

            Ingrediente tortillaHarina = new Ingrediente("Tortilla de Harina", UnidadMedida.PIEZA, 200.0, getRuta("tortilla_harina.png"));
            em.persist(tortillaHarina);

            Ingrediente queso = new Ingrediente("Queso Oaxaca", UnidadMedida.GRAMO, 3000.0, getRuta("queso.png"));
            em.persist(queso);

            // --- 3. PRODUCTOS ---
            // Vaso de Agua
            Producto vasoAgua = new Producto();
            vasoAgua.setNombre("Vaso de Agua");
            vasoAgua.setPrecio(15.0);
            vasoAgua.setDescripcion("Vaso de agua fría con hielo");
            vasoAgua.setTipo(TipoProducto.BEBIDA);
            vasoAgua.setEstado(true);
            vasoAgua.setImagen(getRuta("vaso_agua.png"));
            em.persist(vasoAgua);

            em.persist(new ProductoIngrediente(250.0, vasoAgua, agua));
            em.persist(new ProductoIngrediente(3.0, vasoAgua, hielo));

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

            // --- 4. MESAS ---
            Mesa mesa1 = new Mesa("1", true); em.persist(mesa1);
            Mesa mesa2 = new Mesa("2", true); em.persist(mesa2);
            
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

            // --- 5. CLIENTES FRECUENTES ---
            ClienteFrecuente cliente1 = new ClienteFrecuente();
            cliente1.setNombres("Juan");
            cliente1.setApellido_paterno("Pérez");
            cliente1.setApellido_materno("López");
            cliente1.setCorreo("juan.perez@gmail.com");
            cliente1.setTelefono(encriptarTelefono("6441234567")); 
            cliente1.setPuntos(150.0);
            cliente1.setFechaRegistro(LocalDate.now());
            em.persist(cliente1);

            ClienteFrecuente cliente2 = new ClienteFrecuente();
            cliente2.setNombres("María");
            cliente2.setApellido_paterno("González");
            cliente2.setApellido_materno("Ruiz");
            cliente2.setCorreo("maria.g@hotmail.com");
            cliente2.setTelefono(encriptarTelefono("6449876543")); 
            cliente2.setPuntos(300.0);
            cliente2.setFechaRegistro(LocalDate.now());
            em.persist(cliente2);

            // --- 6. COMANDAS Y DETALLES DE PRUEBA ---
            
            // Comanda 1: Abierta (En proceso), Mesa 1, Atiende Luis Ortiz, Pide Cliente 1
            Comanda comanda1 = new Comanda();
            comanda1.setFolio("COM-001");
            comanda1.setFechaHora(LocalDateTime.now().minusHours(1)); // Hace 1 hora
            comanda1.setEstadoComanda(EstadoComanda.ABIERTA); 
            comanda1.setMesa(mesa1);
            comanda1.setMesero(mesero1);
            comanda1.setCliente(cliente1);
            comanda1.setTotal(85.0); // 35 (Tacos) + 50 (Quesadilla)
            em.persist(comanda1);

            DetalleComanda det1 = new DetalleComanda();
            det1.setComanda(comanda1);
            det1.setProducto(tacos);
            det1.setCantidad(1);
            det1.setCosto(35.0);
            det1.setComentario("Sin cebolla");
            em.persist(det1);

            DetalleComanda det2 = new DetalleComanda();
            det2.setComanda(comanda1);
            det2.setProducto(quesadilla);
            det2.setCantidad(1);
            det2.setCosto(50.0);
            det2.setComentario("");
            em.persist(det2);

            // Comanda 2: Entregada (Ya pagada), Mesa 2, Atiende Julian, Pide Cliente 2
            Comanda comanda2 = new Comanda();
            comanda2.setFolio("COM-002");
            comanda2.setFechaHora(LocalDateTime.now().minusDays(1)); // Ayer
            comanda2.setEstadoComanda(EstadoComanda.ENTREGADA); 
            comanda2.setMesa(mesa2);
            comanda2.setMesero(mesero2);
            comanda2.setCliente(cliente2);
            comanda2.setTotal(65.0); // 2 Vasos de Agua (30) + 1 Taco (35)
            em.persist(comanda2);

            DetalleComanda det3 = new DetalleComanda();
            det3.setComanda(comanda2);
            det3.setProducto(vasoAgua);
            det3.setCantidad(2);
            det3.setCosto(30.0); // 15 c/u x 2
            det3.setComentario("Mucho hielo");
            em.persist(det3);

            DetalleComanda det4 = new DetalleComanda();
            det4.setComanda(comanda2);
            det4.setProducto(tacos);
            det4.setCantidad(1);
            det4.setCosto(35.0);
            det4.setComentario("Bien doraditos");
            em.persist(det4);

            em.getTransaction().commit();
            System.out.println("¡Listo! Datos y Comandas ingresados correctamente.");

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

    private static String hashear(String passwordPlana) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(passwordPlana.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Error al hashear", ex);
        }
    }
    
    private static String encriptarTelefono(String telefonoPlano) {
        if (telefonoPlano == null || telefonoPlano.isEmpty()) return telefonoPlano;
        try {
            java.security.Key aesKey = new javax.crypto.spec.SecretKeySpec(java.util.Arrays.copyOf(LLAVE_SECRETA.getBytes("UTF-8"), 16), "AES");
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, aesKey);
            byte[] encriptado = cipher.doFinal(telefonoPlano.getBytes("UTF-8"));
            return java.util.Base64.getEncoder().encodeToString(encriptado);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar el teléfono", e);
        }
    }
    
}
