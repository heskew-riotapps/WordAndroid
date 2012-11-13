package com.riotapps.word.ui;

public class GameStateLocation {

	private String letter = "";
	private int boardLocation = -1;
	private int trayLocation = -1;
 
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public int getBoardLocation() {
		return boardLocation;
	}
	public void setBoardLocation(int boardLocation) {
		this.boardLocation = boardLocation;
	}
	public int getTrayLocation() {
		return trayLocation;
	}
	public void setTrayLocation(int trayLocation) {
		this.trayLocation = trayLocation;
	}
	
	public boolean isOnBoard(){
		return this.boardLocation > -1;
	}

	public boolean isOnTray(){
		return this.trayLocation > -1;
	}
 
	
}
