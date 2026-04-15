/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panelesProductos;

import com.mycompany.presentacion.controlador.Coordinador;
import java.awt.*;
import javax.swing.*;
import utilidades.UIUtils;

/**
 *
 * @author julian izaguirre
 */
public class PanelOpcionProducto extends JPanel{
    private final Coordinador coordinador;
    
    public PanelOpcionProducto(Coordinador coordinador) {
        this.coordinador = coordinador;
        iniciarlizarComponentes();
    }
    
    private void iniciarlizarComponentes() {
        setLayout(new BorderLayout());
        
        JPanel panelFondo = UIUtils.crearPanelFondo();

        JLabel labelTitulo = new JLabel("Productos", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 45));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(60, 0, 20, 0));

        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 0, 40));
        panelCentro.setOpaque(false);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(70, 250, 120, 250));

        JButton btnAgregar = UIUtils.crearBotonBlanco("Agregar Producto");
        JButton btnEditar = UIUtils.crearBotonBlanco("Editar Producto");

        panelCentro.add(btnAgregar);
        panelCentro.add(btnEditar);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 40));
        panelSur.setOpaque(false);
        
        JButton btnRegresar = UIUtils.crearBotonAccion("Regresar", new Color(105, 105, 105));
        btnRegresar.setPreferredSize(new Dimension(220, 50)); // Lo hacemos un poco más grande
        panelSur.add(btnRegresar);

        btnRegresar.addActionListener(e -> coordinador.mostrarPanelMenuAdmin());
        btnAgregar.addActionListener(e -> coordinador.mostrarPanelFormularioProducto(false, null));
        btnEditar.addActionListener(e -> coordinador.mostrarPanelBuscarProducto());

        panelFondo.add(labelTitulo, BorderLayout.NORTH);
        panelFondo.add(panelCentro, BorderLayout.CENTER);
        panelFondo.add(panelSur, BorderLayout.SOUTH);

        add(panelFondo, BorderLayout.CENTER);
    }
}