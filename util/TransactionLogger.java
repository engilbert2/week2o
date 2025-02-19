package com.banking.util;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.io.IOException;
import java.util.List;

public class TransactionLogger {
    private static final Path TRANSACTIONS_PATH = Path.of("transactions.txt");

    public void saveTransaction(String accountNumber, BigDecimal amount) {
        try {
            // Create transaction record with timestamp
            String transaction = String.format("%s,%s,%.2f%n",
                    LocalDateTime.now(), accountNumber, amount);

            // Write to file using NIO - create if doesn't exist, append if exists
            Files.writeString(TRANSACTIONS_PATH, transaction,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.err.println("Could not save transaction: " + e.getMessage());
        }
    }

    public void showAllTransactions() {
        try {
            // Check if file exists
            if (!Files.exists(TRANSACTIONS_PATH)) {
                System.out.println("No transactions found.");
                return;
            }

            System.out.println("\nTransaction History:");
            System.out.println("------------------");

            // Read all lines using NIO
            List<String> lines = Files.readAllLines(TRANSACTIONS_PATH);
            for (String line : lines) {
                String[] parts = line.split(",");
                // Format: timestamp, account, amount
                System.out.printf("Time: %s, Account: %s, Amount: $%s%n",
                        parts[0], parts[1], parts[2]);
            }

        } catch (IOException e) {
            System.err.println("Could not read transactions: " + e.getMessage());
        }
    }

    public void clearTransactions() {
        try {
            Files.deleteIfExists(TRANSACTIONS_PATH);
        } catch (IOException e) {
            System.err.println("Could not clear transactions: " + e.getMessage());
        }
    }
}