package com.riotapps.word.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameTile {
	private int id;
	
	private int xPosition = 0;
	private int yPosition = 0;
	private Bitmap currentBitmap;
	private Bitmap originalBitmap;
	private int row;
	private int column;
    private boolean isLastPlayed = false;
    private boolean isOverlay = false;
    private boolean isPlacement = false;
    private String placedLetter = "";
    private Canvas canvas;


	public GameTile(Canvas canvas){
		this.canvas = canvas;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getxPosition() {
		return xPosition;
	}


	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}


	public int getyPosition() {
		return yPosition;
	}


	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}


	public Bitmap getCurrentBitmap() {
		return currentBitmap;
	}


	public void setCurrentBitmap(Bitmap currentBitmap) {
		this.currentBitmap = currentBitmap;
	}


	public Bitmap getOriginalBitmap() {
		return originalBitmap;
	}


	public void setOriginalBitmap(Bitmap originalBitmap) {
		this.originalBitmap = originalBitmap;
	}


	public int getRow() {
		return row;
	}


	public void setRow(int row) {
		this.row = row;
	}


	public int getColumn() {
		return column;
	}


	public void setColumn(int column) {
		this.column = column;
	}


	public boolean isLastPlayed() {
		return isLastPlayed;
	}


	public void setLastPlayed(boolean isLastPlayed) {
		this.isLastPlayed = isLastPlayed;
	}


	public boolean isOverlay() {
		return isOverlay;
	}


	public void setOverlay(boolean isOverlay) {
		this.isOverlay = isOverlay;
	}


	public boolean isPlacement() {
		return isPlacement;
	}


	public void setPlacement(boolean isPlacement) {
		this.isPlacement = isPlacement;
	}


	public String getPlacedLetter() {
		return placedLetter;
	}


	public void setPlacedLetter(String placedLetter) {
		this.placedLetter = placedLetter;
	}


}


