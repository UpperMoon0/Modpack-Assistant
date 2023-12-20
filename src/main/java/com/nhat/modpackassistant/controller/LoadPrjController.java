package com.nhat.modpackassistant.controller;

import com.nhat.modpackassistant.model.Project;
import com.nhat.modpackassistant.util.FileUtil;
import com.nhat.modpackassistant.util.ItemUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoadPrjController extends InitPrjController {
    @FXML
    private Label projectPathLabel;

    @FXML
    private TextField projectPathField;

    @FXML
    private Button loadButton;

    @FXML
    public void initialize() {
        super.initialize();

        loadButton.setOnAction(e -> {
            String projectPath = projectPathField.getText();

            switch (canLoad()) {
                case 0:
                    showErrorAlert("Error loading project", "Project path is empty.");
                    break;
                case 1:
                    showErrorAlert("Error loading project", "Project path does not exist.");
                    break;
                case 2:
                    // Load the "item-view.fxml" file
                    loadView("/com/nhat/modpackassistant/item-view.fxml", loadButton);

                    // Set the project path
                    Project.getInstance().setPath(projectPath);

                    // Load items from JSON files
                    try {
                        ItemUtil.getInstance().loadItems();
                    } catch (IOException ex) {
                        showErrorAlert("Error loading items", "Could not load items from JSON files.");
                    }
                    break;
            }
        });
    }

    private int canLoad() {
        String projectPath = projectPathField.getText();
        if (projectPath.isEmpty()) {
            return 0;
        } else if (!FileUtil.pathExists(projectPath)) {
            return 1;
        } else {
            return 2;
        }
    }
}