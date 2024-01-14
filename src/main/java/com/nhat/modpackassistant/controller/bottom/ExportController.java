package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.controller.BaseController;
import com.nhat.modpackassistant.model.Item;
import com.nhat.modpackassistant.model.Items;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class ExportController extends BaseController {

    @FXML
    private TextField exportPath;

    @FXML
    private Button exportButton;

    @FXML
    public void initialize() {
        exportButton.setOnAction(event -> {
            String path = exportPath.getText();
            if (Files.exists(Paths.get(path))) {
                // Create gen_tooltips.js file
                Path dirPath = Paths.get(path, "kubejs", "client_scripts");
                createFileInDirectory(dirPath, "gen_item_tooltips.js", generateTooltipScript());

                // Create gen_item_stages.zs file
                Path scriptDirPath = Paths.get(path, "scripts");
                createFileInDirectory(scriptDirPath, "gen_item_stages.zs", generateItemStagesScript());
            }
        });
    }

    private void createFileInDirectory(Path dirPath, String fileName, String content) {
        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            Path filePath = dirPath.resolve(fileName);
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            Files.write(filePath, content.getBytes());
        } catch (IOException ex) {
            System.err.println("Error creating directory or file: " + ex.getMessage());
        }
    }

    public static String generateTooltipScript() {
        StringBuilder content = new StringBuilder();
        ObservableList<Item> items = Items.getInstance().getItems();
        items.sort(Comparator.comparing(Item::getLevel));

        int currentLevel = 0;
        for (Item item : items) {
            if (item.getLevel() != currentLevel) {
                currentLevel = item.getLevel();
                content.append("\n    // Level ").append(currentLevel).append("\n");
            }
            String itemId = item.getId();
            content.append("    e.add('").append(itemId).append("', ['Value: ").append(item.getValue()).append("'])\n");
        }

        return "ItemEvents.tooltip((e) => {" + content + "})";
    }

    public static String generateItemStagesScript() {
        StringBuilder content = new StringBuilder();
        content.append("import mods.itemstages.ItemStages;\n");

        ObservableList<Item> items = Items.getInstance().getItems();
        items.sort(Comparator.comparing(Item::getLevel));

        int currentLevel = 0;
        for (Item item : items) {
            if (item.getLevel() != currentLevel) {
                currentLevel = item.getLevel();
                content.append("\n// Level ").append(currentLevel).append("\n");
            }

            String itemId = item.getId();
            String itemName = itemId.contains(":") ? itemId.split(":")[1] : itemId;
            String stageName = "level_" + currentLevel;

            if (currentLevel == 1 && item.getNeedResearch()) {
                stageName += "_" + itemName;
            }

            if (itemId.startsWith("#")) {
                itemId = itemId.replace("#", "tag:items:");
                content.append("ItemStages.restrict(<").append(itemId).append(">, \"").append(stageName).append("\");\n");
            } else {
                content.append("ItemStages.restrict(<item:").append(itemId).append(">, \"").append(stageName).append("\");\n");
            }
        }

        return content.toString();
    }
}