package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;
 
public class TransportGameTurn {
	
	@SerializedName("a_t")
	private String token;
	
	@SerializedName("id")
	private String gameId;
	
	@SerializedName("played_words")
	private List<Word> words = new ArrayList<Word>();
	
	@SerializedName("played_tiles")
	private List<Tile> tiles = new ArrayList<Tile>();

	@SerializedName("t")
	private int turn;
	
	@SerializedName("p")
	private int points;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void addToWords(String word) {
		Word o = new Word();
		o.setWord(word);
		this.words.add(o);
	}

	public void addToTiles(String letter, int position) {
		Tile o = new Tile();
		o.setLetter(letter);
		o.setPosition(position);
		this.tiles.add(o);
	}
	
	protected class Word
	{
		@SerializedName("w")
		private String word;

		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}
			
	}
	
	protected class Tile{
		@SerializedName("p")
		private int position;
		
		@SerializedName("l")
		private String letter;

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public int getLetter() {
			return letter;
		}

		public void setLetter(String letter) {
			this.letter = letter;
		}

		
		
	}
	
}
