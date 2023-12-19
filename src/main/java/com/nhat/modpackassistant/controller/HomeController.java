package com.nhat.modpackassistant.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    @FXML
    private Button createButton;

    @FXML
    public void initialize() {
        createButton.setOnAction(e -> {
            try {
                // Load the "Create Project" view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nhat/modpackassistant/create-prj-view.fxml"));
                Parent root = loader.load();

                // Get the current stage
                Stage stage = (Stage) createButton.getScene().getWindow();

                // Get the current scene's width and height
                double currentWidth = stage.getScene().getWidth();
                double currentHeight = stage.getScene().getHeight();

                // Set the new scene to the stage with the same size as the current scene
                stage.setScene(new Scene(root, currentWidth, currentHeight));

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}