/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panelesProductos;

import com.mycompany.presentacion.controlador.Coordinador;
import dtos.ProductoDTO;
import excepciones.NegocioException;
import java.awt.*;
import javax.swing.*;
import java.util.List;
import objetosnegocio.ProductoBO;
import utilidades.UIUtils;

/**
 *
 * @author julian izaguirre
 */
public class PanelBuscarProducto extends JPanel {
    private final Coordinador coordinador;
    private JTextField txtBuscar;

    public PanelBuscarProducto(Coordinador coordinador) {
        this.coordinador = coordinador;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        JPanel panelFondo = UIUtils.crearPanelFondo();

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("Ingrese el nombre del Producto:", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(Color.WHITE);
        
        gbc.gridy = 0;
        panelCentro.add(lblTitulo, gbc);

        txtBuscar = new JTextField();
        txtBuscar.setPreferredSize(new Dimension(350, 50));
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 20));
         
        UIUtils.aplicarPlaceholder(txtBuscar, "nombre del producto");
        
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE; 
        panelCentro.add(txtBuscar, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelBotones.setOpaque(false);

        JButton btnContinuar = UIUtils.crearBotonAccion("continuar", new Color(100, 200, 100)); 
        JButton btnRegresar = UIUtils.crearBotonAccion("Regresar", new Color(255, 80, 50)); 

        panelBotones.add(btnContinuar);
        panelBotones.add(btnRegresar);

        gbc.gridy = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        panelCentro.add(panelBotones, gbc);

        // Acciones
        btnRegresar.addActionListener(e -> coordinador.mostrarPanelOpcionProducto());

        btnContinuar.addActionListener(e -> {
            String busqueda = txtBuscar.getText().trim();
            if (busqueda.isEmpty() || busqueda.equals("nombre del producto")) {
                JOptionPane.showMessageDialog(this, "ingrese un nombre del product pa  buscar");
                return;
            }
            try {
                // este cambio lo menciono en mi video
                List<ProductoDTO> resultados = ProductoBO.getInstance().buscarProductosFiltrados(busqueda, null, null);
                if (resultados == null || resultados.isEmpty()) {
                    JOptionPane.showMessageDialog(this,"no hay ningun producto con ese name","Sin resultados",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (resultados.size() == 1) {
                    coordinador.mostrarPanelFormularioProducto(true, resultados.get(0));
                    return;
                }
                
                String[] opciones = resultados.stream().map(ProductoDTO::getNombre).toArray(String[]::new);
                String seleccionado = (String) JOptionPane.showInputDialog(this,"Se encontraron varios productos, seleccione uno:", "Seleccionar Producto",
                        JOptionPane.PLAIN_MESSAGE, null,opciones,opciones[0]);
                
                if (seleccionado == null) return;
                
                ProductoDTO productoSeleccionado = resultados.stream().filter(p -> p.getNombre().equals(seleccionado))
                        .findFirst().orElse(null);

                if (productoSeleccionado != null) {
                    coordinador.mostrarPanelFormularioProducto(true, productoSeleccionado);
                }
            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelFondo.add(panelCentro, BorderLayout.CENTER);
        add(panelFondo, BorderLayout.CENTER);
    }
}
