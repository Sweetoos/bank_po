package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Transactable{
    protected String accountId;
    protected double balance;
    protected List<Transaction> transactionHistory;

    public double getBalance() {
        return 0;
    }

    public Account(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }
    public Account recordTransaction(String type, double amount){
        return null;
    }
    public boolean withdraw(double amount){
        return false;
    }

    public String getAccountId(){
        return accountId;
    }
}
