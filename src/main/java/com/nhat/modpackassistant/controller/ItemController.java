package com.nhat.modpackassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class ItemController {
    @FXML
    private ListView<String> itemList;

    @FXML
    private Button addButton;

    @FXML
    public void initialize() {
        addButton.setOnAction(e -> {
            // Add new item to the list
            itemList.getItems().add("New Item");
        });
    }
}