package com.mycompany.presentacion.paneles.PanelesComandas;

import com.mycompany.presentacion.controlador.Coordinador;
import com.mycompany.presentacion.controlador.CoordinadorNegocio;
import dtos.EmpleadoDTO;
import net.sf.jasperreports.engine.export.Grid;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelElegirMesa extends JPanel {
    private final Coordinador coordinador;
    private CoordinadorNegocio coordinadorNegocio = new CoordinadorNegocio();
    private Image imagen;

    public PanelElegirMesa(Coordinador coordinador){
        this.coordinador = coordinador;
        crearFondo();
        mostrar();
    }

    public void mostrar(){
        coordinador.cambiarTitulo("Menu de seleccion de mesas");
        setOpaque(false);
        setLayout(new BorderLayout());

        DefaultTableModel modelo = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 5 columnas
        modelo.addColumn("");
        modelo.addColumn("");
        modelo.addColumn("");
        modelo.addColumn("");
        modelo.addColumn("");

        // 4 filas
        modelo.addRow(new Object[]{"Mesa 1",  "Mesa 2",  "Mesa 3",  "Mesa 4",  "Mesa 5"});
        modelo.addRow(new Object[]{"Mesa 6",  "Mesa 7",  "Mesa 8",  "Mesa 9",  "Mesa 10"});
        modelo.addRow(new Object[]{"Mesa 11", "Mesa 12", "Mesa 13", "Mesa 14", "Mesa 15"});
        modelo.addRow(new Object[]{"Mesa 16", "Mesa 17", "Mesa 18", "Mesa 19", "Mesa 20"});

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(140); //Esto se encarga del tamaño de cada fila
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(800, 500));

        tabla.setBackground(new Color(255, 204, 204));
        tabla.setSelectionBackground(new Color(255, 153, 153));

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(255, 204, 204));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JPanel panelNorte = new JPanel();
        panelNorte.setOpaque(false);

        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);

        JPanel panelSur = new JPanel();
        panelSur.setOpaque(false);
        panelSur.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 30, 20);

        JLabel labelTitulo = new JLabel("Seleccion de mesa");
        labelTitulo.setFont(new Font("arial", Font.BOLD, 45));
        labelTitulo.setForeground(Color.WHITE);

        JButton buttonRegresar = new JButton("Regresar");
        JButton buttonModificar = new JButton("Modificar");
        JButton buttonBuscar = new JButton("Buscar");

        panelNorte.add(labelTitulo);
        panelCentro.add(scrollPane);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelSur.add(buttonRegresar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panelSur.add(buttonModificar, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        panelSur.add(buttonBuscar, gbc);
        add(panelNorte, BorderLayout.NORTH);
        add(panelCentro, BorderLayout.CENTER);
        add(panelSur, BorderLayout.SOUTH);

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.rowAtPoint(e.getPoint());
                int columna = tabla.columnAtPoint(e.getPoint());

                String[] numMesa = tabla.getValueAt(fila, columna).toString().split(" ");
                long nMesa = Long.parseLong(numMesa[1]);
                coordinadorNegocio.setMesaActual(numMesa[1]);
                coordinador.mostrarPanelCrearComanda();
            }
        });

        buttonRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(coordinador.getRolActivo().toLowerCase().equals("admin")){
                    coordinador.cambiarTitulo("Bienvenido al sistema Administrador");
                    coordinador.mostrarPanelMenuAdmin();
                }
                else{
                    coordinador.cambiarTitulo("Bienvenido al sistema Mesero");
                    coordinador.mostrarPanelMenuMesero();
                }
            }
        });
    }

    private void crearFondo() {
        // Intentamos cargar la imagen desde la raiz de resources
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null && imagen.getWidth(null) > 0) {
            Graphics2D g2 = (Graphics2D) g;
            // Calidad de renderizado
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
            }
        else {
            g.setColor(new Color(30, 30, 30));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
