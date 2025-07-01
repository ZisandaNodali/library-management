package com.library;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.TranslateTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;



public class LoginPage extends StackPane {

    public LoginPage(Stage stage) {
        // Create modern gradient background with floating elements
        createModernBackground();
        
        // Main container with glassmorphism effect
        VBox mainContainer = createGlassmorphicContainer(stage);
        
        getChildren().add(mainContainer);
    }
    
    private void createModernBackground() {
        // Dynamic gradient background
        LinearGradient backgroundGradient = new LinearGradient(
            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#FF8A65")),
            new Stop(0.3, Color.web("#FF7043")),
            new Stop(0.7, Color.web("#FF5722")),
            new Stop(1, Color.web("#D84315"))
        );
        
        Region background = new Region();
        background.setBackground(new Background(new BackgroundFill(backgroundGradient, null, null)));
        
        // Add floating geometric shapes for modern effect
        StackPane shapesContainer = new StackPane();
        
        // Floating circles with blur effect
        for (int i = 0; i < 5; i++) {
            Circle circle = new Circle();
            circle.setRadius(50 + (i * 20));
            circle.setFill(Color.web("#FFFFFF", 0.1));
            circle.setEffect(new GaussianBlur(15));
            
            // Position circles randomly
            circle.setTranslateX((Math.random() - 0.5) * 800);
            circle.setTranslateY((Math.random() - 0.5) * 600);
            
            // Add floating animation
            TranslateTransition transition = new TranslateTransition(Duration.seconds(8 + i * 2), circle);
            transition.setFromY(circle.getTranslateY());
            transition.setToY(circle.getTranslateY() + (Math.random() * 100 - 50));
            transition.setCycleCount(TranslateTransition.INDEFINITE);
            transition.setAutoReverse(true);
            transition.play();
            
            shapesContainer.getChildren().add(circle);
        }
        
        // Add geometric rectangles
        for (int i = 0; i < 3; i++) {
            Rectangle rect = new Rectangle();
            rect.setWidth(100 + i * 30);
            rect.setHeight(100 + i * 30);
            rect.setFill(Color.web("#FFFFFF", 0.05));
            rect.setArcWidth(20);
            rect.setArcHeight(20);
            rect.setRotate(45 + i * 15);
            rect.setEffect(new GaussianBlur(10));
            
            rect.setTranslateX((Math.random() - 0.5) * 600);
            rect.setTranslateY((Math.random() - 0.5) * 400);
            
            shapesContainer.getChildren().add(rect);
        }
        
        getChildren().addAll(background, shapesContainer);
    }
    
    private VBox createGlassmorphicContainer(Stage stage) {
        VBox container = new VBox();
        container.setAlignment(Pos.CENTER);
        container.setPrefSize(450, 650);
        container.setMaxSize(450, 650);
        container.setPadding(new Insets(40));
        container.setSpacing(30);
        
        // Glassmorphism effect
        container.setBackground(new Background(new BackgroundFill(
            Color.web("#FFFFFF", 0.15), new CornerRadii(25), null)));
        
        // Enhanced shadow and blur effects
        DropShadow glassShadow = new DropShadow();
        glassShadow.setColor(Color.web("#000000", 0.3));
        glassShadow.setRadius(25);
        glassShadow.setOffsetY(10);
        container.setEffect(glassShadow);
        
        // Modern border
        container.setStyle(
            "-fx-border-color: rgba(255,255,255,0.3);" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 25px;" +
            "-fx-background-radius: 25px;"
        );
        
        // Header section with modern logo
        VBox headerSection = createModernHeader();
        
        // Form section
        VBox formSection = createModernForm(stage);
        
        container.getChildren().addAll(headerSection, formSection);
        
        // Entrance animation
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), container);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        
        ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(1), container);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1);
        scaleIn.setToY(1);
        
        fadeIn.play();
        scaleIn.play();
        
        return container;
    }
    
    private VBox createModernHeader() {
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setSpacing(15);
        
        // Modern logo container with gradient background
        StackPane logoContainer = new StackPane();
        logoContainer.setPrefSize(80, 80);
        logoContainer.setMaxSize(80, 80);
        
        // Gradient background for logo
        RadialGradient logoGradient = new RadialGradient(
            0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#FF6B35")),
            new Stop(1, Color.web("#FF8A65"))
        );
        
        Circle logoCircle = new Circle(40);
        logoCircle.setFill(logoGradient);
        logoCircle.setEffect(new DropShadow(10, Color.web("#000000", 0.3)));
        
        // Modern book icon (using text for now)
        Label bookIcon = new Label("📚");
        bookIcon.setStyle("-fx-font-size: 35px;");
        
        logoContainer.getChildren().addAll(logoCircle, bookIcon);
        
        // Modern typography
        VBox titleContainer = new VBox();
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setSpacing(5);
        
        Label libraryLabel = new Label("LIBRARY");
        libraryLabel.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 32));
        libraryLabel.setTextFill(Color.WHITE);
        libraryLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 2, 2);");
        
        Label managementLabel = new Label("MANAGEMENT SYSTEM");
        managementLabel.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 14));
        managementLabel.setTextFill(Color.web("#FFFFFF", 0.9));
        managementLabel.setStyle("-fx-letter-spacing: 2px;");
        
        Label welcomeLabel = new Label("Welcome Back");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        welcomeLabel.setTextFill(Color.web("#FFFFFF", 0.8));
        welcomeLabel.setStyle("-fx-font-style: italic;");
        
        titleContainer.getChildren().addAll(libraryLabel, managementLabel, welcomeLabel);
        
        header.getChildren().addAll(logoContainer, titleContainer);
        return header;
    }
    
    private VBox createModernForm(Stage stage) {
        VBox form = new VBox();
        form.setSpacing(25);
        form.setPadding(new Insets(20, 0, 0, 0));
        
        // Modern username field
        VBox usernameSection = createModernField("Username", "👤", true);
        TextField usernameField = (TextField) ((StackPane) usernameSection.getChildren().get(1)).getChildren().get(0);
        
        // Modern password field
        VBox passwordSection = createModernField("Password", "🔒", false);
        PasswordField passwordField = (PasswordField) ((StackPane) passwordSection.getChildren().get(1)).getChildren().get(0);
        
        // Modern login button
        Button loginButton = createModernButton("Sign In", true);
        
        // Modern register button
        Button registerButton = createModernButton("Create Account", false);
        
        // Modern message label
        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 13));
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setStyle("-fx-padding: 10px;");
        
        // Button actions with modern feedback
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            
            // Add button animation
            ScaleTransition buttonPress = new ScaleTransition(Duration.millis(100), loginButton);
            buttonPress.setFromX(1);
            buttonPress.setFromY(1);
            buttonPress.setToX(0.95);
            buttonPress.setToY(0.95);
            buttonPress.setAutoReverse(true);
            buttonPress.setCycleCount(2);
            buttonPress.play();
            
            if (username.isEmpty() || password.isEmpty()) {
                showMessage(messageLabel, "Please fill in all fields", false);
                return;
            }
            
            LibraryService service = new LibraryService();
            User user = service.login(username, password);
            
            if (user != null) {
                showMessage(messageLabel, "Welcome back! Redirecting...", true);
                // Delay for smooth transition
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> {
                    if (user.isAdmin()) {
                        stage.setScene(new Scene(new AdminDashboard(stage, user), 1000, 700));
                    } else {
                        stage.setScene(new Scene(new UserDashboard(stage, user), 1000, 700));
                    }
                });
                pause.play();
            } else {
                showMessage(messageLabel, "Invalid credentials. Please try again.", false);
            }
        });
        
        registerButton.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.3), form);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(event -> {
                stage.setScene(new Scene(new RegistrationPage(stage), 700, 600));
            });
            fadeOut.play();
        });
        
        // Divider
        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setStyle("-fx-background-color: rgba(255,255,255,0.3);");
        
        form.getChildren().addAll(
            usernameSection,
            passwordSection,
            loginButton,
            divider,
            registerButton,
            messageLabel
        );
        
        return form;
    }
    
    private VBox createModernField(String labelText, String icon, boolean isTextField) {
        VBox fieldContainer = new VBox();
        fieldContainer.setSpacing(8);
        
        // Modern field label
        Label label = new Label(labelText);
        label.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 14));
        label.setTextFill(Color.web("#FFFFFF", 0.9));
        
        // Field container with icon
        StackPane fieldStack = new StackPane();
        
       TextInputControl field;

        if (isTextField) {
            field = new TextField();
        } else {
            field = new PasswordField();
        }
        
        field.setPrefHeight(55);
        field.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 15));
        field.setStyle(
            "-fx-background-color: rgba(255,255,255,0.1);" +
            "-fx-border-color: rgba(255,255,255,0.3);" +
            "-fx-border-width: 1.5px;" +
            "-fx-border-radius: 15px;" +
            "-fx-background-radius: 15px;" +
            "-fx-padding: 15px 50px 15px 20px;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: rgba(255,255,255,0.6);"
        );
        
        // Focus effects
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.2);" +
                    "-fx-border-color: #FF6B35;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-radius: 15px;" +
                    "-fx-background-radius: 15px;" +
                    "-fx-padding: 15px 50px 15px 20px;" +
                    "-fx-text-fill: white;" +
                    "-fx-prompt-text-fill: rgba(255,255,255,0.6);" +
                    "-fx-effect: dropshadow(gaussian, rgba(255,107,53,0.4), 10, 0, 0, 0);"
                );
            } else {
                field.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.1);" +
                    "-fx-border-color: rgba(255,255,255,0.3);" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 15px;" +
                    "-fx-background-radius: 15px;" +
                    "-fx-padding: 15px 50px 15px 20px;" +
                    "-fx-text-fill: white;" +
                    "-fx-prompt-text-fill: rgba(255,255,255,0.6);"
                );
            }
        });
        
        // Icon
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(18));
        iconLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.7);");
        
        fieldStack.getChildren().addAll(field, iconLabel);
        StackPane.setAlignment(iconLabel, Pos.CENTER_RIGHT);
        StackPane.setMargin(iconLabel, new Insets(0, 20, 0, 0));
        
        fieldContainer.getChildren().addAll(label, fieldStack);
        return fieldContainer;
    }
    
    private Button createModernButton(String text, boolean isPrimary) {
        Button button = new Button(text);
        button.setPrefHeight(55);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        
        if (isPrimary) {
            button.setTextFill(Color.WHITE);
            button.setStyle(
                "-fx-background-color: linear-gradient(to right, #FF6B35, #FF8A65);" +
                "-fx-background-radius: 15px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);"
            );
            
            // Hover effects
            button.setOnMouseEntered(e -> {
                button.setStyle(
                    "-fx-background-color: linear-gradient(to right, #E55A2B, #FF7043);" +
                    "-fx-background-radius: 15px;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0, 0, 6);"
                );
            });
            
            button.setOnMouseExited(e -> {
                button.setStyle(
                    "-fx-background-color: linear-gradient(to right, #FF6B35, #FF8A65);" +
                    "-fx-background-radius: 15px;" +
                    "-fx-cursor: hand;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 4);"
                );
            });
        } else {
            button.setTextFill(Color.web("#FFFFFF", 0.9));
            button.setStyle(
                "-fx-background-color: rgba(255,255,255,0.1);" +
                "-fx-border-color: rgba(255,255,255,0.3);" +
                "-fx-border-width: 1.5px;" +
                "-fx-border-radius: 15px;" +
                "-fx-background-radius: 15px;" +
                "-fx-cursor: hand;"
            );
            
            button.setOnMouseEntered(e -> {
                button.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.2);" +
                    "-fx-border-color: rgba(255,255,255,0.5);" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 15px;" +
                    "-fx-background-radius: 15px;" +
                    "-fx-cursor: hand;"
                );
            });
            
            button.setOnMouseExited(e -> {
                button.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.1);" +
                    "-fx-border-color: rgba(255,255,255,0.3);" +
                    "-fx-border-width: 1.5px;" +
                    "-fx-border-radius: 15px;" +
                    "-fx-background-radius: 15px;" +
                    "-fx-cursor: hand;"
                );
            });
        }
        
        return button;
    }
    
    private void showMessage(Label messageLabel, String message, boolean isSuccess) {
        messageLabel.setText(message);
        if (isSuccess) {
            messageLabel.setTextFill(Color.web("#4CAF50"));
            messageLabel.setStyle(
                "-fx-padding: 10px;" +
                "-fx-background-color: rgba(76,175,80,0.1);" +
                "-fx-background-radius: 10px;" +
                "-fx-border-color: rgba(76,175,80,0.3);" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 10px;"
            );
        } else {
            messageLabel.setTextFill(Color.web("#FF5252"));
            messageLabel.setStyle(
                "-fx-padding: 10px;" +
                "-fx-background-color: rgba(255,82,82,0.1);" +
                "-fx-background-radius: 10px;" +
                "-fx-border-color: rgba(255,82,82,0.3);" +
                "-fx-border-width: 1px;" +
                "-fx-border-radius: 10px;"
            );
        }
        
        // Fade in animation for message
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), messageLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
}