package com.mycompany.presentacion.paneles.panelesInicio;

import com.mycompany.presentacion.controlador.Coordinador;
import dtos.EmpleadoDTO;
import excepciones.NegocioException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        }
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        panelFondo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (imagen != null) g2.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
            }
        };

        // Tarjeta central semitransparente
        JPanel tarjeta = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 160));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setPreferredSize(new Dimension(500, 420));
        tarjeta.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Título dentro de la tarjeta
        JLabel labelTitulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        labelTitulo.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 30, 0);
        tarjeta.add(labelTitulo, gbc);

        // Label usuario
        JLabel labelUsuario = new JLabel("Usuario");
        labelUsuario.setForeground(new Color(200, 200, 200));
        labelUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 2, 0);
        tarjeta.add(labelUsuario, gbc);

        // Campo usuario
        textFieldUsuario = new JTextField();
        estilizarCampo(textFieldUsuario);
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        tarjeta.add(textFieldUsuario, gbc);

        // Label contraseña
        JLabel labelContraseña = new JLabel("Contraseña");
        labelContraseña.setForeground(new Color(200, 200, 200));
        labelContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 2, 0);
        tarjeta.add(labelContraseña, gbc);

        // Campo contraseña
        textFieldContraseña = new JPasswordField();
        estilizarCampo(textFieldContraseña);
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 25, 0);
        tarjeta.add(textFieldContraseña, gbc);

        // Botón ingresar
        JButton botonIngresar = new JButton("Ingresar");
        botonIngresar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        botonIngresar.setBackground(new Color(139, 0, 0));
        botonIngresar.setForeground(Color.WHITE);
        botonIngresar.setFocusPainted(false);
        botonIngresar.setBorderPainted(false);
        botonIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonIngresar.setPreferredSize(new Dimension(380, 48));
        botonIngresar.addActionListener(e -> iniciarSesion());
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 12, 0);
        tarjeta.add(botonIngresar, gbc);
        // Agrega la tarjeta al fondo centrada
        panelFondo.add(tarjeta);
        add(panelFondo, BorderLayout.CENTER);

        // Enter en los campos
        ActionListener accionLogin = e -> iniciarSesion();
        textFieldUsuario.addActionListener(accionLogin);
        textFieldContraseña.addActionListener(accionLogin);
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setPreferredSize(new Dimension(380, 42));
        campo.setBackground(new Color(255, 255, 255, 220));
        campo.setForeground(Color.BLACK);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        campo.setOpaque(true);
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
                coordinador.tituloPersonaLogeada("Admin: " + usuario);
                coordinador.setRolActivo(empleadoLogueado.getRol());
                coordinador.mostrarPanelMenuAdmin();
            } else {
                coordinador.tituloPersonaLogeada("Mesero: " + usuario);
                coordinador.setRolActivo(empleadoLogueado.getRol());
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