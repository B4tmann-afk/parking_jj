package Controller;

import Modelos.Registro;
import Modelos.Tarifa;
import Logica.ServicioTarifa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SalidaController {
    
    @FXML private TextField txtPlacaSalida;
    @FXML private Label lblTarifaCarro;
    @FXML private Label lblTarifaMoto;
    @FXML private Label lblInfo;
    @FXML private VBox panelInfo;
    
    // Usar el servicio compartido del IngresoController
    private final ServicioTarifa servicioTarifa = IngresoController.getServicioTarifa();
    private Registro registroActual = null;
    
    @FXML
    public void initialize() {
        cargarTarifas();
    }
    
    /**
     * Carga las tarifas actuales
     */
    private void cargarTarifas() {
        try {
            Tarifa moto = servicioTarifa.obtenerTarifa("MOTO");
            Tarifa auto = servicioTarifa.obtenerTarifa("AUTO");
            
            lblTarifaMoto.setText("$" + String.format("%.0f", moto.getValorHora()) + " / hora");
            lblTarifaCarro.setText("$" + String.format("%.0f", auto.getValorHora()) + " / hora");
        } catch (Exception e) {
            lblTarifaMoto.setText("No definida");
            lblTarifaCarro.setText("No definida");
            System.err.println("Error al cargar tarifas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Busca el vehículo y muestra su información
     */
    @FXML
    private void buscarVehiculo() {
        // Recargar tarifas para mostrar las más actuales
        cargarTarifas();
        
        String placa = txtPlacaSalida.getText().trim().toUpperCase();
        
        if (placa.isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar una placa", AlertType.ERROR);
            return;
        }
        
        try {
            // Buscar registro activo
            registroActual = null;
            for (Registro r : IngresoController.getRepoRegistro().listarRegistros()) {
                if (r.getVehiculo().getPlaca().equalsIgnoreCase(placa) && r.getFechaSalida() == null) {
                    registroActual = r;
                    break;
                }
            }
            
            if (registroActual == null) {
                lblInfo.setText("⚠️ Vehículo no encontrado en el parqueadero");
                lblInfo.setStyle("-fx-font-size: 14px; -fx-text-fill: #e74c3c;");
                return;
            }
            
            // Calcular tiempo y costo
            long minutos = registroActual.calcularMinutos();
            double horas = Math.ceil(minutos / 60.0); // Redondear hacia arriba
            
            String tipo = registroActual.getVehiculo().getTipo();
            Tarifa tarifa = servicioTarifa.obtenerTarifa(tipo);
            double costo = horas * tarifa.getValorHora();
            
            String horaIngreso = registroActual.getFechaEntrada().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            );
            
            // Mostrar información
            lblInfo.setText(
                "✅ Vehículo encontrado:\n" +
                "Placa: " + placa + "\n" +
                "Tipo: " + tipo + "\n" +
                "Hora ingreso: " + horaIngreso + "\n" +
                "Tiempo: " + minutos + " minutos (" + (int)horas + " horas)\n" +
                "💰 TOTAL A PAGAR: $" + String.format("%.0f", costo)
            );
            lblInfo.setStyle("-fx-font-size: 14px; -fx-text-fill: #2ecc71; -fx-font-weight: bold;");
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al buscar vehículo: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Registra la salida y cobra
     */
    @FXML
    private void registrarSalida() {
        // Recargar tarifas para usar las más actuales
        cargarTarifas();
        
        String placa = txtPlacaSalida.getText().trim().toUpperCase();
        
        if (placa.isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar una placa", AlertType.ERROR);
            return;
        }
        
        try {
            // Buscar registro activo si no se ha buscado antes
            if (registroActual == null) {
                for (Registro r : IngresoController.getRepoRegistro().listarRegistros()) {
                    if (r.getVehiculo().getPlaca().equalsIgnoreCase(placa) && r.getFechaSalida() == null) {
                        registroActual = r;
                        break;
                    }
                }
            }
            
            if (registroActual == null) {
                mostrarAlerta("Error", 
                    "Vehículo con placa " + placa + " no encontrado en el parqueadero", 
                    AlertType.ERROR);
                return;
            }
            
            // Calcular costo
            long minutos = registroActual.calcularMinutos();
            double horas = Math.ceil(minutos / 60.0);
            
            String tipo = registroActual.getVehiculo().getTipo();
            Tarifa tarifa = servicioTarifa.obtenerTarifa(tipo);
            double costo = horas * tarifa.getValorHora();
            
            // Registrar salida
            registroActual.setFechaSalida(LocalDateTime.now());
            registroActual.setCosto(costo);
            
            // Actualizar en el repositorio
            IngresoController.getRepoRegistro().actualizar(registroActual);
            
            // Mostrar resumen
            String horaIngreso = registroActual.getFechaEntrada().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            );
            String horaSalida = registroActual.getFechaSalida().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            );
            
            mostrarAlerta("Salida Registrada", 
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                "🧾 RECIBO DE PAGO\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                "Placa: " + placa + "\n" +
                "Tipo: " + tipo + "\n\n" +
                "Ingreso: " + horaIngreso + "\n" +
                "Salida: " + horaSalida + "\n\n" +
                "Tiempo: " + minutos + " minutos\n" +
                "Horas cobradas: " + (int)horas + "\n" +
                "Tarifa: $" + String.format("%.0f", tarifa.getValorHora()) + " / hora\n\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                "💰 TOTAL: $" + String.format("%.0f", costo) + "\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                "¡Gracias por usar nuestro servicio!", 
                AlertType.INFORMATION);
            
            // Limpiar y cerrar
            txtPlacaSalida.clear();
            lblInfo.setText("Ingrese una placa para ver la información");
            lblInfo.setStyle("-fx-font-size: 14px; -fx-text-fill: #8b949e;");
            registroActual = null;
            
            cerrarVentana();
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al registrar salida: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtPlacaSalida.getScene().getWindow();
        stage.close();
    }
    
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}