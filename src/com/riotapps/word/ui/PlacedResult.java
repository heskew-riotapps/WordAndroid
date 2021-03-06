package com.riotapps.word.ui;

import java.util.ArrayList;
import java.util.List;

public class PlacedResult {

	private List<PlacedWord> placedWords = new ArrayList<PlacedWord>();
	private List<GameTile> placedTiles = new ArrayList<GameTile>();
	private int totalPoints = 0;
	
	public List<GameTile> getPlacedTiles() {
		return placedTiles;
	}
	public void setPlacedTiles(List<GameTile> placedTiles) {
		this.placedTiles = placedTiles;
	}
	public int getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	public List<PlacedWord> getPlacedWords() {
		return placedWords;
	}
	public void setPlacedWords(List<PlacedWord> placedWords) {
		this.placedWords = placedWords;
	}
	
	
	
}