package com.revolut.rest.datastore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.revolut.rest.Account;
import com.revolut.rest.MoneyTransferSuccesfulTO;
import com.revolut.rest.MoneyTransferTO;
import com.revolut.rest.Response;

public class AccountDataStore {
	private HashMap<Integer, Account> dataStore = new HashMap<Integer, Account>();

	private int accountIDGenerator = 1;

	private List<MoneyTransferSuccesfulTO> transfers = new ArrayList<MoneyTransferSuccesfulTO>();

	AccountDataStore() {
	}

	public Optional<Account> getAccount(Integer id) {
		return Optional.ofNullable((dataStore.get(id)));
	}

	public synchronized Response addAccount(Account account) {

		if (account == null) {
			return Response.SPECIFY_ACCOUNT;
		}

		account.setId(accountIDGenerator++);

		dataStore.put(account.getId(), account);
		return Response.OK;
	}

	public synchronized Response updateAccount(Account account) {
		if (account == null) {
			return Response.SPECIFY_ACCOUNT;
		}
		Optional<Account> inStoreAccount = getAccount(account.getId());
		if (!inStoreAccount.isPresent())
			return Response.ACCOUNT_DOES_NOT_EXIST;
		dataStore.put(account.getId(), account);
		return Response.OK;
	}

	public int addTransfer(MoneyTransferTO transfer) {
		// id starting from 0 - would fix for real
		MoneyTransferSuccesfulTO successTransfer = new MoneyTransferSuccesfulTO(transfer);
		transfers.add(successTransfer);
		successTransfer.setTransactionID(transfers.size() - 1);
		return transfers.size() - 1;
	}

	public List<MoneyTransferSuccesfulTO> getTransfers() {
		return transfers;
	}

	public synchronized <T> T runTransactionAlike(Transaction<T> transaction) {
		return transaction.doTransaction();
	}

}
