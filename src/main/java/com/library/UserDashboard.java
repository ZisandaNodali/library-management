package com.library;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class UserDashboard extends StackPane {
    public UserDashboard(Stage stage, User user) {
        createBackgroundDesign();
        Platform.runLater(() -> stage.setMaximized(true));
        
        // Main content container
        VBox mainContent = new VBox(25);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(40));
        mainContent.setMaxWidth(800);

        mainContent.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); " +
                           "-fx-background-radius: 20; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);");

        // Header section
        VBox headerBox = createHeader(user);
        
        // Buttons grid layout
        GridPane buttonsGrid = createButtonsGrid(stage, user);
        
        // Footer
        Label footerLabel = new Label("© 2025 Library Management System - User Portal");
        footerLabel.setStyle("-fx-font-size: 12px; " +
                           "-fx-text-fill: #5a6c7d; " +
                           "-fx-font-style: italic; " +
                           "-fx-font-family: 'Segoe UI', Arial, sans-serif;");

        mainContent.getChildren().addAll(headerBox, buttonsGrid, footerLabel);
        
        // Add main content to stack pane
        getChildren().add(mainContent);
        StackPane.setAlignment(mainContent, Pos.CENTER);
    }

    private void createBackgroundDesign() {
        // Base gradient background
        Rectangle background = new Rectangle();
        background.widthProperty().bind(widthProperty());
        background.heightProperty().bind(heightProperty());
        background.setStyle("-fx-fill: linear-gradient(to bottom right, #667eea, #764ba2, #f093fb);");
        
        // Decorative circles
        for (int i = 0; i < 8; i++) {
            Circle circle = new Circle();
            circle.setRadius(50 + (i * 20));
            circle.setFill(Color.web("#ffffff", 0.1));
            circle.setTranslateX(-200 + (i * 100));
            circle.setTranslateY(-150 + (i * 50));
            getChildren().add(circle);
        }
        
        // Additional decorative elements
        for (int i = 0; i < 5; i++) {
            Rectangle rect = new Rectangle(80, 80);
            rect.setFill(Color.web("#ffffff", 0.08));
            rect.setRotate(45);
            rect.setTranslateX(100 + (i * 120));
            rect.setTranslateY(200 - (i * 80));
            getChildren().add(rect);
        }
        
        getChildren().add(background);
    }

    private VBox createHeader(User user) {
        VBox headerBox = new VBox(15);
        headerBox.setAlignment(Pos.CENTER);
        
        // Main title with book icon effect
        Label titleLabel = new Label("📚 LIBRARY MANAGEMENT SYSTEM");
        titleLabel.setStyle("-fx-font-size: 32px; " +
                          "-fx-font-weight: bold; " +
                          "-fx-text-fill: linear-gradient(to right, #2c3e50, #3498db); " +
                          "-fx-font-family: 'Segoe UI', Arial, sans-serif; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 2);");

        Label welcomeLabel = new Label("Welcome, " + user.getName());
        welcomeLabel.setStyle("-fx-font-size: 20px; " +
                            "-fx-font-weight: 500; " +
                            "-fx-text-fill: #34495e; " +
                            "-fx-font-family: 'Segoe UI', Arial, sans-serif;");

        Label subtitleLabel = new Label("Browse and manage your borrowed books");
        subtitleLabel.setStyle("-fx-font-size: 14px; " +
                             "-fx-text-fill: #7f8c8d; " +
                             "-fx-font-style: italic; " +
                             "-fx-font-family: 'Segoe UI', Arial, sans-serif;");

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: linear-gradient(to right, transparent, #bdc3c7, transparent);");
        separator.setMaxWidth(400);

        headerBox.getChildren().addAll(titleLabel, welcomeLabel, subtitleLabel, separator);
        return headerBox;
    }

    private GridPane createButtonsGrid(Stage stage, User user) {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));
        
        Button searchBooksBtn = createStyledButton("🔍 Search Books", "#3498db", "Search the library collection");
        Button viewBorrowedBtn = createStyledButton("📖 My Borrowed Books", "#27ae60", "View your currently borrowed books");
        Button logoutBtn = createLogoutButton();

        searchBooksBtn.setOnAction(e -> {
            SearchBooksPage searchBooksPage = new SearchBooksPage(stage, user);
            stage.setScene(new Scene(searchBooksPage, 600, 400));
        });

        viewBorrowedBtn.setOnAction(e -> {
            BorrowedBooksPage borrowedBooksPage = new BorrowedBooksPage(stage, user);
            stage.setScene(new Scene(borrowedBooksPage, 700, 450));
        });

        logoutBtn.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(new Scene(loginPage, 400, 300));
        });

        // Arrange buttons in grid (2x1 + logout centered below)
        grid.add(searchBooksBtn, 0, 0);
        grid.add(viewBorrowedBtn, 1, 0);
        
        // Center logout button below
        HBox logoutContainer = new HBox();
        logoutContainer.setAlignment(Pos.CENTER);
        logoutContainer.getChildren().add(logoutBtn);
        grid.add(logoutContainer, 0, 1, 2, 1); // Span 2 columns

        return grid;
    }

    private Button createStyledButton(String text, String color, String description) {
        VBox buttonContainer = new VBox(8);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPrefWidth(200);
        buttonContainer.setPrefHeight(120);
        
        // Main button text
        Label mainText = new Label(text);
        mainText.setStyle("-fx-font-size: 16px; " +
                         "-fx-font-weight: bold; " +
                         "-fx-text-fill: white; " +
                         "-fx-font-family: 'Segoe UI', Arial, sans-serif;");
        
        // Description text
        Label descText = new Label(description);
        descText.setStyle("-fx-font-size: 11px; " +
                         "-fx-text-fill: rgba(255,255,255,0.9); " +
                         "-fx-font-family: 'Segoe UI', Arial, sans-serif; " +
                         "-fx-text-alignment: center; " +
                         "-fx-wrap-text: true;");
        descText.setMaxWidth(180);
        
        buttonContainer.getChildren().addAll(mainText, descText);
        
        Button button = new Button();
        button.setGraphic(buttonContainer);
        button.setPrefWidth(220);
        button.setPrefHeight(140);
        
        // Base styling with glassmorphism effect
        String baseStyle = "-fx-background-color:  rgba(70, 104, 213, 0.8);" +
                          "-fx-background-radius: 15; " +
                          "-fx-border-radius: 15; " +
                          "-fx-border-color: rgba(255,255,255,0.3); " +
                          "-fx-border-width: 1; " +
                          "-fx-cursor: hand; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);";
        
        button.setStyle(baseStyle);

        // Hover effects
        button.setOnMouseEntered(e -> {
            button.setStyle(baseStyle + 
                          "-fx-scale-x: 1.08; " +
                          "-fx-scale-y: 1.08; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0, 0, 6);");
        });

        button.setOnMouseExited(e -> {
            button.setStyle(baseStyle);
        });

        // Press effect
        button.setOnMousePressed(e -> {
            button.setStyle(baseStyle + 
                          "-fx-scale-x: 1.02; " +
                          "-fx-scale-y: 1.02; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 4, 0, 0, 2);");
        });

        button.setOnMouseReleased(e -> {
            button.setStyle(baseStyle + 
                          "-fx-scale-x: 1.08; " +
                          "-fx-scale-y: 1.08; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0, 0, 6);");
        });

        return button;
    }

    private Button createLogoutButton() {
        Button logoutBtn = new Button("🚪 Logout");
        logoutBtn.setPrefWidth(160);
        logoutBtn.setPrefHeight(45);
        
        String logoutStyle = "-fx-font-size: 14px; " +
                           "-fx-font-weight: bold; " +
                           "-fx-text-fill: white; " +
                           "-fx-background-color:  rgba(70, 104, 213, 0.8); " +
                           "-fx-background-radius: 25; " +
                           "-fx-border-radius: 25; " +
                           "-fx-border-width: 1; " +
                           "-fx-cursor: hand; " +
                           "-fx-font-family: 'Segoe UI', Arial, sans-serif; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 0, 3);";
        
        logoutBtn.setStyle(logoutStyle);

        // Logout button hover effects
        logoutBtn.setOnMouseEntered(e -> {
            logoutBtn.setStyle(logoutStyle + 
                             "-fx-scale-x: 1.1; " +
                             "-fx-scale-y: 1.1; " +
                             "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 4);");
        });

        logoutBtn.setOnMouseExited(e -> {
            logoutBtn.setStyle(logoutStyle);
        });

        return logoutBtn;
    }
}
