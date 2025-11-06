classDiagram
    direction TB
    
    %% ============================================
    %% PUNTO DE ENTRADA (main package)
    %% ============================================
    
    class Main {
        <<application>>
        + main(String[] args)$ void
    }
    
    %% ============================================
    %% CAPA DE MODELO (model package)
    %% ============================================
    
    class Usuario {
        <<entity>>
        - int id
        - String nombre
        + Usuario(int id, String nombre)
        + int getId()
        + String getNombre()
        + boolean equals(Object o)
        + String toString()
    }
    
    class GrafoDirigido {
        <<aggregate>>
        - Usuario[] usuarios
        - int numUsuarios
        - boolean[][] adj
        - int siguienteId
        - int CAPACIDAD_INICIAL
        + GrafoDirigido()
        + Usuario agregarUsuario(String nombre)
        + void eliminarUsuario(String nombre)
        + void agregarRelacion(String origen, String destino)
        + void eliminarRelacion(String origen, String destino)
        + boolean existeRelacion(String origen, String destino)
        + Usuario[] obtenerVecinos(String nombre)
        + Usuario[] obtenerUsuarios()
        + Usuario buscarUsuario(String nombre)
        + boolean existeUsuario(String nombre)
        + int contarUsuarios()
        + int contarRelaciones()
        + boolean estaVacio()
        + GrafoDirigido obtenerGrafoTranspuesto()
        + double calcularDensidad()
        + int contarRelacionesEntrantes(String nombre)
        + int contarRelacionesSalientes(String nombre)
        - void asegurarCapacidad(int capacidadMinima)
        - int indiceDe(String nombre)
    }
    
    %% ============================================
    %% CAPA DE ALGORITMOS (algorithms package)
    %% ============================================
    
    class AlgoritmoDFS {
        <<utility>>
        + obtenerOrdenFinalizacion(GrafoDirigido)$ Usuario[]
        + recolectarComponentePorIndices(GrafoDirigido, int, boolean[], int[])$ int
        - dfsFinalizacionDesdeIndice(GrafoDirigido, int, boolean[], Usuario[], int)$ int
        - dfsRecolectar(GrafoDirigido, int, boolean[], int[], int)$ int
    }
    
    class AlgoritmoKosaraju {
        <<utility>>
        + encontrarComponentes(GrafoDirigido)$ Usuario[][]
        - indiceDeUsuarioPorNombre(Usuario[], String)$ int
        - expandirMatriz(Usuario[][], int)$ Usuario[][]
    }
    
    %% ============================================
    %% CAPA DE UTILIDADES (utils package)
    %% ============================================
    
    class GestorArchivos {
        <<utility>>
        + cargar(File archivo)$ GrafoDirigido
        + guardar(File archivo, GrafoDirigido grafo)$ void
        + validarFormato(File archivo)$ boolean
    }
    
    class ValidadorEntradas {
        <<utility>>
        + validarNombreUsuario(String)$ boolean
        + validarFormatoRelacion(String)$ boolean
        + normalizarNombre(String)$ String
        + validarDuplicados(String[])$ String[]
    }
    
    class FormatoArchivoException {
        <<exception>>
        + FormatoArchivoException(String mensaje)
        + FormatoArchivoException(String mensaje, Throwable causa)
    }
    
    %% ============================================
    %% CAPA DE VISUALIZACIÓN (view package)
    %% ============================================
    
    class VentanaPrincipal {
        <<boundary>>
        - DefaultListModel~String~ modeloListaUsuarios
        - JList~String~ listaUsuarios
        - JMenuItem itemCargar
        - JMenuItem itemGuardar
        - JMenuItem itemSalir
        - JMenuItem itemAnalizar
        - JMenuItem itemLimpiarAnalisis
        - JMenuItem itemAgregarUsuario
        - JMenuItem itemEliminarUsuario
        - JMenuItem itemGestionarRelaciones
        - JPanel panelCentral
        - JPanel panelLeyenda
        - JButton btnRefrescarAnalisis
        + VentanaPrincipal()
        + void refrescarUsuarios(GrafoDirigido grafo)
        + void setContenidoCentral(Component comp)
        + void actualizarLeyenda(Usuario[][], String[])
        + JMenuItem getItemCargar()
        + JMenuItem getItemGuardar()
        + JMenuItem getItemSalir()
        + JMenuItem getItemAnalizar()
        + JMenuItem getItemLimpiarAnalisis()
        + JMenuItem getItemAgregarUsuario()
        + JMenuItem getItemEliminarUsuario()
        + JMenuItem getItemGestionarRelaciones()
        + JButton getBtnRefrescarAnalisis()
        - void construirMenu()
        - void construirLateralUsuarios()
        - void construirPanelCentral()
        - void construirPanelLeyenda()
        - void construirBarraHerramientas()
    }
    
    class DialogoAgregarUsuario {
        <<boundary>>
        - JTextField txtNombreUsuario
        - JButton btnAceptar
        - JButton btnCancelar
        - boolean aceptado
        - GrafoDirigido grafo
        + DialogoAgregarUsuario(JFrame parent, GrafoDirigido grafo)
        + void setVisible(boolean visible)
        + boolean fueAceptado()
        + String getNombreUsuario()
        - void configurarEventos()
        - void validarYAgregar()
    }
    
    class DialogoEliminarUsuario {
        <<boundary>>
        - JComboBox~String~ comboUsuarios
        - JTextArea txtDetalles
        - JButton btnEliminar
        - JButton btnCancelar
        - boolean confirmado
        - GrafoDirigido grafo
        + DialogoEliminarUsuario(JFrame parent, GrafoDirigido grafo)
        + void setVisible(boolean visible)
        + boolean fueAceptado()
        + String getUsuarioSeleccionado()
        - void configurarEventos()
        - void actualizarDetalles()
        - void eliminarUsuario()
    }
    
    class DialogoGestionarRelaciones {
        <<boundary>>
        - JComboBox~String~ comboOrigen
        - JComboBox~String~ comboDestino
        - JList~String~ listaRelaciones
        - JButton btnAgregar
        - JButton btnEliminar
        - JButton btnCerrar
        - boolean modificado
        - GrafoDirigido grafo
        + DialogoGestionarRelaciones(JFrame parent, GrafoDirigido grafo)
        + void setVisible(boolean visible)
        + boolean fueronModificados()
        - void configurarEventos()
        - void actualizarListaRelaciones()
        - void agregarRelacion()
        - void eliminarRelacion()
    }
    
    class VisualizadorGrafo {
        <<service>>
        - Graph grafoVisual
        - String[] PALETA
        + construirPanel(GrafoDirigido, Usuario[][])$ Component
        + crearVisualizacion(GrafoDirigido, Usuario[][])$ Graph
        + aplicarEstilos(Graph grafo)$ void
        + colorearPorComponentes(Graph, Usuario[][])$ void
        + configurarLayout(Graph grafo)$ void
        - void agregarNodos(GrafoDirigido, Graph)$ void
        - void agregarAristas(GrafoDirigido, Graph)$ void
        - String generarIdArista(String, String)$ String
    }
    
    %% ============================================
    %% CAPA DE CONTROL (controller package)
    %% ============================================
    
    class ControladorPrincipal {
        <<controller>>
        - VentanaPrincipal vista
        - GestorArchivos gestorArchivos
        - GrafoDirigido grafoActual
        - File archivoActual
        - boolean datosModificados
        + ControladorPrincipal(VentanaPrincipal vista)
        + void inicializarGrafo(GrafoDirigido grafo, File archivo)
        - void conectarEventos()
        - void onCargar()
        - void onGuardar()
        - void onSalir()
        - void onAnalizar()
        - void onLimpiarAnalisis()
        - void onRefrescarAnalisis()
        - void onAgregarUsuario()
        - void onEliminarUsuario()
        - void onGestionarRelaciones()
        - void informar(String msg)
        - void error(String msg)
        - boolean confirmar(String msg)
    }
    
    %% ============================================
    %% RELACIONES - HERENCIA
    %% ============================================
    
    Exception <|-- FormatoArchivoException
    JFrame <|-- VentanaPrincipal
    JDialog <|-- DialogoAgregarUsuario
    JDialog <|-- DialogoEliminarUsuario
    JDialog <|-- DialogoGestionarRelaciones
    
    %% ============================================
    %% RELACIONES - COMPOSICIÓN
    %% ============================================
    
    GrafoDirigido *-- Usuario : contiene
    VentanaPrincipal *-- DefaultListModel~String~ : contiene
    VentanaPrincipal *-- JList~String~ : contiene
    VentanaPrincipal *-- JPanel : contiene
    DialogoAgregarUsuario *-- JTextField : contiene
    DialogoEliminarUsuario *-- JComboBox~String~ : contiene
    DialogoEliminarUsuario *-- JTextArea : contiene
    DialogoGestionarRelaciones *-- JComboBox~String~ : contiene
    DialogoGestionarRelaciones *-- JList~String~ : contiene
    VisualizadorGrafo *-- Graph : contiene
    
    %% ============================================
    %% RELACIONES - ASOCIACIÓN
    %% ============================================
    
    Main ..> ControladorPrincipal : "crea y ejecuta"
    
    ControladorPrincipal "1" --> "1" VentanaPrincipal : coordina
    ControladorPrincipal "1" --> "0..1" GrafoDirigido : gestiona
    ControladorPrincipal "1" --> "1" GestorArchivos : usa
    ControladorPrincipal ..> DialogoAgregarUsuario : crea
    ControladorPrincipal ..> DialogoEliminarUsuario : crea
    ControladorPrincipal ..> DialogoGestionarRelaciones : crea
    ControladorPrincipal ..> VisualizadorGrafo : usa
    
    AlgoritmoKosaraju ..> AlgoritmoDFS : "utiliza (static)"
    AlgoritmoDFS ..> GrafoDirigido : "recorre (static)"
    AlgoritmoKosaraju ..> GrafoDirigido : "analiza (static)"
    
    VisualizadorGrafo --> GrafoDirigido : visualiza
    
    GestorArchivos ..> GrafoDirigido : "carga/guarda"
    GestorArchivos ..> ValidadorEntradas : "usa (static)"
    GestorArchivos ..> FormatoArchivoException : "<<throws>>"
    
    DialogoAgregarUsuario ..> ValidadorEntradas : valida
    DialogoAgregarUsuario ..> GrafoDirigido : consulta
    DialogoEliminarUsuario ..> GrafoDirigido : consulta
    DialogoGestionarRelaciones ..> GrafoDirigido : consulta
    
    %% ============================================
    %% RELACIONES - DEPENDENCIA
    %% ============================================
    
    ControladorPrincipal ..> AlgoritmoKosaraju : "usa (static)"
    VentanaPrincipal ..> GrafoDirigido : muestra
    VentanaPrincipal ..> Usuario : muestra
    VisualizadorGrafo ..> Usuario : visualiza