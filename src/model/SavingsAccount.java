package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;

    double annualInterestRate;

    SavingsAccount(int accountNumber, double annualInterestRate, MainVariables mv, LocalDate creationDate) {
        super(accountNumber, "Savings", mv, creationDate);
        this.annualInterestRate = annualInterestRate;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void applyAnnualInterest(LocalDate currentDate) {
        if (this.balance <= 0 || this.annualInterestRate <= 0) {
            return;
        }
        if (lastInterestAppliedDate != null && ChronoUnit.YEARS.between(lastInterestAppliedDate, currentDate) >= 1) {
            double interest = this.balance * (this.annualInterestRate / 100.0);

            TransactionData td = new TransactionData(mv.newTransactionId(), this.balance);
            td.amount = interest;
            td.inAccNumber = this.accountNumber;
            td.outAccNumber = -1;
            td.setTransactionType("Interest");

            this.balance += interest;
            td.balanceAfter = this.balance;
            transactionHistory.add(new Transaction(td));

            this.lastInterestAppliedDate = currentDate;
            System.out.println("Applied interest for savings account " + this.accountNumber);
        }
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            TransactionData td = new TransactionData(mv.newTransactionId(), this.balance);
            td.amount = amount;
            td.inAccNumber = this.accountNumber;
            td.outAccNumber = -1;
            td.setTransactionType("Deposit");
            this.balance += amount;
            td.balanceAfter = this.balance;
            transactionHistory.add(new Transaction(td));
        }
    }

    @Override
    public boolean withdraw(double amount) {
        System.err.println("Withdrawal from a Savings Account is not permitted.");
        return false;
    }

    @Override
    public boolean transfer(double amount, int targetAccountNumber, Bank bank) {
        Client owner = bank.findClientByAccountNumber(this.accountNumber);
        Client targetOwner = bank.findClientByAccountNumber(targetAccountNumber);

        if (owner != null && owner.equals(targetOwner)) {
            if (amount > 0 && amount <= this.balance) {
                TransactionData td = new TransactionData(mv.newTransactionId(), this.balance);
                td.amount = -amount;
                td.outAccNumber = this.accountNumber;
                td.inAccNumber = targetAccountNumber;
                td.setTransactionType("Internal Transfer");

                this.balance -= amount;
                td.balanceAfter = this.balance;
                this.transactionHistory.add(new Transaction(td));

                targetOwner.getAccountById(targetAccountNumber).ifPresent(
                        acc -> acc.receiveTransfer(amount, this.accountNumber)
                );
                return true;
            }
        }

        System.err.println("External transfer from a Savings Account is not permitted.");
        return false;
    }
}