package model;

public class SavingsAccount extends Account {

    double annualInterestRate;

    SavingsAccount(int accountNumber, double annualInterestRate, MainVariables mv, UserInterface UI) {
        super(accountNumber, "Savings model.Account", mv, UI);

        this.annualInterestRate = annualInterestRate;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }
}
