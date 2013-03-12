package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import com.google.gson.Gson;
import com.riotapps.word.hooks.Chat;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.PlayedWord;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.ui.GameAction.GameActionType;
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
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class GameChat extends FragmentActivity implements  View.OnClickListener{
	private static final String TAG = GameChat.class.getSimpleName();
	private Game game;
	private Player player;
	EditText etText;
	NetworkTask runningTask = null;
	private Context context;
	private ImageFetcher imageLoader;
	private String chatPlayer1Id = "";
	private String chatPlayer2Id = "";
	private String chatPlayer3Id = "";
	private String chatPlayer4Id = "";
	private String prevPlayerId = "";
	private String prevTimeSince = "";
	
	private boolean isGameUpdated = false; 
	
	//private ChatArrayAdapter adapter = null;
	
//	ListView lvChat;
	ScrollView scrChat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamechat);
		this.context = this;
		
		
	 	Intent i = getIntent();
	 	String gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
	 	this.game = GameService.getGameFromLocal(gameId); 
		
	    this.player = PlayerService.getPlayerFromLocal(); 
	 	GameService.loadScoreboard(this, this.game, this.player);
	 	
	 	Button bSave = (Button) findViewById(R.id.bSave);
	 	bSave.setOnClickListener(this);
	 	this.etText = (EditText) findViewById(R.id.etText);
	 	
	 	this.imageLoader = new ImageFetcher(this, Constants.DEFAULT_AVATAR_SIZE, Constants.DEFAULT_AVATAR_SIZE, 0);
	    this.imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
	 	
	 	this.loadLayout();
	 	this.checkGameStatus();
	}

	 
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	//	if (!this.isGameUpdated){
	//	super.onBackPressed();
	//	Logger.d(TAG, "onBackPressed");
	//	}
	//	else {
			//during this chat view we have added a chat and pulled the game into local
			//set this extra so that the game surface will be updated properly
			Intent intent = new Intent(this.context,  GameSurface.class);
			intent.putExtra(Constants.EXTRA_IS_GAME_UPDATED, this.isGameUpdated);
			//this.context.startActivity(intent);
			this.setResult(Activity.RESULT_OK, intent);
			 this.finish();
	//	}
	    
	}
 



	private void loadLayout(){
		scrChat = (ScrollView) findViewById(R.id.scrChat);
		LinearLayout llChat = (LinearLayout) findViewById(R.id.llChat);
		llChat.removeAllViews();
		this.chatPlayer1Id = "";
		this.chatPlayer2Id = "";
		this.chatPlayer3Id = "";
		this.chatPlayer4Id = "";
		this.prevPlayerId = "";
		this.prevTimeSince = "";
		
		if (this.game.getChats().size() > 0){
  
	        for (Chat c : this.game.getChats()){
	        	llChat.addView(getChatView(c));
			}
	        llChat.setVisibility(View.VISIBLE);
    	}
		else{
			llChat.setVisibility(View.INVISIBLE);
		}
		
		scrChat.post(new Runnable(){
			  public void run() {
				  scrChat.fullScroll(View.FOCUS_DOWN);
			  }});
		//scrChat.fullScroll(View.FOCUS_DOWN);
	}
	
	 private void checkGameStatus(){
		 if (this.game.getStatus() == 3 || this.game.getStatus() == 4){
			 long diff = System.currentTimeMillis() - this.game.getCompletionDate().getTime();
			 long diffMinutes = (long)diff/60000;
			 
			 if (diffMinutes > 30){
				 EditText etText = (EditText) this.findViewById(R.id.etText);
				 etText.setEnabled(false);
				 
				 Button bSave = (Button) this.findViewById(R.id.bSave);
				 bSave.setEnabled(false);
				 bSave.setTextColor(Color.parseColor(this.getString(R.color.button_text_color_off)));
			 }
		 }
	 }

	public View getChatView(Chat chat) {
		View view = LayoutInflater.from(this).inflate(R.layout.gamechatitem, null);
		  
		  //is chatPlayerId already assigned? if not assign him the next slot/color
		  if (!chatPlayer1Id.equals(chat.getPlayerId()) && 
				  !chatPlayer2Id.equals(chat.getPlayerId()) && 
				  !chatPlayer3Id.equals(chat.getPlayerId()) && 
				  !chatPlayer4Id.equals(chat.getPlayerId())){
			  if (chatPlayer1Id == ""){
	    		  chatPlayer1Id = chat.getPlayerId(); 
	    	  }
	    	  else if (chatPlayer2Id == ""){
	    		  chatPlayer2Id = chat.getPlayerId(); 
	    	  }
	    	  else if (chatPlayer3Id == ""){
	    		  chatPlayer3Id = chat.getPlayerId(); 
	    	  }
	    	  else {
	    		  chatPlayer4Id = chat.getPlayerId(); 
	    	  }
		  }
		  
    	   TextView tvChat = (TextView) view.findViewById(R.id.tvChat);
    	   TextView tvPlayerName = (TextView) view.findViewById(R.id.tvPlayerName);
    	   TextView tvChatDate = (TextView) view.findViewById(R.id.tvChatDate);
    	   
    	   if (chat.getPlayerId().equals(chatPlayer1Id)){
    		   tvChat.setBackgroundResource(R.drawable.chat_player1_background);
    	   }
    	   else if (chat.getPlayerId().equals(chatPlayer2Id)){
    		   tvChat.setBackgroundResource(R.drawable.chat_player2_background);
    	   }
    	   else if (chat.getPlayerId().equals(chatPlayer3Id)){
    		   tvChat.setBackgroundResource(R.drawable.chat_player3_background);
    	   }
    	   else {
    		   tvChat.setBackgroundResource(R.drawable.chat_player4_background);
    	   }
 
    	   Player chatPlayer = this.game.getPlayerById(chat.getPlayerId());
		   ImageView ivPlayer = (ImageView)view.findViewById(R.id.ivPlayer);
	//	   imageLoader.loadImage(chatPlayer.getImageUrl(), ivPlayer);
		   

    	   tvChat.setText(chat.getText());
		   String timeSince = Utils.getTimeSinceString(context, chat.getChatDate());
		   tvPlayerName.setText(chatPlayer.getAbbreviatedName());
    	   tvChatDate.setText(timeSince);
    	   
		//   Logger.d(TAG, "ChatArrayAdapter prevPlayerId=" + prevPlayerId + " prevSequentialCount=" + prevSequentialCount);
    	   if (!this.prevPlayerId.equals(chat.getPlayerId())){// || (this.prevPlayerId == chat.getPlayerId() && this.prevSequentialCount >= 4) ){
    		 //  this.prevSequentialCount = 0;
    		   this.prevPlayerId = chat.getPlayerId();
    		   imageLoader.loadImage(chatPlayer.getImageUrl(), ivPlayer);
    		   ivPlayer.setVisibility(View.VISIBLE);
    		   
        	   tvPlayerName.setVisibility(View.VISIBLE);
        	   tvChatDate.setVisibility(View.VISIBLE);        	   
    		   
    	   }
    	   else{ 
    		   ivPlayer.setVisibility(View.INVISIBLE);
    		   
    		   //if timeSince is the same as the previous chat item, hide this line.  its just creates clutter by showing it
    		   if (this.prevTimeSince.equals(timeSince)){
    			   tvPlayerName.setVisibility(View.GONE);
    			   tvChatDate.setVisibility(View.GONE);
    		   }
    		   else{
    			   tvPlayerName.setVisibility(View.VISIBLE);
    			   tvChatDate.setVisibility(View.VISIBLE);    			   
    		   }
        	   
    	   }
    	   
    	   this.prevTimeSince = timeSince;
		   
		   
    	   return view;
		
	}
	 
	private class ChatArrayAdapter extends ArrayAdapter<Chat> {
	   	  private final GameChat context;
	   	  private Chat[] values;
	   	  private String prevPlayerId = "";
	   	  private int prevSequentialCount = 0;
	   	  LayoutInflater inflater;
	   	//  public ArrayList<Integer> selectedIds = new ArrayList<Integer>();

	   	  public void resetControls(){
	   		  this.prevPlayerId = "";
	   		  this.prevSequentialCount = 0;
	   	  }
	   	  
	   	  public void reloadList(Chat[] values){
	   		  this.values = values;
	   		  this.resetControls();
	   	  }
	   	  
	   	  public ChatArrayAdapter(GameChat context, Chat[] values) {
	   	    super(context, R.layout.gamechatitem, values);
	    	    this.context = context;
	    	    this.values = values;
 	    
	    	    this.inflater = (LayoutInflater) context
		    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	  }

	    	  @Override
	    	  public View getView(int position, View rowView, ViewGroup parent) {
	    		 
	    		//  if ( rowView == null ) {
	    			  rowView = inflater.inflate(R.layout.gamechatitem, parent, false);
	    		//  }
	    		  
		    	  Chat chat = values[position];
		    	 
		    	  //is chatPlayerId already assigned? if not assign him the next slot/color
				  if (chatPlayer1Id != chat.getPlayerId() && 
						  chatPlayer2Id != chat.getPlayerId() && 
						  chatPlayer3Id != chat.getPlayerId() && 
						  chatPlayer4Id != chat.getPlayerId()){
			    	  if (chatPlayer1Id == ""){
			    		  chatPlayer1Id = chat.getPlayerId(); 
			    	  }
			    	  else if (chatPlayer2Id == ""){
			    		  chatPlayer2Id = chat.getPlayerId(); 
			    	  }
			    	  else if (chatPlayer3Id == ""){
			    		  chatPlayer3Id = chat.getPlayerId(); 
			    	  }
			    	  else {
			    		  chatPlayer4Id = chat.getPlayerId(); 
			    	  }
				  }
		    	  
		    	   TextView tvChat = (TextView) rowView.findViewById(R.id.tvChat);
		    	   
		    	   if (chat.getPlayerId() == chatPlayer1Id){
		    		   tvChat.setBackgroundResource(R.drawable.chat_player1_background);
		    	   }
		    	   else if (chat.getPlayerId() == chatPlayer2Id){
		    		   tvChat.setBackgroundResource(R.drawable.chat_player2_background);
		    	   }
		    	   else if (chat.getPlayerId() == chatPlayer3Id){
		    		   tvChat.setBackgroundResource(R.drawable.chat_player3_background);
		    	   }
		    	   else {
		    		   tvChat.setBackgroundResource(R.drawable.chat_player4_background);
		    	   }
		    	   //game.get
		    	   

	    		   ImageView ivPlayer = (ImageView)rowView.findViewById(R.id.ivPlayer);
	    		   imageLoader.loadImage(player.getImageUrl(), ivPlayer);
	    		   
		    	   tvChat.setText(chat.getText());

		    	   return rowView;
	    	  } 
	} 	
	 
	
	@Override
	public void onClick(View v) {

    	switch(v.getId()){  
	        case R.id.bSave:  
	        	saveChat();
				break;
    	}
		
	}
	
    private void saveChat(){	
    	
		try { 
			if ( this.etText.getText().toString().length() > 0) {
		
				String json = GameService.setupGameChat(this.game, etText.getText().toString());
				
				//kick off thread to cancel game on server
				runningTask = new NetworkTask(this, RequestType.POST, json,  getString(R.string.progress_sending));
				runningTask.execute(Constants.REST_GAME_CHAT);
			}
		} catch (DesignByContractException e) {
			 
			DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
		}
    }
    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPause called");
		super.onPause();
		if (this.runningTask != null){
    		this.runningTask.cancel(true);
    	}
	}
	  
    private class NetworkTask extends AsyncNetworkRequest{
			
	    	GameChat context;
	     		
    		public NetworkTask(GameChat ctx, RequestType requestType,
    				String json,
    				String shownOnProgressDialog) {
    			super(GameChat.this, requestType, shownOnProgressDialog, json);
    			this.context = ctx;
    		 
    		}

    		@Override
    		protected void onPostExecute(NetworkTaskResult result) {
    		 
    			super.onPostExecute(result);
    			
    			this.handleResponse(result);

    		}
     
    		private void handleResponse(NetworkTaskResult result){
 
    		     Exception exception = result.getException();

    		     if(result.getResult() != null){  
   		  
    		         switch(result.getStatusCode()){  
    		             case 200:  
    		             case 201:   
		            	   
    		            	 //Logger.d(TAG, "result=" + result.getResult());
		            	    isGameUpdated = true;
	            	 		//refresh game board
	            	 		game = GameService.handleGameChatResponse(context, result.getResult());
	            	 		
	            	 		//refresh the list
	            			etText.setText("");
	            			
	            			loadLayout();
	            			GameService.checkGameChatAlert(context, game, true);
	            	 		
	            	 		break;
	            	 		//end of case 200 & 201 
    		           
    		             case 401:
    			             //case Status code == 422
    			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.validation_unauthorized));  
    			            	 break;
    		             case 404:
    		             //case Status code == 422
    		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.find_player_opponent_not_found), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
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
    		         Log.d("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

    		     }//end of else  
    		}
    		
     
    	}


}
