package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Utils;
import com.riotapps.word.utils.Enums.RequestType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlanding);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);

        //PlayerService playerSvc = new PlayerService();
        //Player player = PlayerService.getPlayerFromLocal();
        
       // Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	   // t.show();
        
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
	 	
		if (!isGameListPrefetched){
			//fetch games
 
			try { 
				String json = PlayerService.setupAuthTokenCheck(this, this.player.getAuthToken());
				//this will bring back the players games too
				new NetworkTask(this, RequestType.POST, "", json, false).execute(Constants.REST_AUTHENTICATE_PLAYER_BY_TOKEN);
			} catch (DesignByContractException e) {
				//this should never happen unless there is some tampering
				 DialogManager.SetupAlert(context, getString(R.string.oops), e.getLocalizedMessage(), true, 0);
			}

		}	
		//no games yet, send player to StartGame to get started
	//	else if (this.player.getTotalNumLocalGames() == 0){
     //   	Intent intent = new Intent(getApplicationContext(), StartGame.class);
	//		startActivity(intent);	
	//	}
		//else {
		//	this.loadLists();
		//}
    }
     
    private void loadLists(){
 
    	//Logger.d(TAG, "loadLists started");
    	LinearLayout llYourTurn = (LinearLayout)findViewById(R.id.llYourTurn);
    	LinearLayout llOpponentsTurn = (LinearLayout)findViewById(R.id.llOpponentsTurn);
      	LinearLayout llYourTurnWrapper = (LinearLayout)findViewById(R.id.llYourTurnWrapper);
    	LinearLayout llOpponentsTurnWrapper = (LinearLayout)findViewById(R.id.llOpponentsTurnWrapper);
    	
    	//clear out view
    	llYourTurn.removeAllViews();
    	llOpponentsTurn.removeAllViews();

    	
   //	Logger.w(TAG, "loadLists this.player.getActiveGamesYourTurn() size=" + this.player.getActiveGamesYourTurn().size() );
    //	Logger.w(TAG, "loadLists this.player.getActiveGamesOpponentTurn() size=" + this.player.getActiveGamesOpponentTurn().size() );
    	
    	if (this.player.getActiveGamesYourTurn().size() > 0){
	        for (Game g : this.player.getActiveGamesYourTurn()){
	        	 llYourTurn.addView(getGameYourTurnView(g));
			}
	        llYourTurnWrapper.setVisibility(View.VISIBLE);
    	}
    	else {
    		llYourTurnWrapper.setVisibility(View.GONE);
    	}

    	if (this.player.getActiveGamesOpponentTurn().size() > 0){
	        for (Game g : this.player.getActiveGamesOpponentTurn()){
	        	llOpponentsTurn.addView(getGameYourTurnView(g));
			}
	        llOpponentsTurnWrapper.setVisibility(View.VISIBLE);
    	}
    	else {
    		llOpponentsTurnWrapper.setVisibility(View.GONE);
    	}

    }
    
    public View getGameYourTurnView(Game game ) {
    	
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
  		 ImageFetcher imageLoader = new ImageFetcher(this, Constants.DEFAULT_AVATAR_SIZE, Constants.DEFAULT_AVATAR_SIZE, 0);
         imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
 	//	Logger.d(TAG, "getGameYourTurnView 2");
  	   // TextView tvPlayerName = (TextView)view.findViewById(R.id.tvOpponent1);
	 //	tvPlayerName.setText(game.getId()); //temp
	 	ImageView ivOpponent1 = (ImageView)view.findViewById(R.id.ivOpponent1);
	 	ImageView ivOpponent2 = (ImageView)view.findViewById(R.id.ivOpponent2);
	 	ImageView ivOpponent3 = (ImageView)view.findViewById(R.id.ivOpponent3);
		Logger.d(TAG, "getGameYourTurnView 3");
	 	ImageView ivOpponentBadge_1 = (ImageView)view.findViewById(R.id.ivOpponentBadge_1);
	 	TextView tvOpponent_1 = (TextView)view.findViewById(R.id.tvOpponent_1);
		Logger.d(TAG, "getGameYourTurnView 4");
	 	//first opponent
	 	List<PlayerGame> opponentGames = game.getOpponentPlayerGames(this.player);
	 	
	 	//Logger.d(TAG, "getGameYourTurnView opponebtGames count" + opponentGames.size());
	 	//Logger.d(TAG, "getGameYourTurnView this.player" + this.player.getAbbreviatedName());

	 	tvOpponent_1.setText(opponentGames.get(0).getPlayer().getAbbreviatedName());
	 	//Logger.d(TAG, "getGameYourTurnView 4.1");
		int opponentBadgeId_1 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponentGames.get(0).getPlayer().getBadgeDrawable(), null, null);
	 	//Logger.d(TAG, "getGameYourTurnView 4.2");
	
		ivOpponentBadge_1.setImageResource(opponentBadgeId_1);
	 	//Logger.d(TAG, "getGameYourTurnView 4.3");

		imageLoader.loadImage(opponentGames.get(0).getPlayer().getImageUrl(), ivOpponent1);  
		//Logger.d(TAG, "getGameYourTurnView 5");
		//optional 2nd opponent
	 	if (opponentGames.size() >= 2){
		 	ImageView ivOpponentBadge_2 = (ImageView)view.findViewById(R.id.ivOpponentBadge_2);
		 	TextView tvOpponent_2 = (TextView)view.findViewById(R.id.tvOpponent_2);
		 	
		 	tvOpponent_2.setText(opponentGames.get(1).getPlayer().getAbbreviatedName());
			int opponentBadgeId_2 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponentGames.get(1).getPlayer().getBadgeDrawable(), null, null);
			ivOpponentBadge_2.setImageResource(opponentBadgeId_2);
			imageLoader.loadImage( opponentGames.get(1).getPlayer().getImageUrl(), ivOpponent2);
	 	}
	 	else {
	 		TableRow trOpponent2 = (TableRow)view.findViewById(R.id.trOpponent2);
	 		trOpponent2.setVisibility(View.GONE);
	 		ivOpponent2.setVisibility(View.GONE);
	 	}
		//Logger.d(TAG, "getGameYourTurnView 6");
	 	//optional 3rd opponent
	 	if (opponentGames.size() >= 3){
		 	ImageView ivOpponentBadge_3 = (ImageView)view.findViewById(R.id.ivOpponentBadge_3);
		 	TextView tvOpponent_3 = (TextView)view.findViewById(R.id.tvOpponent_3);
		 	
		 	tvOpponent_3.setText(opponentGames.get(2).getPlayer().getAbbreviatedName());
			int opponentBadgeId_3 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + opponentGames.get(2).getPlayer().getBadgeDrawable(), null, null);
			ivOpponentBadge_3.setImageResource(opponentBadgeId_3);
			imageLoader.loadImage( opponentGames.get(2).getPlayer().getImageUrl(), ivOpponent3);
	 	}
	 	else {
	 		TableRow trOpponent3 = (TableRow)view.findViewById(R.id.trOpponent3);
	 		trOpponent3.setVisibility(View.GONE);
	 		ivOpponent3.setVisibility(View.GONE);
	 	}
		///Logger.d(TAG, "getGameYourTurnView 7");
	 	
	 	
	 	view.setTag(game.getId());
	 	view.setOnClickListener(this);
  	    return view;
  	}
    
    
    @Override 
    public void onClick(View v) {
    	Intent intent;
    	
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
    
   private void handleGameClick(String gameId){	   
	   try { 
		   //this logic needs to be refactored more than likely
		   Logger.w(TAG, "handleGameClick called");
		   
		   Game game = GameService.getGameFromLocal(gameId);
		   
		   Logger.w(TAG, "handleGameClick gameId param=" + gameId + " stored=" + game.getId());
		   if (game.getId().equals(gameId)){
			   long localStorageDuration = System.nanoTime() / 1000000 - game.getLocalStorageDateInMilliseconds();
			   Logger.w(TAG, "handleGameClick game Found localStorageDuration=" + localStorageDuration);
			   if (localStorageDuration < Constants.LOCAL_GAME_STORAGE_DURATION_IN_MILLISECONDS){
			 	//game was found locally and was stored there less than 15 seconds ago, no need to hit the server
				   Logger.w(TAG, "handleGameClick game Found with local storage duration. bypassing server fetch");
		            Intent intent = new Intent(context, com.riotapps.word.GameSurface.class);
		            intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
		      	    context.startActivity(intent);
				    
		      	    return;
			   }
		   }
			String json = GameService.setupGetGame(this, gameId);
			//this will bring back the players games too
			new NetworkTask(this, RequestType.POST, getString(R.string.progress_loading), json, true).execute(Constants.REST_GET_GAME_URL);
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
		protected void onPostExecute(ServerResponse serverResponseObject) {
			// TODO Auto-generated method stub
			super.onPostExecute(serverResponseObject);
			
			this.context.handleResponse(serverResponseObject, this.fetchGame);
		}
 	}
	
	private void handleResponse(ServerResponse serverResponseObject, boolean fetchGame){
	     HttpResponse response = serverResponseObject.response;   
	     Exception exception = serverResponseObject.exception;   

	     if(response != null){  

	         InputStream iStream = null;  

	         try {  
	             iStream = response.getEntity().getContent();  
	         } catch (IllegalStateException e) {  
	             Logger.e("in ResponseHandler -> in handleResponse() -> in if(response !=null) -> in catch ","IllegalStateException " + e);  
	         } catch (IOException e) {  
	             Logger.e("in ResponseHandler -> in handleResponse() -> in if(response !=null) -> in catch ","IOException " + e);  
	         }  

	         int statusCode = response.getStatusLine().getStatusCode();  
	         
	         Logger.i(MainLanding.TAG, "StatusCode: " + statusCode); 

	         switch(statusCode){  
	             case 200:  
	            	 try{
	            		 if (fetchGame){
			            	 
			            	 Game game = GameService.handleCreateGameResponse(this.context, iStream);
			            //	 handleResponseFromIOThread(game);
			            	 //saving game locally instead of passing by parcel because nested parcelable classes with lists of more nests
			            	 //was not working and driving me crazy
			            	 GameService.putGameToLocal(context, game);
			            	 
			            	 Intent intent = new Intent(this.context, com.riotapps.word.GameSurface.class);
			            	 intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
			      	      	 this.context.startActivity(intent);
	            		 }
	            		 else{	 //this is the same as authenticating, so this is ok
		            		 player = PlayerService.handleAuthByTokenResponse(this.context, iStream);
		            		 
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
	            	 DialogManager.SetupAlert(context, this.getString(R.string.sorry), this.getString(R.string.server_404_error), true, 0);
	            	 break;
	             case 422: 
	             case 500:

	            	 DialogManager.SetupAlert(context, this.getString(R.string.oops), statusCode + " " + response.getStatusLine().getReasonPhrase(), true, 0);  
	            	 break;
	         }  
	     }else if (exception instanceof ConnectTimeoutException) {
	    	 DialogManager.SetupAlert(context, this.getString(R.string.oops), this.getString(R.string.msg_connection_timeout), true, 0);
	     }else if(exception != null){  
	    	 DialogManager.SetupAlert(context, this.getString(R.string.oops), this.getString(R.string.msg_not_connected), true, 0);  

	     }  
	     else{  
	         Logger.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

	     }//end of else  
	}
    
    
}
        
