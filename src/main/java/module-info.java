module com.nhat.modpackassistant {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.nhat.modpackassistant.controller to javafx.fxml;
    exports com.nhat.modpackassistant;
}