package com.certh.iti.easytv.user.exceptions;

public class UserContentParsingException extends UserProfileParsingException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public UserContentParsingException(String message){
		super(message);
	}
	
	public UserContentParsingException(String message, Throwable cause){
		super(message, cause);
	}

	public UserContentParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public UserContentParsingException(Throwable cause){
		super(cause);
	}
	

}
