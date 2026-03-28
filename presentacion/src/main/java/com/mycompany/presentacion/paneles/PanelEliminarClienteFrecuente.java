package com.mycompany.presentacion.paneles;

import entidades.ClienteFrecuente;
import excepciones.NegocioException;
import javax.swing.*;
import java.awt.*;
import objetosnegocio.ClienteFrecuenteBO;

/**
 *
 * @author julian izaguirre
 * Panel para confirmar y eliminar un cliente de la base de datos.
 */
public class PanelEliminarClienteFrecuente extends JPanel {

    private ClienteFrecuente cliente; // El cliente que vamos a eliminar
    private ClienteFrecuenteBO bo = ClienteFrecuenteBO.getInstance();

    // Recibimos al cliente desde el PanelSeleccionarID (Igual que en Editar)
    public PanelEliminarClienteFrecuente(ClienteFrecuente cliente) {
        this.cliente = cliente;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        JPanel fondo = crearFondo();
        fondo.setLayout(new BorderLayout());

        JLabel t = new JLabel("¿Seguro que deseas eliminar este cliente?", SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 26));
        t.setForeground(new Color(255, 100, 100)); // Un rojo clarito para advertir
        t.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        // Panel central para mostrar los datos bloqueados
        JPanel pC = new JPanel(new GridLayout(3, 2, 10, 15));
        pC.setOpaque(false);
        pC.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        // Mostramos los datos para que el usuario confirme, pero bloqueamos la edición
        JTextField txtNombre = new JTextField(cliente.getNombres() + " " + cliente.getApellido_paterno());
        txtNombre.setEditable(false); 
        
        JTextField txtCorreo = new JTextField(cliente.getCorreo());
        txtCorreo.setEditable(false);
        
        JTextField txtPuntos = new JTextField(String.valueOf(cliente.getPuntos()));
        txtPuntos.setEditable(false);

        pC.add(etiqueta("Nombre completo:"));
        pC.add(txtNombre);
        pC.add(etiqueta("Correo electrónico:"));
        pC.add(txtCorreo);
        pC.add(etiqueta("Puntos acumulados:"));
        pC.add(txtPuntos);

        // Botones
        JPanel pB = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        pB.setOpaque(false);
        JButton btnEliminar = boton("Sí, Eliminar", new Color(200, 50, 30)); // Rojo
        JButton btnCancelar = boton("Cancelar", new Color(100, 100, 100)); // Gris

        btnEliminar.addActionListener(e -> eliminar());
        btnCancelar.addActionListener(e -> regresar());

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
                regresar();
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
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
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        return l;
    }

    private JButton boton(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(140, 45));
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
            System.err.println("Error: No se encontro FondoInicio.jpg en la raiz de resources");
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
