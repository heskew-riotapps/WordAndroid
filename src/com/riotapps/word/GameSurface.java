package com.riotapps.word;

import java.util.ArrayList;
import java.util.List;

import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.TrayTile;
import com.riotapps.word.ui.GameState;
import com.riotapps.word.ui.GameStateService;
import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.ui.GameTile;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.ImageFetcher;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class GameSurface extends Activity implements View.OnClickListener{
	private static final String TAG = GameSurface.class.getSimpleName();
	GameSurface context = this;
	GameSurfaceView gameSurfaceView;
	ImageFetcher imageLoader;
	private RelativeLayout scoreboard;
	 SurfaceView surfaceView;
	 Button bShuffle;
	//View bottom;
	
	public static final int MSG_SCOREBOARD_VISIBILITY = 1;
	public static final int MSG_POINTS_SCORED = 2;
	public static final int SCOREBOARD_HEIGHT = 30;
	public static final int BUTTON_CONTROL_HEIGHT = 48;
	private int windowHeight;
	private int scoreboardHeight;
	private Game game;
	private GameState gameState;
 
	private String contextUserId;
	private TextView tvNumPoints;
	private PlayerGame contextPlayerGame;
 

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getScoreboardHeight() {
		return scoreboardHeight;
	}

	public void setScoreboardHeight(int scoreboardHeight) {
		this.scoreboardHeight = scoreboardHeight;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

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
		
		 SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);
	     this.contextUserId = settings.getString(Constants.USER_PREFS_USER_ID, "");  
	     this.tvNumPoints = (TextView)findViewById(R.id.tvNumPoints);
	   
	     //check for turn number, if not a match, clearGameState
	     
	     
	     Log.w(TAG, "contextUserID=" + this.contextUserId);
		
		 Display display = getWindowManager().getDefaultDisplay(); 
	     this.windowHeight = display.getHeight();  // deprecated
	     
	     
	     Log.w(TAG, "long press " + android.view.ViewConfiguration.getLongPressTimeout() + 
	    		 " dbltap " + android.view.ViewConfiguration.getDoubleTapTimeout() + 
	    		  " tap  " + android.view.ViewConfiguration.getTapTimeout());
	     
	     
	  	this.scoreboard = (RelativeLayout)findViewById(R.id.scoreboard);
	  	this.scoreboardHeight = this.scoreboard.getHeight();
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
	 	this.game = getTempGame();

		this.gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
	 	this.gameSurfaceView.setParent(this);
	 	
	 	this.loadScoreboard();
	 	this.fillGameState();
	 	
	 	bShuffle = (Button) findViewById(R.id.bShuffle);
	 	bShuffle.setOnClickListener(this);
	 	
	 //	this.gameSurfaceView.setGame(game);
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
	            case GameSurface.MSG_POINTS_SCORED:
	            
	            	break;
	            }
	            super.handleMessage(msg);
	        }
	    };

	 private void fillGameState(){
		 this.gameState = GameStateService.getGameState(context, "123"); ///fix this temp code
		 
		 //if this.turn != gameState.turn, clearGameState
		 
		 ////come back and add this logic back
		 
		 if (this.gameState.getTrayTiles().size() == 0){
			 for(int x = 0; x < 7; x++){
				 TrayTile trayTile = new TrayTile();
				 trayTile.setIndex(x);
				 trayTile.setLetter(this.contextPlayerGame.getTrayLetters().size() > x ? this.contextPlayerGame.getTrayLetters().get(x) : "");
				 this.gameState.getTrayTiles().add(trayTile);
				 
			 }
			 //this.gameState.setTrayTiles(this.contextPlayerGame.getTrayLetters());
		 }
	 }
	 
	 private void loadScoreboard(){
		 //determine length of name and font size if too long (maybe)
		 //always use abbreviated name when 3 or more players
		 
		 //find context user in list.  context user will always be display in top left of scoreboard with
		 //other players in the assigned game order following
		 //for instance if contextUser is #3 in order, he will still be in top left corner and
		 // #4 will be under him (or #1 if there are only 3 players in the game) and
		 //#1 will be in top right and #2 will be in bottom right
		 //if there are only 3 players, the bottom right will always be empty
		 //if there are only 2 players the right column will be hidden 

		 int contextPlayerIndex = -1;
		 int contextPlayerTurnOrder = -1;
		 int playerIndex2 = -1;
		 int playerIndex3 = -1;
		 int playerIndex4 = -1;
		 
		 String activePlayer;
		 int playerGameCount = this.game.getPlayerGames().size();
		 
		 Log.w(TAG,"num players=" + playerGameCount);
		 
		 //loop through once to find context player
		 for(int x = 0; x < playerGameCount; x++){
		 //for (PlayerGame pg : this.game.getPlayerGames()) {
		    PlayerGame pg = this.game.getPlayerGames().get(x);	
		    Log.w(TAG,"player=" + pg.getPlayer().getId());
			 if (pg.getPlayer().getId() != null && pg.getPlayer().getId().equals(this.contextUserId)) {
		    		contextPlayerIndex = x;
		    		contextPlayerTurnOrder = pg.getPlayerOrder();
		    		this.contextPlayerGame = pg;
		     }
			 if (pg.isTurn()){
				 activePlayer = pg.getPlayer().getId();
			 }
		  }
		 
		 if (contextPlayerIndex == -1){ 
			 Log.w(TAG,"context player not found.  this is a problem that requires game to be removed from user's device.");
		 }
		 
		 if (playerGameCount > 2){
			 //loop through again to fill out the order of the other players for placement in the scoreboard 
			 //i'm sure there is a 2 line formula that will shrink this code but i'm tired and settling for switch statements
			 for(int x = 0; x < playerGameCount; x++){
				
			    PlayerGame pg = this.game.getPlayerGames().get(x);
			    if (x != contextPlayerIndex){
			    	//we already know where the context player (the player logged in right now) is
			    	//let's find the order to display the others
				    int orderDiff = contextPlayerTurnOrder - pg.getPlayerOrder();
				    //if (contextPlayerTurnOrder == 1 && pg.getPlayerOrder() == 2)
				    switch (contextPlayerTurnOrder){
					    case 1:
					    	//1 3  <-- 4 players
					    	//2 4
					    	//1 3  <-- 3 players
					    	//2  
					    	switch (pg.getPlayerOrder()){
						    	case 2:
						    		playerIndex2 = x;
						    		break;
						    	case 3:
						    		playerIndex3 = x;
						    		break;
						    	case 4:
						    		playerIndex4 = x;
						    		break;
					    	}
					    	break;
					    case 2:
					    	//2 4  <-- 4 players
					    	//3 1
					    	//2 1  <-- 3 players
					    	//3  
					    	switch (pg.getPlayerOrder()){
						    	case 3:
						    		playerIndex2 = x;
						    		break;
						    	case 4:
						    		playerIndex3 = x;
						    		break;
						    	case 1:
						    		if (playerGameCount == 3){
						    			playerIndex3 = x;
						    		}
						    		else {
						    			playerIndex4 = x;
						    		}
						    		break;
						    	}
					    	break;
					    case 3:
					    	//3 2  <-- 3 players
					    	//1
					    	//3 1  <-- 4 players
					    	//4 2
					    	switch (pg.getPlayerOrder()){
						    	case 1:
						    		if (playerGameCount == 3){
						    			playerIndex2 = x;
						    		}
						    		else {
						    			playerIndex3 = x;
						    		}
						    		break;
						    	case 4:
						    		playerIndex2 = x;
						    		break;
						    	case 2:
						    		if (playerGameCount == 3){
						    			playerIndex3 = x;
						    		}
						    		else {
						    			playerIndex4 = x;
						    		}
						    		break;
						    	}
					    	break;
					    case 4:
					    	//4 2  <-- 4 players (can never be 3 players by definition)
					    	//1 3
					    	switch (pg.getPlayerOrder()){
					    		case 1:
						    		playerIndex2 = x;
						    		break;
						    	case 2:
						    		playerIndex3 = x;
						    		break;
						    	case 3:
						    		playerIndex4 = x;
						    		break;
						    	}
					    	break;
				    }
			    }
			 }
		 }
		 else{
			 playerIndex2 = (contextPlayerIndex == 0 ? 1 : 0);
		 }
		 

		 
		 TextView tvContextPlayer = (TextView)findViewById(R.id.tvPlayer_1);
		 TextView tvContextPlayerScore = (TextView)findViewById(R.id.tvPlayerScore_1);
		 ImageView ivContextPlayerBadge = (ImageView)findViewById(R.id.ivPlayerBadge_1);
		 ImageView ivContextPlayerTurn = (ImageView)findViewById(R.id.ivPlayerTurn_1);
		 
		 TextView tvPlayer2 = (TextView)findViewById(R.id.tvPlayer_2);
		 TextView tvPlayerScore2 = (TextView)findViewById(R.id.tvPlayerScore_2);
		 ImageView ivPlayerBadge2 = (ImageView)findViewById(R.id.ivPlayerBadge_2);
		 ImageView ivPlayerTurn2 = (ImageView)findViewById(R.id.ivPlayerTurn_2);

		 TextView tvPlayer3 = (TextView)findViewById(R.id.tvPlayer_3);
		 TextView tvPlayerScore3 = (TextView)findViewById(R.id.tvPlayerScore_3);
		 ImageView ivPlayerBadge3 = (ImageView)findViewById(R.id.ivPlayerBadge_3);
		 ImageView ivPlayerTurn3 = (ImageView)findViewById(R.id.ivPlayerTurn_3);
		 
		 TextView tvPlayer4 = (TextView)findViewById(R.id.tvPlayer_4);
		 TextView tvPlayerScore4 = (TextView)findViewById(R.id.tvPlayerScore_4);
		 ImageView ivPlayerBadge4 = (ImageView)findViewById(R.id.ivPlayerBadge_4);
		 ImageView ivPlayerTurn4 = (ImageView)findViewById(R.id.ivPlayerTurn_4);
		 
		 //position 1
		 String contextPlayer = this.game.getPlayerGames().get(contextPlayerIndex).getPlayer().getFullName();
		 if (contextPlayer.length() > 25 || playerGameCount > 2){contextPlayer = this.game.getPlayerGames().get(contextPlayerIndex).getPlayer().getAbbreviatedName();}
		 tvContextPlayer.setText(contextPlayer);
		 tvContextPlayerScore.setText(Integer.toString(this.game.getPlayerGames().get(contextPlayerIndex).getScore()));
		 int contextPlayerBadgeId = getResources().getIdentifier("com.riotapps.word:drawable/" + this.game.getPlayerGames().get(contextPlayerIndex).getPlayer().getBadgeDrawable(), null, null);
		 ivContextPlayerBadge.setImageResource(contextPlayerBadgeId);
		 if (this.game.getPlayerGames().get(contextPlayerIndex).isTurn() == false){ivContextPlayerTurn.setVisibility(View.INVISIBLE);}

		 //position 2
		 String player2 = this.game.getPlayerGames().get(playerIndex2).getPlayer().getFullName();
		 if (player2.length() > 25 || playerGameCount > 2){player2 = this.game.getPlayerGames().get(playerIndex2).getPlayer().getAbbreviatedName();}
		 tvPlayer2.setText(player2);
		 tvPlayerScore2.setText(Integer.toString(this.game.getPlayerGames().get(playerIndex2).getScore()));
		 int playerBadgeId2 = getResources().getIdentifier("com.riotapps.word:drawable/" + this.game.getPlayerGames().get(playerIndex2).getPlayer().getBadgeDrawable(), null, null);
		 ivPlayerBadge2.setImageResource(playerBadgeId2);
		 if (this.game.getPlayerGames().get(playerIndex2).isTurn() == false){ivPlayerTurn2.setVisibility(View.INVISIBLE);}

		 //if neither context player and player 2 are currently taking their turn, 
		 //collapse the turn image column on the left
		 if (this.game.getPlayerGames().get(playerIndex2).isTurn() == false &&
				 this.game.getPlayerGames().get(contextPlayerIndex).isTurn() == false) {
			 ivContextPlayerTurn.setVisibility(View.GONE);
			 ivPlayerTurn2.setVisibility(View.GONE);
		 }
		 
		  
		 if (playerGameCount == 3){
			 //hide 4th player
			 tvPlayer4.setVisibility(View.INVISIBLE);
			 tvPlayerScore4.setVisibility(View.INVISIBLE);
			 ivPlayerBadge4.setVisibility(View.INVISIBLE);
			 ivPlayerTurn4.setVisibility(View.INVISIBLE);	
			 
			 //position 3
			 String player3 = this.game.getPlayerGames().get(playerIndex3).getPlayer().getFullName();
			 if (player3.length() > 25 || playerGameCount > 2){player3 = this.game.getPlayerGames().get(playerIndex3).getPlayer().getAbbreviatedName();}
			 tvPlayer3.setText(player3);
			 tvPlayerScore3.setText(Integer.toString(this.game.getPlayerGames().get(playerIndex3).getScore()));
			 int playerBadgeId3 = getResources().getIdentifier("com.riotapps.word:drawable/" + this.game.getPlayerGames().get(playerIndex3).getPlayer().getBadgeDrawable(), null, null);
			 ivPlayerBadge3.setImageResource(playerBadgeId3);
			 if (this.game.getPlayerGames().get(playerIndex3).isTurn() == false){ivPlayerTurn3.setVisibility(View.INVISIBLE);}

		 }
		 else if (playerGameCount == 2)
		 {
			 //hide second column if there are only 2 players
			 tvPlayer3.setVisibility(View.GONE);
			 tvPlayerScore3.setVisibility(View.GONE);
			 ivPlayerBadge3.setVisibility(View.GONE);
			 ivPlayerTurn3.setVisibility(View.GONE);
			 
			 tvPlayer4.setVisibility(View.GONE);
			 tvPlayerScore4.setVisibility(View.GONE);
			 ivPlayerBadge4.setVisibility(View.GONE);
			 ivPlayerTurn4.setVisibility(View.GONE);	 
		 }
		 else if (playerGameCount == 4){
			 
			 //position 3
			 String player3 = this.game.getPlayerGames().get(playerIndex3).getPlayer().getFullName();
			 if (player3.length() > 25 || playerGameCount > 2){player3 = this.game.getPlayerGames().get(playerIndex3).getPlayer().getAbbreviatedName();}
			 tvPlayer3.setText(player3);
			 tvPlayerScore3.setText(Integer.toString(this.game.getPlayerGames().get(playerIndex3).getScore()));
			 int playerBadgeId3 = getResources().getIdentifier("com.riotapps.word:drawable/" + this.game.getPlayerGames().get(playerIndex3).getPlayer().getBadgeDrawable(), null, null);
			 ivPlayerBadge3.setImageResource(playerBadgeId3);
			 if (this.game.getPlayerGames().get(playerIndex3).isTurn() == false){ivPlayerTurn3.setVisibility(View.INVISIBLE);}

			 //position 4
			 String player4 = this.game.getPlayerGames().get(playerIndex4).getPlayer().getFullName();
			 if (player4.length() > 25 || playerGameCount > 2){player4 = this.game.getPlayerGames().get(playerIndex4).getPlayer().getAbbreviatedName();}
			 tvPlayer4.setText(player4);
			 tvPlayerScore4.setText(Integer.toString(this.game.getPlayerGames().get(playerIndex4).getScore()));
			 int playerBadgeId4 = getResources().getIdentifier("com.riotapps.word:drawable/" + this.game.getPlayerGames().get(playerIndex4).getPlayer().getBadgeDrawable(), null, null);
			 ivPlayerBadge4.setImageResource(playerBadgeId4);
			 if (this.game.getPlayerGames().get(playerIndex4).isTurn() == false){ivPlayerTurn4.setVisibility(View.INVISIBLE);}
		 }
		 
		 TextView tvLettersLeft = (TextView)findViewById(R.id.tvLettersLeft);
		 if (this.game.getNumLettersLeft() == 1){
			 tvLettersLeft.setText(R.string.scoreboard_1_letter_left);
		 }
		 else{
			 tvLettersLeft.setText(String.format(context.getString(R.string.scoreboard_letters_left), this.game.getNumLettersLeft()));
		 }
		 this.tvNumPoints.setText(String.format(context.getString(R.string.scoreboard_num_points), "0"));
		 
	 }
	    
	 private Game getTempGame(){
		 Game game = new Game();
		 
		 PlayerGame pg1 = new PlayerGame();
		 	Player p1 = new Player();
		 	p1.setFirstName("Burgermeister");
		 	p1.setLastname("Meisterburger");
		 	p1.setNumWins(175); 
		 	pg1.setPlayer(p1);
		 	pg1.setScore(101);
		 	pg1.setPlayerOrder(2);
		 	pg1.setTurn(true);
		 	
		 	PlayerGame pg2 = new PlayerGame(); 
		 	Player p2 = new Player();
		 	p2.setFirstName("Flip");
		 	p2.setLastname("Wilson");
		 	p2.setId(this.contextUserId);
		 	p2.setNumWins(41); 
		 	pg2.setPlayer(p2);
		 	pg2.setScore(4);
		 	pg2.setPlayerOrder(1);
		 	pg2.setTurn(false);
		 	
		 	PlayerGame pg3 = new PlayerGame();
		 	Player p3 = new Player();
		 	p3.setFirstName("Junior18");
		 	p3.setLastname("");
		 	p3.setNumWins(243); 
		 	p3.setId(this.contextUserId);
		 	pg3.setPlayer(p3);
		 	pg3.setPlayerOrder(1);
		 	pg3.setTurn(true);
		 	pg3.setScore(265);
		  
		 	
		 	
		 	
		 //	this.game.
		 
		 	List<TrayTile> trayTiles = new ArrayList<TrayTile>();
		 	TrayTile trayTile1 = new TrayTile();
		 	trayTile1.setLetter("A");
		 	trayTile1.setIndex(1);
		 	TrayTile trayTile2 = new TrayTile();
		 	trayTile2.setLetter("B");
		 	trayTile2.setIndex(2);
		 	TrayTile trayTile3 = new TrayTile();
		 	trayTile3.setLetter("Q");
		 	TrayTile trayTile4 = new TrayTile();
		 	trayTile4.setLetter("O");
		 	trayTile4.setIndex(3);
		 	TrayTile trayTile5 = new TrayTile();
		 	trayTile5.setLetter("H");
		 	trayTile5.setIndex(4);
		 	TrayTile trayTile6 = new TrayTile();
		 	trayTile6.setLetter("D");
		 	trayTile6.setIndex(5);
		 	TrayTile trayTile7 = new TrayTile();
		 	trayTile7.setLetter("Z");
		 	trayTile7.setIndex(6);
		 	trayTiles.add(trayTile1);
		 	trayTiles.add(trayTile2);
		 	trayTiles.add(trayTile3);
		 	trayTiles.add(trayTile4);
		 	trayTiles.add(trayTile5);
		 	trayTiles.add(trayTile6);
		 	trayTiles.add(trayTile7);
	//	 	pg3.setTrayTiles(trayTiles);
	//	 	pg2.setTrayTiles(trayTiles);
		 	
		 	PlayerGame pg4 = new PlayerGame();
		 	Player p4 = new Player();
		 	p4.setFirstName("Star");
		 	p4.setLastname("Lizardface");
		 	p4.setNumWins(11); 
		 	pg4.setPlayer(p4);
		 	pg4.setPlayerOrder(4);
		 	pg4.setTurn(true);
		 	pg4.setScore(51);
		 	
		 	List<PlayerGame>players = new ArrayList<PlayerGame>();
		 	players.add(pg1);
		 	players.add(pg2);
		  //	players.add(pg3);
		 //	players.add(pg4);
		 	
		 	game.setLastActionText("Junior18 played HAMMER for 21" );
		 	game.setNumLettersLeft(87);
		 	
		
		 	game.setPlayerGames(players);

		 	
		 return game;
	 }
	    
	 public class UpdateThread implements Runnable {
	    	 
	        @Override
	        public void run() {
	             while(true){
	            	 GameSurface.this.updateHandler.sendEmptyMessage(0);
	            }
	        }
	 
	    }

	    
//	public RelativeLayout getScoreboard() {
//		return scoreboard;
//	}

//	public void setScoreboard(RelativeLayout scoreboard) {
//		this.scoreboard = scoreboard;
//	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.w(TAG, "onDestroy called");
		
		this.gameSurfaceView.onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.w(TAG, "onStop called");
		this.gameSurfaceView.onStop();
		super.onStop();
		
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.w(TAG, "onPause called");
		super.onPause();
		this.gameSurfaceView.onPause();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.w(TAG, "onResume called");
		super.onResume();
		this.gameSurfaceView.onResume();
	

	}

//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		// TODO Auto-generated method stub
//		super.onWindowFocusChanged(hasFocus);
//		this.gameSurfaceView.onWindowFocusChanged();
//	}

//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		super.onBackPressed();
//		this.gameSurfaceView.onBackPressed();
//	}

	
	
	@Override
	protected void onRestart() {
		Log.w(TAG, "onRestart called");
		super.onRestart();
		this.gameSurfaceView.onRestart();
		

	}

	 @Override 
	    public void onClick(View v) {
	    	Intent goToActivity;
	    	
	    	switch(v.getId()){  
	        case R.id.bShuffle:  
	        	this.gameSurfaceView.shuffleTray();
				break;

	    	}
}
		
		
}
