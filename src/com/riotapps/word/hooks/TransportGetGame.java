package com.riotapps.word.hooks;

 

import com.google.gson.annotations.SerializedName;

public class TransportGetGame {
	
	
	@SerializedName("a_t")
	private String token;
	
	@SerializedName("id")
	private String gameId;

	public TransportGetGame(){}
	
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
	
	
}
