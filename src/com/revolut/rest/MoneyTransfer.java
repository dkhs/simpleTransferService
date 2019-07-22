package com.revolut.rest;

import java.util.List;
import java.util.Optional;

import com.revolut.rest.datastore.AccountDataStore;
import com.revolut.rest.datastore.Transaction;

public class MoneyTransfer {
	
	private AccountDataStore dao;
	
	public MoneyTransfer(AccountDataStore dao) {
		this.dao = dao;
	}

	public MoneyTransferResponseTO transferMoney(MoneyTransferTO transfer) {
		
		MoneyTransferResponseTO resp = new MoneyTransferResponseTO(transfer);
		
		Optional<MoneyTransferResponseTO> transferNegative = checkIfTransferNegative(transfer, resp);
		if(transferNegative.isPresent()) return transferNegative.get();

		Transaction<MoneyTransferResponseTO> transaction = ()->{
			
			Optional<MoneyTransferResponseTO> accountFromDoesNotExists = checkIfAccountExist(dao, transfer.getAccountFromID(), resp);
			if(accountFromDoesNotExists.isPresent()) return accountFromDoesNotExists.get();
			
			Optional<MoneyTransferResponseTO> accountToDoesNotExists = checkIfAccountExist(dao, transfer.getAccountToID(), resp);
			if(accountToDoesNotExists.isPresent()) return accountToDoesNotExists.get();	
			
			Account accountFrom = dao.getAccount(transfer.getAccountFromID()).get();
			Account accountTo = dao.getAccount(transfer.getAccountToID()).get();
			
			if(transfer.getAmount()>accountFrom.getAmount()) {
				resp.setResponseCode(Response.NOT_ENOUGH_MONEY);
				resp.setMessage("Account does not have enough founds to support transfer such amount");
				return resp;
			}
			
			Account updatedFromAccount = new Account();
			Account updatedToAccount = new Account();
			updatedFromAccount.setId(accountFrom.getId());
			updatedToAccount.setId(accountTo.getId());
			
			updatedFromAccount.setAmount(accountFrom.getAmount()-transfer.getAmount());
			updatedToAccount.setAmount(accountTo.getAmount()+transfer.getAmount());

			if(dao.updateAccount(updatedFromAccount).equals(Response.OK)) {

				 if( dao.updateAccount(updatedToAccount).equals(Response.OK)) {
					resp.setTransactionID(dao.addTransfer(transfer));
					resp.setMessage("Succesful");
					resp.setResponseCode(Response.OK);
					return resp;
				 }
				 else {
					 return tryToRevertChanges(dao, accountFrom, accountTo, resp);
				 }
			}else {
				return tryToRevertChanges(dao, accountFrom, accountTo, resp);
			}
		};
		
		return dao.runTransactionAlike(transaction);
	}
	
	public List<MoneyTransferSuccesfulTO> getTransfers(){
		return dao.getTransfers();
	}
	
	private static MoneyTransferResponseTO tryToRevertChanges(AccountDataStore dao,Account origFrom, Account origTo,MoneyTransferResponseTO resp) {
		 dao.updateAccount(origFrom);
		 dao.updateAccount(origTo);
		 resp.setResponseCode(Response.ERROR);
		 resp.setMessage("DataStore problem");
		 return resp;
	}
	
	private static Optional<MoneyTransferResponseTO> checkIfAccountExist(AccountDataStore dao,int accountID,MoneyTransferResponseTO resp){
		if(accountID<1) {
			resp.setResponseCode(Response.ACCOUNT_DOES_NOT_EXIST);
			resp.setMessage("Account does not exist in data store - id cannot be negative nor 0");
			return Optional.of(resp);
		}
		if(!dao.getAccount(accountID).isPresent()) {
			resp.setResponseCode(Response.ACCOUNT_DOES_NOT_EXIST);
			resp.setMessage("Account does not exist in data store");
			return Optional.of(resp);		
		}
		return Optional.empty();
	}
	private static Optional<MoneyTransferResponseTO> checkIfTransferNegative(MoneyTransferTO transfer,MoneyTransferResponseTO resp){
		if(transfer.getAmount()<0.0d) {
			resp.setResponseCode(Response.TRANSFER_NEGATIVE);
			resp.setMessage("transfer amount cannot be leess the 0");
			return Optional.of(resp);
		}
		return Optional.empty();
	}
}
