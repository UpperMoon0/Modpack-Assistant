package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.controller.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public abstract class InitPrjController extends BaseController {
    @FXML
    protected Button backButton;

    @FXML
    public void initialize() {
        backButton.setOnAction(e -> loadBottomView("/com/nhat/modpackassistant/home-view.fxml", backButton));
    }
}