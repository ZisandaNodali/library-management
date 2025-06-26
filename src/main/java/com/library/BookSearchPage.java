package com.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class BookSearchPage extends VBox {

    public BookSearchPage(Stage stage, User currentUser) {
        setSpacing(10);
        setPadding(new Insets(20));

        Label titleLabel = new Label("Search Available Books");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> searchTypeBox = new ComboBox<>();
        searchTypeBox.getItems().addAll("Title", "Author", "Genre", "Year");
        searchTypeBox.setPromptText("Select Search Type");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter search term");

        Button searchButton = new Button("Search");

        TableView<Book> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAuthor()));

        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGenre()));

        TableColumn<Book, String> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getYear())));

        tableView.getColumns().addAll(titleCol, authorCol, genreCol, yearCol);

        searchButton.setOnAction(e -> {
            String searchType = searchTypeBox.getValue();
            String term = searchField.getText().trim().toLowerCase();

            if (searchType == null || term.isEmpty()) {
                return;
            }

            LibraryService service = new LibraryService();
            Map<String, Book> books = service.getAllBooks();
            ObservableList<Book> results = FXCollections.observableArrayList();

            for (Book book : books.values()) {
                if (!book.isAvailable())
                    continue; // Only available books

                boolean matches = switch (searchType) {
                    case "Title" -> book.getTitle().toLowerCase().contains(term);
                    case "Author" -> book.getAuthor().toLowerCase().contains(term);
                    case "Genre" -> book.getGenre().toLowerCase().contains(term);
                    case "Year" -> String.valueOf(book.getYear()).equals(term);
                    default -> false;
                };

                if (matches) {
                    results.add(book);
                }
            }

            tableView.setItems(results);
        });

        tableView.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Book selectedBook = row.getItem();
                    showBookDetails(selectedBook);
                }
            });
            return row;
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            UserDashboard dashboard = new UserDashboard(stage, currentUser);
            stage.setScene(new Scene(dashboard, 600, 400));
        });

        getChildren().addAll(
                titleLabel,
                new HBox(10, searchTypeBox, searchField, searchButton),
                tableView,
                backButton);
    }

    private void showBookDetails(Book book) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Book Details");
        alert.setHeaderText(book.getTitle());
        alert.setContentText("Author: " + book.getAuthor() +
                "\nGenre: " + book.getGenre() +
                "\nYear: " + book.getYear() +
                "\nLibrary: " + book.getLibrary());
        alert.showAndWait();
    }
}
