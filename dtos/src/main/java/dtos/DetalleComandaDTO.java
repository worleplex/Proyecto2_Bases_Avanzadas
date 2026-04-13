package dtos;

/**
 * @author Luis Alonso
 * */
public class DetalleComandaDTO {
    private Long id;
    private Integer cantidad;
    private String comentario;
    private double Costo;
    private Long idComanda;
    private Long idProducto;

    public DetalleComandaDTO() {
    }

    public DetalleComandaDTO(Long id, Integer cantidad, String comentario, double costo, Long idComanda, Long idProducto) {
        this.id = id;
        this.cantidad = cantidad;
        this.comentario = comentario;
        Costo = costo;
        this.idComanda = idComanda;
        this.idProducto = idProducto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public double getCosto() {
        return Costo;
    }

    public void setCosto(double costo) {
        Costo = costo;
    }

    public Long getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(Long idComanda) {
        this.idComanda = idComanda;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }
}
