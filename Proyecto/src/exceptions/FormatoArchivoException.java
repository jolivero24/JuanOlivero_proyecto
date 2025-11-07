package exceptions;

/**
 * Se lanza cuando el archivo de datos no cumple el formato esperado.
 */
public class FormatoArchivoException extends Exception {
    public FormatoArchivoException(String message) {
        super(message);
    }

    public FormatoArchivoException(String message, Throwable cause) {
        super(message, cause);
    }
}


