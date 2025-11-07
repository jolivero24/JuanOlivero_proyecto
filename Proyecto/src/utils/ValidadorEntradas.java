package utils;

/** Utilidades de validación de entradas de usuario y formato de archivo. */
public class ValidadorEntradas {

    /** Valida que el nombre comience con '@' y no sea vacío. */
    public static boolean validarNombreUsuario(String nombre) {
        if (nombre == null) {
            return false;
        }
        String s = nombre.trim();
        if (s.isEmpty()) {
            return false;
        }
        if (s.startsWith("@") == false) {
            return false;
        }
        return true;
    }

    /** Valida línea de relación con formato "@a, @b". */
    public static boolean validarFormatoRelacion(String relacion) {
        if (relacion == null) {
            return false;
        }
        String s = relacion.trim();
        String[] partes = s.split(",");
        if (partes.length != 2) {
            return false;
        }
        String o = partes[0].trim();
        String d = partes[1].trim();
        boolean origenValido = validarNombreUsuario(o);
        boolean destinoValido = validarNombreUsuario(d);
        return origenValido && destinoValido;
    }

    /** Normaliza nombre aplicando trim. */
    public static String normalizarNombre(String nombre) {
        if (nombre == null) {
            return null;
        } else {
            return nombre.trim();
        }
    }

    /** Devuelve un arreglo de elementos duplicados encontrados. */
    public static String[] validarDuplicados(String[] nombres) {
        if (nombres == null) {
            return new String[0];
        }
        // Estrategia O(n^2) sin colecciones: marcar duplicados en arreglo auxiliar
        boolean[] esDuplicado = new boolean[nombres.length];
        int conteo = 0;
        for (int i = 0; i < nombres.length; i++) {
            String si = normalizarNombre(nombres[i]);
            if (si == null) {
                continue;
            }
            for (int j = i + 1; j < nombres.length; j++) {
                String sj = normalizarNombre(nombres[j]);
                if (sj == null) {
                    continue;
                }
                if (esDuplicado[j] == false && si.equals(sj)) {
                    esDuplicado[j] = true;
                    conteo = conteo + 1;
                }
            }
        }
        String[] res = new String[conteo];
        int k = 0;
        for (int i = 0; i < nombres.length; i++) {
            if (esDuplicado[i] == true) {
                res[k] = normalizarNombre(nombres[i]);
                k = k + 1;  // Incremento separado
            }
        }
        return res;
    }
}