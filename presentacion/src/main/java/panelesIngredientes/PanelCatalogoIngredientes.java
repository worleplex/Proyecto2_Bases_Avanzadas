/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panelesIngredientes;
import com.mycompany.presentacion.controlador.Coordinador;
import java.util.List; 
import dtos.IngredienteDTO;
import excepciones.NegocioException;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import objetosnegocio.IngredienteBO;
/**
 * Panel de interfaz grafica que muestra el catalogo completo de ingredientes.
 * @author Gael Galaviz
 */
public class PanelCatalogoIngredientes extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtFiltro;
    private JComboBox<String> cbUnidadFiltro;
    private JButton btnBuscar;
    private Image imagenFondo;
    private final Coordinador coordinador;
    /**
     * Constructor que inicializa el panel del catalogo.
     */
    public PanelCatalogoIngredientes(Coordinador coordinador) {
        this.coordinador = coordinador;
        cargarFondo();
        setLayout(new BorderLayout());
        inicializarComponentes();
        actualizarTabla("", "");
    }
    /**
     * Carga la imagen de fondo.
     */
    private void cargarFondo() {
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagenFondo = new ImageIcon(url).getImage();
        }
    }
    /**
     * Inicializa todos los componentes del panel.
     */
    private void inicializarComponentes() {
        JLabel labelTitulo = new JLabel("Catalogo de Ingredientes", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 45));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFiltros.setOpaque(false);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 16));

        txtFiltro = new JTextField(15);
        txtFiltro.setPreferredSize(new Dimension(150, 35));

        JLabel lblUnidad = new JLabel("Unidad:");
        lblUnidad.setForeground(Color.WHITE);
        lblUnidad.setFont(new Font("Segoe UI", Font.BOLD, 16));

        cbUnidadFiltro = new JComboBox<>(new String[]{"Todas", "GRAMO", "MILILITRO", "PIEZA"});
        cbUnidadFiltro.setPreferredSize(new Dimension(130, 35));

        btnBuscar = crearBoton("Buscar", new Color(51, 153, 255));
        JButton btnNuevo = crearBoton("Nuevo Ingrediente", new Color(102, 204, 102));

        panelFiltros.add(lblNombre);
        panelFiltros.add(txtFiltro);
        panelFiltros.add(lblUnidad);
        panelFiltros.add(cbUnidadFiltro);
        panelFiltros.add(btnBuscar);
        panelFiltros.add(btnNuevo);

        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Stock", "Unidad", "Imagen"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(60);
        configurarRendererImagen();

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 50));

        JPanel panelContenedorCentro = new JPanel(new BorderLayout());
        panelContenedorCentro.setOpaque(false);
        panelContenedorCentro.add(panelFiltros, BorderLayout.NORTH);
        panelContenedorCentro.add(scroll, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setPreferredSize(new Dimension(150, 45));
        btnRegresar.setBackground(new Color(105, 105, 105));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnRegresar.addActionListener(e -> coordinador.mostrarPanelMenuAdmin());

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelAcciones.setOpaque(false);

        JButton btnStock = crearBoton("Ajustar Stock", new Color(255, 180, 50));
        JButton btnEliminar = crearBoton("Eliminar", new Color(255, 80, 50));

        panelAcciones.add(btnStock);
        panelAcciones.add(btnEliminar);

        panelSur.add(btnRegresar, BorderLayout.WEST);
        panelSur.add(panelAcciones, BorderLayout.EAST);

        add(labelTitulo, BorderLayout.NORTH);
        add(panelContenedorCentro, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            String u = cbUnidadFiltro.getSelectedItem().toString();
            actualizarTabla(txtFiltro.getText().trim(), u.equals("Todas") ? "" : u);
        });

        btnNuevo.addActionListener(e -> abrirFormulario(null));

        btnStock.addActionListener(e -> ajustarStock());

        btnEliminar.addActionListener(e -> eliminarSeleccionado());
    }

    /**
     * Configura el renderizador personalizado para la columna de imagenes.
     */
    private void configurarRendererImagen() {
        tabla.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);

                if (isSelected) {
                    label.setBackground(t.getSelectionBackground());
                    label.setOpaque(true);
                }

                if (value != null && !value.toString().isEmpty()) {
                    String ruta = value.toString();
                    ImageIcon icono = null;

                    if (ruta.startsWith("/")) {
                        java.net.URL imgURL = getClass().getResource(ruta);
                        if (imgURL != null) {
                            icono = new ImageIcon(imgURL);
                        }
                    } 
                    else {
                        File archivo = new File(ruta);
                        if (archivo.exists()) {
                            icono = new ImageIcon(ruta);
                        }
                    }

                    if (icono != null && icono.getImage() != null) {
                        Image img = icono.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(img));
                    } else {
                        label.setText("Sin foto");
                    }
                }
                return label;
            }
        });
    }
    /**
     * Muestra un cuadro de dialogo para sumar o restar cantidad al stock del
     * ingrediente seleccionado en la tabla.
     */
    private void ajustarStock() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            Long id = (Long) modelo.getValueAt(fila, 0);
            String nom = (String) modelo.getValueAt(fila, 1);
            String res = JOptionPane.showInputDialog(this, "Ajuste de Stock para " + nom + " EJ: 10 para sumar, -10 para restar");
            if (res != null) {
                try {
                    Double cant = Double.parseDouble(res);
                    IngredienteBO.getInstance().actualizarStock(id, cant);
                    btnBuscar.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un ingrediente.");
        }
    }
    /**
     * 
     */
    public void actualizarTabla(String nombre, String unidad) {
        try {
            modelo.setRowCount(0);
            List<IngredienteDTO> lista = IngredienteBO.getInstance().buscarIngredientes(nombre, unidad);
            for (IngredienteDTO i : lista) {
                modelo.addRow(new Object[]{i.getId(), i.getNombre(), i.getStock(), i.getUnidadMedida(), i.getImagen()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Elimina el ingrediente seleccionado de la tabla tras una confirmacion.
     */
    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            Long id = (Long) modelo.getValueAt(fila, 0);
            String nombre = (String) modelo.getValueAt(fila, 1);
            int conf = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminarlo?");
            if (conf == JOptionPane.YES_OPTION) {
                try {
                    IngredienteBO.getInstance().eliminarIngrediente(id);
                    btnBuscar.doClick();
                } catch (NegocioException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }else {
            JOptionPane.showMessageDialog(this, "Seleccione un ingrediente.");
        }
    }
    /**
     * Abre el dialogo de formulario para registrar un nuevo ingrediente o
     * editar uno existente.
     */
    private void abrirFormulario(IngredienteDTO dto) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new DialogFormularioIngrediente(frame, dto).setVisible(true);
        btnBuscar.doClick();
    }
    /**
     * Crea un boton estilizado con el esquema.
     */
    private JButton crearBoton(String t, Color c) {
        JButton b = new JButton(t);
        b.setPreferredSize(new Dimension(180, 40));
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }
    /**
     * Dibujo para renderizar la imagen de fondo.
     * 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
