package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;
import com.riotapps.word.utils.Constants;

public class FBFriend {
	
	public FBFriend(){}
	
	private String id;
	
	@SerializedName("n")
	private String name;
	
	@SerializedName("p")
	private String playerId = "";
	
	@SerializedName("w")
	private int numWins = 0;
	
	public String getPlayerId() {
		return playerId;
	}
	
	
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isRegisteredPlayer(){
		return this.playerId.length() > 0;
	}
	
	public String getImageUrl(){
		return String.format(Constants.FACEBOOK_IMAGE_URL, this.id);
	}

	public Player getPlayer(){
		return new Player();
	}
	
}
