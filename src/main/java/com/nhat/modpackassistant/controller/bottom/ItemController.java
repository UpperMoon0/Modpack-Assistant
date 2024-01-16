package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.controller.BaseController;
import com.nhat.modpackassistant.model.*;
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
    private TableColumn<Item, String> researchLevelColumn;
    @FXML
    private TextField idField;
    @FXML
    private TextField valueField;
    @FXML
    private TextField levelField;
    @FXML
    private TextField bountyLevelField;
    @FXML
    private CheckBox needResearchCheckBox;
    @FXML
    private TextField researchLevelField;
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
        idField.setText("item");
        valueField.setText("1");
        levelField.setText("1");
        bountyLevelField.setText("");
        researchLevelField.setText("");

        // Add a change listener to the textProperty of the idField to only allow the specified format.
        idField.textProperty().addListener((observable, oldValue, newValue) -> idField.setStyle(!newValue.isEmpty() ? "-fx-text-fill: white;" : "-fx-text-fill: red;"));

        // Add a focus change listener to the idField to set the text back to "item" if it's invalid.
        idField.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused && idField.getText().isEmpty()) {
                idField.setText("item");
            }
        });

        // Add a change listener to the textProperty of the valueField to only allow positive integers.
        valueField.textProperty().addListener((observable, oldValue, newValue) -> valueField.setStyle(isPositiveInteger(newValue) ? "-fx-text-fill: white;" : "-fx-text-fill: red;"));

        // Add a focus change listener to the valueField to set the text back to 1 if it's invalid.
        valueField.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused && !isPositiveInteger(valueField.getText())) {
                valueField.setText("1");
            }
        });

        // Add a change listener to the textProperty of the levelField to only allow integers between 1 and the current max level.
        levelField.textProperty().addListener((observable, oldValue, newValue) -> levelField.setStyle(isValidLevel(newValue) ? "-fx-text-fill: white;" : "-fx-text-fill: red;"));

        // Add a focus change listener to the levelField to set the text back to 1 if it's invalid.
        levelField.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused && !isValidLevel(levelField.getText())) {
                levelField.setText("1");
            }
        });

        // Add a change listener to the textProperty of the bountyLevelField to only allow the specified format.
        bountyLevelField.textProperty().addListener((observable, oldValue, newValue) -> bountyLevelField.setStyle(Bounties.isValidBountyLevels(newValue) ? "-fx-text-fill: white;" : "-fx-text-fill: red;"));

        // Add a focus change listener to the bountyLevelField to set the text back to 1 if it's invalid.
        bountyLevelField.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused && !Bounties.isValidBountyLevels(bountyLevelField.getText())) {
                bountyLevelField.setText("");
            }
        });

        // Set up the cell value factories for the TableView columns.
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        itemValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        itemLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        itemBountyLevelColumn.setCellValueFactory(new PropertyValueFactory<>("bountyLevels"));
        researchLevelColumn.setCellValueFactory(new PropertyValueFactory<>("researchLevel"));

        // Set up a cell factory to allow editing of the cells.
        itemIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        itemValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        itemLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        itemBountyLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BountyLevelStringConverter()));
        researchLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Handle the editCommit event to update the item when the cell is edited.
        itemIdColumn.setOnEditCommit(event -> event.getRowValue().setId(event.getNewValue()));
        itemValueColumn.setOnEditCommit(event -> event.getRowValue().setValue(event.getNewValue()));
        itemLevelColumn.setOnEditCommit(event -> event.getRowValue().setLevel(event.getNewValue()));
        itemBountyLevelColumn.setOnEditCommit(event -> event.getRowValue().setBountyLevels(event.getNewValue()));
        researchLevelColumn.setOnEditCommit(event -> event.getRowValue().setResearchLevel(event.getNewValue()));

        // Bind the prefWidthProperty of each TableColumn to a percentage of the TableView's width.
        itemIdColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.4));
        itemValueColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.12));
        itemLevelColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.12));
        itemBountyLevelColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.2));
        researchLevelColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.16));

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
        String itemId = idField.getText();
        String itemValueText = valueField.getText();
        String itemLevelText = levelField.getText();
        String itemBountyLevelsText = bountyLevelField.getText();

        int itemValue = Integer.parseInt(itemValueText);
        int itemLevel = Integer.parseInt(itemLevelText);
        Set<Integer> itemBountyLevels = Bounties.parseBountyLevels(itemBountyLevelsText);
        boolean needResearch = needResearchCheckBox.isSelected();
        Item item;

        if (!needResearch) {
            item = new Item(itemId, itemValue, itemLevel, itemBountyLevels);
        } else {
            String researchLevel = researchLevelField.getText();
            if (researchLevel.isEmpty()) {
                researchLevel = itemId.contains(":") ? itemId.split(":")[1] : itemId;
            }
            item = new Item(itemId, itemValue, itemLevel, itemBountyLevels, researchLevel);
        }

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