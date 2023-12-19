package com.nhat.modpackassistant.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class BaseController {
    protected void loadView(String viewPath, Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
            Stage stage = (Stage) button.getScene().getWindow();
            double currentWidth = stage.getScene().getWidth();
            double currentHeight = stage.getScene().getHeight();
            stage.setScene(new Scene(loader.load(), currentWidth, currentHeight));
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
}