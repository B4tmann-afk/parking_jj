package Controller;

import Modelos.Tarifa;
import Modelos.Usuario;
import Modelos.Rol;
import Logica.ServicioTarifa;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.net.URL;

public class MenuController {
    
    @FXML private Label lblTarifaMoto;
    @FXML private Label lblTarifaCarro;
    @FXML private Label lblUsuario;
    
    // Botones que dependen del rol
    @FXML private Button btnTarifas;
    @FXML private Button btnClientes;
    @FXML private Button btnTrabajadores;
    
    // Usar el servicio compartido de IngresoController
    private final ServicioTarifa servicioTarifa = IngresoController.getServicioTarifa();
    
    // Usuario actual en sesión
    private Usuario usuarioActual;
    
    @FXML
    public void initialize() {
        cargarTarifas();
    }
    
    /**
     * Establece el usuario actual en sesión y configura permisos
     */
    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        System.out.println("✅ Usuario " + usuario.getNombre() + " (" + usuario.getRol() + ") ha iniciado sesión");
        
        // Mostrar nombre de usuario en el menú
        if (lblUsuario != null) {
            lblUsuario.setText("👤 " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        }
        
        // Configurar permisos según el rol
        configurarPermisos();
    }
    
    /**
     * Configura los permisos según el rol del usuario
     */
    private void configurarPermisos() {
        if (usuarioActual == null) {
            return;
        }
        
        Rol rol = usuarioActual.getRol();
        
        if (rol == Rol.TRABAJADOR) {
            // TRABAJADORES: Solo pueden registrar ingresos/salidas y ver vehículos
            deshabilitarBoton(btnTarifas, "🔒 Tarifas\n(Solo Admin)");
            deshabilitarBoton(btnClientes, "🔒 Clientes\n(Solo Admin)");
            deshabilitarBoton(btnTrabajadores, "🔒 Trabajadores\n(Solo Admin)");
            
            System.out.println("🔒 Permisos de TRABAJADOR aplicados - Acceso limitado");
            
        } else if (rol == Rol.ADMIN) {
            // ADMINISTRADORES: Acceso completo
            System.out.println("🔓 Permisos de ADMINISTRADOR - Acceso completo");
        }
    }
    
    /**
     * Deshabilita un botón y cambia su texto
     */
    private void deshabilitarBoton(Button boton, String nuevoTexto) {
        if (boton != null) {
            boton.setDisable(true);
            boton.setText(nuevoTexto);
            boton.setStyle(boton.getStyle() + "; -fx-opacity: 0.5; -fx-cursor: not-allowed;");
        }
    }
    
    /**
     * Obtiene el usuario actual
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    /**
     * Carga y muestra las tarifas actuales
     */
    private void cargarTarifas() {
        try {
            Tarifa tarifaMoto = servicioTarifa.obtenerTarifa("MOTO");
            Tarifa tarifaCarro = servicioTarifa.obtenerTarifa("AUTO");
            
            lblTarifaMoto.setText("🏍️ Moto: $" + String.format("%.0f", tarifaMoto.getValorHora()) + "/hora");
            lblTarifaCarro.setText("🚗 Carro: $" + String.format("%.0f", tarifaCarro.getValorHora()) + "/hora");
            
        } catch (Exception e) {
            lblTarifaMoto.setText("🏍️ Moto: no definida");
            lblTarifaCarro.setText("🚗 Carro: no definida");
            System.err.println("Error al cargar tarifas: " + e.getMessage());
        }
    }
    
    /**
     * Refresca las tarifas mostradas (llamar después de modificarlas)
     */
    public void refrescarTarifas() {
        cargarTarifas();
    }
    
    @FXML
    private void abrirIngreso() {
        abrirVentana("vista_ingreso.fxml", "Registrar Ingreso");
    }
    
    @FXML
    private void abrirSalida() {
        abrirVentana("vista_salida.fxml", "Registrar Salida");
    }
    
    @FXML
    private void abrirTarifas() {
        // Verificar permiso antes de abrir
        if (!tienePermiso(Rol.ADMIN)) {
            mostrarAlertaPermisos("Gestión de Tarifas");
            return;
        }
        
        abrirVentana("vista_tarifas.fxml", "Gestión de Tarifas");
        cargarTarifas(); // Refrescar tarifas cuando se cierre la ventana
    }
    
    @FXML
    private void abrirVehiculos() {
        // Todos pueden ver vehículos
        abrirVentana("vista_vehiculos.fxml", "Gestión de Vehículos");
    }
    
    @FXML
    private void abrirClientes() {
        // Verificar permiso antes de abrir
        if (!tienePermiso(Rol.ADMIN)) {
            mostrarAlertaPermisos("Gestión de Clientes");
            return;
        }
        
        abrirVentana("vista_clientes.fxml", "Gestión de Clientes");
    }
    
    @FXML
    private void abrirTrabajadores() {
        // Verificar permiso antes de abrir
        if (!tienePermiso(Rol.ADMIN)) {
            mostrarAlertaPermisos("Gestión de Trabajadores");
            return;
        }
        
        abrirVentana("vista_trabajadores.fxml", "Gestión de Trabajadores");
    }
    
    @FXML
    private void salir() {
        System.out.println("👋 Usuario " + usuarioActual.getNombre() + " ha cerrado sesión");
        System.exit(0);
    }
    
    /**
     * Verifica si el usuario actual tiene el permiso necesario
     */
    private boolean tienePermiso(Rol rolRequerido) {
        if (usuarioActual == null) {
            return false;
        }
        return usuarioActual.getRol() == rolRequerido;
    }
    
    /**
     * Muestra una alerta de permisos insuficientes
     */
    private void mostrarAlertaPermisos(String funcionalidad) {
        javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.WARNING);
        alerta.setTitle("Acceso Denegado");
        alerta.setHeaderText("🔒 Permisos Insuficientes");
        alerta.setContentText(
            "No tiene permisos para acceder a: " + funcionalidad + "\n\n" +
            "Esta función está disponible solo para Administradores.\n" +
            "Su rol actual es: " + usuarioActual.getRol()
        );
        alerta.showAndWait();
        
        System.out.println("⚠️ Acceso denegado a " + funcionalidad + " para usuario " + 
            usuarioActual.getNombre() + " (" + usuarioActual.getRol() + ")");
    }
    
    /**
     * Método auxiliar para abrir ventanas con mejor manejo de rutas
     */
    private void abrirVentana(String fxml, String titulo) {
        try {
            // Intentar múltiples rutas posibles
            String[] rutasPosibles = {
                "/Views/" + fxml,
                "/View/" + fxml,
                "/views/" + fxml,
                "/view/" + fxml,
                "/" + fxml
            };
            
            URL ubicacion = null;
            String rutaEncontrada = null;
            
            // Buscar el archivo en las rutas posibles
            for (String ruta : rutasPosibles) {
                System.out.println("Intentando cargar: " + ruta);
                ubicacion = getClass().getResource(ruta);
                if (ubicacion != null) {
                    rutaEncontrada = ruta;
                    System.out.println("✅ Archivo encontrado en: " + ruta);
                    break;
                }
            }
            
            // Si no se encontró el archivo
            if (ubicacion == null) {
                throw new Exception("Archivo FXML no encontrado en ninguna ruta conocida");
            }
            
            // Cargar el FXML
            FXMLLoader loader = new FXMLLoader(ubicacion);
            Parent root = loader.load();
            
            // Crear y mostrar la ventana
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            // Refrescar tarifas después de cerrar cualquier ventana
            cargarTarifas();
            
        } catch (Exception e) {
            System.err.println("❌ Error al abrir ventana " + fxml + ": " + e.getMessage());
            e.printStackTrace();
            
            // Mostrar alerta detallada al usuario
            javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
            alerta.setTitle("Error al Abrir Ventana");
            alerta.setHeaderText("No se pudo cargar: " + fxml);
            alerta.setContentText(
                "Detalles del error:\n" + e.getMessage() + 
                "\n\nVerifique que:\n" +
                "1. El archivo existe en src/View/ o src/Views/\n" +
                "2. El proyecto está compilado (Clean and Build)\n" +
                "3. El nombre del archivo es exacto (mayúsculas/minúsculas)"
            );
            alerta.showAndWait();
        }
    }
}