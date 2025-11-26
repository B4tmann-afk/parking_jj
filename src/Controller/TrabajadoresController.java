package Controller;

import Modelos.Usuario;
import Modelos.Rol;
import Persistencia.RepositorioUsuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TrabajadoresController {
    
    // Campos del formulario
    @FXML private TextField txtUsuario;
    @FXML private TextField txtNombre;
    @FXML private PasswordField txtPassword;
    
    // Tabla y columnas
    @FXML private TableView<Usuario> tablaTrabajadores;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colRol;
    
    // Lista observable para la tabla
    private ObservableList<Usuario> listaTrabajadores = FXCollections.observableArrayList();
    
    @FXML
    private void initialize() {
        // Configurar las columnas de la tabla
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        
        // Cargar trabajadores existentes
        actualizarTabla();
    }
    
    /**
     * Agrega un nuevo trabajador al sistema
     */
    @FXML
    private void agregarTrabajador() {
        try {
            // Validar campos
            String usuario = txtUsuario.getText().trim();
            String nombre = txtNombre.getText().trim();
            String password = txtPassword.getText();
            
            if (usuario.isEmpty() || nombre.isEmpty() || password.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios", AlertType.ERROR);
                return;
            }
            
            // Verificar si el usuario ya existe
            try {
                Usuario existente = RepositorioUsuario.buscar(usuario);
                if (existente != null) {
                    mostrarAlerta("Error", "El usuario '" + usuario + "' ya existe", AlertType.ERROR);
                    return;
                }
            } catch (Exception e) {
                // Usuario no existe, podemos continuar
            }
            
            // Crear nuevo trabajador con rol TRABAJADOR
            // Generar un ID único basado en timestamp
            String id = "T" + System.currentTimeMillis();
            
            // Crear usuario con todos los parámetros requeridos
            Usuario nuevoTrabajador = new Usuario(
                id,              // id
                nombre,          // nombre
                "",              // cedula (vacío por ahora)
                "",              // telefono (vacío por ahora)
                usuario,         // usuario
                password,        // contraseña
                Rol.TRABAJADOR   // rol
            );
            
            // Guardar en el repositorio (usa guardar, no agregar)
            RepositorioUsuario.guardar(nuevoTrabajador);
            
            // Limpiar campos
            txtUsuario.clear();
            txtNombre.clear();
            txtPassword.clear();
            
            // Actualizar tabla
            actualizarTabla();
            
            mostrarAlerta("Éxito", 
                "Trabajador agregado correctamente:\n" +
                "Usuario: " + usuario + "\n" +
                "Nombre: " + nombre, 
                AlertType.INFORMATION);
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar trabajador: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Elimina el trabajador seleccionado
     * NOTA: Como RepositorioUsuario no tiene método eliminar,
     * solo mostramos un mensaje. Puedes implementarlo después.
     */
    @FXML
    private void eliminarTrabajador() {
        Usuario seleccionado = tablaTrabajadores.getSelectionModel().getSelectedItem();
        
        if (seleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un trabajador para eliminar", AlertType.ERROR);
            return;
        }
        
        // No permitir eliminar al administrador
        if (seleccionado.getRol() == Rol.ADMIN) {
            mostrarAlerta("Error", "No se puede eliminar un administrador", AlertType.ERROR);
            return;
        }
        
        // TODO: Implementar método eliminar en RepositorioUsuario
        mostrarAlerta("Información", 
            "La funcionalidad de eliminar aún no está implementada.\n" +
            "Necesitas agregar un método eliminar() en RepositorioUsuario.", 
            AlertType.INFORMATION);
    }
    
    /**
     * Actualiza la tabla con los trabajadores actuales
     */
    @FXML
    private void actualizarTabla() {
        try {
            // Limpiar la lista
            listaTrabajadores.clear();
            
            // Obtener todos los usuarios del repositorio
            for (Usuario usuario : RepositorioUsuario.listarTodos()) {
                // Solo mostrar trabajadores y admins (no clientes)
                if (usuario.getRol() == Rol.TRABAJADOR || usuario.getRol() == Rol.ADMIN) {
                    listaTrabajadores.add(usuario);
                }
            }
            
            // Actualizar la tabla
            tablaTrabajadores.setItems(listaTrabajadores);
            tablaTrabajadores.refresh();
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar trabajadores: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Cierra la ventana
     */
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablaTrabajadores.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Muestra una alerta al usuario
     */
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}