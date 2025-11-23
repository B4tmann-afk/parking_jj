package Presentacion;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class IngresoController {

    @FXML
    private TextField txtPlaca;

    @FXML
    private TextField txtCliente;

    @FXML
    private void registrarIngreso() {

        String placa = txtPlaca.getText();

        if (placa.isEmpty()) {
            mostrar("Error", "Debe ingresar una placa");
            return;
        }

        mostrar("Ingreso registrado", "Vehículo: " + placa);
    }

    private void mostrar(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.show();
    }
}
