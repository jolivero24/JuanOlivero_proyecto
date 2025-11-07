package algorithms;

import model.GrafoDirigido;
import model.Usuario;

/**
 * Utilidades de DFS (Búsqueda en Profundidad) sin usar colecciones estándar.
 * Provee recorridos para orden de finalización y para marcar componentes.
 * 
 * TODO: Aprender sobre arreglos dinámicos más eficientes
 * FIXME: Verificar si esto funciona correctamente con grafos grandes
 */
public class AlgoritmoDFS {

    /**
     * Obtiene el orden de finalización (tipo pila) de todos los nodos del grafo.
     * Retorna un arreglo con los usuarios en el orden en que terminaron (tope al final).
     */
    public static Usuario[] obtenerOrdenFinalizacion(GrafoDirigido grafo) {
        Usuario[] usuarios = grafo.obtenerUsuarios();
        int n = usuarios.length;
        boolean[] visitado = new boolean[n];
        // usaremos un arreglo como pila de finalización
        Usuario[] pila = new Usuario[n];
        int tope = 0;  // Variable simple, no arreglo
        
        for (int i = 0; i < n; i++) {
            if (visitado[i] == false) {
                tope = dfsFinalizacionDesdeIndice(grafo, i, visitado, pila, tope);
            }
        }
        return pila;
    }

    // DFS que apila por finalización usando sólo arreglos
    private static int dfsFinalizacionDesdeIndice(GrafoDirigido grafo,
                                                   int indice,
                                                   boolean[] visitado,
                                                   Usuario[] pila,
                                                   int tope) {
        if (visitado[indice] == true) {
            return tope;  // Retornar valor, no usar referencia
        }
        visitado[indice] = true;
        Usuario[] usuarios = grafo.obtenerUsuarios();
        int n = usuarios.length;
        // Explorar vecinos salientes chequeando todas las posibles aristas
        for (int j = 0; j < n; j++) {
            if (grafo.existeRelacion(usuarios[indice].getNombre(), usuarios[j].getNombre())) {
                if (visitado[j] == false) {
                    tope = dfsFinalizacionDesdeIndice(grafo, j, visitado, pila, tope);
                }
            }
        }
        // al terminar, apilar
        pila[tope] = usuarios[indice];
        tope = tope + 1;  // Incremento separado
        return tope;  // Retornar nuevo valor
    }

    /**
     * Ejecuta DFS desde un índice y llena el buffer con los índices visitados.
     * Devuelve la cantidad de nodos del componente visitado.
     */
    public static int recolectarComponentePorIndices(GrafoDirigido grafo,
                                                     int indiceInicio,
                                                     boolean[] visitado,
                                                     int[] bufferIndices) {
        int contador = 0;  // Variable simple
        contador = dfsRecolectar(grafo, indiceInicio, visitado, bufferIndices, contador);
        return contador;
    }

    private static int dfsRecolectar(GrafoDirigido grafo,
                                      int indice,
                                      boolean[] visitado,
                                      int[] buffer,
                                      int contador) {
        if (visitado[indice] == true) {
            return contador;
        }
        visitado[indice] = true;
        buffer[contador] = indice;
        contador = contador + 1;  // Incremento separado
        
        Usuario[] usuarios = grafo.obtenerUsuarios();
        int n = usuarios.length;
        for (int j = 0; j < n; j++) {
            if (grafo.existeRelacion(usuarios[indice].getNombre(), usuarios[j].getNombre())) {
                if (visitado[j] == false) {
                    contador = dfsRecolectar(grafo, j, visitado, buffer, contador);
                }
            }
        }
        return contador;
    }
}