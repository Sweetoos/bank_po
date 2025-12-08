package org.example.bank_po.model;

import java.util.HashMap;
import java.util.Map;

public class Bank {
    private final Map<Integer, Client> clients;
    private final MainVariables mv;

    private final UserInterface UI;

    Bank(){
        clients = new HashMap<>();

        mv = new MainVariables();
        UI = new UserInterface();
    }

    public void addClient(){
        ClientData cd = UI.addClient();

        mv.incLastClientId();
        mv.incLastAccountNumber();

        Client client = new Client(
                cd.clientName,
                cd.clientAddress,
                mv,
                UI
        );
        clients.put(mv.getLastClientId(), client);
    }


}
