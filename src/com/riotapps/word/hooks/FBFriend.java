package com.riotapps.word.hooks;

import java.util.Comparator;

import com.google.gson.annotations.SerializedName;
import com.riotapps.word.utils.Constants;

public class FBFriend{
	
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
	
	public int getNumWins() {
		return numWins;
	}


	public void setNumWins(int numWins) {
		this.numWins = numWins;
	}


	public Boolean isRegisteredPlayer(){
		return this.playerId.length() > 0;
	}
	
	public String getImageUrl(){
		return String.format(Constants.FACEBOOK_IMAGE_URL, this.id);
	}

	public String getBadgeDrawable(){
		return PlayerService.getBadgeDrawable(this.numWins);
	}
		
	public String getAdjustedName(int max){
		if (this.name.length() <= max) {return this.name;}
		else{
			String[] parts = this.name.split(" ");
			
			StringBuilder sb = new StringBuilder();
			for (int x = 0;x < parts.length; x++){
				if (x == 0) {
					sb.append(parts[x]);
				}
				else{
					sb.append(" ");
					sb.append(parts[x].substring(0, 1));
					sb.append(".");
				}
			}
			String adjusted = sb.toString();
			
			if (adjusted.length() > max){
				adjusted = adjusted.substring(0, max - 3) + "...";
			}
			return adjusted;
		}
	}
 


	
}
 