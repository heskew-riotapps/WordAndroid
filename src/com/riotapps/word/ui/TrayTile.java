package com.riotapps.word.ui;

import android.graphics.Bitmap;
 

public class TrayTile {
	private int id;
	private int xPosition = 0;
	private int yPosition = 0;
	private Bitmap currentBitmap = null;
	private Bitmap originalBitmap;
	private Bitmap originalBitmapDragging;
	private int xPositionDragging = 0;
	private int yPositionDragging = 0;
	private String originalLetter = "";
	private String currentLetter = "";
	private String draggingLetter = "";
	private boolean dragging = false;
	
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
		return this.currentBitmap == null ? this.originalBitmap : this.currentBitmap;
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
	public Bitmap getOriginalBitmapDragging() {
		return originalBitmapDragging;
	}
	public void setOriginalBitmapDragging(Bitmap originalBitmapDragging) {
		this.originalBitmapDragging = originalBitmapDragging;
	}
	public int getxPositionDragging() {
		return xPositionDragging;
	}
	public void setxPositionDragging(int xPositionDragging) {
		this.xPositionDragging = xPositionDragging;
	}
	public int getyPositionDragging() {
		return yPositionDragging;
	}
	public void setyPositionDragging(int yPositionDragging) {
		this.yPositionDragging = yPositionDragging;
	}
	public String getOriginalLetter() {
		return originalLetter;
	}
	public void setOriginalLetter(String originalLetter) {
		this.originalLetter = originalLetter;
		this.currentLetter = originalLetter;
	}
	public String getVisibleLetter() {
		return currentLetter.length() > 0 ? currentLetter : originalLetter;
	}
	
	public String getCurrentLetter() {
		return currentLetter;
	}
	public void setCurrentLetter(String currentLetter) {
		this.currentLetter = currentLetter;
	}
	public String getDraggingLetter() {
		return draggingLetter;
	}
	public void setDraggingLetter(String draggingLetter) {
		this.draggingLetter = draggingLetter;
	}
	public boolean isDragging() {
		return dragging;
	}
	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	public void setUpForDrag(){
		this.dragging = true;
	}
	
	public void removeDrag(){
		this.dragging = false;
	}

}


