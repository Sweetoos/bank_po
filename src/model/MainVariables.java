package model;

import java.io.Serializable;

public class MainVariables implements Serializable {

    private int lastAccountNumber;
    private int lastClientId;
    private int lastTransactionId;

    MainVariables(){
        this.lastAccountNumber = -1;
        this.lastClientId = -1;
        this.lastTransactionId = -1;
    }

    public void incLastAccountNumber() {
        this.lastAccountNumber++;
    }
    public int getLastAccountNumber() {
        return lastAccountNumber;
    }
    public int newAccountNumber() {
        return ++this.lastAccountNumber;
    }

    public void incLastClientId() {
        this.lastClientId++;
    }
    public int getLastClientId() {
        return lastClientId;
    }
    public int newClientId() {
        return ++this.lastClientId;
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
