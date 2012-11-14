package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;

public class PlayedTile {

	//key :p,   Integer, :default => -1 #board_position
	//key :l, Array #letters
	//key :t, Array #turn
	
	@SerializedName("p")
	private int boardPosition; 

	@SerializedName("l")
	private int letter;

	public int getBoardPosition() {
		return boardPosition;
	}

	public void setBoardPosition(int boardPosition) {
		this.boardPosition = boardPosition;
	}

	public int getLetter() {
		return letter;
	}

	public void setLetter(int letter) {
		this.letter = letter;
	}

	 
	

}
