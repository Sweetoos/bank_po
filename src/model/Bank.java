package model;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Bank implements Serializable {
    private final Map<Integer, Client> clients;
    private final Map<Integer, Integer> accountList; // <accNumber, client ID>
    private static final long serialVersionUID = 1L;
    private final MainVariables mv;

    public final String appVersion;

    public Bank() {
        this.appVersion = "3.0.0";

        clients = new HashMap<>();
        accountList = new HashMap<>();
        mv = new MainVariables();
    }

    public Map<Integer, Client> getClients() {
        return this.clients;
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

    public void saveState(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            System.out.println("Bank state has been successfully saved to " + filename + " file");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error with saving save state: " + e.getMessage());
        }
    }


    public static Bank loadState(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (Bank) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error with loading state: " + e.getMessage());
            return null;
        }
    }

    /*
    public void doTasks() {
        ch.opOne = -1;

        while (true) {
            ch.opOne = UI.firstLayer();

            if (ch.opOne == 1) {
                addClient();

            } else if (ch.opOne == 2) {

                optionTwo();

            } else if (ch.opOne > 2 || ch.opOne < 0) {
                break;
            }

            if (IO.isEndPr()) {
                break;
            }
            ch.opOne = -1;
        }
    }

    private void optionTwo() {
        ch.opTwo = -1;
        ch.opThree = -1;
        //ch.opFour = -1;

        Client client;

        ch.opTwo = UI.chooseClient(this.clients);
        client = this.clients.get(ch.opTwo);

        ch.opThree = UI.chooseOptionTwo(client);

        int accId = client.getMainAccountId();
        switch (ch.opThree) {
            case 1:
                accId = help1(accId, client);

                client.getAcc(accId).deposit();
                break;
            case 2:
                accId = help1(accId, client);

                client.getAcc(accId).withdraw();
                break;
            case 3:
                accId = help1(accId, client);

                executeTransaction(client.getAcc(accId).transfer());
                break;
            case 4:


                break;
            case 5:
                accId = client.addAccount();

                UI.addAcc(client.getAcc(accId));
                break;
            case 6:
                break;
            case 7:
                break;
            default:
                break;
        }


    }




    private int help1(int accId, Client client) {

        if (client.isUseMaOp()) {
            return accId;
        }

        if (UI.fromMainAcc() != 1) {
            return UI.showAccList(client.createAccList());
        }

        return accId;
    }


    private void executeTransaction(TransactionData tx) {

        if (!tx.makeTransaction) {
            return;
        }

        if (!accountList.containsKey(tx.inAccNumber)) {
            tx.inAccNumber = tx.outAccNumber;
        }

        int clientId = accountList.get(tx.inAccNumber);
        Client client = this.clients.get(clientId);

        client.executeTransaction(tx);
    }

    public void initializeTransientFields() {
        this.IO = IOConsole.start(this.appVersion);
        this.UI = new UserInterface(this.appVersion, this.IO);
        this.ch = new Checks();

        if (this.clients != null) {
            for (Client client : this.clients.values()) {
                client.setUI(this.UI);
                if (client.getAccounts() != null) {
                    for (Account account : client.getAccounts()) {
                        account.setUI(this.UI);
                    }
                }
            }
        }
    }


    public class Checks implements Serializable {

        public int opOne;
        public int opTwo;
        public int opThree;
        public int opFour;

        Checks() {
            opOne = 0;
            opTwo = 0;
            opThree = 0;
            opFour = 0;
        }

    }
*/
}
