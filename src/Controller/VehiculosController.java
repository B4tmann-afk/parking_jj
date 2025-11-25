package Controller;

import Modelos.VehiculoRow;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VehiculosController {

    @FXML private TextField txtPlaca;
    @FXML private TextField txtTipo;

    @FXML private TableView<VehiculoRow> tablaVehiculos;
    @FXML private TableColumn<VehiculoRow, String> colPlaca;
    @FXML private TableColumn<VehiculoRow, String> colTipo;

    private final ObservableList<VehiculoRow> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colPlaca.setCellValueFactory(c -> c.getValue().placaProperty());
        colTipo.setCellValueFactory(c -> c.getValue().tipoProperty());

        tablaVehiculos.setItems(lista);
    }

    @FXML
    private void agregarVehiculo() {
        if (txtPlaca.getText().isEmpty() || txtTipo.getText().isEmpty()) {
            mostrar("Error", "Debe ingresar la placa y el tipo.");
            return;
        }

        lista.add(new VehiculoRow(txtPlaca.getText(), txtTipo.getText()));
        mostrar("Éxito", "Vehículo agregado.");
    }

    @FXML
    private void eliminarVehiculo() {
        VehiculoRow seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrar("Error", "Debe seleccionar un vehículo.");
            return;
        }

        lista.remove(seleccionado);
        mostrar("Éxito", "Vehículo eliminado.");
    }

    @FXML
    private void actualizarVehiculo() {
        VehiculoRow sel = tablaVehiculos.getSelectionModel().getSelectedItem();

        if (sel == null) {
            mostrar("Error", "Debe seleccionar un vehículo.");
            return;
        }

        sel.setPlaca(txtPlaca.getText());
        sel.setTipo(txtTipo.getText());
        tablaVehiculos.refresh();

        mostrar("Éxito", "Vehículo actualizado.");
    }

    private void mostrar(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.show();
    }
}
