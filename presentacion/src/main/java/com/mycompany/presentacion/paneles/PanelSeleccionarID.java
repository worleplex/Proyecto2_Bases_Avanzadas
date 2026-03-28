package com.mycompany.presentacion.paneles;

import adaptadores.ClienteFrecuenteAdapter;
import dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import javax.swing.*;
import java.awt.*;
import objetosnegocio.ClienteFrecuenteBO;

/**
 *
 * @author Gael Galaviz
 * Clase para buscar un cliente por su ID antes de
 * realizar una accion.
 */
public class PanelSeleccionarID extends JPanel {

    private String accion; // Define si vamos a editar o eliminar
    private JTextField txtId;
    private ClienteFrecuenteBO bo = ClienteFrecuenteBO.getInstance();

    public PanelSeleccionarID(String accion) {
        this.accion = accion;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        JPanel fondo = crearFondo();
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
        JButton btnOk = boton("Continuar", new Color(46, 180, 100));
        JButton btnCan = boton("Regresar", new Color(200, 50, 30));

        btnOk.addActionListener(e -> procesar());
        btnCan.addActionListener(e -> regresar());

        pB.add(btnOk);
        pB.add(btnCan);

        contenido.add(lbl, BorderLayout.NORTH);
        contenido.add(txtId, BorderLayout.CENTER);
        contenido.add(pB, BorderLayout.SOUTH);

        fondo.add(contenido);
        add(fondo, BorderLayout.CENTER);
    }

    /**
     * Valida el ID ingresado y abre el siguiente paso (Editar o Eliminar).
     */
    private void procesar() {
        try {
            Long id = Long.parseLong(txtId.getText());
            
            // 1. Buscamos si el cliente existe en la BD
            ClienteFrecuenteDTO dto = bo.buscarClientePorId(id);
            
            if (dto != null) {
                // 2. Si sí existe, revisamos qué botón presionó el usuario al principio
                if (accion.equals("editar")) {
                    abrir(new PanelEditarCliente(ClienteFrecuenteAdapter.dtoAEntidad(dto)));
                } else {
                    // ¡AQUÍ ESTÁ LA MAGIA! Lo mandamos a tu nuevo panel de eliminar
                    abrir(new PanelEliminarClienteFrecuente(ClienteFrecuenteAdapter.dtoAEntidad(dto)));
                }
            } else {
                JOptionPane.showMessageDialog(this, "ID no encontrado en la base de datos.");
            }
            
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido. Por favor ingresa solo números.");
        }
    }

    private void abrir(JPanel p) {
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
        v.setContentPane(p);
        v.revalidate();
    }

    private void regresar() {
        abrir(new PanelMenuClientes());
    }

    private JButton boton(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(110, 38));
        return b;
    }

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
