package utils;

import exceptions.FormatoArchivoException;
import model.GrafoDirigido;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Lee y escribe grafos desde/hacia archivos de texto con el formato especificado.
 */
public class GestorArchivos {

    /** Carga un grafo desde archivo de texto con formato del enunciado. */
    public GrafoDirigido cargar(File archivo) throws IOException, FormatoArchivoException {
        if (archivo == null || archivo.exists() == false) {
            throw new IOException("Archivo no encontrado: " + archivo.getAbsolutePath());
        }
        
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(archivo));
            GrafoDirigido grafo = new GrafoDirigido();
            String linea;
            boolean enUsuarios = false;
            boolean enRelaciones = false;
            // arreglo dinámico para relaciones pendientes (pares origen, destino)
            String[][] relacionesPendientes = new String[100][2];  // Tamaño fijo
            int relacionesCount = 0;
            
            while ((linea = br.readLine()) != null) {
                String l = linea.trim();
                if (l.isEmpty()) {
                    continue;
                }
                if (l.equalsIgnoreCase("usuarios")) {
                    enUsuarios = true;
                    enRelaciones = false;
                    continue;
                }
                if (l.equalsIgnoreCase("relaciones")) {
                    enUsuarios = false;
                    enRelaciones = true;
                    continue;
                }
                
                if (enUsuarios == true) {
                    if (ValidadorEntradas.validarNombreUsuario(l) == false) {
                        throw new FormatoArchivoException("Línea de usuario inválida: " + l);
                    }
                    if (grafo.existeUsuario(l) == false) {
                        grafo.agregarUsuario(l);
                    }
                } else if (enRelaciones == true) {
                    if (ValidadorEntradas.validarFormatoRelacion(l) == false) {
                        throw new FormatoArchivoException("Línea de relación inválida: " + l);
                    }
                    String[] partes = l.split(",");
                    String origen = partes[0].trim();
                    String destino = partes[1].trim();
                    
                    relacionesPendientes[relacionesCount][0] = origen;
                    relacionesPendientes[relacionesCount][1] = destino;
                    relacionesCount = relacionesCount + 1;
                }
            }
            
            // Validaciones básicas
            if (grafo.estaVacio() == true) {
                throw new FormatoArchivoException("El archivo no contiene usuarios");
            }
            
            // Crear relaciones tras cargar usuarios
            for (int idx = 0; idx < relacionesCount; idx++) {
                String o = relacionesPendientes[idx][0];
                String d = relacionesPendientes[idx][1];
                if (grafo.existeUsuario(o) == false || grafo.existeUsuario(d) == false) {
                    throw new FormatoArchivoException("Relación refiere a usuario inexistente: " + o + " -> " + d);
                }
                if (o.equals(d) == false) {
                    grafo.agregarRelacion(o, d);
                }
            }
            
            return grafo;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // Ignorar error al cerrar
                }
            }
        }
    }

    /** Guarda el grafo en un archivo de texto siguiendo el formato del enunciado. */
    public void guardar(File archivo, GrafoDirigido grafo) throws IOException {
        if (archivo == null) {
            throw new IOException("Ruta de archivo no válida");
        }
        
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write("usuarios\n");
            // escribir usuarios en orden de inserción
            model.Usuario[] usuarios = grafo.obtenerUsuarios();
            for (int i = 0; i < usuarios.length; i++) {
                bw.write(usuarios[i].getNombre());
                bw.newLine();
            }
            bw.newLine();
            bw.write("relaciones\n");
            // Escribir relaciones en orden de inserción
            for (int i = 0; i < usuarios.length; i++) {
                String origen = usuarios[i].getNombre();
                model.Usuario[] vecinos = grafo.obtenerVecinos(origen);
                for (int j = 0; j < vecinos.length; j++) {
                    String destino = vecinos[j].getNombre();
                    bw.write(origen + ", " + destino);
                    bw.newLine();
                }
            }
        } catch (RuntimeException wrap) {
            if (wrap.getCause() instanceof IOException) {
                throw (IOException) wrap.getCause();
            }
            throw wrap;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    // Ignorar error al cerrar
                }
            }
        }
    }

    /** Valida rápidamente el formato del archivo (secciones y estructura general). */
    public boolean validarFormato(File archivo) throws IOException {
        if (archivo == null || archivo.exists() == false) {
            return false;
        }
        
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(archivo));
            String linea;
            boolean vioUsuarios = false;
            boolean vioRelaciones = false;
            while ((linea = br.readLine()) != null) {
                String l = linea.trim();
                if (l.equalsIgnoreCase("usuarios")) {
                    vioUsuarios = true;
                }
                if (l.equalsIgnoreCase("relaciones")) {
                    vioRelaciones = true;
                }
            }
            return vioUsuarios && vioRelaciones;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // Ignorar error al cerrar
                }
            }
        }
    }
}