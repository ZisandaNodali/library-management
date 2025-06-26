package com.library;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboard extends VBox {
    public AdminDashboard(Stage stage, User admin) {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome, Admin " + admin.getName());
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button viewUsersBtn = new Button("View All Users");
        Button viewBooksBtn = new Button("View All Books");
        Button addBookBtn = new Button("Add New Book"); // Added button
        Button manageBooksBtn = new Button("Edit/Delete Books");
        Button logoutBtn = new Button("Logout");

        viewUsersBtn.setOnAction(e -> {
            ViewUsersPage usersPage = new ViewUsersPage(stage, admin);
            stage.setScene(new Scene(usersPage, 600, 400));
        });

        viewBooksBtn.setOnAction(e -> {
            ViewBooksPage booksPage = new ViewBooksPage(stage, admin);
            stage.setScene(new Scene(booksPage, 600, 400));
        });

        addBookBtn.setOnAction(e -> {
            AddBookPage addBookPage = new AddBookPage(stage, admin);
            stage.setScene(new Scene(addBookPage, 600, 400));
        });

        manageBooksBtn.setOnAction(e -> {
            ManageBooksPage manageBooksPage = new ManageBooksPage(stage, admin);
            stage.setScene(new Scene(manageBooksPage, 700, 450));
        });

        logoutBtn.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(new Scene(loginPage, 400, 300));
        });

        getChildren().addAll(welcomeLabel, viewUsersBtn, viewBooksBtn, addBookBtn, manageBooksBtn, logoutBtn);

    }
}