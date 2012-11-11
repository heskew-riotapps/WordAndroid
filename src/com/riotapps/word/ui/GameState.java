 package com.riotapps.word.ui;
 

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameState {
	
	public GameState(){
		this.locations = new ArrayList<GameStateLocation>();
	}
	
	private String gameId;
	private int turn = 0;
	private int trayVersion = 1;
	private List<GameStateLocation> locations = new ArrayList<GameStateLocation>();
	
	//placetiles to remember board state
	
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}

	public List<GameStateLocation> getLocations() {
		return locations;
	}
	public void setLocations(List<GameStateLocation> locations) {
		this.locations = locations;
	}
	
	
	public int getTrayVersion() {
		return trayVersion;
	}
	public void setTrayVersion(int trayVersion) {
		this.trayVersion = trayVersion;
	}
	public int getNumTrayTiles(){
		int num = 0;
		for(GameStateLocation location : this.locations){
			if (location.getLetter().length() > 0){
				num += 1;
			}
		}
		
		return num;
	}
 
	public String getBoardLetter(int index){
		for(GameStateLocation location : this.locations){
			if (index == location.getBoardLocation()){
				return location.getLetter();
			}
		}
		return "";
	}

	public String getTrayLetter(int index){
		for(GameStateLocation location : this.locations){
			if (index == location.getTrayLocation()){
				return location.getLetter();
			}
		}
		return "";
	}	
	public void shuffleLetters(){
		 List<String> list = new ArrayList<String>();
		 list.add(this.locations.get(0).getLetter());
		 if (this.locations.size() > 1) {list.add(this.locations.get(1).getLetter());}
		 if (this.locations.size() > 2) {list.add(this.locations.get(2).getLetter());}
		 if (this.locations.size() > 3) {list.add(this.locations.get(3).getLetter());}
		 if (this.locations.size() > 4) {list.add(this.locations.get(4).getLetter());}
		 if (this.locations.size() > 5) {list.add(this.locations.get(5).getLetter());}
		 if (this.locations.size() > 6) {list.add(this.locations.get(6).getLetter());}
		 Collections.shuffle(list);
		 
		 int n = this.locations.size();
		 for (int i = 0; i < n; i++) {
			 this.locations.get(n).setLetter(list.get(n));
		 }
	}
 }
