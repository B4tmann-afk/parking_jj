package Controller;

import Modelos.Tarifa;
import Logica.ServicioTarifa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TarifasController {
    
    @FXML private TextField txtTarifaMotoHora;
    @FXML private TextField txtTarifaMotoDia;
    @FXML private TextField txtTarifaAutoHora;
    @FXML private TextField txtTarifaAutoDia;
    
    // Usar el servicio compartido
    private final ServicioTarifa servicioTarifa = IngresoController.getServicioTarifa();
    
    @FXML
    public void initialize() {
        cargarTarifasActuales();
        
        // Solo permitir números en los campos
        configurarCampoNumerico(txtTarifaMotoHora);
        configurarCampoNumerico(txtTarifaMotoDia);
        configurarCampoNumerico(txtTarifaAutoHora);
        configurarCampoNumerico(txtTarifaAutoDia);
    }
    
    /**
     * Carga las tarifas actuales del sistema
     */
    private void cargarTarifasActuales() {
        try {
            Tarifa tarifaMoto = servicioTarifa.obtenerTarifa("MOTO");
            Tarifa tarifaAuto = servicioTarifa.obtenerTarifa("AUTO");
            
            txtTarifaMotoHora.setText(String.format("%.0f", tarifaMoto.getValorHora()));
            txtTarifaMotoDia.setText(String.format("%.0f", tarifaMoto.getValorDia()));
            txtTarifaAutoHora.setText(String.format("%.0f", tarifaAuto.getValorHora()));
            txtTarifaAutoDia.setText(String.format("%.0f", tarifaAuto.getValorDia()));
            
        } catch (Exception e) {
            System.err.println("Error al cargar tarifas: " + e.getMessage());
            // Valores por defecto si hay error
            txtTarifaMotoHora.setText("2000");
            txtTarifaMotoDia.setText("20000");
            txtTarifaAutoHora.setText("3000");
            txtTarifaAutoDia.setText("30000");
        }
    }
    
    /**
     * Configura un campo para que solo acepte números
     */
    private void configurarCampoNumerico(TextField campo) {
        campo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                campo.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
    
    /**
     * Actualiza las tarifas en el sistema
     */
    @FXML
    private void actualizarTarifas() {
        try {
            // Validar campos
            if (!validarCampos()) {
                return;
            }
            
            // Obtener valores
            double motoHora = Double.parseDouble(txtTarifaMotoHora.getText());
            double motoDia = Double.parseDouble(txtTarifaMotoDia.getText());
            double autoHora = Double.parseDouble(txtTarifaAutoHora.getText());
            double autoDia = Double.parseDouble(txtTarifaAutoDia.getText());
            
            // Validar que las tarifas tengan sentido
            if (motoDia < motoHora) {
                mostrarAlerta("Error de Validación", 
                    "La tarifa diaria de motos debe ser mayor o igual a la tarifa por hora", 
                    AlertType.WARNING);
                return;
            }
            
            if (autoDia < autoHora) {
                mostrarAlerta("Error de Validación", 
                    "La tarifa diaria de autos debe ser mayor o igual a la tarifa por hora", 
                    AlertType.WARNING);
                return;
            }
            
            // Actualizar tarifas
            servicioTarifa.actualizarTarifa("MOTO", motoHora, motoDia);
            servicioTarifa.actualizarTarifa("AUTO", autoHora, autoDia);
            
            // Mensaje de éxito
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("✅ Tarifas actualizadas exitosamente\n\n");
            mensaje.append("📊 NUEVAS TARIFAS:\n");
            mensaje.append("━━━━━━━━━━━━━━━━━━━━━━━\n");
            mensaje.append("🏍️ MOTOS:\n");
            mensaje.append("   • Por hora: $").append(String.format("%.0f", motoHora)).append("\n");
            mensaje.append("   • Por día: $").append(String.format("%.0f", motoDia)).append("\n\n");
            mensaje.append("🚗 AUTOS:\n");
            mensaje.append("   • Por hora: $").append(String.format("%.0f", autoHora)).append("\n");
            mensaje.append("   • Por día: $").append(String.format("%.0f", autoDia)).append("\n");
            
            mostrarAlerta("Éxito", mensaje.toString(), AlertType.INFORMATION);
            
            System.out.println("✅ Tarifas actualizadas correctamente");
            
            // Cerrar ventana
            cerrarVentana();
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Por favor ingrese valores numéricos válidos", AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar tarifas: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Restablece las tarifas a los valores actuales del sistema
     */
    @FXML
    private void restablecerTarifas() {
        cargarTarifasActuales();
        mostrarAlerta("Información", "Valores restablecidos a las tarifas actuales", AlertType.INFORMATION);
    }
    
    /**
     * Valida que todos los campos estén llenos y sean válidos
     */
    private boolean validarCampos() {
        if (txtTarifaMotoHora.getText().trim().isEmpty() ||
            txtTarifaMotoDia.getText().trim().isEmpty() ||
            txtTarifaAutoHora.getText().trim().isEmpty() ||
            txtTarifaAutoDia.getText().trim().isEmpty()) {
            
            mostrarAlerta("Campos Vacíos", 
                "Por favor complete todos los campos de tarifas", 
                AlertType.WARNING);
            return false;
        }
        
        try {
            double motoHora = Double.parseDouble(txtTarifaMotoHora.getText());
            double motoDia = Double.parseDouble(txtTarifaMotoDia.getText());
            double autoHora = Double.parseDouble(txtTarifaAutoHora.getText());
            double autoDia = Double.parseDouble(txtTarifaAutoDia.getText());
            
            if (motoHora <= 0 || motoDia <= 0 || autoHora <= 0 || autoDia <= 0) {
                mostrarAlerta("Valores Inválidos", 
                    "Las tarifas deben ser mayores a cero", 
                    AlertType.WARNING);
                return false;
            }
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Formato Inválido", 
                "Por favor ingrese solo números enteros", 
                AlertType.WARNING);
            return false;
        }
        
        return true;
    }
    
    /**
     * Cierra la ventana actual
     */
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtTarifaMotoHora.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Muestra una alerta
     */
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}