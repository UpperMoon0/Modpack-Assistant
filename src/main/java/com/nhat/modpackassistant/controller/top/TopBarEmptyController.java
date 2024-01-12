package com.nhat.modpackassistant.controller.top;

import com.nhat.modpackassistant.controller.BaseController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class TopBarEmptyController extends BaseController {
    @FXML
    public AnchorPane topBar;

    @FXML
    public void initialize() {
        topBar.setPrefHeight(60);
    }
}