package controller;

import utils.GestorArchivos;
import view.VentanaPrincipal;
import view.VisualizadorGrafo;
import view.DialogoAgregarUsuario;
import view.DialogoEliminarUsuario;
import view.DialogoGestionarRelaciones;
import model.GrafoDirigido;
import algorithms.AlgoritmoKosaraju;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * DIA 3: Controlador principal con integraciones de JFileChooser para cargar/guardar.
 */
public class ControladorPrincipal {
    private  VentanaPrincipal vista;
    private  GestorArchivos gestorArchivos = new GestorArchivos();

    private GrafoDirigido grafoActual;
    private File archivoActual;
    private boolean datosModificados = false;

    public ControladorPrincipal(VentanaPrincipal vista) {
        this.vista = vista;
        conectarEventos();
    }

    // DIA 5: Método público para inicializar grafo desde Main (carga automática)
    public void inicializarGrafo(GrafoDirigido grafo, File archivo) {
        this.grafoActual = grafo;
        this.archivoActual = archivo;
        this.datosModificados = false;
        vista.refrescarUsuarios(grafoActual);
    }

    // DIA 3: Conectar listeners de menú
    private void conectarEventos() {
        vista.getItemCargar().addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onCargar(); }
        });
        vista.getItemGuardar().addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onGuardar(); }
        });
        vista.getItemSalir().addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onSalir(); }
        });
        // DIA 4: acciones de análisis
        vista.getItemAnalizar().addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onAnalizar(); }
        });
        vista.getItemLimpiarAnalisis().addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onLimpiarAnalisis(); }
        });
        // DIA 5: botón de refrescar análisis
        vista.getBtnRefrescarAnalisis().addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onRefrescarAnalisis(); }
        });
        // DIA 5: edición
        vista.getItemAgregarUsuario().addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onAgregarUsuario(); }
        });
        vista.getItemEliminarUsuario().addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onEliminarUsuario(); }
        });
        // DIA 5: relaciones
        vista.getItemGestionarRelaciones().addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { onGestionarRelaciones(); }
        });
    }

    // DIA 3: Acción Cargar
    private void onCargar() {
        if (datosModificados) {
            if (!confirmar("Hay cambios sin guardar. ¿Desea continuar sin guardar?")) return;
        }
        JFileChooser fc = new JFileChooser();
        int res = fc.showOpenDialog(vista);
        if (res == JFileChooser.APPROVE_OPTION) {
            File sel = fc.getSelectedFile();
            try {
                grafoActual = gestorArchivos.cargar(sel);
                archivoActual = sel;
                datosModificados = false;
                vista.refrescarUsuarios(grafoActual);
                informar("Archivo cargado: " + sel.getName());
            } catch (Exception ex) {
                error("Error al cargar archivo: " + ex.getMessage());
            }
        }
    }

    // DIA 3: Acción Guardar
    private void onGuardar() {
        if (grafoActual == null) {
            informar("No hay grafo para guardar.");
            return;
        }
        JFileChooser fc = new JFileChooser();
        if (archivoActual != null) fc.setSelectedFile(archivoActual);
        int res = fc.showSaveDialog(vista);
        if (res == JFileChooser.APPROVE_OPTION) {
            File sel = fc.getSelectedFile();
            try {
                gestorArchivos.guardar(sel, grafoActual);
                archivoActual = sel;
                datosModificados = false;
                informar("Guardado en: " + sel.getName());
            } catch (Exception ex) {
                error("Error al guardar: " + ex.getMessage());
            }
        }
    }

    // DIA 3: Acción Salir
    private void onSalir() {
        if (datosModificados) {
            if (!confirmar("Hay cambios sin guardar. ¿Salir igualmente?")) return;
        }
        System.exit(0);
    }

    // DIA 4: Ejecutar análisis de componentes y mostrar visualización placeholder
    private void onAnalizar() {
        if (grafoActual == null) {
            informar("Cargue primero un archivo con usuarios/relaciones.");
            return;
        }
        // Ejecutar Kosaraju (resultado no mostrado aún, pero valida el flujo)
        try {
            model.Usuario[][] componentes = AlgoritmoKosaraju.encontrarComponentes(grafoActual);
            vista.setContenidoCentral(VisualizadorGrafo.construirPanel(grafoActual, componentes));
            vista.actualizarLeyenda(componentes, VisualizadorGrafo.PALETA);
            informar("Análisis completado. Componentes encontrados: " + componentes.length);
        } catch (RuntimeException ex) {
            error("Error durante el análisis: " + ex.getMessage());
        }
    }

    // DIA 5: Refrescar análisis (útil después de modificar el grafo)
    private void onRefrescarAnalisis() {
        if (grafoActual == null || grafoActual.estaVacio()) {
            informar("No hay grafo cargado para analizar.");
            return;
        }
        onAnalizar(); // Ejecuta el análisis nuevamente
    }

    // DIA 4: Limpiar panel central (quitar visualización)
    private void onLimpiarAnalisis() {
        vista.setContenidoCentral(new javax.swing.JPanel());
        vista.actualizarLeyenda(null, null);
    }

    // DIA 5: Agregar usuario simplificado (solo nombre)
    private void onAgregarUsuario() {
        if (grafoActual == null) grafoActual = new GrafoDirigido();
        
        DialogoAgregarUsuario dialogo = new DialogoAgregarUsuario(null, grafoActual);
        dialogo.setVisible(true);
        
        if (!dialogo.fueAceptado()) return;
        
        try {
            String nombre = dialogo.getNombreUsuario();
            grafoActual.agregarUsuario(nombre);
            datosModificados = true;
            vista.refrescarUsuarios(grafoActual);
            informar("Usuario " + nombre + " agregado.\n\n" +
                    "• Usa menú 'Relaciones' para conectarlo\n" +
                    "• Usa 'Análisis → Refrescar' para actualizar componentes");
        } catch (RuntimeException ex) {
            error("No se pudo agregar: " + ex.getMessage());
        }
    }

    // DIA 5: Eliminar usuario con diálogo mostrando conexiones
    private void onEliminarUsuario() {
        if (grafoActual == null || grafoActual.estaVacio()) {
            informar("No hay usuarios para eliminar.");
            return;
        }
        
        DialogoEliminarUsuario dialogo = new DialogoEliminarUsuario(null, grafoActual);
        dialogo.setVisible(true);
        
        if (!dialogo.fueAceptado()) return;
        
        try {
            String nombre = dialogo.getUsuarioSeleccionado();
            grafoActual.eliminarUsuario(nombre);
            datosModificados = true;
            vista.refrescarUsuarios(grafoActual);
            informar("Usuario " + nombre + " eliminado.\n\n" +
                    "💡 Usa 'Análisis → Refrescar' para actualizar componentes");
        } catch (RuntimeException ex) {
            error("No se pudo eliminar: " + ex.getMessage());
        }
    }

    // DIA 5: Gestionar relaciones (agregar/eliminar)
    private void onGestionarRelaciones() {
        if (grafoActual == null || grafoActual.estaVacio()) {
            informar("Debe haber al menos un usuario en el grafo para gestionar relaciones.");
            return;
        }
        
        if (grafoActual.contarUsuarios() < 2) {
            informar("Necesitas al menos 2 usuarios para crear relaciones.\nAgrega más usuarios primero.");
            return;
        }
        
        DialogoGestionarRelaciones dialogo = new DialogoGestionarRelaciones(null, grafoActual);
        dialogo.setVisible(true);
        
        if (dialogo.fueronModificados()) {
            datosModificados = true;
            vista.refrescarUsuarios(grafoActual);
            informar("Relaciones actualizadas.\n\n" +
                    "💡 Usa 'Análisis → Refrescar' para ver cambios en componentes");
        }
    }

    // Helpers UI
    private void informar(String msg) {
        JOptionPane.showMessageDialog(vista, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(vista, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean confirmar(String msg) {
        int r = JOptionPane.showConfirmDialog(vista, msg, "Confirmar", JOptionPane.YES_NO_OPTION);
        return r == JOptionPane.YES_OPTION;
    }
}


