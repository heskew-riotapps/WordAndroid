package com.riotapps.word.hooks;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

 
public class Game implements Parcelable {
 
	public Game(){}
	
	private String id;
	@SerializedName("played_words")
	private List<PlayedWord> playedWords;
	
	@SerializedName("player_games")
	private List<PlayerGame> playerGames;
	
	@SerializedName("last_action_alert_text")
	private String lastActionText;
	

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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(this.id);
		out.writeList(this.playedWords);
		out.writeList(this.playerGames);
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
        // same order as writeToParcel
         this.id = in.readString();
     //    this.playedWords = in.readList(outVal, loader);
     //    this.playerGames = in.readList(outVal, loader);
     	 
     }
//	 many :player_games #, :length => { :maximum => 2 }  
//	  many :played_words
//	#many :letters remaining vs played?#, :length => { :maximum => 2 }  
//	  many :played_tiles
//	  many :chatters
//	  key :remaining_letters, String
//	  key :played_letters, String
//	  key :random_vowels, String #, format => /[AEIOU]/
//	  key :random_consonants, String #, format => /[BCDFGHJKLMNPQRSTVWXYZ]/
//	  key :num_consecutive_skips, Integer, :default => 0
//	  key :num_words_played, Integer, :default => 0
//	  key :completion_date, Time
//	  key :last_action_popup_title, String
//	  key :last_action_popup_text, String
//	  key :last_action_alert_text, String
//	  key :status, Integer
	
//	private 
	
	
}
