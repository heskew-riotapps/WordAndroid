package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import com.riotapps.word.hooks.FBFriend;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.CustomDialog;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Enums.RequestType;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChooseFBFriends extends FragmentActivity implements View.OnClickListener{
	
	private static final String TAG = ChooseFBFriends.class.getSimpleName();
	 
	Player player;
	ChooseFBFriends context = this;
	Game game;
	int maxAvailable;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosefbfriends);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);

        
        player = PlayerService.getPlayerFromLocal();
    	PlayerService.loadPlayerInHeader(this);
   
    	Intent i = getIntent();
    	this.game =  (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
    	
    	
      //  Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	  //  t.show();
    	  
    	Button bAddFBFriends = (Button)findViewById(R.id.bAddFBFriends); 
     
    	TextView tvSubtitle =(TextView)findViewById(R.id.tvSubtitle);
    	 
    	if (this.game.getPlayerGames().size() == 1 ){
    		tvSubtitle.setText(this.getString(R.string.choose_opponents_up_to_3_subtitle));
    		this.maxAvailable = 3;
       	}
    	else if(this.game.getPlayerGames().size() == 2) {
    		tvSubtitle.setText(this.getString(R.string.choose_opponents_up_to_2_subtitle));
    		this.maxAvailable = 2;
    	}
    	else {
    		tvSubtitle.setText(this.getString(R.string.choose_opponents_one_more_subtitle));
    		this.maxAvailable = 1;
    	}
     
    	this.loadListPrep();
    	
        //bAddFBFriends.setOnClickListener(this);
           
      
            //in this case ListView was major overkill
          //  LinearLayout llPlayers = (LinearLayout)findViewById(R.id.llPlayers);
            
          //  for (Player p : this.game.getOpponents(player)){
          //  	llPlayers.addView(getView(p));
		//	}

    }
  
    
    @Override 
    public void onClick(View v) {
    	Intent intent;
    	switch(v.getId()){  
        case R.id.bAddFBFriends:  
        	//find checked opponents and add to game
        	//return error if none or too many checked
          	intent = new Intent(this.context, FindPlayer.class);
          	intent.putExtra(Constants.EXTRA_GAME, this.game);
			this.context.startActivity(intent);
			break;
        case R.id.tvStartByFacebook:  
        	intent = new Intent(this.context, FindPlayer.class);
        	intent.putExtra(Constants.EXTRA_GAME, this.game);
			this.context.startActivity(intent);
			break;
	    case R.id.tvStartByOpponent:  
	    	intent = new Intent(this.context, FindPlayer.class);
			intent.putExtra(Constants.EXTRA_GAME, this.game);
			this.context.startActivity(intent);
			break;
    
	    case R.id.bStartGame:  
	    	try {
				String json = GameService.setupStartGame(context, this.game);
				
				Logger.d(TAG, "bStartGame json=" + json);
				//kick off thread
				 new NetworkTask(context, RequestType.POST, json, getString(R.string.progress_starting_game)).execute(Constants.REST_CREATE_GAME_URL);
				
			} 
			catch (DesignByContractException e) {
				//e.printStackTrace();
				DialogManager.SetupAlert(this.context, getString(R.string.oops), e.getMessage(), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
			}
			break;

    	}
    	
    }  
    private void loadListPrep() {
    	
    	//only do this once a day at the most (or so)
    	
    	String json;
		try {
			json = PlayerService.setupFindPlayersByFB(context);
			//fetch fb friends (which is already stored locally) that are registered with wordsmash already
			Logger.d(TAG, "loadListPrep json=" + json);
			
	    	new NetworkTask(context, RequestType.POST, json, getString(R.string.progress_syncing)).execute(Constants.REST_FIND_REGISTERED_FB_FRIENDS);
		
		} catch (DesignByContractException e) {
			DialogManager.SetupAlert(this.context, getString(R.string.oops), e.getMessage(), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
		}
		
		//if we already have the friends list stored locally just load the list 
		//this.loadList();
    }
    
    private void loadList(FBFriend[] friends){
    	FBFriendArrayAdapter adapter = new FBFriendArrayAdapter(context, friends);
    
    	ListView lvFBFriends = (ListView) findViewById(R.id.lvFBFriends);
    	lvFBFriends.setAdapter(adapter); 

    }
    
    private class FBFriendArrayAdapter extends ArrayAdapter<Game> {
	   	  private final Context context;
	   	  private final Game[] values;
	
	   	  public FBFriendArrayAdapter(Context context, FBFriend[] values) {
	   	    super(context, R.layout.choosefbfrienditem, values);
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

    
private class NetworkTask extends AsyncNetworkRequest{
		
	ChooseFBFriends context;
		
		public NetworkTask(ChooseFBFriends ctx, RequestType requestType,
				String json,
				String shownOnProgressDialog) {
			super(ctx, requestType, shownOnProgressDialog, json);
			this.context = ctx;
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPostExecute(ServerResponse serverResponseObject) {
			// TODO Auto-generated method stub
			super.onPostExecute(serverResponseObject);
			
			this.handleResponse(serverResponseObject);
			
			
		}
 
		private void handleResponse(ServerResponse serverResponseObject){
		     HttpResponse response = serverResponseObject.response;   
		     Exception exception = serverResponseObject.exception;   

		     if(response != null){  

		         InputStream iStream = null;  

		         try {  
		             iStream = response.getEntity().getContent();  
		         } catch (IllegalStateException e) {  
		             Log.e("in ResponseHandler -> in handleResponse() -> in if(response !=null) -> in catch ","IllegalStateException " + e);  
		         } catch (IOException e) {  
		             Log.e("in ResponseHandler -> in handleResponse() -> in if(response !=null) -> in catch ","IOException " + e);  
		         }  

		         int statusCode = response.getStatusLine().getStatusCode();  
		         
		         Log.i(ChooseFBFriends.TAG, "StatusCode: " + statusCode);

		         switch(statusCode){  
		             case 200:  
		             case 201: {
		            	 
		              Game game = GameService.handleCreateGameResponse(this.context, iStream);
		            //	 handleResponseFromIOThread(game);
		            	 //saving game locally instead of passing by parcel because nested parcelable classes with lists of more nests
		            	 //was not working and driving me crazy
		            //	 GameService.putGameToLocal(context, game);
		            	 
		            //	 Intent intent = new Intent(this.context, com.riotapps.word.GameSurface.class);
		            	 
		           // 	 Logger.d(TAG, "game about to be added as extra");
		         	   //  intent.putExtra(Constants.EXTRA_GAME, game);
		           // 	 intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
		           // 	 Logger.d(TAG, "game added as extra");
		      	     // 	 this.context.startActivity(intent);
		                 break;  

		             }//end of case 200 & 201 
		             case 401:
			             //case Status code == 422
			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.validation_unauthorized), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
			            	 break;
		             case 404:
		             //case Status code == 422
		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.find_player_opponent_not_found), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
		            	 break;
		             case 422: 
		             case 500:

		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), statusCode + " " + response.getStatusLine().getReasonPhrase(), 0);  
		            	 break;
		         }  
		     }else if (exception instanceof ConnectTimeoutException) {
		    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_connection_timeout), 0);
		     }else if(exception != null){  
		    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_not_connected), 0);  

		     }  
		     else{  
		         Log.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

		     }//end of else  
		}
		
 
	}
}
        
