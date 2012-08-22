package com.riotapps.word;

import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.ImageResizer;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.Utils;

import android.app.Activity;
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
		
		String gravatar = "http://www.gravatar.com/avatar/" + Utils.md5("hunter.eskew@gmail.com");
		
		imageLoader = new ImageFetcher(this, 40, 40);
		imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
		
	 	this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
 
	 	
	 	ImageView ivPlayer = (ImageView) findViewById(R.id.ivPlayerScoreboard);
	 	imageLoader.loadImage(gravatar, ivPlayer); //default image
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
