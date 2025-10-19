package org.example.bank_po.model;

import org.example.bank_po.interfaces.Asset;

import java.time.LocalDate;

public class Deposit implements Asset {
    private int depositId;
    private double amount;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private DepositType depositType;

    public Deposit(int depositId, double amount, LocalDate openingDate, DepositType depositType) {
        this.depositId = depositId;
        this.amount = amount;
        this.openingDate = openingDate;
        this.closingDate = openingDate.plusMonths(depositType.getDurationInMonths());
        this.depositType = depositType;
    }

    @Override
    public double getValue() {
        return 0;
    }

    public int getDepositId() { return 0; }
    public double getAmount() { return 0.0; }
    public LocalDate getOpeningDate() { return null; }
    public LocalDate getClosingDate() { return null; }
    public DepositType getDepositType() { return null; }

}
