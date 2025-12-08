import model.Bank;

import java.io.FileNotFoundException;

public class Main {
    private static final String SAVE_FILE = "bank_data.txt";

    public static void main(String[] args) throws FileNotFoundException {
        Bank bank = Bank.loadState(SAVE_FILE);

        if (bank == null) {
            System.out.println("Tworzenie nowej instancji banku.");
            bank = new Bank();
        } else {
            System.out.println("Bank wczytany. Witaj ponownie!");
        }

        bank.doTasks();

        System.out.println("Zamykanie aplikacji. Zapisywanie stanu...");
        bank.saveState(SAVE_FILE);
    }
}