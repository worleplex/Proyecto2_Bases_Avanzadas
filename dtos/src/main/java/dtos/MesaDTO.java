package dtos;

/***
 * @author Luis Alonso
 */


public class MesaDTO {
    private Long id;
    private String numero;
    private boolean estado;
    private Long idComanda;

    public MesaDTO() {

    }

    public MesaDTO(Long id, String numero, boolean estado, Long idComanda) {
        this.id = id;
        this.numero = numero;
        this.estado = estado;
        this.idComanda = idComanda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Long getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(Long idComanda) {
        this.idComanda = idComanda;
    }
}
