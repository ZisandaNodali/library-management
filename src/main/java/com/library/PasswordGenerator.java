package com.library;

import java.util.*;

public class PasswordGenerator {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()_+-=[]{}|;:,.<>?";
    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SYMBOLS;

    public static String generatePassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // Ensure at least one of each type
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));

        // Fill rest (e.g. total length 12)
        for (int i = 4; i < 12; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        return shuffle(password.toString());
    }

    private static String shuffle(String input) {
        List<Character> chars = new ArrayList<>();
        for (char c : input.toCharArray())
            chars.add(c);
        Collections.shuffle(chars);
        StringBuilder sb = new StringBuilder();
        for (char c : chars)
            sb.append(c);
        return sb.toString();
    }
}
