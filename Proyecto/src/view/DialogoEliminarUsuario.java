package view;

import model.GrafoDirigido;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * DIA 5: Diálogo para eliminar un usuario mostrando sus conexiones.
 */
public class DialogoEliminarUsuario extends JDialog {
    private JComboBox<String> comboUsuarios = new JComboBox<>();
    private JLabel lblEntrantes = new JLabel("Relaciones entrantes: 0");
    private JLabel lblSalientes = new JLabel("Relaciones salientes: 0");
    private JLabel lblTotal = new JLabel("Total conexiones: 0");
    private JTextArea txtInfo = new JTextArea(8, 30);
    
    private boolean aceptado = false;
    private GrafoDirigido grafo;

    public DialogoEliminarUsuario(Frame owner, GrafoDirigido grafo) {
        super(owner, "Eliminar Usuario", true);
        this.grafo = grafo;
        setSize(450, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Panel superior: selección
        JPanel panelSuperior = new JPanel(new GridLayout(4, 1, 5, 5));
        JPanel panelCombo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCombo.add(new JLabel("Usuario a eliminar:"));
        panelCombo.add(comboUsuarios);
        panelSuperior.add(panelCombo);
        panelSuperior.add(lblEntrantes);
        panelSuperior.add(lblSalientes);
        panelSuperior.add(lblTotal);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central: información detallada
        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.add(new JLabel("Detalles de conexiones:"), BorderLayout.NORTH);
        txtInfo.setEditable(false);
        txtInfo.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        panelInfo.add(new JScrollPane(txtInfo), BorderLayout.CENTER);
        add(panelInfo, BorderLayout.CENTER);

        // Panel inferior: botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnCancelar = new JButton("Cancelar");
        
        btnEliminar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onEliminar(); }
        });
        btnCancelar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onCancelar(); }
        });
        
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        cargarUsuarios();
        
        comboUsuarios.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { actualizarInfo(); }
        });
        
        if (comboUsuarios.getItemCount() > 0) {
            actualizarInfo();
        }
    }

    private void cargarUsuarios() {
        if (grafo == null) return;
        Usuario[] usuarios = grafo.obtenerUsuarios();
        for (int i = 0; i < usuarios.length; i++) {
            comboUsuarios.addItem(usuarios[i].getNombre());
        }
    }

    private void actualizarInfo() {
        String seleccionado = (String) comboUsuarios.getSelectedItem();
        if (seleccionado == null || grafo == null) return;
        
        int entrantes = grafo.contarRelacionesEntrantes(seleccionado);
        int salientes = grafo.contarRelacionesSalientes(seleccionado);
        int total = entrantes + salientes;
        
        lblEntrantes.setText("Relaciones entrantes: " + entrantes);
        lblSalientes.setText("Relaciones salientes: " + salientes);
        lblTotal.setText("Total conexiones: " + total);
        
        // Detalles
        StringBuilder sb = new StringBuilder();
        sb.append("Sigue a:\n");
        Usuario[] vecinos = grafo.obtenerVecinos(seleccionado);
        if (vecinos.length == 0) {
            sb.append("  (ninguno)\n");
        } else {
            for (int i = 0; i < vecinos.length; i++) {
                sb.append("  - ").append(vecinos[i].getNombre()).append('\n');
            }
        }
        sb.append("\nEs seguido por:\n");
        Usuario[] todos = grafo.obtenerUsuarios();
        int countSeguidores = 0;
        for (int i = 0; i < todos.length; i++) {
            if (grafo.existeRelacion(todos[i].getNombre(), seleccionado)) {
                sb.append("  - ").append(todos[i].getNombre()).append('\n');
                countSeguidores++;
            }
        }
        if (countSeguidores == 0) {
            sb.append("  (ninguno)\n");
        }
        
        if (total > 10) {
            sb.append("\n⚠ Este usuario tiene muchas conexiones.");
        }
        
        txtInfo.setText(sb.toString());
        txtInfo.setCaretPosition(0);
    }

    private void onEliminar() {
        String seleccionado = (String) comboUsuarios.getSelectedItem();
        if (seleccionado == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar " + seleccionado + " y todas sus relaciones?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            aceptado = true;
            setVisible(false);
        }
    }

    private void onCancelar() {
        aceptado = false;
        setVisible(false);
    }

    public boolean fueAceptado() { return aceptado; }
    
    public String getUsuarioSeleccionado() {
        return (String) comboUsuarios.getSelectedItem();
    }
}

