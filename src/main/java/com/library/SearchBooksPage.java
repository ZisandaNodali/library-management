package com.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchBooksPage extends VBox {

    private final LibraryService libraryService;
    private final TextField searchField;
    private final ComboBox<String> searchTypeBox;
    private final TextArea resultArea;

    public SearchBooksPage(Stage stage, User user) {
        this.libraryService = new LibraryService(); // fresh load of books/users from file

        setSpacing(10);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER);

        Label title = new Label("Search Books");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        searchField = new TextField();
        searchField.setPromptText("Enter search term");

        searchTypeBox = new ComboBox<>();
        searchTypeBox.setItems(FXCollections.observableArrayList("Title", "Author", "Genre"));
        searchTypeBox.setValue("Title");

        Button searchButton = new Button("Search");
        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(200);

        Button backButton = new Button("Back");

        searchButton.setOnAction(e -> performSearch());
        backButton.setOnAction(e -> {
            UserDashboard dashboard = new UserDashboard(stage, user);
            stage.setScene(new Scene(dashboard, 400, 300));
        });

        getChildren().addAll(title, searchField, searchTypeBox, searchButton, resultArea, backButton);
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        String type = searchTypeBox.getValue();

        if (searchTerm.isEmpty()) {
            resultArea.setText("Please enter a search term.");
            return;
        }

        List<Book> matches = new ArrayList<>();
        for (Book book : libraryService.getAllBooks().values()) {
            switch (type) {
                case "Title":
                    if (book.getTitle().toLowerCase().contains(searchTerm))
                        matches.add(book);
                    break;
                case "Author":
                    if (book.getAuthor().toLowerCase().contains(searchTerm))
                        matches.add(book);
                    break;
                case "Genre":
                    if (book.getGenre().toLowerCase().contains(searchTerm))
                        matches.add(book);
                    break;
            }
        }

        if (matches.isEmpty()) {
            resultArea.setText("No books found.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Book b : matches) {
                sb.append(b).append("\n---\n");
            }
            resultArea.setText(sb.toString());
        }
    }
}
