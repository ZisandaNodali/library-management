package com.library;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage extends VBox {

    public LoginPage(Stage stage) {
        setSpacing(10);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Login");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            LibraryService service = new LibraryService();
            User user = service.login(username, password);

            if (user != null) {
                messageLabel.setText("Login successful!");
                if (user.isAdmin()) {
                    stage.setScene(new Scene(new AdminDashboard(stage, user), 600, 400));
                } else {
                    stage.setScene(new Scene(new UserDashboard(stage, user), 600, 400));
                }
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        });

        registerButton.setOnAction(e -> {
            stage.setScene(new Scene(new RegistrationPage(stage), 400, 300));
        });

        getChildren().addAll(
                titleLabel,
                usernameField,
                passwordField,
                loginButton,
                registerButton,
                messageLabel);
    }
}
