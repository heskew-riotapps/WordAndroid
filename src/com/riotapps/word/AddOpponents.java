package com.riotapps.word;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookActivity;
import com.facebook.LoggingBehaviors;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.Session.StatusCallback;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
 
import com.facebook.FacebookException;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
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

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class AddOpponents extends FacebookActivity implements View.OnClickListener{
	
	private static final String TAG = AddOpponents.class.getSimpleName();
	TextView tvStartByNickname;
	Player player;
	AddOpponents context = this;
	Game game;
 
	private SharedPreferences settings;
	private Session session;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addopponents);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);

        Logger.d(TAG, "AddOpponents onCreate called");
        player = PlayerService.getPlayerFromLocal();
    	PlayerService.loadPlayerInHeader(this);
   
    	Intent i = getIntent();
    	this.game =  (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
    	settings = this.getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE); //getPreferences(MO
 
    	Button bStartGame = (Button)findViewById(R.id.bStartGame); 
    	Button bCancelGame = (Button)findViewById(R.id.bCancelGame); 
    	TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
    	TextView tvSubtitle =(TextView)findViewById(R.id.tvSubtitle);
    	TextView tvOpponentsAlreadyAddedTitle =(TextView)findViewById(R.id.tvOpponentsAlreadyAddedTitle);
    	if (this.game.getPlayerGames().size() < 3){
    		tvTitle.setText(this.getString(R.string.add_opponents_add_x_more_title));
    		tvSubtitle.setText(String.format(this.getString(R.string.add_opponents_add_x_more_subtitle), 3 - this.game.getPlayerGameOpponentsArray().length));
       	}
    	else if(this.game.getPlayerGames().size() == 4) {
    		RelativeLayout rlAddOpponents = (RelativeLayout)findViewById(R.id.rlAddOpponents);
    		rlAddOpponents.setVisibility(View.GONE);
    	}
    	else {
    		tvTitle.setText(this.getString(R.string.add_opponents_add_one_more_title));
    		tvSubtitle.setText(this.getString(R.string.add_opponents_add_one_more_subtitle));
    	}
    	if (this.game.getPlayerGames().size() > 2){
    		tvOpponentsAlreadyAddedTitle.setText(this.getString(R.string.add_opponents_already_added_x_title)); 		    
    	}
    	else{
    		tvOpponentsAlreadyAddedTitle.setText(this.getString(R.string.add_opponents_already_added_one_title));
    	}
    	  
    	   TextView tvFB =(TextView)findViewById(R.id.tvStartByFacebook);
          // TextView tvRandom =(TextView)findViewById(R.id.tvStartByRandom);
           
        TextView tvByOpponent =(TextView)findViewById(R.id.tvStartByOpponent);
        tvStartByNickname =(TextView)findViewById(R.id.tvStartByNickname);
        tvStartByNickname.setOnClickListener(this);
        bStartGame.setOnClickListener(this);
        bCancelGame.setOnClickListener(this);
           
         
            //not ready for this option
        //    tvRandom.setVisibility(View.GONE);
           //if not in by facebook, hide this option
            if (!player.isFacebookUser()){
           	 tvFB.setVisibility(View.GONE);
            }
            else{
           	 tvFB.setOnClickListener(this);
            }
            
            if (this.player.getOpponents().size() == 0){
           	 tvByOpponent.setVisibility(View.GONE);
            }
            else {
           	 tvByOpponent.setOnClickListener(this);
            }
            
            //in this case ListView was major overkill
            LinearLayout llPlayers = (LinearLayout)findViewById(R.id.llPlayers);
            
            for (Player p : this.game.getOpponents(player)){
            	llPlayers.addView(getView(p));
			}

    }
    
    public View getView(Player opponent ) {
  		View view = LayoutInflater.from(this).inflate(R.layout.playerlistitem, null);
  
  	    TextView tvPlayerName = (TextView)view.findViewById(R.id.tvPlayerName);
	 	tvPlayerName.setText(opponent.getNameWithMaxLength(28));

		TextView tvPlayerWins = (TextView)view.findViewById(R.id.tvPlayerWins);
		tvPlayerWins.setText(String.format(this.context.getString(R.string.line_item_num_wins),opponent.getNumWins()));
	 	
	 	ImageFetcher imageLoader = new ImageFetcher(this.context, 34, 34, 0);
		imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
		ImageView ivPlayer = (ImageView) view.findViewById(R.id.ivPlayer);
		
		imageLoader.loadImage(opponent.getImageUrl(), ivPlayer); //default image
		
		int badgeId = getResources().getIdentifier(Constants.DRAWABLE_LOCATION + opponent.getBadgeDrawable(), null, null);
		ImageView ivBadge = (ImageView)view.findViewById(R.id.ivBadge);	 
		ivBadge.setImageResource(badgeId);
	 
  	    return view;
  	}
    
    @Override 
    public void onClick(View v) { 
    	Intent intent;
    	switch(v.getId()){  
        case R.id.tvStartByNickname:  
          	intent = new Intent(this.context, FindPlayer.class);
          	intent.putExtra(Constants.EXTRA_GAME, this.game);
			this.context.startActivity(intent);
			break;
        case R.id.tvStartByFacebook:  
        	intent = new Intent(this.context, ChooseFBFriends.class);
        	intent.putExtra(Constants.EXTRA_GAME, this.game);
			this.context.startActivity(intent);
			break;
	    case R.id.tvStartByOpponent:  
	    	intent = new Intent(this.context, FindPlayer.class);
			intent.putExtra(Constants.EXTRA_GAME, this.game);
			this.context.startActivity(intent);
			break;
    
	    case R.id.bStartGame:  
    		//don't forget to sign up for google web services notifications!!!!!! 
			Logger.d(TAG, "bStartGame clicked");
			this.handleGameStart();
		
			break;
	    case R.id.bCancelGame:  
	    	this.handleCancel();
			break;
    	}
    	
    }  
    
    private void handleGameStart(){
    	//don't forget to sign up for google web services notifications!!!!!! 
    	
    	//1. determine if any new facebook players have been added to this game
    	//2. if so, call fb's apprequest dialog to send apprequest informing new players of 
    	//   their invitation
    	//3. if the app request dialog is canceled, assume the game will be canceled since there will 
    	//   generally be no way for new fb player to be informed of their invitation to play (outside of word-of-mouth_
    	//4. during the apprequest dialog process, if the fb session token has to be renewed and a different fb user
    	//   other than the content player fb user is authorized by fb, kill local cache, log user out and 
    	//   send player to welcome screen to start over.  this should be a very rare occurrence
    	//5. if apprequest is completed successfully (or there are no new fb users), start the game
    	
    	if (this.game.getUnregisteredFBPlayers().size() > 0){
    		this.handleFacebookInvitationAppRequests();
    	}
    	else{
    		this.startGame();
    	}
 	
    }
    
    private void startGame(){
    	//don't forget to sign up for google web services notifications!!!!!! 
		String json;
		try {
			json = GameService.setupStartGame(context, this.game);
			
			//kick off thread
		    new NetworkTask(context, RequestType.POST, json, getString(R.string.progress_starting_game)).execute(Constants.REST_CREATE_GAME_URL);
		
		} catch (DesignByContractException e) {
			//this should be rare also, but if it occurs, show error and cancel the game (by finalizing activity)
			DialogManager.SetupAlert(context, this.getString(R.string.oops), e.getLocalizedMessage(), true);
		}
		
    }
    
    
    private void handleAppRequestCancel(){
    	String message;
    	
    	Logger.d(TAG, "handleAppRequestCancel fired");
    	if (this.game.getUnregisteredFBPlayers().size() == 1){
    		message = String.format(this.getString(R.string.add_opponents_cancel_app_request_1_message), 
    				this.game.getUnregisteredFBPlayers().get(0).getShortName());
    	}
    	else if(this.game.getUnregisteredFBPlayers().size() == 2){
    		message = String.format(this.getString(R.string.add_opponents_cancel_app_request_2_message), 
    				this.game.getUnregisteredFBPlayers().get(0).getShortName(),
    				this.game.getUnregisteredFBPlayers().get(1).getShortName());
    	}
    	else {
    		message = String.format(this.getString(R.string.add_opponents_cancel_app_request_3_message), 
    				this.game.getUnregisteredFBPlayers().get(0).getShortName(),
    				this.game.getUnregisteredFBPlayers().get(1).getShortName(),
    				this.game.getUnregisteredFBPlayers().get(2).getShortName());
    	}
    	
    	
    	final CustomDialog dialog = new CustomDialog(this, 
    			this.getString(R.string.dialog_title_are_you_sure), 
    			message);
    	
    	dialog.setOnOKClickListener(new View.OnClickListener() {
	 		@Override
			public void onClick(View v) {
	 			dialog.dismiss(); 
	 			context.startGame();
	 		}
		});

    	dialog.show();	
    }
    
    private void handleCancel(){
    	final CustomDialog dialog = new CustomDialog(this, 
    			this.getString(R.string.add_opponents_cancel_game_confirmation_title), 
    			this.getString(R.string.add_opponents_cancel_game_confirmation_text),
    			this.getString(R.string.yes),
    			this.getString(R.string.no));
    	
    	dialog.setOnOKClickListener(new View.OnClickListener() {
	 		@Override
			public void onClick(View v) {
	 			dialog.dismiss(); 
	 			Intent intent = new Intent(context, StartGame.class);
				context.startActivity(intent);
	 		}
		});

    	dialog.show();	
    }
    
  //  private void handleResponseFromIOThread(Game game){
 //  	 Game game = GameService.handleCreateGameResponse(this.context, iStream);

   // 	Intent intent = new Intent(this.context, com.riotapps.word.GameSurface.class);
	 
    	//Logger.d(TAG, "handleResponseFromIOThread game about to be added as extra");
   // 	intent.putExtra(Constants.EXTRA_GAME, game);
	     
    	 //Logger.d(TAG, "handleResponseFromIOThread game added as extra");
   // 	 this.context.startActivity(intent);
   // }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.session.onActivityResult(this, requestCode, resultCode, data);
    }
    
    private Session createSession() {
        return new Session.Builder(this).setApplicationId(this.getString(R.string.app_id)).build();
    }
    
    //if player is invited non-registered facebook friends, use facebook app requests to inform those friends.
    //it's the only way to inform them since we don't have their email address
    private void handleFacebookInvitationAppRequests() {
    	 this.session = createSession();
    	 Settings.addLoggingBehavior(LoggingBehaviors.INCLUDE_ACCESS_TOKENS);
        
    	 if (this.session.isOpened()) {
    		 handleFacebookAppRequest();
         } else {
             StatusCallback callback = new StatusCallback() {
                 public void call(Session session, SessionState state, Exception exception) {
                     if (state.isOpened()) {
                    	 handleFacebookAppRequest();
                     } else if (exception != null) {
                    	 DialogManager.SetupAlert(context, context.getString(R.string.sorry), exception.getMessage());
                         AddOpponents.this.session = createSession();
                     }
                 }
             };
             this.session.openForRead(new Session.OpenRequest(this).setCallback(callback));
         }
    }
    
    
    
 /*   private void handleFacebookAppRequest(){
    	
    	Logger.d(TAG, "handleFacebookAppRequest this.game.getInvitedFBPlayersString()=" + this.game.getUnregisteredFBPlayersString());
    	Bundle params = new Bundle();
	    params.putString("message", this.getString(R.string.add_opponents_fb_dialog_message));
 
	    params.putString("to", this.game.getUnregisteredFBPlayersString());
	    facebook.dialog(context, "apprequests", params, new fbAppRequestsListener());

    }
   
    private void startFacebookAppRequestCallChain(){
    	Logger.w(TAG, "handleFacebookMeRequest");
   	 	SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.FB_TOKEN, facebook.getAccessToken());
        editor.putLong(Constants.FB_TOKEN_EXPIRES, facebook.getAccessExpires());
        editor.commit();
     
        // get information about the currently logged in user
        mAsyncRunner.request("me", new fbMeRequestListener());
    	
    }
    */
  //  private class fbAppRequestsListener implements DialogListener {
//
//		@Override
//		public void onComplete(Bundle values) {
//			// TODO Auto-generated method stub
//			Logger.d(TAG, "fbAppRequestsListener.onComplete");
//			context.startGame();
//		}

//		@Override
//		public void onFacebookError(FacebookError e) {
//			// TODO Auto-generated method stub
//			DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());
//			Logger.e(TAG, "fbAppRequestsListener onFacebookError=" + e.getLocalizedMessage());			
//		}

//		@Override
//		public void onError(DialogError e) {
//			Logger.e(TAG, "fbAppRequestsListener onError=" + e.getLocalizedMessage());
//			DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());
			
//		}

//		@Override
//		public void onCancel() {
//			// TODO Auto-generated method stub
//			
//			/////call custom dialog here to allow player to make the decision if the player cancels the apprequest
//			
//			context.handleAppRequestCancel();
			//DialogManager.SetupAlert(context, context.getString(R.string.sorry), )
//		}

 //   }
    
/*    private class fbMeRequestListener implements RequestListener {

    	@Override
    	public void onComplete(String response, Object state) {
            
    		// get the logged-in user's friends
    		//save user to server...
    		Logger.d(TAG, "fbMeRequestListener.onComplete response=" + response);
    		
    		context.runOnUiThread(new handleFacebookMeResponseRunnable(response));
		
     	}

    	@Override
    	public void onIOException(IOException e, Object state) {
    		// TODO Auto-generated method stub

    	}

    	@Override
    	public void onFileNotFoundException(FileNotFoundException e, Object state) {
    		// TODO Auto-generated method stub

    	}

    	@Override
    	public void onMalformedURLException(MalformedURLException e, Object state) {
    		// TODO Auto-generated method stub

    	}

    	@Override
    	public void onFacebookError(FacebookError e, Object state) {
    		// TODO Auto-generated method stub

    	}

    }
   
    private class handleFacebookMeResponseRunnable implements Runnable {
		 private String response;	
		 
		 public handleFacebookMeResponseRunnable(String response){
		 		this.response = response;
		 	}
		 
		 
		    public void run() {
		    	Logger.w(TAG, "handleFacebookMeResponse");
		    	String fbId; 
				String fbFirstName; 
				String fbLastName; 
				String fbEmail; 
				try {
					JSONObject json_fb = Util.parseJson(this.response);
					fbId = json_fb.getString("id");
					fbFirstName = json_fb.getString("first_name");
					fbLastName = json_fb.getString("last_name");
					fbEmail = json_fb.getString("email");
					
					
					//something is wrong...the currently logged in user is not the proper FB user...
					//clear all cache and settings and re-route to welcome
					Player player = PlayerService.getPlayerFromLocal();
					if (!player.getFB().equals(fbId)){
						Logger.e(TAG,"handleFacebookMeResponse.FacebookError=fbUser in context is a mismatch with local user");
						
						//clear out everything and send user to login
						PlayerService.clearLocalStorageAndCache(context);
						
						Intent intent = new Intent(context, com.riotapps.word.Welcome.class);
			    	    context.startActivity(intent);
					
					}
					else{
						context.startGame();
					
					}
					
				} catch (FacebookError e) {
					Logger.w(TAG,"handleFacebookMeResponse.FacebookError=" + e.getLocalizedMessage());
					DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());  
				} catch (JSONException e) {
					Logger.w(TAG,"handleFacebookMeResponse.JSONException=" + e.getLocalizedMessage());
					
					DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());  
				}
		    }
		  }
    
   */  
    private void handleFacebookAppRequest() {
        Bundle params = new Bundle();
	    params.putString("message", this.getString(R.string.add_opponents_fb_dialog_message));
	    params.putString("to", this.game.getUnregisteredFBPlayersString());
        WebDialog requestsDialog = (
            new WebDialog.RequestsDialogBuilder(this,
                Session.getActiveSession(),
                params))
                .setOnCompleteListener(new OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                        FacebookException error) {
                        final String requestId = values.getString("request");
                        if (requestId != null) {
                        	startGame();
                           // Toast.makeText(getActivity().getApplicationContext(), 
                           //     "Request sent",  
                           //     Toast.LENGTH_SHORT).show();
                        } else {
                           // Toast.makeText(getActivity().getApplicationContext(), 
                           //     "Request cancelled", 
                           //     Toast.LENGTH_SHORT).show();
                            handleAppRequestCancel();
                        }
                    }

                })
                .build();
        requestsDialog.show();
    }
    
private class NetworkTask extends AsyncNetworkRequest{
		
	AddOpponents context;
		
		public NetworkTask(AddOpponents ctx, RequestType requestType,
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
		         
		         Log.i(AddOpponents.TAG, "StatusCode: " + statusCode);

		         switch(statusCode){  
		             case 200:  
		             case 201: {
		            	 
		            	 Game game = GameService.handleCreateGameResponse(this.context, iStream);
		            //	 handleResponseFromIOThread(game);
		            	 //saving game locally instead of passing by parcel because nested parcelable classes with lists of more nests
		            	 //was not working and driving me crazy
		            	 GameService.putGameToLocal(this.context, game);
		            	 GameService.clearLastGameListCheckTime(this.context);
		            	 
		            	 Intent intent = new Intent(this.context, com.riotapps.word.GameSurface.class);
		            	 intent.putExtra(Constants.EXTRA_GAME_ID, game.getId());
		             
		      	      	 this.context.startActivity(intent);
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
        
