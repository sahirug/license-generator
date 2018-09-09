package com.home.test.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Runner extends Application {
    private Stage primaryStage;
    private VBox generator;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("License Generator v1.0");

        showGenerator();
    }

    private void showGenerator() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClassLoader.getSystemClassLoader().getResource("fxml/generator.fxml"));
            generator = loader.load();

            Scene scene = new Scene(generator);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(false);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
