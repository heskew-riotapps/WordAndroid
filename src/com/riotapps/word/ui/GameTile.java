package com.riotapps.word.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameTile {
	private int id;
	
	private int xPosition = 0;
	private int yPosition = 0;
	private Bitmap currentBitmap;
	private Bitmap originalBitmap;
	private Bitmap originalBitmapZoomed;
	private int xPositionZoomed = 0;
	private int yPositionZoomed = 0;
	private String originalText = "";
	private String originalLetter = "";
	private String draggingLetter = "";

	//private String placedText = "";

	private int row;
	private int column;
    private boolean isLastPlayed = false;
    private boolean isOverlay = false;
    private boolean isPlacement = false;
    private String placedLetter = "";
    private boolean isDraggable = false;
    private boolean isDragging = false;
    private Canvas canvas;


	public GameTile(){
		
	}
	public GameTile(Canvas canvas){
		this.canvas = canvas; //???is canvas needed?
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

	public Bitmap getOriginalBitmapZoomed() {
		return originalBitmapZoomed;
	}
	public void setOriginalBitmapZoomed(Bitmap originalBitmapZoomed) {
		this.originalBitmapZoomed = originalBitmapZoomed;
	}
	public int getxPositionZoomed() {
		return xPositionZoomed;
	}
	public void setxPositionZoomed(int xPositionZoomed) {
		this.xPositionZoomed = xPositionZoomed;
	}
	public int getyPositionZoomed() {
		return yPositionZoomed;
	}
	public void setyPositionZoomed(int yPositionZoomed) {
		this.yPositionZoomed = yPositionZoomed;
	}
	
	public String getDisplayLetter(){
		return (this.placedLetter.length() > 0 ? this.placedLetter : this.originalLetter);
	}

 
public String getOriginalText() {
		return originalText;
	}
	public void setOriginalText(String originalText) {
		this.originalText = originalText;
	}
	public String getOriginalLetter() {
		return originalLetter;
	}
	//	public String getPlacedText() {
//		return placedText;
//	}
//	public void setPlacedText(String placeText) {
//		this.placedText = placedText;
//	}
	public void setPlacedLetter(String placedLetter) {
		this.placedLetter = placedLetter;
		if (placedLetter.length() > 0) {
			this.isDraggable = true;
		}
		else{
			this.isDraggable = false;
		}
	}
	
	public String getPlacedLetter() {
		return placedLetter;
	}
	public void setOriginalLetter(String originalLetter) {
		this.originalLetter = originalLetter;
	}
	public String getCurrentLetter() {
		return placedLetter.length() > 0 ? placedLetter : originalLetter;
	}
	public boolean isDraggable() {
		return isDraggable;
	}
	public void setDraggable(boolean isDraggable) {
		this.isDraggable = isDraggable;
	}
	public boolean isDragging() {
		return isDragging;
	}
	public void setDragging(boolean isDragging) {
		this.isDragging = isDragging;
	}
	public String getDraggingLetter() {
		return draggingLetter;
	}
	public void setDraggingLetter(String draggingLetter) {
		this.draggingLetter = draggingLetter;
	}
	
	public void recallLetter(){
		this.placedLetter = "";
		this.draggingLetter = "";
		this.isDragging = false;
		this.isDraggable = false;
	}
	
}


