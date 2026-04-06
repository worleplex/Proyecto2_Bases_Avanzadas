/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.presentacion.paneles.panelesClientes;

import com.mycompany.presentacion.controlador.Coordinador;
import dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import objetosnegocio.ClienteFrecuenteBO;

/**
 *
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
        JPanel fondo = crearFondo();
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

        pCentral.add(etiqueta("Nombre:"));
        pCentral.add(txtNombre);
        pCentral.add(etiqueta("Apellido Paterno:"));
        pCentral.add(txtApPaterno);
        pCentral.add(etiqueta("Apellido Materno:"));
        pCentral.add(txtApMaterno);
        pCentral.add(etiqueta("Correo:"));
        pCentral.add(txtCorreo);
        pCentral.add(etiqueta("Telefono:"));
        pCentral.add(txtTelefono);

        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pBotones.setOpaque(false);

        JButton btnGuardar = botonVerde("Guardar");
        JButton btnCancelar = botonRojo("Cancelar");

        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> regresar());

        pBotones.add(btnGuardar);
        pBotones.add(btnCancelar);

        fondo.add(titulo, BorderLayout.NORTH);
        fondo.add(pCentral, BorderLayout.CENTER);
        fondo.add(pBotones, BorderLayout.SOUTH);
        add(fondo, BorderLayout.CENTER);
    }
    /**
     * Recolecta los datos de la interfaz y pide al BO que los guarde.
     */
    private void guardar() {
        try {
            ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
            dto.setNombres(txtNombre.getText());
            dto.setApellidoPaterno(txtApPaterno.getText());
            dto.setApellidoMaterno(txtApMaterno.getText());
            dto.setCorreo(txtCorreo.getText());
            dto.setTelefono(txtTelefono.getText());
            dto.setFechaRegistro(LocalDate.now());
            dto.setPuntos(0.0);
            dto.setFechaRegistro(java.time.LocalDate.now()); 

            bo.guardarCliente(dto);
            JOptionPane.showMessageDialog(this, "Cliente registrado con exito.");
            regresar();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    
    /**
     * Regresa al menu principal.
     */
    private void regresar() {
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
        v.setContentPane(new PanelMenuClientes(coordinador));
        v.revalidate();
    }

    private JLabel etiqueta(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        return l;
    }

    private JButton botonVerde(String t) {
        return boton(t, new Color(46, 180, 100));
    }

    private JButton botonRojo(String t) {
        return boton(t, new Color(200, 50, 30));
    }

    private JButton boton(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(120, 38));
        return b;
    }

    /**
     * Crea un panel con la imagen de fondo cargada desde recursos.
     *
     * @return JPanel con el fondo configurado.
     */
    private JPanel crearFondo() {
        // Intentamos cargar la imagen desde la raiz de resources
        java.net.URL url = getClass().getResource("/FondoInicio.png");

        ImageIcon icono;
        if (url != null) {
            icono = new ImageIcon(url);
            // Si no encuentra, mandamos un error a la consola para saber
        } else {
            System.err.println("Error: No se encontro FondoInicio.png en la raiz de resources");
            icono = new ImageIcon();
        }

        Image imagen = icono.getImage();

        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (imagen != null && imagen.getWidth(null) > 0) {
                    Graphics2D g2 = (Graphics2D) g;
                    // Calidad de renderizado
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.setColor(new Color(30, 30, 30));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        p.setOpaque(false);
        return p;
    }
}