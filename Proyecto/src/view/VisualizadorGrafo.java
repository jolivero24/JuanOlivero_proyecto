package view;

import model.GrafoDirigido;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

// DIA 4: Implementación con GraphStream para visualizar el grafo y colorear CFCs
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;

public class VisualizadorGrafo {
    // DIA 5: Exponer paleta para leyenda
    public static String[] PALETA = new String[]{
            "#e57373", "#64b5f6", "#81c784", "#ffd54f", "#ba68c8",
            "#4db6ac", "#ff8a65", "#a1887f", "#90a4ae", "#f06292"
    };
    /**
     * DIA 4 (GraphStream): Construye un panel Swing con el grafo coloreado por componentes.
     */
    public static JComponent construirPanel(GrafoDirigido grafo, Usuario[][] componentes) {
        System.setProperty("org.graphstream.ui", "swing");
        Graph g = new SingleGraph("red-social");

        // Estilos base
        String css = String.join("",
                "graph { padding: 40px; }",
                "node { size: 22px; fill-color: #90caf9; text-size: 14px; text-alignment: above; stroke-mode: plain; stroke-color: #444; stroke-width: 2px; }",
                "edge { arrow-shape: arrow; arrow-size: 10px,6px; size: 2px; fill-color: #777; }"
        );
        g.setAttribute("ui.stylesheet", css);

        // Nodos
        Usuario[] usuarios = grafo.obtenerUsuarios();
        for (int i = 0; i < usuarios.length; i++) {
            String id = usuarios[i].getNombre();
            Node n = g.addNode(id);
            n.setAttribute("ui.label", id);
        }

        // Aristas dirigidas (IDs únicas por par origen->destino)
        for (int i = 0; i < usuarios.length; i++) {
            String origen = usuarios[i].getNombre();
            Usuario[] vecinos = grafo.obtenerVecinos(origen);
            for (int j = 0; j < vecinos.length; j++) {
                String destino = vecinos[j].getNombre();
                String edgeId = origen + "->" + destino;
                if (g.getEdge(edgeId) == null) {
                    g.addEdge(edgeId, origen, destino, true);
                }
            }
        }

        // Colorear por componente (paleta cíclica)
        if (componentes != null) {
            for (int i = 0; i < componentes.length; i++) {
                String color = PALETA[i % PALETA.length];
                for (int k = 0; k < componentes[i].length; k++) {
                    String id = componentes[i][k].getNombre();
                    Node n = g.getNode(id);
                    if (n != null) {
                        n.setAttribute("ui.style", "fill-color: " + color + ";");
                    }
                }
            }
        }

        SwingViewer viewer = new SwingViewer(g, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        View view = viewer.addDefaultView(false); // false: componente Swing
        ViewPanel viewPanel = (ViewPanel) view;
        JPanel container = new JPanel(new BorderLayout());
        container.add(viewPanel, BorderLayout.CENTER);
        return container;
    }
}


