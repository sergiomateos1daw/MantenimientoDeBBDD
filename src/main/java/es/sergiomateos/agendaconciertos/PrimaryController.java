package es.sergiomateos.agendaconciertos;

import es.sergiomateos.agendaconciertos.entities.Artista;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.Query;

public class PrimaryController implements Initializable{
    
    
    private Artista artistaSeleccionado;
    @FXML
    private TableView<Artista> tableViewContactos;
    @FXML
    private TableColumn<Artista, String> columnNombre;
    @FXML
    private TableColumn<Artista, String> columnApellidos;
    @FXML
    private TableColumn<Artista, String> columnEmail;
    @FXML
    private TableColumn<Artista, String> columnCiudad;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private TextField textFieldBuscar;
    @FXML
    private Button buttonBuscar;
    @FXML
    private CheckBox checkBoxCoincide;
  

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnCiudad.setCellValueFactory(
                cellData -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    if (cellData.getValue().getCiudad() != null) {
                        String nombre = cellData.getValue().getCiudad().getNombre();
                        property.setValue(nombre);
                    }
                    return property;
                });
        
        
        tableViewContactos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    artistaSeleccionado = newValue;
                    if (artistaSeleccionado != null) {
                        textFieldNombre.setText(artistaSeleccionado.getNombre());
                        textFieldApellidos.setText(artistaSeleccionado.getApellidos()); 
                    } else {
                        textFieldNombre.setText("");
                        textFieldApellidos.setText("");
                    }
                });
        
        cargarTodosArtista();
    }
    
    
    
    private void cargarTodosArtista() {
        System.out.println("Se han cargado todos los artistas, cargarTodosArtista()");
        Query queryArtistaFindAll = App.em.createNamedQuery("Artista.findAll");
        List<Artista> listArtista = queryArtistaFindAll.getResultList();
        tableViewContactos.setItems(FXCollections.observableArrayList(listArtista));
    }

    @FXML
    private void onAcctionButtonGuardar(ActionEvent event) {
        if(artistaSeleccionado != null){
            artistaSeleccionado.setNombre(textFieldNombre.getText());
            artistaSeleccionado.setApellidos(textFieldApellidos.getText());
            App.em.getTransaction().begin();
            App.em.merge(artistaSeleccionado);
            App.em.getTransaction().commit();
            
            int numFilaSeleccionada = tableViewContactos.getSelectionModel().getSelectedIndex();
            tableViewContactos.getItems().set(numFilaSeleccionada, artistaSeleccionado);
            TablePosition pos = new TablePosition(tableViewContactos, numFilaSeleccionada, null);
            tableViewContactos.getFocusModel().focus(pos);
            tableViewContactos.requestFocus();
        }
    }

    @FXML
    private void onAcctionButtonSuprimir(ActionEvent event) {
        if(artistaSeleccionado != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar borrado");
            alert.setHeaderText("¿Desea eliminar el siguiente registro?");
            alert.setContentText(artistaSeleccionado.getNombre() + " " + artistaSeleccionado.getApellidos());
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                App.em.getTransaction().begin();
                App.em.remove(artistaSeleccionado);
                App.em.getTransaction().commit();
                tableViewContactos.getItems().remove(artistaSeleccionado);
                tableViewContactos.getFocusModel().focus(null);
                tableViewContactos.requestFocus();
            } else{
                int numFilaSeleccionada = tableViewContactos.getSelectionModel().getSelectedIndex();
                tableViewContactos.getItems().set(numFilaSeleccionada, artistaSeleccionado);
                TablePosition pos = new TablePosition(tableViewContactos, numFilaSeleccionada, null);
                tableViewContactos.getFocusModel().focus(pos);
                tableViewContactos.requestFocus();
            }
        } else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Atención");
                alert.setHeaderText("Debes seleccionar un registro");
                alert.showAndWait();
                }
    }

    @FXML
    private void onAcctionButtonNuevo(ActionEvent event) {
        try {
            App.setRoot("secondary");
            SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController();
            artistaSeleccionado = new Artista();
            secondaryController.setArtista(artistaSeleccionado, true);
        } catch (IOException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    @FXML
    private void onAcctionButtonEditar(ActionEvent event) {
        if(artistaSeleccionado != null) {
            try {
                App.setRoot("secondary");
                SecondaryController secondaryController = (SecondaryController)App.fxmlLoader.getController();
                secondaryController.setArtista(artistaSeleccionado, false);
                
            } catch (IOException ex) {
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Atencion");
            alert.setHeaderText("Debe seleccionar un registro");
            alert.showAndWait();
        }
    }
    
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void onAcctionButtonBuscar(ActionEvent event) {
         if (!textFieldBuscar.getText().isEmpty()){
            if(checkBoxCoincide.isSelected()){
                Query queryJugadorFindAll = App.em.createNamedQuery("Artista.findByNombre");
                queryJugadorFindAll.setParameter("nombre", textFieldBuscar.getText());
                List<Artista> listArtista = queryJugadorFindAll.getResultList();
                tableViewContactos.setItems(FXCollections.observableArrayList(listArtista));
            } else {
                String strQuery = "SELECT * FROM Artista WHERE LOWER(nombre) LIKE ";
                strQuery += "\'%" + textFieldBuscar.getText().toLowerCase() + "%\'";
                Query queryArtistaLikeNombre = App.em.createNativeQuery(strQuery, Artista.class);

                List<Artista> listArtista = queryArtistaLikeNombre.getResultList();
                tableViewContactos.setItems(FXCollections.observableArrayList(listArtista));

                Logger.getLogger(this.getClass().getName()).log(Level.INFO, strQuery);
            
            }
            
        } else {
            cargarTodosArtista();
        }
    }

    
}
