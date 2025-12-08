package model;
import interfaces.Transactable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public abstract class Account implements Transactable, Serializable {

    public final int accountNumber;
    public final String accountType;

    protected double balance;
    protected final List<Transaction> transactionHistory;

    protected final MainVariables mv;
    protected transient final UserInterface UI;

    Account(int accountNumber, String accountType, MainVariables mv, UserInterface UI) {

        this.accountNumber = accountNumber;
        this.accountType = accountType;

        this.balance = 0;
        this.transactionHistory = new ArrayList<>();

        this.mv = mv;
        this.UI = UI;
    }

    protected double getBalance() {
        return balance;
    }

    protected int getAccountNumber() {
        return accountNumber;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public String getAccDataString() {
        String result = "";

        result += "model.Account Number: " + this.accountNumber + "\n";
        result += "model.Account   Type: " + this.accountType + "\n";
        result += "       Balance: " + this.balance;

        return result;
    }

    public int deposit() {
        return 0;
    }

    public int withdraw() {
        return 0;
    }

    public TransactionData transfer() {
        return new TransactionData(-1, -1);
    }

    public void receiveTransfer(TransactionData tr) {
        return;
    }

    public void setUI(UserInterface ui) {
    }
}
