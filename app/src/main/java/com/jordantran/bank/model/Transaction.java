package com.jordantran.bank.model;

public class Transaction {

    private String transactionType;
    private double amount;

    public Transaction(String transactionType, double amount) {
        this.transactionType = transactionType;
        this.amount = amount;
    }

    /*
     * Returns status of a transaction in terms of transaction type and amount
     */
    public String getStatus() {
        return String.format("Transaction %s: $%.2f", this.transactionType, this.amount);
    }

}
