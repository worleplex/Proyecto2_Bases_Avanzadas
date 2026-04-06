package com.mycompany.presentacion.paneles;

import com.mycompany.presentacion.controlador.Coordinador;
import dtos.EmpleadoDTO;
import excepciones.NegocioException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import objetosnegocio.EmpleadoBO;

public class PanelLogin extends JPanel {
    private final Coordinador coordinador;
    private Image imagen;
    
    private JTextField textFieldUsuario;
    private JPasswordField textFieldContraseña;
    private JPanel panelFondo;

    public PanelLogin(Coordinador coordinador) {
        this.coordinador = coordinador;
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        } else {
            System.err.println("Error: No se encontro FondoInicio.png en la raiz de resources");
        }
        inicializarComponentes(); 
    }

    private void inicializarComponentes(){
        setSize(1080, 720);
        setLayout(new BorderLayout(20, 20));

        panelFondo = new JPanel(new BorderLayout()){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (imagen != null) {
                    g2.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
                }
            }
        };

        JPanel panelNorte = new JPanel();
        panelNorte.setOpaque(false);

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel labelUsuario = new JLabel("Ingrese su nombre: ");
        labelUsuario.setForeground(Color.WHITE);
        labelUsuario.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel labelContraseña = new JLabel("Ingrese su contraseña: ");
        labelContraseña.setFont(new Font("Arial", Font.BOLD, 24));
        labelContraseña.setForeground(Color.WHITE);

        textFieldUsuario = new JTextField(20);
        textFieldContraseña = new JPasswordField(20); 

        JLabel labelTitulo = new JLabel("Menu principal");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        labelTitulo.setForeground(Color.WHITE);
        gbc.insets = new Insets(10, 10, 90, 10);

        // Fila 1
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panelCentro.add(labelUsuario, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panelCentro.add(textFieldUsuario, gbc);

        // Fila 2
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panelCentro.add(labelContraseña, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panelCentro.add(textFieldContraseña, gbc);

        // Panel sur con botón Regresar
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setOpaque(false);

        JButton botonRegresar = new JButton("Regresar");
        botonRegresar.setFont(new Font("Arial", Font.BOLD, 16));
        botonRegresar.setBackground(new Color(100, 100, 100));
        botonRegresar.setForeground(Color.WHITE);
        botonRegresar.setFocusPainted(false);
        botonRegresar.setBorderPainted(false);
        botonRegresar.setPreferredSize(new Dimension(160, 40));
        botonRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonRegresar.addActionListener(e -> {
            limpiarCampos();
            coordinador.mostrarPanelElegir();
        });

        panelSur.add(botonRegresar);

        panelNorte.add(labelTitulo);
        panelFondo.add(panelNorte, BorderLayout.NORTH);
        panelFondo.add(panelCentro, BorderLayout.CENTER);
        panelFondo.add(panelSur, BorderLayout.SOUTH);

        add(panelFondo);
        ActionListener accionLogin = e -> iniciarSesion();
        textFieldUsuario.addActionListener(accionLogin);
        textFieldContraseña.addActionListener(accionLogin);
    }

    private void iniciarSesion() {
        String usuario = textFieldUsuario.getText().trim();
        String contraseña = new String(textFieldContraseña.getPassword()); 

        if (usuario.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese usuario y contraseña", 
                "Campos vacíos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            EmpleadoDTO empleadoLogueado = EmpleadoBO.getInstance().iniciarSesion(usuario, contraseña);

            if (empleadoLogueado == null) {
                JOptionPane.showMessageDialog(this, 
                    "Usuario o contraseña incorrectos", 
                    "Error de Login", 
                    JOptionPane.ERROR_MESSAGE);
                return; 
            }

            limpiarCampos();

            if ("ADMIN".equals(empleadoLogueado.getRol())) {
                coordinador.cambiarTituloFrame("Admin: " + usuario);
                coordinador.mostrarPanelMenuAdmin();
            } else {
                coordinador.cambiarTituloFrame("Mesero: " + usuario);
                coordinador.mostrarPanelMenuMesero();
            }

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, 
                ex.getMessage(), 
                "Error del Sistema", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limpiarCampos() {
        if (textFieldUsuario != null) textFieldUsuario.setText("");
        if (textFieldContraseña != null) textFieldContraseña.setText("");
    }

    public void mostrar() {
        limpiarCampos();
        setVisible(true);
        textFieldUsuario.requestFocusInWindow();
    }
}