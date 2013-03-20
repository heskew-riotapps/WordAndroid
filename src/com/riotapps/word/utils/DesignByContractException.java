package com.riotapps.word.utils;

public class DesignByContractException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private int errorCode = 0;
	
	
    public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public DesignByContractException(String message) {
        super(message);
    }
    
	public DesignByContractException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
	
    public DesignByContractException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
