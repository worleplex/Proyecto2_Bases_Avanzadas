/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.presentacion.paneles.panelesClientes;

import com.mycompany.presentacion.controlador.Coordinador;
import dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import utilidades.UIUtils;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import objetosnegocio.ClienteFrecuenteBO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author Gael Galaviz / julian izaguirre
 * Clase que muestra la lista completa de clientes con filtros avanzados
 */
public class PanelConsultarClientes extends JPanel {
    private final Coordinador coordinador;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtFiltroNombre, txtFiltroTelefono, txtFiltroCorreo;
    private ClienteFrecuenteBO bo = ClienteFrecuenteBO.getInstance();

    public PanelConsultarClientes(Coordinador coordinador) {
        this.coordinador = coordinador;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        
        JPanel fondo = UIUtils.crearPanelFondo();
        fondo.setLayout(new BorderLayout());

        JLabel t = new JLabel("Consultar clientes", SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 36));
        t.setForeground(Color.WHITE);
        t.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        JPanel panelCentral = new JPanel(new BorderLayout(20, 0));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 40));

        modelo = new DefaultTableModel(new Object[]{
            "Nombre", "Apellido paterno", "Apellido materno", "Correo electrónico", "Teléfono", "Puntos de fidelidad"
        }, 0);
        
        tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setBackground(new Color(255, 204, 204));
        tabla.setSelectionBackground(new Color(255, 153, 153));
        
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(255, 204, 204));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane sp = new JScrollPane(tabla);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        panelCentral.add(sp, BorderLayout.CENTER);

        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.Y_AXIS));
        panelFiltros.setOpaque(false);
        panelFiltros.setPreferredSize(new Dimension(300, 0)); 

        panelFiltros.add(UIUtils.crearEtiquetaGris("Filtrar por nombre:"));
        txtFiltroNombre = new JTextField();
        aplicarPlaceholder(txtFiltroNombre, "ingresar el nombre del cliente");
        panelFiltros.add(txtFiltroNombre);
        panelFiltros.add(Box.createVerticalStrut(20)); 

        panelFiltros.add(UIUtils.crearEtiquetaGris("Filtrar por Teléfono:"));
        txtFiltroTelefono = new JTextField();
        aplicarPlaceholder(txtFiltroTelefono, "ingresar el telefono");
        panelFiltros.add(txtFiltroTelefono);
        panelFiltros.add(Box.createVerticalStrut(20)); 

        panelFiltros.add(UIUtils.crearEtiquetaGris("Filtrar por Correo:"));
        txtFiltroCorreo = new JTextField();
        aplicarPlaceholder(txtFiltroCorreo, "ingrese el correo");
        panelFiltros.add(txtFiltroCorreo);
        panelFiltros.add(Box.createVerticalStrut(30));

        JButton btnBuscar = UIUtils.crearBotonAccion("Ver informacion", new Color(110, 220, 110));
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBuscar.addActionListener(e -> cargarDatos());
        panelFiltros.add(btnBuscar);

        panelCentral.add(panelFiltros, BorderLayout.EAST);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(0, 40, 30, 0));
        
        JButton btnR = UIUtils.crearBotonAccion("Regresar", new Color(255, 80, 50));
        btnR.setPreferredSize(new Dimension(200, 45));
        btnR.addActionListener(e -> coordinador.mostrarPanelMenuClientes());
        panelSur.add(btnR);

        fondo.add(t, BorderLayout.NORTH);
        fondo.add(panelCentral, BorderLayout.CENTER);
        fondo.add(panelSur, BorderLayout.SOUTH);
        add(fondo, BorderLayout.CENTER);
        
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            modelo.setRowCount(0);
            List<ClienteFrecuenteDTO> listaCompleta = bo.obtenerTodosLosClientes();
            
            String fNombre = obtenerTextoFiltro(txtFiltroNombre, "ingresar el nombre del cliente").toLowerCase();
            String fTel = obtenerTextoFiltro(txtFiltroTelefono, "ingresar el telefono").toLowerCase();
            String fCorreo = obtenerTextoFiltro(txtFiltroCorreo, "ingrese el correo").toLowerCase();

            for (ClienteFrecuenteDTO c : listaCompleta) {
                String nomCliente = c.getNombres() != null ? c.getNombres().toLowerCase() : "";
                String telCliente = c.getTelefono() != null ? c.getTelefono().toLowerCase() : "";
                String correoCliente = c.getCorreo() != null ? c.getCorreo().toLowerCase() : "";

                boolean pasaNombre = fNombre.isEmpty() || nomCliente.contains(fNombre);
                boolean pasaTel = fTel.isEmpty() || telCliente.contains(fTel);
                boolean pasaCorreo = fCorreo.isEmpty() || correoCliente.contains(fCorreo);

                if (pasaNombre && pasaTel && pasaCorreo) {
                    modelo.addRow(new Object[]{
                        c.getNombres(), 
                        c.getApellidoPaterno(), 
                        c.getApellidoMaterno(), 
                        c.getCorreo() != null && !c.getCorreo().isEmpty() ? c.getCorreo() : "N/A", 
                        c.getTelefono(), 
                        c.getPuntos()
                    });
                }
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage());
        }
    }

    private String obtenerTextoFiltro(JTextField campo, String placeholder) {
        String texto = campo.getText().trim();
        return texto.equals(placeholder) ? "" : texto;
    }

    private void aplicarPlaceholder(JTextField campo, String placeholder) {
        campo.setText(placeholder);
        campo.setForeground(Color.GRAY);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campo.setHorizontalAlignment(JTextField.CENTER);
        
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setForeground(Color.GRAY);
                    campo.setText(placeholder);
                }
            }
        });
    }
}