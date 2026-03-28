package com.mycompany.presentacion.paneles;

import com.mycompany.presentacion.controlador.Coordinador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class PanelLogin extends JFrame {
    private final Coordinador coordinador;
    private Image imagen;
    private String usuarioAdmin = "Maye";
    private String contraseñaAdmin = "1234";

    private String nombreMesero = "luis";
    private String contraseñaMesero = "4321";

    /*
        metodo auxiliar para facilitar la navegacion entre paneles
        @param toma como parametro el metodo al que quieres cambiar
     */
    public void cambiarPanel(JPanel panel){
        getContentPane().removeAll();
        getContentPane().add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
        panel.requestFocusInWindow();
    }


    public PanelLogin(Coordinador coordinador) {
        this.coordinador = coordinador;
        // Carga desde resources
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagen = new ImageIcon(url).getImage();
        } else {
            System.err.println("Error: No se encontro FondoInicio.png en la raiz de resources");
        }
        mostrar();
    }
    public void mostrar(){
        setTitle("Inicio de sesion");

        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en el escritorio
        setLayout(new BorderLayout(20, 20));

        JPanel panelFondo = new JPanel(new BorderLayout()){

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                // Habilitar alta calidad
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dibujar imagen con interpolación suave
                g2.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
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

        JTextField textFieldUsuario = new JTextField(20);
        JTextField textFieldContraseña = new JTextField(20);

        JLabel labelTitulo = new JLabel("Menu principal");
        labelTitulo.setFont(new Font("Arial",Font.BOLD, 32));
        labelTitulo.setForeground(Color.WHITE);
                                //arriba, izquierda, abajo, derecha en ese orden
        gbc.insets = new Insets(10,10, 90, 10);

        //fila 1
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panelCentro.add(labelUsuario, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panelCentro.add(textFieldUsuario, gbc);

        //fila 2
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panelCentro.add(labelContraseña, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panelCentro.add(textFieldContraseña, gbc);

        panelNorte.add(labelTitulo);
        panelFondo.add(panelNorte, BorderLayout.NORTH);
        panelFondo.add(panelCentro, BorderLayout.CENTER);

        add(panelFondo);



        panelFondo.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "accion");

        panelFondo.getActionMap().put("accion", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textFieldUsuario.getText().isEmpty() || textFieldContraseña.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "No puede dejar ningun campo vacio");
                }

                if(textFieldUsuario.getText().equals(usuarioAdmin) && textFieldContraseña.getText().equals(contraseñaAdmin)){
                    setTitle("Menu admin");
                    cambiarPanel(new PanelMenuAdmin());
                }
                else if(textFieldUsuario.getText().equals(nombreMesero) && textFieldContraseña.getText().equals(contraseñaMesero)){
                    setTitle("Menu mesero");
                    cambiarPanel(new PanelMenuMesero());
                }
                else {
                    JOptionPane.showMessageDialog(null, "El usuario o contraseña son incorrectos");
                }

            }
        });

        setVisible(true);
    }
}