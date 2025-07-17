package com.library;

import java.time.LocalDate;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String status;
    private String genre;
    private int year;
    private String library;
    private boolean available;
    private int quantity;
    private int borrowedCount;
     private LocalDate returnDate;
    // Constructor without availability (defaults to true)
    public Book(String bookId, String title, String author, String genre, int year, String library) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.library = library;
        this.available = true;
        this.status = "Available";
        this.quantity = 1; // Default quantity is 1
        this.borrowedCount = 0; // Default borrowed count is 0
    }

    // Constructor with availability
    public Book(String bookId, String title, String author, String genre, int year, String library, boolean available) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.library = library;
        this.available = available;
        this.status = available ? "Available" : "Not Available";
        this.quantity = 1; // Default quantity is 1
        this.borrowedCount = 0; // Default borrowed count is 0
    }

    // Constructor with all parameters including quantity
    public Book(String bookId, String title, String author, String genre, int year, String library, boolean available, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.library = library;
        this.available = available;
        this.status = available ? "Available" : "Not Available";
        this.quantity = quantity;
        this.borrowedCount = 0; // Default borrowed count is 0
    }

    // Getters
    public String getBookId() {
        return bookId;
    }
    
    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public String getLibrary() {
        return library;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    // Getters for calculated values
    public int getAvailableCount() {
        return quantity - borrowedCount;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    public boolean hasAvailableCopies() {
        return borrowedCount < quantity;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    public void setAvailable(boolean available) {
        this.available = available;
        // Only auto-update status if not manually set to specific values
        if (available && !status.equals("Not Available")) {
            this.status = "Available";
        } else if (!available) {
            this.status = "Not Available";
        }
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        // Check if status should be updated based on availability
        updateStatusBasedOnAvailability();
    }

    public void setBorrowedCount(int borrowedCount) {
        this.borrowedCount = borrowedCount;
        // Check if status should be updated based on availability
        updateStatusBasedOnAvailability();
    }

    // Helper method to update status based on availability
    private void updateStatusBasedOnAvailability() {
        if (borrowedCount >= quantity) {
            this.status = "Not Available";
            this.available = false;
        } else if (borrowedCount < quantity && !status.equals("Not Available")) {
            this.status = "Available";
            this.available = true;
        }
    }
    

    // Method to borrow a copy
    public boolean borrowCopy() {
        if (hasAvailableCopies()) {
            borrowedCount++;
            updateStatusBasedOnAvailability();
            return true;
        }
        return false;
    }

    // Method to return a copy
    public boolean returnCopy() {
        if (borrowedCount > 0) {
            borrowedCount--;
            updateStatusBasedOnAvailability();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Book ID: " + bookId + "\n"
                + "Title: " + title + "\n"
                + "Author: " + author + "\n"
                + "Genre: " + genre + "\n"
                + "Year: " + year + "\n"
                + "Library: " + library + "\n"
                + "Status: " + status + "\n"
                + "Quantity: " + quantity + "\n"
                + "Borrowed: " + borrowedCount + "\n"
                + "Available: " + getAvailableCount();
    }
}
