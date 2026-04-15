package panelesProductos;

import com.mycompany.presentacion.controlador.Coordinador;
import dtos.IngredienteDTO;
import dtos.ProductoDTO;
import dtos.ProductoIngredienteDTO;
import entidades.TipoProducto;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import objetosnegocio.ProductoBO;
import panelesIngredientes.PanelBuscarIngrediente;
import utilidades.UIUtils;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import static utilidades.UIUtils.crearPanelFondo;

/**
 * Panel para registrar o editar un producto del restaurante.
 * Permite añadir ingredientes, seleccionar imagen y guardar en BD.
 *
 * @author julian izaguirre
 */
public class PanelFormularioProducto extends JPanel {

    private static final Logger LOG = Logger.getLogger(PanelFormularioProducto.class.getName());

    private final Coordinador coordinador;
    private final boolean modoEdicion;
    private ProductoDTO productoDTO;

    private List<ProductoIngredienteDTO> listaTemporalIngredientes;

    private JTextField txtNombre, txtPrecio, txtDescripcion;
    private JComboBox<String> cbTipo, cbEstado;
    private JLabel lblRutaImagen;
    private JLabel lblPreviewImagen;
    private DefaultTableModel modeloIngredientes;
    private JTable tablaIngredientes;

    public PanelFormularioProducto(Coordinador coordinador, boolean modoEdicion, ProductoDTO productoDTO) {
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

    // Carga imagen desde classpath o ruta absoluta
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

        JPanel panelFondo = UIUtils.crearPanelFondo();
        panelFondo.setLayout(new BorderLayout());

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

        JButton btnImagen = UIUtils.crearBotonAccion("Seleccionar", new Color(70, 130, 180));
        btnImagen.setPreferredSize(new Dimension(140, 35));

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
                    "Imágenes", "jpg", "jpeg", "png", "gif"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File archivo = chooser.getSelectedFile();
                try {
                    java.net.URL carpetaUrl = getClass().getResource("/");
                    if (carpetaUrl != null) {
                        File destino = new File(carpetaUrl.getPath() + archivo.getName());
                        java.nio.file.Files.copy(archivo.toPath(), destino.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        productoDTO.setImagen("/" + archivo.getName());
                        LOG.log(Level.INFO, "Imagen copiada a resources: {0}", archivo.getName());
                    } else {
                        productoDTO.setImagen(archivo.getAbsolutePath());
                    }
                } catch (Exception ex) {
                    LOG.log(Level.WARNING, "No se pudo copiar imagen, se usará ruta absoluta: {0}", ex.getMessage());
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

        gbc.gridy = fila++; gbc.gridx = 0; gbc.weightx = 0.3; gbc.gridwidth = 1;
        panelFormulario.add(UIUtils.crearEtiquetaGris("Imagen:"), gbc);
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
        JButton btnAnadirIngrediente = UIUtils.crearBotonAccion("Añadir Ingrediente", new Color(100, 200, 100));
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
            public boolean isCellEditable(int row, int column) { return false; }
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

        tablaIngredientes.getColumnModel().getColumn(4).setCellRenderer(new BotonColorRenderer("editar", new Color(70, 160, 220)));
        tablaIngredientes.getColumnModel().getColumn(5).setCellRenderer(new BotonColorRenderer("eliminar", Color.RED));

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

        JButton btnGuardar  = UIUtils.crearBotonAccion("Guardar",  new Color(110, 220, 110));
        JButton btnLimpiar  = UIUtils.crearBotonAccion("Limpiar",  new Color(70, 160, 220));
        JButton btnRegresar = UIUtils.crearBotonAccion("Regresar", new Color(255, 80, 50));
        btnLimpiar.setVisible(!modoEdicion);

        btnRegresar.addActionListener(e -> coordinador.mostrarPanelOpcionProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnAnadirIngrediente.addActionListener(e -> {
            JFrame padre = (JFrame) SwingUtilities.getWindowAncestor(this);
            PanelBuscarIngrediente dialogo = new PanelBuscarIngrediente(padre, this);
            dialogo.setVisible(true);
        });

        btnGuardar.addActionListener(e -> guardar());

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

    private void guardar() {
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
                LOG.log(Level.INFO, "Editando producto: {0}", productoDTO.getNombre());
                ProductoBO.getInstance().editarProducto(productoDTO);
                JOptionPane.showMessageDialog(this, "Producto editado correctamente");
            } else {
                LOG.log(Level.INFO, "Registrando producto: {0}", productoDTO.getNombre());
                ProductoBO.getInstance().registrarProducto(productoDTO);
                JOptionPane.showMessageDialog(this, "Producto registrado correctamente");
            }
            coordinador.mostrarPanelOpcionProducto();

        } catch (NumberFormatException ex) {
            LOG.log(Level.WARNING, "Precio inválido ingresado: {0}", txtPrecio.getText());
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (NegocioException | PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al guardar producto: {0}", ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        LOG.info("Campos del formulario limpiados");
    }

    public void agregarIngredienteDesdeDialogo(IngredienteDTO ingrediente, float cantidad) {
        if (ingrediente.getStock() != null && cantidad > ingrediente.getStock()) {
            JOptionPane.showMessageDialog(this,
                "No puedes requerir " + cantidad + " " + (ingrediente.getUnidadMedida() != null ? ingrediente.getUnidadMedida() : "") + ".\n" +
                "Solo hay " + ingrediente.getStock() + " en inventario",
                "Stock Insuficiente", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean yaExiste = listaTemporalIngredientes.stream()
                .anyMatch(pi -> pi.getIdIngrediente().equals(ingrediente.getId()));

        if (yaExiste) {
            JOptionPane.showMessageDialog(this,
                "El ingrediente '" + ingrediente.getNombre() + "' ya fue añadido\nEdita su cantidad con el botón Editar",
                "Ingrediente duplicado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProductoIngredienteDTO pi = new ProductoIngredienteDTO(
                ingrediente.getId(), ingrediente.getNombre(), cantidad,
                ingrediente.getUnidadMedida() != null ? ingrediente.getUnidadMedida().toString() : "-",
                ingrediente.getStock());
        listaTemporalIngredientes.add(pi);
        actualizarTablaIngredientes();
        LOG.log(Level.INFO, "Ingrediente añadido: {0}", ingrediente.getNombre());
    }

    private void cargarDatosEnFormulario() {
        LOG.log(Level.INFO, "Cargando datos del producto: {0}", productoDTO.getNombre());
        txtNombre.setText(productoDTO.getNombre());
        txtPrecio.setText(productoDTO.getPrecio() != null ? String.valueOf(productoDTO.getPrecio()) : "");
        txtDescripcion.setText(productoDTO.getDescripcion());
        
        if (productoDTO.getTipo() != null) {
            String tipoBD = productoDTO.getTipo().toString(); 
            for (int i = 0; i < cbTipo.getItemCount(); i++) {
                if (cbTipo.getItemAt(i).equalsIgnoreCase(tipoBD)) {
                    cbTipo.setSelectedIndex(i);
                    break;
                }
            }
        }

        if (productoDTO.getEstado() != null) {
            cbEstado.setSelectedItem(productoDTO.getEstado() ? "Activo" : "Inactivo");
        }

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
                pi.getNombreIngrediente(), unidad, stock,
                pi.getCantidadRequerida(), "editar", "eliminar"
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

                if (columna == 4) { // Editar
                    ProductoIngredienteDTO pi = listaTemporalIngredientes.get(fila);
                    String input = JOptionPane.showInputDialog(PanelFormularioProducto.this,
                        "Nueva cantidad para '" + pi.getNombreIngrediente() + "':",
                        pi.getCantidadRequerida());
                    if (input != null && !input.trim().isEmpty()) {
                        try {
                            double nuevaCantidad = Double.parseDouble(input.trim());
                            if (nuevaCantidad <= 0) {
                                JOptionPane.showMessageDialog(PanelFormularioProducto.this,
                                    "La cantidad debe ser mayor a cero", "Valor inválido", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            
                            if (pi.getStock() != null && nuevaCantidad > pi.getStock()) {
                                JOptionPane.showMessageDialog(PanelFormularioProducto.this,
                                    "Stock insuficiente. Solo hay " + pi.getStock() + " disponibles en inventario", 
                                    "Error de Stock", JOptionPane.ERROR_MESSAGE);
                                return; 
                            }
                            
                            pi.setCantidadRequerida(nuevaCantidad);
                            actualizarTablaIngredientes();
                            LOG.log(Level.INFO, "Cantidad editada para: {0}", pi.getNombreIngrediente());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(PanelFormularioProducto.this,
                                "Ingresa un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

                if (columna == 5) { // Eliminar
                    String nombre = listaTemporalIngredientes.get(fila).getNombreIngrediente();
                    listaTemporalIngredientes.remove(fila);
                    actualizarTablaIngredientes();
                    LOG.log(Level.INFO, "Ingrediente eliminado de la lista: {0}", nombre);
                }
            }
        });
    }

    class BotonColorRenderer extends JButton implements TableCellRenderer {
        public BotonColorRenderer(String texto, Color color) {
            setOpaque(true);
            setBackground(color);
            setForeground(Color.WHITE);
            setText(texto);
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
        gbc.gridy = fila; gbc.gridx = 0; gbc.weightx = 0.3; gbc.gridwidth = 1;
        panel.add(UIUtils.crearEtiquetaGris(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        campo.setPreferredSize(new Dimension(300, 35));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(campo, gbc);
    }
}