/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.presentacion.paneles;
import dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import java.util.List;
import java.awt.RenderingHints;
import javax.swing.table.DefaultTableModel;
import objetosnegocio.ClienteFrecuenteBO;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Gael Galaviz
 * Clase que muestra la lista completa de clientes en
 * una tabla.
 */
public class PanelConsultarClientes extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;// Maneja los datos de la tabla
    private JTextField txtFiltro;
    private ClienteFrecuenteBO bo = ClienteFrecuenteBO.getInstance();

    public PanelConsultarClientes() {
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        JPanel fondo = crearFondo();
        fondo.setLayout(new BorderLayout());

        JLabel t = new JLabel("Consulta de Clientes", SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 28));
        t.setForeground(Color.WHITE);
        t.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        txtFiltro = new JTextField(20);
        JButton btnBus = botonVerde("Buscar");
        btnBus.addActionListener(e -> cargarDatos());

        JPanel pS = new JPanel(new FlowLayout());
        pS.setOpaque(false);
        pS.add(new JLabel("Nombre/Tel:") {
            {
                setForeground(Color.WHITE);
            }
        });
        pS.add(txtFiltro);
        pS.add(btnBus);

        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Telefono", "Puntos"}, 0);
        tabla = new JTable(modelo);
        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);

        JButton btnR = botonRojo("Regresar");
        btnR.addActionListener(e -> regresar());

        fondo.add(t, BorderLayout.NORTH);
        fondo.add(pS, BorderLayout.BEFORE_FIRST_LINE);
        fondo.add(btnR, BorderLayout.SOUTH);
        add(fondo, BorderLayout.CENTER);

        cargarDatos();
    }
    /**
     *Solicita al BO la lista de clientes y los dibuja en la tabla.
     */
    private void cargarDatos() {
        try {
            modelo.setRowCount(0);
            List<ClienteFrecuenteDTO> lista = bo.obtenerTodosLosClientes(); // Método correcto
            for (ClienteFrecuenteDTO c : lista) {
                modelo.addRow(new Object[]{c.getId(), c.getNombres(), c.getTelefono(), c.getPuntos()});
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    private void regresar() {
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
        v.setContentPane(new PanelMenuClientes());
        v.revalidate();
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
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return b;
    }

    private JPanel crearFondo() {
        Image img = new ImageIcon("presentacion\\src\\main\\java\\com\\mycompany\\presentacion\\fondos\\FondoInicio.png").getImage();
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            }
        };
        p.setOpaque(false);
        return p;
    }
}

    