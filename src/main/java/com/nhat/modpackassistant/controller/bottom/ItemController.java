package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.controller.BaseController;
import com.nhat.modpackassistant.model.*;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;

import java.util.Optional;
import java.util.Set;

/**
 * Controller for the Item view.
 * Handles the interaction between the Item model and the Item view.
 */
public class ItemController extends BaseController {
    @FXML
    private AnchorPane root;
    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Item, String> itemIdColumn;
    @FXML
    private TableColumn<Item, Integer> itemValueColumn;
    @FXML
    private TableColumn<Item, Integer> itemLevelColumn;
    @FXML
    private TableColumn<Item, Set<Integer>> itemBountyLevelColumn;
    @FXML
    private TableColumn<Item, Boolean> needResearchColumn;
    @FXML
    private TextField itemIdField;
    @FXML
    private TextField itemValueField;
    @FXML
    private TextField itemLevelField;
    @FXML
    private TextField itemBountyLevelField;
    @FXML
    private CheckBox needResearchCheckBox;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;

    private ObservableList<Item> items;

    /**
     * Initializes the ItemController.
     * Sets up the TableView and its columns, and the actions of the buttons.
     */
    @FXML
    public void initialize() {
        // Get the ObservableList of items from the ItemList singleton.
        items = Items.getInstance().getItems();

        // Set the items to the TableView.
        itemTable.setItems(items);

        // Bind the prefHeightProperty of the TableView to 80% of the height of the root.
        itemTable.prefHeightProperty().bind(root.heightProperty().multiply(0.8));

        // Set the text of the input fields to default values.
        itemIdField.setText("item");
        itemValueField.setText("1");
        itemLevelField.setText("1");
        itemBountyLevelField.setText("");

        // Add a change listener to the textProperty of the itemIdField to only allow the specified format.
        itemIdField.textProperty().addListener((observable, oldValue, newValue) -> itemIdField.setStyle(!newValue.isEmpty() ? "-fx-text-fill: white;" : "-fx-text-fill: red;"));

        // Add a focus change listener to the itemIdField to set the text back to "item" if it's invalid.
        itemIdField.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused && itemIdField.getText().isEmpty()) {
                itemIdField.setText("item");
            }
        });

        // Add a change listener to the textProperty of the itemValueField to only allow positive integers.
        itemValueField.textProperty().addListener((observable, oldValue, newValue) -> itemValueField.setStyle(isPositiveInteger(newValue) ? "-fx-text-fill: white;" : "-fx-text-fill: red;"));

        // Add a focus change listener to the itemValueField to set the text back to 1 if it's invalid.
        itemValueField.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused && !isPositiveInteger(itemValueField.getText())) {
                itemValueField.setText("1");
            }
        });

        // Add a change listener to the textProperty of the itemLevelField to only allow integers between 1 and the current max level.
        itemLevelField.textProperty().addListener((observable, oldValue, newValue) -> itemLevelField.setStyle(isValidLevel(newValue) ? "-fx-text-fill: white;" : "-fx-text-fill: red;"));

        // Add a focus change listener to the itemLevelField to set the text back to 1 if it's invalid.
        itemLevelField.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused && !isValidLevel(itemLevelField.getText())) {
                itemLevelField.setText("1");
            }
        });

        // Add a change listener to the textProperty of the itemBountyLevelField to only allow the specified format.
        itemBountyLevelField.textProperty().addListener((observable, oldValue, newValue) -> itemBountyLevelField.setStyle(Bounties.isValidBountyLevels(newValue) ? "-fx-text-fill: white;" : "-fx-text-fill: red;"));

        // Add a focus change listener to the itemBountyLevelField to set the text back to 1 if it's invalid.
        itemBountyLevelField.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused && !Bounties.isValidBountyLevels(itemBountyLevelField.getText())) {
                itemBountyLevelField.setText("");
            }
        });

        // Set up the cell value factories for the TableView columns.
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        itemValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        itemLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        itemBountyLevelColumn.setCellValueFactory(new PropertyValueFactory<>("bountyLevels"));
        needResearchColumn.setCellValueFactory(new PropertyValueFactory<>("needResearch"));

        // Set up a cell factory to allow editing of the cells.
        itemIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        itemValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        itemLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        itemBountyLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BountyLevelStringConverter()));
        needResearchColumn.setCellFactory(column -> new CheckBoxTableCell<>() {
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CheckBox checkBox = new CheckBox();
                    checkBox.selectedProperty().setValue(item);
                    checkBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
                        Item currentItem = getTableView().getItems().get(getIndex());
                        currentItem.setNeedResearch(newValue);
                    });
                    setGraphic(checkBox);
                }
            }
        });

        // Handle the editCommit event to update the item when the cell is edited.
        itemIdColumn.setOnEditCommit(event -> event.getRowValue().setId(event.getNewValue()));
        itemValueColumn.setOnEditCommit(event -> event.getRowValue().setValue(event.getNewValue()));
        itemLevelColumn.setOnEditCommit(event -> event.getRowValue().setLevel(event.getNewValue()));
        itemBountyLevelColumn.setOnEditCommit(event -> event.getRowValue().setBountyLevels(event.getNewValue()));
        needResearchColumn.setOnEditCommit(event -> event.getRowValue().setNeedResearch(event.getNewValue()));

        // Bind the prefWidthProperty of each TableColumn to a percentage of the TableView's width.
        itemIdColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.4));
        itemValueColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.12));
        itemLevelColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.12));
        itemBountyLevelColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.2));
        needResearchColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.16));

        // Set the action of the addButton to call the addItem method.
        addButton.setOnAction(e -> addItem());

        // Set the action of the removeButton to call the removeItem method.
        removeButton.setOnAction(e -> removeItem());

        // Make the TableView editable.
        itemTable.setEditable(true);
    }

    /**
     * Adds a new item to the ItemList.
     * The item is created using the text from the input fields.
     */
    private void addItem() {
        String itemId = itemIdField.getText();
        String itemValueText = itemValueField.getText();
        String itemLevelText = itemLevelField.getText();
        String itemBountyLevelsText = itemBountyLevelField.getText();

        int itemValue = Integer.parseInt(itemValueText);
        int itemLevel = Integer.parseInt(itemLevelText);
        Set<Integer> itemBountyLevels = Bounties.parseBountyLevels(itemBountyLevelsText);
        boolean needResearch = needResearchCheckBox.isSelected();

        // Create a new Item and add it to the ItemList.
        Item item = new Item(itemId, itemValue, itemLevel, itemBountyLevels, needResearch);
        Items.getInstance().addItem(item);
    }

    /**
     * Removes the selected item from the ItemList.
     */
    private void removeItem() {
        // Get the selected item and remove it from the ItemList.
        Optional.ofNullable(itemTable.getSelectionModel().getSelectedItem())
                .ifPresent(selectedItem -> {
                    Items.getInstance().removeItem(selectedItem);
                    items.remove(selectedItem);
                });
    }

    /**
     * Checks if a string is a positive integer.
     *
     * @param value the string to check
     * @return true if the string is a positive integer, false otherwise
     */
    private boolean isPositiveInteger(String value) {
        return !value.isEmpty() && value.matches("\\d+") && !value.equals("0");
    }

    /**
     * Checks if a string is a valid level.
     * A valid level is a positive integer that is less than or equal to the current max level.
     *
     * @param value the string to check
     * @return true if the string is a valid level, false otherwise
     */
    private boolean isValidLevel(String value) {
        return isPositiveInteger(value) && Integer.parseInt(value) <= MaxLevel.getInstance().getLevel();
    }
}