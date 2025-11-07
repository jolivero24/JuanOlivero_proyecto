package exceptions;

/**
 * Se lanza al intentar realizar operaciones que requieren datos cuando el grafo está vacío.
 */
public class GrafoVacioException extends Exception {
    public GrafoVacioException(String operation) {
        super("Operación no válida en grafo vacío: " + operation);
    }
}


