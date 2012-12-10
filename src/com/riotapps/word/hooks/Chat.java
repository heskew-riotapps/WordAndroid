package com.riotapps.word.hooks;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Chat {
//	  belongs_to :game
//	  key :t,     String  #text
//	  key :player_id , ObjectId
//	  key :ch_d, Time #chat date
	
	@SerializedName("t")
	private String text;
	
	@SerializedName("player_id")
	private String playerId;  
	
	@SerializedName("ch_d")
	private Date chatDate;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public Date getChatDate() {
		return chatDate;
	}

	public void setChatDate(Date chatDate) {
		this.chatDate = chatDate;
	}  

	
	
	
}
