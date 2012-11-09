package com.riotapps.word.ui;

public class RectArea {
	
	private int top;
	private int left;
	private int bottom;
	private int right;
	
	public RectArea(){}
	
	public RectArea(int top, int left, int bottom, int right){
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}
	
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getBottom() {
		return bottom;
	}
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	
	public boolean isCoordinateWithinArea(int xPosition, int yPosition){
		if (yPosition >= this.top && 
			yPosition <= this.bottom &&
			xPosition >= this.left &&
			xPosition <= this.right){
			return true;
		}
		return false;
	}
	
	public boolean isRectPartiallyWithinArea(RectArea rect){
		if (rect.getTop() > this.getBottom()){return false;}  //fully below
		if (rect.getLeft() > this.getRight()){return false;} //fully to the right
		if (rect.getBottom() < this.getTop()){return false;} //fully above
		if (rect.getRight() < this.getLeft()){return false;} //fully to the left
		
	//	if ((rect.getTop() >= this.top && rect.getTop() <= this.bottom) ||
	//		(rect.getBottom() >= this.top && rect.getBottom() <= this.bottom) ||
	//		(rect.getLeft() >= this.left && rect.getLeft() <= this.right) ||
	//		 rect.getRight() >= this.left && rect.getRight() <= this.right){
	//		return true;
	//	}
		return true;
	}
	
	//based on the context rect and the passed in rect, return the overlapping rect
	//return null if no overlap
	public RectArea getAreaWithinBounds(RectArea rect){
		
		if (this.isRectPartiallyWithinArea(rect)){
			RectArea overlap = new RectArea();
			//always take largest top 
			overlap.setTop(this.getTop() > rect.getTop() ? this.getTop() : rect.getTop());
	
			//always take smallest bottom
			overlap.setBottom(this.getBottom() < rect.getBottom() ? this.getBottom() : rect.getBottom());
			
			//if rect is situated across the left side of this, take left side of this (outer) (since left side of rect is out of bounds)
			overlap.setLeft(rect.getLeft() <= this.getLeft()  ? this.getLeft() : rect.getLeft());

			//if rect is situated across the right side of this, take right side of this (outer) (since right side of rect is out of bounds)
			overlap.setRight(rect.getRight() >= this.getRight()  ? this.getRight() : rect.getRight());
			
			return overlap;
		}
		
		return null;
		
	}

}
