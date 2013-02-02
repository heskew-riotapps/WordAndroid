package com.riotapps.word.hooks;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class TransportDeclineGame {
	@SerializedName("a_t")
	private String token;
	
	@SerializedName("id")
	private String gameId;
	
	@SerializedName("c_g_d")
	private Date completedGameDate;

	public Date getCompletedGameDate() {
		return completedGameDate;
	}

	public void setCompletedGameDate(Date completedGameDate) {
		this.completedGameDate = completedGameDate;
	}

	public TransportDeclineGame(){}
	
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