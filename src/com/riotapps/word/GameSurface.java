package com.riotapps.word;

import com.riotapps.word.ui.GameSurfaceView;
import android.app.Activity;
import android.os.Bundle;

import android.widget.ImageView;

//import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class GameSurface extends Activity {



	GameSurfaceView gameSurfaceView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamesurface);
		//setContentView(new GameSurfaceView2(this));
		
	 	this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
 
	 	
	 	ImageView ivPlayer = (ImageView) findViewById(R.id.ivPlayerScoreboard);
//	 	 ImageView iv;
 //        if (convertView == null)
 //            convertView = iv = new ImageView(UrlImageViewHelperSample.this);
 //        else
 //            iv = (ImageView)convertView;
         
         // yep, that's it. it handles the downloading and showing an interstitial image automagically.
         // UrlImageViewHelper.setUrlDrawable(ivPlayer, getItem(position), R.drawable.badge_0, null);
         
 //        return iv;
		//this._surfaceView = new GameSurfaceView(this); 
	}
	
		@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.gameSurfaceView.onDestroy();
		}
}
