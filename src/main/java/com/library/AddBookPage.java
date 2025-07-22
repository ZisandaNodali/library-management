package com.library;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.UUID;
import java.util.Map;

public class AddBookPage extends VBox {

    public AddBookPage(Stage stage, User admin) {
        setSpacing(10);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Add New Book");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField titleField = new TextField();
        titleField.setPromptText("Book Title");

        TextField authorField = new TextField();
        authorField.setPromptText("Author");

        TextField genreField = new TextField();
        genreField.setPromptText("Genre");

        TextField yearField = new TextField();
        yearField.setPromptText("Year (e.g., 2023)");

        TextField libraryField = new TextField();
        libraryField.setPromptText("Library Name");

        Label messageLabel = new Label();

        Button saveButton = new Button("Save");
        Button backButton = new Button("Back");

        saveButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            String yearText = yearField.getText().trim();
            String library = libraryField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || yearText.isEmpty() || library.isEmpty()) {
                messageLabel.setText("Please fill in all fields.");
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearText);
            } catch (NumberFormatException ex) {
                messageLabel.setText("Year must be a valid number.");
                return;
            }

            String bookId = UUID.randomUUID().toString();

            Book newBook = new Book(bookId, title, author, genre, year, library, true);

            LibraryService service = new LibraryService();
            Map<String, Book> books = service.getAllBooks();

            // Add new book
            books.put(bookId, newBook);
            service.saveBooks(books);

            messageLabel.setText("Book added successfully!");
            titleField.clear();
            authorField.clear();
            genreField.clear();
            yearField.clear();
            libraryField.clear();
        });

        backButton.setOnAction(e -> {
            // AdminDashboard dashboard = new AdminDashboard(stage, admin);
            // stage.setScene(new Scene(dashboard, 600, 400));
            stage.getScene().setRoot(new AdminDashboard(stage, admin));
        });

        getChildren().addAll(titleLabel, titleField, authorField, genreField, yearField, libraryField, saveButton,
                backButton, messageLabel);
    }
}
