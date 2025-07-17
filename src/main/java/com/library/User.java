package com.library;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String name;
    private String contact;
    private boolean isAdmin;
    private List<String> borrowedBooks = new ArrayList<>();

    public User(String username, String password, String name, String contact, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.contact = contact;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    public boolean hasBorrowedBook(String bookId) {
        return borrowedBooks.contains(bookId);
    }

    public void borrowBook(String bookId) {
        if (!hasBorrowedBook(bookId)) {
            borrowedBooks.add(bookId);
        }
    }
}
