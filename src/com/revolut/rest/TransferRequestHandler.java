package com.revolut.rest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;
import com.revolut.rest.datastore.AccountDataStore;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class TransferRequestHandler {

	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	
	static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final int NO_RESPONSE_LENGTH = -1;
	
	private static final int STATUS_OK = 200;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;
    private static final String HEADER_ALLOW = "Allow";
	
	static final String HEADER_CONTENT_TYPE_VALUE = "application/json; charset=" + CHARSET;

	private HttpExchange exchange;

	private AccountDataStore dao;

	public TransferRequestHandler(HttpExchange exchange, AccountDataStore dao) {
		this.exchange = exchange;
		this.dao = dao;
	}

	public void handleTransferPOST() throws IOException {
		Headers headers = exchange.getResponseHeaders();
		MoneyTransferTO mt = ParameterParser.parseFromJSON(ParameterParser.readBody((exchange.getRequestBody())));
		
		MoneyTransfer transfer = new MoneyTransfer(dao);
		MoneyTransferResponseTO resp = transfer.transferMoney(mt);
		
		Gson gson = new Gson();
		String responseBody = gson.toJson(resp);
		
		headers.set(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE);
		
		byte[] byteBody = responseBody.getBytes(CHARSET);
		exchange.sendResponseHeaders(STATUS_OK, byteBody.length);
		exchange.getResponseBody().write(byteBody);
	}
	
	public void handleTransferGET() throws IOException {
		Headers headers = exchange.getResponseHeaders();
		
		MoneyTransfer transfer = new MoneyTransfer(dao);
		List<MoneyTransferSuccesfulTO> resp = transfer.getTransfers();
		
		Gson gson = new Gson();
		String responseBody = gson.toJson(resp);
		
		headers.set(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE);
		
		byte[] byteBody = responseBody.getBytes(CHARSET);
		exchange.sendResponseHeaders(STATUS_OK, byteBody.length);
		exchange.getResponseBody().write(byteBody);
	}
	
	public void handleTransferOPTIONS() throws IOException {
		Headers headers = exchange.getResponseHeaders();
        headers.set(HEADER_ALLOW, RESTServer.ALLOWED_METHODS);
        exchange.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
	}
	
	public void handleTransferDefault() throws IOException {
		Headers headers = exchange.getResponseHeaders();
        headers.set(HEADER_ALLOW, RESTServer.ALLOWED_METHODS);
        exchange.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
	}

}
