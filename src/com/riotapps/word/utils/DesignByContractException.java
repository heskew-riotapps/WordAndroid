package com.riotapps.word.utils;

public class DesignByContractException extends Exception {
	private static final long serialVersionUID = 1L;
    public DesignByContractException(String message) {
        super(message);
    }
    
    public DesignByContractException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
