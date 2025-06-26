package com.library;

public class BorrowingRecord {
    private String username;
    private String bookId;
    private String borrowDate;
    private String returnDate;

    public BorrowingRecord(String username, String bookId, String borrowDate, String returnDate) {
        this.username = username;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public String getUsername() {
        return username;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
