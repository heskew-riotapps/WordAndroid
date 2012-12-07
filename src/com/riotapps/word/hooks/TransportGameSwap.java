package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
 
public class TransportGameSwap {
	
	@SerializedName("a_t")
	private String token;
	
	@SerializedName("id")
	private String gameId;
	
	@SerializedName("t")
	private int turn;
	
	@SerializedName("s_l")
	List<String> swappedLetters = new ArrayList<String>();
	
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

	public List<String> getSwappedLetters() {
		return swappedLetters;
	}

	public void setSwappedLetters(List<String> swappedLetters) {
		this.swappedLetters = swappedLetters;
	}

	
}
