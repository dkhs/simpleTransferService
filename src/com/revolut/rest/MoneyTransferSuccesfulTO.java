package com.revolut.rest;

public class MoneyTransferSuccesfulTO extends MoneyTransferTO {
	private Integer transactionID;

	public MoneyTransferSuccesfulTO(MoneyTransferTO from) {
		this.setAccountFromID(from.getAccountFromID());
		this.setAccountToID(from.getAccountToID());
		this.setAmount(from.getAmount());
	}

	public Integer getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(Integer transactionID) {
		this.transactionID = transactionID;
	}

}
