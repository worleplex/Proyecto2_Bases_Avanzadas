package daos;

import conexion.ConexionBD;
import entidades.Mesa;
import entidades.Producto;
import entidades.TipoProducto;

import javax.persistence.EntityManager;
import java.util.List;

public class MesaDAO {
    private static MesaDAO instancia;

    public MesaDAO(){

    }

    public static MesaDAO getInstancia() {
        if(instancia == null){
            instancia = new MesaDAO();
        }
        return instancia;
    }

    public Mesa mesaOcupada(Long numero){
        EntityManager em = ConexionBD.crearConexion();
        return em.find(Mesa.class, numero);
    }

    public List<Producto> obtenerTodos() {
        EntityManager em = ConexionBD.crearConexion();
        List<Producto> productos = em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
        return productos;
    }

    public List<Producto> obtenerPorTipo(TipoProducto tipo) {
        EntityManager em = ConexionBD.crearConexion();
        return em.createQuery("SELECT p FROM Producto p WHERE p.tipo = :tipo", Producto.class)
                .setParameter("tipo", tipo)
                .getResultList();
    }

    public List<Producto> buscarProductoNombre(String nombre){
        EntityManager em = ConexionBD.crearConexion();
        return em.createQuery("SELECT p FROM Producto p WHERE p.nombre LIKE :nombre", Producto.class)
                .setParameter("nombre", "%" + nombre + "%")
                .getResultList();
    }

}
