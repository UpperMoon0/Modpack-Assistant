package com.nhat.modpackassistant.model;

public class Bounty {
    private int level;
    private int minValue;
    private int maxValue;

    public Bounty() {
    }

    public Bounty(int level, int minValue, int maxValue) {
        this.level = level;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
