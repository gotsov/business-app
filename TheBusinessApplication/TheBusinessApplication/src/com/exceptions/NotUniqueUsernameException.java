package com.exceptions;

public class NotUniqueUsernameException extends Exception{

private String message;
	
	public NotUniqueUsernameException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}

