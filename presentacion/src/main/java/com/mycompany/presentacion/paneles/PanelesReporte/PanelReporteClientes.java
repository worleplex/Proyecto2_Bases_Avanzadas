package com.mycompany.presentacion.paneles.PanelesReporte;

import com.mycompany.presentacion.controlador.Coordinador;
import dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import java.util.List;
import objetosnegocio.ClienteFrecuenteBO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
// dependencias para generar PDF
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;


/**
 *
 * @author julian izaguirre
 */
public class PanelReporteClientes extends JPanel {
    
    private final Coordinador coordinador;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtFiltroVisitas, txtFiltroNombre;
    private ClienteFrecuenteBO bo = ClienteFrecuenteBO.getInstance();

    private void cambiarPanel(JPanel panel) {
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (v != null) {
            v.setContentPane(panel);
            v.revalidate();
            v.repaint();
        }
    }

    public PanelReporteClientes(Coordinador coordinador) {
        this.coordinador = coordinador;
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        JPanel fondo = crearFondo();
        fondo.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Reporte de clientes frecuentes", SwingConstants.CENTER);
        titulo.setFont(new Font("Monospaced", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel panelCentro = new JPanel(new BorderLayout(30, 0));
        panelCentro.setOpaque(false);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        modelo = new DefaultTableModel(new Object[]{
            "ID", "Nombres", "Apellido Paterno", "Apellido Materno", "Numero de visitas", "Total gastado", "Fecha ultima comanda"
        }, 0);
        
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

        panelFiltros.add(crearEtiquetaFiltro("Filtrar por numero de visitas:"));
        txtFiltroVisitas = new JTextField();
        aplicarPlaceholder(txtFiltroVisitas, "ingrese el numero de visitas");
        panelFiltros.add(txtFiltroVisitas);
        panelFiltros.add(Box.createVerticalStrut(20));

        panelFiltros.add(crearEtiquetaFiltro("Filtrar por Nombre:"));
        txtFiltroNombre = new JTextField();
        aplicarPlaceholder(txtFiltroNombre, "ingrese el nombre");
        panelFiltros.add(txtFiltroNombre);
        panelFiltros.add(Box.createVerticalStrut(30));

        JButton btnBuscar = boton("Aplicar Filtros", new Color(110, 220, 110));
        btnBuscar.setForeground(Color.BLACK);
        btnBuscar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBuscar.addActionListener(e -> cargarDatos());
        panelFiltros.add(btnBuscar);

        panelCentro.add(panelFiltros, BorderLayout.EAST);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        panelSur.setOpaque(false);

        JButton btnRegresar = boton("Regresar", new Color(105, 105, 105));
        btnRegresar.setPreferredSize(new Dimension(250, 50));
        
        JButton btnGenerar = boton("Generar Reporte PDF", new Color(100, 200, 100));
        btnGenerar.setPreferredSize(new Dimension(250, 50));

        btnRegresar.addActionListener(e -> cambiarPanel(new PanelMenuReportes(coordinador)));
        
        btnGenerar.addActionListener(e -> {
            generarReportePDF();
        });

        panelSur.add(btnRegresar);
        panelSur.add(btnGenerar);

        fondo.add(titulo, BorderLayout.NORTH);
        fondo.add(panelCentro, BorderLayout.CENTER);
        fondo.add(panelSur, BorderLayout.SOUTH);
        add(fondo, BorderLayout.CENTER);
        cargarDatos();
    }

    /**
     * Obtiene los clientes de la BD y los dibuja en la tabla
     */
    private void cargarDatos() {
        try {
            modelo.setRowCount(0); 
            List<ClienteFrecuenteDTO> listaCompleta = bo.obtenerTodosLosClientes();
            
            String fVisitas = obtenerTextoFiltro(txtFiltroVisitas, "ingrese el numero de visitas");
            String fNombre = obtenerTextoFiltro(txtFiltroNombre, "ingrese el nombre").toLowerCase();

            for (ClienteFrecuenteDTO c : listaCompleta) {
                String nomCliente = c.getNombres() != null ? c.getNombres().toLowerCase() : "";
                
                String visitasCalculadas = "10"; // Aquí iría: String.valueOf(c.getVisitas())
                String totalCalculado = "$1,500.00"; // Aquí iría: c.getTotalGastado()
                String fechaUltima = "20/03/2026"; // Aquí iría: c.getFechaUltimaComanda()

                boolean pasaNombre = fNombre.isEmpty() || nomCliente.contains(fNombre);
                boolean pasaVisitas = fVisitas.isEmpty() || visitasCalculadas.equals(fVisitas);

                if (pasaNombre && pasaVisitas) {
                    modelo.addRow(new Object[]{
                        c.getId(), 
                        c.getNombres(), 
                        c.getApellidoPaterno(), 
                        c.getApellidoMaterno(), 
                        visitasCalculadas, 
                        totalCalculado, 
                        fechaUltima
                    });
                }
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos de BD: " + ex.getMessage());
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
            // aquip es un nombre por defecto y como soy fan de la maye lo puse asip
            fileChooser.setSelectedFile(new File("ReporteMaye.pdf"));
            int seleccion = fileChooser.showSaveDialog(this);
            
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File archivoSeleccionado = fileChooser.getSelectedFile();
                String ruta = archivoSeleccionado.getAbsolutePath();
                if (!ruta.toLowerCase().endsWith(".pdf")) {
                    ruta += ".pdf";
                }
                JasperExportManager.exportReportToPdfFile(print, ruta);

                JOptionPane.showMessageDialog(this, 
                    "report descargado y guardado exitosamente en:\n" + ruta, 
                    "Descarga Completada", 
                    JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al descargar el reporte PDF: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerTextoFiltro(JTextField campo, String placeholder) {
        String texto = campo.getText().trim();
        if (texto.equals(placeholder)) {
            return ""; 
        }
        return texto;
    }

    private void aplicarPlaceholder(JTextField campo, String placeholder) {
        campo.setText(placeholder);
        campo.setForeground(Color.GRAY);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campo.setHorizontalAlignment(JTextField.CENTER);
        
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setForeground(Color.GRAY);
                    campo.setText(placeholder);
                }
            }
        });
    }

    private JLabel crearEtiquetaFiltro(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(77, 184, 255));
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return lbl;
    }

    private JButton boton(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 18));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel crearFondo() {
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        Image img = url != null ? new ImageIcon(url).getImage() : null;
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (img != null) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
                }
            }
        };
        p.setOpaque(false);
        return p;
    }
}