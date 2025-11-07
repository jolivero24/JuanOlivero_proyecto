package view;

import model.GrafoDirigido;
import utils.ValidadorEntradas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * DIA 5: Diálogo simplificado para agregar un nuevo usuario (solo nombre).
 * Las relaciones se gestionan por separado en DialogoGestionarRelaciones.
 */
public class DialogoAgregarUsuario extends JDialog {
    private JTextField txtNombre = new JTextField(25);
    private boolean aceptado = false;
    private GrafoDirigido grafo;

    public DialogoAgregarUsuario(Frame owner, GrafoDirigido grafo) {
        super(owner, "Agregar Usuario", true);
        this.grafo = grafo;
        setSize(450, 180);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Panel superior: instrucciones
        JPanel panelInstrucciones = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("  Agregar nuevo usuario a la red social");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 13f));
        panelInstrucciones.add(lblTitulo, BorderLayout.NORTH);
        JLabel lblInfo = new JLabel("  (Usa el menú Relaciones para conectar usuarios después)");
        lblInfo.setFont(lblInfo.getFont().deriveFont(11f));
        lblInfo.setForeground(Color.GRAY);
        panelInstrucciones.add(lblInfo, BorderLayout.SOUTH);
        add(panelInstrucciones, BorderLayout.NORTH);

        // Panel central: campo de nombre
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelCampo = new JPanel(new BorderLayout(5, 5));
        panelCampo.add(new JLabel("Nombre del usuario:"), BorderLayout.WEST);
        panelCampo.add(txtNombre, BorderLayout.CENTER);
        panelCentral.add(panelCampo, BorderLayout.NORTH);
        
        JLabel lblAyuda = new JLabel("  💡 Debe comenzar con @ (ejemplo: @juanito)");
        lblAyuda.setFont(lblAyuda.getFont().deriveFont(11f));
        panelCentral.add(lblAyuda, BorderLayout.SOUTH);
        
        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior: botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAceptar = new JButton("Agregar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnAceptar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onAceptar(); }
        });
        btnCancelar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onCancelar(); }
        });
        
        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Focus en el campo de texto
        txtNombre.requestFocusInWindow();
    }

    private void onAceptar() {
        String nombre = txtNombre.getText().trim();
        
        // Validar que no esté vacío
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese un nombre de usuario.", 
                "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar formato
        if (!ValidadorEntradas.validarNombreUsuario(nombre)) {
            JOptionPane.showMessageDialog(this, 
                "Nombre inválido. Debe comenzar con @ (ejemplo: @juanito)", 
                "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validar que no exista
        if (grafo != null && grafo.existeUsuario(nombre)) {
            JOptionPane.showMessageDialog(this, 
                "El usuario " + nombre + " ya existe en el grafo.", 
                "Usuario Duplicado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        aceptado = true;
        setVisible(false);
    }

    private void onCancelar() {
        aceptado = false;
        setVisible(false);
    }

    public boolean fueAceptado() { return aceptado; }
    
    public String getNombreUsuario() { return txtNombre.getText().trim(); }
}

