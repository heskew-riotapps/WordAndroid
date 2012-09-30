package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;

public class TransportPasswordChange {
	@SerializedName("a_t")
	private String token;

	@SerializedName("p_w")
	private String password;

	public TransportPasswordChange(){}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password.trim();
	}
	
	
	
}
