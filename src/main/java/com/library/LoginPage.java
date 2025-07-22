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

public class LoginPage extends StackPane {

    public LoginPage(Stage stage) {
        // Create background image filling window
        createBackground();

        // Create login form centered
        VBox loginForm = createLoginForm(stage);
        getChildren().add(loginForm);
        StackPane.setAlignment(loginForm, Pos.CENTER);
    }

    private void createBackground() {
        Image bgImage = new Image(getClass().getResource("/images/library.jpg").toExternalForm());
        ImageView bgView = new ImageView(bgImage);

        bgView.setPreserveRatio(false);
        bgView.fitWidthProperty().bind(this.widthProperty());
        bgView.fitHeightProperty().bind(this.heightProperty());

        getChildren().add(bgView);
        bgView.toBack();
    }

    private VBox createLoginForm(Stage stage) {
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

        // Entrance animations
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

        HBox logoSection = new HBox();
        logoSection.setAlignment(Pos.CENTER);
        logoSection.setSpacing(12);

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

        logoSection.getChildren().addAll(booksStack, titleBox);

        header.getChildren().add(logoSection);
        return header;
    }

    private VBox createFormFields(Stage stage) {
        VBox fieldsContainer = new VBox();
        fieldsContainer.setSpacing(20);
        fieldsContainer.setPadding(new Insets(20, 0, 0, 0));

        VBox usernameSection = createFormField("User Name", "👤");
        TextField usernameField = (TextField) usernameSection.getChildren().get(1);

        VBox passwordSection = createFormField("Password", "🔒");
        PasswordField passwordField = (PasswordField) passwordSection.getChildren().get(1);

        Button loginButton = createLoginButton();

        Label messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setWrapText(true);

        HBox signupSection = createSignupSection(stage);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                showMessage(messageLabel, "Please fill in all fields", false);
                return;
            }

            LibraryService service = new LibraryService();
            User user = service.login(username, password);

            if (user != null) {
                showMessage(messageLabel, "Login successful! Redirecting...", true);
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> {
                    if (user.isAdmin()) {
                        // stage.setScene(new Scene(new AdminDashboard(stage, user), 1000, 700));
                        stage.getScene().setRoot(new AdminDashboard(stage, user));
                    } else {
                        // stage.setScene(new Scene(new UserDashboard(stage, user), 1000, 700));
                        stage.getScene().setRoot(new UserDashboard(stage, user));
                    }
                });
                pause.play();
            } else {
                showMessage(messageLabel, "Invalid username or password", false);
            }
        });

        fieldsContainer.getChildren().addAll(
                usernameSection,
                passwordSection,
                loginButton,
                messageLabel,
                signupSection);

        return fieldsContainer;
    }

    private HBox createSignupSection(Stage stage) {
        HBox signupSection = new HBox();
        signupSection.setAlignment(Pos.CENTER);
        signupSection.setSpacing(5);
        signupSection.setPadding(new Insets(15, 0, 0, 0));

        Label questionLabel = new Label("Don't have an account?");
        questionLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        questionLabel.setTextFill(Color.web("#666666"));

        Hyperlink signupLink = new Hyperlink("Sign up");
        signupLink.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        signupLink.setTextFill(Color.web("#FF6B35"));
        signupLink.setUnderline(true);
        signupLink.setStyle(
                "-fx-padding: 0;" +
                        "-fx-border-color: transparent;" +
                        "-fx-background-color: transparent;");

        signupLink.setOnMouseEntered(e -> signupLink.setTextFill(Color.web("#E55A2B")));
        signupLink.setOnMouseExited(e -> signupLink.setTextFill(Color.web("#FF6B35")));

        signupLink.setOnAction(e -> {
            // stage.setScene(new Scene(new SignUpPage(stage), 1000, 700));
            stage.getScene().setRoot(new SignUpPage(stage));
        });

        signupSection.getChildren().addAll(questionLabel, signupLink);
        return signupSection;
    }

    private VBox createFormField(String labelText, String icon) {
        VBox fieldContainer = new VBox();
        fieldContainer.setSpacing(8);

        Label label = new Label(labelText);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        label.setTextFill(Color.web("#333333"));

        TextInputControl inputField;
        if (labelText.equals("Password")) {
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
                        "-fx-text-fill: #333333;");

        inputField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                inputField.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-border-color: #FF6B35;" +
                                "-fx-border-width: 2px;" +
                                "-fx-border-radius: 8px;" +
                                "-fx-padding: 0 12px;" +
                                "-fx-text-fill: #333333;");
            } else {
                inputField.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-border-color: #DDDDDD;" +
                                "-fx-border-width: 1px;" +
                                "-fx-border-radius: 8px;" +
                                "-fx-padding: 0 12px;" +
                                "-fx-text-fill: #333333;");
            }
        });

        fieldContainer.getChildren().addAll(label, inputField);
        return fieldContainer;
    }

    private Button createLoginButton() {
        Button loginButton = new Button("Login");
        loginButton.setPrefHeight(45);
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        loginButton.setTextFill(Color.WHITE);
        loginButton.setStyle(
                "-fx-background-color: #FF6B35;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;");

        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
                "-fx-background-color: #E55A2B;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;"));

        loginButton.setOnMouseExited(e -> loginButton.setStyle(
                "-fx-background-color: #FF6B35;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-cursor: hand;"));

        return loginButton;
    }

    private void showMessage(Label messageLabel, String message, boolean isSuccess) {
        messageLabel.setText(message);
        if (isSuccess) {
            messageLabel.setTextFill(Color.web("#4CAF50"));
        } else {
            messageLabel.setTextFill(Color.web("#FF5252"));
        }

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), messageLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
}
