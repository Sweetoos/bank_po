package org.example.bank_po.model;

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
