package com.revolut.rest;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.revolut.rest.datastore.AccountDataStore;
import com.revolut.rest.datastore.AccountDataStoreFactory;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class RESTServer {
    private static final int PORT = 8080;
    private static final String CONTEXT = "/transfer";

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_OPTIONS = "OPTIONS";
    public static final String ALLOWED_METHODS = METHOD_POST + ","+METHOD_GET + "," + METHOD_OPTIONS;
    

	    public static void main(String[] args) throws Exception {
	    	
	    	AccountDataStore dao = AccountDataStoreFactory.getAccountDAOWithSomeItems();
	    	
	        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
	        server.createContext(CONTEXT, new MyHandler(dao));
	        server.setExecutor(null);
	        server.start();
	    }

	    static class MyHandler implements HttpHandler {
	    	
	    	private AccountDataStore dao;

	        public MyHandler(AccountDataStore dao) {
				this.dao = dao;
			}

			@Override
	        public void handle(HttpExchange exchange) throws IOException {
	            try {
	                final String requestMethod = exchange.getRequestMethod().toUpperCase();
	                switch (requestMethod) {
	                    case METHOD_GET:
	                    	new TransferRequestHandler(exchange,dao).handleTransferGET();
	                        break;
	                    case METHOD_POST:
	                    	new TransferRequestHandler(exchange,dao).handleTransferPOST();
	                    	break;
	                    case METHOD_OPTIONS:
	                    	new TransferRequestHandler(exchange,dao).handleTransferOPTIONS();
	                        break;
	                    default:
	                    	new TransferRequestHandler(exchange,dao).handleTransferDefault();
	                        break;
	                }
	            } finally {
	                exchange.close();
	            }
	        }
	    }
}
