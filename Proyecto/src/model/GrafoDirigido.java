package model;

/**
 * Grafo dirigido implementado con matriz de adyacencia y arreglos dinámicos propios.
 * No utiliza colecciones de la librería estándar para el TAD del grafo.
 * 
 * TODO: Mejorar la eficiencia del método eliminarUsuario
 * FIXME: Verificar que la matriz de adyacencia se mantenga correcta
 */
public class GrafoDirigido {
    private static int CAPACIDAD_INICIAL = 16;

    private Usuario[] usuarios = new Usuario[CAPACIDAD_INICIAL];
    private int numUsuarios = 0;
    // matriz de adyacencia: adj[i][j] == true si existe arista usuarios[i] -> usuarios[j]
    private boolean[][] adj = new boolean[CAPACIDAD_INICIAL][CAPACIDAD_INICIAL];
    private int siguienteId = 1;

    /** Agrega un nuevo usuario con nombre en formato "@nombre". */
    public Usuario agregarUsuario(String nombre) {
        if (existeUsuario(nombre) == true) {
            throw new IllegalArgumentException("El usuario ya existe: " + nombre);
        }
        asegurarCapacidad(numUsuarios + 1);
        Usuario u = new Usuario(siguienteId, nombre);
        siguienteId = siguienteId + 1;  // Incremento separado
        usuarios[numUsuarios] = u;
        numUsuarios = numUsuarios + 1;  // Incremento separado
        return u;
    }

    /** Elimina un usuario y todas sus relaciones. */
    public void eliminarUsuario(String nombre) {
        int idx = indiceDe(nombre);
        if (idx < 0) {
            return;
        }
        // Desplazar filas y columnas de la matriz para remover idx
        for (int i = idx; i < numUsuarios - 1; i++) {
            // desplazar fila i+1 -> i
            for (int j = 0; j < numUsuarios; j++) {
                adj[i][j] = adj[i + 1][j];
            }
        }
        for (int j = idx; j < numUsuarios - 1; j++) {
            // desplazar columna j+1 -> j
            for (int i = 0; i < numUsuarios; i++) {
                adj[i][j] = adj[i][j + 1];
            }
        }
        // Limpiar última fila/columna
        for (int i = 0; i < numUsuarios; i++) {
            adj[numUsuarios - 1][i] = false;
            adj[i][numUsuarios - 1] = false;
        }
        // Remover usuario del arreglo
        for (int i = idx; i < numUsuarios - 1; i++) {
            usuarios[i] = usuarios[i + 1];
        }
        usuarios[numUsuarios - 1] = null;
        numUsuarios = numUsuarios - 1;  // Decremento separado
    }

    /** Crea una arista dirigida desde origen hacia destino. */
    public void agregarRelacion(String origenNombre, String destinoNombre) {
        int i = indiceDe(origenNombre);
        int j = indiceDe(destinoNombre);
        if (i < 0 || j < 0) {
            throw new IllegalArgumentException("Relación inválida: usuario inexistente");
        }
        if (i == j) {
            return; // evitar lazos
        }
        adj[i][j] = true;
    }

    /** Elimina una arista dirigida específica. */
    public void eliminarRelacion(String origenNombre, String destinoNombre) {
        int i = indiceDe(origenNombre);
        int j = indiceDe(destinoNombre);
        if (i < 0 || j < 0) {
            return;
        }
        adj[i][j] = false;
    }

    /** Verifica si existe la arista origen -> destino. */
    public boolean existeRelacion(String origenNombre, String destinoNombre) {
        int i = indiceDe(origenNombre);
        int j = indiceDe(destinoNombre);
        if (i < 0 || j < 0) {
            return false;
        }
        return adj[i][j];
    }

    /** Devuelve los vecinos (a quiénes sigue) de un usuario por nombre. */
    public Usuario[] obtenerVecinos(String nombre) {
        int i = indiceDe(nombre);
        if (i < 0) {
            return new Usuario[0];
        }
        // contar
        int c = 0;
        for (int j = 0; j < numUsuarios; j++) {
            if (adj[i][j] == true) {
                c = c + 1;
            }
        }
        Usuario[] res = new Usuario[c];
        int k = 0;
        for (int j = 0; j < numUsuarios; j++) {
            if (adj[i][j] == true) {
                res[k] = usuarios[j];
                k = k + 1;  // Incremento separado
            }
        }
        return res;
    }

    /** Lista todos los usuarios registrados (copia del arreglo interno). */
    public Usuario[] obtenerUsuarios() {
        Usuario[] copia = new Usuario[numUsuarios];
        for (int i = 0; i < numUsuarios; i++) {
            copia[i] = usuarios[i];
        }
        return copia;
    }

    /** Busca y retorna el usuario por nombre, o null si no existe. */
    public Usuario buscarUsuario(String nombre) {
        int idx = indiceDe(nombre);
        if (idx >= 0) {
            return usuarios[idx];
        } else {
            return null;
        }
    }

    public boolean existeUsuario(String nombre) {
        return indiceDe(nombre) >= 0;
    }

    public int contarUsuarios() { 
        return numUsuarios; 
    }

    public int contarRelaciones() {
        int total = 0;
        for (int i = 0; i < numUsuarios; i++) {
            for (int j = 0; j < numUsuarios; j++) {
                if (adj[i][j] == true) {
                    total = total + 1;
                }
            }
        }
        return total;
    }

    public boolean estaVacio() { 
        return numUsuarios == 0; 
    }

    /** Cuenta las relaciones entrantes hacia el usuario indicado. */
    public int contarRelacionesEntrantes(String nombre) {
        int j = indiceDe(nombre);
        if (j < 0) {
            return 0;
        }
        int total = 0;
        for (int i = 0; i < numUsuarios; i++) {
            if (adj[i][j] == true) {
                total = total + 1;
            }
        }
        return total;
    }

    /** Cuenta las relaciones salientes desde el usuario indicado. */
    public int contarRelacionesSalientes(String nombre) {
        int i = indiceDe(nombre);
        if (i < 0) {
            return 0;
        }
        int total = 0;
        for (int j = 0; j < numUsuarios; j++) {
            if (adj[i][j] == true) {
                total = total + 1;
            }
        }
        return total;
    }

    /** Densidad = E / (V * (V - 1)) para grafo dirigido sin lazos. */
    public double calcularDensidad() {
        int v = contarUsuarios();
        if (v <= 1) {
            return 0.0;
        }
        int e = contarRelaciones();
        double posibles = (double) v * (v - 1);
        return e / posibles;
    }

    /** Construye y retorna una copia transpuesta del grafo. */
    public GrafoDirigido obtenerGrafoTranspuesto() {
        GrafoDirigido t = new GrafoDirigido();
        for (int i = 0; i < numUsuarios; i++) {
            t.agregarUsuario(usuarios[i].getNombre());
        }
        for (int i = 0; i < numUsuarios; i++) {
            for (int j = 0; j < numUsuarios; j++) {
                if (adj[i][j] == true) {
                    t.agregarRelacion(usuarios[j].getNombre(), usuarios[i].getNombre());
                }
            }
        }
        return t;
    }

    private void asegurarCapacidad(int capacidadMinima) {
        if (usuarios.length >= capacidadMinima) {
            return;
        }
        int nuevaCap = Math.max(capacidadMinima, usuarios.length * 2);
        Usuario[] nuevoArr = new Usuario[nuevaCap];
        for (int i = 0; i < numUsuarios; i++) {
            nuevoArr[i] = usuarios[i];
        }
        usuarios = nuevoArr;
        boolean[][] nuevaAdj = new boolean[nuevaCap][nuevaCap];
        for (int i = 0; i < numUsuarios; i++) {
            for (int j = 0; j < numUsuarios; j++) {
                nuevaAdj[i][j] = adj[i][j];
            }
        }
        adj = nuevaAdj;
    }

    private int indiceDe(String nombre) {
        if (nombre == null) {
            return -1;
        }
        for (int i = 0; i < numUsuarios; i++) {
            if (usuarios[i].getNombre().equals(nombre)) {
                return i;
            }
        }
        return -1;
    }
}


