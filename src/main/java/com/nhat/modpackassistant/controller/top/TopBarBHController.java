package com.nhat.modpackassistant.controller.top;

import com.nhat.modpackassistant.controller.BaseController;
import com.nhat.modpackassistant.model.Bounties;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class TopBarBHController extends BaseController {
    @FXML
    public AnchorPane topBar;

    @FXML
    private Button itemsButton;

    @FXML
    private Button levelsButton;

    @FXML
    private Button bountiesButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button saveButton;

    @FXML
    public void initialize() {
        topBar.setPrefHeight(60);

        itemsButton.setOnAction(e -> loadBottomView("/com/nhat/modpackassistant/item-view.fxml", itemsButton));
        levelsButton.setOnAction(e -> loadBottomView("/com/nhat/modpackassistant/level-view.fxml", levelsButton));
        bountiesButton.setOnAction(e -> loadBottomView("/com/nhat/modpackassistant/bounty-view.fxml", bountiesButton));
        exportButton.setOnAction(e -> loadBottomView("/com/nhat/modpackassistant/export-view.fxml", exportButton));
        saveButton.setOnAction(e -> save());
    }

    private void save() {
        try {
            com.nhat.modpackassistant.model.Items.saveItems();
        } catch (IOException ex) {
            showErrorAlert("Error saving items", "Could not save items to JSON files.");
        }

        try {
            com.nhat.modpackassistant.model.MaxLevel.saveLevel();
        } catch (IOException ex) {
            showErrorAlert("Error saving items", "Could not save level to JSON files.");
        }

        try {
            Bounties.saveBounties();
        } catch (IOException ex) {
            showErrorAlert("Error saving items", "Could not save bounties to JSON files.");
        }
    }
}