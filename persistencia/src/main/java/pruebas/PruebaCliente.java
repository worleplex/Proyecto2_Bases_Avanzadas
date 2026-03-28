/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pruebas;

import daos.ClienteDAO;
import entidades.Cliente;
import java.time.LocalDate;

/**
 *
 * @author julian izaguirre
 */
public class PruebaCliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Instanciamos el DAO que se encarga de las operaciones en la BD
        ClienteDAO clienteDAO = new ClienteDAO();

        try {
            System.out.println("pruebas de clientes");

            System.out.println("\nGuardar Cliente");
            Cliente nuevoCliente = new Cliente();
            
            nuevoCliente.setNombres("Julio Cesar");
            nuevoCliente.setApellido_paterno("Chavez");
            nuevoCliente.setApellido_materno("Gonzalez");
            nuevoCliente.setTelefono("6449876543"); 
            nuevoCliente.setCorreo("campeon@itson.edu.mx");
            nuevoCliente.setFechaRegistro(LocalDate.now());

            clienteDAO.guardar(nuevoCliente);
            System.out.println("Cliente guardado exitosamente");
            System.out.println("El ID asignado por la base de datos es: " + nuevoCliente.getId());

            System.out.println("\nConsultar Cliente");
            Cliente clienteConsultado = clienteDAO.buscarPorId(nuevoCliente.getId());
            
            if (clienteConsultado != null) {
                System.out.println("¡Cliente encontrado en la BD!");
                System.out.println("Nombre completo: " + clienteConsultado.getNombres() + " " + clienteConsultado.getApellido_paterno() + " " + clienteConsultado.getApellido_materno());
                System.out.println("Teléfono: " + clienteConsultado.getTelefono());
                System.out.println("Fecha de registro: " + clienteConsultado.getFechaRegistro());
            } else {
                System.out.println("No se encontró ningún cliente con ese ID");
            }

            System.out.println("\nActualizar Cliente");
            clienteConsultado.setTelefono("6440000000");
            clienteDAO.editar(clienteConsultado);
            System.out.println("Teléfono actualizado correctamente a: " + clienteConsultado.getTelefono());

        } catch (Exception e) {
            System.err.println("Ocurrió un error al probar las operaciones del Cliente");
            e.printStackTrace();
        }
    }
    
}