package interfaces;

import model.TransactionData;

public interface Transactable {
    public int deposit ();
    public int withdraw();
    public TransactionData transfer();
}
