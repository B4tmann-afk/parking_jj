package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class SalidaController {

    @FXML private TextField txtPlacaSalida;

    @FXML
    private void registrarSalida() {
        String placa = txtPlacaSalida.getText();

        if (placa.isEmpty()) {
            mostrar("Error", "Debe ingresar una placa");
            return;
        }

        mostrar("Salida registrada", "Salida del vehículo " + placa + " registrada correctamente.");
    }

    private void mostrar(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
