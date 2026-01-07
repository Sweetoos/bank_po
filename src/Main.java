import gui.LoginPage;
import model.Bank;
import javax.swing.SwingUtilities;

public class Main {
    private static final String SAVE_FILE = "bank_data.ser";

    public static void main(String[] args) {
        Bank bank = Bank.loadState(SAVE_FILE);

        if (bank == null) {
            System.out.println("Save file not found or corrupted. Creating a new bank instance.");
            bank = new Bank();
        } else {
            System.out.println("Bank state loaded successfully. Welcome back!");
        }

        final Bank finalBank = bank;

        SwingUtilities.invokeLater(() -> {
            new LoginPage(finalBank);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Closing application. Saving state...");
            finalBank.saveState(SAVE_FILE);
        }));
    }
}