public class DepositAccData {
    public int    depositId;
    public String depositName;

    public double amount;
    public double interestRate;

    public double durationInMonths;

    public DepositAccData() {
        this.depositId = 0;
        this.depositName = null;

        this.amount = 0.0;
        this.interestRate = 0.0;

        this.durationInMonths = 0.0;
    }
}
