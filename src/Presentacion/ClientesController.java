package Presentacion;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientesController {

    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;

    @FXML private TableView<ClienteRow> tablaClientes;
    @FXML private TableColumn<ClienteRow, String> colCedula;
    @FXML private TableColumn<ClienteRow, String> colNombre;

    private final ObservableList<ClienteRow> lista = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colCedula.setCellValueFactory(c -> c.getValue().cedulaProperty());
        colNombre.setCellValueFactory(c -> c.getValue().nombreProperty());
        tablaClientes.setItems(lista);
    }

    @FXML
    private void agregarCliente() {
        if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty()) {
            mostrar("Error", "Debe ingresar cédula y nombre.");
            return;
        }

        lista.add(new ClienteRow(txtCedula.getText(), txtNombre.getText()));
        mostrar("Éxito", "Cliente agregado.");
    }

    @FXML
    private void eliminarCliente() {
        ClienteRow sel = tablaClientes.getSelectionModel().getSelectedItem();

        if (sel == null) {
            mostrar("Error", "Debe seleccionar un cliente.");
            return;
        }

        lista.remove(sel);
        mostrar("Éxito", "Cliente eliminado.");
    }

    @FXML
    private void actualizarCliente() {
        ClienteRow sel = tablaClientes.getSelectionModel().getSelectedItem();

        if (sel == null) {
            mostrar("Error", "Debe seleccionar un cliente.");
            return;
        }

        sel.setCedula(txtCedula.getText());
        sel.setNombre(txtNombre.getText());
        tablaClientes.refresh();

        mostrar("Éxito", "Cliente actualizado.");
    }

    private void mostrar(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.show();
    }
}
