module es.sergiomateos.agendaconciertos {
    requires javafx.controls;
    requires javafx.fxml;
    
    requires java.instrument;
    requires java.persistence;
    requires java.sql;
    requires java.base;
    opens es.sergiomateos.agendaconciertos.entities;
    opens es.sergiomateos.agendaconciertos to javafx.fxml;
    exports es.sergiomateos.agendaconciertos;
}
