/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panelesIngredientes;

import dtos.IngredienteDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 *
 * @author julian izaguirre
 */
public class DialogSeleccionarIngrediente extends JDialog {

    private Image imagen;
    private IngredienteDTO ingredienteSeleccionado;
    private float cantidadSeleccionada = 1f;
    private JSpinner spinner;
    private List<IngredienteDTO> ingredientes;
    private DefaultTableModel modelo;

    public DialogSeleccionarIngrediente(JFrame padre, List<IngredienteDTO> ingredientes) {
        super(padre, "Seleccionar Ingrediente", true);
        this.ingredientes = ingredientes;
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        }
        setSize(650, 480);
        setLocationRelativeTo(padre);
        setResizable(false);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
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

        JLabel lblTitulo = new JLabel("Seleccione la cantidad deseada", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1.0, 0.1, 10000.0, 0.5);
        spinner = new JSpinner(spinnerModel);
        spinner.setPreferredSize(new Dimension(100, 40));
        spinner.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel panelSpinner = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSpinner.setOpaque(false);
        panelSpinner.add(spinner);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setOpaque(false);
        panelNorte.add(lblTitulo, BorderLayout.NORTH);
        panelNorte.add(panelSpinner, BorderLayout.CENTER);

        modelo = new DefaultTableModel(
            new Object[]{"Nombre", "Unidad de Medida", "Cantidad actual", "Agregar"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (IngredienteDTO i : ingredientes) {
            modelo.addRow(new Object[]{
                i.getNombre(),
                i.getUnidadMedida() != null ? i.getUnidadMedida().toString() : "-",
                i.getStock(),
                "Agregar"
            });
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(45);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(110, 220, 200));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabla.getColumnModel().getColumn(3).setCellRenderer(new BotonAgregarRenderer());

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tabla.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / tabla.getRowHeight();
                if (row >= 0 && row < tabla.getRowCount() && col == 3) {
                    ingredienteSeleccionado = ingredientes.get(row);
                    cantidadSeleccionada = ((Double) spinner.getValue()).floatValue();
                    dispose();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JButton btnSalir = new JButton("Salir");
        btnSalir.setPreferredSize(new Dimension(160, 45));
        btnSalir.setBackground(new Color(255, 80, 50));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> dispose());
        panelSur.add(btnSalir);

        panelFondo.add(panelNorte, BorderLayout.NORTH);
        panelFondo.add(scroll, BorderLayout.CENTER);
        panelFondo.add(panelSur, BorderLayout.SOUTH);
        setContentPane(panelFondo);
    }

    public IngredienteDTO getIngredienteSeleccionado() { return ingredienteSeleccionado; }
    public float getCantidadSeleccionada() { return cantidadSeleccionada; }

    class BotonAgregarRenderer extends JButton implements TableCellRenderer {
        public BotonAgregarRenderer() {
            setOpaque(true);
            setBackground(new Color(100, 200, 100));
            setForeground(Color.WHITE);
            setText("Agregar");
            setFont(new Font("Segoe UI", Font.BOLD, 13));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
}
