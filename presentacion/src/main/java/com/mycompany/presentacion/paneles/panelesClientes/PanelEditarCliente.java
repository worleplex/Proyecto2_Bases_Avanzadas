/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.presentacion.paneles.panelesClientes;

import adaptadores.ClienteFrecuenteAdapter;
import com.mycompany.presentacion.controlador.Coordinador;
import dtos.ClienteFrecuenteDTO;
import entidades.ClienteFrecuente;
import excepciones.NegocioException;
import utilidades.UIUtils;
import javax.swing.*;
import java.awt.*;
import objetosnegocio.ClienteFrecuenteBO;

/**
 * @author Gael Galaviz
 * Clase para modificar los datos de un cliente existente.
 */
public class PanelEditarCliente extends JPanel {
    private final Coordinador coordinador;
    private ClienteFrecuente cliente; 
    private JTextField txtNombre, txtApPaterno, txtApMaterno, txtCorreo, txtTelefono;
    private ClienteFrecuenteBO bo = ClienteFrecuenteBO.getInstance();

    public PanelEditarCliente(Coordinador coordinador, ClienteFrecuente cliente) {
        this.coordinador = coordinador;
        this.cliente = cliente;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        JPanel fondo = UIUtils.crearPanelFondo();
        fondo.setLayout(new BorderLayout());

        JLabel t = new JLabel("Editar Cliente", SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 28));
        t.setForeground(Color.WHITE);
        t.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        JPanel pC = new JPanel(new GridLayout(5, 2, 10, 15));
        pC.setOpaque(false);
        pC.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        txtNombre = new JTextField(cliente.getNombres());
        txtApPaterno = new JTextField(cliente.getApellido_paterno());
        txtApMaterno = new JTextField(cliente.getApellido_materno());
        txtCorreo = new JTextField(cliente.getCorreo());
        txtTelefono = new JTextField(cliente.getTelefono());

        pC.add(UIUtils.crearEtiquetaGris("Nombre:"));
        pC.add(txtNombre);
        pC.add(UIUtils.crearEtiquetaGris("Ap. Paterno:"));
        pC.add(txtApPaterno);
        pC.add(UIUtils.crearEtiquetaGris("Ap. Materno:"));
        pC.add(txtApMaterno);
        pC.add(UIUtils.crearEtiquetaGris("Correo:"));
        pC.add(txtCorreo);
        pC.add(UIUtils.crearEtiquetaGris("Telefono:"));
        pC.add(txtTelefono);

        JPanel pB = new JPanel(new FlowLayout());
        pB.setOpaque(false);
        
        JButton btnG = UIUtils.crearBotonAccion("Actualizar", new Color(46, 180, 100));
        JButton btnC = UIUtils.crearBotonAccion("Cancelar", new Color(200, 50, 30));

        btnG.addActionListener(e -> actualizar());
        btnC.addActionListener(e -> coordinador.mostrarPanelMenuClientes());

        pB.add(btnG);
        pB.add(btnC);

        fondo.add(t, BorderLayout.NORTH);
        fondo.add(pC, BorderLayout.CENTER);
        fondo.add(pB, BorderLayout.SOUTH);
        add(fondo, BorderLayout.CENTER);
    }

    private void actualizar() {
        try {
            cliente.setNombres(txtNombre.getText());
            cliente.setApellido_paterno(txtApPaterno.getText());
            cliente.setApellido_materno(txtApMaterno.getText());
            cliente.setCorreo(txtCorreo.getText());
            cliente.setTelefono(txtTelefono.getText());

            ClienteFrecuenteDTO dto = ClienteFrecuenteAdapter.entidadADTO(cliente);
            bo.editarCliente(dto);

            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente");
            coordinador.mostrarPanelMenuClientes();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
        }
    }
}