package com.nhat.modpackassistant.controller.bottom;

import com.nhat.modpackassistant.controller.BaseController;
import com.nhat.modpackassistant.model.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.util.Optional;

/**
 * Controller for the Bounty view.
 * Handles the interaction between the Bounty model and the Bounty view.
 */
public class BountyController extends BaseController {

    @FXML
    private TableView<Bounty> bountyTable;
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
    private final IntegerStringConverter integerStringConverter = new IntegerStringConverter();

    /**
     * Initializes the BountyController.
     * Sets up the TableView and its columns, and the actions of the buttons.
     */
    @FXML
    public void initialize() {
        // Get the ObservableList of bounties from the BountyList singleton.
        bounties = Bounties.getInstance().getBounties();
        // Set the bounties to the TableView.
        bountyTable.setItems(bounties);

        // Set the text of the input fields to default values.
        bountyLevelField.setText("1");
        minValueField.setText("1");
        maxValueField.setText("1");

        // Set up the cell value factories for the TableView columns.
        bountyLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        minValueColumn.setCellValueFactory(new PropertyValueFactory<>("minValue"));
        maxValueColumn.setCellValueFactory(new PropertyValueFactory<>("maxValue"));

        // Set up a cell factory to allow editing of the cells.
        bountyLevelColumn.setCellFactory(TextFieldTableCell.forTableColumn(integerStringConverter));
        minValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(integerStringConverter));
        maxValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(integerStringConverter));

        // Add a change listener to the textProperty of the bountyLevelField to only allow positive integers.
        bountyLevelField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isPositiveInteger(newValue)) {
                bountyLevelField.setStyle("-fx-text-fill: red;");
            } else {
                bountyLevelField.setStyle("-fx-text-fill: white;");
            }
        });

        // Add a change listener to the textProperty of the minValueField to only allow positive integers.
        minValueField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isPositiveInteger(newValue)) {
                minValueField.setStyle("-fx-text-fill: red;");
            } else {
                minValueField.setStyle("-fx-text-fill: white;");
            }
        });

        // Add a change listener to the textProperty of the maxValueField to only allow positive integers.
        maxValueField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isPositiveInteger(newValue)) {
                maxValueField.setStyle("-fx-text-fill: red;");
            } else {
                maxValueField.setStyle("-fx-text-fill: white;");
            }
        });

        // Add a focus change listener to the bountyLevelField to set the text back to 1 if it's invalid.
        bountyLevelField.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused && !isPositiveInteger(bountyLevelField.getText())) {
                bountyLevelField.setText("1");
            }
        });

        // Add a focus change listener to the minValueField to set the text back to 1 if it's invalid.
        minValueField.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused && !isPositiveInteger(minValueField.getText())) {
                minValueField.setText("1");
            }
        });

        // Add a focus change listener to the maxValueField to set the text back to 1 if it's invalid.
        maxValueField.focusedProperty().addListener((observable, wasFocused, isNowFocused) -> {
            if (wasFocused && !isNowFocused && !isPositiveInteger(maxValueField.getText())) {
                maxValueField.setText("1");
            }
        });

        // Handle the editCommit event to update the bounty when the cell is edited.
        bountyLevelColumn.setOnEditCommit(event -> event.getRowValue().setLevel(event.getNewValue()));
        minValueColumn.setOnEditCommit(event -> event.getRowValue().setMinValue(event.getNewValue()));
        maxValueColumn.setOnEditCommit(event -> event.getRowValue().setMaxValue(event.getNewValue()));

        // Bind the prefWidthProperty of each TableColumn to a percentage of the TableView's width.
        bountyLevelColumn.prefWidthProperty().bind(Bindings.divide(bountyTable.widthProperty(), 3));
        minValueColumn.prefWidthProperty().bind(Bindings.divide(bountyTable.widthProperty(), 3));
        maxValueColumn.prefWidthProperty().bind(Bindings.divide(bountyTable.widthProperty(), 3));

        // Set the action of the addButton to call the addBounty method.
        addButton.setOnAction(e -> addBounty());

        // Set the action of the removeButton to call the removeItem method.
        removeButton.setOnAction(e -> removeItem());

        // Make the TableView editable.
        bountyTable.setEditable(true);
    }

    /**
     * Adds a new bounty to the BountyList.
     * The bounty is created using the text from the input fields.
     * The bounty is only added if all input conditions are met.
     */
    private void addBounty() {
        String bountyLevelText = bountyLevelField.getText();
        String minValueText = minValueField.getText();
        String maxValueText = maxValueField.getText();

        // Check if all conditions are met
        if (!isPositiveInteger(bountyLevelText) ||
            !isPositiveInteger(minValueText) ||
            !isPositiveInteger(maxValueText)) {
            return;
        }

        int bountyLevel = integerStringConverter.fromString(bountyLevelText);
        int minValue = integerStringConverter.fromString(minValueText);
        int maxValue = integerStringConverter.fromString(maxValueText);

        // Create a new Bounty and add it to the BountyList.
        Bounty bounty = new Bounty(bountyLevel, minValue, maxValue);
        Bounties.getInstance().addBounty(bounty);
    }

    /**
     * Removes the selected bounty from the BountyList.
     */
    private void removeItem() {
        // Get the selected bounty and remove it from the BountyList.
        Optional.ofNullable(bountyTable.getSelectionModel().getSelectedItem())
                .ifPresent(selectedItem -> {
                    Bounties.getInstance().removeBounty(selectedItem);
                    Platform.runLater(() -> bounties.remove(selectedItem));
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
}