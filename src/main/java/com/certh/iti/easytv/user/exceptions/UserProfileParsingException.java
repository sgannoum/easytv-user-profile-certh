package com.certh.iti.easytv.user.exceptions;

public class UserProfileParsingException extends java.lang.Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserProfileParsingException(String message){
		super(message);
	}
	
	public UserProfileParsingException(String message, Throwable cause){
		super(message, cause);
	}

	public UserProfileParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public UserProfileParsingException(Throwable cause){
		super(cause);
	}
}
