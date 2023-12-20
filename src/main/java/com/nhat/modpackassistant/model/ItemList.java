package com.nhat.modpackassistant.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ItemList {
    private static ItemList instance;
    private final ObservableList<Item> items;

    private ItemList() {
        items = FXCollections.observableArrayList();
    }

    public static ItemList getInstance() {
        if (instance == null) {
            instance = new ItemList();
        }
        return instance;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public ObservableList<Item> getItems() {
        return items;
    }
}