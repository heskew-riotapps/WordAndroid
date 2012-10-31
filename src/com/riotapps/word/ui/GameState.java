package com.riotapps.word.ui;


import java.util.ArrayList;
import java.util.List;

public class GameState {
	private String gameId;
	private int turn = 0;
	private List<com.riotapps.word.hooks.TrayTile> trayTiles = new ArrayList<com.riotapps.word.hooks.TrayTile>();
	
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
	public List<com.riotapps.word.hooks.TrayTile> getTrayTiles() {
		return trayTiles;
	}
	public void setTrayTiles(List<com.riotapps.word.hooks.TrayTile> trayTiles) {
		this.trayTiles = trayTiles;
	}
	
	public int getNumTrayTiles(){
		int num = 0;
		for(com.riotapps.word.hooks.TrayTile tile : this.trayTiles){
			if (tile.getLetter().length() > 0){
				num += 1;
			}
		}
		
		return num;
	}
 
	
}
