package com.riotapps.word;

import java.util.ArrayList;
import java.util.List;

import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.ImageResizer;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

//import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class GameSurface extends Activity {
	
	GameSurface context = this;
	GameSurfaceView gameSurfaceView;
	ImageFetcher imageLoader;
	RelativeLayout scoreboard;
	 SurfaceView surfaceView;
	//View bottom;
	
	public static final int MSG_SCOREBOARD_VISIBILITY = 1;
	public static final int MSG_POINTS_SCORED = 2;

	
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
	// 	this.surfaceView = (SurfaceView)findViewById(R.id.gameSurface);
		
	 	//this.surfaceView = new GameSurfaceView(this);
	 	
	 //	this.scoreboard = (RelativeLayout)findViewById(R.id.scoreboard);
	 //	this.scoreboard.setVisibility(android.view.View.GONE);
	 	//this.bottom = (View)findViewById(R.id.bottomControlsPlaceholder);
	 	//this.bottom.//se
 
	 	
	 //	ImageView ivPlayer = (ImageView) findViewById(R.id.ivPlayerScoreboard);
	// 	imageLoader.loadImage(gravatar, ivPlayer); //default image
	 	
	 	Bundle extras = getIntent().getExtras(); 
	 	if(extras !=null)
	 	{
	 		String value = extras.getString("gameId");
	 	}
	 	
	 	Intent i = getIntent();
	 	//Game game = (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
	 	
	 	//temp
	 	Game game = new Game();
	 	PlayerGame pg1 = new PlayerGame();
	 	Player p1 = new Player();
	 	p1.setFirstName("Burgermeister");
	 	p1.setLastname("Meisterburger");
	 	pg1.setPlayer(p1);
	 	pg1.setScore(101);
	 	pg1.setPlayerOrder(2);
	 	
	 	PlayerGame pg2 = new PlayerGame();
	 	Player p2 = new Player();
	 	p2.setFirstName("Jimmy");
	 	p2.setLastname("Dean");
	 	pg2.setPlayer(p2);
	 	pg2.setScore(4);
	 	pg2.setPlayerOrder(3);
	 	
	 	PlayerGame pg3 = new PlayerGame();
	 	Player p3 = new Player();
	 	p3.setFirstName("Junior18");
	 	p3.setLastname("");
	 	pg3.setPlayer(p3);
	 	pg3.setPlayerOrder(1);
	 	
	 	PlayerGame pg4 = new PlayerGame();
	 	Player p4 = new Player();
	 	p4.setFirstName("Star");
	 	p4.setLastname("Lizardface");
	 	pg4.setPlayer(p4);
	 	pg4.setPlayerOrder(4);
	 	
	 	List<PlayerGame>players = new ArrayList<PlayerGame>();
	 	
	 	game.setPlayerGames(players);
	 	
	 	this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
	 	this.gameSurfaceView.setParent(this);
	 	this.gameSurfaceView.setGame(game);
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
	
	 public Handler updateHandler = new Handler(){
	        /** Gets called on every message that is received */
	        // @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what){
	            case GameSurface.MSG_SCOREBOARD_VISIBILITY:
	            	context.setScoreboardVisibility(msg.arg1);
	            	break;
	            case GameSurface.MSG_POINTS_SCORED:
	            
	            	break;
	            }
	            super.handleMessage(msg);
	        }
	    };

	 public void setScoreboardVisibility(int visibility) {
		 this.scoreboard.setVisibility(visibility);
		 
	 }
	    
	 public class UpdateThread implements Runnable {
	    	 
	        @Override
	        public void run() {
	             while(true){
	            	 GameSurface.this.updateHandler.sendEmptyMessage(0);
	            }
	        }
	 
	    }

	    
	public RelativeLayout getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(RelativeLayout scoreboard) {
		this.scoreboard = scoreboard;
	}

		@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.gameSurfaceView.onDestroy();
		}
}
