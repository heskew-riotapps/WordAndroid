package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;
 
public class TransportGameChat {
	
	@SerializedName("a_t")
	private String token;
	
	@SerializedName("id")
	private String gameId;
	
	@SerializedName("t")
	private String text;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
 
	
}
