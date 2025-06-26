package com.library;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class UserDashboard extends VBox {

    public UserDashboard(Stage stage, User user) {
        setSpacing(15);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label title = new Label("User Dashboard");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label welcomeLabel = new Label("Welcome, " + user.getName() + "!");
        welcomeLabel.setStyle("-fx-font-size: 14px;");

        Button searchBooksBtn = new Button("Search Books");
        Button viewBorrowedBtn = new Button("My Borrowed Books");
        Button logoutBtn = new Button("Logout");

        searchBooksBtn.setOnAction(e -> {
            SearchBooksPage searchBooksPage = new SearchBooksPage(stage, user);
            stage.setScene(new Scene(searchBooksPage, 500, 400));
        });

        viewBorrowedBtn.setOnAction(e -> {
            // Placeholder — navigate to BorrowedBooksPage
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "BorrowedBooksPage goes here.");
            alert.showAndWait();
        });

        logoutBtn.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(new Scene(loginPage, 400, 300));
        });

        getChildren().addAll(
                title,
                welcomeLabel,
                searchBooksBtn,
                viewBorrowedBtn,
                logoutBtn);
    }
}
