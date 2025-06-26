package com.library;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegistrationPage extends VBox {

    public RegistrationPage(Stage stage) {
        setSpacing(10);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Register New User");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        TextField contactField = new TextField();
        contactField.setPromptText("Contact (email/phone)");

        CheckBox adminCheckbox = new CheckBox("Register as Administrator");

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        Label messageLabel = new Label();

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            boolean isAdmin = adminCheckbox.isSelected();

            if (username.isEmpty() || name.isEmpty() || contact.isEmpty()) {
                messageLabel.setText("Please fill in all fields.");
                return;
            }

            String password = PasswordGenerator.generatePassword();
            User newUser = new User(username, password, name, contact, isAdmin);

            LibraryService service = new LibraryService(); // this loads current users
            boolean success = service.registerUser(newUser);

            if (success) {
                messageLabel.setText("Registered " + username + " | Password: " + password);
            } else {
                messageLabel.setText("Username already exists.");
            }
        });

        backButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage);
            stage.setScene(new Scene(loginPage, 400, 300));
        });

        getChildren().addAll(
                titleLabel,
                usernameField,
                nameField,
                contactField,
                adminCheckbox,
                registerButton,
                backButton,
                messageLabel);
    }
}
