package mf.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResponseComplex {
	private String message;
	private String cash;
	private List<Map<String, String>> funds = new ArrayList<>();

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
		this.cash = cash;
	}

	public List<Map<String, String>> getFunds() {
		return funds;
	}

	public void setFunds(List<Map<String, String>> funds) {
		this.funds = funds;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}