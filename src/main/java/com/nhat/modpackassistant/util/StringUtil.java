package com.nhat.modpackassistant.util;

public class StringUtil {
    public static String formatProjectName(String projectName) {
        return projectName.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s", "_");
    }
}
