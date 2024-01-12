package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.controller.BaseController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomeController extends BaseController {
    @FXML
    private Button loadButton;

    @FXML
    private Button createButton;

    @FXML
    public void initialize() {
        createButton.setOnAction(e -> loadBottomView("/com/nhat/modpackassistant/create-prj-view.fxml", createButton));
        loadButton.setOnAction(e -> loadBottomView("/com/nhat/modpackassistant/load-prj-view.fxml", loadButton));
    }
}