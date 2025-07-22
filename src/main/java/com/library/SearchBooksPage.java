package com.library;

import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

public class SearchBooksPage extends BorderPane {

    private final LibraryService libraryService;
    private final TextField searchField;
    private final ComboBox<String> searchTypeBox;
    private final TableView<Book> tableView;
    private final User currentUser;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    public SearchBooksPage(Stage stage, User user) {
        this.libraryService = new LibraryService();
        this.currentUser = user;

        // === TOP ===
        VBox topContainer = new VBox(10);
        topContainer.setPadding(new Insets(20));
        topContainer.setBackground(
                new Background(new BackgroundFill(Color.web("#f4f7f9"), CornerRadii.EMPTY, Insets.EMPTY)));

        Label title = new Label("Search Books");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label subtitle = new Label("Find your next read in our library collection.");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");

        HBox searchBarContainer = new HBox(10);
        searchBarContainer.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefHeight(30);

        searchTypeBox = new ComboBox<>();
        searchTypeBox.setItems(FXCollections.observableArrayList("Title", "Author", "Genre"));
        searchTypeBox.setValue("Title");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        searchBarContainer.getChildren().addAll(searchField, searchTypeBox, searchButton);
        topContainer.getChildren().addAll(title, subtitle, searchBarContainer);

        // === CENTER ===
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(350);

        TableColumn<Book, String> titleCol = new TableColumn<>("Book Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Book, String> yearCol = new TableColumn<>("Book Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Book, String> locationCol = new TableColumn<>("Library Name");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("library"));

        // Fixed Status Column
        TableColumn<Book, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            String status = book.isAvailable() ? "Available" : "Not Available";
            return new SimpleStringProperty(status);
        });

        // Fixed Borrowed Column
        TableColumn<Book, String> borrowedCol = new TableColumn<>("Borrowed");
        borrowedCol.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            String borrowed = book.isAvailable() ? "No" : "Yes";
            return new SimpleStringProperty(borrowed);
        });

        // Fixed Return Date Column
        TableColumn<Book, String> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            if (book.isAvailable()) {
                return new SimpleStringProperty("Not borrowed");
            } else {
                // Get return date from borrowing records
                String returnDate = getBorrowingReturnDate(book.getBookId());
                return new SimpleStringProperty(returnDate != null ? returnDate : "Unknown");
            }
        });

        TableColumn<Book, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button borrowBtn = new Button("Borrow");

            {
                borrowBtn.setStyle(
                        "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");

                borrowBtn.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());

                    if (!book.isAvailable()) {
                        showAlert(Alert.AlertType.WARNING, "Unavailable", "This book is not available for borrowing.");
                        return;
                    }

                    // Check if user has already borrowed this book
                    if (currentUser != null && hasUserBorrowedBook(currentUser.getUsername(), book.getBookId())) {
                        showAlert(Alert.AlertType.WARNING, "Already Borrowed",
                                "You have already borrowed this book!");
                        return;
                    }

                    // Calculate return date (2 weeks from now)
                    LocalDate returnDate = LocalDate.now().plusWeeks(2);
                    String borrowDate = LocalDate.now().format(dateFormatter);
                    String returnDateStr = returnDate.format(dateFormatter);

                    // Update book status
                    book.setAvailable(false);

                    // Update library data
                    libraryService.updateBook(book);

                    // Add borrowing record
                    BorrowingRecord record = new BorrowingRecord(
                            currentUser.getUsername(),
                            book.getBookId(),
                            borrowDate,
                            null // null means not returned yet
                    );
                    libraryService.addBorrowingRecord(record);

                    // Refresh the table to show updated status
                    refreshTableData();

                    // Show success message with return date
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "You have successfully borrowed the book!\n" +
                                    "Book: " + book.getTitle() + " by " + book.getAuthor() + "\n" +
                                    "Please return by: " + returnDateStr);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Book book = getTableView().getItems().get(getIndex());
                    borrowBtn.setDisable(!book.isAvailable());
                    if (!book.isAvailable()) {
                        borrowBtn.setText("Unavailable");
                        borrowBtn.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-size: 12px;");
                    } else {
                        borrowBtn.setText("Borrow");
                        borrowBtn.setStyle(
                                "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
                    }
                    setGraphic(borrowBtn);
                }
            }
        });

        tableView.getColumns().addAll(titleCol, authorCol, genreCol, yearCol, locationCol,
                statusCol, borrowedCol, returnDateCol, actionCol);

        // === BOTTOM ===
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setOnAction(e -> {
            // UserDashboard dashboard = new UserDashboard(stage, user);
            // stage.setScene(new Scene(dashboard, 800, 600));
            stage.getScene().setRoot(new UserDashboard(stage, user));
        });

        HBox bottomBox = new HBox(backButton);
        bottomBox.setPadding(new Insets(15));
        bottomBox.setAlignment(Pos.BOTTOM_LEFT);

        // === SET LAYOUT ===
        setTop(topContainer);
        setCenter(tableView);
        setBottom(bottomBox);

        // Load initial data
        refreshTableData();

        // === ACTIONS ===
        searchButton.setOnAction(e -> performSearch());
    }

    // Helper method to get return date from borrowing records
    private String getBorrowingReturnDate(String bookId) {
        List<BorrowingRecord> records = libraryService.getAllBorrowingRecords();
        for (BorrowingRecord record : records) {
            if (record.getBookId().equals(bookId) && record.getReturnDate() == null) {
                // Calculate expected return date (2 weeks from borrow date)
                try {
                    LocalDate borrowDate = LocalDate.parse(record.getBorrowDate(), dateFormatter);
                    LocalDate expectedReturn = borrowDate.plusWeeks(2);
                    return expectedReturn.format(dateFormatter);
                } catch (Exception e) {
                    return "Unknown";
                }
            }
        }
        return null;
    }

    // Helper method to check if user has already borrowed a book
    private boolean hasUserBorrowedBook(String username, String bookId) {
        List<BorrowingRecord> records = libraryService.getAllBorrowingRecords();
        for (BorrowingRecord record : records) {
            if (record.getUsername().equals(username) &&
                    record.getBookId().equals(bookId) &&
                    record.getReturnDate() == null) {
                return true; // User has this book and hasn't returned it
            }
        }
        return false;
    }

    private void refreshTableData() {
        tableView.setItems(FXCollections.observableArrayList(libraryService.getAllBooks().values()));
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        String type = searchTypeBox.getValue();

        if (searchTerm.isEmpty()) {
            refreshTableData();
            return;
        }

        List<Book> matches = new ArrayList<>();
        Map<String, Book> allBooks = libraryService.getAllBooks();

        for (Book book : allBooks.values()) {
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

        tableView.setItems(FXCollections.observableArrayList(matches));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.showAndWait();
    }
}