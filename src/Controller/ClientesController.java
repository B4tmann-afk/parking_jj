package Controller;

import Modelos.Cliente;
import Modelos.ParqueoException;
import Logica.ServicioCliente;
import Persistencia.RepositorioCliente;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientesController {
    
    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colCedula;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colUsuario;
    
    // Repositorio y servicio compartidos
    private static RepositorioCliente repoCliente = new RepositorioCliente();
    private static ServicioCliente servicioCliente = new ServicioCliente(repoCliente);
    
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        // Configurar columnas de la tabla
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        
        // Cargar clientes existentes
        cargarClientes();
        
        // Listener para selección en la tabla
        tablaClientes.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    cargarDatosCliente(newSelection);
                }
            }
        );
    }
    
    /**
     * Carga todos los clientes en la tabla
     */
    private void cargarClientes() {
        listaClientes.clear();
        listaClientes.addAll(servicioCliente.listarClientes());
        tablaClientes.setItems(listaClientes);
    }
    
    /**
     * Carga los datos de un cliente seleccionado en los campos
     */
    private void cargarDatosCliente(Cliente cliente) {
        txtCedula.setText(cliente.getCedula());
        txtNombre.setText(cliente.getNombre());
        txtTelefono.setText(cliente.getTelefono());
        txtUsuario.setText(cliente.getUsuario());
        txtContrasena.clear(); // No mostrar contraseña por seguridad
    }
    
    /**
     * Agrega un nuevo cliente
     */
    @FXML
    private void agregarCliente() {
        try {
            // Validar campos
            String cedula = txtCedula.getText().trim();
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String contrasena = txtContrasena.getText();
            
            if (cedula.isEmpty() || nombre.isEmpty() || usuario.isEmpty() || contrasena.isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
                return;
            }
            
            // Verificar si ya existe un cliente con esa cédula
            try {
                servicioCliente.buscarCliente(cedula);
                mostrarAlerta("Error", "Ya existe un cliente con la cédula " + cedula, Alert.AlertType.ERROR);
                return;
            } catch (ParqueoException e) {
                // No existe, se puede crear
            }
            
            // Crear nuevo cliente
            Cliente nuevoCliente = new Cliente(
                cedula,  // id
                nombre,
                cedula,
                telefono.isEmpty() ? "Sin teléfono" : telefono,
                usuario,
                contrasena
            );
            
            // Registrar cliente
            servicioCliente.registrarCliente(nuevoCliente);
            
            // Actualizar tabla
            cargarClientes();
            
            // Limpiar campos
            limpiarCampos();
            
            mostrarAlerta("Éxito", 
                "Cliente " + nombre + " registrado exitosamente", 
                Alert.AlertType.INFORMATION);
            
        } catch (ParqueoException e) {
            mostrarAlerta("Error", "Error al agregar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Elimina el cliente seleccionado
     */
    @FXML
    private void eliminarCliente() {
        Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        
        if (clienteSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un cliente para eliminar", Alert.AlertType.ERROR);
            return;
        }
        
        // Confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de eliminar al cliente " + 
            clienteSeleccionado.getNombre() + "?");
        
        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            try {
                // Eliminar de la lista
                repoCliente.eliminar(clienteSeleccionado.getId());
                
                // Actualizar tabla
                cargarClientes();
                
                // Limpiar campos
                limpiarCampos();
                
                mostrarAlerta("Éxito", "Cliente eliminado exitosamente", Alert.AlertType.INFORMATION);
                
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al eliminar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    /**
     * Actualiza los datos del cliente seleccionado
     */
    @FXML
    private void actualizarCliente() {
        Cliente clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        
        if (clienteSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un cliente para actualizar", Alert.AlertType.ERROR);
            return;
        }
        
        try {
            String cedula = txtCedula.getText().trim();
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String usuario = txtUsuario.getText().trim();
            String contrasena = txtContrasena.getText();
            
            if (cedula.isEmpty() || nombre.isEmpty() || usuario.isEmpty()) {
                mostrarAlerta("Error", "Cédula, nombre y usuario son obligatorios", Alert.AlertType.ERROR);
                return;
            }
            
            // Crear cliente actualizado
            Cliente clienteActualizado = new Cliente(
                clienteSeleccionado.getId(), // Mantener el mismo ID
                nombre,
                cedula,
                telefono.isEmpty() ? "Sin teléfono" : telefono,
                usuario,
                contrasena.isEmpty() ? clienteSeleccionado.getContraseña() : contrasena
            );
            
            // Actualizar en el repositorio
            repoCliente.actualizar(clienteActualizado);
            
            // Actualizar tabla
            cargarClientes();
            
            // Limpiar campos
            limpiarCampos();
            
            mostrarAlerta("Éxito", "Cliente actualizado exitosamente", Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al actualizar cliente: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Limpia todos los campos del formulario
     */
    private void limpiarCampos() {
        txtCedula.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtUsuario.clear();
        txtContrasena.clear();
        tablaClientes.getSelectionModel().clearSelection();
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
    
    // Métodos estáticos para acceder desde otros controladores
    public static RepositorioCliente getRepoCliente() {
        return repoCliente;
    }
    
    public static ServicioCliente getServicioCliente() {
        return servicioCliente;
    }
}