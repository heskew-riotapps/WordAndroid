package com.riotapps.word.utils;

 

public class PreconditionException extends DesignByContractException {
	private static final long serialVersionUID = 1L;
    public PreconditionException(String message) {
        super(message);
    }
    
    public PreconditionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
 
 