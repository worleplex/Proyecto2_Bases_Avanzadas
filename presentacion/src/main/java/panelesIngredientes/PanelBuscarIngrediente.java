/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panelesIngredientes;

import dtos.IngredienteDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import objetosnegocio.IngredienteBO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import panelesProductos.PanelFormularioProducto;

/**
 *
 * @author julian izaguirre
 */
public class PanelBuscarIngrediente extends JDialog {

    private JTextField txtBuscar;
    private Image imagen;
    private final PanelFormularioProducto formulario;

    public PanelBuscarIngrediente(JFrame padre, PanelFormularioProducto formulario) {
        super(padre, "Buscar Ingrediente", true);
        this.formulario = formulario;
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        }
        setSize(600, 400);
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

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Ingrese el nombre del ingrediente:", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panelCentro.add(lblTitulo, gbc);

        txtBuscar = new JTextField();
        txtBuscar.setPreferredSize(new Dimension(350, 50));
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        aplicarPlaceholder(txtBuscar, "nombre del ingrediente");
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panelCentro.add(txtBuscar, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelBotones.setOpaque(false);

        JButton btnContinuar = crearBoton("Continuar", new Color(100, 200, 100));
        JButton btnRegresar = crearBoton("Regresar", new Color(255, 80, 50));
        panelBotones.add(btnContinuar);
        panelBotones.add(btnRegresar);

        gbc.gridy = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        panelCentro.add(panelBotones, gbc);

        btnRegresar.addActionListener(e -> dispose());

        btnContinuar.addActionListener(e -> {
            String busqueda = txtBuscar.getText().trim();
            if (busqueda.isEmpty() || busqueda.equals("nombre del ingrediente")) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un nombre.");
                return;
            }
            try {
                List<IngredienteDTO> resultados = IngredienteBO.getInstance().buscarIngredientes(busqueda);
                if (resultados == null || resultados.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "No se encontró ningún ingrediente con ese nombre.",
                        "Sin resultados",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                JFrame padre = (JFrame) SwingUtilities.getWindowAncestor(this);
                DialogSeleccionarIngrediente dialogo = new DialogSeleccionarIngrediente(padre, resultados);
                dialogo.setVisible(true);

                // FIX: usa referencia directa al formulario
                if (dialogo.getIngredienteSeleccionado() != null) {
                    formulario.agregarIngredienteDesdeDialogo(
                        dialogo.getIngredienteSeleccionado(),
                        dialogo.getCantidadSeleccionada()
                    );
                    dispose();
                }

            } catch (NegocioException | PersistenciaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Enter en el campo también busca
        txtBuscar.addActionListener(e -> btnContinuar.doClick());

        panelFondo.add(panelCentro, BorderLayout.CENTER);
        setContentPane(panelFondo);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void aplicarPlaceholder(JTextField campo, String placeholder) {
        campo.setText(placeholder);
        campo.setForeground(Color.GRAY);
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
}
