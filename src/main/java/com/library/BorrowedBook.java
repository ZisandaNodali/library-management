package com.library;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BorrowedBook {
    private final StringProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty borrowerId;
    private final StringProperty borrowerName;
    private final StringProperty status;
    private final StringProperty dueDate;
    
    public BorrowedBook(String id, String title, String author, 
                       String borrowerId, String borrowerName, 
                       String status, String dueDate) {
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.borrowerId = new SimpleStringProperty(borrowerId);
        this.borrowerName = new SimpleStringProperty(borrowerName);
        this.status = new SimpleStringProperty(status);
        this.dueDate = new SimpleStringProperty(dueDate);
    }
    
    // Getters
    public String getId() { return id.get(); }
    public String getTitle() { return title.get(); }
    public String getAuthor() { return author.get(); }
    public String getBorrowerId() { return borrowerId.get(); }
    public String getBorrowerName() { return borrowerName.get(); }
    public String getStatus() { return status.get(); }
    public String getDueDate() { return dueDate.get(); }
    
    // Property getters
    public StringProperty idProperty() { return id; }
    public StringProperty titleProperty() { return title; }
    public StringProperty authorProperty() { return author; }
    public StringProperty borrowerIdProperty() { return borrowerId; }
    public StringProperty borrowerNameProperty() { return borrowerName; }
    public StringProperty statusProperty() { return status; }
    public StringProperty dueDateProperty() { return dueDate; }
}
