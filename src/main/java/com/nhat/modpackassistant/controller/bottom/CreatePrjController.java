package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.model.Project;
import com.nhat.modpackassistant.util.StringUtil;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreatePrjController extends InitPrjController {
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
    public void initialize() {
        super.initialize();

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

            switch (canCreate(projectLocation, formattedProjectName)) {
                case 0:
                    showErrorAlert("Error creating project", "Project name is empty.");
                    break;
                case 1:
                    showErrorAlert("Error creating project", "Project location is empty.");
                    break;
                case 2:
                    showErrorAlert("Error creating project", "Project location does not exist.");
                    break;
                case 3:
                    try {
                        // Create the project directory
                        Files.createDirectories(Paths.get(projectLocation, formattedProjectName));
                        // Create the subdirectories
                        String path = Paths.get(projectLocation, formattedProjectName).toString();
                        Files.createDirectories(Paths.get(path, "bounties"));
                        Files.createDirectories(Paths.get(path, "items"));
                        Files.createDirectories(Paths.get(path, "temp"));

                        // Load the bottom view and top view
                        loadTopView("/com/nhat/modpackassistant/top-bar-bh-view.fxml", createButton);
                        loadBottomView("/com/nhat/modpackassistant/item-view.fxml", createButton);

                        // Set the project path
                        Project.getInstance().setPath(path);
                    } catch (IOException ioException) {
                        showErrorAlert("Error creating project", "Could not create the project: " + formattedProjectName);
                    }
                    break;
            }
        });
    }

    private int canCreate(String projectLocation, String projectName) {
        if (projectName.isEmpty()) {
            return 0;
        } else if (projectLocation.isEmpty()) {
            return 1;
        } else if (!Files.exists(Paths.get(projectLocation))) {
            return 2;
        } else {
            return 3;
        }
    }
}