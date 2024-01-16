package com.nhat.modpackassistant.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a collection of bounties.
 * It uses the Singleton design pattern to ensure only one instance of the class is created.
 */
public class Bounties {
    private static Bounties instance;
    private final ObservableList<Bounty> bounties;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private Bounties() {
        bounties = FXCollections.observableArrayList();
    }

    /**
     * Returns the single instance of the Bounties class.
     * If the instance has not been created yet, it will be created.
     *
     * @return the single instance of the Bounties class
     */
    public static Bounties getInstance() {
        if (instance == null) {
            instance = new Bounties();
        }
        return instance;
    }

    /**
     * Adds a bounty to the list of bounties.
     *
     * @param bounty the bounty to add
     */
    public void addBounty(Bounty bounty) {
        bounties.add(bounty);
    }

    /**
     * Removes a bounty from the list of bounties.
     *
     * @param bounty the bounty to remove
     */
    public void removeBounty(Bounty bounty) {
        bounties.remove(bounty);
    }

    /**
     * Returns the list of bounties.
     *
     * @return the list of bounties
     */
    public ObservableList<Bounty> getBounties() {
        return bounties;
    }

    /**
     * Saves the list of bounties to a file.
     *
     * @throws IOException if an I/O error occurs
     */
    public static void saveBounties() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String projectPath = Project.getInstance().getPath();
        Path dirPath = Paths.get(projectPath, "bounties");

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        } else {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dirPath)) {
                for (Path path : directoryStream) {
                    Files.delete(path);
                }
            }
        }

        for (Bounty bounty : getInstance().getBounties()) {
            Path filePath = dirPath.resolve("bounty_" + bounty.getLevel() + ".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), bounty);
        }
    }

    /**
     * Loads the list of bounties from a file.
     *
     * @throws IOException if an I/O error occurs
     */
    public static void loadBounties() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String projectPath = Project.getInstance().getPath();
        Path dirPath = Paths.get(projectPath, "bounties");

        if (Files.exists(dirPath)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dirPath)) {
                for (Path path : directoryStream) {
                    if (path.toString().endsWith(".json")) {
                        Bounty bounty = mapper.readValue(path.toFile(), Bounty.class);
                        getInstance().getBounties().add(bounty);
                    }
                }
            }
        }
    }

    /**
     * Checks if a string of bounty levels is valid.
     *
     * @param levelsString the string of bounty levels
     * @return true if the string of bounty levels is valid, false otherwise
     */
    public static boolean isValidBountyLevels(String levelsString) {
        Set<Integer> levels = parseBountyLevels(levelsString);

        for (int level : levels) {
            if (level <= 0 || level > Bounties.getInstance().getBounties().size()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Parses a string of bounty levels into a set of integers.
     *
     * @param str the string of bounty levels
     * @return a set of integers representing the bounty levels
     */
    public static Set<Integer> parseBountyLevels(String str) {
        String levelsString = str.replace(" ", "");
        if (levelsString.isEmpty()) {
            return new HashSet<>();
        }
        String[] levels = levelsString.split(",");
        Set<Integer> levelSet = new HashSet<>();
        for (String level : levels) {
            try {
                int levelValue = Integer.parseInt(level);
                levelSet.add(levelValue);
            } catch (NumberFormatException e) {
                return new HashSet<>() {{
                    add(-1);
                }};
            }
        }
        return levelSet;
    }

    /**
     * Returns the bounty with the specified level.
     *
     * @param level the level of the bounty to return
     * @return the bounty with the specified level
     */
    public Bounty getBountyByLevel(int level) {
        for (Bounty bounty : bounties) {
            if (bounty.getLevel() == level) {
                return bounty;
            }
        }
        return null;
    }
}