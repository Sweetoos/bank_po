package interfaces;

import model.Bank;

public interface Transactable {
    void deposit (double amount);
    boolean withdraw(double amount);
    boolean transfer(double amount, int targetAccountNumber, Bank bank);
}
