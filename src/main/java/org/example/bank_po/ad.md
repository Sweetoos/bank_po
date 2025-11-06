import java.util.ArrayList;
import java.util.List;

public class Account {

    public  final int               accoutnNumber;
    private       double            balance;
    private final List<Transaction> transactionHistory;

    Account(int accoutnNumber) {
        this.accoutnNumber = accoutnNumber;
        this.balance = 0;
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }
    public int getAccoutnNumber(){
        return accoutnNumber;
    }
    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}
public interface Asset {
public double getValue();
}
import java.util.HashMap;
import java.util.Map;

public class Bank {
private final Map<Integer, Client> clients;

    private int lastAccountNumber;
    private int lastClientId;

    private final UserInterface UI;

    Bank(){
        clients = new HashMap<>();
        lastAccountNumber = -1;
        lastClientId = -1;

        UI = new UserInterface();
    }

    public void addClient(){
        ClientData cd = UI.addClient();

        lastClientId++;
        lastAccountNumber++;

        Client client = new Client(
                lastClientId,
                cd.clientName,
                cd.clientAddress,
                lastAccountNumber,
                UI
        );
        clients.put(lastClientId, client);
    }

    private void endClient(){
        if(lastClientId < 0) return; //Error
        lastAccountNumber = clients.get(lastClientId).getLastAccountNumber();
    }



}
import java.util.ArrayList;
import java.util.List;

public class Client {

    public final int           clientId;
    public       String        clientName;
    public       String        clientAddress;
    public final List<Account> accounts;
    public final List<Deposit> deposits;

    private      int mainAccountNumber;

    private      int lastAccountNumber;

    public final UserInterface UI;

    public Client(int clientId, String clientName, String clientAddress, int lastNumber, UserInterface UI) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.accounts = new ArrayList<>();
        this.deposits = new ArrayList<>();

        this.lastAccountNumber = lastNumber;
        this.mainAccountNumber = -1;

        this.UI = UI;
    }

    public int addAccount() {
        lastAccountNumber++;
        Account account = new Account(lastAccountNumber);

        if(mainAccountNumber == -1) mainAccountNumber = lastAccountNumber;
        accounts.add(account);
        return lastAccountNumber;
    }

    public void addDeposit(double amount) {

    }

    public List getAssets(String asset){
        if(asset == "accounts"){
            return accounts;
        } else if(asset == "deposits"){
            return deposits;
        } else {
            return null;
        }
    }

    public int getLastAccountNumber(){
        return lastAccountNumber;
    }
}
public class ClientData {
public int clientId;
public String clientName;
public String clientAddress;

    public ClientData() {
        clientId = 0;
        clientName = "";
        clientAddress = "";
    }
}
import java.time.LocalDate;

public class Deposit {

    public  final int depositId;
    public  final String depositName;
    public  final double amount;
    public  final double interestRate;
    public  final double durationInMonths;
    public  final LocalDate openingDate;
    public  final LocalDate closingDate;

    public Deposit(DepositData dd) {
        this.depositId = dd.depositId;
        this.depositName = dd.depositName;

        this.amount = dd.amount;
        this.interestRate = dd.interestRate;

        this.durationInMonths = dd.durationInMonths;

        this.openingDate = null;
        this.closingDate = null;
    }
}
import java.time.LocalDate;

public class DepositData {
public int    depositId;
public String depositName;

    public double amount;
    public double interestRate;

    public double durationInMonths;

    public DepositData() {
        this.depositId = 0;
        this.depositName = null;

        this.amount = 0.0;
        this.interestRate = 0.0;

        this.durationInMonths = 0.0;
    }
}
//Class not used. To delete.
public class DepositType {
public final String name;
public final double interestRate;
public final int    durationInMonths;

    DepositType(String name, double interestRate, int durationInMonths) {
        this.name = name;
        this.interestRate = interestRate;
        this.durationInMonths = durationInMonths;
    }
}
import java.util.Scanner;

public final class IOConsole {

    private final String appVersion;

    private final Scanner in;

    private IOConsole(String appVersion) {
        this.appVersion = appVersion;
        this.in = new Scanner(System.in);
    }

    public static IOConsole start(String appVersion){
        IOConsole UI = new IOConsole(appVersion);

        UI.greeting();

        return UI;
    }

    public void greeting(){
        System.out.println("Witaj");
        System.out.println("Jest to program symulujący Bank");
        System.out.println("Wersja " + this.appVersion + "\n");

    }

    public void print(String message){

        System.out.print(message);
    }

    public void println(String message){

        System.out.println(message);
    }

    public String read(){
        String line = in.nextLine();
        endProgram(line);

        return line;
    }

    public String printRead(String message){
        System.out.print(message);
        String line = in.nextLine();

        endProgram(line);
        return line;
    }

    public void endProgram(String line){
        if(line.equals("exit")){
            System.exit(0);
        }
    }
}
public class Main{
public static void main(String[] args){



    }
}public interface Transactable {
public void    deposit (double amount);
public boolean withdraw(double amount);
public boolean transfer(Account targetAccount, double amount);
}
public class Transaction {
public final String transactionId;
public final String type;
public final double amount;

    public Transaction(String transactionId, String type, double amount) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
    }
}
import java.util.List;

public class UserInterface {
public final String appVersion;
public final IOConsole IO;

    public UserInterface() {
        this.appVersion = "2.1.2";
        this.IO = IOConsole.start(this.appVersion);


    }

    public ClientData addClient(){
        ClientData cd = new ClientData();

        cd.clientName    = IO.printRead("Proszę podać imie i nazwisko: ");
        cd.clientAddress = IO.printRead("Proszę podać adres zamieszkania: ");

        return cd;
    }

    /*
    * Abstract:
    * Funkcja ma za zadanie pobrać dane od użytkownika: z jakiego account ma być pobrana kwota wpłacona na deposit,
    * ile ma wynosić kwota wpłacona, ma pobrać na ile miesięcy pieniądze mają być na deposycie.
    * Details:
    * Funckja ma wyświetlić listę kont jakie posiada użytkownik, i umożliwić wybranie rządanego konta,
    * (opcjonalnie pierwszym kontem jakie wyświetli będzie zawsze konto główne, którego mainAccountNumber jest przekazywany do funkcji).
    * Funkcja ma za zadanie pobrać wartość przelewy, transferu i sprawdzić czy nie przekracza wartosci pieniędzy dostępnych na koncie.
    * Funkcja ma pobrać dane na ile miesięcy pieniądze mają być na depozycie.
    */
    public DepositData createDeposit(List<Account> accounts, int mainAccountNumber){
        DepositData dd = new DepositData();




        return dd;
    }

    private void printAccountData(Account account){
        IO.println("Numer konta:");
        IO.println(String.valueOf(account.getAccoutnNumber()));
        IO.println("Ilość dostępnych środków: " + String.valueOf(account.getBalance()));
    }

    private void printAccountsData(){

    }
}