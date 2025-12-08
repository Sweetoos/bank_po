package model;

import java.util.Scanner;

public final class IOConsole {

    private final String appVersion;

    private final Scanner in;

    public boolean endPr;

    private IOConsole(String appVersion) {
        this.appVersion = appVersion;
        this.in = new Scanner(System.in);
        this.endPr = false;
    }

    public static IOConsole start(String appVersion) {
        IOConsole UI = new IOConsole(appVersion);

        UI.greeting();

        return UI;
    }

    public void greeting() {
        System.out.println("Witaj");
        System.out.println("Jest to program symulujący model.Bank");
        System.out.println("Wersja " + this.appVersion + "\n");

    }

    public void print(String message) {

        System.out.print(message);
    }

    public void println(String message) {

        System.out.println(message);
    }

    public String read() {
        String line = in.nextLine();
        exitProgram(line);

        return line;
    }

    public String printRead(String message) {
        System.out.print(message);
        String line = in.nextLine();

        exitProgram(line);
        return line;
    }

    public int printReadInt(String message) {
        System.out.print(message);
        String line = in.nextLine();

        exitProgram(line);
        return Integer.parseInt(line);
    }


    private void exitProgram(String line) {
        if (line.equals("exit")) {
            System.exit(0);
        } else if (line.equals("")) {
            System.exit(0);
        }
        endProgram(line);
    }

    private void endProgram(String line) {
        if (line.equals("end")) {
            this.endPr = true;
        }
    }

    public boolean isEndPr() {
        if (this.endPr) {
            this.endPr = false;
            return true;
        }
        return false;
    }


}
