package com.library;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginPage loginPage = new LoginPage(primaryStage);
        Scene scene = new Scene(loginPage, 400, 300);
        primaryStage.setTitle("Library Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
