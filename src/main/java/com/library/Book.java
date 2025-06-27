package com.library;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String status;
    private String genre;
    private int year;
    private String library;
    private boolean available;

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
    }

    // Getters
    public String getBookId() {
        return bookId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

     // Setter
    public void setAvailable(boolean available) {
        this.available = available;
        this.status = available ? "Available" : "Not Available";
    }

    @Override
    public String toString() {
        return "Book ID: " + bookId + "\n"
                + "Title: " + title + "\n"
                + "Author: " + author + "\n"
                + "Genre: " + genre + "\n"
                + "Year: " + year + "\n"
                + "Library: " + library + "\n"
                + "Status: " + status;
    }
}
