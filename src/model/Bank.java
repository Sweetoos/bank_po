package model;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Bank implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<Integer, Client> clients;
    private final Map<Integer, Integer> accountList;
    private final MainVariables mv;
    public final String appVersion;

    private double savingsAccountInterestRate;
    private LocalDate currentDate;

    public Bank() {
        this.appVersion = "3.0.0-GUI";
        this.clients = new HashMap<>();
        this.accountList = new HashMap<>();
        this.mv = new MainVariables();
        this.savingsAccountInterestRate = 5.5;
        this.currentDate = LocalDate.now();
    }

    public Map<Integer, Client> getClients() {
        return this.clients;
    }

    public LocalDate getCurrentDate() {
        return this.currentDate;
    }

    public void addClient(ClientData cd) {
        cd.clientId = mv.newClientId();
        Client client = new Client(cd, mv, this);
        this.clients.put(client.clientId, client);
    }

    public void removeClient(int clientId) {
        Client clientToRemove = clients.get(clientId);
        if (clientToRemove != null) {
            for (Account acc : clientToRemove.getAccounts()) {
                accountList.remove(acc.getAccountNumber());
            }
            clients.remove(clientId);
        }
    }

    public void registerAcc(int clientId, int accountNumber) {
        this.accountList.put(accountNumber, clientId);
    }

    public Client findClientByAccountNumber(int accNum) {
        Integer clientId = accountList.get(accNum);
        return clientId != null ? clients.get(clientId) : null;
    }

    public Client authenticateClient(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        for (Client client : clients.values()) {
            if (username.equals(client.username) && client.checkPassword(password)) {
                return client;
            }
        }
        return null;
    }

    public double getSavingsAccountInterestRate() {
        return this.savingsAccountInterestRate;
    }

    public void setSavingsAccountInterestRate(double rate) {
        if (rate >= 0) {
            this.savingsAccountInterestRate = rate;
        }
    }

    public void advanceTimeByDays(int days) {
        for (int i = 0; i < days; i++) {
            this.currentDate = this.currentDate.plusDays(1);
            runDailyProcesses();
        }
    }

    private void runDailyProcesses() {
        for (Client client : clients.values()) {
            for (Account account : client.getAccounts()) {
                if (account instanceof SavingsAccount) {
                    ((SavingsAccount) account).calculateDailyInterest();
                }
            }
        }
        if (this.currentDate.getDayOfMonth() == this.currentDate.lengthOfMonth()) {
            runMonthlyCapitalizationProcess();
        }
    }

    public void runMonthlyCapitalizationProcess() {
        for (Client client : clients.values()) {
            for (Account account : client.getAccounts()) {
                if (account instanceof SavingsAccount) {
                    ((SavingsAccount) account).capitalizeInterest();
                }
            }
        }
    }

    public void saveState(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            System.out.println("Bank state has been successfully saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error while saving state: " + e.getMessage());
        }
    }

    public static Bank loadState(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Bank) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error while loading state: " + e.getMessage());
            return null;
        }
    }
}