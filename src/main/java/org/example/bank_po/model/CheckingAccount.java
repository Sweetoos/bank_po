package org.example.bank_po.model;

public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(String accountId, double initialBalance, double overdraftLimit) {
        super(accountId, initialBalance);
        this.overdraftLimit=overdraftLimit;
    }

    public double getOverdraftLimit() {
        return 0;
    }

    public void setOverdraftLimit(double overdraftLimit) {}

    @Override
    public boolean withdraw(double amount) {
        return false;
    }
}
