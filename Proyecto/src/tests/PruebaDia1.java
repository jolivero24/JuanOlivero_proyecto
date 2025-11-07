package tests;

import model.GrafoDirigido;
import model.Usuario;
import utils.GestorArchivos;

import java.io.File;

/**
 * Pruebas de verificación para el Día 1.
 * Nota: Esta clase usa salida por consola SOLO para pruebas de desarrollo.
 * Eliminar antes de la entrega final para cumplir la restricción de no I/O de consola.
 */
public class PruebaDia1 {
    public static void main(String[] args) throws Exception {
        titulo("Día 1 - Verificación integral (carga, modificación, guardado)");
        probarCargaYConteos();
        probarRelacionesBasicas();
        probarModificaciones();
        probarGuardadoYRecarga();
        probarTranspuesto();
        probarValidacionesDeFormato();
        ok("Todas las pruebas de Día 1 pasaron");
    }

    private static void probarCargaYConteos() throws Exception {
        GestorArchivos gestor = new GestorArchivos();
        GrafoDirigido grafo = gestor.cargar(new File("datos/ejemplo-red.txt"));

        int usuarios = grafo.contarUsuarios();
        int relaciones = grafo.contarRelaciones();

        seccion("Carga y conteos");
        System.out.println("Usuarios cargados: " + usuarios);
        System.out.println("Relaciones cargadas: " + relaciones);
        double densidad = grafo.calcularDensidad();
        System.out.println("Densidad (E / (V*(V-1))): " + relaciones + " / (" + usuarios + "*(" + usuarios + "-1)) = " + densidad);
        imprimirMuestraUsuarios(grafo, 5);

        if (usuarios != 13) {
            throw new AssertionError("Se esperaban 13 usuarios, obtenido: " + usuarios);
        }
        if (relaciones != 18) {
            throw new AssertionError("Se esperaban 18 relaciones, obtenido: " + relaciones);
        }
        if (grafo.estaVacio()) {
            throw new AssertionError("El grafo no debería estar vacío");
        }
        if (densidad <= 0 || densidad >= 1) {
            throw new AssertionError("Densidad fuera de rango: " + densidad);
        }
        ok("Carga y conteos básicos correctos");
    }

    private static void probarRelacionesBasicas() throws Exception {
        GestorArchivos gestor = new GestorArchivos();
        GrafoDirigido grafo = gestor.cargar(new File("datos/ejemplo-red.txt"));

        seccion("Relaciones básicas y vecinos");
        // @mazinger -> @juanc, @tuqui33
        Usuario[] vecinosMaz = grafo.obtenerVecinos("@mazinger");
        int count = contarPorNombre(vecinosMaz, new String[]{"@juanc", "@tuqui33"});
        if (count != 2) {
            throw new AssertionError("@mazinger debería seguir a @juanc y @tuqui33");
        }
        if (!grafo.existeRelacion("@xoxojaime", "@pepe")) {
            throw new AssertionError("Relación esperada @xoxojaime -> @pepe no encontrada");
        }
        int inMaz = grafo.contarRelacionesEntrantes("@mazinger");
        if (inMaz < 1) {
            throw new AssertionError("@mazinger debería tener al menos un seguidor");
        }
        imprimirRelacionesDe(grafo, "@mazinger");
        ok("Relaciones básicas correctas");
    }

    private static void probarModificaciones() throws Exception {
        seccion("Modificaciones (agregar/eliminar usuario y relaciones)");
        GestorArchivos gestor = new GestorArchivos();
        GrafoDirigido grafo = gestor.cargar(new File("datos/ejemplo-red.txt"));

        int u0 = grafo.contarUsuarios();
        int e0 = grafo.contarRelaciones();
        System.out.println("Antes: usuarios=" + u0 + ", relaciones=" + e0);

        grafo.agregarUsuario("@nuevo");
        grafo.agregarRelacion("@nuevo", "@pepe");
        grafo.agregarRelacion("@mazinger", "@nuevo");

        int u1 = grafo.contarUsuarios();
        int e1 = grafo.contarRelaciones();
        System.out.println("Después de agregar: usuarios=" + u1 + ", relaciones=" + e1);

        if (u1 != u0 + 1) throw new AssertionError("Usuario no agregado correctamente");
        if (e1 != e0 + 2) throw new AssertionError("Relaciones no agregadas correctamente");

        // eliminar y verificar reversión parcial
        grafo.eliminarUsuario("@nuevo");
        int u2 = grafo.contarUsuarios();
        int e2 = grafo.contarRelaciones();
        System.out.println("Tras eliminar @nuevo: usuarios=" + u2 + ", relaciones=" + e2);
        if (u2 != u0) throw new AssertionError("Usuario no eliminado correctamente");
        if (e2 != e0) throw new AssertionError("Relaciones no ajustadas tras eliminar usuario");

        ok("Modificaciones sobre el grafo correctas");
    }

    private static void probarGuardadoYRecarga() throws Exception {
        seccion("Guardado y recarga (persistencia)");
        GestorArchivos gestor = new GestorArchivos();
        GrafoDirigido grafo = gestor.cargar(new File("datos/ejemplo-red.txt"));
        // modificar algo y guardar
        grafo.agregarUsuario("@persistente");
        grafo.agregarRelacion("@persistente", "@pepe");

        File destino = new File("out/pruebas/guardado-red.txt");
        if (!destino.getParentFile().exists()) destino.getParentFile().mkdirs();
        gestor.guardar(destino, grafo);

        GrafoDirigido recargado = gestor.cargar(destino);
        System.out.println("Guardado en: " + destino.getPath());
        System.out.println("Usuarios recargados: " + recargado.contarUsuarios());
        System.out.println("Relaciones recargadas: " + recargado.contarRelaciones());
        if (!recargado.existeUsuario("@persistente") || !recargado.existeRelacion("@persistente", "@pepe")) {
            throw new AssertionError("Persistencia fallida: faltan datos guardados");
        }
        ok("Guardado y recarga verificados");
    }

    private static void probarTranspuesto() throws Exception {
        GestorArchivos gestor = new GestorArchivos();
        GrafoDirigido grafo = gestor.cargar(new File("datos/ejemplo-red.txt"));
        GrafoDirigido t = grafo.obtenerGrafoTranspuesto();
        if (grafo.contarUsuarios() != t.contarUsuarios()) {
            throw new AssertionError("El transpuesto debe tener igual número de usuarios");
        }
        if (grafo.contarRelaciones() != t.contarRelaciones()) {
            throw new AssertionError("El transpuesto debe tener igual número de relaciones");
        }
        // En original: @mazinger -> @juanc; en transpuesto: @juanc -> @mazinger
        if (!t.existeRelacion("@juanc", "@mazinger")) {
            throw new AssertionError("Transpuesto incorrecto: falta @juanc -> @mazinger");
        }
        seccion("Transpuesto - muestra de verificación");
        imprimirRelacionesDe(t, "@juanc");
        ok("Grafo transpuesto correcto");
    }

    private static void probarValidacionesDeFormato() throws Exception {
        seccion("Validaciones de formato y errores esperados");
        GestorArchivos gestor = new GestorArchivos();
        // Archivo correcto
        boolean okFormato = gestor.validarFormato(new File("datos/ejemplo-red.txt"));
        System.out.println("validarFormato(archivo válido) = " + okFormato);
        if (!okFormato) throw new AssertionError("El archivo de ejemplo debería ser válido");

        // Archivo mal formado (sin secciones)
        File mal = new File("out/pruebas/mal-formato.txt");
        if (!mal.getParentFile().exists()) mal.getParentFile().mkdirs();
        java.io.BufferedWriter w = new java.io.BufferedWriter(new java.io.FileWriter(mal));
        w.write("@usuario\n@otro\n@otro2\n");
        w.close();
        boolean okFormatoMal = gestor.validarFormato(mal);
        System.out.println("validarFormato(archivo inválido) = " + okFormatoMal);
        if (okFormatoMal) throw new AssertionError("El archivo inválido no debería pasar validación de formato");

        ok("Validaciones de formato correctas");
    }

    private static int contarPorNombre(Usuario[] usuarios, String[] esperados) {
        int c = 0;
        for (int i = 0; i < esperados.length; i++) {
            String nombre = esperados[i];
            for (int j = 0; j < usuarios.length; j++) {
                if (usuarios[j].getNombre().equals(nombre)) {
                    c++; break;
                }
            }
        }
        return c;
    }

    private static void imprimirMuestraUsuarios(GrafoDirigido grafo, int max) {
        Usuario[] usuarios = grafo.obtenerUsuarios();
        int limite = Math.min(max, usuarios.length);
        System.out.println("Muestra de usuarios (" + limite + "):");
        for (int i = 0; i < limite; i++) {
            System.out.println("  - " + usuarios[i].getNombre());
        }
    }

    private static void imprimirRelacionesDe(GrafoDirigido grafo, String nombre) {
        Usuario[] vecinos = grafo.obtenerVecinos(nombre);
        System.out.println("Vecinos de " + nombre + " (" + vecinos.length + "):");
        for (int i = 0; i < vecinos.length; i++) {
            System.out.println("  " + nombre + " -> " + vecinos[i].getNombre());
        }
    }

    private static void titulo(String t) {
        System.out.println("\n===============================");
        System.out.println(t);
        System.out.println("===============================\n");
    }

    private static void seccion(String s) {
        System.out.println("\n-- " + s + " --");
    }

    private static void ok(String msg) {
        System.out.println("[OK] " + msg);
    }
}


