package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;

public class PlayedTile {

	//key :p,   Integer, :default => -1 #board_position
	//key :l, Array #letters
	//key :t, Array #turn
	
	@SerializedName("p")
	private int boardPosition; 

	@SerializedName("l_")
	private String letter;
	
	@SerializedName("t_")
	private int turn;

	public int getBoardPosition() {
		return boardPosition;
	}

	public void setBoardPosition(int boardPosition) {
		this.boardPosition = boardPosition;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

}
