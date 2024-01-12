package com.nhat.modpackassistant.model;

public class MaxLevel {
    private static MaxLevel instance;
    private int level = 1;

    private MaxLevel() {
    }

    public static MaxLevel getInstance() {
        if (instance == null) {
            instance = new MaxLevel();
        }
        return instance;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}