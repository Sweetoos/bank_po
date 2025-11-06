package org.example.bank_po.model;

public class CheckingAccount extends Account{

    private double overdraftLimit;

    CheckingAccount(int accoutnNumber, MainVariables mv, UserInterface UI) {
        super(accoutnNumber, "CheckingAccount", mv, UI);

        this.overdraftLimit = 0;
    }

    @Override
    public int withdraw(){
        TransactionData wd = new TransactionData(mv.newTransactionId(), balance);
            wd.outAccountId = accoutnNumber;
            wd.inAccountId  = (-4); //Means this transaction is going nowhere.
            wd.setTransactionType("Withdraw");

        wd = UI.withdraw(wd); //Getting WithdrawData from the user.

        if(wd.makeTransaction) {
            Transaction internal = new Transaction(wd);

            wd.setBalanceAfterWithdraw();
            balance = wd.balanceAfter;
            return 0;
        }

        return 1;
    }

    @Override
    public int deposit(){
        TransactionData dd = new TransactionData(mv.newTransactionId(), balance);
            dd.outAccountId = (-3); //Means this transaction is coming from nowhere.
            dd.inAccountId  = accoutnNumber;
            dd.setTransactionType("Deposit");

        dd = UI.deposit(dd); //Getting DepositData from the user.

        if(dd.makeTransaction) {
            Transaction internal = new Transaction(dd);

            dd.setBalanceAfterDeposit();
            balance = dd.balanceAfter;
            return 0;
        }

        return 1;
    }

    @Override
    public int transfer(){
        TransactionData td =  new TransactionData(mv.newTransactionId(), balance);
        td.outAccountId = accoutnNumber;
        td.inAccountId  = (-5); //Means this transaction is transfered, but this parameter needs to be set.
        td.setTransactionType("Transfer");
        td.overdraftLimit = overdraftLimit;

        td = UI.withdraw(td); //Getting data from the user.

        if(td.makeTransaction) {
            Transaction internal = new Transaction(td);

            balance = td.balanceAfter;

            return 0;
        }

        return 1;
    }
}
