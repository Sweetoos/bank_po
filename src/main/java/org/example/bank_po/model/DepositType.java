package org.example.bank_po.model;

//Class not used. To delete.
public class DepositType {
    public final String name;
    public final double interestRate;
    public final int    durationInMonths;

    DepositType(String name, double interestRate, int durationInMonths) {
        this.name = name;
        this.interestRate = interestRate;
        this.durationInMonths = durationInMonths;
    }
}
