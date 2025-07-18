package com.library;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageBorrowingPage extends BorderPane {
    private static final String FILE_PATH = "LibraryData.txt";
    private TableView<BorrowedBook> tableView;

    public ManageBorrowingPage(Stage stage, User admin) {
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
        
        Button collectBtn = new Button("Mark as Collected");
        collectBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        collectBtn.setOnAction(e -> markAsCollected());
        
        Button returnBtn = new Button("Mark as Returned");
        returnBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        returnBtn.setOnAction(e -> markAsReturned());
        
        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        backBtn.setOnAction(e -> {
            AdminDashboard dashboard = new AdminDashboard(stage, admin);
            stage.setScene(new Scene(dashboard));
            stage.setMaximized(true);
        });
        
        buttonBox.getChildren().addAll(collectBtn, returnBtn, backBtn);
        
        // Add components to main container
        mainContainer.getChildren().addAll(
            titleLabel, subtitleLabel, separator, 
            searchBox, tableView, buttonBox
        );
        
        // Set the center of the BorderPane
        this.setCenter(mainContainer);
        this.setStyle("-fx-background-color: #f5f7fa;");
    }
    
    private TableView<BorrowedBook> createBorrowedBooksTable() {
    TableView<BorrowedBook> tableView = new TableView<>();
    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tableView.setMaxWidth(1000);  // Increased width to accommodate new column
    
    // Book ID column
    TableColumn<BorrowedBook, String> idCol = new TableColumn<>("Book ID");
    idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
    
    // Title column
    TableColumn<BorrowedBook, String> titleCol = new TableColumn<>("Title");
    titleCol.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    
    // Author column
    TableColumn<BorrowedBook, String> authorCol = new TableColumn<>("Author");
    authorCol.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
    
    // Borrower column
    TableColumn<BorrowedBook, String> borrowerCol = new TableColumn<>("Borrower");
    borrowerCol.setCellValueFactory(cellData -> cellData.getValue().borrowerNameProperty());
    
    // Borrowed Date column (NEW)
    TableColumn<BorrowedBook, String> borrowedDateCol = new TableColumn<>("Borrowed Date");
    borrowedDateCol.setCellValueFactory(cellData -> cellData.getValue().borrowedDateProperty());
    
    // Due Date column
    TableColumn<BorrowedBook, String> dueDateCol = new TableColumn<>("Due Date");
    dueDateCol.setCellValueFactory(cellData -> cellData.getValue().dueDateProperty());
    
    // Status column
    TableColumn<BorrowedBook, String> statusCol = new TableColumn<>("Status");
    statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
    
    tableView.getColumns().addAll(idCol, titleCol, authorCol, borrowerCol, 
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
    File file = new File(FILE_PATH);
    
    if (!file.exists()) {
        showErrorAlert("File Not Found", "Library data file not found at: " + file.getAbsolutePath());
        return borrowedBooks;
    }
    
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        boolean inBorrowingSection = false;
        
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            if (line.equals("[BORROWING]")) {
                inBorrowingSection = true;
                continue;
            }
            
            if (inBorrowingSection && line.startsWith("Borrowing Record:")) {
                String record = line.substring("Borrowing Record:".length()).trim();
                Map<String, String> recordMap = parseBorrowingRecord(record);
                
                if (recordMap != null) {
                    BorrowedBook book = new BorrowedBook(
                        recordMap.get("BookID"),
                        recordMap.get("Title"),
                        recordMap.get("Author"),
                        "", // Borrower ID
                        recordMap.get("Username"),
                        recordMap.get("Status"),
                        recordMap.get("BorrowDate"), // Borrowed Date
                        recordMap.get("ReturnDate")  // Due Date
                    );
                    borrowedBooks.add(book);
                }
            }
        }
    } catch (IOException e) {
        showErrorAlert("Error", "Failed to read library data: " + e.getMessage());
        e.printStackTrace();
    }
    
    return borrowedBooks;
}

    private Map<String, String> parseBorrowingRecord(String record) {
    Map<String, String> recordMap = new HashMap<>();
    String[] pairs = record.split(", ");
    
    for (String pair : pairs) {
        String[] keyValue = pair.split("=", 2);
        if (keyValue.length == 2) {
            recordMap.put(keyValue[0].trim(), keyValue[1].trim());
        }
    }
    
    // Verify we have all required fields
    String[] requiredFields = {"BookID", "Title", "Author", "Username", "BorrowDate", "ReturnDate", "Status"};
    for (String field : requiredFields) {
        if (!recordMap.containsKey(field)) {
            System.out.println("Missing field in borrowing record: " + field);
            System.out.println("Complete record: " + record);
            return null;
        }
    }
    
    return recordMap;
}
    
    private void markAsCollected() {
        BorrowedBook selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (updateBookStatus(selected.getId(), "COLLECTED")) {
                showSuccessAlert("Book Collected", 
                    "Book '" + selected.getTitle() + "' has been marked as collected.");
                refreshTableData();
            }
        } else {
            showWarningAlert("No Selection", "Please select a book to mark as collected.");
        }
    }
    
    private void markAsReturned() {
        BorrowedBook selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (updateBookStatus(selected.getId(), "RETURNED")) {
                showSuccessAlert("Book Returned", 
                    "Book '" + selected.getTitle() + "' has been marked as returned.");
                refreshTableData();
            }
        } else {
            showWarningAlert("No Selection", "Please select a book to mark as returned.");
        }
    }
    
    private boolean updateBookStatus(String bookId, String newStatus) {
    File file = new File(FILE_PATH);
    if (!file.exists()) {
        showErrorAlert("File Not Found", "Library data file not found at: " + file.getAbsolutePath());
        return false;
    }

    try {
        // Read all lines from file
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        // Find and update the specific borrowing record
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.startsWith("Borrowing Record:")) {
                // Extract the record part
                String record = line.substring("Borrowing Record:".length()).trim();
                Map<String, String> recordMap = parseBorrowingRecord(record);
                
                if (recordMap != null && recordMap.get("BookID").equals(bookId)) {
                    // Preserve all original values, only change status
                    String updatedRecord = "Borrowing Record: " + 
                        "BookID=" + recordMap.get("BookID") + ", " +
                        "Title=" + recordMap.get("Title") + ", " +
                        "Author=" + recordMap.get("Author") + ", " +
                        "Username=" + recordMap.get("Username") + ", " +
                        "BorrowDate=" + recordMap.get("BorrowDate") + ", " +
                        "ReturnDate=" + recordMap.get("ReturnDate") + ", " +
                        "Status=" + newStatus;
                    
                    lines.set(i, updatedRecord);
                    found = true;
                    break;
                }
            }
        }

        if (found) {
            // Write all lines back to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            return true;
        } else {
            showErrorAlert("Update Failed", "Borrowing record not found for book ID: " + bookId);
            return false;
        }
    } catch (IOException e) {
        showErrorAlert("Update Failed", "Error updating book status: " + e.getMessage());
        e.printStackTrace();
        return false;
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
