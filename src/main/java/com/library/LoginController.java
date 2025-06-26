package com.library;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Map;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    // Load users once
    private Map<String, User> users = LibraryDataHandler.loadUsers();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Login successful! Welcome, " + user.getName());

            // TODO: Open Admin or User dashboard based on user.isAdmin()

        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Invalid username or password!");
        }
    }
}
