package com.exceptions;

public class NegativeQuantityException extends Exception{
	
	private String message;
	
	public NegativeQuantityException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
