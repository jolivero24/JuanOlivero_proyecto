package view;

import model.GrafoDirigido;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * DIA 3: Ventana principal básica con menú y lista de usuarios.
 */
public class VentanaPrincipal extends JFrame {
    private DefaultListModel<String> modeloListaUsuarios = new DefaultListModel<>();
    private JList<String> listaUsuarios = new JList<>(modeloListaUsuarios);

    private JMenuItem itemCargar = new JMenuItem("Cargar...");
    private JMenuItem itemGuardar = new JMenuItem("Guardar...");
    private JMenuItem itemSalir = new JMenuItem("Salir");
    // DIA 4: Menú Análisis
    private JMenuItem itemAnalizar = new JMenuItem("Ejecutar Kosaraju");
    private JMenuItem itemLimpiarAnalisis = new JMenuItem("Limpiar Análisis");
    // DIA 5: Menú Editar
    private JMenuItem itemAgregarUsuario = new JMenuItem("Agregar Usuario...");
    private JMenuItem itemEliminarUsuario = new JMenuItem("Eliminar Usuario...");
    // DIA 5: Menú Relaciones
    private JMenuItem itemGestionarRelaciones = new JMenuItem("Gestionar Relaciones...");

    // DIA 4: Panel central para visualización
    private JPanel panelCentral = new JPanel(new BorderLayout());
    // DIA 5: Panel leyenda a la derecha
    private JPanel panelLeyenda = new JPanel();
    // DIA 5: Botón de refrescar análisis
    private JButton btnRefrescarAnalisis = new JButton("🔄 Refrescar Análisis");

    public VentanaPrincipal() {
        super("Análisis de Redes Sociales - CFC");
        // DIA 3: Configuración base de la ventana
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        construirMenu();
        construirLateralUsuarios();
        construirPanelCentral(); // DIA 4
        construirPanelLeyenda(); // DIA 5
        construirBarraHerramientas(); // DIA 5
    }

    // DIA 3: Construye barra de menú superior
    private void construirMenu() {
        JMenuBar barra = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.add(itemCargar);
        menuArchivo.add(itemGuardar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        barra.add(menuArchivo);
        // DIA 5: menú Editar
        JMenu menuEditar = new JMenu("Editar");
        menuEditar.add(itemAgregarUsuario);
        menuEditar.add(itemEliminarUsuario);
        barra.add(menuEditar);
        // DIA 5: menú Relaciones
        JMenu menuRelaciones = new JMenu("Relaciones");
        menuRelaciones.add(itemGestionarRelaciones);
        barra.add(menuRelaciones);
        // DIA 4: menú Análisis
        JMenu menuAnalisis = new JMenu("Análisis");
        menuAnalisis.add(itemAnalizar);
        menuAnalisis.add(itemLimpiarAnalisis);
        barra.add(menuAnalisis);
        setJMenuBar(barra);
    }

    // DIA 3: Panel lateral con lista de usuarios
    private void construirLateralUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(260, 600));
        panel.add(new JLabel("Usuarios"), BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(listaUsuarios);
        panel.add(scroll, BorderLayout.CENTER);
        add(panel, BorderLayout.WEST);
    }

    // DIA 4: Panel central placeholder (será reemplazado por GraphStream)
    private void construirPanelCentral() {
        panelCentral.add(new JLabel("Visualización del grafo (Día 4)"), BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    // DIA 5: Leyenda de componentes (colores y tamaños)
    private void construirPanelLeyenda() {
        panelLeyenda.setLayout(new BoxLayout(panelLeyenda, BoxLayout.Y_AXIS));
        panelLeyenda.setPreferredSize(new Dimension(220, 600));
        panelLeyenda.add(new JLabel("Leyenda de Componentes"));
        add(new JScrollPane(panelLeyenda), BorderLayout.EAST);
    }

    // DIA 5: Barra de herramientas con botón de refrescar
    private void construirBarraHerramientas() {
        JPanel barraHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        barraHerramientas.setBorder(BorderFactory.createEtchedBorder());
        
        btnRefrescarAnalisis.setFont(btnRefrescarAnalisis.getFont().deriveFont(Font.BOLD));
        btnRefrescarAnalisis.setToolTipText("Actualizar análisis de componentes después de modificar el grafo");
        
        barraHerramientas.add(btnRefrescarAnalisis);
        add(barraHerramientas, BorderLayout.NORTH);
    }

    // DIA 3: Refresca la lista de usuarios desde el grafo
    public void refrescarUsuarios(GrafoDirigido grafo) {
        modeloListaUsuarios.clear();
        if (grafo == null) return;
        Usuario[] usuarios = grafo.obtenerUsuarios();
        for (int i = 0; i < usuarios.length; i++) {
            modeloListaUsuarios.addElement(usuarios[i].getNombre());
        }
    }

    // DIA 3: Exponer acciones de menú para el controlador
    public JMenuItem getItemCargar() { return itemCargar; }
    public JMenuItem getItemGuardar() { return itemGuardar; }
    public JMenuItem getItemSalir() { return itemSalir; }
    // DIA 4: Exponer items de análisis
    public JMenuItem getItemAnalizar() { return itemAnalizar; }
    public JMenuItem getItemLimpiarAnalisis() { return itemLimpiarAnalisis; }
    // DIA 5: Exponer botón de refrescar
    public JButton getBtnRefrescarAnalisis() { return btnRefrescarAnalisis; }
    // DIA 5: Exponer items de edición
    public JMenuItem getItemAgregarUsuario() { return itemAgregarUsuario; }
    public JMenuItem getItemEliminarUsuario() { return itemEliminarUsuario; }
    // DIA 5: Exponer items de relaciones
    public JMenuItem getItemGestionarRelaciones() { return itemGestionarRelaciones; }

    // DIA 4: Permitir reemplazar el contenido central
    public void setContenidoCentral(Component comp) {
        panelCentral.removeAll();
        if (comp != null) {
            panelCentral.add(comp, BorderLayout.CENTER);
        }
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    // DIA 5: Actualiza panel de leyenda con componentes y colores
    public void actualizarLeyenda(model.Usuario[][] componentes, String[] coloresUsados) {
        panelLeyenda.removeAll();
        panelLeyenda.add(new JLabel("Leyenda de Componentes"));
        if (componentes != null && coloresUsados != null) {
            for (int i = 0; i < componentes.length; i++) {
                JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JPanel colorBox = new JPanel();
                colorBox.setBackground(Color.decode(coloresUsados[i % coloresUsados.length]));
                colorBox.setPreferredSize(new Dimension(16, 16));
                JLabel lbl = new JLabel("Componente " + (i + 1) + " (" + componentes[i].length + ")");
                fila.add(colorBox);
                fila.add(lbl);
                panelLeyenda.add(fila);
            }
        }
        panelLeyenda.revalidate();
        panelLeyenda.repaint();
    }
}


