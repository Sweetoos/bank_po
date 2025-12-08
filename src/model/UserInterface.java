package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;

public class UserInterface {
    public final String appVersion;
    public final IOConsole IO;

    public UserInterface(String appVersion, IOConsole IO) {
        this.appVersion = appVersion;
        this.IO = IO;
    }

    // -- --

//    public void printString(String chars){ // Function not tu use mainly.
//        IO.print(chars);
//    }

    // -- --

    public int firstLayer() {

        IO.println("Co byś chciał zrobić?");
        IO.println("1. Dodać nowego użytkownika");
        IO.println("2. Wykonać operacje w imeniu użytkownika");
        IO.println("0. Wyjście");

        return IO.printReadInt("-> ");
    }

    // --

    public int chooseOptionTwo(Client client) {

        IO.print("Dane clienta: \n\n");
        IO.println("Imie i nazwisko: " + client.clientName);
        IO.println("         Adres : " + client.clientAddress);

        IO.println("\nWartość środków na koncie głównym: " + client.getBalanceString());

        IO.print("\n");
        IO.println("Co byś chciał zrobić: ");
        IO.print("\n");
        IO.println("1. Wpłacić  gotówkę"); //OK
        IO.println("2. Wypłacić gotówkę"); //OK
        IO.print("\n");
        IO.println("3. Zlecić przelew");   //OK
        IO.print("\n");
        IO.println("4. Pokarz historię tranzakcji");   //Nie
        IO.print("\n");
        IO.println("5. Pokarz listę kont");   //Nie
        IO.print("\n");
        IO.println("6. Zmień limit debetu");// Nie zaimplementowane
        IO.print("\n");
        IO.println("7. Utworzyć nowe konto");//Nie zaimplementowane

        //IO.println("6. Utworzyć nowe konto oszczędnościowe"); //nie
        //IO.println("7. Utworzyć nowy depozyt"); //nie
        IO.print("\n");

        return IO.printReadInt("-> ");
    }

    // -- Klasy pomocnicze --

    public int chooseClient(Map<Integer, Client> clients) {

        showClientList(clients);

        IO.println("Wybierz klienta:\n");

        showClientList(clients);

        IO.println("");
        return IO.printReadInt("-> ");
    }

    // anonymous class with sorting by name
    private void showClientList(Map<Integer, Client> clients) {
        List<Client> clientList = new ArrayList<>(clients.values());

        clientList.sort(new Comparator<Client>() {
            @Override
            public int compare(Client c1, Client c2) {
                return c1.clientName.compareTo(c2.clientName);
            }
        });
        IO.println("Klienci posortowani alfabetycznie: ");
        for (Client client : clientList) {
            IO.print(Integer.toString(client.clientId) + " - " + client.clientName + "\n");
        }
    }

    // -- --


    public int showAccList(String list) {

        IO.print("Wybierz konto do operacji:\n\n");

        IO.print(list);
        IO.print("\n");

        return IO.printReadInt("-> ");
    }

    public void addAcc(Account account) { //Można to uprościć  ----------- do całkowitej naprawy
        IO.print(account.getAccDataString());
        IO.print("\n");
    }

    // -- --


    /*
     * The method fulls cd container using data given by the user and returns it (cd container).
     * container - in this case class created to carry data;
     * */
    public ClientData addClient(ClientData cd) {

        cd.clientName = IO.printRead("Proszę podać imie i nazwisko: ");
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
//    public model.DepositAccData createDepositAcc(List<model.Account> accounts, int mainAccountNumber){
//        int maximumDuration = 24;
//        model.DepositAccData dd = new model.DepositAccData();
//
//        while(true) {
//            double amount = parseDouble(IO.printRead("Podaj wartość jaką chcesz przelać na depozyt: "));
//            if(amount > accounts.get(mainAccountNumber).getBalance()) {
//                IO.print("Wartość przekracza dostępne środki na twoim koncie!\n\n");
//            } else{
//                dd.amount = amount;
//                break;
//            }
//        }
//
//        while(true) {
//            int months = Integer.parseInt(IO.printRead("Podaj na ile miesięcy ma być wpłacony depozyt: "));
//            if(months < 0){
//                IO.print("Wartość miesięcy nie może być mniejsza od zera!\n\n");
//            } else if(months > 24){
//                IO.print("Wartość miesięcy nie może przekraczać 2 lat.\n\n");
//            } else{
//                dd.durationInMonths = months;
//                break;
//            }
//        }
//
//        return dd;
//    }


//Na razie nie używane
//    private void printAccountData(model.Account account){
//        IO.println("Numer konta:");
//        IO.println(String.valueOf(account.getAccountNumber()));
//        IO.println("Ilość dostępnych środków: " + String.valueOf(account.getBalance()));
//    }


    public int fromMainAcc() { //Function decides whether transaction will be done on main account.

        IO.println("Czy chcesz aby operacja była wykonana z głównego konta?");
        IO.print("\n");
        IO.println("0. Nie");
        IO.println("1. Tak");
        IO.print("\n");

        return IO.printReadInt("-> ");
    }

    // --

    TransactionData deposit(TransactionData dd) {

        dd.amount = parseDouble(IO.printRead("Podaj kwotę do wpłaty (depozyt - metoda) "));
        IO.print("Kwota " + Double.toString(dd.amount) + " zostanie wpłacona na twoje konto\n");

        dd.setBalanceAfterDeposit();
        dd.makeTransaction = true;

        IO.print("Stan konta po wpłacie wynosi " + Double.toString(dd.balanceAfter) + "\n\n");

        return dd;
    }

    TransactionData withdraw(TransactionData wd) {
        int cnt = 0;

        while (true) {
            wd.amount = parseDouble(IO.printRead("Podaj kwotę wypłaty: "));

            if (cnt == 3) {
                IO.println("Zbyt wiele prób");
                break;
            }

            if (wd.firstCheck()) {
                wd.makeTransaction = true;

                IO.print("Kwota " + Double.toString(wd.amount) + " zostanie pobrana z twojego konta\n");
                wd.setBalanceAfterWithdraw();
                IO.print("Wartość środków pozostałch na koncie " + Double.toString(wd.balanceAfter) + "\n\n");

                break;
            } else if (wd.secondCheck()) {
                wd.makeTransaction = true;

                IO.print("Kwota " + Double.toString(wd.amount) + "przekracza stan środków na twoim koncie\n");
                IO.print("Kwota zostanie pobrana uwzględniając limit debetu twojego konta\n");
                wd.setBalanceAfterWithdraw();
                IO.print("Wartość dostępnych środków po operacji " + Double.toString(wd.balanceAfter));

                break;
            } else {
                IO.print("Nieposiadasz odpowiednich środków do wykonania tej operajci\n");
                IO.print("Obecny stan twojego konta to " + Double.toString(wd.balance) + "\n");
                if (wd.overdraftLimit > 0) {
                    IO.print("Sumaryczna dostępna ilość środków uwzdlęniając debet " + Double.toString(wd.balance + wd.overdraftLimit) + "\n");
                }
                IO.println("Spróbuj mniejszej kwoty");
            }
            cnt++;
        }

        return wd;
    }


    TransactionData transfer(TransactionData td) { // To change
        int cnt = 0;

        td.inAccNumber = IO.printReadInt("Proszę podać numer konta do przelewu: ");

        while (true) {
            td.amount = parseDouble(IO.printRead("Podaj kwotę przelewu: "));

            if (cnt == 3) {
                IO.println("Zbyt wiele prób");
                td.makeTransaction = false;
                break;
            }

            if (td.firstCheck()) {
                td.makeTransaction = true;

                IO.print("Kwota " + Double.toString(td.amount) + " zostanie pobrana z twojego konta\n");
                td.setBalanceAfterWithdraw();
                IO.print("Wartość środków pozostałch na koncie " + Double.toString(td.balanceAfter));

                IO.print("\n\n");

                break;
            } else if (td.secondCheck()) {
                td.makeTransaction = true;

                IO.print("Kwota " + Double.toString(td.amount) + "przekracza stan środków na twoim koncie\n");
                IO.print("Kwota zostanie pobrana uwzględniając limit debetu twojego konta\n");
                IO.print("Wartość dostępnych środków po operacji " + Double.toString(td.balanceAfter));

                break;
            } else {
                IO.print("Nieposiadasz odpowiednich środków do wykonania tej operajci\n");
                IO.print("Obecny stan twojego konta to " + Double.toString(td.balance) + "\n");
                if (td.overdraftLimit > 0) {
                    IO.print("Sumaryczna dostępna ilość środków uwzdlęniając debet " + Double.toString(td.balance + td.overdraftLimit) + "\n");
                }
                IO.println("Spróbuj mniejszej kwoty");
            }
            cnt++;
        }

        return td;
    }
    // anonymous class


}
