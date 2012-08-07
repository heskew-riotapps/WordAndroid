package com.riotapps.word.ui;

import android.graphics.Bitmap;

public class GameTile {
	private int _xPosition = 0;
	private int _yPosition = 0;
	private Bitmap _currentBitmap;
	private Bitmap _originalBitmap;
	private int _row;
	private int _column;
    private boolean _isLastPlayed = false;
    private boolean _isOverlay = false;
    private boolean _isPlacement = false;
    private String _placedLetter = "";
}
