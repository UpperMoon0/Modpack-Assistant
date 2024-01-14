package com.nhat.modpackassistant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

public class Item {
    private String id;
    private int value;
    private int level;
    private Set<Integer> bountyLevels;
    private boolean needResearch;

    public Item() {
        bountyLevels = new HashSet<>();
    }

    public Item(String id, int value, int level, Set<Integer> bountyLevels, boolean needResearch) {
        this.id = id;
        this.value = value;
        this.level = level;
        this.bountyLevels = bountyLevels;
        this.needResearch = needResearch;
    }

    public String getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public int getLevel() {
        return level;
    }

    public Set<Integer> getBountyLevels() {
        return bountyLevels;
    }

    public boolean getNeedResearch() {
        return needResearch;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setBountyLevels(Set<Integer> bountyLevels) {
        this.bountyLevels = bountyLevels;
    }

    public void setNeedResearch(boolean needResearch) {
        this.needResearch = needResearch;
    }
}