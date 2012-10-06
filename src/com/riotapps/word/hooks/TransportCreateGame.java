package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class TransportCreateGame {

	@SerializedName("a_t")
	private String token;
	
	@SerializedName("player_games")
	private List<PlayerGame> player_games = new ArrayList<PlayerGame>();
	
	
	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}

	
	public List<PlayerGame> getPlayerGames() {
		return player_games;
	}


	public void setPlayerGames(List<PlayerGame> player_games) {
		this.player_games = player_games;
	}

	public void addToPlayerGames(String playerId, int playerOrder) {
		PlayerGame pg = new PlayerGame();
		pg.setPlayerId(playerId);
		pg.setPlayerOrder(playerOrder);
		
		this.player_games.add(pg);
	}

	private class PlayerGame{
		@SerializedName("o")
		private int playerOrder;
		
		@SerializedName("player_id")
		private String playerId;

		public int getPlayerOrder() {
			return playerOrder;
		}

		public void setPlayerOrder(int playerOrder) {
			this.playerOrder = playerOrder;
		}

		public String getPlayerId() {
			return playerId;
		}

		public void setPlayerId(String playerId) {
			this.playerId = playerId;
		}
		
	}
	
}
