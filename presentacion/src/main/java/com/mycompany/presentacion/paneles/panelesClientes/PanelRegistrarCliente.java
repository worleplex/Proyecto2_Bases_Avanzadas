/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.presentacion.paneles.panelesClientes;

import com.mycompany.presentacion.controlador.Coordinador;
import dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import utilidades.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import objetosnegocio.ClienteFrecuenteBO;

/**
 * @author Gael Galaviz
 * Clase para el registro de nuevos clientes en el sistema.
 */
public class PanelRegistrarCliente extends JPanel {
    private final Coordinador coordinador;
    private JTextField txtNombre, txtApPaterno, txtApMaterno, txtCorreo, txtTelefono;
    private ClienteFrecuenteBO bo =  ClienteFrecuenteBO.getInstance();

    public PanelRegistrarCliente(Coordinador coordinador) {
        this.coordinador = coordinador;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        JPanel fondo = UIUtils.crearPanelFondo();
        fondo.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Registrar Nuevo Cliente", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        JPanel pCentral = new JPanel(new GridLayout(5, 2, 10, 15));
        pCentral.setOpaque(false);
        pCentral.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        txtNombre = new JTextField();
        txtApPaterno = new JTextField();
        txtApMaterno = new JTextField();
        txtCorreo = new JTextField();
        txtTelefono = new JTextField();

        pCentral.add(UIUtils.crearEtiquetaGris("Nombre:"));
        pCentral.add(txtNombre);
        pCentral.add(UIUtils.crearEtiquetaGris("Apellido Paterno:"));
        pCentral.add(txtApPaterno);
        pCentral.add(UIUtils.crearEtiquetaGris("Apellido Materno:"));
        pCentral.add(txtApMaterno);
        pCentral.add(UIUtils.crearEtiquetaGris("Correo:"));
        pCentral.add(txtCorreo);
        pCentral.add(UIUtils.crearEtiquetaGris("Telefono:"));
        pCentral.add(txtTelefono);

        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pBotones.setOpaque(false);

        JButton btnGuardar = UIUtils.crearBotonAccion("Guardar", new Color(46, 180, 100));
        JButton btnCancelar = UIUtils.crearBotonAccion("Cancelar", new Color(200, 50, 30));

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> regresar());

        pBotones.add(btnGuardar);
        pBotones.add(btnCancelar);

        fondo.add(titulo, BorderLayout.NORTH);
        fondo.add(pCentral, BorderLayout.CENTER);
        fondo.add(pBotones, BorderLayout.SOUTH);
        add(fondo, BorderLayout.CENTER);
    }

    private void guardar() {
        try {
            ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
            dto.setNombres(txtNombre.getText());
            dto.setApellidoPaterno(txtApPaterno.getText());
            dto.setApellidoMaterno(txtApMaterno.getText());
            dto.setCorreo(txtCorreo.getText());
            dto.setTelefono(txtTelefono.getText());
            dto.setPuntos(0.0);
            dto.setFechaRegistro(LocalDate.now()); 

            bo.guardarCliente(dto);
            JOptionPane.showMessageDialog(this, "Cliente registrado con exito.");
            regresar();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    private void regresar() {
        coordinador.mostrarPanelMenuClientes();
    }
}