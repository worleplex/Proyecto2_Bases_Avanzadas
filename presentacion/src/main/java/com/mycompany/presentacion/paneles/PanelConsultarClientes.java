package com.mycompany.presentacion.paneles;

import dtos.ClienteFrecuenteDTO;
import excepciones.NegocioException;
import java.util.List;
import java.awt.RenderingHints;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import objetosnegocio.ClienteFrecuenteBO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 *
 * @author Gael Galaviz / Adaptación visual por Julian
 * Clase que muestra la lista completa de clientes con filtros avanzados.
 */
public class PanelConsultarClientes extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtFiltroNombre, txtFiltroTelefono, txtFiltroCorreo;
    private ClienteFrecuenteBO bo = ClienteFrecuenteBO.getInstance();

    public PanelConsultarClientes() {
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout());
        JPanel fondo = crearFondo();
        fondo.setLayout(new BorderLayout());

        // --- TÍTULO ---
        JLabel t = new JLabel("Consultar clientes", SwingConstants.CENTER);
        t.setFont(new Font("Segoe UI", Font.BOLD, 36));
        t.setForeground(Color.WHITE);
        t.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        // --- PANEL CENTRAL (Tabla a la izquierda, Filtros a la derecha) ---
        JPanel panelCentral = new JPanel(new BorderLayout(20, 0));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 40));

        // 1. Configuración de la Tabla (Izquierda)
        modelo = new DefaultTableModel(new Object[]{
            "Nombre", "Apellido paterno", "Apellido materno", "Correo electrónico", "Teléfono", "Puntos de fidelidad"
        }, 0);
        
        tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setBackground(new Color(255, 204, 204)); // Color durazno/rosita de la imagen
        tabla.setSelectionBackground(new Color(255, 153, 153));
        
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(255, 204, 204));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane sp = new JScrollPane(tabla);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        panelCentral.add(sp, BorderLayout.CENTER);

        // 2. Configuración del Panel de Filtros (Derecha)
        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.Y_AXIS));
        panelFiltros.setOpaque(false);
        panelFiltros.setPreferredSize(new Dimension(300, 0)); // Ancho fijo para los filtros

        // Filtro Nombre
        panelFiltros.add(crearEtiquetaFiltro("Filtrar por nombre:"));
        txtFiltroNombre = new JTextField();
        aplicarPlaceholder(txtFiltroNombre, "ingresar el nombre del cliente");
        panelFiltros.add(txtFiltroNombre);
        panelFiltros.add(Box.createVerticalStrut(20)); // Espaciador

        // Filtro Teléfono
        panelFiltros.add(crearEtiquetaFiltro("Filtrar por Teléfono:"));
        txtFiltroTelefono = new JTextField();
        aplicarPlaceholder(txtFiltroTelefono, "ingresar el telefono");
        panelFiltros.add(txtFiltroTelefono);
        panelFiltros.add(Box.createVerticalStrut(20)); // Espaciador

        // Filtro Correo
        panelFiltros.add(crearEtiquetaFiltro("Filtrar por Correo:"));
        txtFiltroCorreo = new JTextField();
        aplicarPlaceholder(txtFiltroCorreo, "ingrese el correo");
        panelFiltros.add(txtFiltroCorreo);
        panelFiltros.add(Box.createVerticalStrut(30)); // Espaciador más grande antes del botón

        // Botón Ver Información (Buscar)
        JButton btnBuscar = boton("ver informacion", new Color(110, 220, 110)); // Verde claro
        btnBuscar.setForeground(Color.BLACK); // Letra negra como en tu diseño
        btnBuscar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBuscar.addActionListener(e -> cargarDatos());
        panelFiltros.add(btnBuscar);

        panelCentral.add(panelFiltros, BorderLayout.EAST);

        // --- PANEL INFERIOR (Botón Regresar) ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(0, 40, 30, 0));
        JButton btnR = boton("Regresar", new Color(255, 80, 50)); // Rojo anaranjado
        btnR.setPreferredSize(new Dimension(200, 45));
        btnR.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnR.addActionListener(e -> regresar());
        panelSur.add(btnR);

        // --- ENSAMBLAJE FINAL ---
        fondo.add(t, BorderLayout.NORTH);
        fondo.add(panelCentral, BorderLayout.CENTER);
        fondo.add(panelSur, BorderLayout.SOUTH);
        add(fondo, BorderLayout.CENTER);

        // Cargar los datos la primera vez que se abre
        cargarDatos();
    }

    /**
     * Solicita al BO la lista de clientes, aplica los filtros si están escritos,
     * y dibuja los resultados en la tabla.
     */
    private void cargarDatos() {
        try {
            modelo.setRowCount(0); // Limpiamos la tabla
            List<ClienteFrecuenteDTO> listaCompleta = bo.obtenerTodosLosClientes();
            
            String fNombre = obtenerTextoFiltro(txtFiltroNombre, "ingresar el nombre del cliente").toLowerCase();
            String fTel = obtenerTextoFiltro(txtFiltroTelefono, "ingresar el telefono").toLowerCase();
            String fCorreo = obtenerTextoFiltro(txtFiltroCorreo, "ingrese el correo").toLowerCase();

            for (ClienteFrecuenteDTO c : listaCompleta) {
                String nomCliente = c.getNombres() != null ? c.getNombres().toLowerCase() : "";
                String telCliente = c.getTelefono() != null ? c.getTelefono().toLowerCase() : "";
                String correoCliente = c.getCorreo() != null ? c.getCorreo().toLowerCase() : "";

                boolean pasaNombre = fNombre.isEmpty() || nomCliente.contains(fNombre);
                boolean pasaTel = fTel.isEmpty() || telCliente.contains(fTel);
                boolean pasaCorreo = fCorreo.isEmpty() || correoCliente.contains(fCorreo);

                // Si pasa los filtros, lo agregamos a la tabla
                if (pasaNombre && pasaTel && pasaCorreo) {
                    modelo.addRow(new Object[]{
                        c.getNombres(), 
                        c.getApellidoPaterno(), 
                        c.getApellidoMaterno(), 
                        c.getCorreo() != null && !c.getCorreo().isEmpty() ? c.getCorreo() : "N/A", 
                        c.getTelefono(), 
                        c.getPuntos()
                    });
                }
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage());
        }
    }


    private String obtenerTextoFiltro(JTextField campo, String placeholder) {
        String texto = campo.getText().trim();
        if (texto.equals(placeholder)) {
            return ""; // Si es el placeholder, cuenta como vacío
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
        lbl.setBackground(new Color(77, 184, 255)); // Azul claro de tu diseño
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return lbl;
    }

    private void regresar() {
        JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
        v.setContentPane(new PanelMenuClientes());
        v.revalidate();
    }

    private JButton boton(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        return b;
    }

    private JPanel crearFondo() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                java.net.URL url = getClass().getResource("/com/mycompany/presentacion/fondos/fondoInicio.png");
                if (url != null) {
                    Image img = new ImageIcon(url).getImage();
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
                }
            }
        };
        p.setOpaque(false);
        return p;
    }
}