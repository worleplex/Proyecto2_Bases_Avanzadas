/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.presentacion.paneles.panelesClientes;

import com.mycompany.presentacion.controlador.Coordinador;
import entidades.ClienteFrecuente;
import excepciones.NegocioException;
import utilidades.UIUtils;
import javax.swing.*;
import java.awt.*;
import objetosnegocio.ClienteFrecuenteBO;

/**
 * @author julian izaguirre
 * Panel para confirmar y eliminar un cliente de la base de datos.
 */
public class PanelEliminarClienteFrecuente extends JPanel {
    private final Coordinador coordinador;
    private ClienteFrecuente cliente; 
    private ClienteFrecuenteBO bo = ClienteFrecuenteBO.getInstance();

    public PanelEliminarClienteFrecuente(Coordinador coordinador, ClienteFrecuente cliente) {
        this.coordinador = coordinador;
        this.cliente = cliente;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        JPanel fondo = UIUtils.crearPanelFondo();
        fondo.setLayout(new BorderLayout());

        JLabel t = new JLabel("¿Seguro que deseas eliminar este cliente?", SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 26));
        t.setForeground(new Color(255, 100, 100)); // Rojo adv
        t.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        JPanel pC = new JPanel(new GridLayout(3, 2, 10, 15));
        pC.setOpaque(false);
        pC.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        JTextField txtNombre = new JTextField(cliente.getNombres() + " " + cliente.getApellido_paterno());
        txtNombre.setEditable(false); 
        
        JTextField txtCorreo = new JTextField(cliente.getCorreo());
        txtCorreo.setEditable(false);
        
        JTextField txtPuntos = new JTextField(String.valueOf(cliente.getPuntos()));
        txtPuntos.setEditable(false);

        pC.add(UIUtils.crearEtiquetaGris("Nombre completo:"));
        pC.add(txtNombre);
        pC.add(UIUtils.crearEtiquetaGris("Correo electrónico:"));
        pC.add(txtCorreo);
        pC.add(UIUtils.crearEtiquetaGris("Puntos acumulados:"));
        pC.add(txtPuntos);

        JPanel pB = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        pB.setOpaque(false);

        JButton btnEliminar = UIUtils.crearBotonAccion("Sí, Eliminar", new Color(200, 50, 30));
        JButton btnCancelar = UIUtils.crearBotonAccion("Cancelar", new Color(100, 100, 100)); 

        btnEliminar.addActionListener(e -> eliminar());
        btnCancelar.addActionListener(e -> coordinador.mostrarPanelMenuClientes());

        pB.add(btnEliminar);
        pB.add(btnCancelar);

        fondo.add(t, BorderLayout.NORTH);
        fondo.add(pC, BorderLayout.CENTER);
        fondo.add(pB, BorderLayout.SOUTH);
        
        add(fondo, BorderLayout.CENTER);
    }

    private void eliminar() {
        try {
            int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "Esta acción no se puede deshacer ¿Eliminar definitivamente?", 
                    "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                bo.eliminarCliente(cliente.getId());
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente");
                coordinador.mostrarPanelMenuClientes();
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }
}