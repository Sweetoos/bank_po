package org.example.bank_po.model;

import java.util.ArrayList;
import java.util.List;

public class Client {

    public final int           clientId;
    public       String        clientName;
    public       String        clientAddress;
    public final List<Account> accounts;
    public final List<Deposit> deposits;

    private      int mainAccountNumber;

    private final MainVariables mv;
    public  final UserInterface UI;

    public Client(String clientName, String clientAddress, MainVariables mv, UserInterface UI) {
        this.clientId = mv.getLastClientId();
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.accounts = new ArrayList<>();
        this.deposits = new ArrayList<>();

        this.mainAccountNumber = -1;

        this.mv = mv;
        this.UI = UI;
    }

    public void addAccount() {
        mv.incLastAccountNumber();
        CheckingAccount account = new CheckingAccount(
                mv.getLastAccountNumber(),
                mv,
                UI
        );

        if(mainAccountNumber == -1) mainAccountNumber = mv.getLastAccountNumber();
        accounts.add(account);
    }



    public List getAssets(String asset){
        if(asset == "accounts"){
            return accounts;
        } else if(asset == "deposits"){
            return deposits;
        } else {
            return null;
        }
    }
}
