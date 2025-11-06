package org.example.bank_po.model;

import java.util.List;

import static java.lang.Double.parseDouble;

public class UserInterface {
    public final String appVersion;
    public final IOConsole IO;

    public UserInterface() {
        this.appVersion = "2.1.8";
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
    public DepositAccData createDepositAcc(List<Account> accounts, int mainAccountNumber){
        int maximumDuration = 24;
        DepositAccData dd = new DepositAccData();

        while(true) {
            double amount = parseDouble(IO.printRead("Podaj wartość jaką chcesz przelać na depozyt: "));
            if(amount > accounts.get(mainAccountNumber).getBalance()) {
                IO.print("Wartość przekracza dostępne środki na twoim koncie!\n\n");
            } else{
                dd.amount = amount;
                break;
            }
        }

        while(true) {
            int months = Integer.parseInt(IO.printRead("Podaj na ile miesięcy ma być wpłacony depozyt: "));
            if(months < 0){
                IO.print("Wartość miesięcy nie może być mniejsza od zera!\n\n");
            } else if(months > 24){
                IO.print("Wartość miesięcy nie może przekraczać 2 lat.\n\n");
            } else{
                dd.durationInMonths = months;
                break;
            }
        }

        return dd;
    }

    private void printAccountData(Account account){
        IO.println("Numer konta:");
        IO.println(String.valueOf(account.getAccountNumber()));
        IO.println("Ilość dostępnych środków: " + String.valueOf(account.getBalance()));
    }


    // --


    TransactionData withdraw(TransactionData wd){
        int cnt = 0;

        while(true) {
            wd.amount = parseDouble(IO.printRead("Podaj kwotę wypłaty: "));

            if(cnt == 3){
                IO.println("Zbyt wiele prób");
                break;
            }

            if(wd.firstCheck()) {
                wd.makeTransaction = true;

                IO.print("Kwota " + Double.toString(wd.amount) + " zostanie pobrana z twojego konta\n");
                IO.print("Wartość środków pozostałch na koncie " + Double.toString(wd.balanceAfter) );

                break;
            } else if (wd.secondCheck()) {
                wd.makeTransaction = true;

                IO.print("Kwota " + Double.toString(wd.amount) + "przekracza stan środków na twoim koncie\n");
                IO.print("Kwota zostanie pobrana uwzględniając limit debetu twojego konta\n");
                IO.print("Wartość dostępnych środków po operacji " + Double.toString(wd.balanceAfter) );

                break;
            } else {
                IO.print("Nieposiadasz odpowiednich środków do wykonania tej operajci\n");
                IO.print("Obecny stan twojego konta to " + Double.toString(wd.balance) + "\n");
                if(wd.overdraftLimit > 0) {
                    IO.print("Sumaryczna dostępna ilość środków uwzdlęniając debet " + Double.toString(wd.balance + wd.overdraftLimit) + "\n");
                }
                IO.println("Spróbuj mniejszej kwoty");
                }
            cnt++;
        }

        return wd;
    }

    TransactionData deposit(TransactionData dd){

        dd.amount = parseDouble(IO.printRead("Podaj kwotę do wpłaty "));
        IO.print("Kwota "  + Double.toString(dd.amount) + " zostanie wpłacona na twoje konto\n");

        dd.setBalanceAfterDeposit();
        dd.makeTransaction = true;

        IO.print("Stan konta po wpłacie wynosi " + Double.toString(dd.balanceAfter) + "\n\n");

        return dd;
    }

    TransactionData transfer(TransactionData wd){
        int cnt = 0;

        while(true) {
            wd.amount = parseDouble(IO.printRead("Podaj kwotę wypłaty: "));

            if(cnt == 3){
                IO.println("Zbyt wiele prób");
                break;
            }

            if(wd.firstCheck()) {
                wd.makeTransaction = true;

                IO.print("Kwota " + Double.toString(wd.amount) + " zostanie pobrana z twojego konta\n");
                IO.print("Wartość środków pozostałch na koncie " + Double.toString(wd.balanceAfter) );

                break;
            } else if (wd.secondCheck()) {
                wd.makeTransaction = true;

                IO.print("Kwota " + Double.toString(wd.amount) + "przekracza stan środków na twoim koncie\n");
                IO.print("Kwota zostanie pobrana uwzględniając limit debetu twojego konta\n");
                IO.print("Wartość dostępnych środków po operacji " + Double.toString(wd.balanceAfter) );

                break;
            } else {
                IO.print("Nieposiadasz odpowiednich środków do wykonania tej operajci\n");
                IO.print("Obecny stan twojego konta to " + Double.toString(wd.balance) + "\n");
                if(wd.overdraftLimit > 0) {
                    IO.print("Sumaryczna dostępna ilość środków uwzdlęniając debet " + Double.toString(wd.balance + wd.overdraftLimit) + "\n");
                }
                IO.println("Spróbuj mniejszej kwoty");
            }
            cnt++;
        }

        return wd;
    }



}
