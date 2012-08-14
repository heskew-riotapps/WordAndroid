package com.riotapps.word.hooks;

import java.util.Date;
import com.google.gson.annotations.SerializedName;


public class PlayedWord {
  
	private String word;
	
	@SerializedName("turn_num")
	private int turnNum;

	@SerializedName("points_scored")
	private int pointsScored;
	
	@SerializedName("player_id")
	private String playerId;  

	@SerializedName("played_date")
	private Date playedDate;  
	
//	  key :player_id , ObjectId --how to handle
//	  key :turn_num,     Integer, :default => 0
//	  key :points_scored, Integer, :default => 0
//	  key :played_date, Time

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getTurnNum() {
		return turnNum;
	}

	public void setTurnNum(int turnNum) {
		this.turnNum = turnNum;
	}

	public int getPointsScored() {
		return pointsScored;
	}

	public void setPointsScored(int pointsScored) {
		this.pointsScored = pointsScored;
	}

	public Date getPlayedDate() {
		return playedDate;
	}

	public void setPlayedDate(Date playedDate) {
		this.playedDate = playedDate;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
}
