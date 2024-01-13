package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.controller.BaseController;
import com.nhat.modpackassistant.model.*;
import com.nhat.modpackassistant.util.ItemUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

public class BountyController extends BaseController {
    @FXML
    private TableView<Bounty> itemTable;
    @FXML
    private TableColumn<Bounty, Integer> bountyLevelColumn;
    @FXML
    private TableColumn<Bounty, Integer> minValueColumn;
    @FXML
    private TableColumn<Bounty, Integer> maxValueColumn;
    @FXML
    private TextField bountyLevelField;
    @FXML
    private TextField minValueField;
    @FXML
    private TextField maxValueField;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;

    private ObservableList<Bounty> bounties;

    @FXML
    public void initialize() {
        // Get the ObservableList of items from the ItemList singleton.
        bounties = BountyList.getInstance().getBounties();
        // Set the items to the TableView.
        itemTable.setItems(bounties);

        // Set up the cell value factories for the TableView columns.
        bountyLevelColumn.setCellValueFactory(new PropertyValueFactory<>("bountyLevel"));
        minValueColumn.setCellValueFactory(new PropertyValueFactory<>("minValue"));
        maxValueColumn.setCellValueFactory(new PropertyValueFactory<>("maxValue"));

        // Set up a cell factory to allow editing of the cells.
        bountyLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        minValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        maxValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // Handle the editCommit event to update the item when the cell is edited.
        bountyLevelColumn.setOnEditCommit(event -> event.getRowValue().setLevel(event.getNewValue()));
        minValueColumn.setOnEditCommit(event -> event.getRowValue().setMinValue(event.getNewValue()));
        maxValueColumn.setOnEditCommit(event -> event.getRowValue().setMaxValue(event.getNewValue()));

        // Bind the prefWidthProperty of each TableColumn to a percentage of the TableView's width.
        bountyLevelColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.3));
        minValueColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.35));
        maxValueColumn.prefWidthProperty().bind(itemTable.widthProperty().multiply(0.35));

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
    }
}
