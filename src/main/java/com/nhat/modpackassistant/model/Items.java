package com.nhat.modpackassistant.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class represents a collection of items.
 * It uses the Singleton design pattern to ensure only one instance of the class is created.
 */
public class Items {
    private static Items instance;
    private final ObservableList<Item> items;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private Items() {
        items = FXCollections.observableArrayList();
    }

    /**
     * Returns the single instance of the Items class.
     * If the instance has not been created yet, it will be created.
     *
     * @return the single instance of the Items class
     */
    public static Items getInstance() {
        if (instance == null) {
            instance = new Items();
        }
        return instance;
    }

    /**
     * Adds an item to the list of items.
     *
     * @param item the item to add
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Removes an item from the list of items.
     *
     * @param item the item to remove
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Returns the list of items.
     *
     * @return the list of items
     */
    public ObservableList<Item> getItems() {
        return items;
    }

    /**
     * Saves the list of items to a file.
     * Each item is saved as a separate JSON file.
     *
     * @throws IOException if an I/O error occurs
     */
    public static void saveItems() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String projectPath = Project.getInstance().getPath();
        Path dirPath = Paths.get(projectPath, "items");

        // If the directory does not exist, create it
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        } else {
            // If the directory exists, delete all files in it
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dirPath)) {
                for (Path path : directoryStream) {
                    Files.delete(path);
                }
            }
        }

        // Save each item as a separate JSON file
        for (Item item : getInstance().getItems()) {
            String itemId = item.getId().replace("#", "tag_").replace(":", "_").replace("/", "_s_");
            Path filePath = dirPath.resolve(itemId + ".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), item);
        }
    }

    /**
     * Loads the list of items from a file.
     * Each item is loaded from a separate JSON file.
     *
     * @throws IOException if an I/O error occurs
     */
    public static void loadItems() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String projectPath = Project.getInstance().getPath();
        Path dirPath = Paths.get(projectPath, "items");

        // If the directory exists, load each item from a separate JSON file
        if (Files.exists(dirPath)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dirPath)) {
                for (Path path : directoryStream) {
                    Item item = mapper.readValue(path.toFile(), Item.class);
                    getInstance().addItem(item);
                }
            }
        }
    }
}