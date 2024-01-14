package com.nhat.modpackassistant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ModpackAssistant extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the top bar view
        FXMLLoader topBarLoader = new FXMLLoader(ModpackAssistant.class.getResource("top-bar-empty-view.fxml"));
        AnchorPane topBar = topBarLoader.load();

        // Load the home view
        FXMLLoader homeLoader = new FXMLLoader(ModpackAssistant.class.getResource("home-view.fxml"));
        AnchorPane homeView = homeLoader.load();

        // Create a BorderPane and add the top bar and home view
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topBar);
        borderPane.setCenter(homeView);

        Scene scene = new Scene(borderPane, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/nhat/modpackassistant/css/style.css")).toExternalForm());
        stage.setTitle("Modpack Assistant");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}