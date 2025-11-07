package tests;

import algorithms.AlgoritmoKosaraju;
import model.GrafoDirigido;
import java.io.File;
import model.Usuario;
import utils.GestorArchivos;

/**
 * DIA 2: Pruebas explícitas del algoritmo de Kosaraju y DFS.
 */
public class PruebaDia2 {
    public static void main(String[] args) throws Exception {
        System.out.println("=== DIA 2 :: PRUEBAS DE KOSARAJU ===");

        // Cargar grafo de ejemplo
        GestorArchivos gestorArchivos = new GestorArchivos();
        GrafoDirigido grafo = gestorArchivos.cargar(new File("datos/ejemplo-red.txt"));
        System.out.println("Usuarios: " + grafo.contarUsuarios());
        System.out.println("Relaciones: " + grafo.contarRelaciones());

        // Ejecutar Kosaraju
        Usuario[][] componentes = AlgoritmoKosaraju.encontrarComponentes(grafo);
        System.out.println("Componentes encontrados: " + componentes.length);

        // Mostrar componentes
        for (int i = 0; i < componentes.length; i++) {
            System.out.print("Componente #" + (i + 1) + " (" + componentes[i].length + ") : ");
            for (int j = 0; j < componentes[i].length; j++) {
                System.out.print(componentes[i][j].getNombre());
                if (j + 1 < componentes[i].length) System.out.print(", ");
            }
            System.out.println();
        }

        // Validaciones esperadas para el archivo ejemplo (3 componentes: 7, 5 y 1)
        int[] tamanos = new int[componentes.length];
        for (int i = 0; i < componentes.length; i++) tamanos[i] = componentes[i].length;
        ordenarAsc(tamanos);

        boolean ok = componentes.length == 3
                && tamanos[0] == 1
                && tamanos[1] == 5
                && tamanos[2] == 7
                && componenteContiene(componentes, "@newageforever");

        System.out.println("Resultado esperado (3 componentes con tamaños 1,5,7): " + (ok ? "OK" : "FALLO"));
    }

    private static void ordenarAsc(int[] arr) {
        // Burbuja simple (no usar colecciones)
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j + 1 < n; j++) {
                if (arr[j] > arr[j + 1]) {
                    int t = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = t;
                }
            }
        }
    }

    private static boolean componenteContiene(Usuario[][] comps, String nombre) {
        for (int i = 0; i < comps.length; i++) {
            for (int j = 0; j < comps[i].length; j++) {
                if (comps[i][j].getNombre().equals(nombre)) return true;
            }
        }
        return false;
    }
}


