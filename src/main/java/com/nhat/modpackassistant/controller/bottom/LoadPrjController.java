package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.model.Bounties;
import com.nhat.modpackassistant.model.Items;
import com.nhat.modpackassistant.model.MaxLevel;
import com.nhat.modpackassistant.model.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                    // Load the bottom view and top view
                    loadTopView("/com/nhat/modpackassistant/top-bar-bh-view.fxml", loadButton);
                    loadBottomView("/com/nhat/modpackassistant/item-view.fxml", loadButton);

                    // Set the project path
                    Project.getInstance().setPath(projectPath);

                    // Load items from JSON files
                    try {
                        Items.loadItems();
                    } catch (IOException ex) {
                        showErrorAlert("Error loading items", "Could not load items from JSON files.");
                    }

                    // Load levels from JSON file
                    try {
                        MaxLevel.loadLevels();
                    } catch (IOException ex) {
                        showErrorAlert("Error loading levels", "Could not load levels from JSON file.");
                    }

                    // Load bounties from JSON file
                    try {
                        Bounties.loadBounties();
                    } catch (IOException ex) {
                        showErrorAlert("Error loading bounties", "Could not load bounties from JSON file.");
                        ex.printStackTrace();
                    }

                    break;
            }
        });
    }

    private int canLoad() {
        String projectPath = projectPathField.getText();
        if (projectPath.isEmpty()) {
            return 0;
        } else if (!Files.exists(Paths.get(projectPath))) {
            return 1;
        } else {
            return 2;
        }
    }
}