package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;
 
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.NetworkTaskResult;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Utils;
import com.riotapps.word.utils.Enums.RequestType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainLanding extends FragmentActivity implements View.OnClickListener{
	private static final String TAG = MainLanding.class.getSimpleName();
	TextView tvStartByNickname;
	Button bStart;
	Button bOptions;
	Button bBadges;
	ImageView ivContextPlayer;
	ImageView ivContextPlayerBadge;
	Context context = this;
	Player player;
	ImageFetcher imageLoader;
	NetworkTask runningTask = null;
	Timer timer = null;
	boolean callingIntent = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlanding);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);

        //PlayerService playerSvc = new PlayerService();
        //Player player = PlayerService.getPlayerFromLocal();
        
       // Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	   // t.show();
        
        Logger.d(TAG, "onCreate called");
        
    	//Logger.d(TAG, "onCreate started");
        this.imageLoader = new ImageFetcher(this, Constants.DEFAULT_AVATAR_SIZE, Constants.DEFAULT_AVATAR_SIZE, 0);
        this.imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
        
	    bStart = (Button) findViewById(R.id.bStart);
	    bOptions = (Button) findViewById(R.id.bOptions);
	    bBadges = (Button) findViewById(R.id.bBadges);
	    
	    bStart.setOnClickListener(this);
		bOptions.setOnClickListener(this);
		bBadges.setOnClickListener(this);
		
		this.player = PlayerService.getPlayerFromLocal();
		PlayerService.loadPlayerInHeader(this);
		
		SharedPreferences settings = this.getSharedPreferences(Constants.USER_PREFS, 0);
	    String completedDate = settings.getString(Constants.USER_PREFS_LATEST_COMPLETED_GAME_DATE, Constants.DEFAULT_COMPLETED_GAMES_DATE);

	    
	 	Bundle extras = getIntent().getExtras(); 
	 	Boolean isGameListPrefetched = false;
	 	if(extras !=null)
	 	{
	 		isGameListPrefetched = extras.getBoolean(Constants.EXTRA_GAME_LIST_PREFETCHED, false);
	 	}
		
	 	this.loadLists();
	 	
	 	long lastPlayerCheckTime = GameService.getLastGameListCheckTime(this);
		if (!isGameListPrefetched && Utils.convertNanosecondsToMilliseconds(System.nanoTime()) - lastPlayerCheckTime > Constants.LOCAL_GAME_LIST_STORAGE_DURATION_IN_MILLISECONDS){
			//fetch games
 
			try { 
				String json = PlayerService.setupAuthTokenCheck(this, this.player.getAuthToken());
				//this will bring back the players games too
				new NetworkTask(this, RequestType.POST, "", json, false).execute(Constants.REST_GET_PLAYER_BY_TOKEN);
			} catch (DesignByContractException e) {
				//this should never happen unless there is some tampering
				 DialogManager.SetupAlert(context, getString(R.string.oops), e.getLocalizedMessage(), true, 0);
			}

		}	
		else if (isGameListPrefetched){
			GameService.updateLastGameListCheckTime(this);
		}
		//no games yet, send player to StartGame to get started
	//	else if (this.player.getTotalNumLocalGames() == 0){
     //   	Intent intent = new Intent(getApplicationContext(), StartGame.class);
	//		startActivity(intent);	
	//	}
		//else {
		//	this.loadLists();
		//}
		this.setupTimer();
    }
    
    @Override
	protected void onRestart() {
		//Log.w(TAG, "onRestart called"); 
		super.onRestart();
		
		if (this.player == null){
			this.player = PlayerService.getPlayerFromLocal();
		}
		this.callingIntent = false; 
		this.setupTimer(Constants.GAME_SURFACE_CHECK_START_AFTER_RESTART_IN_MILLISECONDS);
		//if (!this.getIntent().getBooleanExtra(name, false)){
		//	//((Activity) context).runOnUiThread(new handleGameListCheck());
	//		this.loadLists();
		//} 
	}
    
    private void setupTimer(){
		this.setupTimer(Constants.GAME_LIST_CHECK_START_IN_MILLISECONDS);
	}
	
    private void setupTimer(long delay){
		Logger.d(TAG, "setupTimer");
		if (this.timer == null){
			this.timer = new Timer();  
			updateListTask updateList = new updateListTask();
			this.timer.scheduleAtFixedRate(updateList, delay, Constants.GAME_LIST_CHECK_INTERVAL_IN_MILLISECONDS);
		}
    }
    
    private void stopTimer(){
    	Logger.d(TAG, "stopTimer called");
    	if (this.timer != null) {
    		this.timer.cancel();
    		this.timer = null;
    	}
    
    }
    
    private class updateListTask extends TimerTask {
 
    	   public void run() {
    		   ((Activity) context).runOnUiThread(new handleGameListCheck());
 
    	   }
    }
    
    private class handleGameListCheck implements Runnable {
		    public void run() {
				 if (!callingIntent){   
					 try { 
			    		 Logger.d(TAG, "handleGameListCheck");
		   				String json = PlayerService.setupGameListCheck(context, player.getAuthToken(), player.getLastRefreshDate());
		   				//this will bring back the players games too
		   				new NetworkTask((MainLanding) context, RequestType.POST, context.getString(R.string.progress_syncing), json, false).execute(Constants.REST_GAME_LIST_CHECK);
			   			} catch (DesignByContractException e) {
			   				//this should never happen unless there is some tampering
			   				 DialogManager.SetupAlert(context, getString(R.string.oops), e.getLocalizedMessage(), true, 0);
			   			}
		    	 }
		    }
	  }
    
    @Override
	protected void onResume() {
		super.onResume();
		
		Logger.d(TAG, "onResume is timer null=" + (timer == null));
	 
		this.setupTimer();
	 
	}

 
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.stopTimer();
		this.player = null;

	}

	@Override
	public void onBackPressed() {
		// do nothing if back is pressed
		//super.onBackPressed();
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
	}

	private void loadLists(){
 
    	 Logger.d(TAG, "loadLists started");
		LinearLayout llCompletedGames = (LinearLayout)findViewById(R.id.llCompletedGames);
    	LinearLayout llYourTurn = (LinearLayout)findViewById(R.id.llYourTurn);
    	LinearLayout llOpponentsTurn = (LinearLayout)findViewById(R.id.llOpponentsTurn);
    	LinearLayout llCompletedGamesWrapper = (LinearLayout)findViewById(R.id.llCompletedGamesWrapper);
      	LinearLayout llYourTurnWrapper = (LinearLayout)findViewById(R.id.llYourTurnWrapper);
    	LinearLayout llOpponentsTurnWrapper = (LinearLayout)findViewById(R.id.llOpponentsTurnWrapper);
    	TextView tvWaiting = (TextView)findViewById(R.id.tvWaiting);
    	
    	//clear out view
    	llYourTurn.removeAllViews();
    	llOpponentsTurn.removeAllViews();
    	llCompletedGames.removeAllViews();

    	
  //	Logger.w(TAG, "loadLists this.player.getActiveGamesYourTurn() size=" + this.player.getActiveGamesYourTurn().size() );

    	
    	int i = 1;
    	if (this.player.getActiveGamesYourTurn().size() > 0){
    		tvWaiting.setVisibility(View.GONE);
    		
	        for (Game g : this.player.getActiveGamesYourTurn()){
	        	Logger.w(TAG, "loadLists this.player.getActiveGamesYourTurn() game=" +g.getId() );

	        	
	        	 llYourTurn.addView(getGameYourTurnView(g, i == 1, this.player.getActiveGamesYourTurn().size() == i));
	        	 i += 1;
			}
	        llYourTurnWrapper.setVisibility(View.VISIBLE);
	        llYourTurn.setVisibility(View.VISIBLE);
    	}
    	else {
    		if (this.player.getActiveGamesOpponentTurn().size() > 0){
    			llYourTurn.setVisibility(View.GONE);
        		tvWaiting.setVisibility(View.VISIBLE);

    		}
    		else{
        		llYourTurnWrapper.setVisibility(View.GONE);
        		tvWaiting.setVisibility(View.GONE);
    		}
    	}

     //   Logger.w(TAG, "loadLists this.player.getActiveGamesOpponentTurn() size=" + this.player.getActiveGamesOpponentTurn().size() );
    	i = 1;
    	if (this.player.getActiveGamesOpponentTurn().size() > 0){
    	
    		
	        for (Game g : this.player.getActiveGamesOpponentTurn()){
	        	Logger.w(TAG, "loadLists this.player.getActiveGamesOpponentTurn() game=" + g.getId() );
	        	
	        	llOpponentsTurn.addView(getGameYourTurnView(g, i == 1, this.player.getActiveGamesOpponentTurn().size() == i));
	        	 i += 1;
			}
	        llOpponentsTurnWrapper.setVisibility(View.VISIBLE);
    	}
    	else {
    		llOpponentsTurnWrapper.setVisibility(View.GONE);
    	}
    	
    	
     //   Logger.w(TAG, "loadLists this.player.getCompletedGames() size=" + this.player.getCompletedGames().size() );
    	i = 1;
    	if (this.player.getCompletedGames().size() > 0){
	        for (Game g : this.player.getCompletedGames()){
	        	Logger.w(TAG, "loadLists this.player.getCompletedGames() game=" + g.getId() );
	        	
	        	llCompletedGames.addView(getGameYourTurnView(g, i == 1, this.player.getCompletedGames().size() == i));
	        	 i += 1;
			}
	        llCompletedGamesWrapper.setVisibility(View.VISIBLE);
    	}
    	else {
    		llCompletedGamesWrapper.setVisibility(View.GONE);
    	}

    }
    
    public View getGameYourTurnView(Game game, boolean firstItem, boolean lastItem ) {
    	
    //	Logger.d(TAG, "getGameYourTurnView started");
  		View view = LayoutInflater.from(this).inflate(R.layout.gameyourturnlistitem, null);
  
  		//just in case something fluky happened to the game and only one player was saved
  	 	int numPlayers = game.getPlayerGames().size();
	 	//Logger.w(TAG, "getGameYourTurnView numPlayers=" + numPlayers);
	 	if (numPlayers == 1){
	 		view.setVisibility(View.GONE);  
	 		return view;
	 	}
	//	Logger.d(TAG, "getGameYourTurnView 1");
  		 ImageFetcher imageLoader = new ImageFetcher(this, Constants.LARGE_AVATAR_SIZE, Constants.LARGE_AVATAR_SIZE, 0);
         imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
 	//	Logger.d(TAG, "getGameYourTurnView 2");
  	   // TextView tvPlayerName = (TextView)view.findViewById(R.id.tvOpponent1);
	 //	tvPlayerName.setText(game.getId()); //temp
        ImageView ivOpponentBadge_1 = (ImageView)view.findViewById(R.id.ivOpponentBadge_1);
	 	ImageView ivOpponentBadge_2 = (ImageView)view.findViewById(R.id.ivOpponentBadge_2);
	 	ImageView ivOpponentBadge_3 = (ImageView)view.findViewById(R.id.ivOpponentBadge_3);
	 	RelativeLayout rlPlayer_1 = (RelativeLayout)view.findViewById(R.id.rlPlayer_1);
	 	RelativeLayout rlPlayer_2 = (RelativeLayout)view.findViewById(R.id.rlPlayer_2);
	 	LinearLayout rlAvatars = (LinearLayout)view.findViewById(R.id.rlAvatars);
	 	
		RelativeLayout.LayoutParams layoutLastAction = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

        ImageView ivOpponent1 = (ImageView)view.findViewById(R.id.ivOpponent1);
	 	ImageView ivOpponent2 = (ImageView)view.findViewById(R.id.ivOpponent2);
	 	ImageView ivOpponent3 = (ImageView)view.findViewById(R.id.ivOpponent3);
	 	
	 	TextView tvOpponent_1 = (TextView)view.findViewById(R.id.tvOpponent_1);
	 	TextView tvOpponent_2 = (TextView)view.findViewById(R.id.tvOpponent_2);
	 	TextView tvOpponent_3 = (TextView)view.findViewById(R.id.tvOpponent_3);
	 	
	 	TextView tvLastAction = (TextView)view.findViewById(R.id.tvLastAction);
	 	ImageView ivChatAlert = (ImageView)view.findViewById(R.id.ivChatAlert);
	 	RelativeLayout llLastAction = (RelativeLayout)view.findViewById(R.id.llLastAction);
	 	
	 	if (!GameService.checkGameChatAlert(context, game, false)){
	 		ivChatAlert.setVisibility(View.GONE);
	 	}
	 	
	 	tvLastAction.setText(game.getLastActionTextForList(context, player.getId()));
	 
	 	//first opponent
	 	List<PlayerGame> opponentGames = game.getOpponentPlayerGames(this.player);

	 	if (opponentGames.size() == 1){
	 		tvOpponent_1.setText(opponentGames.get(0).getPlayer().getNameWithMaxLength(25));
	 	}
	 	else if (opponentGames.size() == 2){
	 		tvOpponent_1.setText(opponentGames.get(0).getPlayer().getNameWithMaxLength(19));
	 	}
	 	else{
	 		tvOpponent_1.setText(opponentGames.get(0).getPlayer().getNameWithMaxLength(13));
	 	}
	 	//Logger.d(TAG, "getGameYourTurnView 4.1");
	 	//RelativeLayout rlPlayer_1 = (RelativeLayout)view.findViewById(R.id.rlPlayer_1);
		int opponentBadgeId_1 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponentGames.get(0).getPlayer().getBadgeDrawable(), null, null);
		ivOpponentBadge_1.setImageResource(opponentBadgeId_1);

		imageLoader.loadImage(opponentGames.get(0).getPlayer().getImageUrl(), ivOpponent1);  
		//Logger.d(TAG, "getGameYourTurnView 5");
		//optional 2nd opponent
		
		if (lastItem){
			RelativeLayout rlLineItem = (RelativeLayout)view.findViewById(R.id.rlLineItem);
			int bgLineItem = context.getResources().getIdentifier("com.riotapps.word:drawable/text_selector_bottom", null, null);
			rlLineItem.setBackgroundResource(bgLineItem);
			LinearLayout llBottomBorder = (LinearLayout)view.findViewById(R.id.llBottomBorder);
			llBottomBorder.setVisibility(View.INVISIBLE);

		}
	//	if (firstItem){
	//		RelativeLayout rlLineItem = (RelativeLayout)view.findViewById(R.id.rlLineItem);
	//		int bgLineItem = context.getResources().getIdentifier("com.riotapps.word:drawable/text_selector_top", null, null);
	//		rlLineItem.setBackgroundResource(bgLineItem);
	//	}
		//drawable/text_selector_bottom
		
		if (opponentGames.size() >= 2){
			if (opponentGames.size() == 2){
				tvOpponent_2.setText(opponentGames.get(1).getPlayer().getNameWithMaxLength(19));
		 	}
		 	else{
		 		tvOpponent_2.setText(opponentGames.get(1).getPlayer().getNameWithMaxLength(13));
		 	}
		  
			int opponentBadgeId_2 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponentGames.get(1).getPlayer().getBadgeDrawable(), null, null);
			ivOpponentBadge_2.setImageResource(opponentBadgeId_2);
			imageLoader.loadImage( opponentGames.get(1).getPlayer().getImageUrl(), ivOpponent2);
	 	}
	 	else {
	 		
	 		rlPlayer_2.setVisibility(View.GONE);
	 		//TableRow trOpponent2 = (TableRow)view.findViewById(R.id.trOpponent2);
	 	//	trOpponent2.setVisibility(View.GONE);
	 		ivOpponent2.setVisibility(View.GONE);
	 	}
		//Logger.d(TAG, "getGameYourTurnView 6");
	 	//optional 3rd opponent
	 	if (opponentGames.size() >= 3){
		 	
		 	tvOpponent_3.setText(opponentGames.get(2).getPlayer().getNameWithMaxLength(13));
			int opponentBadgeId_3 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponentGames.get(2).getPlayer().getBadgeDrawable(), null, null);
			ivOpponentBadge_3.setImageResource(opponentBadgeId_3);
			imageLoader.loadImage( opponentGames.get(2).getPlayer().getImageUrl(), ivOpponent3);
	 	}
	 	else {
	 	//	TableRow trOpponent3 = (TableRow)view.findViewById(R.id.trOpponent3);
	 	//	trOpponent3.setVisibility(View.GONE);
	 		RelativeLayout rlPlayer_3 = (RelativeLayout)view.findViewById(R.id.rlPlayer_3);
	 		rlPlayer_3.setVisibility(View.GONE);
	 		ivOpponent3.setVisibility(View.GONE);
	 	}
		///Logger.d(TAG, "getGameYourTurnView 7");
	 	
	 	int badgeSize = Utils.convertDensityPixelsToPixels(context, 11);
	 	int badgeRightMargin = Utils.convertDensityPixelsToPixels(context, 2);
	 	int textSize;
	 	
	 	int badgeTopMargin;
		if (opponentGames.size() == 1){
			badgeTopMargin = Utils.convertDensityPixelsToPixels(context, 5);
			textSize = Utils.convertDensityPixelsToPixels(context, 14);
			tvOpponent_1.setTextSize(14);
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(badgeSize, badgeSize);
		    rlp.setMargins(0, badgeTopMargin, badgeRightMargin, 0); // llp.setMargins(left, top, right, bottom);
		    ivOpponentBadge_1.setLayoutParams(rlp);
		    
			layoutLastAction.addRule(RelativeLayout.RIGHT_OF, rlAvatars.getId());	
			layoutLastAction.addRule(RelativeLayout.BELOW, rlPlayer_1.getId());
			//layoutLastAction.setMargins(left, top, right, bottom); 
			llLastAction.setLayoutParams(layoutLastAction);
			
			//RelativeLayout.LayoutParams layoutAvatars = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			//layoutAvatars.setMargins(0, 0, 0, 30);
			//rlAvatars.setLayoutParams(layoutAvatars);

		}
		else if (opponentGames.size() == 2){
		
			
			badgeTopMargin = Utils.convertDensityPixelsToPixels(context, 4);
			textSize = Utils.convertDensityPixelsToPixels(context, 12);
			tvOpponent_1.setTextSize(13);
			tvOpponent_2.setTextSize(13);	
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(badgeSize, badgeSize);
		    rlp.setMargins(0, badgeTopMargin, badgeRightMargin, 0); // llp.setMargins(left, top, right, bottom);
		    ivOpponentBadge_2.setLayoutParams(rlp);
		    ivOpponentBadge_1.setLayoutParams(rlp);
		    
			layoutLastAction.addRule(RelativeLayout.BELOW, rlAvatars.getId());	
			llLastAction.setLayoutParams(layoutLastAction);
			//tvLastAction.setLayoutParams(layoutLastAction);
			
			RelativeLayout.LayoutParams layoutOpponent2  = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutOpponent2.setMargins(0, 4, 0, 0);
			layoutOpponent2.addRule(RelativeLayout.RIGHT_OF, rlAvatars.getId());	
			layoutOpponent2.addRule(RelativeLayout.BELOW, rlPlayer_1.getId());
			rlPlayer_2.setLayoutParams(layoutOpponent2);

		}
		else{
			layoutLastAction.addRule(RelativeLayout.BELOW, rlAvatars.getId());
			llLastAction.setLayoutParams(layoutLastAction);
			//tvLastAction.setLayoutParams(layoutLastAction);

		}
	 	
	 	view.setTag(game.getId());
	 	view.setOnClickListener(this);
  	    return view;
  	}
    
    
    @Override 
    public void onClick(View v) {
    	Intent intent;
    	
		 this.callingIntent = true;
			    
    	//stop running task if one is active
    	if (this.runningTask != null){
	  		this.runningTask.cancel(true);
	  		this.runningTask = null;
	  	} 
    	
    	switch(v.getId()){  
        case R.id.bStart:  
        	intent = new Intent(getApplicationContext(), StartGame.class);
			startActivity(intent);
			break;
        case R.id.bBadges:  
        	intent = new Intent(getApplicationContext(), Badges.class);
			startActivity(intent);
			break; 
        case R.id.bOptions:  
        	intent = new Intent(getApplicationContext(), Options.class);
			startActivity(intent);
			break;
        default:
        	String gameId = (String)v.getTag();
        	//DialogManager.SetupAlert(context, "tapped", gameId);
        	this.handleGameClick(gameId);
    	}
    	
    }  
    
   @Override
	protected void onPause() {
		// TODO Auto-generated method stub
	  	if (this.runningTask != null){
	  		this.runningTask.cancel(true);
	  	}
	  	this.stopTimer();

		super.onPause();
	}

   
private void handleGameClick(String gameId){	   
	   try { 
		   //this logic needs to be refactored more than likely
		   Logger.d(TAG, "handleGameClick called");
		   
		   Game game = GameService.getGameFromLocal(gameId);
		   
		
		   
		   Logger.d(TAG, "handleGameClick gameId param=" + gameId + " stored=" + game.getId());
		   if (game.getId().equals(gameId)){
			   long localStorageDuration = (System.nanoTime() / 1000000) - game.getLocalStorageDateInMilliseconds();
			   
			   Logger.d(TAG, "handleGameClick called this.player.getLastPlayedDateFromGameList(gameId)=" + this.player.getLastPlayedDateFromGameList(gameId));
			   Logger.d(TAG, "handleGameClick called  game.getLocalStorageLastTurnDate()=" + game.getLocalStorageLastTurnDate());
			   Logger.d(TAG, "handleGameClick called  localStorageDuration=" + localStorageDuration);
			   Logger.d(TAG, "handleGameClick called  game.getLocalStorageDateInMilliseconds()=" + game.getLocalStorageDateInMilliseconds());
			   Logger.d(TAG, "handleGameClick called  System.nanoTime() / 1000000=" + (System.nanoTime() / 1000000));
			   
			   //also compare lastturndate from list and local storage..if list is later, refresh game from server 
			   //if (this.player.getLastPlayedDateFromGameList(gameId) > game.getLocalStorageLastTurnDate()){
			   
			   //also compare turns from list and local storage..if list is different, refresh game from server 
			   Game gameFromList = this.player.getGameFromLists(gameId);
				if (gameFromList != null && gameFromList.getTurn() == game.getTurn()){
				   Logger.d(TAG, "handleGameClick game Found localStorageDuration=" + localStorageDuration);
				   if (localStorageDuration < Constants.LOCAL_GAME_STORAGE_DURATION_IN_MILLISECONDS){
				 	//game was found locally and was stored there less than 15 seconds ago, no need to hit the server
					   Logger.d(TAG, "handleGameClick game Found with local storage duration. bypassing server fetch");
			            Intent intent = new Intent(context, com.riotapps.word.GameSurface.class);
			            intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
			      	    context.startActivity(intent);
					    
			      	    return;
				   }
			   }
		   }
			String json = GameService.setupGetGame(this, gameId);
			 Logger.w(TAG, "handleGameClick game not Found with local storage duration. starting server fetch");
			//this will bring back the players games too
			this.runningTask = new NetworkTask(this, RequestType.POST, getString(R.string.progress_loading), json, true);
			this.runningTask.execute(Constants.REST_GET_GAME_URL);
			
		 
		} catch (DesignByContractException e) {
			 DialogManager.SetupAlert(context, getString(R.string.oops), e.getLocalizedMessage(), true, 0);
		}
 
   }
    
    private class NetworkTask extends AsyncNetworkRequest{

		MainLanding context;
		boolean fetchGame = false;
		
		
		public NetworkTask(MainLanding ctx, RequestType requestType, String shownOnProgressDialog, String jsonPost, boolean fetchGame) {
			super(ctx, requestType, shownOnProgressDialog, jsonPost);
			this.context = ctx;
			this.fetchGame = fetchGame;
		 
		}

		@Override
		protected void onPostExecute(NetworkTaskResult result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			this.context.handleResponse(result, this.fetchGame);
		}
 	}
	
	private void handleResponse(NetworkTaskResult result, boolean fetchGame){
	     Exception exception = result.getException();   

	     if(result.getResult() != null){  
	         switch(result.getStatusCode()){  
	             case 200:  
	            	 try{
	            		 if (fetchGame){
			            	 
			            	 Game game = GameService.handleCreateGameResponse(this.context, result.getResult());
			    
			            	 //saving game locally instead of passing by parcel because nested parcelable classes with lists of more nests
			            	 //was not working and driving me crazy
			            	 GameService.putGameToLocal(context, game);
			            	 
			            	 Intent intent = new Intent(this.context, com.riotapps.word.GameSurface.class);
			            	 intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
			            	 
			            	 game = null;
			      	      	 this.context.startActivity(intent);
	            		 }
	            		 else{	 //this is the same as authenticating, so this is ok
	            			 Logger.d(TAG, "handleAuthByTokenResponse after timer");
		            		 player = PlayerService.handleAuthByTokenResponse(this.context, result.getResult());
		            		 GameService.updateLastGameListCheckTime(this.context);
		            		 
		            		 if (player.getTotalNumLocalGames() == 0){
		            			 Intent intent = new Intent( context, com.riotapps.word.StartGame.class);
		            			 //intent.putExtra(Constants.EXTRA_GAME_LIST_PREFETCHED, true);
		            			 this.startActivity(intent);
		            		 }
	            		 
	            		 loadLists();
	            		 }
	            		 
	            	 }
	            	 catch(Exception e){
	            		// e.getStackTrace().toString()
	            		 Logger.w(TAG, "status code=200 " + e.toString());
	            		 String err = (e.getMessage()==null)?"status code=200, unknown error":e.getMessage();
	            		 Logger.w(TAG, err);
	            		 DialogManager.SetupAlert(context, this.getString(R.string.sorry), err, true, 0); 
	            	 }
	               break;
	             case 401:    
	                //unauthorized
	            	 //clear local storage and send to login
	            	 PlayerService.clearLocalStorageAndCache(this);

	            	 Intent intent = new Intent( context, com.riotapps.word.Welcome.class);
		     	     this.startActivity(intent);
		     	     break;  

	             case 404:
	            	 //no updates, do nothing
	
	            	 break;
	             case 422: 
	             case 500:

	            	 DialogManager.SetupAlert(context, this.getString(R.string.oops), result.getStatusCode() + " " + result.getStatusReason(), true, 0);  
	            	 break;
	         }  
	     }else if (exception instanceof ConnectTimeoutException ||  exception instanceof java.net.SocketTimeoutException) {
	    	 DialogManager.SetupAlert(context, this.getString(R.string.oops), this.getString(R.string.msg_connection_timeout), true, 0);
	     }else if(exception != null){  
	    	 DialogManager.SetupAlert(context, this.getString(R.string.oops), this.getString(R.string.msg_not_connected), true, 0);  

	     }  
	     else{  
	         Logger.d("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

	     }//end of else  
	}
    
    
}
        
