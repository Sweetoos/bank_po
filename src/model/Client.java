package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Client implements Serializable {
    private static final long serialVersionUID = 1L;

    public final int clientId;
    public String clientName;
    public String clientAddress;
    public String username;
    private String password;

    private final List<Account> accounts;
    private int mainAccountId;

    private final MainVariables mv;
    private final Bank bank;

    public Client(ClientData cd, MainVariables mv, Bank bank) {
        this.clientId = cd.clientId;
        this.clientName = cd.clientName;
        this.clientAddress = cd.clientAddress;
        this.accounts = new ArrayList<>();
        this.username = cd.username;
        this.password = cd.password;
        this.mv = mv;
        this.bank = bank;
        addAccount();
    }

    public boolean checkPassword(String pass) {
        return this.password != null && this.password.equals(pass);
    }

    public double getBalance() {
        return getMainAccount()
                .map(Account::getBalance)
                .orElse(0.0);
    }

    public Optional<Account> getMainAccount() {
        return accounts.stream()
                .filter(acc -> acc.getAccountNumber() == this.mainAccountId)
                .findFirst();
    }

    public Optional<Account> getAccountById(int accountNumber) {
        return accounts.stream()
                .filter(acc -> acc.getAccountNumber() == accountNumber)
                .findFirst();
    }

    public void setMainAccountId(int accountId) {
        if (getAccountById(accountId).isPresent()) {
            this.mainAccountId = accountId;
        }
    }

    public int getMainAccountId() {
        return this.mainAccountId;
    }

    public Account addAccount() {
        int newAccNum = mv.newAccountNumber();
        CheckingAccount newAccount = new CheckingAccount(newAccNum, mv);
        accounts.add(newAccount);
        bank.registerAcc(this.clientId, newAccNum);

        if (accounts.size() == 1) {
            setMainAccountId(newAccNum);
        }

        return newAccount;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    public double getTotalBalance() {
        double total = 0.0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }
}