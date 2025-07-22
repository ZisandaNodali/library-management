package com.library;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManageBorrowingPage extends BorderPane {
    private TableView<BorrowedBook> tableView;
    private LibraryService libraryService;

    public ManageBorrowingPage(Stage stage, User admin) {
        this.libraryService = new LibraryService();

        // Create the main container
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        // Header section
        Label titleLabel = new Label("📚 Borrowed Books Management");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label subtitleLabel = new Label("Manage book collection and returns");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        Separator separator = new Separator();
        separator.setMaxWidth(400);

        // Create the table view for borrowed books
        tableView = createBorrowedBooksTable();
        refreshTableData();

        // Add search functionality
        HBox searchBox = createSearchBox();

        // Button container
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button returnBtn = new Button("Mark as Returned");
        returnBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        returnBtn.setOnAction(e -> markAsReturned());

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        backBtn.setOnAction(e -> {
            // AdminDashboard dashboard = new AdminDashboard(stage, admin);
            // stage.setScene(new Scene(dashboard));
            // stage.setMaximized(true);
            stage.getScene().setRoot(new AdminDashboard(stage, admin));
        });

        buttonBox.getChildren().addAll(returnBtn, backBtn);

        // Add components to main container
        mainContainer.getChildren().addAll(
                titleLabel, subtitleLabel, separator,
                searchBox, tableView, buttonBox);

        // Set the center of the BorderPane
        this.setCenter(mainContainer);
        this.setStyle("-fx-background-color: #f5f7fa;");
    }

    private TableView<BorrowedBook> createBorrowedBooksTable() {
        TableView<BorrowedBook> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setMaxWidth(1200);

        // Book ID column
        TableColumn<BorrowedBook, String> idCol = new TableColumn<>("Book ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());

        // Title column
        TableColumn<BorrowedBook, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

        // Author column
        TableColumn<BorrowedBook, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(cellData -> cellData.getValue().authorProperty());

        // Borrower Username column (NEW - for debugging)
        TableColumn<BorrowedBook, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cellData -> cellData.getValue().borrowerIdProperty());

        // Borrower Name column
        TableColumn<BorrowedBook, String> borrowerCol = new TableColumn<>("Borrower Name");
        borrowerCol.setCellValueFactory(cellData -> cellData.getValue().borrowerNameProperty());

        // Borrowed Date column
        TableColumn<BorrowedBook, String> borrowedDateCol = new TableColumn<>("Borrowed Date");
        borrowedDateCol.setCellValueFactory(cellData -> cellData.getValue().borrowedDateProperty());

        // Due Date column
        TableColumn<BorrowedBook, String> dueDateCol = new TableColumn<>("Due Date");
        dueDateCol.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());

        // Status column
        TableColumn<BorrowedBook, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        tableView.getColumns().addAll(idCol, titleCol, authorCol, usernameCol, borrowerCol,
                borrowedDateCol, dueDateCol, statusCol);
        return tableView;
    }

    private HBox createSearchBox() {
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by title, author, or borrower...");
        searchField.setPrefWidth(400);

        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        Button clearBtn = new Button("Clear");
        clearBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        searchBtn.setOnAction(e -> {
            String searchTerm = searchField.getText().toLowerCase();
            if (!searchTerm.isEmpty()) {
                List<BorrowedBook> filteredBooks = new ArrayList<>();
                for (BorrowedBook book : loadBorrowedBooks()) {
                    if (book.getTitle().toLowerCase().contains(searchTerm) ||
                            book.getAuthor().toLowerCase().contains(searchTerm) ||
                            book.getBorrowerName().toLowerCase().contains(searchTerm)) {
                        filteredBooks.add(book);
                    }
                }
                tableView.getItems().setAll(filteredBooks);
            }
        });

        clearBtn.setOnAction(e -> {
            searchField.clear();
            refreshTableData();
        });

        searchBox.getChildren().addAll(searchField, searchBtn, clearBtn);
        return searchBox;
    }

    private void refreshTableData() {
        List<BorrowedBook> books = loadBorrowedBooks();
        if (books.isEmpty()) {
            showWarningAlert("No Data", "No borrowed books found in the library data.");
        }
        tableView.getItems().setAll(books);
    }

    private List<BorrowedBook> loadBorrowedBooks() {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();

        try {
            // Get borrowing records from LibraryService
            List<BorrowingRecord> borrowingRecords = libraryService.getAllBorrowingRecords();
            Map<String, Book> allBooks = libraryService.getAllBooks();
            Map<String, User> allUsers = libraryService.getAllUsers();

            System.out.println("Total borrowing records: " + borrowingRecords.size());
            System.out.println("Total books: " + allBooks.size());
            System.out.println("Total users: " + allUsers.size());

            for (BorrowingRecord record : borrowingRecords) {
                // Only show books that haven't been returned yet
                if (record.getReturnDate() == null) {
                    Book book = allBooks.get(record.getBookId());
                    User user = allUsers.get(record.getUsername());

                    System.out.println("Processing record - BookID: " + record.getBookId() +
                            ", Username: " + record.getUsername() +
                            ", Book found: " + (book != null) +
                            ", User found: " + (user != null));

                    if (book != null && user != null) {
                        // Calculate due date (2 weeks from borrow date)
                        String dueDate = calculateDueDate(record.getBorrowDate());

                        BorrowedBook borrowedBook = new BorrowedBook(
                                record.getBookId(),
                                book.getTitle(),
                                book.getAuthor(),
                                record.getUsername(), // Store username as borrower ID
                                user.getName(), // User's full name
                                "BORROWED", // Status
                                record.getBorrowDate(),
                                dueDate);
                        borrowedBooks.add(borrowedBook);
                    } else {
                        if (book == null) {
                            System.out.println("WARNING: Book not found for ID: " + record.getBookId());
                        }
                        if (user == null) {
                            System.out.println("WARNING: User not found for username: " + record.getUsername());
                        }
                    }
                }
            }
        } catch (Exception e) {
            showErrorAlert("Error", "Failed to load borrowed books: " + e.getMessage());
            e.printStackTrace();
        }

        return borrowedBooks;
    }

    private String calculateDueDate(String borrowDate) {
        try {
            // Assuming date format is "MMM dd, yyyy" based on SearchBooksPage
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy");
            java.time.LocalDate borrow = java.time.LocalDate.parse(borrowDate, formatter);
            java.time.LocalDate due = borrow.plusWeeks(2);
            return due.format(formatter);
        } catch (Exception e) {
            return "Unknown";
        }
    }

    // Debug method to help troubleshoot
    private void debugSelected() {
        BorrowedBook selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String debugInfo = "Selected Book Debug Info:\n" +
                    "Book ID: " + selected.getId() + "\n" +
                    "Username (stored): " + selected.getBorrowerId() + "\n" +
                    "Borrower Name: " + selected.getBorrowerName() + "\n" +
                    "Title: " + selected.getTitle();

            // Also check what's actually in the borrowing records
            List<BorrowingRecord> records = libraryService.getAllBorrowingRecords();
            debugInfo += "\n\nMatching records in database:";

            for (BorrowingRecord record : records) {
                if (record.getBookId().equals(selected.getId()) && record.getReturnDate() == null) {
                    debugInfo += "\nFound: BookID=" + record.getBookId() +
                            ", Username=" + record.getUsername() +
                            ", BorrowDate=" + record.getBorrowDate();
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Debug Information");
            alert.setHeaderText(null);
            alert.setContentText(debugInfo);
            alert.showAndWait();
        } else {
            showWarningAlert("No Selection", "Please select a book to debug.");
        }
    }

    private void markAsReturned() {
        BorrowedBook selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Use the username stored in borrowerIdProperty (which should be the username)
            String username = selected.getBorrowerId();
            System.out.println("Attempting to return book - BookID: " + selected.getId() + ", Username: " + username);

            if (updateBorrowingRecord(selected.getId(), username)) {
                showSuccessAlert("Book Returned",
                        "Book '" + selected.getTitle() + "' has been marked as returned.");
                refreshTableData();
            }
        } else {
            showWarningAlert("No Selection", "Please select a book to mark as returned.");
        }
    }

    private boolean updateBorrowingRecord(String bookId, String username) {
        try {
            // Get all data
            List<BorrowingRecord> records = libraryService.getAllBorrowingRecords();
            Map<String, Book> books = libraryService.getAllBooks();

            System.out.println("Looking for record with BookID: " + bookId + " and Username: " + username);

            // Find and update the borrowing record
            boolean found = false;
            for (BorrowingRecord record : records) {
                System.out.println("Checking record - BookID: " + record.getBookId() +
                        ", Username: " + record.getUsername() +
                        ", ReturnDate: " + record.getReturnDate());

                if (record.getBookId().equals(bookId) &&
                        record.getUsername().equals(username) &&
                        record.getReturnDate() == null) {

                    // Set return date to today
                    java.time.LocalDate today = java.time.LocalDate.now();
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                            .ofPattern("MMM dd, yyyy");
                    record.setReturnDate(today.format(formatter));
                    System.out.println("Found and updated record!");
                    found = true;
                    break;
                }
            }

            if (!found) {
                showErrorAlert("Update Failed",
                        "Borrowing record not found for BookID: " + bookId + " and Username: " + username);
                return false;
            }

            // Update book availability
            Book book = books.get(bookId);
            if (book != null) {
                book.setAvailable(true);
                libraryService.updateBook(book);
            }

            // Save all borrowing records back to file
            saveBorrowingRecords(records);

            return true;
        } catch (Exception e) {
            showErrorAlert("Update Failed", "Error updating borrowing record: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void saveBorrowingRecords(List<BorrowingRecord> records) {
        try {
            // Use LibraryService to save - we need to rewrite the entire file
            Map<String, Book> books = libraryService.getAllBooks();
            Map<String, User> users = libraryService.getAllUsers();

            // Create a new LibraryService instance and manually write the file
            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("LibraryData.txt"));

            // Write users section
            writer.write("[USERS]");
            writer.newLine();
            for (User user : users.values()) {
                writer.write(user.getUsername() + "|" + user.getPassword() + "|" +
                        user.getName() + "|" + user.getContact() + "|" + user.isAdmin());
                writer.newLine();
            }

            writer.newLine();
            writer.write("[BOOKS]");
            writer.newLine();
            // Write books section
            for (Book book : books.values()) {
                writer.write(book.getBookId() + "|" + book.getTitle() + "|" + book.getAuthor() + "|" +
                        book.getGenre() + "|" + book.getYear() + "|" + book.getLibrary() + "|" + book.isAvailable());
                writer.newLine();
            }

            writer.newLine();
            writer.write("[BORROWING]");
            writer.newLine();
            // Write borrowing records
            for (BorrowingRecord record : records) {
                writer.write(record.getUsername() + "|" + record.getBookId() + "|" +
                        record.getBorrowDate() + "|" + (record.getReturnDate() != null ? record.getReturnDate() : ""));
                writer.newLine();
            }

            writer.close();
        } catch (java.io.IOException e) {
            showErrorAlert("Save Failed", "Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}