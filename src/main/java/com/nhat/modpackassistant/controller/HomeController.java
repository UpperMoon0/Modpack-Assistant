package com.nhat.modpackassistant.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeController extends BaseController {
    @FXML
    private Button loadButton;

    @FXML
    private Button createButton;

    @FXML
    public void initialize() {
        createButton.setOnAction(e -> loadView("/com/nhat/modpackassistant/create-prj-view.fxml", createButton));
        loadButton.setOnAction(e -> loadView("/com/nhat/modpackassistant/load-prj-view.fxml", loadButton));
    }
}