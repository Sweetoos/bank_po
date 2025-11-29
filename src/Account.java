import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Transactable{

    public    final int               accoutnNumber;
    public    final String            accountType;

    protected       double            balance;
    protected final List<Transaction> transactionHistory;

    protected final MainVariables     mv;
    protected final UserInterface     UI;

    Account(int accoutnNumber, String accountType,  MainVariables mv, UserInterface UI) {

        this.accoutnNumber = accoutnNumber;
        this.accountType   = accountType;

        this.balance = 0;
        this.transactionHistory = new ArrayList<>();

        this.mv = mv;
        this.UI = UI;
    }

    protected double getBalance() {
        return balance;
    }
    protected int getAccountNumber(){
        return accoutnNumber;
    }

    protected List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public String getAccDataString(){
        String result = "";

        result += "Account Number: " + this.accoutnNumber + "\n";
        result += "Account   Type: " + this.accountType   + "\n";
        result += "       Balance: " + this.balance;

        return result;
    }

    public int             deposit() { return 0; }

    public int             withdraw(){
        return 0;
    }

    public TransactionData transfer(){ return new TransactionData(-1, -1); }

    public void            reciveTransfer(TransactionData tr) { return; }

}
