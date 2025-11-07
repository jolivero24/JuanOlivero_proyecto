package view;

import model.GrafoDirigido;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * DIA 5: Diálogo para gestionar relaciones (agregar/eliminar) entre usuarios existentes.
 */
public class DialogoGestionarRelaciones extends JDialog {
    private GrafoDirigido grafo;
    private JComboBox<String> comboOrigen = new JComboBox<>();
    private JComboBox<String> comboDestino = new JComboBox<>();
    private DefaultListModel<String> modeloRelaciones = new DefaultListModel<>();
    private JList<String> listaRelaciones = new JList<>(modeloRelaciones);
    private JLabel lblEstadisticas = new JLabel("Total relaciones: 0");
    
    private boolean datosModificados = false;

    public DialogoGestionarRelaciones(Frame owner, GrafoDirigido grafo) {
        super(owner, "Gestionar Relaciones", true);
        this.grafo = grafo;
        setSize(700, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Panel superior: agregar relación
        JPanel panelAgregar = new JPanel(new BorderLayout(10, 10));
        panelAgregar.setBorder(BorderFactory.createTitledBorder("Agregar Nueva Relación"));
        
        JPanel panelCombos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCombos.add(new JLabel("Usuario origen:"));
        panelCombos.add(comboOrigen);
        panelCombos.add(new JLabel(" → sigue a → "));
        panelCombos.add(comboDestino);
        JButton btnAgregar = new JButton("Agregar →");
        btnAgregar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { agregarRelacion(); }
        });
        panelCombos.add(btnAgregar);
        panelAgregar.add(panelCombos, BorderLayout.CENTER);
        
        JLabel lblInfo = new JLabel("  💡 Selecciona quién sigue a quién y presiona 'Agregar'");
        lblInfo.setFont(lblInfo.getFont().deriveFont(11f));
        panelAgregar.add(lblInfo, BorderLayout.SOUTH);
        
        add(panelAgregar, BorderLayout.NORTH);

        // Panel central: lista de relaciones existentes
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.setBorder(BorderFactory.createTitledBorder("Relaciones Existentes"));
        
        listaRelaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaRelaciones.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollRelaciones = new JScrollPane(listaRelaciones);
        panelCentral.add(scrollRelaciones, BorderLayout.CENTER);
        
        JPanel panelBotonesLista = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnEliminar = new JButton("Eliminar Seleccionada");
        btnEliminar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { eliminarRelacion(); }
        });
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { cargarRelaciones(); }
        });
        panelBotonesLista.add(btnEliminar);
        panelBotonesLista.add(btnRefrescar);
        panelCentral.add(panelBotonesLista, BorderLayout.SOUTH);
        
        add(panelCentral, BorderLayout.CENTER);

        // Panel inferior: estadísticas y botón cerrar
        JPanel panelInferior = new JPanel(new BorderLayout());
        lblEstadisticas.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panelInferior.add(lblEstadisticas, BorderLayout.WEST);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { setVisible(false); }
        });
        JPanel panelBotonCerrar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonCerrar.add(btnCerrar);
        panelInferior.add(panelBotonCerrar, BorderLayout.EAST);
        
        add(panelInferior, BorderLayout.SOUTH);

        // Cargar datos iniciales
        cargarUsuarios();
        cargarRelaciones();
    }

    private void cargarUsuarios() {
        comboOrigen.removeAllItems();
        comboDestino.removeAllItems();
        
        if (grafo == null) return;
        Usuario[] usuarios = grafo.obtenerUsuarios();
        for (int i = 0; i < usuarios.length; i++) {
            String nombre = usuarios[i].getNombre();
            comboOrigen.addItem(nombre);
            comboDestino.addItem(nombre);
        }
    }

    private void cargarRelaciones() {
        modeloRelaciones.clear();
        
        if (grafo == null) return;
        Usuario[] usuarios = grafo.obtenerUsuarios();
        int total = 0;
        
        for (int i = 0; i < usuarios.length; i++) {
            String origen = usuarios[i].getNombre();
            Usuario[] vecinos = grafo.obtenerVecinos(origen);
            for (int j = 0; j < vecinos.length; j++) {
                String destino = vecinos[j].getNombre();
                modeloRelaciones.addElement(String.format("%-20s → %-20s", origen, destino));
                total++;
            }
        }
        
        lblEstadisticas.setText("Total relaciones: " + total + 
                                " | Usuarios: " + usuarios.length);
    }

    private void agregarRelacion() {
        String origen = (String) comboOrigen.getSelectedItem();
        String destino = (String) comboDestino.getSelectedItem();
        
        if (origen == null || destino == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona origen y destino.",
                    "Campos Incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (origen.equals(destino)) {
            JOptionPane.showMessageDialog(this,
                    "Un usuario no puede seguirse a sí mismo.",
                    "Relación Inválida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (grafo.existeRelacion(origen, destino)) {
            JOptionPane.showMessageDialog(this,
                    "La relación " + origen + " → " + destino + " ya existe.",
                    "Relación Duplicada",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            grafo.agregarRelacion(origen, destino);
            cargarRelaciones();
            datosModificados = true;
            // Mensaje silencioso, solo actualiza la lista
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar relación: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRelacion() {
        String seleccion = listaRelaciones.getSelectedValue();
        if (seleccion == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una relación de la lista.",
                    "Ninguna Selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Parsear "origen → destino"
        String[] partes = seleccion.split("→");
        if (partes.length != 2) return;
        
        String origen = partes[0].trim();
        String destino = partes[1].trim();
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la relación " + origen + " → " + destino + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;
        
        try {
            grafo.eliminarRelacion(origen, destino);
            cargarRelaciones();
            datosModificados = true;
            // Mensaje silencioso, solo actualiza la lista
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar relación: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean fueronModificados() {
        return datosModificados;
    }
}

