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

import java.util.*;

public class SearchBooksPage extends BorderPane {

    private final LibraryService libraryService;
    private final TextField searchField;
    private final ComboBox<String> searchTypeBox;
    private final TableView<Book> tableView;
    private final Button viewBtn = new Button("View");
    private final Button borrowBtn = new Button("Borrow");
    private final HBox actionBox = new HBox(10);

    public SearchBooksPage(Stage stage, User user) {
        this.libraryService = new LibraryService();

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

      TableColumn<Book, Void> actionCol = new TableColumn<>("Action");
actionCol.setCellFactory(param -> new TableCell<>() {

    private final Button borrowBtn = new Button("Borrow");

    {
        borrowBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");

        borrowBtn.setOnAction(e -> {
            Book book = getTableView().getItems().get(getIndex());
            if (!book.isAvailable()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "This book is already borrowed.");
                alert.setTitle("Unavailable");
                alert.showAndWait();
                return;
            }

            book.setAvailable(false);
            book.setStatus("Pending");


            Map<String, Book> books = libraryService.getAllBooks();
            books.put(book.getBookId(), book);
            libraryService.saveBooks(books);

            tableView.refresh();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Book borrowed successfully!");
            alert.setTitle("Success");
            alert.showAndWait();
        });
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            HBox buttonBox = new HBox(borrowBtn);
            buttonBox.setAlignment(Pos.CENTER);
            setGraphic(buttonBox);
        }
    }
});

        tableView.getColumns().addAll(titleCol, authorCol, genreCol, yearCol, locationCol, statusCol, actionCol);

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

        tableView.setItems(FXCollections.observableArrayList(libraryService.getAllBooks().values()));


        // === ACTIONS ===
        searchButton.setOnAction(e -> performSearch());
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        String type = searchTypeBox.getValue();

        if (searchTerm.isEmpty()) {
            tableView.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Book> matches = new ArrayList<>();
        for (Book book : libraryService.getAllBooks().values()) {
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
}
