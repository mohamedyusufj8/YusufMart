package com.yusuf.yusufmart.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility for password hashing and verification using jBCrypt.
 * Strictly adheres to rule: Passwords hashed with bcrypt (jBCrypt). No plaintext storage.
 */
public class PasswordUtil {

    private PasswordUtil() {
        // Prevent instantiation
    }

    /**
     * Hashes a plaintext password using BCrypt with log rounds 10.
     *
     * @param plainTextPassword the password in plaintext
     * @return the bcrypt hashed password string
     */
    public static String hash(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(10));
    }

    /**
     * Verifies a candidate plaintext password against an existing BCrypt hash.
     *
     * @param plainTextPassword the candidate password
     * @param hashedPassword the stored bcrypt hash
     * @return true if password matches, false otherwise
     */
    public static boolean check(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
