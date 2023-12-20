package com.nhat.modpackassistant.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileUtil {
    public static boolean pathExists(String path) {
        return Files.exists(Path.of(path));
    }

    public static void createDir(String path, String name) throws IOException {
        Path dirPath = Paths.get(path, name);
        Files.createDirectories(dirPath);
    }

    public static void deleteAllFilesInDir(String dirPath) throws IOException {
        try (Stream<Path> paths = Files.list(Paths.get(dirPath))) {
            paths.forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException ex) {
                    System.err.println("Error deleting file: " + path);
                }
            });
        }
    }

    public static void writeToFile(String filePath, byte[] content) throws IOException {
        Files.write(Paths.get(filePath), content);
    }
}
