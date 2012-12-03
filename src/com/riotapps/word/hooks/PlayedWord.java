package com.riotapps.word.hooks;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class PlayedWord implements Parcelable{
  
	@SerializedName("player_id")
	private String playerId;  

	@SerializedName("w")
	private String word = "";
	
	@SerializedName("t")
	private int turn = 0;

	@SerializedName("p_s")
	private int pointsScored = 0;
	
	@SerializedName("p_d")
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

	public int getTurn(){
		return turn;
	}

	public void setTurn(int turnNum) {
		this.turn = turnNum;
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {

		out.writeString(this.playerId);
		out.writeString(this.word); 
		out.writeInt(this.turn);
		out.writeInt(this.pointsScored);
		out.writeLong(this.playedDate == null ? 0 : this.playedDate.getTime());

	}
	public static final Parcelable.Creator<PlayedWord> CREATOR
	= new Parcelable.Creator<PlayedWord>() {
	public PlayedWord createFromParcel(Parcel in) {
		return new PlayedWord(in);
	}

	public PlayedWord[] newArray(int size) {
		return new PlayedWord[size];
	}
};

	private PlayedWord(Parcel in) {
		
		this.playerId = in.readString();
		this.word = in.readString();
		this.turn = in.readInt();
		this.pointsScored = in.readInt();
		this.playedDate = new Date();
		this.playedDate.setTime(in.readLong());
	}
}
