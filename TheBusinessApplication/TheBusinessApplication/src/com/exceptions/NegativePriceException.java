package com.exceptions;

public class NegativePriceException extends Exception{

	private String message;
	
	public NegativePriceException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
