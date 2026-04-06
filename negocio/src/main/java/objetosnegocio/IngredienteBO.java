/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosnegocio;

import adaptadores.IngredienteAdapter;
import daos.IngredienteDAO;
import dtos.IngredienteDTO;
import entidades.Ingrediente;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gael Galaviz
 */
public class IngredienteBO {
    // solo lo necesario
    private static IngredienteBO instancia;
    private IngredienteDAO ingredienteDAO;

    private IngredienteBO() {
        this.ingredienteDAO = IngredienteDAO.getInstance();
    }

    public static IngredienteBO getInstance() {
        if (instancia == null) {
            instancia = new IngredienteBO();
        }
        return instancia;
    }

    /**
     * 
     * @param nombre
     * @return
     * @throws NegocioException
     * @throws PersistenciaException 
     */
    public List<IngredienteDTO> buscarIngredientes(String nombre) throws NegocioException, PersistenciaException {
        try {
            if (nombre == null) {
                nombre = ""; 
            }
            List<Ingrediente> ingredientesBD = ingredienteDAO.buscarPorNombre(nombre);
            List<IngredienteDTO> listaDTOs = new ArrayList<>();

            for (Ingrediente i : ingredientesBD) {
                listaDTOs.add(IngredienteAdapter.aDTO(i));
            }

            return listaDTOs;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al consultar los ingredientes: " + e.getMessage());
        }
    }
}
