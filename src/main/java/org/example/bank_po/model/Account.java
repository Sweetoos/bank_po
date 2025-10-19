package org.example.bank_po.model;

import org.example.bank_po.interfaces.Asset;
import org.example.bank_po.interfaces.Transactable;

import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Transactable, Asset {
    protected String accountId;
    protected double balance;
    protected List<Transaction> transactionHistory;

    public Account(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance() {
        return 0;
    }

    public Account recordTransaction(String type, double amount) {
        return null;
    }

    @Override
    public void deposit(double amount) {
    }

    @Override
    public boolean withdraw(double amount) {
        return false;
    }

    @Override
    public boolean transfer(double amount, Account target) {
        return false;
    }

    public String getAccountId() {
        return accountId;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public double getValue(){
        return 0;
    }

}
