package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Client implements Serializable {

    public final int clientId;

    public String clientName;
    public String clientAddress;

    private final List<Account> accounts;
    private final List<Deposit> deposits;

    private int mainAccountId;
    private boolean isUseMainAccOp;

    private final MainVariables mv;
    public transient UserInterface UI;
    private final Bank bank;

    public Client(ClientData cd, MainVariables mv, UserInterface UI, Bank bank) {
        this.clientId = cd.clientId;

        this.clientName = cd.clientName;
        this.clientAddress = cd.clientAddress;

        this.accounts = new ArrayList<>();
        this.deposits = new ArrayList<>();

        this.mainAccountId = -1;
        this.isUseMainAccOp = true;

        this.bank = bank;
        this.mv = mv;
        this.UI = UI;

        addAccount();
        setAsMain(0);
    }

    public double getAccBalance(int accountNumber) {
        return accounts.get(accountNumber).getBalance();
    }

    public String getAccBalanceString(int accountNumber) {
        return Double.toString(accounts.get(accountNumber).getBalance());
    }

    public double getBalance() {
        return accounts.get(0).getBalance();
    }

    public String getBalanceString() {
        return Double.toString(accounts.get(0).getBalance());
    }

    public void setAsMain(int accId) {
        this.mainAccountId = accId;
    }

    public int getMainAccountId() {
        return mainAccountId;
    }

    public void useMainAccOp(boolean use) { // useMainAccountOption
        if (use) {
            this.isUseMainAccOp = true;
        } else {
            this.isUseMainAccOp = false;
        }
    }

    public boolean isUseMaOp() {
        return this.isUseMainAccOp;
    }

    // -- --


    // -- --

    public int addAccount() {
        CheckingAccount account = new CheckingAccount(
                mv.newAccountNumber(),
                mv,
                UI
        );

        accounts.add(account);

        this.bank.registerAcc(this.clientId);
        return accounts.size() - 1;
    }

    public String createAccList() {
        String accList = "";

        int cnt = 0;
        for (Account account : this.accounts) {
            accList += "      ID Konta: " + Integer.toString(cnt) + "\n" + account.getAccDataString();

            cnt++;
        }
        accList += "\n";

        return accList;
    }

    public Account getAcc(int accId) {
        return accounts.get(accId); //Warto dodać zabezpieczenie przed wyjściem poza zakres!
    }

    // -- --


    // -- --

    // Wyjątek! : dodać wykorzystaniw sprawdzenia czy konto tx.inAccNumber jest w kontach tego klienta.
    public void executeTransaction(TransactionData tx) {
        int accId = searchAccount(tx.inAccNumber);

        if (accId == -1) {
            return;
        } // <- TU

        accounts.get(accId).receiveTransfer(tx);


    }

    private int searchAccount(int inAccNumber) {

        int i = 0;
        for (Account acc : accounts) {
            if (acc.accountNumber == inAccNumber) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public void setUI(UserInterface ui) {
        this.UI=ui;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    // -- --
}
