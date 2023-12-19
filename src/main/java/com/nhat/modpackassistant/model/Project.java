package com.nhat.modpackassistant.model;

public class Project {
    private static Project instance;
    private String path;

    private Project() {
    }

    public static Project getInstance() {
        if (instance == null) {
            instance = new Project();
        }
        return instance;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}