package com.banking.util;

import com.banking.BankingSystem;
import com.banking.model.*;
import com.banking.exception.*;
import java.math.BigDecimal;
import java.util.Scanner;

public class BankingScanner {
    private final Scanner scanner;
    private final BankingSystem bankingSystem;
    private final TransactionLogger logger;

    public BankingScanner(BankingSystem bankingSystem, TransactionLogger logger) {
        this.scanner = new Scanner(System.in);
        this.bankingSystem = bankingSystem;
        this.logger = logger;
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        performDeposit();
                        break;
                    case 3:
                        performWithdrawal();
                        break;
                    case 4:
                        checkBalance();
                        break;
                    case 5:
                        viewTransactionHistory();
                        break;
                    case 6:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n=== Banking System Menu ===");
        System.out.println("1. Create New Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Check Balance");
        System.out.println("5. View Transaction History");
        System.out.println("6. Exit");
    }

    private void createAccount() {
        System.out.println("\n=== Create New Account ===");
        System.out.println("1. Savings Account");
        System.out.println("2. Checking Account");

        int accountType = getIntInput("Select account type: ");
        String accountNumber = getStringInput("Enter account number: ");
        BigDecimal initialBalance = getBigDecimalInput("Enter initial balance: ");

        Account account;
        if (accountType == 1) {
            BigDecimal interestRate = getBigDecimalInput("Enter interest rate (e.g., 0.025 for 2.5%): ");
            account = AccountFactory.createSavingsAccount(accountNumber, initialBalance, interestRate);
        } else if (accountType == 2) {
            account = AccountFactory.createAccount(AccountType.CHECKING, accountNumber, initialBalance);
        } else {
            System.out.println("Invalid account type selected.");
            return;
        }

        bankingSystem.addAccount(account);
        System.out.println("Account created successfully: " + account);
    }

    private void performDeposit() {
        String accountNumber = getStringInput("Enter account number: ");
        BigDecimal amount = getBigDecimalInput("Enter deposit amount: ");

        try {
            Account account = bankingSystem.findAccount(accountNumber);
            account.deposit(amount);
            logger.saveTransaction(accountNumber, amount);
            System.out.printf("Successfully deposited $%.2f to account %s%n", amount, accountNumber);
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void performWithdrawal() {
        String accountNumber = getStringInput("Enter account number: ");
        BigDecimal amount = getBigDecimalInput("Enter withdrawal amount: ");

        try {
            Account account = bankingSystem.findAccount(accountNumber);
            account.withdraw(amount);
            logger.saveTransaction(accountNumber, amount.negate());
            System.out.printf("Successfully withdrew $%.2f from account %s%n", amount, accountNumber);
        } catch (AccountNotFoundException | InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void checkBalance() {
        String accountNumber = getStringInput("Enter account number: ");

        try {
            Account account = bankingSystem.findAccount(accountNumber);
            System.out.println(account);
        } catch (AccountNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewTransactionHistory() {
        logger.showAllTransactions();
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private BigDecimal getBigDecimalInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return new BigDecimal(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid decimal number.");
            }
        }
    }
}