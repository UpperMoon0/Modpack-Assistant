package com.nhat.modpackassistant.model;

public class Item {
    private String id;
    private String name;
    private int value;
    private int level;
    private int bountyLevel;

    public Item(String id, String name, int value, int level, int bountyLevel) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.level = level;
        this.bountyLevel = bountyLevel;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getLevel() {
        return level;
    }

    public int getBountyLevel() {
        return bountyLevel;
    }
}
