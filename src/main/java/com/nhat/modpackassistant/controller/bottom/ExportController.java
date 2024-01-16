package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.controller.BaseController;
import com.nhat.modpackassistant.model.*;
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

    private static final String[] RARITIES = {"COMMON", "UNCOMMON", "RARE", "EPIC"};
    private static final double[] RARITY_PERCENTAGES = {0, 0.25, 0.5, 0.75, 1.0};
    private static final String[] CURRENCIES = {"kubejs:copper_coin", "kubejs:iron_coin", "kubejs:gold_coin", "kubejs:diamond_coin", "kubejs:emerald_coin"};
    private static final int[] UNIT_WORTHS = {1, 10, 100, 1000, 10000};

    @FXML
    public void initialize() {
        exportButton.setOnAction(event -> {
            String exportPathText = exportPath.getText();
            Path exportPath = Paths.get(exportPathText);
            if (Files.exists(exportPath)) {
                String prjPath = Project.getInstance().getPath();
                createDirectoriesAndFiles(exportPath, prjPath);
            }
        });
    }

    private void createDirectoriesAndFiles(Path exportPath, String prjPath) {
        // Create gen_tooltips.js file
        Path dirPath = Paths.get(exportPath.toString(), "kubejs", "client_scripts");
        createFileInDirectory(dirPath, "gen_item_tooltips.js", generateTooltipScript());

        // Create gen_item_stages.zs file
        Path scriptDirPath = Paths.get(exportPath.toString(), "scripts");
        createFileInDirectory(scriptDirPath, "gen_item_stages.zs", generateItemStagesScript());

        // Create temp directory and files
        Path tempDirPath = Paths.get(prjPath, "temp");
        createDirectory(tempDirPath);
        createFileInDirectory(tempDirPath, "pack.mcmeta", "{\n    \"pack\": {\n        \"pack_format\": 15,\n        \"description\": \"Bountiful Harvest datapack for custom bounties.\"\n    }\n}");

        // Create data directory and files
        Path dataDirPath = Paths.get(tempDirPath.toString(), "data");
        createDirectory(dataDirPath);
        createDirectory(Paths.get(dataDirPath.toString(), "bountiful", "bounty_decrees", "bountiful"));

        // Create bounty_pools directory
        Path bountyPoolPath = Paths.get(dataDirPath.toString(), "bountiful", "bounty_pools", "bountiful");
        createDirectory(bountyPoolPath);

        // Create bounty_decrees files
        Path bountyDecreesDirPath = Paths.get(prjPath, "temp", "data", "bountiful", "bounty_decrees", "bountiful");
        int maxBountyLevel = Bounties.getInstance().getBounties().size();
        for (int i = 1; i <= maxBountyLevel; i++) {
            String content = generateLevelContent(i);
            createFileInDirectory(bountyDecreesDirPath, "level_" + i + ".json", content);
        }

        // Create objs and rews files
        for (int i = 1; i <= maxBountyLevel; i++) {
            createFileInDirectory(bountyPoolPath, "level_" + i + "_objs.json", generateObjsContent(i));
            createFileInDirectory(bountyPoolPath, "level_" + i + "_rews.json", generateRewsContent(i));
        }
    }

    private void createDirectory(Path dirPath) {
        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        } catch (IOException ex) {
            System.err.println("Error creating directory: " + ex.getMessage());
        }
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

    private String generateTooltipScript() {
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

    private String generateItemStagesScript() {
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

    private String generateLevelContent(int level) {
        StringBuilder objectives = new StringBuilder();
        for (int i = level; i >= Math.max(1, level - 2); i--) {
            objectives.append("\t\t\"level_").append(i).append("_objs\",\n");
        }
        return "{\n" +
                "\t\"objectives\": [\n" +
                objectives +
                "\t],\n" +
                "\t\"rewards\": [\n" +
                "\t\t\"level_" + level + "_rews\"\n" +
                "\t],\n" +
                "\t\"name\": \"Level " + level + "\"\n" +
                "}";
    }

    private String generateObjsContent(int level) {
        StringBuilder content = new StringBuilder();
        ObservableList<Item> items = Items.getInstance().getItems();
        int orderNum = 1;
        Bounty bounty = Bounties.getInstance().getBountyByLevel(level);

        for (Item item : items) {
            if (item.getBountyLevels().contains(level)) {
                String itemId = item.getId();
                int itemValue = item.getValue();
                int minAmount = (int) Math.ceil((double) bounty.getMinValue() / itemValue);
                int maxAmount = (int) Math.ceil((double) bounty.getMaxValue() / itemValue);

                if (itemId.startsWith("#")) {
                    // Handle tag item
                    content.append(String.format(
                            """
                                    \t"obj_%d": {
                                    \t\t"type": "item_tag",
                                    \t\t"content": "%s",
                                    \t\t"amount": {
                                    \t\t\t"min": %d,
                                    \t\t\t"max": %d
                                    \t\t},
                                    \t\t"unitWorth": %d,
                                    \t\t"name": "Any %s"
                                    \t},
                                    """,
                            orderNum, itemId.substring(1), minAmount, maxAmount, itemValue, itemId.substring(1)
                    ));
                } else {
                    // Handle regular item
                    content.append(String.format(
                            """
                                    \t"obj_%d": {
                                    \t\t"type": "item",
                                    \t\t"content": "%s",
                                    \t\t"amount": {
                                    \t\t\t"min": %d,
                                    \t\t\t"max": %d
                                    \t\t},
                                    \t\t"unitWorth": %d
                                    \t},
                                    """,
                            orderNum, itemId, minAmount, maxAmount, itemValue
                    ));
                }
                orderNum++;
            }
        }

        return "{\n" + content + "\n}";
    }

    private String generateRewsContent(int level) {
        StringBuilder content = new StringBuilder();
        Bounty bounty = Bounties.getInstance().getBountyByLevel(level);

        for (int i = 0; i < RARITIES.length; i++) {
            int minAmount = (int) Math.ceil(bounty.getMinValue() + (bounty.getMaxValue() - bounty.getMinValue()) * RARITY_PERCENTAGES[i]);
            int maxAmount = (int) Math.ceil(bounty.getMinValue() + (bounty.getMaxValue() - bounty.getMinValue()) * RARITY_PERCENTAGES[i + 1]);

            for (int j = CURRENCIES.length - 1; j >= 0; j--) {
                if (minAmount >= UNIT_WORTHS[j] || maxAmount >= UNIT_WORTHS[j]) {
                    int minCoins = minAmount / UNIT_WORTHS[j];
                    int maxCoins = maxAmount / UNIT_WORTHS[j];

                    // Only use higher coin tier if value is greater than 64 of current coin
                    if (minCoins > 64) {
                        continue;
                    }

                    content.append(String.format(
                            """
                                    \t"rew_%d": {
                                    \t\t"type": "item",
                                    \t\t"rarity": "%s",
                                    \t\t"content": "%s",
                                    \t\t"amount": {
                                    \t\t\t"min": %d,
                                    \t\t\t"max": %d
                                    \t\t},
                                    \t\t"unitWorth": %d
                                    \t},
                                    """,
                            i + 1, RARITIES[i], CURRENCIES[j], minCoins, maxCoins, UNIT_WORTHS[j]
                    ));
                    break; // break out of the inner loop once a coin is used for a reward
                }
            }
        }

        return "{\n" + content + "\n}";
    }
}