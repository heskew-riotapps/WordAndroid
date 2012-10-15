package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import com.riotapps.word.hooks.FBFriend;
import com.riotapps.word.hooks.FBFriends;
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
import com.riotapps.word.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChooseFBFriends extends FragmentActivity implements View.OnClickListener{
	
	private static final String TAG = ChooseFBFriends.class.getSimpleName();
	 
	Player player;
	ChooseFBFriends context = this;
	Game game;
	int maxAvailable;
	ImageFetcher imageLoader;
	ArrayList<Integer> selectedIds = new ArrayList<Integer>();
	int itemBGColor;
	int itemBGSelectedColor;
	FBFriends friends;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosefbfriends);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);

        this.imageLoader = new ImageFetcher(this, Constants.DEFAULT_AVATAR_SIZE, Constants.DEFAULT_AVATAR_SIZE, 0);
       this.imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
        
        player = PlayerService.getPlayerFromLocal();
    	PlayerService.loadPlayerInHeader(this);
   
    	Intent i = getIntent();
    	this.game =  (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
    	this.itemBGColor =  getResources().getIdentifier(Constants.DRAWABLE_LOCATION + Constants.CONTENT_AREA_BACKGROUND_COLOR, null, null);
    	this.itemBGSelectedColor =  getResources().getIdentifier(Constants.DRAWABLE_LOCATION + Constants.CONTENT_AREA_BACKGROUND_SELECTED_COLOR, null, null);
    	
    	
      //  Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	  //  t.show();
    	  
    	Button bAddFBFriends = (Button)findViewById(R.id.bAddFBFriends); 
    	bAddFBFriends.setOnClickListener(this);
     
    	TextView tvSubtitle =(TextView)findViewById(R.id.tvSubtitle);
    	 
    	if (this.game.getNumPlayers() == 1 ){
    		tvSubtitle.setText(this.getString(R.string.choose_opponents_up_to_3_subtitle));
    		this.maxAvailable = 3;
       	}
    	else if(this.game.getNumPlayers() == 2) {
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
    
    	switch(v.getId()){  
	    	case R.id.bAddFBFriends:  
		    	this.addPlayers();
				break;
    	}
    	
    }  
    
    private void addPlayers(){
		
		try 
		{
			if (this.selectedIds.size() == 0){
				DialogManager.SetupAlert(context, getString(R.string.oops), getString(R.string.choose_fb_friends_choose_1), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);
			}
			else if (this.selectedIds.size() > 3){
				DialogManager.SetupAlert(context, getString(R.string.oops), getString(R.string.validation_too_many_players), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);
			}
			else{
				
				for(int i : this.selectedIds){
					FBFriend friend = friends.getFriends().get(selectedIds.get(i));
					Player opponent = new Player();
					opponent.setId(friend.getPlayerId());
					opponent.setFB(friend.getId());
					opponent.setNumWins(friend.getNumWins());
					opponent.setNickname(friend.getName());
					this.game = GameService.addPlayerToGame(this, this.game, opponent);
				}
	
				Intent intent = new Intent(this, com.riotapps.word.AddOpponents.class);
	       	    intent.putExtra(Constants.EXTRA_GAME, this.game);
	    	    this.startActivity(intent);
			}
		} 
		catch (DesignByContractException e) {
			DialogManager.SetupAlert(this, this.getString(R.string.oops), e.getMessage(), true);
		}
	}
    
    private void loadListPrep() {
    	
    	//only do this once a day at the most (or so)
    	
    	String json;
		try {
			
			SharedPreferences settings = context.getSharedPreferences(Constants.USER_PREFS, 0);
			long currentTime = Utils.convertNanosecondsToMilliseconds(System.nanoTime());
			long lastCheckTime = settings.getLong(Constants.USER_PREFS_FRIENDS_LAST_REGISTERED_CHECK_TIME, 0);
			
			if (lastCheckTime == 0 || currentTime - lastCheckTime > Constants.REGISTERED_FB_FRIENDS_CACHE_DURATION){

				json = PlayerService.setupFindPlayersByFB(context);
				//fetch fb friends (which is already stored locally) that are registered with wordsmash already
			//	Logger.d(TAG, "loadListPrep json=" + json);
				
		    	new NetworkTask(context, RequestType.POST, json, getString(R.string.progress_syncing)).execute(Constants.REST_FIND_REGISTERED_FB_FRIENDS);
			}
			else {
				 this.friends = PlayerService.getLocalFBFriends(this.context);
            	 loadList(this.friends.getArray());
			}
		
		} catch (DesignByContractException e) {
			DialogManager.SetupAlert(this.context, getString(R.string.oops), e.getMessage(), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
		}
		
		//if we already have the friends list stored locally just load the list 
		//this.loadList();
    }
    
    private void loadList(FBFriend[] fbFriends){
    	FBFriendArrayAdapter adapter = new FBFriendArrayAdapter(context, fbFriends);
    
    	ListView lvFBFriends = (ListView) findViewById(R.id.lvFBFriends);
    	lvFBFriends.setAdapter(adapter); 
    	
    	lvFBFriends.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
    
            	  RelativeLayout rlItem = (RelativeLayout) view.findViewById(R.id.rlItem);
            	  
      		      Integer pos = new Integer(position);
      		      if(selectedIds.contains(pos)) {
      		           selectedIds.remove(pos);
      		          
      		           rlItem.setBackgroundResource(itemBGColor);
      		           
      		       }
      		       else {
      		    	   if (maxAvailable == 1){
    		        	   //remove others first
      		    		   selectedIds.clear();
      		    		   ((FBFriendArrayAdapter)parent.getAdapter()).notifyDataSetChanged();
    		        	   selectedIds.add(pos);
    		    		   rlItem.setBackgroundResource(itemBGSelectedColor);
    		           }
      		    	   else if (maxAvailable == 2 && selectedIds.size() == maxAvailable){
      		    		   FBFriend friendSelected = friends.getFriends().get(pos);
      		    		   FBFriend friend1 = friends.getFriends().get(selectedIds.get(0));
      		    		   FBFriend friend2 = friends.getFriends().get(selectedIds.get(1));
      		    		   
      		    		   DialogManager.SetupAlert(context, context.getString(R.string.sorry), 
      		    				   String.format(context.getString(R.string.choose_fb_friends_already_chosen_2, 
      		    						   						friendSelected.getAdjustedName(23), 
      		    						   						friend1.getAdjustedName(23),
      		    						   						friend2.getAdjustedName(23))));
      		    	   }
    		    	   else if (maxAvailable == 3 && selectedIds.size() == maxAvailable){
      		    		   FBFriend friendSelected = friends.getFriends().get(pos);
      		    		   FBFriend friend1 = friends.getFriends().get(selectedIds.get(0));
      		    		   FBFriend friend2 = friends.getFriends().get(selectedIds.get(1));
      		    		   FBFriend friend3 = friends.getFriends().get(selectedIds.get(2));
      		    		   
      		    		   DialogManager.SetupAlert(context, context.getString(R.string.sorry), 
      		    				   String.format(context.getString(R.string.choose_fb_friends_already_chosen_3, 
      		    						   						friendSelected.getAdjustedName(23),
      		    						   						friend1.getAdjustedName(23),
      		    						   						friend2.getAdjustedName(23),
      		    						   						friend3.getAdjustedName(23))));
      		    	   }
      		    	   else {
      		    		   selectedIds.add(pos);
      		    		   rlItem.setBackgroundResource(itemBGSelectedColor);
      		    	   }
      		       }
            }
        });

    }
    
    private class FBFriendArrayAdapter extends ArrayAdapter<FBFriend> {
	   	  private final ChooseFBFriends context;
	   	  private final FBFriend[] values;
	   	  LayoutInflater inflater;
	   	//  public ArrayList<Integer> selectedIds = new ArrayList<Integer>();
	
	   	  public FBFriendArrayAdapter(ChooseFBFriends context, FBFriend[] values) {
	   	    super(context, R.layout.choosefbfrienditem, values);
	    	    this.context = context;
	    	    this.values = values;
	    	    
	    	    this.inflater = (LayoutInflater) context
		    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	  }
	
	    	  @Override
	    	  public View getView(int position, View rowView, ViewGroup parent) {
	    		 
	    		  if ( rowView == null ) {
	    			  rowView = inflater.inflate(R.layout.choosefbfrienditem, parent, false);
	    		  }
	    	    
	    	   // View rowView = inflater.inflate(R.layout.choosefbfrienditem, parent, false);
	    		  RelativeLayout rlItem = (RelativeLayout) rowView.findViewById(R.id.rlItem);
	    		  
	    		 // rlItem.setBackgroundColor(selectedIds.contains(position) ? Color.CYAN : android.R.color.transparent);  
	    	    
	    		  rlItem.setBackgroundResource(selectedIds.contains(position) ? itemBGSelectedColor : itemBGColor);
	    		  
		    	  FBFriend friend = values[position];
		    	 
		    	   TextView tvInvitationWillBeSent = (TextView) rowView.findViewById(R.id.tvInvitationWillBeSent);
		    	   TextView tvPlayerName = (TextView) rowView.findViewById(R.id.tvPlayerName);
		    	   tvPlayerName.setText(friend.getAdjustedName(23));
		    	   
		    	   ImageView ivPlayer = (ImageView)rowView.findViewById(R.id.ivPlayer);
		    	   imageLoader.loadImage(friend.getImageUrl(), ivPlayer);  
		    	   
		    	   if (friend.isRegisteredPlayer()){
		    		   int badgeId = getResources().getIdentifier("com.riotapps.word:drawable/" + friend.getBadgeDrawable(), null, null);
		   				ImageView ivBadge = (ImageView)findViewById(R.id.ivBadge);	 
			   			ivBadge.setImageResource(badgeId);
			   			
			   			TextView tvPlayerWins = (TextView)findViewById(R.id.tvPlayerWins);
						tvPlayerWins.setText(String.format(context.getString(R.string.line_item_num_wins),friend.getNumWins())); 
						tvInvitationWillBeSent.setVisibility(View.GONE);
		    	   }
		    	   else{
		    		 
		    	   }
		    	   
		    	   rowView.setTag(friend.getId());
		    	   return rowView;
	    	  }
	    		  
    	//	  @Override
    	//	  public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//
 //   		       ArrayList<Integer> selectedIds = ((FBFriendArrayAdapter) parent).selectedIds;
  //  		       Integer pos = new Integer(position);
   // 		       if(selectedIds.contains(pos) {
   // 		           selectedIds.remove(pos);
   // 		       }
   // 		       else {
   // 		           selectedIds.add(pos);
   // 		       }
///
  //  		       parent.notifyDataChanged();
   // 		  }
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
		            	 
		            	 friends = PlayerService.findRegisteredFBFriendsResponse(this.context, iStream);
		            	 loadList(friends.getArray());
		                 break;  

		             }//end of case 200 & 201 
		             case 401:
			             //case Status code == 422
		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.validation_unauthorized), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
		            	 break;
		             case 404:
		             //case Status code == 422
		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.validation_404_error), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
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
        
