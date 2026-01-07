package model;

import java.time.LocalDate;

public class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;

    double annualInterestRate;
    private double accruedInterest;

    private static final double BELKA_TAX_RATE = 0.19;

    SavingsAccount(int accountNumber, double annualInterestRate, MainVariables mv, LocalDate creationDate) {
        super(accountNumber, "Savings", mv, creationDate);
        this.annualInterestRate = annualInterestRate;
        this.accruedInterest = 0.0;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void calculateDailyInterest() {
        if (this.balance > 0 && this.annualInterestRate > 0) {
            double dailyRate = (this.annualInterestRate / 100.0) / 365.0;
            double dailyInterest = this.balance * dailyRate;
            this.accruedInterest += dailyInterest;
        }
    }

    public void capitalizeInterest() {
        if (this.accruedInterest <= 0) return;

        double tax = this.accruedInterest * BELKA_TAX_RATE;
        tax = Math.floor(tax * 100) / 100.0;

        double netInterest = this.accruedInterest - tax;

        TransactionData capitalTd = new TransactionData(mv.newTransactionId(), this.balance);
        capitalTd.amount = netInterest;
        capitalTd.inAccNumber = this.accountNumber;
        capitalTd.setTransactionType("Interest Capitalization");
        this.balance += netInterest;
        capitalTd.balanceAfter = this.balance;
        transactionHistory.add(new Transaction(capitalTd));

        if (tax > 0) {
            TransactionData taxTd = new TransactionData(mv.newTransactionId(), this.balance);
            taxTd.amount = -tax;
            taxTd.inAccNumber = this.accountNumber;
            taxTd.setTransactionType("Capital Gains Tax");
            taxTd.balanceAfter = this.balance;
            transactionHistory.add(new Transaction(taxTd));
        }

        this.accruedInterest = 0.0;
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
                Account targetAccount = targetOwner.getAccountById(targetAccountNumber).orElse(null);
                if (targetAccount == null) return false;

                TransactionData td = new TransactionData(mv.newTransactionId(), this.balance);
                td.amount = -amount;
                td.outAccNumber = this.accountNumber;
                td.inAccNumber = targetAccountNumber;
                td.setTransactionType("Internal Transfer");
                this.balance -= amount;
                td.balanceAfter = this.balance;
                this.transactionHistory.add(new Transaction(td));

                targetAccount.receiveTransfer(amount, this.accountNumber);
                return true;
            }
        }

        System.err.println("External transfer from a Savings Account is not permitted.");
        return false;
    }

    @Override
    protected void receiveTransfer(double amount, int fromAccountNumber) {
        TransactionData incomingTd = new TransactionData(mv.newTransactionId(), this.balance);
        incomingTd.amount = amount;
        incomingTd.inAccNumber = this.accountNumber;
        incomingTd.outAccNumber = fromAccountNumber;
        incomingTd.setTransactionType("Transfer In");
        this.balance += amount;
        incomingTd.balanceAfter = this.balance;
        this.transactionHistory.add(new Transaction(incomingTd));
    }
}