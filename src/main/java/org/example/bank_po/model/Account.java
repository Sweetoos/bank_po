package org.example.bank_po.model;

import org.example.bank_po.interfaces.Transactable;

import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Transactable {

    protected final int               accoutnNumber;
    public    final String            accountType;

    protected       double            balance;
    protected final List<Transaction> transactionHistory;

    protected final MainVariables mv;
    protected final UserInterface UI;

    Account(int accoutnNumber, String accountType, MainVariables mv, UserInterface UI) {
        this.accoutnNumber = accoutnNumber;
        this.accountType   = accountType;

        this.balance = 0;
        this.transactionHistory = new ArrayList<>();

        this.mv = mv;
        this.UI = UI;
    }

    protected double getBalance() {
        return balance;
    }
    protected int getAccountNumber(){
        return accoutnNumber;
    }

    protected List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

//    protected void setBalance(double balance) {
//        this.balance = balance;
//    }

    public int deposit(){
        return 0;
    }

    public int withdraw(){
        return 0;
    }

    public int transfer(){
        return 0;
    }

}
