package com.mycompany.presentacion.paneles.PanelesComandas;

import com.mycompany.presentacion.controlador.Coordinador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class PanelCrearComanda extends JPanel {
    private final Coordinador coordinador;
    private Image imagen;

    public PanelCrearComanda(Coordinador coordinador) {
        this.coordinador = coordinador;
        mostrar();
    }

    public void mostrar(){
        crearFondo();

        coordinador.cambiarTitulo("Creacion de comanda");
        setLayout(new BorderLayout());

        JPanel panelNorte = new JPanel();
        panelNorte.setOpaque(false);
        panelNorte.setLayout(new GridBagLayout());

        JPanel panelIzquierda = new JPanel();
        panelIzquierda.setOpaque(false);
        panelIzquierda.setLayout(new GridBagLayout());

        JPanel panelDerecha = new JPanel();
        panelDerecha.setOpaque(false);
        panelDerecha.setLayout(new GridBagLayout());

        JPanel panelSur = new JPanel();
        panelSur.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel labelTitulo = new JLabel("Mesa 1"); //Logica para que al seleccionar la mesa el titulo sea la mesa seleccionada pendiente
        labelTitulo.setFont(new Font("arial", Font.BOLD, 45));
        labelTitulo.setForeground(Color.WHITE);

        JLabel labelBusquedas = new JLabel("Resultados de la busqueda");
        labelBusquedas.setFont(new Font("arial", Font.BOLD, 20));
        labelBusquedas.setForeground(Color.WHITE);

        JLabel labelProductos = new JLabel("Productos");
        labelProductos.setFont(new Font("arial", Font.BOLD, 20));
        labelProductos.setForeground(Color.WHITE);

        JLabel labelFiltrar = new JLabel("Filtro");
        labelFiltrar.setFont(new Font("arial", Font.BOLD, 20));
        labelFiltrar.setForeground(Color.WHITE);

        String[] opciones = {"Todos", "Platillos", "Bebida", "Postre"};
        JComboBox<String> filtro = new JComboBox<>(opciones);

        JLabel labelBusqueda = new JLabel("Buscar Productos");
        labelBusqueda.setFont(new Font("arial", Font.BOLD, 25));
        labelBusqueda.setForeground(Color.WHITE);
        JTextField textfieldBusqueda = new JTextField(20);

        DefaultTableModel modeloProductos = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloProductos.addColumn("Nombre");
        modeloProductos.addColumn("Tipo");
        modeloProductos.addColumn("Precio");

        modeloProductos.addRow(new Object[]{"Tacos de asada",  "platillo",  "30.00"});
        JTable tablaProductos = new JTable(modeloProductos);
        tablaProductos.setRowHeight(35);
        JScrollPane scrollPaneProductos = new JScrollPane(tablaProductos);
        scrollPaneProductos.setPreferredSize(new Dimension(450, 270));

        tablaProductos.setBackground(new Color(255, 204, 204));
        tablaProductos.setSelectionBackground(new Color(255, 153, 153));

        JTableHeader header = tablaProductos.getTableHeader();
        header.setBackground(new Color(255, 204, 204));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JButton buttonBusqueda = new JButton("Buscar");
        JButton buttonRegresar = new JButton("Regresar");

        //Panel izquierdo
        gbc.insets = new Insets(-35, 35, 85, 35);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelIzquierda.add(labelBusquedas, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelIzquierda.add(labelBusqueda, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(-60, -115, 70, 215);
        panelIzquierda.add(labelFiltrar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panelIzquierda.add(filtro, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(-60, 35, 70, 35);
        panelIzquierda.add(scrollPaneProductos, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 90, 0);
        panelIzquierda.add(buttonRegresar, gbc);

        //Panel derecha
        DefaultTableModel modeloCuenta = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloCuenta.addColumn("Producto");
        modeloCuenta.addColumn("Cantidad");
        modeloCuenta.addColumn("Subtotal");
        modeloCuenta.addColumn("Comentarios");

        modeloCuenta.addRow(new Object[]{"Taco de asada",  "3",  "90.00", "La carne termino medio"});
        JTable tablaCuenta = new JTable(modeloCuenta);
        tablaCuenta.setRowHeight(40);
        JScrollPane scrollPaneCuenta = new JScrollPane(tablaCuenta);
        scrollPaneCuenta.setPreferredSize(new Dimension(450, 270));

        tablaCuenta.setBackground(new Color(255, 204, 204));
        tablaCuenta.setSelectionBackground(new Color(255, 153, 153));

        JTableHeader headerCuenta = tablaCuenta.getTableHeader();
        headerCuenta.setBackground(new Color(255, 204, 204));
        headerCuenta.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel labelCuenta = new JLabel("Cuenta");
        labelCuenta.setForeground(Color.WHITE);
        labelCuenta.setFont(new Font("arial", Font.BOLD, 20));

        JLabel labelTotal = new JLabel("Total: 90.00");
        labelTotal.setForeground(Color.WHITE);
        labelTotal.setFont(new Font("arial", Font.BOLD, 20));

        JButton buttonEliminar = new JButton("Eliminar");
        JButton buttonConfirmar = new JButton("Confirmar comanda");

        gbc.insets = new Insets(-45, 50, 60, 40);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelDerecha.add(labelCuenta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelDerecha.add(scrollPaneCuenta, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelDerecha.add(labelTotal, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, -360, 0, 0);
        panelDerecha.add(buttonEliminar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panelDerecha.add(buttonConfirmar, gbc);

        //Panel norte
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        panelNorte.add(labelTitulo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets( 0, 10, 0 , 5);
        panelNorte.add(labelBusqueda, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panelNorte.add(textfieldBusqueda, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        panelNorte.add(buttonBusqueda,gbc);
        add(panelIzquierda, BorderLayout.WEST);
        add(panelDerecha, BorderLayout.EAST);
        add(panelNorte, BorderLayout.NORTH);
    }

    private void crearFondo() {
        // Intentamos cargar la imagen desde la raiz de resources
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        }
    }

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
        }
        else {
            g.setColor(new Color(30, 30, 30));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
