package com.banking;

import com.banking.util.TransactionLogger;
import com.banking.util.BankingScanner;

public class Main {
    public static void main(String[] args) {
        // Initialize components
        TransactionLogger logger = new TransactionLogger();
        BankingSystem bankingSystem = new BankingSystem();

        // Create and start the banking scanner
        BankingScanner bankingScanner = new BankingScanner(bankingSystem, logger);
        bankingScanner.start();
    }
}