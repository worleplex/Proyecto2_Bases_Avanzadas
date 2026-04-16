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
import javax.swing.table.TableCellRenderer;
import objetosnegocio.IngredienteBO;
import panelesProductos.PanelFormularioProducto;
import utilidades.UIUtils;

/**
 * Panel de interfaz grafica que muestra el catalogo completo de ingredientes.
 * Tambien funciona en modo "Seleccion" cuando es llamado desde el Formulario de Productos.
 * @author Gael Galaviz
 */
public class PanelCatalogoIngredientes extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtFiltro;
    private JComboBox<String> cbUnidadFiltro;
    private JButton btnBuscar;
    private final Coordinador coordinador;
    private final PanelFormularioProducto formularioPadre;

    /**
     * Constructor que inicializa el panel del catalogo
     * @param coordinador El coordinador general
     * @param formularioPadre El panel que lo llamó (null si viene del menú principal)
     */
    public PanelCatalogoIngredientes(Coordinador coordinador, PanelFormularioProducto formularioPadre) {
        this.coordinador = coordinador;
        this.formularioPadre = formularioPadre; 
        setLayout(new BorderLayout());
        inicializarComponentes();
        actualizarTabla("", "");
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = UIUtils.crearPanelFondo();
        panelPrincipal.setLayout(new BorderLayout());

        JLabel labelTitulo = new JLabel(formularioPadre == null ? "Catálogo de Ingredientes" : "Seleccione un Ingrediente", SwingConstants.CENTER);
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
        
        panelFiltros.add(lblNombre);
        panelFiltros.add(txtFiltro);
        panelFiltros.add(lblUnidad);
        panelFiltros.add(cbUnidadFiltro);
        panelFiltros.add(btnBuscar);

        JButton btnNuevo = crearBoton("Nuevo Ingrediente", new Color(102, 204, 102));
        if (formularioPadre == null) {
            btnNuevo.addActionListener(e -> abrirFormularioRegistro()); 
        
            panelFiltros.add(btnNuevo);
        }

        String[] columnas = formularioPadre != null 
                ? new String[]{"ID", "Nombre", "Stock", "Unidad", "Imagen", "Acción"}
                : new String[]{"ID", "Nombre", "Stock", "Unidad", "Imagen"};

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(60);
        configurarRendererImagen();
        
        if (formularioPadre != null) {
            tabla.getColumnModel().getColumn(5).setCellRenderer(new BotonAnadirRenderer());
        }
        
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (formularioPadre != null) {
                    int fila = tabla.getSelectedRow();
                    int col = tabla.getColumnModel().getColumnIndexAtX(e.getX());
                    
                    if (fila != -1 && (e.getClickCount() == 2 || col == 5)) {
                        try {
                            Long id = (Long) modelo.getValueAt(fila, 0);
                            String nom = (String) modelo.getValueAt(fila, 1);
                            Double stock = (Double) modelo.getValueAt(fila, 2);
                            String uni = (String) modelo.getValueAt(fila, 3);
                            String img = (String) modelo.getValueAt(fila, 4);

                            IngredienteDTO ingDTO = new IngredienteDTO();
                            ingDTO.setId(id);
                            ingDTO.setNombre(nom);
                            ingDTO.setStock(stock);
                            ingDTO.setImagen(img);
                            if (uni != null && !uni.isEmpty() && !uni.equals("-")) {
                                ingDTO.setUnidadMedida(entidades.UnidadMedida.valueOf(uni));
                            }

                            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1.0, 0.1, 10000.0, 0.5);
                            JSpinner spinner = new JSpinner(spinnerModel);
                            spinner.setFont(new Font("Segoe UI", Font.BOLD, 18));
                            
                            Object[] mensaje = {
                                "¿Cuánta cantidad de " + nom + " quieres añadir a la receta?",
                                spinner
                            };

                            int opcion = JOptionPane.showConfirmDialog(PanelCatalogoIngredientes.this, 
                                    mensaje, "Añadir Ingrediente", 
                                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                            if (opcion == JOptionPane.OK_OPTION) {
                                float cantidadSeleccionada = ((Double) spinner.getValue()).floatValue();
                                boolean seAgregoBien = formularioPadre.agregarIngredienteDesdeDialogo(ingDTO, cantidadSeleccionada);
                                if (seAgregoBien) {
                                    coordinador.cambiarPanel(formularioPadre);
                                }
                            }
                            
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error al seleccionar: " + ex.getMessage());
                        }
                    }
                }
            }
        });

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
        btnRegresar.addActionListener(e -> {
            if (formularioPadre != null) {
                coordinador.cambiarPanel(formularioPadre);
            } else {
                coordinador.mostrarPanelMenuAdmin();
            }
        });

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelAcciones.setOpaque(false);

        if (formularioPadre == null) {
            JButton btnStock = crearBoton("Ajustar Stock", new Color(255, 180, 50));
            JButton btnEliminar = crearBoton("Eliminar", new Color(255, 80, 50));
            btnStock.addActionListener(e -> ajustarStock());
            btnEliminar.addActionListener(e -> eliminarSeleccionado());
            panelAcciones.add(btnStock);
            panelAcciones.add(btnEliminar);
        } else {
            JLabel aviso = new JLabel("Clic en Añadir para mandar el ingrediente a la receta");
            aviso.setForeground(new Color(200, 255, 200));
            aviso.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            panelAcciones.add(aviso);
        }

        panelSur.add(btnRegresar, BorderLayout.WEST);
        panelSur.add(panelAcciones, BorderLayout.EAST);

        panelPrincipal.add(labelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelContenedorCentro, BorderLayout.CENTER);
        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        add(panelPrincipal, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> {
            String u = cbUnidadFiltro.getSelectedItem().toString();
            actualizarTabla(txtFiltro.getText().trim(), u.equals("Todas") ? "" : u);
        });
    }

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
            JOptionPane.showMessageDialog(this, "Seleccione un ingrediente");
        }
    }

    public void actualizarTabla(String nombre, String unidad) {
        try {
            modelo.setRowCount(0);
            List<IngredienteDTO> lista = IngredienteBO.getInstance().buscarIngredientesFiltrados(nombre, unidad);
            
            for (IngredienteDTO i : lista) {
                String uniStr = i.getUnidadMedida() != null ? i.getUnidadMedida().toString() : "";
                
                if (formularioPadre != null) {
                    modelo.addRow(new Object[]{i.getId(), i.getNombre(), i.getStock(), uniStr, i.getImagen(), "Añadir"});
                } else {
                    modelo.addRow(new Object[]{i.getId(), i.getNombre(), i.getStock(), uniStr, i.getImagen()});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            Long id = (Long) modelo.getValueAt(fila, 0);
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
    private void abrirFormularioRegistro() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        DialogFormularioIngrediente dialogo = new DialogFormularioIngrediente(parent, null);
        dialogo.setVisible(true);
        btnBuscar.doClick();
    }
    
    private JButton crearBoton(String t, Color c) {
        JButton b = new JButton(t);
        b.setPreferredSize(new Dimension(180, 40));
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    class BotonAnadirRenderer extends JButton implements TableCellRenderer {
        public BotonAnadirRenderer() {
            setOpaque(true);
            setBackground(new Color(100, 200, 100)); // Verde
            setForeground(Color.WHITE);
            setText("Añadir");
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
}