package org.example.bank_po.model;

public class MainVariables {

    private int lastAccountNumber;
    private int lastClientId;
    private int lastTransactionId;

    MainVariables(){
        this.lastAccountNumber = -1;
        this.lastAccountNumber = -1;
        this.lastTransactionId = -1;
    }

    public void incLastAccountNumber() {
        this.lastAccountNumber++;
    }
    public int getLastAccountNumber() {
        return lastAccountNumber;
    }

    public void incLastClientId() {
        this.lastClientId++;
    }
    public int getLastClientId() {
        return lastClientId;
    }

    public void incLastTransactionId() {
        this.lastTransactionId++;
    }
    public int getLastTransactionId() {
        return lastTransactionId;
    }
    public int newTransactionId() {
        this.lastTransactionId++;
        return this.lastTransactionId;
    }
}
