/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panelesProductos;

import com.mycompany.presentacion.controlador.Coordinador;
import dtos.IngredienteDTO;
import dtos.ProductoDTO;
import dtos.ProductoIngredienteDTO;
import entidades.TipoProducto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import panelesIngredientes.PanelBuscarIngrediente;
import panelesProductos.PanelOpcionProducto;

/**
 *
 * @author julian izaguirre
 */
public class PanelFormularioProducto extends JPanel {
    
    private final Coordinador coordinador;
    private final boolean modoEdicion;
    private ProductoDTO productoDTO;
    private Image imagen;
    
    private List<ProductoIngredienteDTO> listaTemporalIngredientes;

    private JTextField txtNombre, txtPrecio, txtDescripcion;
    private JComboBox<String> cbTipo, cbEstado;
    private JLabel lblRutaImagen;
    private DefaultTableModel modeloIngredientes;
    private JTable tablaIngredientes;

    public PanelFormularioProducto(Coordinador coordinador, boolean modoEdicion, ProductoDTO productoDTO) {
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        } else {
            System.err.println("Error: No se encontro FondoInicio.png en la raiz de resources");
        }
        this.coordinador = coordinador;
        this.modoEdicion = modoEdicion;
        this.productoDTO = (productoDTO != null) ? productoDTO : new ProductoDTO();
        this.listaTemporalIngredientes = this.productoDTO.getIngredientes() != null ? 
                new ArrayList<>(this.productoDTO.getIngredientes()) : new ArrayList<>();
        inicializarComponentes();
        
        if (this.modoEdicion) {
            cargarDatosEnFormulario();
        }
    }

    private void inicializarComponentes() {
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

        String textoTitulo = modoEdicion ? "Edición de Producto" : "Registro de Producto";
        JLabel titulo = new JLabel(textoTitulo, SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(20);
        txtPrecio = new JTextField(20);
        txtDescripcion = new JTextField(20);
        cbTipo = new JComboBox<>(new String[]{"Platillo", "Bebida", "Postre"});
        cbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo"});

        JButton btnImagen = new JButton("Seleccionar");
        lblRutaImagen = new JLabel("Ninguna");
        lblRutaImagen.setForeground(Color.LIGHT_GRAY);
        // aver intento en lo de la imagen
        
        btnImagen.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes", "jpg", "jpeg", "png", "gif"
            ));

            int resultado = chooser.showOpenDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivo = chooser.getSelectedFile();
                lblRutaImagen.setText(archivo.getName());
                productoDTO.setImagen(archivo.getAbsolutePath());
            }
        });
        
        int fila = 0;
        agregarFilaFormulario(panelFormulario, gbc, "Nombre del producto:", txtNombre, fila++);
        agregarFilaFormulario(panelFormulario, gbc, "Precio del Producto:", txtPrecio, fila++);
        agregarFilaFormulario(panelFormulario, gbc, "Descripción del Producto:", txtDescripcion, fila++);
        agregarFilaFormulario(panelFormulario, gbc, "Tipo del Producto:", cbTipo, fila++);
        agregarFilaFormulario(panelFormulario, gbc, "Estado:", cbEstado, fila++);

        gbc.gridy = fila++; gbc.gridx = 0;
        panelFormulario.add(crearEtiquetaGris("Imagen:"), gbc);
        gbc.gridx = 1;
        JPanel panelImg = new JPanel(new BorderLayout(10, 0));
        panelImg.setOpaque(false);
        panelImg.add(btnImagen, BorderLayout.WEST);
        panelImg.add(lblRutaImagen, BorderLayout.CENTER);
        panelFormulario.add(panelImg, gbc);

        gbc.gridy = fila++; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnAnadirIngrediente = crearBotonAccion("Añadir Ingrediente", new Color(100, 200, 100));
        panelFormulario.add(btnAnadirIngrediente, gbc);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 80, 10, 80));

        JLabel lblTabla = new JLabel("Ingredientes añadidos:", SwingConstants.CENTER);
        lblTabla.setForeground(Color.WHITE);
        lblTabla.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelTabla.add(lblTabla, BorderLayout.NORTH);

        modeloIngredientes = new DefaultTableModel(new Object[]{
            "Nombre", "Unidad de Medida", "Cantidad actual", "Cantidad requerida", "Acción"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaIngredientes = new JTable(modeloIngredientes);
        tablaIngredientes.setRowHeight(40);
        JTableHeader header = tablaIngredientes.getTableHeader();
        header.setBackground(new Color(110, 220, 200));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tablaIngredientes.getColumnModel().getColumn(4).setCellRenderer(new BotonEliminarRenderer());
        configurarBotonEliminarEnTabla();

        JScrollPane scrollTabla = new JScrollPane(tablaIngredientes);
        scrollTabla.setPreferredSize(new Dimension(800, 180));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        JPanel centroContenedor = new JPanel(new BorderLayout());
        centroContenedor.setOpaque(false);
        centroContenedor.add(panelFormulario, BorderLayout.NORTH);
        centroContenedor.add(panelTabla, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        panelSur.setOpaque(false);

        JButton btnGuardar = crearBotonAccion("Guardar", new Color(110, 220, 110));
        JButton btnLimpiar = crearBotonAccion("Limpiar", new Color(70, 160, 220)); // FIX: botón limpiar
        JButton btnRegresar = crearBotonAccion("Regresar", new Color(255, 80, 50));

        btnRegresar.addActionListener(e -> coordinador.mostrarPanelOpcionProducto());

        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnAnadirIngrediente.addActionListener(e -> {
            JFrame padre = (JFrame) SwingUtilities.getWindowAncestor(this);
            PanelBuscarIngrediente dialogo = new PanelBuscarIngrediente(padre);
            dialogo.setVisible(true);
        });

        btnGuardar.addActionListener(e -> {
            try {
                productoDTO.setNombre(txtNombre.getText().trim());
                productoDTO.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
                productoDTO.setDescripcion(txtDescripcion.getText().trim());
                productoDTO.setTipo(TipoProducto.valueOf(cbTipo.getSelectedItem().toString().toUpperCase()));
                productoDTO.setEstado(cbEstado.getSelectedItem().toString().equals("Activo"));
                productoDTO.setIngredientes(listaTemporalIngredientes);

                JOptionPane.showMessageDialog(this, "El producto se ha guardado correctamente en el catálogo");
                coordinador.mostrarPanelOpcionProducto();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "El precio debe ser un número válido.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        panelSur.add(btnGuardar);
        panelSur.add(btnLimpiar);
        panelSur.add(btnRegresar);

        panelFondo.add(titulo, BorderLayout.NORTH);
        panelFondo.add(centroContenedor, BorderLayout.CENTER);
        panelFondo.add(panelSur, BorderLayout.SOUTH);
        add(panelFondo, BorderLayout.CENTER);
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtDescripcion.setText("");
        cbTipo.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);
        lblRutaImagen.setText("Ninguna");
        listaTemporalIngredientes.clear();
        actualizarTablaIngredientes();
    }
    // intento en lo de añadir ingredientes
    /**
     * 
     * @param ingrediente
     * @param cantidad 
     */
    public void agregarIngredienteDesdeDialogo(IngredienteDTO ingrediente, float cantidad) {
        ProductoIngredienteDTO pi = new ProductoIngredienteDTO(
            ingrediente.getId(),
            ingrediente.getNombre(),
            cantidad
        );
        listaTemporalIngredientes.add(pi);
        actualizarTablaIngredientes();
    }
    
    /**
     * 
     */
    private void cargarDatosEnFormulario() {
        txtNombre.setText(productoDTO.getNombre());
        txtPrecio.setText(productoDTO.getPrecio() != null ? String.valueOf(productoDTO.getPrecio()) : "");
        txtDescripcion.setText(productoDTO.getDescripcion());
        
        if (productoDTO.getTipo() != null) {
            cbTipo.setSelectedItem(productoDTO.getTipo().toString());
        }
        cbEstado.setSelectedItem(productoDTO.getEstado() ? "Activo" : "Inactivo");

        actualizarTablaIngredientes();
    }

    private void actualizarTablaIngredientes() {
        modeloIngredientes.setRowCount(0);
        for (ProductoIngredienteDTO pi : listaTemporalIngredientes) {
            modeloIngredientes.addRow(new Object[]{
                pi.getNombreIngrediente(), 
                "Gramos",
                "1000",   
                pi.getCantidadRequerida(), 
                "eliminar"
            });
        }
    }

    private void configurarBotonEliminarEnTabla() {
        tablaIngredientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columna = tablaIngredientes.getColumnModel().getColumnIndexAtX(e.getX());
                int fila = e.getY() / tablaIngredientes.getRowHeight();
                
                if (fila < tablaIngredientes.getRowCount() && fila >= 0 && columna == 4) {
                    listaTemporalIngredientes.remove(fila);
                    actualizarTablaIngredientes(); // Refrescamos
                }
            }
        });
    }
    
    
    class BotonEliminarRenderer extends JButton implements TableCellRenderer {
        public BotonEliminarRenderer() {
            setOpaque(true);
            setBackground(Color.RED);
            setForeground(Color.WHITE);
            setText("eliminar");
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private void agregarFilaFormulario(JPanel panel, GridBagConstraints gbc, String label, JComponent campo, int fila) {
        gbc.gridy = fila; gbc.gridx = 0; gbc.weightx = 0.3;
        panel.add(crearEtiquetaGris(label), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        campo.setPreferredSize(new Dimension(300, 35));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(campo, gbc);
    }

    private JLabel crearEtiquetaGris(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(210, 210, 210)); 
        lbl.setForeground(Color.BLACK);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setPreferredSize(new Dimension(250, 35));
        return lbl;
    }

    private JButton crearBotonAccion(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}