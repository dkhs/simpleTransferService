package com.revolut.rest;

public class MoneyTransferResponseTO {
	Response responseCode;
	String message = "";
	Integer transactionID;
	MoneyTransferTO origTransfer;

	public MoneyTransferResponseTO(MoneyTransferTO origTransfer) {
		this.origTransfer = origTransfer;
	}

	public Response getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Response responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(Integer transactionID) {
		this.transactionID = transactionID;
	}

	public MoneyTransferTO getOrigTransfer() {
		return origTransfer;
	}

}
