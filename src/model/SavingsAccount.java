package model;

public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    double annualInterestRate;

    SavingsAccount(int accountNumber, double annualInterestRate, MainVariables mv) {
        super(accountNumber, "Savings", mv);
        this.annualInterestRate = annualInterestRate;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) this.balance += amount;
    }

    @Override
    public boolean withdraw(double amount) {
        return false;
    }

    @Override
    public boolean transfer(double amount, int targetAccountNumber, Bank bank) {
        return false;
    }
}
