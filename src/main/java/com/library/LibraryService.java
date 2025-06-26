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

            // Just preserve structure
            writer.newLine();
            writer.write(BOOKS_SECTION);
            writer.newLine();
            writer.write(BORROWING_SECTION);
            writer.newLine();

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
        // Ideally this should load books from file if not already loaded
        // For now, reuse existing code or load it here if necessary
        Map<String, Book> books = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("LibraryData.txt"))) {
            String line;
            boolean inBooks = false;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("[BOOKS]")) {
                    inBooks = true;
                    continue;
                }
                if (inBooks && (line.trim().equals("[BORROWING]") || line.trim().equals("[USERS]"))) {
                    break;
                }

                if (inBooks && !line.trim().isEmpty()) {
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
                        String returnDate = (parts.length > 3 && !parts[3].equals("null")) ? parts[3] : null;

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
        Map<String, Book> books = getAllBooks(); // Load current books
        books.put(updatedBook.getBookId(), updatedBook);

        // Rewrite entire file including users and books
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
            for (Book book : books.values()) {
                writer.write(book.getBookId() + "|" + book.getTitle() + "|" + book.getAuthor() + "|" +
                        book.getGenre() + "|" + book.getYear() + "|" + book.getLibrary() + "|" + book.isAvailable());
                writer.newLine();
            }

            writer.newLine();
            writer.write(BORROWING_SECTION);
            writer.newLine();

            // Rewriting borrowing section (load + re-save)
            List<BorrowingRecord> allRecords = getAllBorrowingRecords();
            for (BorrowingRecord record : allRecords) {
                writer.write(record.getUsername() + "|" + record.getBookId() + "|" +
                        record.getBorrowDate() + "|" + (record.getReturnDate() != null ? record.getReturnDate() : ""));
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBorrowingRecord(BorrowingRecord record) {
        List<BorrowingRecord> records = getAllBorrowingRecords();
        records.add(record);

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
            for (Book book : getAllBooks().values()) {
                writer.write(book.getBookId() + "|" + book.getTitle() + "|" + book.getAuthor() + "|" +
                        book.getGenre() + "|" + book.getYear() + "|" + book.getLibrary() + "|" + book.isAvailable());
                writer.newLine();
            }

            writer.newLine();
            writer.write(BORROWING_SECTION);
            writer.newLine();
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
        saveBooks(books); // assumes this exists
        return true;
    }

    public void saveBooks(Map<String, Book> books) {
        try {
            File file = new File(DATA_FILE);
            List<String> lines = new ArrayList<>();

            // Read the current contents to preserve [USERS] and [BORROWING]
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                boolean inBooks = false;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().equals(BOOKS_SECTION)) {
                        inBooks = true;
                        lines.add(line); // keep section header
                        // skip existing books
                        while ((line = reader.readLine()) != null && !line.trim().startsWith("[")) {
                            // skip old book lines
                        }
                        if (line != null)
                            lines.add(line); // next section header
                    } else {
                        lines.add(line);
                    }
                }
                reader.close();
            } else {
                lines.add(USERS_SECTION);
                lines.add("");
                lines.add(BOOKS_SECTION);
                lines.add("");
                lines.add(BORROWING_SECTION);
                lines.add("");
            }

            // Insert books into [BOOKS] section
            int insertIndex = lines.indexOf(BOOKS_SECTION) + 1;
            while (insertIndex < lines.size() && !lines.get(insertIndex).startsWith("[")) {
                lines.remove(insertIndex); // remove old book entries
            }

            for (Book book : books.values()) {
                lines.add(insertIndex++, String.join("|",
                        book.getBookId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getGenre(),
                        String.valueOf(book.getYear()),
                        book.getLibrary(),
                        String.valueOf(book.isAvailable())));
            }

            // Write back to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
