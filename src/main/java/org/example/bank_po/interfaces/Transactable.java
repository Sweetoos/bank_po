package org.example.bank_po.interfaces;

import org.example.bank_po.model.Account;

public interface Transactable {
    public void deposit(double amount);
    public boolean withdraw(double amount);
    public boolean transfer(double amount, Account target);
}
