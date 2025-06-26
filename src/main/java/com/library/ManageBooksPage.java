package com.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Map;

public class ManageBooksPage extends VBox {

    private TableView<Book> tableView;
    private ObservableList<Book> bookList;
    private LibraryService service;

    public ManageBooksPage(Stage stage, User admin) {
        setPadding(new Insets(15));
        setSpacing(10);

        Label title = new Label("Manage Books");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        service = new LibraryService();
        bookList = FXCollections.observableArrayList(service.getAllBooks().values());

        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(bookList);

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

        TableColumn<Book, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().isAvailable() ? "Available" : "Borrowed"));

        tableView.getColumns().addAll(titleCol, authorCol, genreCol, yearCol, libraryCol, statusCol);

        Button editButton = new Button("Edit Selected");
        Button deleteButton = new Button("Delete Selected");
        Button backButton = new Button("Back");

        editButton.setOnAction(e -> editSelectedBook(stage, admin));
        deleteButton.setOnAction(e -> deleteSelectedBook());
        backButton.setOnAction(e -> {
            AdminDashboard dashboard = new AdminDashboard(stage, admin);
            stage.setScene(new Scene(dashboard, 600, 400));
        });

        HBox buttons = new HBox(10, editButton, deleteButton, backButton);
        getChildren().addAll(title, tableView, buttons);
    }

    private void editSelectedBook(Stage stage, User admin) {
        Book selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a book to edit.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Book Details");

        TextField titleField = new TextField(selected.getTitle());
        TextField authorField = new TextField(selected.getAuthor());
        TextField genreField = new TextField(selected.getGenre());
        TextField yearField = new TextField(String.valueOf(selected.getYear()));
        TextField libraryField = new TextField(selected.getLibrary());
        CheckBox availableCheck = new CheckBox("Available");
        availableCheck.setSelected(selected.isAvailable());

        VBox content = new VBox(10,
                new Label("Title:"), titleField,
                new Label("Author:"), authorField,
                new Label("Genre:"), genreField,
                new Label("Year:"), yearField,
                new Label("Library:"), libraryField,
                availableCheck);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String genre = genreField.getText().trim();
                String yearText = yearField.getText().trim();
                String library = libraryField.getText().trim();
                boolean isAvailable = availableCheck.isSelected();

                if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || yearText.isEmpty() || library.isEmpty()) {
                    showAlert("All fields are required.");
                    return;
                }

                int year;
                try {
                    year = Integer.parseInt(yearText);
                } catch (NumberFormatException ex) {
                    showAlert("Year must be a valid number.");
                    return;
                }

                Book updatedBook = new Book(
                        selected.getBookId(),
                        title,
                        author,
                        genre,
                        year,
                        library);
                updatedBook.setAvailable(isAvailable);

                Map<String, Book> books = service.getAllBooks();
                books.put(updatedBook.getBookId(), updatedBook);
                service.saveBooks(books);
                refreshTable();
            }
        });
    }

    private void deleteSelectedBook() {
        Book selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a book to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Are you sure you want to delete this book?");
        confirm.setContentText(selected.getTitle());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Map<String, Book> books = service.getAllBooks();
                books.remove(selected.getBookId());
                service.saveBooks(books);
                refreshTable();
            }
        });
    }

    private void refreshTable() {
        bookList.setAll(service.getAllBooks().values());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
