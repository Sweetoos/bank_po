package org.example.bank_po.model;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accountId, double initialBalance, double interestRate) {
        super(accountId, initialBalance);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void addInterest(){}

    @Override
    public boolean withdraw(double amount) {
        return false;
    }
}
