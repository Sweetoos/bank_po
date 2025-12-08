package model;

public class CheckingAccount extends Account {

    CheckingAccount(int accountNumber, MainVariables mv, UserInterface UI) {
        super(accountNumber, "model.CheckingAccount", mv, UI);
    }

    @Override
    public int deposit(){
        TransactionData dd = new TransactionData(mv.newTransactionId(), balance);
        dd.outAccNumber = (-3); //Means this transaction is coming from nowhere.
        dd.inAccNumber = accountNumber;
        dd.setTransactionType("model.Deposit");

        dd = UI.deposit(dd); //Getting model.DepositData from the user. UI Class

        if(dd.makeTransaction) {
            Transaction internal = new Transaction(dd);

            dd.setBalanceAfterDeposit();
            balance = dd.balanceAfter;
            return 0;
        }

        return 1;
    }

    @Override
    public int withdraw(){

        TransactionData wd = new TransactionData(mv.newTransactionId(), balance);

        wd.outAccNumber = accountNumber;
        wd.inAccNumber = (-4); //Means this transaction is going nowhere.
        wd.setTransactionType("Withdraw");

        wd = UI.withdraw(wd);

        if(wd.makeTransaction) {
            Transaction internal = new Transaction(wd);
            this.balance  = wd.balanceAfter;
            return 0;
        }

        return 1;
    }

    @Override
    public TransactionData transfer(){
        TransactionData td = new TransactionData(mv.newTransactionId(), balance);
        td.outAccNumber = accountNumber;
        td.inAccNumber = (-5); //Means this transaction is transfered, but this parameter needs to be set.
        td.setTransactionType("Transfer");
        td.overdraftLimit = 0; //To be set by ...

        td = UI.transfer(td); //Getting data from the user. UI Class.

        if(td.makeTransaction) {
            //Możliwość dodania wyjątku
        }

        if(td.makeTransaction) {
            Transaction outgoing = new Transaction(td);


            balance = td.balanceAfter;
        }

        TransactionData moneyTransfer = new TransactionData(td);

        return moneyTransfer;
    }

    @Override
    public void receiveTransfer(TransactionData tr){

        tr.balance = balance;
        IO.println("balance: " + Double.toString(balance));
        tr.setBalanceAfterDeposit();

        balance = tr.balanceAfter;

        Transaction incoming = new Transaction(tr);
    }
}