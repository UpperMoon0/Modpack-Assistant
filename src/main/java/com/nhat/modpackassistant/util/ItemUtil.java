package com.nhat.modpackassistant.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhat.modpackassistant.model.Item;
import com.nhat.modpackassistant.model.ItemList;
import com.nhat.modpackassistant.model.MaxLevel;
import com.nhat.modpackassistant.model.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Utility class for handling operations related to Items.
 */
public class ItemUtil {
    private static com.nhat.modpackassistant.util.ItemUtil instance;
    private final ObjectMapper mapper;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private ItemUtil() {
        this.mapper = new ObjectMapper();
    }

    /**
     * Returns the singleton instance of ItemUtil.
     * @return the singleton instance of ItemUtil.
     */
    public static ItemUtil getInstance() {
        if (instance == null) {
            instance = new ItemUtil();
        }
        return instance;
    }

    /**
     * Saves all items to disk.
     * @throws IOException if an I/O error occurs.
     */
    public void saveItems() throws IOException {
        String projectPath = Project.getInstance().getPath();
        Path itemsDir = Paths.get(projectPath, "items");

        if (!FileUtil.pathExists(itemsDir.toString())) {
            FileUtil.createDir(projectPath, "items");
        } else {
            FileUtil.deleteAllFilesInDir(itemsDir.toString());
        }

        for (Item item : ItemList.getInstance().getItems()) {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item);
            String itemId = item.getId();
            Path itemFile = itemsDir.resolve(itemId + ".json");
            FileUtil.writeToFile(itemFile.toString(), json.getBytes());
        }
    }

    /**
     * Loads all items from disk.
     * @throws IOException if an I/O error occurs.
     */
    public void loadItems() throws IOException {
        String projectPath = Project.getInstance().getPath();
        Path itemsDir = Paths.get(projectPath, "items");

        try (Stream<Path> paths = Files.list(itemsDir)) {
            paths.filter(path -> path.toString().endsWith(".json")).forEach(path -> {
                try {
                    Item item = mapper.readValue(path.toFile(), Item.class);
                    ItemList.getInstance().addItem(item);
                } catch (IOException ex) {
                    System.err.println("Error loading item from file: " + path);
                }
            });
        } catch (IOException ex) {
            System.err.println("Error listing files in directory: " + itemsDir);
        }
    }

    /**
     * Checks if a value is valid.
     * @param value the value to check.
     * @return true if the value is valid, false otherwise.
     */
    public boolean valueValid(String value) {
        if (StringUtil.isInteger(value)) {
            return Integer.parseInt(value) > 0;
        }
        return false;
    }

    /**
     * Checks if a level is valid.
     * @param level the level to check.
     * @return true if the level is valid, false otherwise.
     */
    public boolean levelValid(String level) {
        if (StringUtil.isInteger(level)) {
            int parsedLevel = Integer.parseInt(level);
            return parsedLevel > 0 && parsedLevel <= MaxLevel.getInstance().getLevel();
        }
        return false;
    }

    /**
     * Parses bounty levels from a string.
     * @param bountyLevels the string to parse.
     * @return a set of parsed bounty levels.
     */
    public Set<Integer> parseBountyLevels(String bountyLevels) {
        if (!bountyLevelsValid(bountyLevels)) {
            return new java.util.HashSet<>(Set.of());
        }

        String[] splitBountyLevels = bountyLevels.split(",");
        Set<Integer> convertedBountyLevels = new java.util.HashSet<>(Set.of());

        for (String bountyLevel : splitBountyLevels) {
            convertedBountyLevels.add(Integer.parseInt(bountyLevel.trim()));
        }

        return convertedBountyLevels;
    }

    /**
     * Checks if bounty levels are valid.
     * @param bountyLevels the bounty levels to check.
     * @return true if the bounty levels are valid, false otherwise.
     */
    public boolean bountyLevelsValid(String bountyLevels) {
        String[] splitBountyLevels = bountyLevels.split(",");
        for (String bountyLevel : splitBountyLevels) {
            if (!StringUtil.isInteger(bountyLevel.trim())) {
                return false;
            }
        }
        return true;
    }
}