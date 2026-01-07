package model;

import interfaces.Transactable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Transactable, Serializable {
    private static final long serialVersionUID = 1L;

    public final int accountNumber;
    public final String accountType;
    protected double balance;
    protected final List<Transaction> transactionHistory;
    protected final MainVariables mv;
    protected LocalDate lastInterestAppliedDate;

    Account(int accountNumber, String accountType, MainVariables mv, LocalDate creationDate) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
        this.mv = mv;
        this.lastInterestAppliedDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public abstract void deposit(double amount);
    public abstract boolean withdraw(double amount);
    public abstract boolean transfer(double amount, int targetAccountNumber, Bank bank);

    protected void receiveTransfer(double amount, int fromAccountNumber) {
        // Domyślna implementacja jest pusta, podklasy ją nadpisują
    }
}