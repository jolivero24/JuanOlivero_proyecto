package exceptions;

/**
 * Se lanza al intentar crear una relación inválida entre usuarios.
 */
public class RelacionInvalidaException extends Exception {
    public RelacionInvalidaException(String message) {
        super(message);
    }
}


