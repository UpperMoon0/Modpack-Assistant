package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.controller.BaseController;
import com.nhat.modpackassistant.model.MaxLevel;
import com.nhat.modpackassistant.util.LevelUtil;
import com.nhat.modpackassistant.util.StringUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LevelController extends BaseController {
    @FXML
    private TextField maxLevelField;

    @FXML
    public void initialize() {
        // Display the current max level in the maxLevelField text field.
        maxLevelField.setText(String.valueOf(MaxLevel.getInstance().getLevel()));

        // Add a listener to the textProperty of the maxLevelField to update the text fill color and the MaxLevel singleton.
        maxLevelField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (LevelUtil.getInstance().levelValid(newValue)) {
                maxLevelField.setStyle("-fx-text-fill: white;");
                MaxLevel.getInstance().setLevel(Integer.parseInt(newValue));
            } else {
                maxLevelField.setStyle("-fx-text-fill: red;");
            }
        });
    }
}