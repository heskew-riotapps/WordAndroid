package com.riotapps.word.hooks;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Game {
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
