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

	public void addToPlayerGames(String playerId, int playerOrder, String fb, String name) {
		PlayerGame pg = new PlayerGame();
		pg.setPlayerId(playerId);
		pg.setPlayerOrder(playerOrder);
		pg.setFb(fb);
		pg.setName(name);
		
		this.player_games.add(pg);
	}

	private class PlayerGame{
		@SerializedName("o")
		private int playerOrder;
		
		@SerializedName("player_id")
		private String playerId = "";
		
		@SerializedName("fb")
		private String fb = "";

		@SerializedName("f_n")
		private String firstName = "";

		@SerializedName("l_n")
		private String lastName = "";
		
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

		public String getFb() {
			return fb;
		}

		public void setFb(String fb) {
			this.fb = fb;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setName(String name) {
			//everything before first space becomes first name
			//this will be fixed naturally after the player registered via facebook
	 
			String[] parts = name.split(" ");
			
			this.firstName = parts[0];
			this.lastName = "";
			
			StringBuilder sb = new StringBuilder();
			for (int x = 1; x < parts.length; x++){
				sb.append(parts[x]);
				sb.append(" ");
			}
			
			this.lastName = sb.toString().trim();
		}
		
	}
	
}
