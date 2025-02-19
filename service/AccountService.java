package com.banking.service;

import com.banking.BankingSystem;
import com.banking.model.Account;
import com.banking.exception.*;
import java.math.BigDecimal;

public class AccountService {
    private final BankingSystem bankingSystem;

    public AccountService(BankingSystem bankingSystem) {
        this.bankingSystem = bankingSystem;
    }

    public void transfer(String fromAcc, String toAcc, BigDecimal amount)
            throws InsufficientFundsException, AccountNotFoundException {
        Account from = findAccount(fromAcc);
        Account to = findAccount(toAcc);

        try {
            from.withdraw(amount);
            to.deposit(amount);
        } catch (InsufficientFundsException | IllegalArgumentException e) {
            // Handle specific exceptions
            throw e;
        } catch (Exception e) {
            // Handle general exceptions
            throw new BankingException("Transfer failed", e);
        }
    }

    private Account findAccount(String number) {
        // Assertions for invariants
        assert number != null && !number.isEmpty() :
                "Account number cannot be null or empty";

        return bankingSystem.findAccount(number);
    }
}