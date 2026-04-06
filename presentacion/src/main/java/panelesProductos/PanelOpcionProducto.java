/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panelesProductos;

import com.mycompany.presentacion.controlador.Coordinador;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author julian izaguirre
 */
public class PanelOpcionProducto extends JPanel{
    private final Coordinador coordinador;
    private Image imagen;
    
    public PanelOpcionProducto(Coordinador coordinador) {
        this.coordinador = coordinador;
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        } else {
            System.err.println("Error: No se encontro FondoInicio.png en la raiz de resources");
        }
        iniciarlizarComponentes();
    }
    
    private void cambiarPanel(JPanel panel) {
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (v != null) {
            v.setContentPane(panel);
            v.revalidate();
            v.repaint();
        }
    }
    
    private void iniciarlizarComponentes() {
        setLayout(new BorderLayout());

        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagen != null) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
                }
            }
        };
        panelFondo.setOpaque(false);

        JLabel labelTitulo = new JLabel("Productos", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 45));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(60, 0, 20, 0));

        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 0, 40));
        panelCentro.setOpaque(false);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(70, 250, 120, 250));

        JButton btnAgregar = crearBotonBlanco("Agregar Producto");
        JButton btnEditar = crearBotonBlanco("Editar Producto");

        panelCentro.add(btnAgregar);
        panelCentro.add(btnEditar);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 40));
        panelSur.setOpaque(false);
        
        JButton btnRegresar = crearBotonGris("Regresar");
        panelSur.add(btnRegresar);

        btnRegresar.addActionListener(e -> {
            coordinador.mostrarPanelMenuAdmin();
        });
        
        btnAgregar.addActionListener(e -> {
            coordinador.mostrarPanelFormularioProducto(false, null);
        });

        btnEditar.addActionListener(e -> {
            coordinador.mostrarPanelBuscarProducto();
        });

        panelFondo.add(labelTitulo, BorderLayout.NORTH);
        panelFondo.add(panelCentro, BorderLayout.CENTER);
        panelFondo.add(panelSur, BorderLayout.SOUTH);

        add(panelFondo, BorderLayout.CENTER);
    }
    
    private JButton crearBotonBlanco(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(Color.WHITE);
        boton.setForeground(Color.BLACK);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JButton crearBotonGris(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(220, 50));
        btn.setBackground(new Color(105, 105, 105));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
}
