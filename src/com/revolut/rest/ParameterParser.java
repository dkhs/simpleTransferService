package com.revolut.rest;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import com.google.gson.Gson;

public class ParameterParser {

	static final Charset CHARSET = StandardCharsets.UTF_8;

	static MoneyTransferTO parseFromJSON(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, MoneyTransferTO.class);
	}

	static String readBody(InputStream bodyStream) {
		return new BufferedReader(new InputStreamReader(bodyStream, CHARSET)).lines().collect(Collectors.joining("\n"));
	}
}
