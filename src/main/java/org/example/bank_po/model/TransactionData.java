package org.example.bank_po.model;

public class TransactionData {

    public final int     transactionId;

    public final double  balance;
    public       double  balanceAfter;

    public       int     outAccountId;
    public       int     inAccountId;

    public       String  transactionType; public boolean traTyp = false;

    public       double  amount;

    public       boolean makeTransaction;
    public       double  overdraftLimit;

    TransactionData(int transactionId, double balance){

        this.transactionId = transactionId;

        this.balance = balance;
        this.balanceAfter = 0;

        this.outAccountId = -2;
        this.inAccountId  = -2;

        this.transactionType = "";

        this.amount = 0;

        this.makeTransaction = false;
        this.overdraftLimit = overdraftLimit;
    }

    public void setBalanceAfterDeposit() { balanceAfter = balance + amount; }
    public void setBalanceAfterWithdraw(){ balanceAfter = balance - amount; }


    public boolean firstCheck()  { return amount <= balance; }
    public boolean secondCheck(){
        return amount <= (balance + overdraftLimit);
    }


    public void setTransactionType(String transactionType){
        if(traTyp == false){
            this.transactionType = transactionType;
            traTyp = true;
        }
    }
}
