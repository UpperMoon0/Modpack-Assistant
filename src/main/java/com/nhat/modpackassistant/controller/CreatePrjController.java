package com.nhat.modpackassistant.controller;

import com.nhat.modpackassistant.util.FileUtil;
import com.nhat.modpackassistant.util.StringUtil;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class CreatePrjController {
    @FXML
    private Label projectNameLabel;

    @FXML
    private TextField projectNameField;

    @FXML
    private Label projectLocLabel;

    @FXML
    private TextField projectLocField;

    @FXML
    private Label fullPathLabel;

    @FXML
    private Label fullPath;

    @FXML
    private Button createButton;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        ChangeListener<String> pathUpdater = (observable, oldValue, newValue) -> {
            String formattedProjectName = StringUtil.formatProjectName(projectNameField.getText());
            String projectLocation = projectLocField.getText();
            String combinedPath = Paths.get(projectLocation, formattedProjectName).toString();
            fullPath.setText(combinedPath);
        };

        projectNameField.textProperty().addListener(pathUpdater);
        projectLocField.textProperty().addListener(pathUpdater);

        createButton.setOnAction(e -> {
            String projectLocation = projectLocField.getText();
            String formattedProjectName = StringUtil.formatProjectName(projectNameField.getText());

            if (FileUtil.pathExists(projectLocation)) {
                try {
                    // Create the project directory
                    FileUtil.createDir(projectLocation, formattedProjectName);

                    // Create the "bounties" directory inside the project directory
                    FileUtil.createDir(Paths.get(projectLocation, formattedProjectName).toString(), "bounties");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        backButton.setOnAction(e -> {
            try {
                // Load the "home-view.fxml" file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/nhat/modpackassistant/home-view.fxml"));
                Stage stage = (Stage) backButton.getScene().getWindow();
                double currentWidth = stage.getScene().getWidth();
                double currentHeight = stage.getScene().getHeight();
                Scene scene = new Scene(loader.load(), currentWidth, currentHeight);
                stage.setScene(scene);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}