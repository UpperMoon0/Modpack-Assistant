package com.nhat.modpackassistant.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class represents the maximum level in the application.
 * It uses the Singleton design pattern to ensure only one instance of the class is created.
 */
public class MaxLevel {
    private static MaxLevel instance;
    private int level = 1;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private MaxLevel() {
    }

    /**
     * Returns the single instance of the MaxLevel class.
     * If the instance has not been created yet, it will be created.
     *
     * @return the single instance of the MaxLevel class
     */
    public static MaxLevel getInstance() {
        if (instance == null) {
            instance = new MaxLevel();
        }
        return instance;
    }

    /**
     * Sets the maximum level.
     *
     * @param level the maximum level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Returns the maximum level.
     *
     * @return the maximum level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Saves the maximum level to a file.
     *
     * @throws IOException if an I/O error occurs
     */
    public static void saveLevel() throws IOException {
        String level = String.valueOf(MaxLevel.getInstance().getLevel());
        Files.writeString(Paths.get(Project.getInstance().getPath(), "levels.txt"), level);
    }

    /**
     * Loads the maximum level from a file.
     *
     * @throws IOException if an I/O error occurs
     */
    public static void loadLevels() throws IOException {
        String level = Files.readString(Paths.get(Project.getInstance().getPath(), "levels.txt"));
        MaxLevel.getInstance().setLevel(Integer.parseInt(level.trim()));
    }
}