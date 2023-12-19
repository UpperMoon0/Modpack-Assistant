package com.nhat.modpackassistant.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static boolean pathExists(String path) {
        return Files.exists(Path.of(path));
    }

    public static void createDir(String path, String name) throws IOException {
        Path dirPath = Paths.get(path, name);
        Files.createDirectories(dirPath);
    }
}
