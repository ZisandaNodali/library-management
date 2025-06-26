package com.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.*;

public class BookBorrowPage extends BorderPane {

    private final TableView<Book> tableView = new TableView<>();
    private final ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
    private final LibraryService libraryService = new LibraryService();
    private final User currentUser;

    public BookBorrowPage(Stage stage, User currentUser) {
        this.currentUser = currentUser;

        setPadding(new Insets(15));
        Label titleLabel = new Label("Search Available Books");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox searchBar = createSearchBar();
        VBox topBox = new VBox(10, titleLabel, searchBar);
        topBox.setPadding(new Insets(10));

        setupTable();
        refreshTable();

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            UserDashboard dashboard = new UserDashboard(stage, currentUser);
            stage.setScene(new Scene(dashboard, 600, 400));
        });

        VBox bottomBox = new VBox(10, backButton);
        bottomBox.setPadding(new Insets(10));

        setTop(topBox);
        setCenter(tableView);
        setBottom(bottomBox);
    }

    private HBox createSearchBar() {
        ComboBox<String> filterType = new ComboBox<>();
        filterType.getItems().addAll("Title", "Author", "Genre", "Year");
        filterType.setValue("Title");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter search term");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String filter = filterType.getValue();
            String term = searchField.getText().trim().toLowerCase();
            filterBooks(filter, term);
        });

        return new HBox(10, filterType, searchField, searchButton);
    }

    private void setupTable() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Book, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Book, String> libraryCol = new TableColumn<>("Library");
        libraryCol.setCellValueFactory(new PropertyValueFactory<>("library"));

        tableView.getColumns().addAll(titleCol, authorCol, genreCol, yearCol, libraryCol);
        tableView.setItems(filteredBooks);

        tableView.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Book book = row.getItem();
                    borrowBook(book);
                }
            });
            return row;
        });
    }

    private void filterBooks(String filter, String term) {
        filteredBooks.clear();
        Map<String, Book> allBooks = libraryService.getAllBooks();

        for (Book book : allBooks.values()) {
            if (!book.isAvailable())
                continue;

            boolean matches = switch (filter) {
                case "Title" -> book.getTitle().toLowerCase().contains(term);
                case "Author" -> book.getAuthor().toLowerCase().contains(term);
                case "Genre" -> book.getGenre().toLowerCase().contains(term);
                case "Year" -> String.valueOf(book.getYear()).equals(term);
                default -> false;
            };

            if (matches)
                filteredBooks.add(book);
        }
    }

    private void refreshTable() {
        filterBooks("Title", "");
    }

    private void borrowBook(Book book) {
        if (!book.isAvailable()) {
            showAlert("Book Unavailable", "This book is already borrowed.");
            return;
        }

        // Update availability
        book.setAvailable(false);
        String borrowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        BorrowingRecord record = new BorrowingRecord(currentUser.getUsername(), book.getBookId(), borrowDate, null);

        // Save changes
        libraryService.updateBook(book);
        libraryService.addBorrowingRecord(record);

        showAlert("Success", "You have borrowed: " + book.getTitle());
        refreshTable();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
