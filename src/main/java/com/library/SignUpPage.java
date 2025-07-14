package com.library;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class SignUpPage extends StackPane {

    public SignUpPage(Stage stage) {
        // Create background image and overlay
        createBackground();

        // Create and add the centered form
        VBox centerForm = createSignUpForm(stage);
        getChildren().add(centerForm);
        StackPane.setAlignment(centerForm, Pos.CENTER);
        StackPane.setMargin(centerForm, new Insets(40));
    }

    private void createBackground() {
        // Load background image (adjust the path as needed)
        Image bgImage = new Image(getClass().getResourceAsStream("/images/librarymanage.jpg"));
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setPreserveRatio(false);
        bgImageView.fitWidthProperty().bind(widthProperty());
        bgImageView.fitHeightProperty().bind(heightProperty());
        bgImageView.setSmooth(true);

        // Warm translucent overlay
        Region overlay = new Region();
        overlay.setStyle("-fx-background-color: rgba(245, 240, 232, 0.6);");
        overlay.prefWidthProperty().bind(widthProperty());
        overlay.prefHeightProperty().bind(heightProperty());

        getChildren().addAll(bgImageView, overlay);
    }

    private VBox createSignUpForm(Stage stage) {
        VBox formContainer = new VBox();
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPrefWidth(400);
        formContainer.setMaxWidth(400);
        formContainer.setSpacing(0);

        VBox formCard = new VBox();
        formCard.setAlignment(Pos.CENTER);
        formCard.setPrefWidth(350);
        formCard.setMaxWidth(350);
        formCard.setPadding(new Insets(40));
        formCard.setSpacing(25);
        formCard.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(15), null)));

        DropShadow cardShadow = new DropShadow();
        cardShadow.setColor(Color.web("#000000", 0.15));
        cardShadow.setRadius(20);
        cardShadow.setOffsetY(8);
        formCard.setEffect(cardShadow);

        VBox headerSection = createHeader();
        VBox fieldsSection = createFormFields(stage);

        formCard.getChildren().addAll(headerSection, fieldsSection);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.8), formCard);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(0.8), formCard);
        scaleIn.setFromX(0.9);
        scaleIn.setFromY(0.9);
        scaleIn.setToX(1);
        scaleIn.setToY(1);

        fadeIn.play();
        scaleIn.play();

        formContainer.getChildren().add(formCard);
        return formContainer;
    }

    private VBox createHeader() {
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setSpacing(15);

        // Logo: stacked colored rectangles as books
        VBox booksStack = new VBox();
        booksStack.setAlignment(Pos.CENTER);
        booksStack.setSpacing(-2);

        Rectangle book1 = new Rectangle(35, 8);
        book1.setFill(Color.web("#FF6B35"));
        book1.setArcWidth(2);
        book1.setArcHeight(2);

        Rectangle book2 = new Rectangle(32, 8);
        book2.setFill(Color.web("#4CAF50"));
        book2.setArcWidth(2);
        book2.setArcHeight(2);

        Rectangle book3 = new Rectangle(38, 8);
        book3.setFill(Color.web("#2196F3"));
        book3.setArcWidth(2);
        book3.setArcHeight(2);

        Rectangle book4 = new Rectangle(30, 8);
        book4.setFill(Color.web("#FF9800"));
        book4.setArcWidth(2);
        book4.setArcHeight(2);

        booksStack.getChildren().addAll(book1, book2, book3, book4);

        VBox titleBox = new VBox();
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setSpacing(2);

        Label libraryLabel = new Label("LIBRARY");
        libraryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        libraryLabel.setTextFill(Color.web("#FF6B35"));

        Label systemLabel = new Label("MANAGEMENT SYSTEM");
        systemLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        systemLabel.setTextFill(Color.web("#666666"));
        systemLabel.setStyle("-fx-letter-spacing: 1px;");

        titleBox.getChildren().addAll(libraryLabel, systemLabel);

        HBox logoSection = new HBox(12, booksStack, titleBox);
        logoSection.setAlignment(Pos.CENTER);

        Label signUpLabel = new Label("Create Your Account");
        signUpLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        signUpLabel.setTextFill(Color.web("#666666"));

        header.getChildren().addAll(logoSection, signUpLabel);
        return header;
    }

    private VBox createFormFields(Stage stage) {
        VBox fieldsContainer = new VBox(18);
        fieldsContainer.setPadding(new Insets(20, 0, 0, 0));

        VBox fullNameSection = createFormField("Full Name");
        TextField fullNameField = (TextField) fullNameSection.getChildren().get(1);

        VBox emailSection = createFormField("Email Address");
        TextField emailField = (TextField) emailSection.getChildren().get(1);

        VBox usernameSection = createFormField("Username");
        TextField usernameField = (TextField) usernameSection.getChildren().get(1);

        VBox passwordSection = createFormField("Password");
        PasswordField passwordField = (PasswordField) passwordSection.getChildren().get(1);

        VBox confirmPasswordSection = createFormField("Confirm Password");
        PasswordField confirmPasswordField = (PasswordField) confirmPasswordSection.getChildren().get(1);

        // Admin checkbox
        CheckBox adminCheckBox = new CheckBox("Register as Admin");
        adminCheckBox.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        adminCheckBox.setTextFill(Color.web("#333333"));

        Button signUpButton = createSignUpButton();

        HBox loginLinkSection = createLoginLink(stage);

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setWrapText(true);

        signUpButton.setOnAction(e -> {
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String confirmPassword = confirmPasswordField.getText().trim();

            if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {
                showMessage(messageLabel, "Please fill in all fields", false);
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                showMessage(messageLabel, "Please enter a valid email address", false);
                return;
            }

            if (password.length() < 6) {
                showMessage(messageLabel, "Password must be at least 6 characters long", false);
                return;
            }

            if (!password.equals(confirmPassword)) {
                showMessage(messageLabel, "Passwords do not match", false);
                return;
            }

            boolean isAdmin = adminCheckBox.isSelected();

            User newUser = new User(username, password, fullName, email, isAdmin);

            LibraryService service = new LibraryService();
            boolean success = service.registerUser(newUser);

            if (success) {
                showMessage(messageLabel, "Account created successfully! Redirecting to login...", true);
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(event -> {
                    stage.setScene(new Scene(new LoginPage(stage), 1000, 700));
                });
                pause.play();
            } else {
                showMessage(messageLabel, "Username or email already exists", false);
            }
        });

        fieldsContainer.getChildren().addAll(
            fullNameSection,
            emailSection,
            usernameSection,
            passwordSection,
            confirmPasswordSection,
            adminCheckBox,      // <-- Added admin checkbox here
            signUpButton,
            loginLinkSection,
            messageLabel
        );

        return fieldsContainer;
    }

    private VBox createFormField(String labelText) {
        VBox fieldContainer = new VBox(8);

        Label label = new Label(labelText);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        label.setTextFill(Color.web("#333333"));

        TextInputControl inputField;
        if (labelText.toLowerCase().contains("password")) {
            inputField = new PasswordField();
        } else {
            inputField = new TextField();
        }

        inputField.setPrefHeight(45);
        inputField.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        inputField.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #DDDDDD;" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 8px;" +
            "-fx-padding: 0 12px;" +
            "-fx-text-fill: #333333;"
        );

        inputField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                inputField.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #FF6B35;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-radius: 8px;" +
                    "-fx-padding: 0 12px;" +
                    "-fx-text-fill: #333333;"
                );
            } else {
                inputField.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #DDDDDD;" +
                    "-fx-border-width: 1px;" +
                    "-fx-border-radius: 8px;" +
                    "-fx-padding: 0 12px;" +
                    "-fx-text-fill: #333333;"
                );
            }
        });

        fieldContainer.getChildren().addAll(label, inputField);
        return fieldContainer;
    }

    private Button createSignUpButton() {
        Button signUpButton = new Button("Create Account");
        signUpButton.setPrefHeight(45);
        signUpButton.setMaxWidth(Double.MAX_VALUE);
        signUpButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        signUpButton.setTextFill(Color.WHITE);
        signUpButton.setStyle(
            "-fx-background-color: #FF6B35;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;"
        );

        signUpButton.setOnMouseEntered(e -> signUpButton.setStyle(
            "-fx-background-color: #E55A2B;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;"));

        signUpButton.setOnMouseExited(e -> signUpButton.setStyle(
            "-fx-background-color: #FF6B35;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;"));

        return signUpButton;
    }

    private HBox createLoginLink(Stage stage) {
        HBox loginLinkContainer = new HBox(5);
        loginLinkContainer.setAlignment(Pos.CENTER);

        Label alreadyHaveLabel = new Label("Already have an account?");
        alreadyHaveLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        alreadyHaveLabel.setTextFill(Color.web("#666666"));

        Label loginLink = new Label("Login here");
        loginLink.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        loginLink.setTextFill(Color.web("#FF6B35"));
        loginLink.setStyle("-fx-cursor: hand;");

        loginLink.setOnMouseEntered(e -> loginLink.setStyle("-fx-cursor: hand; -fx-underline: true;"));
        loginLink.setOnMouseExited(e -> loginLink.setStyle("-fx-cursor: hand;"));

        loginLink.setOnMouseClicked(e -> stage.setScene(new Scene(new LoginPage(stage), 1000, 700)));

        loginLinkContainer.getChildren().addAll(alreadyHaveLabel, loginLink);
        return loginLinkContainer;
    }

    private void showMessage(Label messageLabel, String message, boolean isSuccess) {
        messageLabel.setText(message);
        messageLabel.setTextFill(isSuccess ? Color.web("#4CAF50") : Color.web("#FF5252"));
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), messageLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
}
