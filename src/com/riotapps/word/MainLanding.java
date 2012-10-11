package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
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
  		View view = LayoutInflater.from(this).inflate(R.layout.gameyourturnlistitem, null);
  
  	   // TextView tvPlayerName = (TextView)view.findViewById(R.id.tvOpponent1);
	 //	tvPlayerName.setText(game.getId()); //temp
	 	ImageView ivOpponent1 = (ImageView)view.findViewById(R.id.ivOpponent1);
	 	ImageView ivOpponent2 = (ImageView)view.findViewById(R.id.ivOpponent2);
	 	ImageView ivOpponent3 = (ImageView)view.findViewById(R.id.ivOpponent3);

	 	int numPlayers = game.getPlayerGames().size();
	 	Logger.w(TAG, "getGameYourTurnView numPlayers=" + numPlayers);
	 	if (numPlayers == 1){
	 		view.setVisibility(View.GONE);
	 		return view;
	 	}
	 	
	 	//fill opponent images.  the context player should be ignored from this list, since only opponents are shown
	 	//is the context player the first in the list?, if so skip him
	 	int i = (game.getPlayerGames().get(0).getPlayer().getId().equals(this.player.getId()) ? 1 : 0);
		imageLoader.loadImage(game.getPlayerGames().get(i).getPlayer().getImageUrl(), ivOpponent1);  
		
		//advance to next player
		i += 1; 
			
		if (numPlayers >=3){
			//is the context player the next in the list?, if so skip her
		 	i = (game.getPlayerGames().get(i).getPlayer().getId().equals(this.player.getId()) ? i + 1 : i);
		 	
		 	//Logger.w(TAG, "getGameYourTurnView player3 imageUrl=" + numPlayers);
			imageLoader.loadImage(game.getPlayerGames().get(i).getPlayer().getImageUrl(), ivOpponent2);  
			i += 1;			
		}
		else{
			ivOpponent2.setVisibility(View.GONE);
		}

		
		if (numPlayers == 4){
			//is the context player the next in the list?, if so skip her		
		 	i = (game.getPlayerGames().get(i).getPlayer().getId().equals(this.player.getId()) ? i + 1 : i);
			imageLoader.loadImage(game.getPlayerGames().get(i).getPlayer().getImageUrl(), ivOpponent3);  	
		}
		else{
			ivOpponent3.setVisibility(View.GONE);
		}
	 	view.setTag(game.getId());
	 	view.setOnClickListener(this);

//		TextView tvPlayerWins = (TextView)view.findViewById(R.id.tvPlayerWins);
//		tvPlayerWins.setText(String.format(this.context.getString(R.string.line_item_num_wins),opponent.getNumWins()));
//	 	
//	 	ImageFetcher imageLoader = new ImageFetcher(this.context, 34, 34, 0);
//		imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
//		ImageView ivPlayer = (ImageView) view.findViewById(R.id.ivPlayer);
		
//		imageLoader.loadImage(opponent.getImageUrl(), ivPlayer); //default image
		
//		int badgeId = getResources().getIdentifier(Constants.DRAWABLE_LOCATION + opponent.getBadgeDrawable(), null, null);
//		ImageView ivBadge = (ImageView)view.findViewById(R.id.ivBadge);	 
//		ivBadge.setImageResource(badgeId);
	 
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
		   Game game = GameService.getGameFromLocal(gameId);
		   if (game.getId() == gameId){
			   if (System.nanoTime() / 1000000 - game.getLocalStorageDateInMilliseconds() < Constants.LOCAL_GAME_STORAGE_DURATION_IN_MILLISECONDS){
			 	//game was found locally and was stored there less than 15 seconds ago, no need to hit the server
				   
		            Intent intent = new Intent(this.context, com.riotapps.word.GameSurface.class);
		            intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
		      	    this.context.startActivity(intent);
				   
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
		            			 Intent intent = new Intent( this, com.riotapps.word.StartGame.class);
		            			 //intent.putExtra(Constants.EXTRA_GAME_LIST_PREFETCHED, true);
		            			 this.startActivity(intent);
		            		 }
	            		 
	            		 loadLists();
	            		 }
	            		 
	            	 }
	            	 catch(Exception e){
	            		 Logger.w(TAG, e.getLocalizedMessage());
	            		 DialogManager.SetupAlert(this, this.getString(R.string.sorry), e.getLocalizedMessage(), true, 0); 
	            	 }
	               break;
	             case 401:    
	                //unauthorized
	            	 //clear local storage and send to login
	            	 PlayerService.clearLocalStorageAndCache(this);

	            	 Intent intent = new Intent( this, com.riotapps.word.Welcome.class);
		     	     this.startActivity(intent);
		     	     break;  

	             case 404:
	            	 DialogManager.SetupAlert(this, this.getString(R.string.sorry), this.getString(R.string.server_404_error), true, 0);
	            	 break;
	             case 422: 
	             case 500:

	            	 DialogManager.SetupAlert(this, this.getString(R.string.oops), statusCode + " " + response.getStatusLine().getReasonPhrase(), true, 0);  
	            	 break;
	         }  
	     }else if (exception instanceof ConnectTimeoutException) {
	    	 DialogManager.SetupAlert(this, this.getString(R.string.oops), this.getString(R.string.msg_connection_timeout), true, 0);
	     }else if(exception != null){  
	    	 DialogManager.SetupAlert(this, this.getString(R.string.oops), this.getString(R.string.msg_not_connected), true, 0);  

	     }  
	     else{  
	         Logger.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

	     }//end of else  
	}
    
    
}
        
