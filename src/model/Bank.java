package model;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Bank implements Serializable {
    private final Map<Integer, Client> clients;
    private final Map<Integer, Integer> accountList; //First int is accNumber, second int is client ID.
    private static final long serialVersionUID = 1L;
    private final MainVariables mv;

    private transient IOConsole IO;
    private transient UserInterface UI;

    public final String appVersion;

    private transient Checks ch;

    public Bank() {
        this.appVersion = "3.0.0";

        clients = new HashMap<>();
        accountList = new HashMap<>();
        mv = new MainVariables();
    }


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

    public void saveState(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
            System.out.println("Stan banku został pomyślnie zapisany do " + filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Bank loadState(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Plik zapisu nie istnieje. Aplikacja zostanie uruchomiona z nowym stanem");
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = ois.readObject();
            if (obj instanceof Bank) {
                System.out.println("Stan banku został pomyślnie wczytany z " + filename);
                return (Bank) obj;
            } else {
                System.err.println("Błąd: Odczytany obiekt nie jest instancją klasy model.Bank");
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Wystąpił błąd podczas wczytywania z " + e.getMessage());
            e.printStackTrace();
            return null;
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

    public void addClient() {
        ClientData cd = new ClientData();
        cd.clientId = mv.newClientId();

        cd = UI.addClient(cd);

        mv.incLastAccountNumber();

        Client client = new Client(
                cd,
                mv,
                UI,
                this
        );
        this.clients.put(mv.getLastClientId(), client);
    }

    public void registerAcc(int clientId) {
        this.accountList.put(mv.getLastAccountNumber(), clientId);
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
        this.IO=IOConsole.start(this.appVersion);
        this.UI=new UserInterface(this.appVersion, this.IO);
        this.ch=new Checks();

        if(this.clients!=null)
        {
            for(Client client:this.clients.values())
            {
                client.setUI(this.UI);
                if(client.getAccounts()!=null)
                {
                    for(Account account:client.getAccounts()){
                        account.setUI(this.UI);
                    }
                }
            }
        }
    }

    public Collection<Object> getClients() {
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

}
