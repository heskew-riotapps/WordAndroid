package com.riotapps.word.hooks;

import java.util.Comparator;

import com.google.gson.annotations.SerializedName;
import com.riotapps.word.ChooseFBFriends;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;

public class FBFriend{
	private static final String TAG = FBFriend.class.getSimpleName();
	public FBFriend(){}
	
	private String id;
	
	@SerializedName("n")
	private String name;
	
	@SerializedName("p")
	private String playerId = "";
	
	@SerializedName("w")
	private int numWins = -1;
	
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
		return this.playerId.length() > 0 && this.numWins > -1;
	}
	
	public String getImageUrl(){
		return String.format(Constants.FACEBOOK_IMAGE_URL, this.id);
	}

	public String getBadgeDrawable(){
		return PlayerService.getBadgeDrawable(this.numWins);
	}
		
	public boolean nameStartsWith(String s){
		if (this.name.length() == 0) {return false;}
		else{
			String[] parts = this.name.split(" ");
			
			//Logger.d(TAG, "nameStartsWith s=" + s);
			
			for (int x = 0;x < parts.length; x++){
				//Logger.d(TAG, "nameStartsWith parts=" + parts[x].toLowerCase());
				if (parts[x].toLowerCase().startsWith(s.toLowerCase())) {
				//	Logger.d(TAG, "nameStartsWith parts=" + parts[x].toLowerCase() + " TRUE!!!!!");
					return true;
				}
			}			
			return false;
		}
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
 
	public String getFirstName(){
			String[] parts = this.name.split(" ");
			
			if (parts.length > 2){
				return parts[0] + " " + parts[1].substring(0,1);
			}
			else{
				return parts[0];
			}
			
	}
}
 