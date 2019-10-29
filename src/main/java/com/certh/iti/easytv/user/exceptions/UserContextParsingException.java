package com.certh.iti.easytv.user.exceptions;

public class UserContextParsingException extends UserProfileParsingException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public UserContextParsingException(String message){
		super(message);
	}
	
	public UserContextParsingException(String message, Throwable cause){
		super(message, cause);
	}

	public UserContextParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public UserContextParsingException(Throwable cause){
		super(cause);
	}
	

}
