package com.exceptions;

public class InvalidUserException extends Exception{
	private String message;
	
	public InvalidUserException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
