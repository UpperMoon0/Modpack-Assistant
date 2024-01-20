package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.controller.BaseController;
import com.nhat.modpackassistant.model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    /**
     * Creates all the necessary directories and files for exporting.
     *
     * @param exportPath the export path
     * @param prjPath the project path
     */
    private void createDirectoriesAndFiles(Path exportPath, String prjPath) {
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
            String content = generateDecreeContent(i);
            createFileInDirectory(bountyDecreesDirPath, "level_" + i + ".json", content);
        }

        // Create objs and rews files
        for (int i = 1; i <= maxBountyLevel; i++) {
            createFileInDirectory(bountyPoolPath, "level_" + i + "_objs.json", generateObjsContent(i));
            createFileInDirectory(bountyPoolPath, "level_" + i + "_rews.json", generateRewsContent(i));
        }

        // Create zip file
        Path resourcePackPath = Paths.get(exportPath.toString(), "resourcepacks");
        createDirectory(resourcePackPath);
        Path zipFilePath = Paths.get(resourcePackPath.toString(), "gen_bh_bounties.zip");
        try {
            zipDirectory(tempDirPath, zipFilePath);
            clearDirectory(tempDirPath);
        } catch (IOException ex) {
            System.err.println("Error creating zip file or clearing directory: " + ex.getMessage());
        }
    }

    /**
     * Creates a directory at the specified path if it does not exist.
     *
     * @param dirPath the directory path
     */
    private void createDirectory(Path dirPath) {
        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        } catch (IOException ex) {
            System.err.println("Error creating directory: " + ex.getMessage());
        }
    }

    /**
     * Creates a file at the specified path if it does not exist.
     *
     * @param dirPath the directory path
     * @param fileName the file name
     * @param content the file content
     */
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
            String stageName = "level_" + currentLevel;

            if (!item.getResearchLevel().isEmpty()) {
                stageName += "_" + item.getResearchLevel();
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

    private String generateDecreeContent(int level) {
        StringBuilder objectives = new StringBuilder();
        int startLevel = Math.max(1, level - 2);
        for (int i = level; i >= startLevel; i--) {
            objectives.append("\t\t\"level_").append(i).append("_objs\"");
            if (i != startLevel) {
                objectives.append(",\n");
            }
        }
        return """
            {
                \t"objectives": [
                %s
                \t],
                \t"rewards": [
                \t\t"level_%d_rews"
                \t],
                \t"name": "Level %d"
            }
            """.formatted(objectives, level, level);
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
                                    \t\t"obj_%d": {
                                    \t\t\t"type": "item_tag",
                                    \t\t\t"content": "%s",
                                    \t\t\t"amount": {
                                    \t\t\t\t"min": %d,
                                    \t\t\t\t"max": %d
                                    \t\t\t},
                                    \t\t\t"unitWorth": %d,
                                    \t\t\t"name": "Any %s"
                                    \t\t},
                                    """,
                            orderNum, itemId.substring(1), minAmount, maxAmount, itemValue, itemId.substring(1)
                    ));
                } else {
                    // Handle regular item
                    content.append(String.format(
                            """
                                    \t\t"obj_%d": {
                                    \t\t\t"type": "item",
                                    \t\t\t"content": "%s",
                                    \t\t\t"amount": {
                                    \t\t\t\t"min": %d,
                                    \t\t\t\t"max": %d
                                    \t\t\t},
                                    \t\t\t"unitWorth": %d
                                    \t\t},
                                    """,
                            orderNum, itemId, minAmount, maxAmount, itemValue
                    ));
                }
                orderNum++;
            }
        }

        // Remove the trailing comma from the last object
        int lastCommaIndex = content.lastIndexOf(",");
        if (lastCommaIndex != -1) {
            content.deleteCharAt(lastCommaIndex);
        }

        return "{\n\t\"content\": {\n" + content + "\n\t}\n}";
    }

    private String generateRewsContent(int level) {
    List<String> rewards = new ArrayList<>();
    Bounty bounty = Bounties.getInstance().getBountyByLevel(level);

    for (int i = 0; i < RARITIES.length; i++) {
        int minAmount = (int) Math.ceil(bounty.getMinValue() + (bounty.getMaxValue() - bounty.getMinValue()) * RARITY_PERCENTAGES[i]);
        int maxAmount = (int) Math.ceil(bounty.getMinValue() + (bounty.getMaxValue() - bounty.getMinValue()) * RARITY_PERCENTAGES[i + 1]);

        for (int j = 0; j < CURRENCIES.length; j++) {
            if (minAmount >= UNIT_WORTHS[j] || maxAmount >= UNIT_WORTHS[j]) {
                int minCoins = minAmount / UNIT_WORTHS[j];
                int maxCoins = maxAmount / UNIT_WORTHS[j];

                // Only use higher coin tier if value is greater than 64 of current coin
                if (minCoins > 64) {
                    continue;
                }

                String reward = String.format(
                        """
                                \t\t"rew_%d": {
                                \t\t\t"type": "item",
                                \t\t\t"rarity": "%s",
                                \t\t\t"content": "%s",
                                \t\t\t"amount": {
                                \t\t\t\t"min": %d,
                                \t\t\t\t"max": %d
                                \t\t\t},
                                \t\t\t"unitWorth": %d
                                \t\t}""",
                        i + 1, RARITIES[i], CURRENCIES[j], minCoins, maxCoins, UNIT_WORTHS[j]
                );
                rewards.add(reward);
                break; // break out of the inner loop once a coin is used for a reward
            }
        }
    }

    String content = String.join(",\n", rewards);
    return "{\n\t\"content\": {\n" + content + "\n\t}\n}";
}

    private void zipDirectory(Path sourceDirPath, Path zipFilePath) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath.toFile()));
             Stream<Path> paths = Files.walk(sourceDirPath)) {
                 paths.filter(path -> !Files.isDirectory(path)).forEach(path -> {
                     ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                     try {
                         zipOutputStream.putNextEntry(zipEntry);
                         Files.copy(path, zipOutputStream);
                         zipOutputStream.closeEntry();
                     } catch (IOException ex) {
                         System.err.println("Error adding entry to zip file: " + ex.getMessage());
                     }
             });
        }
    }

    private void clearDirectory(Path dirPath) throws IOException {
        try (Stream<Path> paths = Files.walk(dirPath)) {
            paths.sorted(Comparator.reverseOrder())
                 .forEach(path -> {
                     try {
                         if (!path.equals(dirPath)) { // Skip the directory itself
                             Files.deleteIfExists(path);
                         }
                     } catch (IOException ex) {
                         System.err.println("Error deleting file: " + ex.getMessage());
                     }
                 });
        }
    }
}