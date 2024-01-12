package com.nhat.modpackassistant.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Function;

public abstract class BaseController {
    protected void loadBottomView(String viewPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
            AnchorPane newView = loader.load();

            Stage stage = (Stage) button.getScene().getWindow();
            BorderPane borderPane = (BorderPane) stage.getScene().getRoot();
            borderPane.setCenter(newView);
        } catch (IOException ioException) {
            showErrorAlert("Error loading view", "Could not load the view: " + viewPath);
        }
    }

    protected void loadTopView(String viewPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
            AnchorPane newView = loader.load();

            Stage stage = (Stage) button.getScene().getWindow();
            BorderPane borderPane = (BorderPane) stage.getScene().getRoot();
            borderPane.setTop(newView);
        } catch (IOException ioException) {
            showErrorAlert("Error loading view", "Could not load the view: " + viewPath);
        }
    }

    protected void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected void setTextFillBasedOnValidity(TextField textField, Function<String, Boolean> validityCheck) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (validityCheck.apply(newValue)) {
                textField.setStyle("-fx-text-fill: white;");
            } else {
                textField.setStyle("-fx-text-fill: red;");
            }
        });
    }
}