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
        topContainer.setBackground(new Background(new BackgroundFill(Color.web("#f4f7f9"), CornerRadii.EMPTY, Insets.EMPTY)));

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

        TableColumn<Book, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Book, String> borrowedCol = new TableColumn<>("Borrowed");
        borrowedCol.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            return new SimpleStringProperty(book.getBorrowedCount() > 0 ? "Yes" : "No");
        });

        TableColumn<Book, String> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            String returnDate = book.getReturnDate() != null ? 
                book.getReturnDate().format(dateFormatter) : "Not borrowed";
            return new SimpleStringProperty(returnDate);
        });

        TableColumn<Book, Void> actionCol = new TableColumn<>("Action");
actionCol.setCellFactory(param -> new TableCell<>() {
    private final Button borrowBtn = new Button("Borrow");

    {
        borrowBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");

        borrowBtn.setOnAction(e -> {
            Book book = getTableView().getItems().get(getIndex());
            
            
            
            if (!book.isAvailable()) {
                showAlert(Alert.AlertType.WARNING, "Unavailable", "This book is not available for borrowing.");
                return;
            }

                    
                    // Check if user has already borrowed this book
                    if (currentUser != null && currentUser.hasBorrowedBook(book.getBookId())) {
                        showAlert(Alert.AlertType.WARNING, "Already Borrowed", 
                            "You have already borrowed this book!\nReturn date: " + 
                            book.getReturnDate().format(dateFormatter));
                        return;
                    }
                    
                    // Set return date (2 weeks from now)
                    LocalDate returnDate = LocalDate.now().plusWeeks(2);
                    book.setReturnDate(returnDate);
                    
                    // Update book status
                    book.setBorrowedCount(book.getBorrowedCount() + 1);
                    book.setAvailable(false);
                    book.setStatus("Not Available");

                    // Update library data and save to persistent storage
                    libraryService.updateBook(book);

                    if (currentUser != null) {
                        currentUser.borrowBook(book.getBookId());
                    }
                    
                    // Refresh the table to show updated status
                    tableView.refresh();
                    
                    // Show success message with return date
                    showAlert(Alert.AlertType.INFORMATION, "Success", 
                        "You have successfully borrowed the book!\n" +
                        "Please return by: " + returnDate.format(dateFormatter));
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Book book = getTableView().getItems().get(getIndex());
                   
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
            UserDashboard dashboard = new UserDashboard(stage, user);
            stage.setScene(new Scene(dashboard, 800, 600));
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
                    if (book.getTitle().toLowerCase().contains(searchTerm)) matches.add(book);
                    break;
                case "Author":
                    if (book.getAuthor().toLowerCase().contains(searchTerm)) matches.add(book);
                    break;
                case "Genre":
                    if (book.getGenre().toLowerCase().contains(searchTerm)) matches.add(book);
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
