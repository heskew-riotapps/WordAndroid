package com.riotapps.word;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ConnectTimeoutException;

import com.facebook.Session;
import com.riotapps.word.hooks.FBFriend;
import com.riotapps.word.hooks.FBFriends;
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
import com.riotapps.word.utils.NetworkTaskResult;
import com.riotapps.word.utils.Enums.RequestType;
import com.riotapps.word.utils.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseFBFriends extends FragmentActivity implements View.OnClickListener{

	private static final String TAG = ChooseFBFriends.class.getSimpleName();

	private Player player;
	private ChooseFBFriends context = this;
	private Game game;
	private int maxAvailable;
	private ImageFetcher imageLoader;
	private ArrayList<Integer> selectedIds = new ArrayList<Integer>();
	private FBFriends friends;
	private Button bAddFBFriends;
	private EditText etSearch;
	//private FBFriend[] fullArray;
	//private FBFriend[] workingArray;
	
	private List<FBFriend> fullList;
	private List<FBFriend> workingList;
	private List<FBFriend> searchList = new ArrayList<FBFriend>();
	private FBFriendArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosefbfriends);
        
     
        Logger.d(TAG, "ChooseFBFriends onCreate called");
        
        this.imageLoader = new ImageFetcher(this, Constants.DEFAULT_AVATAR_SIZE, Constants.DEFAULT_AVATAR_SIZE, 0);
        this.imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
        
        player = PlayerService.getPlayerFromLocal();
    	PlayerService.loadPlayerInHeader(this);
   
    	Intent i = getIntent();
    	this.game =  (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
    	//this.itemBGColor =  getResources().getIdentifier(Constants.DRAWABLE_LOCATION + Constants.CONTENT_AREA_BACKGROUND_COLOR, null, null);
    	//this.itemBGSelectedColor =  getResources().getIdentifier(Constants.DRAWABLE_LOCATION + Constants.CONTENT_AREA_BACKGROUND_SELECTED_COLOR, null, null);
    	
    	
      //  Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	  //  t.show();
    	  
    	
    	this.bAddFBFriends = (Button)findViewById(R.id.bAddFBFriends); 
    	this.bAddFBFriends.setOnClickListener(this);
    	this.setButton();
    	
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
    		//bAddFBFriends.setText(this.getString(R.string.choose_previous_opponent_add_button_text));
    	}
     
  
    	
    	this.loadListPrep();
    	
    	this.etSearch = (EditText)this.findViewById(R.id.etSearch);
    	 
    	this.etSearch.addTextChangedListener(new EditTextWatcher(this.etSearch));
    	
        //bAddFBFriends.setOnClickListener(this);
           
      
            //in this case ListView was major overkill
          //  LinearLayout llPlayers = (LinearLayout)findViewById(R.id.llPlayers);
            
          //  for (Player p : this.game.getOpponents(player)){
          //  	llPlayers.addView(getView(p));
		//	}

    }
    private class EditTextWatcher implements TextWatcher {
        private EditText editText;

        public EditTextWatcher(EditText e) { 
        	editText = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
        	//Toast.makeText(context, editText.getText(), Toast.LENGTH_LONG).show();
        	//context.fullArray = context.friends.getFriends(). 
        	
        	String term = editText.getText().toString().trim();
             
//       	 Logger.d(TAG, "term=" + term);
             if (term.length() > 0)
             { 
            	 context.workingList = new ArrayList<FBFriend>();
            	// Logger.d(TAG, "context.fullList=" + context.fullList.size() );
            	//context.searchList.clear();
           	 //Logger.d(TAG, "context.fullList=" + context.fullList.size() );

                for (FBFriend f : context.fullList) {
                    if (f.nameStartsWith(term)) {
                    	context.workingList.add(f);
                    }
                 }
             }
             else {
            	// Logger.d(TAG, "term.length=0 context.fullList=" + context.fullList.size() );
            	 context.loadWorkingListFromFull();
             }
             
           //  Logger.d(TAG, "afterTextChanged matches=" + context.workingList.size());
   	             
        	//context.adapter.clear();
        	//context.adapter.addAll(context.workingList);

        	// notify the list that the underlying model has changed
            context.adapter.updateList(context.workingList); 
        	context.adapter.notifyDataSetChanged();
        }
    }
    
    private void loadWorkingListFromFull(){
     
    	this.workingList = new ArrayList<FBFriend>();
    	for (FBFriend fb : this.fullList){
    		this.workingList.add(fb);
    	}
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
    	  super.onActivityResult(requestCode, resultCode, data);
          Logger.d(TAG, "onActivityResult called");
          //Session session = Session.getActiveSession();
          try{ 
          	Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
          }
          catch(Exception e){
          	Logger.d(TAG, "onActivityResult error=" + e.getMessage());
          }
	}


	@Override 
    public void onClick(View v) {
    
    	switch(v.getId()){  
	    	case R.id.bAddFBFriends:  
		    	this.addPlayers();
				break;
    	}
    	
    }  
    private void setButton(){
    	if (this.selectedIds.size() == 0){
    		this.bAddFBFriends.setVisibility(View.GONE);
    	}
    	else {
    		if (this.selectedIds.size() == 1){
    			FBFriend friend = friends.getFriends().get(selectedIds.get(0));
    			this.bAddFBFriends.setText(String.format(this.getString(R.string.choose_1_fb_friend_add_button_text), friend.getFirstName()));
    		}
    		else if (this.selectedIds.size() == 2){
    			FBFriend friend1 = friends.getFriends().get(selectedIds.get(0));
    			FBFriend friend2 = friends.getFriends().get(selectedIds.get(1));
    			this.bAddFBFriends.setText(String.format(this.getString(R.string.choose_2_fb_friends_add_button_text), friend1.getFirstName(), friend2.getFirstName()));
    		}
    		else {
    			FBFriend friend1 = friends.getFriends().get(selectedIds.get(0));
    			FBFriend friend2 = friends.getFriends().get(selectedIds.get(1));
    			FBFriend friend3 = friends.getFriends().get(selectedIds.get(2));

    			this.bAddFBFriends.setText(String.format(this.getString(R.string.choose_3_fb_friends_add_button_text), friend1.getFirstName(), friend2.getFirstName(), friend3.getFirstName()));
			}
	    	this.bAddFBFriends.setVisibility(View.VISIBLE);
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

				for(int i = 0; i < this.selectedIds.size(); i++){
					FBFriend friend = friends.getFriends().get(selectedIds.get(i));
					Logger.d(TAG, "addPlayers friend.getPlayerId()=" + friend.getPlayerId() );
					Player opponent = new Player();
					opponent.setId(friend.getPlayerId());
					opponent.setFB(friend.getId());
					opponent.setNumWins(friend.getNumWins());
					opponent.setFirstName(friend.getName());
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

			SharedPreferences settings = context.getSharedPreferences(Constants.USER_PREFS, Context.MODE_MULTI_PROCESS);
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
				 this.fullList = this.friends.getFriends();
				 this.loadWorkingListFromFull();
				 loadList();
            	 
            	 Logger.d(TAG, "loadListPrep numFriends=" + this.friends.getFriends().size());
             	//Toast.makeText(context, this.friends.getArray().length, Toast.LENGTH_LONG).show();

			}

		} catch (DesignByContractException e) {
			DialogManager.SetupAlert(this.context, getString(R.string.oops), e.getMessage(), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
		}

		//if we already have the friends list stored locally just load the list 
		//this.loadList();
    }
    
    private void loadList(){
    	//this.fullArray = fbFriends;
    	
    	
    	this.adapter = new FBFriendArrayAdapter(context, this.workingList); //bFriends);
    
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
      		          
      		           rlItem.setBackgroundColor(Color.parseColor(context.getString(R.color.content_area_background_color)));
      		          // rlItem.setBackgroundResource(itemBGColor);
      		           
      		       }
      		       else {
      		    	   if (maxAvailable == 1){
    		        	   //remove others first
      		    		   selectedIds.clear();
      		    		   ((FBFriendArrayAdapter)parent.getAdapter()).notifyDataSetChanged();
    		        	   selectedIds.add(pos);
    		        	   rlItem.setBackgroundColor(Color.parseColor(context.getString(R.color.content_area_background_selected_color)));
    		    		  // rlItem.setBackgroundResource(itemBGSelectedColor);
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
      		    		   rlItem.setBackgroundColor(Color.parseColor(context.getString(R.color.content_area_background_selected_color)));
      		    		 //  rlItem.setBackgroundResource(itemBGSelectedColor);
      		    	   }
      		    	
      		       }
      		    setButton();
            }
        });

    }
    
    private class FBFriendArrayAdapter extends ArrayAdapter<FBFriend> {
	   	  private final ChooseFBFriends context;
	   	 // private final FBFriend[] values;
	   	  private List<FBFriend> values;
	   	  LayoutInflater inflater;
	   	//  public ArrayList<Integer> selectedIds = new ArrayList<Integer>();

	   	  public FBFriendArrayAdapter(ChooseFBFriends context, List<FBFriend> friends){ // FBFriend[] values) {
	   	    super(context, R.layout.choosefbfrienditem, friends);
	    	    this.context = context;
	    	    this.values = friends;

	    	    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	  }

	   	     @SuppressLint("NewApi")
			public void updateList(List<FBFriend> friends){ 
		   	    	Logger.d(TAG, "adapter before friends=" + friends.size());
	   	    	 this.values = friends;
		   	    	Logger.d(TAG, "adapter after friends=" + friends.size());
		   	    	Logger.d(TAG, "adapter before super.getCount()" + super.getCount());
	   	    	 super.clear();
	   	    	 
	   	    	 //add all not available until api 11
	   	    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	   	    		super.addAll(friends);
	   	    	} 
	   	    	else {
	   	    	 
		   	    	for (FBFriend f : friends) {
		   	    		super.add(f);
		   	    	}
	   	    	}
	   	    	Logger.d(TAG, "adapter after super.getCount()" + super.getCount());
	   	     }
	   	  
	    	  @Override
	    	  public View getView(int position, View rowView, ViewGroup parent) {

	    		  if ( rowView == null ) {
	    			  rowView = inflater.inflate(R.layout.choosefbfrienditem, parent, false);
	    		  }

	    	   // View rowView = inflater.inflate(R.layout.choosefbfrienditem, parent, false);
	    		  RelativeLayout rlItem = (RelativeLayout) rowView.findViewById(R.id.rlItem);

	    		   rlItem.setBackgroundColor(selectedIds.contains(position) ? Color.parseColor(context.getString(R.color.content_area_background_selected_color)) : Color.parseColor(context.getString(R.color.content_area_background_color)));  

	    		 // rlItem.setBackgroundResource(selectedIds.contains(position) ? itemBGSelectedColor : itemBGColor);

		    	  FBFriend friend = values.get(position);// values[position];

		    	   TextView tvInvitationWillBeSent = (TextView) rowView.findViewById(R.id.tvInvitationWillBeSent);
		    	   TextView tvPlayerName = (TextView) rowView.findViewById(R.id.tvPlayerName);
		    	   tvPlayerName.setText(friend.getAdjustedName(23));
		    	   ImageView ivBadge = (ImageView)rowView.findViewById(R.id.ivPlayerBadge);
		    	   TextView tvPlayerWins = (TextView)rowView.findViewById(R.id.tvPlayerWins);

		    	   ImageView ivPlayer = (ImageView)rowView.findViewById(R.id.ivPlayer);
		    	   imageLoader.loadImage(friend.getImageUrl(), ivPlayer);  


		    	   if (friend.isRegisteredPlayer()){
		    		   int badgeId = getResources().getIdentifier("com.riotapps.word:drawable/" + friend.getBadgeDrawable(), null, null);



		   				//Logger.d(TAG, "FBFriendArrayAdapter friend drawable=" + friend.getName() + " " + friend.getBadgeDrawable() + " badge=" + badgeId + " " + (ivBadge == null));

			   			ivBadge.setImageResource(badgeId);


						if (friend.getNumWins() == 1){
							tvPlayerWins.setText(context.getString(R.string.line_item_1_win)); 
						}
						else if (friend.getNumWins() == -1){
							tvPlayerWins.setText(context.getString(R.string.line_item_invited)); 
						}
						else{
							tvPlayerWins.setText(String.format(context.getString(R.string.line_item_num_wins),friend.getNumWins())); 
						}

						tvInvitationWillBeSent.setVisibility(View.GONE);
						ivBadge.setVisibility(View.VISIBLE);
						tvPlayerWins.setVisibility(View.VISIBLE);

		    	   }
		    	   else{
		    		   tvInvitationWillBeSent.setVisibility(View.VISIBLE);
						ivBadge.setVisibility(View.GONE);
						tvPlayerWins.setVisibility(View.GONE);
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
		protected void onPostExecute(NetworkTaskResult result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			this.handleResponse(result);


		}
 
		private void handleResponse(NetworkTaskResult result){  
		     Exception exception = result.getException();   

		     if(result.getResult() != null){  

		         switch(result.getStatusCode()){  
		             case 200:  
		             case 201: {

		            	// Logger.d(TAG, "handleResponse result=" + result.getResult());

		            	 friends = PlayerService.findRegisteredFBFriendsResponse(this.context, result.getResult());
		            	 fullList = friends.getFriends();
		            	 loadWorkingListFromFull();
		            	 loadList();
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

		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), result.getStatusCode() + " " + result.getStatusReason(), 0);  
		            	 break;
		         }  
		     }else if (exception instanceof ConnectTimeoutException ||  exception instanceof java.net.SocketTimeoutException) {
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
   