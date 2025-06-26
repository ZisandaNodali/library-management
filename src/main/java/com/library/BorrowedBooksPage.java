package com.library;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class BorrowedBooksPage extends BorderPane {

    public BorrowedBooksPage(Stage stage, User currentUser) {
        setPadding(new Insets(20));

        Label titleLabel = new Label("My Borrowed Books");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<BorrowingRecord> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<BorrowingRecord, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> {
            Book book = getBookById(data.getValue().getBookId());
            String title = (book != null) ? book.getTitle() : "Unknown";
            return new SimpleStringProperty(title);
        });

        TableColumn<BorrowingRecord, String> borrowDateCol = new TableColumn<>("Borrow Date");
        borrowDateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBorrowDate()));

        TableColumn<BorrowingRecord, String> returnDateCol = new TableColumn<>("Return Date");
        returnDateCol.setCellValueFactory(data -> {
            String returnDate = data.getValue().getReturnDate();
            return new SimpleStringProperty(returnDate != null ? returnDate : "Not returned");
        });

        tableView.getColumns().addAll(titleCol, borrowDateCol, returnDateCol);

        ObservableList<BorrowingRecord> userRecords = FXCollections.observableArrayList();
        LibraryService service = new LibraryService();
        List<BorrowingRecord> allRecords = service.getAllBorrowingRecords();

        for (BorrowingRecord record : allRecords) {
            if (record.getUsername().equals(currentUser.getUsername())) {
                userRecords.add(record);
            }
        }

        tableView.setItems(userRecords);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            UserDashboard dashboard = new UserDashboard(stage, currentUser);
            stage.setScene(new Scene(dashboard, 600, 400));
        });

        VBox topBox = new VBox(10, titleLabel);
        topBox.setPadding(new Insets(10));
        setTop(topBox);
        setCenter(tableView);
        setBottom(new VBox(backButton));
    }

    private Book getBookById(String bookId) {
        LibraryService service = new LibraryService();
        Map<String, Book> books = service.getAllBooks();
        return books.get(bookId);
    }
}
