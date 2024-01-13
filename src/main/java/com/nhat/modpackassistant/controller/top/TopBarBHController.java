package com.nhat.modpackassistant.controller.top;

import com.nhat.modpackassistant.controller.BaseController;
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
    private Button saveButton;

    @FXML
    public void initialize() {
        topBar.setPrefHeight(60);

        itemsButton.setOnAction(e -> loadBottomView("/com/nhat/modpackassistant/item-view.fxml", itemsButton));
        levelsButton.setOnAction(e -> loadBottomView("/com/nhat/modpackassistant/level-view.fxml", levelsButton));
        saveButton.setOnAction(e -> save());
    }

    private void save() {
        try {
            com.nhat.modpackassistant.util.ItemUtil.getInstance().saveItems();
        } catch (IOException ex) {
            showErrorAlert("Error saving items", "Could not save items to JSON files.");
        }

        try {
            com.nhat.modpackassistant.util.LevelUtil.getInstance().saveLevel();
        } catch (IOException ex) {
            showErrorAlert("Error saving items", "Could not save level to JSON files.");
        }
    }
}