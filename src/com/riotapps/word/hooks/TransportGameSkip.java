package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;
 
public class TransportGameSkip {
	
	@SerializedName("a_t")
	private String token;
	
	@SerializedName("id")
	private String gameId;
	
	@SerializedName("t")
	private int turn;

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

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	
}
