package com.nhat.modpackassistant.controller;

import com.nhat.modpackassistant.model.Item;
import com.nhat.modpackassistant.model.ItemList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ItemController extends BaseController {
    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Item, String> itemIdColumn;
    @FXML
    private TableColumn<Item, Integer> itemValueColumn;
    @FXML
    private TableColumn<Item, Integer> itemLevelColumn;
    // String for adding multiple bounty levels separated by commas.
    @FXML
    private TableColumn<Item, String> itemBountyLevelColumn;
    @FXML
    private TextField itemIdField;
    @FXML
    private TextField itemValueField;
    @FXML
    private TextField itemLevelField;
    @FXML
    private TextField itemBountyLevelField;
    @FXML
    private Button addButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button removeButton;

    private ObservableList<Item> items;

    @FXML
    public void initialize() {
        // Get the ObservableList of items from the ItemList singleton.
        items = ItemList.getInstance().getItems();
        itemTable.setItems(items);

        // Set up the cell value factories for the TableView columns.
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        itemValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        itemLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        itemBountyLevelColumn.setCellValueFactory(new PropertyValueFactory<>("bountyLevelsString"));

        // Set up a cell factory to allow editing of the cells.
        itemIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        itemValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        itemLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        itemBountyLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Handle the editCommit event to update the item when the cell is edited.
        itemIdColumn.setOnEditCommit(event -> event.getRowValue().setId(event.getNewValue()));
        itemValueColumn.setOnEditCommit(event -> event.getRowValue().setValue(event.getNewValue()));
        itemLevelColumn.setOnEditCommit(event -> event.getRowValue().setLevel(event.getNewValue()));
        itemBountyLevelColumn.setOnEditCommit(event -> {
            Set<Integer> bountyLevels = new HashSet<>();
            for (String bountyLevel : event.getNewValue().split(",")) {
                bountyLevels.add(Integer.parseInt(bountyLevel.trim()));
            }
            event.getRowValue().setBountyLevels(bountyLevels);
        });

        // Bind the prefWidthProperty of each TableColumn to a percentage of the TableView's width.
        itemIdColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.45));
        itemValueColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.15));
        itemLevelColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.15));
        itemBountyLevelColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.25));

        // Add a listener to the textProperty of the itemLevelField to update the itemBountyLevelField text.
        itemLevelField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(itemBountyLevelField.getText())) {
                itemBountyLevelField.setText(newValue);
            }
        });

        // Set the action of the addButton to call the addItem method and clear the input fields.
        addButton.setOnAction(e -> {
            try {
                addItem();
                clearFields();
            } catch (NumberFormatException ex) {
                showErrorAlert("Input Error", "Input values must be integers.");
            }
        });

        // Set the action of the saveButton to call the saveItems method.
        saveButton.setOnAction(e -> {
            try {
                com.nhat.modpackassistant.util.ItemUtil.getInstance().saveItems();
            } catch (IOException ex) {
                showErrorAlert("Error saving items", "Could not save items to JSON files.");
            }
        });

        // Set the action of the removeButton to call the removeItem method.
        removeButton.setOnAction(e -> removeItem());

        // Make the TableView editable.
        itemTable.setEditable(true);
    }

    private void addItem() {
        int itemValue = Integer.parseInt(itemValueField.getText());
        int itemLevel = Integer.parseInt(itemLevelField.getText());

        Item item = new Item(
                itemIdField.getText(),
                itemValue,
                itemLevel
        );

        String[] bountyLevels = itemBountyLevelField.getText().split(",");
        for (String bountyLevel : bountyLevels) {
            item.addBountyLevel(Integer.parseInt(bountyLevel.trim()));
        }

        ItemList.getInstance().addItem(item);
    }

    private void removeItem() {
        Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            ItemList.getInstance().removeItem(selectedItem);
            items.remove(selectedItem);
        }
    }

    private void clearFields() {
        itemIdField.clear();
        itemValueField.clear();
        itemLevelField.clear();
        itemBountyLevelField.clear();
    }
}