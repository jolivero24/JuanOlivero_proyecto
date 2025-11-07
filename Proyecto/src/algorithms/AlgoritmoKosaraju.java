package algorithms;

import model.GrafoDirigido;
import model.Usuario;

/**
 * Implementación del algoritmo de Kosaraju sin colecciones estándar.
 * Identifica Componentes Fuertemente Conectados (CFC) con complejidad O(V+E).
 * 
 * Nota: Este algoritmo es complejo, pero funciona bien para el proyecto
 * TODO: Optimizar la parte de expansión de arreglos
 */
public class AlgoritmoKosaraju {

    /**
     * Encuentra todos los componentes fuertemente conectados del grafo.
     * Retorna un arreglo de componentes; cada componente es un arreglo de usuarios.
     */
    public static Usuario[][] encontrarComponentes(GrafoDirigido grafo) {
        Usuario[] usuarios = grafo.obtenerUsuarios();
        int n = usuarios.length;
        if (n == 0) {
            return new Usuario[0][];
        }

        // Fase 1: Orden de finalización en el grafo original
        Usuario[] pilaFinalizacion = AlgoritmoDFS.obtenerOrdenFinalizacion(grafo);

        // Fase 2: Grafo transpuesto
        GrafoDirigido transpuesto = grafo.obtenerGrafoTranspuesto();
        Usuario[] usuariosT = transpuesto.obtenerUsuarios();

        // Fase 3: Procesar en orden inverso de finalización sobre el transpuesto
        boolean[] visitadoT = new boolean[n];
        // Buffer temporal para los índices de cada componente (en el grafo transpuesto)
        int[] buffer = new int[n];

        // Arreglo dinámico manual para los componentes encontrados
        Usuario[][] componentes = new Usuario[Math.max(4, n)][];
        int cantidadComponentes = 0;

        // Recorremos la pila (usuarios) en orden inverso al llenado
        for (int idxPila = n - 1; idxPila >= 0; idxPila--) {
            Usuario u = pilaFinalizacion[idxPila];
            if (u == null) {
                continue; // posiciones no usadas
            }
            int indiceEnT = indiceDeUsuarioPorNombre(usuariosT, u.getNombre());
            if (indiceEnT < 0) {
                continue; // seguridad
            }
            if (visitadoT[indiceEnT] == true) {
                continue;
            }

            // recolectar el componente desde el transpuesto
            int tam = algorithms.AlgoritmoDFS.recolectarComponentePorIndices(transpuesto, indiceEnT, visitadoT, buffer);
            Usuario[] componente = new Usuario[tam];
            for (int k = 0; k < tam; k++) {
                componente[k] = usuariosT[buffer[k]];
            }

            // agregar al arreglo dinámico
            if (cantidadComponentes == componentes.length) {
                componentes = expandirMatriz(componentes, componentes.length * 2);
            }
            componentes[cantidadComponentes] = componente;
            cantidadComponentes = cantidadComponentes + 1;  // Incremento separado
        }

        // Ajustar tamaño exacto
        Usuario[][] resultado = new Usuario[cantidadComponentes][];
        for (int i = 0; i < cantidadComponentes; i++) {
            resultado[i] = componentes[i];
        }
        return resultado;
    }

    // Helpers
    private static int indiceDeUsuarioPorNombre(Usuario[] usuarios, String nombre) {
        if (nombre == null) {
            return -1;
        }
        for (int i = 0; i < usuarios.length; i++) {
            if (usuarios[i].getNombre().equals(nombre)) {
                return i;
            }
        }
        return -1;
    }

    private static Usuario[][] expandirMatriz(Usuario[][] original, int nuevaCapacidad) {
        Usuario[][] nueva = new Usuario[nuevaCapacidad][];
        for (int i = 0; i < original.length; i++) {
            nueva[i] = original[i];
        }
        return nueva;
    }
}