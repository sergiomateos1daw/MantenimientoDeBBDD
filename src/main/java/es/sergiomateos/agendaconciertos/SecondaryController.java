package es.sergiomateos.agendaconciertos;

import es.sergiomateos.agendaconciertos.entities.Artista;
import es.sergiomateos.agendaconciertos.entities.Ciudad;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javax.persistence.Query;
import javax.persistence.RollbackException;

public class SecondaryController {
    
    private Artista artista;
    
    private static final String CARPETA_FOTOS = "Fotos";
    
    private boolean nuevoArtista;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private TextField textFieldTelefono;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldSalario;
    @FXML
    private DatePicker datePickerFechaNacimiento;
    @FXML
    private ComboBox<Ciudad> comboBoxCiudad;
    @FXML
    private ImageView imageViewFoto;
    @FXML
    private BorderPane rootSecondary;

    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    
    public void setArtista(Artista artista, boolean nuevoArtista) {
        App.em.getTransaction().begin();
        if(!nuevoArtista){
            this.artista = App.em.find(Artista.class, artista.getId());
        } else{
            this.artista = artista;
        }
        this.nuevoArtista = nuevoArtista;
        mostrarDatos();
        
        
    }
    
    private void mostrarDatos(){
        System.out.println("Ejecutando método mostrarDatos()");
        textFieldNombre.setText(artista.getNombre());
        textFieldApellidos.setText(artista.getApellidos());
        textFieldTelefono.setText(artista.getTelefono());
        textFieldEmail.setText(artista.getEmail());
        if(artista.getSalario() != null){
            textFieldSalario.setText(String.valueOf(artista.getSalario()));
        }
        if(artista.getFechaNacimiento() != null){
            Date date = artista.getFechaNacimiento();
            Instant instant = date.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            LocalDate localDate = zdt.toLocalDate();
            datePickerFechaNacimiento.setValue(localDate);
        }
        
        
        
        ///////////////////////////////////////
        Query queryCiudadFindAll = App.em.createNamedQuery("Ciudad.findAll");
        List<Ciudad> listCiudad = queryCiudadFindAll.getResultList();
        
        comboBoxCiudad.setItems(FXCollections.observableList(listCiudad));
        if (artista.getCiudad()!= null){
            comboBoxCiudad.setValue(artista.getCiudad());
        }
        comboBoxCiudad.setCellFactory((ListView<Ciudad> l) -> new ListCell<Ciudad>() {
            @Override
            protected void updateItem(Ciudad ciudad, boolean empty) {
                super.updateItem(ciudad, empty);
                if (ciudad == null || empty){
                    setText("");
                } else {
                    setText(ciudad.getCodigo() + "-" + ciudad.getNombre());
                }
            }  
            
        });
    
        // 
        comboBoxCiudad.setConverter(new StringConverter<Ciudad>() {
                @Override
                public String toString(Ciudad ciudad) {
                if (ciudad == null){
                    return null;
                } else {
                    return ciudad.getCodigo() + "-" + ciudad.getNombre();
                }
                }
                
            @Override
            public Ciudad fromString(String userId){
                return null;
            }
        });
        ///////////////////////////////////////
        if(artista.getFoto()!= null){
            String imageFileName = artista.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if (file.exists()){
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No se encuentra la imágen");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void setOnActionButtonGuardar(ActionEvent event) {
        boolean errorFormato = false;
        
        artista.setNombre(textFieldNombre.getText());
        artista.setApellidos(textFieldApellidos.getText());
        artista.setTelefono(textFieldTelefono.getText());
        artista.setEmail(textFieldEmail.getText());
        
        if(!textFieldSalario.getText().isEmpty()){
            try{
                artista.setSalario(BigDecimal.valueOf(Double.valueOf(textFieldSalario.getText()).doubleValue()));
            }catch(NumberFormatException ex){
                errorFormato = true;
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Salario no válido");
                alert.showAndWait();
                textFieldSalario.requestFocus();
            }
        }
        
        if(datePickerFechaNacimiento.getValue() != null){
            LocalDate localDate = datePickerFechaNacimiento.getValue();
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
            Instant instant = zonedDateTime.toInstant();
            Date date = Date.from(instant);
            artista.setFechaNacimiento(date);
        }else{
            artista.setFechaNacimiento(null);
        }
        
        artista.setCiudad(comboBoxCiudad.getValue());
        
        if(!errorFormato){
            try{
                if(artista.getId() == null){
                    System.out.println("Guardando nuevo artista en BD");
                    App.em.persist(artista);
                }else{
                    System.out.println("Actualizando artista en BD");
                    App.em.merge(artista);
                }
                App.em.getTransaction().commit();
                App.setRoot("primary");
            }catch(RollbackException ex){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("No se han podido guardar los cambios."
                        + "Compruebe que los datos cumplen los requisitos");
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            }catch(IOException ex){
                Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    @FXML
    private void setOnActionButtonCancelar(ActionEvent event) throws IOException {
        App.em.getTransaction().rollback();
        try{
            App.setRoot("primary");
        }catch (IOException ex){
            Logger.getLogger(SecondaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    @FXML
    private void setOnActionButtonExaminar(ActionEvent event) throws IOException {
        File carpertaFotos = new File(CARPETA_FOTOS);
        if(!carpertaFotos.exists()){
            carpertaFotos.mkdir();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes (jpg, png)", "*.png"),
                 new FileChooser.ExtensionFilter("Todos los archivos", ".")
        );
        
        File file = fileChooser.showOpenDialog(rootSecondary.getScene().getWindow());
        if (file != null){
            try{
                Files.copy(file.toPath(), new File(CARPETA_FOTOS + "/" + file.getName()).toPath());
                artista.setFoto(file.getName());
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            } catch (FileAlreadyExistsException ex){
                Alert alert = new Alert(Alert.AlertType.WARNING,"Nombre de archivo duplicado");
                alert.showAndWait();
            }
        }
    }


    @FXML
    private void setOnActionButtonEliminar(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar supresion ed imagen");
        alert.setHeaderText("¿Desea SUPRIMIR el archivo asociado a la imagen, \n"
                + "quitar la foto pero MANTENER el archivo, \no CANCELAR la operación?");
        alert.setContentText("Elija a opción deseada");
        
        ButtonType buttonTypeEiminar = new ButtonType("Suprimir");
        ButtonType buttonTypeMantener = new ButtonType("Mantener");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(buttonTypeEiminar, buttonTypeMantener, buttonTypeCancel);
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == buttonTypeEiminar) {
            String imageFileName = artista.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if(file.exists()){
                file.delete();
            }
            artista.setFoto(null);
            imageViewFoto.setImage(null);
        } else if (result.get() == buttonTypeMantener){
            artista.setFoto(null);
            imageViewFoto.setImage(null);
        }
    }
}