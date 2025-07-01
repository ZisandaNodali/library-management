package com.library;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class UserDashboard extends BorderPane {

    public UserDashboard(Stage stage, User user) {

        // Set background image
        String imageUrl = "https://th.bing.com/th/id/OIP.wE9qJ6MSb13YWwvLt5D-TQHaE6?r=0&rs=1&pid=ImgDetMain";
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(imageUrl, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        setBackground(new Background(backgroundImage));
        // Title and welcome at the top
        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(30));

        Label title = new Label("📚 User Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label welcomeLabel = new Label("Welcome, " + user.getName() + "!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: blue;");

        topBox.getChildren().addAll(title, welcomeLabel);
        setTop(topBox);

        // Button setup
        Button searchBooksBtn = new Button("🔍 Search Books");
        Button viewBorrowedBtn = new Button("📖 My Borrowed Books");
        Button logoutBtn = new Button("🚪 Logout");

        // Apply blue button styling with hover effects
        for (Button button : new Button[]{searchBooksBtn, viewBorrowedBtn, logoutBtn}) {
            button.setStyle(
                    "-fx-background-color: #2196F3;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 10 20 10 20;" +
                    "-fx-background-radius: 8;"
            );
            button.setOnMouseEntered(e -> button.setStyle(
                    "-fx-background-color: #1976D2;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 10 20 10 20;" +
                    "-fx-background-radius: 8;"
            ));
            button.setOnMouseExited(e -> button.setStyle(
                    "-fx-background-color: #2196F3;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 10 20 10 20;" +
                    "-fx-background-radius: 8;"
            ));
        }

        // Bottom-left button layout
        VBox buttonBox = new VBox(15);
        buttonBox.setPadding(new Insets(20));
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);
        buttonBox.getChildren().addAll(searchBooksBtn, viewBorrowedBtn, logoutBtn);

        setBottom(buttonBox);
        BorderPane.setAlignment(buttonBox, Pos.BOTTOM_LEFT);

        // Button actions
        searchBooksBtn.setOnAction(e -> {
            SearchBooksPage searchBooksPage = new SearchBooksPage(stage, user);
            stage.setScene(new Scene(searchBooksPage, 500, 400));
        });

        viewBorrowedBtn.setOnAction(e -> {
            BorrowedBooksPage borrowedBooksPage = new BorrowedBooksPage(stage, user);
            stage.setScene(new Scene(borrowedBooksPage, 700, 450));
        });

        logoutBtn.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(new Scene(loginPage, 400, 300));
        });
    }
}
