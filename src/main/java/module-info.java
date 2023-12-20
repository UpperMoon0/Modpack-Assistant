module com.nhat.modpackassistant {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens com.nhat.modpackassistant.controller to javafx.fxml;
    opens com.nhat.modpackassistant.model to javafx.base, com.fasterxml.jackson.databind;
    exports com.nhat.modpackassistant;
}