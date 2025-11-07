import controller.ControladorPrincipal;
import view.VentanaPrincipal;
import utils.GestorArchivos;
import model.GrafoDirigido;

public class Main {
    public static void main(String[] args) {
        // DIA 3: Lanzar GUI básica con controlador
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                VentanaPrincipal vista = new VentanaPrincipal();
                ControladorPrincipal ctrl = new ControladorPrincipal(vista);
                // DIA 5: Autocargar ejemplo si existe
                try {
                    java.io.File f = new java.io.File("datos/ejemplo-red.txt");
                    if (f.exists()) {
                        GestorArchivos g = new GestorArchivos();
                        GrafoDirigido gr = g.cargar(f);
                        ctrl.inicializarGrafo(gr, f);
                    }
                } catch (Exception ignore) {}
                vista.setVisible(true);
            }
        });
    }
}


