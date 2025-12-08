import gui.LoginPage;
import model.Bank;

import javax.swing.*;
import java.io.FileNotFoundException;

public class Main {
    private static final String SAVE_FILE = "bank_data.txt";

    public static void main(String[] args) throws FileNotFoundException {
        Bank bank;

        try{
            bank=Bank.loadState(SAVE_FILE);
        } catch (FileNotFoundException e) {
            bank=null;
        }

        if (bank == null) {
            System.out.println("Tworzenie nowej instancji banku.");
            bank = new Bank();
        } else {
            System.out.println("Bank wczytany. Witaj ponownie!");
            bank.initializeTransientFields();
        }

        final Bank finalBank=bank;

        SwingUtilities.invokeLater(()->{
            new LoginPage(finalBank);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Zamykanie aplikacji. Zapisywanie stanu ");
            finalBank.saveState(SAVE_FILE);
        }));
    }
}