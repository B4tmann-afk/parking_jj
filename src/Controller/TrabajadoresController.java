package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class TrabajadoresController {

    @FXML private TableView tablaTrabajadores;

    // Lista temporal mientras no conectes repo o servicio
    private ObservableList<String> listaTrabajadores = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Cargar datos iniciales de ejemplo
        listaTrabajadores.addAll("Juan Pérez", "María López", "Carlos Ruiz");

        tablaTrabajadores.setItems(listaTrabajadores);
    }

    // =========================================================
    // AGREGAR
    // =========================================================
    @FXML
    private void agregarTrabajador() {
        listaTrabajadores.add("Nuevo Trabajador");
        mostrar("Agregar", "Se agregó un trabajador.");
    }

    // =========================================================
    // ELIMINAR
    // =========================================================
    @FXML
    private void eliminarTrabajador() {
        Object seleccionado = tablaTrabajadores.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrar("Error", "Debes seleccionar un trabajador para eliminarlo.");
            return;
        }

        listaTrabajadores.remove(seleccionado);
        mostrar("Eliminar", "Trabajador eliminado correctamente.");
    }

    // =========================================================
    // ACTUALIZAR (recargar los datos)
    // =========================================================
    @FXML
    private void actualizarTabla() {
        tablaTrabajadores.refresh();
        mostrar("Actualizar", "Tabla actualizada.");
    }

    // =========================================================
    // ALERTAS
    // =========================================================
    private void mostrar(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
    }
}
