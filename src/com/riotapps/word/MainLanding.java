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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlanding);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);

        //PlayerService playerSvc = new PlayerService();
        //Player player = PlayerService.getPlayerFromLocal();
        
       // Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	   // t.show();
        
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
		
		if (!isGameListPrefetched){
			//fetch games
 
			try {
				String json = PlayerService.setupAuthTokenCheck(this, this.player.getAuthToken());
				//this will bring back the players games too
				new NetworkTask(this, RequestType.POST, json).execute(Constants.REST_AUTHENTICATE_PLAYER_BY_TOKEN);
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
		else {
			this.loadLists();
		}
    }
    
    private void loadLists(){
		ListView listYourTurn = (ListView) findViewById(R.id.listYourTurn);
		ListView listOpponentTurn = (ListView) findViewById(R.id.listOpponentTurn);
		ListView listCompletedGames = (ListView) findViewById(R.id.listCompletedGames);
		//go get games
		//check locally, if array(s) is empty hit the server
		//set up list views
		
		//do this in callback listener from network task, if array is empty
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Fourth - the Array of data

		if (player.getActiveGamesYourTurn().size() == 0){
			listYourTurn.setVisibility(View.GONE);
		}
		else {
			YourTurnArrayAdapter adapter = new YourTurnArrayAdapter(context, (Game[]) player.getActiveGamesYourTurn().toArray());
			listYourTurn.setAdapter(adapter); 
		}
		
		if (player.getActiveGamesOpponentTurn().size() == 0){
			listOpponentTurn.setVisibility(View.GONE);
		}
		else {
			OpponentTurnArrayAdapter adapter = new OpponentTurnArrayAdapter(context, (Game[]) player.getActiveGamesOpponentTurn().toArray());
			listOpponentTurn.setAdapter(adapter); 
		}
		
		if (player.getCompletedGames().size() == 0){
			listCompletedGames.setVisibility(View.GONE);
		}
		else {
			CompletedGameArrayAdapter adapter = new CompletedGameArrayAdapter(context, (Game[]) player.getCompletedGames().toArray());
			listCompletedGames.setAdapter(adapter); 
		}
		//		if (games.size() > 0){
			 
//			YourTurnArrayAdapter adapter = new YourTurnArrayAdapter(context, (Game[]) games.toArray());
	
//			// Assign adapter to ListView
//			listYourTurn.setAdapter(adapter); 
//		}
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

    	}
    	
    }  
    
    private class YourTurnArrayAdapter extends ArrayAdapter<Game> {
    	  private final Context context;
    	  private final Game[] values;

    	  public YourTurnArrayAdapter(Context context, Game[] values) {
    	    super(context, R.layout.gameyourturnlistitem, values);
    	    this.context = context;
    	    this.values = values;
    	  }

    	  @Override
    	  public View getView(int position, View convertView, ViewGroup parent) {
    	    LayoutInflater inflater = (LayoutInflater) context
    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	    View rowView = inflater.inflate(R.layout.gameyourturnlistitem, parent, false);
    	    ImageView ivOpponent1 = (ImageView) rowView.findViewById(R.id.ivOpponent1);
    	   // textView.setText(values[position]);
    	

    	    return rowView;
    	  }
    }
    
    private class OpponentTurnArrayAdapter extends ArrayAdapter<Game> {
  	  private final Context context;
  	  private final Game[] values;

  	  public OpponentTurnArrayAdapter(Context context, Game[] values) {
  	    super(context, R.layout.gameopponentturnlistitem, values);
  	    this.context = context;
  	    this.values = values;
  	  }

  	  @Override
  	  public View getView(int position, View convertView, ViewGroup parent) {
  	    LayoutInflater inflater = (LayoutInflater) context
  	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  	    View rowView = inflater.inflate(R.layout.gameopponentturnlistitem, parent, false);
  	    ImageView ivOpponent1 = (ImageView) rowView.findViewById(R.id.ivOpponent1);
  	   // textView.setText(values[position]);
  	

  	    return rowView;
  	  }
  }
    
    private class CompletedGameArrayAdapter extends ArrayAdapter<Game> {
    	  private final Context context;
    	  private final Game[] values;

    	  public CompletedGameArrayAdapter(Context context, Game[] values) {
    	    super(context, R.layout.gamecompletedlistitem, values);
    	    this.context = context;
    	    this.values = values;
    	  }

    	  @Override
    	  public View getView(int position, View convertView, ViewGroup parent) {
    	    LayoutInflater inflater = (LayoutInflater) context
    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	    View rowView = inflater.inflate(R.layout.gamecompletedlistitem, parent, false);
    	    ImageView ivOpponent1 = (ImageView) rowView.findViewById(R.id.ivOpponent1);
    	   // textView.setText(values[position]);
    	

    	    return rowView;
    	  }
    }
      
    
    private class NetworkTask extends AsyncNetworkRequest{

		MainLanding context;
		
		public NetworkTask(MainLanding ctx, RequestType requestType,
				String jsonPost) {
			super(ctx, requestType, "", jsonPost);
			this.context = ctx;
		 
		}

		@Override
		protected void onPostExecute(ServerResponse serverResponseObject) {
			// TODO Auto-generated method stub
			super.onPostExecute(serverResponseObject);
			
			this.context.handleResponse(serverResponseObject);
		}
 	}
	
	private void handleResponse(ServerResponse serverResponseObject){
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
	            		 //this is the same as authenticating, so this is ok
	            		 player = PlayerService.handleAuthByTokenResponse(this.context, iStream);
	            		 
	            		 if (player.getTotalNumLocalGames() == 0){
	            			 Intent intent = new Intent( this, com.riotapps.word.StartGame.class);
	            			 //intent.putExtra(Constants.EXTRA_GAME_LIST_PREFETCHED, true);
	            			 this.startActivity(intent);
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
        
