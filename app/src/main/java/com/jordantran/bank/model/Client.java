package com.jordantran.bank.model;

public class Client {

	
	private final int MAX_TRANSACTIONS = 10;
    private String name;
    private double balance;
    private Transaction[] transactions;
    private int not; // number of transactions

    public Client(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.not = 0;
        this.transactions = new Transaction[MAX_TRANSACTIONS]; 
    }
    
    /*
     * Returns status of client, in terms of name and balance
     */
    public String getStatus() {
    	return String.format("%s: $%.2f", this.name, this.balance);
    }
    
    
    /*
     * Returns the client's current status and a list of transaction history
     */
    public String[] getStatement() {
    	String[] result = new String[this.not+1]; //1 extra index for status to be at index 0, and rest are history of transactions
    	
    	//current status is retrieved each time getStatement is invoked
    	result[0] = this.getStatus();
    	
    	//only retrieving the values that never change --> transaction history, so index 1 and above
    	for(int i = 0; i < this.not; i++) { 
    		result[i+1] = this.transactions[i].getStatus();
    	}
    	return result;
    }
    
    /*
     * Adds deposit transaction object to transactions array, and increases balance to amount deposited 
     */
    public void deposit(double amount) {
    	addTransaction("DEPOSIT", amount);
    	
    	this.balance += amount;
    
    	
    }
    
    /*
     * Adds withdraw transaction object to transactions array, and decrease balance from amount withdrawn 
     */
    public void withdraw(double amount) {
    	
    	addTransaction("WITHDRAW", amount);
    	
    	this.balance -= amount;
    
  
    }
    
    
    public String getName() {
    	return this.name;
    }
    
    public double getBalance() {
    	return this.balance;
    }
    
    /*
     * Appends a transaction object to the transactions array based on type and amount, and increases number of transactions counter. 
     */
    public void addTransaction(String transactionType, double amount) {
    	this.transactions[this.not] = new Transaction(transactionType, amount);
    	this.not++;
    }



}
