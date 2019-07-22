package com.revolut.rest.datastore;

public interface Transaction<T> {
	T doTransaction();
}
