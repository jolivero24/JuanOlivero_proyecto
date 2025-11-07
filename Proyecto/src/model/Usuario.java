package model;

import java.util.Objects;

/**
 * Representa un usuario de la red social (un nodo del grafo dirigido).
 * Un usuario se identifica por un nombre en formato "@usuario" y
 * un identificador numérico inmutable.
 */
public class Usuario {
    private int id;
    private String nombre;

    /**
     * Crea un nuevo usuario.
     *
     * @param id identificador único e inmutable
     * @param nombre nombre del usuario en formato "@nombre"
     * @throws IllegalArgumentException si el nombre es nulo, vacío o no inicia con '@'
     */
    public Usuario(int id, String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser null");
        }
        String trimmed = nombre.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
        if (trimmed.startsWith("@") == false) {
            throw new IllegalArgumentException("El nombre de usuario debe iniciar con '@'");
        }
        this.id = id;
        this.nombre = trimmed;
    }

    /**
     * @return identificador único del usuario.
     */
    public int getId() {
        return id;
    }

    /**
     * @return nombre del usuario en formato "@nombre".
     */
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Usuario == false) {
            return false;
        }
        Usuario usuario = (Usuario) o;
        // Igualdad por nombre garantiza unicidad semántica en el grafo
        return Objects.equals(nombre, usuario.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return nombre;
    }
}