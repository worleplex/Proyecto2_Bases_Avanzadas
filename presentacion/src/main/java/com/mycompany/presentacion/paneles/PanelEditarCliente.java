package com.mycompany.presentacion.paneles;

import adaptadores.ClienteFrecuenteAdapter;
import dtos.ClienteFrecuenteDTO;
import entidades.ClienteFrecuente;
import excepciones.NegocioException;
import javax.swing.*;
import java.awt.*;
import objetosnegocio.ClienteFrecuenteBO;

/**
 *
 * @author Gael Galaviz
 * Clase para modificar los datos de un cliente existente.
 */
public class PanelEditarCliente extends JPanel {

    private ClienteFrecuente cliente; // Datos actuales del cliente
    private JTextField txtNombre, txtApPaterno, txtApMaterno, txtCorreo, txtTelefono;
    private ClienteFrecuenteBO bo =  ClienteFrecuenteBO.getInstance();

    public PanelEditarCliente(ClienteFrecuente cliente) {
        this.cliente = cliente;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        JPanel fondo = crearFondo();
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

        pC.add(etiqueta("Nombre:"));
        pC.add(txtNombre);
        pC.add(etiqueta("Ap. Paterno:"));
        pC.add(txtApPaterno);
        pC.add(etiqueta("Ap. Materno:"));
        pC.add(txtApMaterno);
        pC.add(etiqueta("Correo:"));
        pC.add(txtCorreo);
        pC.add(etiqueta("Telefono:"));
        pC.add(txtTelefono);

        JPanel pB = new JPanel(new FlowLayout());
        pB.setOpaque(false);
        JButton btnG = botonVerde("Actualizar");
        JButton btnC = botonRojo("Cancelar");

        btnG.addActionListener(e -> actualizar());
        btnC.addActionListener(e -> regresar());

        pB.add(btnG);
        pB.add(btnC);

        fondo.add(t, BorderLayout.NORTH);
        fondo.add(pC, BorderLayout.CENTER);
        fondo.add(pB, BorderLayout.SOUTH);
        add(fondo, BorderLayout.CENTER);
    }

    /**
     * Toma los nuevos datos de los campos y actualiza al cliente.
     */
    private void actualizar() {
        try {
            cliente.setNombres(txtNombre.getText());
            cliente.setApellido_paterno(txtApPaterno.getText());
            cliente.setApellido_materno(txtApMaterno.getText());
            cliente.setCorreo(txtCorreo.getText());
            cliente.setTelefono(txtTelefono.getText());

            ClienteFrecuenteDTO dto = ClienteFrecuenteAdapter.entidadADTO(cliente);

            // ¡Corrección! Usamos editarCliente en lugar de guardarCliente
            bo.editarCliente(dto);

            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
            regresar();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage());
        }
    }

    private void regresar() {
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
        v.setContentPane(new PanelMenuClientes());
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
        b.setPreferredSize(new Dimension(120, 38));
        return b;
    }

    private JPanel crearFondo() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // ¡Corrección de la ruta de la imagen!
                java.net.URL url = getClass().getResource("/com/mycompany/presentacion/fondos/fondoInicio.png");
                if (url != null) {
                    Image img = new ImageIcon(url).getImage();
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
                }
            }
        };
        p.setOpaque(false);
        return p;
    }
}