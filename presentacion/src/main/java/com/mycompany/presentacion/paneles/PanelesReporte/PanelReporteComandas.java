package com.mycompany.presentacion.paneles.PanelesReporte;

import com.mycompany.presentacion.controlador.Coordinador;
import dtos.ReporteComandaDTO;
import excepciones.NegocioException;
import objetosnegocio.ComandaBO;
import utilidades.UIUtils; 
import com.toedter.calendar.JDateChooser; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * 
 * @author julian izaguirre
 */
public class PanelReporteComandas extends JPanel {

    private final Coordinador coordinador;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtMesa, txtCliente;
    private JDateChooser txtFechaInicio, txtFechaFin;
    private JComboBox<String> cbEstado;
    private JLabel lblTotalPeriodo;
    private final ComandaBO bo = ComandaBO.getInstance();

    public PanelReporteComandas(Coordinador coordinador) {
        this.coordinador = coordinador;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        
        JPanel fondo = UIUtils.crearPanelFondo();
        fondo.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Reporte de Comandas", SwingConstants.CENTER);
        titulo.setFont(new Font("Monospaced", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        modelo = new DefaultTableModel(new Object[]{"Folio", "Estado", "Mesa", "Fecha y Hora", "Cliente", "Mesero", "Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setBackground(new Color(255, 204, 204));
        tabla.setSelectionBackground(new Color(255, 153, 153));
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(255, 204, 204));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false); scroll.getViewport().setOpaque(false);

        lblTotalPeriodo = new JLabel("Total del Periodo: $0.00", SwingConstants.CENTER);
        lblTotalPeriodo.setForeground(Color.WHITE);
        lblTotalPeriodo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalPeriodo.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JPanel panelTablaConTotal = new JPanel(new BorderLayout());
        panelTablaConTotal.setOpaque(false);
        panelTablaConTotal.add(scroll, BorderLayout.CENTER);
        panelTablaConTotal.add(lblTotalPeriodo, BorderLayout.SOUTH);

        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.Y_AXIS));
        panelFiltros.setOpaque(false);
        panelFiltros.setPreferredSize(new Dimension(280, 0));
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        panelFiltros.add(UIUtils.crearEtiquetaGris("Filtrar por número de mesa:"));
        txtMesa = new JTextField(); aplicarPlaceholder(txtMesa, "ingresar numero de mesa");
        panelFiltros.add(txtMesa); 
        panelFiltros.add(Box.createVerticalStrut(15));
        
        panelFiltros.add(UIUtils.crearEtiquetaGris("Fecha de Inicio:"));
        txtFechaInicio = new JDateChooser();
        txtFechaInicio.setDateFormatString("dd/MM/yyyy");
        // Forzamos el tamaño para que coincida con los JTextField
        txtFechaInicio.setPreferredSize(new Dimension(280, 40));
        txtFechaInicio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelFiltros.add(txtFechaInicio); 
        panelFiltros.add(Box.createVerticalStrut(10));
        
        panelFiltros.add(UIUtils.crearEtiquetaGris("Fecha de Fin:"));
        txtFechaFin = new JDateChooser();
        txtFechaFin.setDateFormatString("dd/MM/yyyy");
        txtFechaFin.setPreferredSize(new Dimension(280, 40));
        txtFechaFin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtFechaFin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelFiltros.add(txtFechaFin); 
        panelFiltros.add(Box.createVerticalStrut(15));

        // 3. Filtro Estado
        panelFiltros.add(UIUtils.crearEtiquetaGris("Filtrar por estado:"));
        cbEstado = new JComboBox<>(new String[]{"Todos", "Abierta", "Entregada", "Cancelada"});
        cbEstado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelFiltros.add(cbEstado); 
        panelFiltros.add(Box.createVerticalStrut(15));

        // 4. Filtro Cliente
        panelFiltros.add(UIUtils.crearEtiquetaGris("Filtrar por cliente:"));
        txtCliente = new JTextField(); aplicarPlaceholder(txtCliente, "ingrese cliente");
        panelFiltros.add(txtCliente); 
        panelFiltros.add(Box.createVerticalStrut(20));

        // Botón
        JButton btnFiltrar = UIUtils.crearBotonAccion("Aplicar Filtros", new Color(110, 220, 110));
        btnFiltrar.setForeground(Color.BLACK);
        btnFiltrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnFiltrar.addActionListener(e -> cargarDatos());
        panelFiltros.add(btnFiltrar);

        JPanel panelCentro = new JPanel(new BorderLayout(20, 0));
        panelCentro.setOpaque(false);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        panelCentro.add(panelTablaConTotal, BorderLayout.CENTER);
        panelCentro.add(panelFiltros, BorderLayout.EAST);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        panelSur.setOpaque(false);

        JButton btnRegresar = UIUtils.crearBotonAccion("Regresar", new Color(105, 105, 105));
        btnRegresar.setPreferredSize(new Dimension(220, 50));
        JButton btnGenerar = UIUtils.crearBotonAccion("Generar PDF", new Color(100, 200, 100));
        btnGenerar.setPreferredSize(new Dimension(220, 50));

        btnRegresar.addActionListener(e -> coordinador.cambiarPanel(new PanelMenuReportes(coordinador)));
        btnGenerar.addActionListener(e -> generarPDF());

        panelSur.add(btnRegresar);
        panelSur.add(btnGenerar);

        fondo.add(titulo, BorderLayout.NORTH);
        fondo.add(panelCentro, BorderLayout.CENTER);
        fondo.add(panelSur, BorderLayout.SOUTH);
        add(fondo, BorderLayout.CENTER);

        cargarDatos(); 
    }

    private void cargarDatos() {
        try {
            LocalDate fechaInicio = parsearFecha(txtFechaInicio);
            LocalDate fechaFin = parsearFecha(txtFechaFin);
            String mesa = obtenerTexto(txtMesa, "ingresar numero de mesa");
            String cliente = obtenerTexto(txtCliente, "ingrese cliente");
            String estadoSel = cbEstado.getSelectedItem().toString();
            String estado = estadoSel.equals("Todos") ? "" : estadoSel;

            List<ReporteComandaDTO> lista = bo.obtenerComandasFiltradas(fechaInicio, fechaFin, mesa, estado, cliente);
            modelo.setRowCount(0);
            double totalPeriodo = 0;

            for (ReporteComandaDTO c : lista) {
                modelo.addRow(new Object[]{ c.getFolio(), c.getEstado(), c.getNumeroMesa(), c.getFechaHora(), c.getNombreCliente(), c.getNombreMesero(), String.format("$%.2f", c.getTotal() != null ? c.getTotal() : 0.0) });
                totalPeriodo += c.getTotal() != null ? c.getTotal() : 0;
            }
            lblTotalPeriodo.setText(String.format("Total del Periodo: $%.2f", totalPeriodo));
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar comandas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarPDF() {
        try {
            JasperReport reporte = JasperCompileManager.compileReport("src/main/resources/ReporteComandas.jrxml");
            List<ReporteComandaDTO> datos = bo.obtenerComandasFiltradas(
                parsearFecha(txtFechaInicio), parsearFecha(txtFechaFin),
                obtenerTexto(txtMesa, "ingresar numero de mesa"), cbEstado.getSelectedItem().toString().equals("Todos") ? "" : cbEstado.getSelectedItem().toString(),
                obtenerTexto(txtCliente, "ingrese cliente")
            );

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datos);
            JasperPrint print = JasperFillManager.fillReport(reporte, null, dataSource);

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar Reporte PDF");
            chooser.setFileFilter(new FileNameExtensionFilter("PDF (*.pdf)", "pdf"));
            chooser.setSelectedFile(new File("ReporteComandas.pdf"));

            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String ruta = chooser.getSelectedFile().getAbsolutePath();
                if (!ruta.toLowerCase().endsWith(".pdf")) ruta += ".pdf";
                JasperExportManager.exportReportToPdfFile(print, ruta);
                JOptionPane.showMessageDialog(this, "PDF guardado en:\n" + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate parsearFecha(JDateChooser calendario) {
        java.util.Date date = calendario.getDate();
        if (date == null) {
            return null; 
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private String obtenerTexto(JTextField campo, String placeholder) {
        String t = campo.getText().trim();
        return t.equals(placeholder) ? "" : t;
    }

    private void aplicarPlaceholder(JTextField campo, String placeholder) {
        campo.setText(placeholder);
        campo.setForeground(Color.GRAY);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campo.setPreferredSize(new Dimension(280, 40));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { if (campo.getText().equals(placeholder)) { campo.setText(""); campo.setForeground(Color.BLACK); } }
            @Override public void focusLost(FocusEvent e) { if (campo.getText().isEmpty()) { campo.setForeground(Color.GRAY); campo.setText(placeholder); } }
        });
    }
}