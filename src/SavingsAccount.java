public class SavingsAccount extends Account {

    double annualInterestRate;

    SavingsAccount(int accoutnNumber, double annualInterestRate, MainVariables mv, UserInterface UI) {
        super(accoutnNumber, "Savings Account", mv, UI);

        this.annualInterestRate = annualInterestRate;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }
}
