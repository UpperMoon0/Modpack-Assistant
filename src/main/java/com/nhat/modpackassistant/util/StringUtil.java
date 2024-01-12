package com.nhat.modpackassistant.util;

public class StringUtil {
    public static String formatProjectName(String projectName) {
        return projectName.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s", "_");
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
