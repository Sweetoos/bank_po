package model;

public interface Transactable {
    public void deposit(double amount);
    public boolean withdraw(double amount);
    public boolean transfer(double amount, Account target);
}
