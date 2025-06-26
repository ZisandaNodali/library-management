package com.library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LibraryDataHandler {
    private static final String DATA_FILE = "LibraryData.txt";
    private static final String USERS_SECTION = "[USERS]";

    public static Map<String, User> loadUsers() {
        Map<String, User> users = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            boolean readingUsers = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.equals(USERS_SECTION)) {
                    readingUsers = true;
                    continue;
                } else if (line.startsWith("[") && !line.equals(USERS_SECTION)) {
                    readingUsers = false;
                }

                if (readingUsers) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 5) {
                        User user = new User(parts[0], parts[1], parts[2], parts[3], Boolean.parseBoolean(parts[4]));
                        users.put(user.getUsername(), user);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
            // Optionally, add a default admin if file missing or error
            users.put("admin", new User("admin", "admin123", "System Administrator", "admin@library.com", true));
        }
        return users;
    }
}