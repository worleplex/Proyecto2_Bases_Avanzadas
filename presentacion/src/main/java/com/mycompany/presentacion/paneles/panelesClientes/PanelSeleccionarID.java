/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.presentacion.paneles.panelesClientes;

import adaptadores.ClienteFrecuenteAdapter;
import com.mycompany.presentacion.controlador.Coordinador;
import dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import utilidades.UIUtils;
import javax.swing.*;
import java.awt.*;
import objetosnegocio.ClienteFrecuenteBO;

/**
 * @author Gael Galaviz
 * Clase para buscar un cliente por su ID antes de realizar una accion.
 */
public class PanelSeleccionarID extends JPanel {
    private final Coordinador coordinador;
    private String accion; 
    private JTextField txtId;
    private ClienteFrecuenteBO bo = ClienteFrecuenteBO.getInstance();

    public PanelSeleccionarID(Coordinador coordinador, String accion) {
        this.coordinador = coordinador;
        this.accion = accion;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        JPanel fondo = UIUtils.crearPanelFondo();
        fondo.setLayout(new GridBagLayout());

        JPanel contenido = new JPanel(new BorderLayout(15, 15));
        contenido.setOpaque(false);

        JLabel lbl = new JLabel("Ingrese ID del Cliente:", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));

        txtId = new JTextField();
        txtId.setPreferredSize(new Dimension(200, 30));
        txtId.setHorizontalAlignment(JTextField.CENTER);

        JPanel pB = new JPanel(new FlowLayout());
        pB.setOpaque(false);
        
        JButton btnOk = UIUtils.crearBotonAccion("Continuar", new Color(46, 180, 100));
        JButton btnCan = UIUtils.crearBotonAccion("Regresar", new Color(200, 50, 30));

        btnOk.addActionListener(e -> procesar());
        btnCan.addActionListener(e -> coordinador.mostrarPanelMenuClientes());

        pB.add(btnOk);
        pB.add(btnCan);

        contenido.add(lbl, BorderLayout.NORTH);
        contenido.add(txtId, BorderLayout.CENTER);
        contenido.add(pB, BorderLayout.SOUTH);

        fondo.add(contenido);
        add(fondo, BorderLayout.CENTER);
    }

    private void procesar() {
        try {
            Long id = Long.parseLong(txtId.getText());
            
            ClienteFrecuenteDTO dto = bo.buscarClientePorId(id);
            
            if (dto != null) {
                if (accion.equals("editar")) {
                    coordinador.mostrarPanelEditarClienteFrecuente(ClienteFrecuenteAdapter.dtoAEntidad(dto));
                } else {
                    coordinador.mostrarPanelEliminarClienteFrecuente(ClienteFrecuenteAdapter.dtoAEntidad(dto));
                }
            } else {
                JOptionPane.showMessageDialog(this, "ID no encontrado en la base de datos");
            }
            
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido. Por favor ingresa solo números");
        }
    }
}