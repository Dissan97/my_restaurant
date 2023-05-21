package org.dissan.restaurant.controllers.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    private static final String HASH_ALGORITH = "SHA-256";

    public static @Nullable String hashString(@NotNull String input) {
        try {
            // Create a MessageDigest object with SHA-256 algorithm
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITH);

            // Convert the input string to bytes
            byte[] inputBytes = input.getBytes();

            // Calculate the hash value
            byte[] hashBytes = digest.digest(inputBytes);

            // Convert the hash bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

}
