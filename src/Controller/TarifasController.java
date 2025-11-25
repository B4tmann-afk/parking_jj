package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class TarifasController {

    @FXML private TextField txtTarifaCarro;
    @FXML private TextField txtTarifaMoto;

    @FXML
    private void actualizarTarifas() {
        String carro = txtTarifaCarro.getText();
        String moto  = txtTarifaMoto.getText();

        if (carro.isEmpty() || moto.isEmpty()) {
            mostrar("Error", "Debe ingresar ambas tarifas.");
            return;
        }

        mostrar("Éxito", "Tarifas actualizadas correctamente:\n"
                + "Carro: " + carro + "\n"
                + "Moto: " + moto);
    }

    private void mostrar(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.show();
    }
}
