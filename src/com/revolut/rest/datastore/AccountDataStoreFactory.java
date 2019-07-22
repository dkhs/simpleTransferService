package com.revolut.rest.datastore;

import com.revolut.rest.Account;

public class AccountDataStoreFactory {
	public static AccountDataStore getAccountDAOWithSomeItems() {
		AccountDataStore dao = new AccountDataStore();

		// add some accounts
		
		//id=1
		Account ac1 = new Account();
		ac1.setAmount(20000.00);
		dao.addAccount(ac1);
		//id=2
		Account ac2 = new Account();
		ac2.setAmount(500.00);
		dao.addAccount(ac2);
		//id=3
		Account ac3 = new Account();
		ac3.setAmount(20.00);
		dao.addAccount(ac3);
		//id=4
		Account ac4 = new Account();
		ac4.setAmount(6000.00);
		dao.addAccount(ac4);

		return dao;
	}
}
