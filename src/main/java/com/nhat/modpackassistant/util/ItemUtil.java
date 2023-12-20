package com.nhat.modpackassistant.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhat.modpackassistant.model.Item;
import com.nhat.modpackassistant.model.ItemList;
import com.nhat.modpackassistant.model.Project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ItemUtil {
    private static com.nhat.modpackassistant.util.ItemUtil instance;
    private final ObjectMapper mapper;

    private ItemUtil() {
        this.mapper = new ObjectMapper();
    }

    public static ItemUtil getInstance() {
        if (instance == null) {
            instance = new com.nhat.modpackassistant.util.ItemUtil();
        }
        return instance;
    }

    public void saveItems() throws IOException {
        String projectPath = Project.getInstance().getPath();
        Path itemsDir = Paths.get(projectPath, "items");

        if (!FileUtil.pathExists(itemsDir.toString())) {
            FileUtil.createDir(projectPath, "items");
        } else {
            // Delete all files in the directory
            FileUtil.deleteAllFilesInDir(itemsDir.toString());
        }

        for (Item item : ItemList.getInstance().getItems()) {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(item);
            String itemId = item.getId();
            Path itemFile = itemsDir.resolve(itemId + ".json");
            FileUtil.writeToFile(itemFile.toString(), json.getBytes());
        }
    }

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
}