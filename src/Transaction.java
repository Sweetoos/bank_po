public class Transaction {

    public final int    transactionId;

    public final double balance;
    public final double balanceAfter;

    public final int    outAccountId;
    public final int    inAccountId;

    public final String transactionType;

    public final double amount;

    public Transaction(TransactionData td) {

        this.transactionId   = td.transactionId;

        this.balance         = td.balance;
        this.balanceAfter    = td.balanceAfter;

        this.outAccountId    = td.outAccNumber;
        this.inAccountId     = td.inAccNumber;

        this.transactionType = td.transactionType;

        this.amount          = td.amount;
    }
}
