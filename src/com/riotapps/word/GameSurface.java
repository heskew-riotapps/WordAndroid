package com.riotapps.word;

import com.riotapps.word.hooks.Game;
import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.ImageResizer;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageView;

//import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class GameSurface extends Activity {
	
	GameSurfaceView gameSurfaceView;
	ImageFetcher imageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamesurface);
		//setContentView(new GameSurfaceView2(this));
		
		//gravatar size = max size...default images
		//https://graph.facebook.com/hunter.eskew/picture?return_ssl_resources=1
	//	String gravatar = "http://graph.facebook.com/donna.guyton/picture?r=1&type=square"; //"http://www.gravatar.com/avatar/" + Utils.md5("hunter.eskew@gmail.com");
		
	//	imageLoader = new ImageFetcher(this, 100, 100);
	//	imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
		
	 	this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
 
	 	
	 //	ImageView ivPlayer = (ImageView) findViewById(R.id.ivPlayerScoreboard);
	// 	imageLoader.loadImage(gravatar, ivPlayer); //default image
	 	
	 	Bundle extras = getIntent().getExtras(); 
	 	if(extras !=null)
	 	{
	 		String value = extras.getString("gameId");
	 	}
	 	
	 	Intent i = getIntent();
	 	Game game = (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
	 	//retrieve game from server
 
	 	
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
