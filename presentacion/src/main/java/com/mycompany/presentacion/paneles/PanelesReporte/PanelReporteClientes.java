package com.mycompany.presentacion.paneles.PanelesReporte;

import com.mycompany.presentacion.controlador.Coordinador;
import dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import java.util.List;
import objetosnegocio.ClienteFrecuenteBO;
import utilidades.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * @author julian izaguirre
 */
public class PanelReporteClientes extends JPanel {
    
    private final Coordinador coordinador;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtFiltroNombre;
    private JSpinner spnVisitas;
    private ClienteFrecuenteBO bo = ClienteFrecuenteBO.getInstance();

    public PanelReporteClientes(Coordinador coordinador) {
        this.coordinador = coordinador;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        JPanel fondo = UIUtils.crearPanelFondo();
        fondo.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Reporte de clientes frecuentes", SwingConstants.CENTER);
        titulo.setFont(new Font("Monospaced", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel panelCentro = new JPanel(new BorderLayout(30, 0));
        panelCentro.setOpaque(false);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        modelo = new DefaultTableModel(new Object[]{ "ID", "Nombres", "Apellido Paterno", "Apellido Materno", "Numero de visitas", "Total gastado", "Fecha ultima comanda" }, 0);
        
        tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setBackground(new Color(255, 204, 204));
        tabla.setSelectionBackground(new Color(255, 153, 153));
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(255, 204, 204));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setOpaque(false);
        scrollTabla.getViewport().setOpaque(false);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.Y_AXIS));
        panelFiltros.setOpaque(false);
        panelFiltros.setPreferredSize(new Dimension(300, 0));
        //
        panelFiltros.add(UIUtils.crearEtiquetaGris("Mínimo de visitas (0 = todos):"));
        spnVisitas = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
        spnVisitas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        spnVisitas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelFiltros.add(spnVisitas);
        panelFiltros.add(Box.createVerticalStrut(20));
        

        panelFiltros.add(UIUtils.crearEtiquetaGris("Filtrar por Nombre:"));
        txtFiltroNombre = new JTextField(); aplicarPlaceholder(txtFiltroNombre, "ingrese el nombre");
        panelFiltros.add(txtFiltroNombre); panelFiltros.add(Box.createVerticalStrut(30));
        JButton btnBuscar = UIUtils.crearBotonAccion("Aplicar Filtros", new Color(110, 220, 110));
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBuscar.addActionListener(e -> cargarDatos());
        panelFiltros.add(btnBuscar);

        panelCentro.add(panelFiltros, BorderLayout.EAST);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        panelSur.setOpaque(false);

        JButton btnRegresar = UIUtils.crearBotonAccion("Regresar", new Color(105, 105, 105));
        btnRegresar.setPreferredSize(new Dimension(250, 50));
        
        JButton btnGenerar = UIUtils.crearBotonAccion("Generar Reporte PDF", new Color(100, 200, 100));
        btnGenerar.setPreferredSize(new Dimension(250, 50));
        btnRegresar.addActionListener(e -> coordinador.cambiarPanel(new PanelMenuReportes(coordinador)));
        btnGenerar.addActionListener(e -> generarReportePDF());

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
            modelo.setRowCount(0);
            String fNombre = obtenerTextoFiltro(txtFiltroNombre, "ingrese el nombre");
            int minimoVisitas = (int) spnVisitas.getValue();
            List<ClienteFrecuenteDTO> lista = bo.obtenerDatosReporteClientes(fNombre, minimoVisitas);

            for (ClienteFrecuenteDTO c : lista) {
                modelo.addRow(new Object[]{
                    c.getId(),
                    c.getNombres(),
                    c.getApellidoPaterno(),
                    c.getApellidoMaterno(),
                    c.getVisitas(),
                    String.format("$%.2f", c.getTotalGastado() != null ? c.getTotalGastado() : 0.0),
                    c.getFechaUltimaComanda()
                });
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage());
        }
    }
    
    private void generarReportePDF() {
        try {
            JasperReport reporte = JasperCompileManager.compileReport("src/main/resources/ReporteClientes.jrxml");
            JRTableModelDataSource dataSource = new JRTableModelDataSource(modelo);
            JasperPrint print = JasperFillManager.fillReport(reporte, null, dataSource);
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Reporte PDF");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos PDF (*.pdf)", "pdf"));
            fileChooser.setSelectedFile(new File("ReporteMaye.pdf"));
            
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String ruta = fileChooser.getSelectedFile().getAbsolutePath();
                if (!ruta.toLowerCase().endsWith(".pdf")) { ruta += ".pdf"; }
                JasperExportManager.exportReportToPdfFile(print, ruta);
                JOptionPane.showMessageDialog(this, "Reporte guardado exitosamente en:\n" + ruta, "Descarga Completada", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al descargar el reporte PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerTextoFiltro(JTextField campo, String placeholder) {
        String texto = campo.getText().trim();
        return texto.equals(placeholder) ? "" : texto;
    }

    private void aplicarPlaceholder(JTextField campo, String placeholder) {
        campo.setText(placeholder);
        campo.setForeground(Color.GRAY);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { if (campo.getText().equals(placeholder)) { campo.setText(""); campo.setForeground(Color.BLACK); } }
            @Override public void focusLost(FocusEvent e) { if (campo.getText().isEmpty()) { campo.setForeground(Color.GRAY); campo.setText(placeholder); } }
        });
    }
}