package model;

import java.io.Serializable;
import java.time.LocalDate;
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

    public static final String ACCOUNT_TYPE_CHECKING = "Checking";
    public static final String ACCOUNT_TYPE_SAVINGS = "Savings";

    public Client(ClientData cd, MainVariables mv, Bank bank) {
        this.clientId = cd.clientId;
        this.clientName = cd.clientName;
        this.clientAddress = cd.clientAddress;
        this.username = cd.username;
        this.password = cd.password;
        this.accounts = new ArrayList<>();
        this.mv = mv;
        this.bank = bank;
        addAccount(ACCOUNT_TYPE_CHECKING, 0);
    }

    public boolean checkPassword(String pass) {
        return this.password != null && this.password.equals(pass);
    }

    public double getBalance() {
        return getMainAccount().map(Account::getBalance).orElse(0.0);
    }

    public double getTotalBalance() {
        return accounts.stream().mapToDouble(Account::getBalance).sum();
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

    public boolean setMainAccountByNumber(int accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accountNumber) {
                if (acc instanceof SavingsAccount) {
                    System.err.println("Attempted to set a Savings Account as main. Operation denied.");
                    return false;
                }
                this.mainAccountId = accountNumber;
                return true;
            }
        }
        return false;
    }

    public int getMainAccountId() {
        return this.mainAccountId;
    }

    public Account addAccount(String accountType, double interestRate) {
        int newAccNum = mv.newAccountNumber();
        Account newAccount;
        LocalDate creationDate = bank.getCurrentDate();

        if (accountType.equals(ACCOUNT_TYPE_SAVINGS)) {
            newAccount = new SavingsAccount(newAccNum, interestRate, mv, creationDate);
        } else {
            newAccount = new CheckingAccount(newAccNum, mv, creationDate);
        }

        accounts.add(newAccount);
        bank.registerAcc(this.clientId, newAccNum);

        if (accounts.size() == 1) {
            setMainAccountByNumber(newAccNum);
        }

        return newAccount;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }
}