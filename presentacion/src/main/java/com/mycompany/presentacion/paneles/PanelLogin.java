package com.mycompany.presentacion.paneles;

import com.mycompany.presentacion.controlador.Coordinador;
import dtos.EmpleadoDTO;
import excepciones.NegocioException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import objetosnegocio.EmpleadoBO;

public class PanelLogin extends JPanel {
    private final Coordinador coordinador;
    private Image imagen;

    public void cambiarPanel(JPanel panel){
            JFrame v = (JFrame) SwingUtilities.getWindowAncestor(this);
            v.setContentPane(panel);
            v.revalidate();
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
        setSize(1080, 720);
        setLayout(new BorderLayout(20, 20));

        JPanel panelFondo = new JPanel(new BorderLayout()){

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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
        
        JPasswordField textFieldContraseña = new JPasswordField(20); 

        JLabel labelTitulo = new JLabel("Menu principal");
        labelTitulo.setFont(new Font("Arial",Font.BOLD, 32));
        labelTitulo.setForeground(Color.WHITE);
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
            public void actionPerformed(ActionEvent e) { // aqui esta lo nuevo q puse
                // para iniciar sesion pero bien
                String usuario = textFieldUsuario.getText();
                String contraseña = new String(textFieldContraseña.getPassword()); 

                try {
                    EmpleadoDTO empleadoLogueado = EmpleadoBO.getInstance().iniciarSesion(usuario, contraseña);

                    if (empleadoLogueado == null) {
                        JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error de Login", JOptionPane.ERROR_MESSAGE);
                        return; 
                    }

                    if ("ADMIN".equals(empleadoLogueado.getRol())) {
                        cambiarPanel(new PanelMenuAdmin());
                    } else {
                        cambiarPanel(new PanelMenuMesero());
                    }

                } catch (NegocioException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error del Sistema", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }
}