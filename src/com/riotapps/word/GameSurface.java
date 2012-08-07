package com.riotapps.word;

import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.ui.GameThread;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends Activity {

	GameSurfaceView _gameSurfaceView;
	SurfaceView _surfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamesurface);
		//setContentView(new GameSurfaceView2(this));
		
	 	this._gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
 
		//this._surfaceView = new GameSurfaceView(this); 
	}
	
}
