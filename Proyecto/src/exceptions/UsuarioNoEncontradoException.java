package exceptions;

/**
 * Se lanza al buscar un usuario inexistente en el grafo.
 */
public class UsuarioNoEncontradoException extends Exception {
    public UsuarioNoEncontradoException(String username) {
        super("Usuario no encontrado: " + username);
    }
}


