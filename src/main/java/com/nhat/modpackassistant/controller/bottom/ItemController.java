package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.controller.BaseController;
import com.nhat.modpackassistant.model.BountyLevelStringConverter;
import com.nhat.modpackassistant.model.Item;
import com.nhat.modpackassistant.model.ItemList;
import com.nhat.modpackassistant.util.ItemUtil;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.util.Optional;
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
    @FXML
    private TableColumn<Item, Set<Integer>> itemBountyLevelColumn;
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
        itemBountyLevelColumn.setCellValueFactory(new PropertyValueFactory<>("bountyLevels"));

        // Set up a cell factory to allow editing of the cells.
        itemIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        itemValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        itemLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        itemBountyLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BountyLevelStringConverter()));

        // Handle the editCommit event to update the item when the cell is edited.
        itemIdColumn.setOnEditCommit(event -> event.getRowValue().setId(event.getNewValue()));
        itemValueColumn.setOnEditCommit(event -> event.getRowValue().setValue(event.getNewValue()));
        itemLevelColumn.setOnEditCommit(event -> event.getRowValue().setLevel(event.getNewValue()));
        itemBountyLevelColumn.setOnEditCommit(event -> event.getRowValue().setBountyLevels(event.getNewValue()));

        // Bind the prefWidthProperty of each TableColumn to a percentage of the TableView's width.
        itemIdColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.45));
        itemValueColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.15));
        itemLevelColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.15));
        itemBountyLevelColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.25));

        // Add a listener to the textProperty of the itemValueField and itemLevelField to update the text fill color.
        ItemUtil itemUtil = ItemUtil.getInstance();
        setTextFillBasedOnValidity(itemValueField, itemUtil::valueValid);
        setTextFillBasedOnValidity(itemLevelField, itemUtil::levelValid);
        setTextFillBasedOnValidity(itemBountyLevelField, itemUtil::bountyLevelsValid);

        // Set the action of the addButton to call the addItem method and clear the input fields.
        addButton.setOnAction(e -> {
            if (!ItemUtil.getInstance().valueValid(itemValueField.getText())) {
                showErrorAlert("Error adding item", "Item value must be a positive integer.");
            } else if (!ItemUtil.getInstance().levelValid(itemLevelField.getText())) {
                showErrorAlert("Error adding item", "Item level must be a positive integer and less than or equal to the max level.");
            } else if (!ItemUtil.getInstance().bountyLevelsValid(itemBountyLevelField.getText())) {
                showErrorAlert("Error adding item", "Bounty levels must be positive integer(s).");
            } else {
                addItem();
                clearFields();
            }
        });

        // Set the action of the removeButton to call the removeItem method.
        removeButton.setOnAction(e -> removeItem());

        // Make the TableView editable.
        itemTable.setEditable(true);
    }

    private void addItem() {
        String itemId = itemIdField.getText();
        int itemValue = Integer.parseInt(itemValueField.getText());
        int itemLevel = Integer.parseInt(itemLevelField.getText());
        Set<Integer> itemBountyLevels = ItemUtil.getInstance().parseBountyLevels(itemBountyLevelField.getText());

        Item item = new Item(itemId, itemValue, itemLevel, itemBountyLevels);
        ItemList.getInstance().addItem(item);
    }

    private void removeItem() {
        Optional.ofNullable(itemTable.getSelectionModel().getSelectedItem())
                .ifPresent(selectedItem -> {
                    ItemList.getInstance().removeItem(selectedItem);
                    items.remove(selectedItem);
                });
    }

    private void clearFields() {
        itemIdField.clear();
        itemValueField.clear();
        itemLevelField.clear();
        itemBountyLevelField.clear();
    }
}