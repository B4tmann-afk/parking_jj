package Controller;

import Modelos.Vehiculo;
import Modelos.Auto;
import Modelos.Moto;
import Modelos.VehiculoRow;
import Persistencia.RepositorioVehiculo;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VehiculosController {
    
    @FXML private TextField txtPlaca;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private ComboBox<String> cmbTipo;
    
    @FXML private TableView<VehiculoRow> tablaVehiculos;
    @FXML private TableColumn<VehiculoRow, String> colPlaca;
    @FXML private TableColumn<VehiculoRow, String> colTipo;
    @FXML private TableColumn<VehiculoRow, String> colMarca;
    @FXML private TableColumn<VehiculoRow, String> colModelo;
    
    // Usar el repositorio compartido del IngresoController
    private final RepositorioVehiculo repoVehiculo = IngresoController.getRepoVehiculo();
    
    private ObservableList<VehiculoRow> listaVehiculosRow = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        // Configurar ComboBox de tipos
        cmbTipo.getItems().addAll("AUTO", "MOTO");
        cmbTipo.setValue("AUTO");
        
        // Configurar columnas de la tabla
        colPlaca.setCellValueFactory(cellData -> cellData.getValue().placaProperty());
        colTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        colMarca.setCellValueFactory(cellData -> cellData.getValue().marcaProperty());
        colModelo.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
        
        // Cargar vehículos existentes
        cargarVehiculos();
        
        // Listener para selección en la tabla
        tablaVehiculos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    cargarDatosVehiculo(newSelection);
                }
            }
        );
    }
    
    /**
     * Carga todos los vehículos en la tabla
     */
    private void cargarVehiculos() {
        listaVehiculosRow.clear();
        for (Vehiculo v : repoVehiculo.listarTodos()) {
            VehiculoRow row = new VehiculoRow(
                v.getPlaca(),
                v.getTipo(),
                v.getMarca(),
                v.getModelo()
            );
            listaVehiculosRow.add(row);
        }
        tablaVehiculos.setItems(listaVehiculosRow);
    }
    
    /**
     * Carga los datos de un vehículo seleccionado en los campos
     */
    private void cargarDatosVehiculo(VehiculoRow vehiculoRow) {
        txtPlaca.setText(vehiculoRow.placaProperty().get());
        cmbTipo.setValue(vehiculoRow.tipoProperty().get());
        txtMarca.setText(vehiculoRow.marcaProperty().get());
        txtModelo.setText(vehiculoRow.modeloProperty().get());
    }
    
    /**
     * Agrega un nuevo vehículo
     */
    @FXML
    private void agregarVehiculo() {
        try {
            String placa = txtPlaca.getText().trim().toUpperCase();
            String tipo = cmbTipo.getValue();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            
            if (placa.isEmpty() || tipo == null) {
                mostrarAlerta("Error", "La placa y el tipo son obligatorios", Alert.AlertType.ERROR);
                return;
            }
            
            // Verificar si ya existe un vehículo con esa placa
            try {
                repoVehiculo.buscar(placa);
                mostrarAlerta("Error", "Ya existe un vehículo con la placa " + placa, Alert.AlertType.ERROR);
                return;
            } catch (Exception e) {
                // No existe, se puede crear
            }
            
            // Crear nuevo vehículo según el tipo
            Vehiculo nuevoVehiculo;
            if (tipo.equals("AUTO")) {
                nuevoVehiculo = new Auto(
                    placa,
                    marca.isEmpty() ? "SIN MARCA" : marca,
                    modelo.isEmpty() ? "SIN MODELO" : modelo
                );
            } else {
                nuevoVehiculo = new Moto(
                    placa,
                    marca.isEmpty() ? "SIN MARCA" : marca,
                    modelo.isEmpty() ? "SIN MODELO" : modelo
                );
            }
            
            // Guardar vehículo
            repoVehiculo.guardar(nuevoVehiculo);
            
            // Actualizar tabla
            cargarVehiculos();
            
            // Limpiar campos
            limpiarCampos();
            
            mostrarAlerta("Éxito", 
                "Vehículo con placa " + placa + " registrado exitosamente", 
                Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar vehículo: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Elimina el vehículo seleccionado
     */
    @FXML
    private void eliminarVehiculo() {
        VehiculoRow vehiculoRowSeleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        
        if (vehiculoRowSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un vehículo para eliminar", Alert.AlertType.ERROR);
            return;
        }
        
        String placaSeleccionada = vehiculoRowSeleccionado.placaProperty().get();
        
        // Confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de eliminar el vehículo con placa " + 
            placaSeleccionada + "?");
        
        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            try {
                // Eliminar del repositorio
                repoVehiculo.eliminar(placaSeleccionada);
                
                // Actualizar tabla
                cargarVehiculos();
                
                // Limpiar campos
                limpiarCampos();
                
                mostrarAlerta("Éxito", "Vehículo eliminado exitosamente", Alert.AlertType.INFORMATION);
                
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al eliminar vehículo: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    /**
     * Actualiza los datos del vehículo seleccionado
     */
    @FXML
    private void actualizarVehiculo() {
        VehiculoRow vehiculoRowSeleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        
        if (vehiculoRowSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un vehículo para actualizar", Alert.AlertType.ERROR);
            return;
        }
        
        try {
            String placa = txtPlaca.getText().trim().toUpperCase();
            String tipo = cmbTipo.getValue();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            
            if (placa.isEmpty() || tipo == null) {
                mostrarAlerta("Error", "La placa y el tipo son obligatorios", Alert.AlertType.ERROR);
                return;
            }
            
            // Crear vehículo actualizado
            Vehiculo vehiculoActualizado;
            if (tipo.equals("AUTO")) {
                vehiculoActualizado = new Auto(
                    placa,
                    marca.isEmpty() ? "SIN MARCA" : marca,
                    modelo.isEmpty() ? "SIN MODELO" : modelo
                );
            } else {
                vehiculoActualizado = new Moto(
                    placa,
                    marca.isEmpty() ? "SIN MARCA" : marca,
                    modelo.isEmpty() ? "SIN MODELO" : modelo
                );
            }
            
            // Actualizar en el repositorio
            String placaOriginal = vehiculoRowSeleccionado.placaProperty().get();
            repoVehiculo.actualizar(vehiculoActualizado, placaOriginal);
            
            // Actualizar tabla
            cargarVehiculos();
            
            // Limpiar campos
            limpiarCampos();
            
            mostrarAlerta("Éxito", "Vehículo actualizado exitosamente", Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar vehículo: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Limpia todos los campos del formulario
     */
    private void limpiarCampos() {
        txtPlaca.clear();
        txtMarca.clear();
        txtModelo.clear();
        cmbTipo.setValue("AUTO");
        tablaVehiculos.getSelectionModel().clearSelection();
    }
    
    /**
     * Muestra una alerta
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}