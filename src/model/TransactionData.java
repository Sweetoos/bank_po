package model;

public class TransactionData {

    public final int transactionId;

    public double balance;
    public double balanceAfter;

    public int outAccNumber;  //Acc. num. out of whitch money are taken.
    public int inAccNumber;   //Acc. num. it  to whitch money are going.

    public String transactionType;
    private boolean traTyp = false;

    public double amount;

    public boolean makeTransaction;
    public double overdraftLimit;

    // --

    TransactionData(int transactionId, double balance) {

        this.transactionId = transactionId;

        this.balance = balance;
        this.balanceAfter = 0;

        this.outAccNumber = -2;
        this.inAccNumber = -2;


        this.transactionType = "";

        this.amount = 0;

        this.makeTransaction = false;
        this.overdraftLimit = -1;

    }

    TransactionData(TransactionData td) {

        this.transactionId = td.transactionId;

        this.balance = 0;
        this.balanceAfter = 0;

        this.outAccNumber = td.outAccNumber;
        this.inAccNumber = td.inAccNumber;

        this.transactionType = td.transactionType;

        this.amount = td.amount;

        this.makeTransaction = td.makeTransaction;
        this.overdraftLimit = td.overdraftLimit;
    }

    public void setBalanceAfterDeposit() {
        balanceAfter = balance + amount;
    }

    public void setBalanceAfterWithdraw() {
        balanceAfter = balance - amount;
    }


    public boolean firstCheck() {
        return amount <= balance;
    }

    public boolean secondCheck() {
        return amount <= (balance + overdraftLimit);
    }

    public void setTransactionType(String transactionType) {
        if (traTyp == false) {
            this.transactionType = transactionType;
            traTyp = true;
        }
    }
}
