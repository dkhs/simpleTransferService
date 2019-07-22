package com.revolut.rest;

public class MoneyTransferTO {
	private int accountFromID;
	private int accountToID;
	private double amount;
	
	public int getAccountFromID() {
		return accountFromID;
	}
	public void setAccountFromID(int accountFromID) {
		this.accountFromID = accountFromID;
	}
	public int getAccountToID() {
		return accountToID;
	}
	public void setAccountToID(int accountToId) {
		this.accountToID = accountToId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
