package com.riotapps.word.ui;

public class DropTarget {
	
	public DropTarget(){}
	
	public DropTarget(int tileId, int distance){
		this.tileId = tileId;
		this.distance = distance;
	}
	
	
	private int tileId;
	private int distance;
	public int getTileId() {
		return tileId;
	}
	public void setTileId(int tileId) {
		this.tileId = tileId;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	

}
