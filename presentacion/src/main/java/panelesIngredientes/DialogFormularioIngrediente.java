/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panelesIngredientes;

import dtos.IngredienteDTO;
import entidades.UnidadMedida;
import excepciones.NegocioException;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import objetosnegocio.IngredienteBO;
/**
 * Dialogo de interfaz grafica para el registro y edicion de ingredientes.
 * Permite capturar nombre, stock, unidad de medida y una imagen representativa.
 * @author Gael Galaviz
 */
public class DialogFormularioIngrediente extends JDialog {

    private JTextField txtNombre, txtStock;
    private JComboBox<String> cbUnidad;
    private JLabel lblPreview;
    private Image imagenFondo;
    private String rutaImagenSeleccionada = "/imagenes/ingredientes/default.png"; 
    private IngredienteDTO ingredienteEditando;
    // Constructor que inicializa el dialogo.
    public DialogFormularioIngrediente(Frame parent, IngredienteDTO dto) {
        super(parent, true);
        this.ingredienteEditando = dto;
        setUndecorated(true);
        setSize(1000, 650); 
        setLocationRelativeTo(parent);
        cargarFondo();
        initComponents();
        if (dto != null) {
            llenarDatos();
        }
    }
    /**
     * Carga la imagen de fondo utilizada para el panel
     */ 
    private void cargarFondo() {
        java.net.URL url = getClass().getResource("/FondoInicio.png");
        if (url != null) {
            this.imagenFondo = new ImageIcon(url).getImage();
        }
    }
    /**
     * Inicializa y posiciona todos los componentes
     */
    private void initComponents() {
        JPanel content = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                if (imagenFondo != null) {
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        JLabel titulo = new JLabel(ingredienteEditando == null ? "Registro de Ingrediente" : "Editar Ingrediente", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 30, 1000, 50);
        content.add(titulo);

        txtNombre = crearCampo(content, "Nombre del ingrediente:", 120);
        txtStock = crearCampo(content, "Cantidad en Stock:", 200);

        JLabel lblUnidad = new JLabel("Unidad de Medida:", SwingConstants.CENTER);
        lblUnidad.setOpaque(true);
        lblUnidad.setBackground(new Color(220, 220, 220));
        lblUnidad.setBounds(50, 280, 300, 40);
        content.add(lblUnidad);

        cbUnidad = new JComboBox<>(new String[]{"GRAMO", "MILILITRO", "PIEZA"});
        cbUnidad.setBounds(370, 280, 550, 40);
        content.add(cbUnidad);

        JLabel lblImagenTitulo = new JLabel("Imagen (Opcional):", SwingConstants.CENTER);
        lblImagenTitulo.setOpaque(true);
        lblImagenTitulo.setBackground(new Color(220, 220, 220));
        lblImagenTitulo.setBounds(50, 360, 300, 40);
        content.add(lblImagenTitulo);

        JButton btnSeleccionarImg = new JButton("Seleccionar");
        btnSeleccionarImg.setBounds(370, 360, 200, 40);
        btnSeleccionarImg.addActionListener(e -> seleccionarImagen());
        content.add(btnSeleccionarImg);

        lblPreview = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblPreview.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        lblPreview.setForeground(Color.WHITE);
        lblPreview.setBounds(590, 335, 100, 100);
        content.add(lblPreview);

        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(102, 204, 102));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBounds(250, 530, 200, 50);
        btnGuardar.addActionListener(e -> guardar());

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(new Color(255, 102, 51));
        btnRegresar.setForeground(Color.WHITE);
        btnRegresar.setBounds(500, 530, 200, 50);
        btnRegresar.addActionListener(e -> dispose());

        content.add(btnGuardar);
        content.add(btnRegresar);
        setContentPane(content);
    }
   /** 
    * Metodo auxiliar para crear etiquetas y campos de texto.
    */
    private JTextField crearCampo(JPanel p, String texto, int y) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(220, 220, 220));
        lbl.setBounds(50, y, 300, 40);
        p.add(lbl);

        JTextField txt = new JTextField();
        txt.setBounds(370, y, 550, 40);
        p.add(txt);
        return txt;
    }
    /**
     * Abre un selector de archivos para permitir al usuario
     * elegir una imagen desde su equipo.
     */
    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar imagen del ingrediente");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imagenes ", "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            rutaImagenSeleccionada = selectedFile.getAbsolutePath();

            ImageIcon icon = new ImageIcon(new ImageIcon(rutaImagenSeleccionada)
                    .getImage().getScaledInstance(lblPreview.getWidth(), lblPreview.getHeight(), Image.SCALE_SMOOTH));
            lblPreview.setIcon(icon);
            lblPreview.setText("");
        }
    }
    /** 
     * Valida la informacion del formulario y persiste los cambios.
     */
    private void guardar() {
        try {
            String nombre = txtNombre.getText().trim();
            String stockStr = txtStock.getText().trim();
            String unidadStr = (String) cbUnidad.getSelectedItem();

            if (nombre.isEmpty() || stockStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, llene todos los campos.");
                return;
            }

            Double stock;
            try {
                stock = Double.parseDouble(stockStr);
                if (stock < 0) {
                    JOptionPane.showMessageDialog(this, "El stock no puede ser menor a cero.");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El stock debe ser un numero valido.");
                return;
            }

            UnidadMedida unidad = UnidadMedida.valueOf(unidadStr);

            IngredienteDTO nuevoDTO = new IngredienteDTO();
            nuevoDTO.setNombre(nombre);
            nuevoDTO.setUnidadMedida(unidad);
            nuevoDTO.setStock(stock);
            nuevoDTO.setImagen(rutaImagenSeleccionada);

            if (ingredienteEditando == null) {
                IngredienteBO.getInstance().registrarIngrediente(nuevoDTO);
                JOptionPane.showMessageDialog(this, "Ingrediente guardado con exito.");
            } else {
                nuevoDTO.setId(ingredienteEditando.getId());
            }

            dispose();

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Duplicado", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
        }
    }
    /**
     * Carga los datos de un ingrediente existente en los campos del formulario
     * cuando el dialogo se abre en modo de edicion.
     */
    private void llenarDatos() {
        txtNombre.setText(ingredienteEditando.getNombre());
        txtStock.setText(String.valueOf(ingredienteEditando.getStock()));
        cbUnidad.setSelectedItem(ingredienteEditando.getUnidadMedida().toString());

        if (ingredienteEditando.getImagen() != null) {
            this.rutaImagenSeleccionada = ingredienteEditando.getImagen();
            try {
                ImageIcon icon = new ImageIcon(new ImageIcon(rutaImagenSeleccionada)
                        .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                lblPreview.setIcon(icon);
                lblPreview.setText("");
            } catch (Exception e) {
                lblPreview.setText("Error carga");
            }
        }
    }
}
