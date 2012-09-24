package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;

public class TransportAuthToken {
	@SerializedName("a_t")
	private String token;

	public TransportAuthToken(){}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
