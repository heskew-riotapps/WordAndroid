package com.riotapps.word.hooks;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

 
public class Game implements Parcelable {
	private int parcelableData;
	
	private String id;
	@SerializedName("played_words")
	private List<PlayedWord> PlayedWords;
	
	@SerializedName("player_games")
	private List<PlayerGame> PlayerGames;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PlayedWord> getPlayedWords() {
		return PlayedWords;
	}

	public void setPlayedWords(List<PlayedWord> playedWords) {
		PlayedWords = playedWords;
	}

	public List<PlayerGame> getPlayerGames() {
		return PlayerGames;
	}

	public void setPlayerGames(List<PlayerGame> playerGames) {
		PlayerGames = playerGames;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(parcelableData);
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
         parcelableData = in.readInt();
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
