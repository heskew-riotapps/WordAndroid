package com.riotapps.word;

import com.riotapps.word.ui.GameSurfaceView;

import android.app.Activity;
import android.os.Bundle;

public class GameSurface extends Activity {

	GameSurfaceView gameSurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamesurface);
		
		gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
		
	}
	
	 @Override
	 protected void onResume() {
	  // TODO Auto-generated method stub
	  super.onResume();
	  gameSurfaceView.onResume();
	 }
	 
	 @Override
	 protected void onPause() {
	  // TODO Auto-generated method stub
	  super.onPause();
	  gameSurfaceView.onPause();
	 }
	 
	}

}
