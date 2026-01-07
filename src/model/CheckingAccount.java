package model;

import java.time.LocalDate;

public class CheckingAccount extends Account {
    private static final long serialVersionUID = 1L;

    CheckingAccount(int accountNumber, MainVariables mv, LocalDate creationDate) {
        super(accountNumber, "Checking", mv, creationDate);
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) return;

        TransactionData td = new TransactionData(mv.newTransactionId(), this.balance);
        td.amount = amount;
        td.inAccNumber = this.accountNumber;
        td.outAccNumber = -1;
        td.setTransactionType("Deposit");
        this.balance += amount;
        td.balanceAfter = this.balance;
        transactionHistory.add(new Transaction(td));
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > this.balance) return false;

        TransactionData td = new TransactionData(mv.newTransactionId(), this.balance);
        td.amount = -amount;
        td.outAccNumber = this.accountNumber;
        td.inAccNumber = -1;
        td.setTransactionType("Withdraw");

        this.balance -= amount;
        td.balanceAfter = this.balance;
        transactionHistory.add(new Transaction(td));
        return true;
    }

    @Override
    public boolean transfer(double amount, int targetAccountNumber, Bank bank) {
        if (amount <= 0 || amount > this.balance) return false;
        if (this.accountNumber == targetAccountNumber) return false;

        Client targetClient = bank.findClientByAccountNumber(targetAccountNumber);
        if (targetClient == null) return false;

        Account targetAccount = targetClient.getAccountById(targetAccountNumber).orElse(null);
        if (targetAccount == null) return false;

        TransactionData outgoingTd = new TransactionData(mv.newTransactionId(), this.balance);
        outgoingTd.amount = -amount;
        outgoingTd.outAccNumber = this.accountNumber;
        outgoingTd.inAccNumber = targetAccountNumber;
        outgoingTd.setTransactionType("Transfer Out");

        this.balance -= amount;
        outgoingTd.balanceAfter = this.balance;
        this.transactionHistory.add(new Transaction(outgoingTd));

        targetAccount.receiveTransfer(amount, this.accountNumber);
        return true;
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