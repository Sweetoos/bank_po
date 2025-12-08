package org.example.bank_po.model;

import java.time.LocalDate;

public class Deposit {

    public  final int depositId;
    public  final String depositName;
    public  final double amount;
    public  final double interestRate;
    public  final double durationInMonths;
    public  final LocalDate openingDate;
    public  final LocalDate closingDate;

    public Deposit(DepositAccData dd) {
        this.depositId = dd.depositId;
        this.depositName = dd.depositName;

        this.amount = dd.amount;
        this.interestRate = dd.interestRate;

        this.durationInMonths = dd.durationInMonths;

        this.openingDate = null;
        this.closingDate = null;
    }
}
