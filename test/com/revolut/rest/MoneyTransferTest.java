package com.revolut.rest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.revolut.rest.datastore.AccountDataStore;
import com.revolut.rest.datastore.AccountDataStoreFactory;

class MoneyTransferTest {

	private MoneyTransfer mt;
	private AccountDataStore dao;
	
	@BeforeEach
	void setUp() {
		dao=AccountDataStoreFactory.getAccountDAOWithSomeItems();
		mt = new MoneyTransfer(dao);
	}
	
	@Test
	final void whenSufficientFoundsAndAccountsExistTransferMoney() {
		MoneyTransferTO moneyT = new MoneyTransferTO();
		moneyT.setAccountFromID(2);
		moneyT.setAccountToID(1);
		moneyT.setAmount(500.00d);
		MoneyTransferResponseTO resp= mt.transferMoney(moneyT);
		
		assertEquals(Response.OK, resp.getResponseCode());
		assertEquals("Succesful", resp.getMessage());
		assertEquals(0, resp.getTransactionID());
		
		//check transfer exist
		List<MoneyTransferSuccesfulTO> transfers= dao.getTransfers();
		assertEquals(1, transfers.size());
	}
	
	@Test
	final void whenNotSufficientFoundsToTransferFromReturnWarningMessage() {
		MoneyTransferTO moneyT = new MoneyTransferTO();
		moneyT.setAccountFromID(2);
		moneyT.setAccountToID(1);
		moneyT.setAmount(500.01d);
		MoneyTransferResponseTO resp= mt.transferMoney(moneyT);
		assertEquals(Response.NOT_ENOUGH_MONEY, resp.getResponseCode());
		assertEquals("Account does not have enough founds to support transfer such amount", resp.getMessage());
		
	}
	
	@Test
	final void whenTransferNegativeReturnApproperiateResponse() {
		MoneyTransferTO moneyT = new MoneyTransferTO();
		moneyT.setAccountFromID(2);
		moneyT.setAccountToID(1);
		moneyT.setAmount(-0.01d);
		MoneyTransferResponseTO resp= mt.transferMoney(moneyT);
		assertEquals(Response.TRANSFER_NEGATIVE, resp.getResponseCode());
		assertEquals("transfer amount cannot be leess the 0", resp.getMessage());
		
	}
	
	@Test
	final void whenNoAccountSpecifiedReturnApproperiateResponse() {
		MoneyTransferTO moneyT = new MoneyTransferTO();
		moneyT.setAccountFromID(0);
		MoneyTransferResponseTO resp= mt.transferMoney(moneyT);
		assertEquals(Response.ACCOUNT_DOES_NOT_EXIST, resp.getResponseCode());
		assertEquals("Account does not exist in data store - id cannot be negative nor 0", resp.getMessage());
		
		moneyT.setAccountFromID(-1);
		resp= mt.transferMoney(moneyT);
		assertEquals(Response.ACCOUNT_DOES_NOT_EXIST, resp.getResponseCode());
		assertEquals("Account does not exist in data store - id cannot be negative nor 0", resp.getMessage());
		
		moneyT.setAccountFromID(1);
		moneyT.setAccountToID(0);
		resp= mt.transferMoney(moneyT);
		assertEquals(Response.ACCOUNT_DOES_NOT_EXIST, resp.getResponseCode());
		assertEquals("Account does not exist in data store - id cannot be negative nor 0", resp.getMessage());
		
		moneyT.setAccountToID(-1);
		resp= mt.transferMoney(moneyT);
		assertEquals(Response.ACCOUNT_DOES_NOT_EXIST, resp.getResponseCode());
		assertEquals("Account does not exist in data store - id cannot be negative nor 0", resp.getMessage());
	}
	
	@Test
	final void whenAccountDoesNotExistReturnApproperiateResponse() {
		MoneyTransferTO moneyT = new MoneyTransferTO();
		moneyT.setAccountFromID(6);
		moneyT.setAccountToID(1);
		MoneyTransferResponseTO resp= mt.transferMoney(moneyT);
		assertEquals(Response.ACCOUNT_DOES_NOT_EXIST, resp.getResponseCode());
		assertEquals("Account does not exist in data store", resp.getMessage());
		
		moneyT.setAccountFromID(1);
		moneyT.setAccountToID(7);
		resp= mt.transferMoney(moneyT);
		assertEquals(Response.ACCOUNT_DOES_NOT_EXIST, resp.getResponseCode());
		assertEquals("Account does not exist in data store", resp.getMessage());
	}
	
	
}
