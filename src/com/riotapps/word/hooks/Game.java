package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

 
public class Game implements Parcelable {
 
	public Game(){}
	
	private String id = "";
	
	@SerializedName("a_t") //mainly just for transport via json
	private String authToken = "";
	
	@SerializedName("played_words")
	private List<PlayedWord> playedWords;
	
	@SerializedName("player_games")
	private List<PlayerGame> playerGames = new ArrayList<PlayerGame>();
	
//	@SerializedName("last_action_alert_text")
	//do not serialize
 	private String lastActionText = "";
	
	@SerializedName("left")
	private int numLettersLeft = 0;
	
	@SerializedName("d_c")
	private String dupCheck = "";

	@SerializedName("cr_d")
	private Date createDate;  
	
	@SerializedName("co_d")
	private Date completionDate;  

	@SerializedName("st")
	private int status = 0;  
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PlayedWord> getPlayedWords() {
		return playedWords;
	}

	public void setPlayedWords(List<PlayedWord> playedWords) {
		this.playedWords = playedWords;
	}

	public List<PlayerGame> getPlayerGames() {
		return playerGames;
	}

	public void setPlayerGames(List<PlayerGame> playerGames) {
		this.playerGames = playerGames;
	}
	
	public String getLastActionText() {
		return lastActionText;
	}

	public void setLastActionText(String lastActionText) {
		this.lastActionText = lastActionText;
	}

	public int getNumLettersLeft() {
		return numLettersLeft;
	}

	public void setNumLettersLeft(int numLettersLeft) {
		this.numLettersLeft = numLettersLeft;
	}

	public String getDupCheck() {
		return dupCheck;
	}

	public void setDupCheck(String dupCheck) {
		this.dupCheck = dupCheck;
	}
	
	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(this.id);
		out.writeString(this.authToken);
		out.writeList(this.playedWords);
		out.writeList(this.playerGames);
		out.writeInt(this.numLettersLeft);
		out.writeString(this.dupCheck);
		//out.writeSerializable(this.createDate);
		//out.writeSerializable(this.completionDate);
		out.writeLong(this.createDate == null ? 0 : this.createDate.getTime());
		out.writeLong(this.completionDate == null ? 0 : this.completionDate.getTime());
		out.writeInt(this.status);
	}

	public static final Parcelable.Creator<Game> CREATOR
		    = new Parcelable.Creator<Game>() {
		public Game createFromParcel(Parcel in) {
		    return new Game(in);
		}
		
		public Game[] newArray(int size) {
		    return new Game[size];
		}
	};
	
	 private Game(Parcel in) {
     
         this.id = in.readString();
         this.authToken = in.readString();
         in.readList(this.playedWords,PlayedWord.class.getClassLoader());
         in.readList(this.playerGames,PlayerGame.class.getClassLoader());
         this.numLettersLeft = in.readInt();
         this.dupCheck = in.readString();
       //  this.createDate = in.readSerializable(); 

         this.createDate = new Date(in.readLong());
         this.completionDate = new Date(in.readLong());
         this.status = in.readInt();
       	 
     }
	
}
