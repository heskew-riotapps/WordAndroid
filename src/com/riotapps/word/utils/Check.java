package com.riotapps.word.utils;

 
//dirt simple design by contract precondition check
public class Check {
	public static void Require(boolean assertion, String message) throws PreconditionException {
		
		 if (!assertion) throw new PreconditionException(message);
	}
}
