package com.library;

import java.io.*;
import java.util.*;

public class LibraryService {
    private static final String DATA_FILE = "LibraryData.txt";
    private static final String USERS_SECTION = "[USERS]";
    private static final String BOOKS_SECTION = "[BOOKS]";
    private static final String BORROWING_SECTION = "[BORROWING]";

    private Map<String, User> users = new HashMap<>();

    public LibraryService() {
        loadUsers();
    }

    public boolean registerUser(User user) {
        if (users.containsKey(user.getUsername())) {
            return false; // already exists
        }
        users.put(user.getUsername(), user);
        saveUsers(); // update file
        return true;
    }

    public boolean validateLogin(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public void loadUsers() {
        try {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            boolean inUserSection = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equals(USERS_SECTION)) {
                    inUserSection = true;
                    continue;
                } else if (line.equals(BOOKS_SECTION) || line.equals(BORROWING_SECTION)) {
                    break;
                }

                if (inUserSection && !line.isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 5) {
                        User user = new User(parts[0], parts[1], parts[2], parts[3], Boolean.parseBoolean(parts[4]));
                        users.put(parts[0], user);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUsers() {
        // Read existing file content to preserve books and borrowing sections
        Map<String, Book> existingBooks = getAllBooks();
        List<BorrowingRecord> existingRecords = getAllBorrowingRecords();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE));

            // Save user section
            writer.write(USERS_SECTION);
            writer.newLine();
            for (User user : users.values()) {
                writer.write(user.getUsername() + "|" + user.getPassword() + "|" +
                        user.getName() + "|" + user.getContact() + "|" + user.isAdmin());
                writer.newLine();
            }

            writer.newLine();
            writer.write(BOOKS_SECTION);
            writer.newLine();
            // Preserve existing books
            for (Book book : existingBooks.values()) {
                writer.write(book.getBookId() + "|" + book.getTitle() + "|" + book.getAuthor() + "|" +
                        book.getGenre() + "|" + book.getYear() + "|" + book.getLibrary() + "|" + book.isAvailable());
                writer.newLine();
            }

            writer.newLine();
            writer.write(BORROWING_SECTION);
            writer.newLine();
            // Preserve existing borrowing records
            for (BorrowingRecord record : existingRecords) {
                writer.write(record.getUsername() + "|" + record.getBookId() + "|" +
                        record.getBorrowDate() + "|" + (record.getReturnDate() != null ? record.getReturnDate() : ""));
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, User> getAllUsers() {
        return users;
    }

    public User login(String username, String password) {
        User user = users.get(username); // Use the class-level map
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public Map<String, Book> getAllBooks() {
        Map<String, Book> books = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            boolean inBooks = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equals(BOOKS_SECTION)) {
                    inBooks = true;
                    continue;
                }
                if (inBooks && (line.equals(BORROWING_SECTION) || line.equals(USERS_SECTION))) {
                    break;
                }

                if (inBooks && !line.isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 7) {
                        Book book = new Book(parts[0], parts[1], parts[2], parts[3], Integer.parseInt(parts[4]),
                                parts[5]);
                        book.setAvailable(Boolean.parseBoolean(parts[6]));
                        books.put(parts[0], book);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return books;
    }

    public List<BorrowingRecord> getAllBorrowingRecords() {
        List<BorrowingRecord> borrowingRecords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            boolean inBorrowingSection = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.equals(BORROWING_SECTION)) {
                    inBorrowingSection = true;
                    continue;
                }
                if (inBorrowingSection && (line.equals(USERS_SECTION) || line.equals(BOOKS_SECTION))) {
                    break; // End of borrowing section
                }

                if (inBorrowingSection && !line.isEmpty()) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 3) {
                        String username = parts[0];
                        String bookId = parts[1];
                        String borrowDate = parts[2];
                        String returnDate = (parts.length > 3 && !parts[3].equals("null") && !parts[3].trim().isEmpty())
                                ? parts[3]
                                : null;

                        BorrowingRecord record = new BorrowingRecord(username, bookId, borrowDate, returnDate);
                        borrowingRecords.add(record);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return borrowingRecords;
    }

    public void updateBook(Book updatedBook) {
        // Get all existing data
        Map<String, Book> books = getAllBooks();
        List<BorrowingRecord> borrowingRecords = getAllBorrowingRecords();

        // Update the specific book
        books.put(updatedBook.getBookId(), updatedBook);

        // Rewrite the entire file with all data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            // Write users
            writer.write(USERS_SECTION);
            writer.newLine();
            for (User user : users.values()) {
                writer.write(user.getUsername() + "|" + user.getPassword() + "|" +
                        user.getName() + "|" + user.getContact() + "|" + user.isAdmin());
                writer.newLine();
            }

            writer.newLine();
            writer.write(BOOKS_SECTION);
            writer.newLine();
            // Write all books (including the updated one)
            for (Book book : books.values()) {
                writer.write(book.getBookId() + "|" + book.getTitle() + "|" + book.getAuthor() + "|" +
                        book.getGenre() + "|" + book.getYear() + "|" + book.getLibrary() + "|" + book.isAvailable());
                writer.newLine();
            }

            writer.newLine();
            writer.write(BORROWING_SECTION);
            writer.newLine();
            // Write all borrowing records
            for (BorrowingRecord record : borrowingRecords) {
                writer.write(record.getUsername() + "|" + record.getBookId() + "|" +
                        record.getBorrowDate() + "|" + (record.getReturnDate() != null ? record.getReturnDate() : ""));
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBorrowingRecord(BorrowingRecord newRecord) {
        // Get all existing data
        Map<String, Book> books = getAllBooks();
        List<BorrowingRecord> records = getAllBorrowingRecords();

        // Add the new record
        records.add(newRecord);

        // Rewrite the entire file with all data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            // Write users
            writer.write(USERS_SECTION);
            writer.newLine();
            for (User user : users.values()) {
                writer.write(user.getUsername() + "|" + user.getPassword() + "|" +
                        user.getName() + "|" + user.getContact() + "|" + user.isAdmin());
                writer.newLine();
            }

            writer.newLine();
            writer.write(BOOKS_SECTION);
            writer.newLine();
            // Write all books
            for (Book book : books.values()) {
                writer.write(book.getBookId() + "|" + book.getTitle() + "|" + book.getAuthor() + "|" +
                        book.getGenre() + "|" + book.getYear() + "|" + book.getLibrary() + "|" + book.isAvailable());
                writer.newLine();
            }

            writer.newLine();
            writer.write(BORROWING_SECTION);
            writer.newLine();
            // Write all borrowing records (including the new one)
            for (BorrowingRecord r : records) {
                writer.write(r.getUsername() + "|" + r.getBookId() + "|" +
                        r.getBorrowDate() + "|" + (r.getReturnDate() != null ? r.getReturnDate() : ""));
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addBook(Book book) {
        Map<String, Book> books = getAllBooks();
        if (books.containsKey(book.getBookId()))
            return false;

        books.put(book.getBookId(), book);
        saveBooks(books);
        return true;
    }

    public void saveBooks(Map<String, Book> books) {
        // Get existing data
        List<BorrowingRecord> existingRecords = getAllBorrowingRecords();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            // Write users
            writer.write(USERS_SECTION);
            writer.newLine();
            for (User user : users.values()) {
                writer.write(user.getUsername() + "|" + user.getPassword() + "|" +
                        user.getName() + "|" + user.getContact() + "|" + user.isAdmin());
                writer.newLine();
            }

            writer.newLine();
            writer.write(BOOKS_SECTION);
            writer.newLine();
            // Write all books
            for (Book book : books.values()) {
                writer.write(book.getBookId() + "|" + book.getTitle() + "|" + book.getAuthor() + "|" +
                        book.getGenre() + "|" + book.getYear() + "|" + book.getLibrary() + "|" + book.isAvailable());
                writer.newLine();
            }

            writer.newLine();
            writer.write(BORROWING_SECTION);
            writer.newLine();
            // Preserve existing borrowing records
            for (BorrowingRecord record : existingRecords) {
                writer.write(record.getUsername() + "|" + record.getBookId() + "|" +
                        record.getBorrowDate() + "|" + (record.getReturnDate() != null ? record.getReturnDate() : ""));
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}