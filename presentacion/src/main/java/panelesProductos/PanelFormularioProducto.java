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
import excepciones.NegocioException;
import excepciones.PersistenciaException;

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
import objetosnegocio.ProductoBO;
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
    private JLabel lblPreviewImagen;
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
        this.listaTemporalIngredientes = this.productoDTO.getIngredientes() != null
                ? new ArrayList<>(this.productoDTO.getIngredientes()) : new ArrayList<>();
        inicializarComponentes();

        if (this.modoEdicion) {
            cargarDatosEnFormulario();
        }
    }

    private ImageIcon cargarImagen(String ruta) {
        if (ruta == null || ruta.isEmpty()) return null;
        java.net.URL url = getClass().getResource(ruta);
        if (url != null) return new ImageIcon(url);
        File archivo = new File(ruta);
        if (archivo.exists()) return new ImageIcon(ruta);
        return null;
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

        lblPreviewImagen = new JLabel("Sin imagen");
        lblPreviewImagen.setPreferredSize(new Dimension(80, 80));
        lblPreviewImagen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblPreviewImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblPreviewImagen.setForeground(Color.LIGHT_GRAY);
        lblPreviewImagen.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        btnImagen.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Imágenes", "jpg", "jpeg", "png", "gif"
            ));
            int resultado = chooser.showOpenDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivo = chooser.getSelectedFile();
                try {
                    java.net.URL carpetaUrl = getClass().getResource("/");
                    if (carpetaUrl != null) {
                        File destino = new File(carpetaUrl.getPath() + archivo.getName());
                        java.nio.file.Files.copy(
                            archivo.toPath(),
                            destino.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING
                        );
                        productoDTO.setImagen("/" + archivo.getName());
                    } else {
                        productoDTO.setImagen(archivo.getAbsolutePath());
                    }
                } catch (Exception ex) {
                    productoDTO.setImagen(archivo.getAbsolutePath());
                }
                lblRutaImagen.setText(archivo.getName());
                ImageIcon icon = new ImageIcon(archivo.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                lblPreviewImagen.setIcon(new ImageIcon(img));
                lblPreviewImagen.setText("");
            }
        });

        int fila = 0;
        agregarFilaFormulario(panelFormulario, gbc, "Nombre del producto:", txtNombre, fila++);
        agregarFilaFormulario(panelFormulario, gbc, "Precio del Producto:", txtPrecio, fila++);
        agregarFilaFormulario(panelFormulario, gbc, "Descripción del Producto:", txtDescripcion, fila++);
        agregarFilaFormulario(panelFormulario, gbc, "Tipo del Producto:", cbTipo, fila++);
        agregarFilaFormulario(panelFormulario, gbc, "Estado:", cbEstado, fila++);

        gbc.gridy = fila++; gbc.gridx = 0; gbc.weightx = 0.3;
        gbc.gridwidth = 1;
        panelFormulario.add(crearEtiquetaGris("Imagen:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JPanel panelImg = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelImg.setOpaque(false);
        panelImg.add(btnImagen);
        panelImg.add(lblRutaImagen);
        panelImg.add(lblPreviewImagen);
        panelFormulario.add(panelImg, gbc);

        gbc.gridy = fila++; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnAnadirIngrediente = crearBotonAccion("Añadir Ingrediente", new Color(100, 200, 100));
        panelFormulario.add(btnAnadirIngrediente, gbc);

        // Tabla ingredientes
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 80, 10, 80));

        JLabel lblTabla = new JLabel("Ingredientes añadidos:", SwingConstants.CENTER);
        lblTabla.setForeground(Color.WHITE);
        lblTabla.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTabla.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelTabla.add(lblTabla, BorderLayout.NORTH);

        modeloIngredientes = new DefaultTableModel(new Object[]{
            "Nombre", "Unidad de Medida", "Cantidad actual", "Cantidad requerida", "Editar", "Eliminar"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaIngredientes = new JTable(modeloIngredientes);
        tablaIngredientes.setRowHeight(40);
        tablaIngredientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaIngredientes.setFillsViewportHeight(true);

        JTableHeader header = tablaIngredientes.getTableHeader();
        header.setBackground(new Color(110, 220, 200));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tablaIngredientes.getColumnModel().getColumn(0).setPreferredWidth(160);
        tablaIngredientes.getColumnModel().getColumn(1).setPreferredWidth(120);
        tablaIngredientes.getColumnModel().getColumn(2).setPreferredWidth(110);
        tablaIngredientes.getColumnModel().getColumn(3).setPreferredWidth(130);
        tablaIngredientes.getColumnModel().getColumn(4).setPreferredWidth(80);
        tablaIngredientes.getColumnModel().getColumn(5).setPreferredWidth(80);

        tablaIngredientes.getColumnModel().getColumn(4).setCellRenderer(new BotonEditarRenderer());
        tablaIngredientes.getColumnModel().getColumn(5).setCellRenderer(new BotonEliminarRenderer());

        configurarBotonesEnTabla();

        JScrollPane scrollTabla = new JScrollPane(tablaIngredientes);
        scrollTabla.setPreferredSize(new Dimension(800, 160));
        scrollTabla.setMinimumSize(new Dimension(800, 120));
        scrollTabla.getViewport().setBackground(Color.WHITE);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        JPanel centroContenedor = new JPanel(new BorderLayout());
        centroContenedor.setOpaque(false);
        centroContenedor.add(panelFormulario, BorderLayout.NORTH);
        centroContenedor.add(panelTabla, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        panelSur.setOpaque(false);

        JButton btnGuardar = crearBotonAccion("Guardar", new Color(110, 220, 110));
        JButton btnLimpiar = crearBotonAccion("Limpiar", new Color(70, 160, 220));
        btnLimpiar.setVisible(!modoEdicion);
        JButton btnRegresar = crearBotonAccion("Regresar", new Color(255, 80, 50));

        btnRegresar.addActionListener(e -> coordinador.mostrarPanelOpcionProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnAnadirIngrediente.addActionListener(e -> {
            JFrame padre = (JFrame) SwingUtilities.getWindowAncestor(this);
            PanelBuscarIngrediente dialogo = new PanelBuscarIngrediente(padre, this);
            dialogo.setVisible(true);
        });

        btnGuardar.addActionListener(e -> {
            try {
                if (txtNombre.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío", "Campo requerido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (txtPrecio.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El precio no puede estar vacío", "Campo requerido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (listaTemporalIngredientes.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debes añadir al menos un ingrediente", "Sin ingredientes", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                productoDTO.setNombre(txtNombre.getText().trim());
                productoDTO.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
                productoDTO.setDescripcion(txtDescripcion.getText().trim());
                productoDTO.setTipo(TipoProducto.valueOf(cbTipo.getSelectedItem().toString().toUpperCase()));
                productoDTO.setEstado(cbEstado.getSelectedItem().toString().equals("Activo"));
                productoDTO.setIngredientes(listaTemporalIngredientes);

                if (modoEdicion) {
                    ProductoBO.getInstance().editarProducto(productoDTO);
                    JOptionPane.showMessageDialog(this, "Producto editado correctamente.");
                } else {
                    ProductoBO.getInstance().registrarProducto(productoDTO);
                    JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
                }
                coordinador.mostrarPanelOpcionProducto();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número válido", "Error de formato", JOptionPane.ERROR_MESSAGE);
            } catch (NegocioException | PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelSur.add(btnGuardar);
        panelSur.add(btnLimpiar);
        panelSur.add(btnRegresar);

        panelFondo.add(titulo, BorderLayout.NORTH);

        JScrollPane scrollPrincipal = new JScrollPane(centroContenedor);
        scrollPrincipal.setOpaque(false);
        scrollPrincipal.getViewport().setOpaque(false);
        scrollPrincipal.setBorder(null);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);

        panelFondo.add(scrollPrincipal, BorderLayout.CENTER);
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
        lblPreviewImagen.setIcon(null);
        lblPreviewImagen.setText("Sin imagen");
        listaTemporalIngredientes.clear();
        actualizarTablaIngredientes();
    }

    public void agregarIngredienteDesdeDialogo(IngredienteDTO ingrediente, float cantidad) {
        boolean yaExiste = listaTemporalIngredientes.stream()
                .anyMatch(pi -> pi.getIdIngrediente().equals(ingrediente.getId()));

        if (yaExiste) {
            JOptionPane.showMessageDialog(this,
                "El ingrediente '" + ingrediente.getNombre() + "' ya fue añadido.\nPuedes editar su cantidad con el botón Editar.",
                "Ingrediente duplicado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProductoIngredienteDTO pi = new ProductoIngredienteDTO(
                ingrediente.getId(),
                ingrediente.getNombre(),
                cantidad,
                ingrediente.getUnidadMedida() != null ? ingrediente.getUnidadMedida().toString() : "-",
                ingrediente.getStock()
        );
        listaTemporalIngredientes.add(pi);
        actualizarTablaIngredientes();
    }

    private void cargarDatosEnFormulario() {
        txtNombre.setText(productoDTO.getNombre());
        txtPrecio.setText(productoDTO.getPrecio() != null ? String.valueOf(productoDTO.getPrecio()) : "");
        txtDescripcion.setText(productoDTO.getDescripcion());
        if (productoDTO.getTipo() != null) {
            cbTipo.setSelectedItem(productoDTO.getTipo().toString());
        }
        cbEstado.setSelectedItem(productoDTO.getEstado() != null && productoDTO.getEstado() ? "Activo" : "Inactivo");

        if (productoDTO.getImagen() != null && !productoDTO.getImagen().isEmpty()) {
            lblRutaImagen.setText(new File(productoDTO.getImagen()).getName());
            ImageIcon icon = cargarImagen(productoDTO.getImagen());
            if (icon != null) {
                Image img = icon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
                lblPreviewImagen.setIcon(new ImageIcon(img));
                lblPreviewImagen.setText("");
            }
        }

        actualizarTablaIngredientes();
    }

    private void actualizarTablaIngredientes() {
        modeloIngredientes.setRowCount(0);
        for (ProductoIngredienteDTO pi : listaTemporalIngredientes) {
            String unidad = pi.getUnidadMedida() != null ? pi.getUnidadMedida() : "-";
            String stock = pi.getStock() != null ? String.valueOf(pi.getStock()) : "-";
            modeloIngredientes.addRow(new Object[]{
                pi.getNombreIngrediente(),
                unidad,
                stock,
                pi.getCantidadRequerida(),
                "editar",
                "eliminar"
            });
        }
    }

    private void configurarBotonesEnTabla() {
        tablaIngredientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columna = tablaIngredientes.getColumnModel().getColumnIndexAtX(e.getX());
                int fila = e.getY() / tablaIngredientes.getRowHeight();

                if (fila < 0 || fila >= tablaIngredientes.getRowCount()) return;

                if (columna == 4) {
                    ProductoIngredienteDTO pi = listaTemporalIngredientes.get(fila);
                    String input = JOptionPane.showInputDialog(
                        PanelFormularioProducto.this,
                        "Nueva cantidad para '" + pi.getNombreIngrediente() + "':",
                        pi.getCantidadRequerida()
                    );
                    if (input != null && !input.trim().isEmpty()) {
                        try {
                            double nuevaCantidad = Double.parseDouble(input.trim());
                            if (nuevaCantidad <= 0) {
                                JOptionPane.showMessageDialog(PanelFormularioProducto.this,
                                    "La cantidad debe ser mayor a cero.", "Valor inválido", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            pi.setCantidadRequerida(nuevaCantidad);
                            actualizarTablaIngredientes();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(PanelFormularioProducto.this,
                                "Ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

                if (columna == 5) {
                    listaTemporalIngredientes.remove(fila);
                    actualizarTablaIngredientes();
                }
            }
        });
    }

    class BotonEditarRenderer extends JButton implements TableCellRenderer {
        public BotonEditarRenderer() {
            setOpaque(true);
            setBackground(new Color(70, 160, 220));
            setForeground(Color.WHITE);
            setText("editar");
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
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
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private void agregarFilaFormulario(JPanel panel, GridBagConstraints gbc, String label, JComponent campo, int fila) {
        gbc.gridy = fila; gbc.gridx = 0; gbc.weightx = 0.3;
        gbc.gridwidth = 1;
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