package Controller;

import Modelos.Vehiculo;
import Modelos.Auto;
import Modelos.Moto;
import Modelos.Registro;
import Modelos.Tarifa;
import Persistencia.RepositorioVehiculo;
import Persistencia.RepositorioRegistro;
import Persistencia.RepositorioTarifa;
import Logica.ServicioTarifa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class IngresoController {
    
    @FXML private TextField txtPlaca;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtCliente;
    @FXML private ComboBox<String> cmbTipo;
    
    // Repositorios compartidos (estáticos)
    private static RepositorioVehiculo repoVehiculo = new RepositorioVehiculo();
    private static RepositorioRegistro repoRegistro = new RepositorioRegistro();
    private static RepositorioTarifa repoTarifa = new RepositorioTarifa();
    private static ServicioTarifa servicioTarifa = new ServicioTarifa(repoTarifa);
    
    @FXML
    public void initialize() {
        // Configurar ComboBox con tipos de vehículos
        cmbTipo.getItems().addAll("AUTO", "MOTO");
        cmbTipo.setValue("AUTO"); // Valor por defecto
        
        // Auto-completar datos si el vehículo ya existe
        txtPlaca.setOnAction(e -> autocompletarDatos());
        txtPlaca.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && !txtPlaca.getText().trim().isEmpty()) {
                autocompletarDatos();
            }
        });
    }
    
    /**
     * Autocompleta marca y modelo si el vehículo ya existe en el sistema
     */
    private void autocompletarDatos() {
        String placa = txtPlaca.getText().trim().toUpperCase();
        if (placa.isEmpty()) return;
        
        try {
            Vehiculo vehiculoExistente = repoVehiculo.buscar(placa);
            
            // Autocompletar datos
            cmbTipo.setValue(vehiculoExistente.getTipo());
            txtMarca.setText(vehiculoExistente.getMarca());
            txtModelo.setText(vehiculoExistente.getModelo());
            
            // Deshabilitar campos si el vehículo ya existe
            cmbTipo.setDisable(true);
            
            System.out.println("ℹ️ Vehículo encontrado: " + vehiculoExistente.getPlaca());
            
        } catch (Exception e) {
            // Vehículo no existe, habilitar todos los campos
            cmbTipo.setDisable(false);
            System.out.println("ℹ️ Vehículo nuevo, ingrese los datos");
        }
    }
    
    @FXML
    private void registrarIngreso() {
        try {
            // Validar campos obligatorios
            String placa = txtPlaca.getText().trim().toUpperCase();
            String tipo = cmbTipo.getValue();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            
            if (placa.isEmpty()) {
                mostrarAlerta("Error", "Debe ingresar una placa", AlertType.ERROR);
                txtPlaca.requestFocus();
                return;
            }
            
            if (tipo == null) {
                mostrarAlerta("Error", "Debe seleccionar un tipo de vehículo", AlertType.ERROR);
                cmbTipo.requestFocus();
                return;
            }
            
            // Usar valores por defecto si marca/modelo están vacíos
            if (marca.isEmpty()) {
                marca = "SIN ESPECIFICAR";
            }
            if (modelo.isEmpty()) {
                modelo = "SIN ESPECIFICAR";
            }
            
            // Verificar si el vehículo ya existe
            Vehiculo vehiculo;
            boolean esVehiculoNuevo = false;
            
            try {
                vehiculo = repoVehiculo.buscar(placa);
                
                // Si ya existe, verificar que no esté actualmente en el parqueadero
                if (estaEnParqueadero(placa)) {
                    mostrarAlerta("Error", 
                        "El vehículo con placa " + placa + " ya está en el parqueadero", 
                        AlertType.ERROR);
                    return;
                }
                
                System.out.println("✅ Vehículo existente encontrado: " + vehiculo.getPlaca());
                
            } catch (Exception e) {
                // El vehículo no existe, crearlo
                esVehiculoNuevo = true;
                
                if (tipo.equals("AUTO")) {
                    vehiculo = new Auto(placa, marca, modelo);
                } else {
                    vehiculo = new Moto(placa, marca, modelo);
                }
                
                repoVehiculo.guardar(vehiculo);
                System.out.println("➕ Nuevo vehículo registrado: " + placa);
            }
            
            // Crear registro de entrada
            Registro registro = new Registro(vehiculo);
            repoRegistro.guardar(registro);
            
            // Construir mensaje de éxito
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("✅ Ingreso registrado correctamente\n\n");
            mensaje.append("📋 Detalles:\n");
            mensaje.append("━━━━━━━━━━━━━━━━━━━━━\n");
            mensaje.append("🚗 Placa: ").append(placa).append("\n");
            mensaje.append("📦 Tipo: ").append(tipo).append("\n");
            mensaje.append("🏷️ Marca: ").append(marca).append("\n");
            mensaje.append("📝 Modelo: ").append(modelo).append("\n");
            mensaje.append("🕐 Hora: ").append(
                java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                )
            ).append("\n");
            
            if (esVehiculoNuevo) {
                mensaje.append("\n🆕 Vehículo nuevo registrado en el sistema");
            }
            
            // Mostrar confirmación
            mostrarAlerta("Éxito", mensaje.toString(), AlertType.INFORMATION);
            
            // Cerrar ventana automáticamente
            cerrarVentana();
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al registrar ingreso: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Verifica si un vehículo está actualmente en el parqueadero
     */
    private boolean estaEnParqueadero(String placa) {
        for (Registro r : repoRegistro.listarRegistros()) {
            if (r.getVehiculo().getPlaca().equalsIgnoreCase(placa) && r.getFechaSalida() == null) {
                return true;
            }
        }
        return false;
    }
    
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtPlaca.getScene().getWindow();
        stage.close();
    }
    
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    // Métodos estáticos para acceder a los repositorios desde otros controladores
    public static RepositorioVehiculo getRepoVehiculo() {
        return repoVehiculo;
    }
    
    public static RepositorioRegistro getRepoRegistro() {
        return repoRegistro;
    }
    
    public static RepositorioTarifa getRepoTarifa() {
        return repoTarifa;
    }
    
    public static ServicioTarifa getServicioTarifa() {
        return servicioTarifa;
    }
}