 package com.riotapps.word.ui;
 

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.riotapps.word.hooks.Game;
import com.riotapps.word.utils.Logger;

public class GameState {
	private static final String TAG = GameState.class.getSimpleName();
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
		//pull all of the letters into a list
		 List<String> list = new ArrayList<String>();
		 list.add(this.locations.get(0).getLetter());
		 if (this.locations.size() > 1) {list.add(this.locations.get(1).getLetter());}
		 if (this.locations.size() > 2) {list.add(this.locations.get(2).getLetter());}
		 if (this.locations.size() > 3) {list.add(this.locations.get(3).getLetter());}
		 if (this.locations.size() > 4) {list.add(this.locations.get(4).getLetter());}
		 if (this.locations.size() > 5) {list.add(this.locations.get(5).getLetter());}
		 if (this.locations.size() > 6) {list.add(this.locations.get(6).getLetter());}
		 
		 //then shuffle them
		 Collections.shuffle(list);
		 
		 //then reassign those letters back to the locations
		 int n = this.locations.size();
		 for (int i = 0; i < n; i++) {
			 this.locations.get(i).setLetter(list.get(i));
		 }
	}
	
	public void addTrayLocation(int index, String letter){
		if (this.locations.size() < 7){
			GameStateLocation location = new GameStateLocation();
			location.setLetter(letter);
			location.setTrayLocation(index);
			this.locations.add(location);
		}
		
	}
	
//	public void setLetterOnBoard(String letter, int trayId, int boardId){
//		for (GameStateLocation location : this.locations){
//			if (location.getLetter().equals(letter) && location.getTrayLocation() == trayId){
//				location.setBoardLocation(boardId);
//			}
//		}
//	}
	
//	public void moveLetterOnBoard(String letter, int sourceBoardId, int targetBoardId){
//		for (GameStateLocation location : this.locations){
//			if (location.getLetter().equals(letter) && location.getTrayLocation() == sourceBoardId){
//				location.setBoardLocation(targetBoardId);
//			}
//		}
//	}
	
	//this might have to be adjusted to get closest possible match to original location
	//but for now, finding the first open tray tile location will do
	public void returnLetterToTray(String letter, int boardId){
		//first find the id of the state location that holds the boardId 
		int locationId = -1;
		
		for(int i = 0; i < 7; i++){
			boolean found = false;
			if (this.locations.get(i).getBoardLocation() == boardId){
				locationId = i;
				break;
			}
		}
		
		//something is awry, will refactor later to address potential issues...somehow
		if (locationId == -1){ return; }
		
		//find the first empty trayId
		for(int i = 0; i < 7; i++){
			for (GameStateLocation location : this.locations){
				boolean found = false;
				if (location.getTrayLocation() == i){
					found = true;
				}
				//we could not find the trayid which means it is an open slot.  so let's take it
				if (found == false){
					Logger.d(TAG, "returnLetterToTray letter " + letter + " is being recalled to tray location " + i);
					this.locations.get(i).setBoardLocation(-1);
					this.locations.get(i).setTrayLocation(i);
					break;
				}
			}
		}
	
	}
	public void resetLettersFromOriginal(List<GameTile> boardTiles, List<TrayTile> trayTiles){
		this.resetLetters(boardTiles, trayTiles, false);
	}
	
	public void resetLettersFromCurrent(List<GameTile> boardTiles, List<TrayTile> trayTiles){
		this.resetLetters(boardTiles, trayTiles, true);
	}
	
	private void resetLetters(List<GameTile> boardTiles, List<TrayTile> trayTiles, boolean current){
		this.locations.clear();
		
		int count = 0;
		//add a location for each tray tile, regardless whether a letter in placed on that tile
		//this fills the location list
		for(TrayTile tile : trayTiles){
			GameStateLocation location = new GameStateLocation();

			if ((current ? tile.getCurrentLetter() : tile.getOriginalLetter()).length() > 0){
				location.setLetter(current ? tile.getCurrentLetter() : tile.getOriginalLetter());
				location.setTrayLocation(tile.getId());
				count += 1;
			}
			this.locations.add(location);
		}
		
		
		if (count < 7){
			//then loop through the board tiles finding the placed tiles
			//if a placed tile is found, add it to the first location that does not contain a tray tile
			for(GameTile tile : boardTiles){
				if (tile.getPlacedLetter().length() > 0){
					this.assignBoardIdToOpenLocation(tile.getPlacedLetter(), tile.getId());
					count += 1;
					if (count == 7){ break; }
				}
			}
		}
	}
	
	private void assignBoardIdToOpenLocation(String letter, int boardId){
		for (GameStateLocation location : this.locations){
			if (!location.isOnTray() && !location.isOnBoard()){
				location.setBoardLocation(boardId);
				location.setLetter(letter);
				break;
			}
		}
	}
 }
