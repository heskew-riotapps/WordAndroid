package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import com.facebook.android.FacebookError;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.CustomProgressDialog;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.NetworkTaskResult;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Enums.RequestType;
import com.riotapps.word.hooks.ErrorService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.Error.ErrorType;

import com.facebook.model.GraphUser;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.LoggingBehavior;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
 
public class ConnectToFacebook  extends FragmentActivity{
	private static final String TAG = ConnectToFacebook.class.getSimpleName();

	// Session.StatusCallback statusCallback = new SessionStatusCallback();
	
	Player player;
    final ConnectToFacebook context = this;	
    private UiLifecycleHelper uiHelper;
    
    NetworkTask runningTask = null;
 
	private SharedPreferences settings;
	private Bundle savedInstanceState;
	private boolean fetchFriendsAlreadyCalled = false;
	private boolean initialCallbackAlreadyCalled = false;
	CustomProgressDialog spinner = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.connecttofacebook);
        
        Logger.d(TAG,  "onCreate called");
        
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        
        spinner = new CustomProgressDialog(this);
        spinner.setMessage(this.getString(R.string.progress_connecting));
        spinner.show();
		 
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, callback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(callback).setPermissions(Arrays.asList(Constants.FACEBOOK_PERMISSIONS)));
            }
        }
        session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(callback).setPermissions(Arrays.asList(Constants.FACEBOOK_PERMISSIONS)));
        } else {
            Session.openActiveSession(this, true, callback);
        }
    }

 

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	   // super.onSessionStateChange(state, exception);
    	Logger.d(TAG, "onSessionStateChange called - enum=" + state.toString());
	    if (session.isOpened()) {
	        // Session open
	    	Logger.d(TAG, "onSessionStateChange called state.isOpened");
	    	 if (!this.initialCallbackAlreadyCalled){
	    		 this.initialCallbackAlreadyCalled = true;
	    		 Logger.d(TAG, "onSessionStateChange Request.newMeRequest about to be called");
	              Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	                // callback after Graph API response with user object
	                @Override
	                public void onCompleted(GraphUser user, Response response) {
	                	Logger.d(TAG, "GraphUserCallback onCompleted called");
	                  if (user != null) {
	                  	Logger.d(TAG, "handleFacebookMeResponseRunnable about to be called");
	              		context.runOnUiThread(new handleFacebookMeResponseRunnable(user, response));
	                  }
	                }
	              });
	              Logger.d(TAG, "onSessionStateChange Request.newMeRequest executeBatchAsync called");
	              Request.executeBatchAsync(request); 
	            }
	    } else if (session.isClosed()) {
	        // Session closed
	    	Logger.d(TAG, "onSessionStateChange called state.isClosed");
	    }
	}
    
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override

        public void call(Session session, SessionState state, Exception exception) {
            Logger.d(TAG, "callback called onSessionStateChange about to be called");
            onSessionStateChange(session, state, exception);
        }
 	};
    
 	
 	@Override
 	public void onResume() {
 	    super.onResume();
 	    uiHelper.onResume();
 	}

 	@Override
 	public void onActivityResult(int requestCode, int resultCode, Intent data) {
 	    super.onActivityResult(requestCode, resultCode, data);
 	    uiHelper.onActivityResult(requestCode, resultCode, data);
 	}

 	@Override
 	public void onPause() {
 	    super.onPause();
 	    uiHelper.onPause();
 	}

 	@Override
 	public void onDestroy() {
 	    super.onDestroy();
 	    uiHelper.onDestroy();
 	}

 	@Override
 	public void onSaveInstanceState(Bundle outState) {
 	    super.onSaveInstanceState(outState);
 	    uiHelper.onSaveInstanceState(outState);
 	}
  


    private class handleDialogRunnable implements Runnable{
    	private String title;
    	private String message;
    	
    	public handleDialogRunnable(String title, String message){
    		this.title = title;
    		this.message = message;
    	}
    	public void run(){
    		DialogManager.SetupAlert(context, title, message);
    	}
    	
    }
    
    
	 private class handleFacebookMeResponseRunnable implements Runnable {
		 private Response response;	
		 private GraphUser user;
		 
		 public handleFacebookMeResponseRunnable(GraphUser user, Response response){
			 Logger.d(TAG, "handleFacebookMeResponseRunnable constructor");
		 		this.response = response;
		 		this.user = user;
		 	}
		 		 
		    public void run() {
		    	 Logger.d(TAG, "handleFacebookMeResponseRunnable");
		    	String fbId; 
				String fbFirstName; 
				String fbLastName; 
				String fbEmail; 
			 
				try {
					fbId = user.getId(); 
					fbFirstName = user.getFirstName();  
					fbLastName = user.getLastName();  
					fbEmail = response.getGraphObject().getProperty("email").toString(); 
					
					//Logger.e(TAG, "handleFacebookMeResponse...response=" + this.response);
					//Logger.w(TAG, "handleFacebookMeResponse...email=" + fbEmail);
					try { 
						String json = PlayerService.setupConnectViaFB(context, fbId, fbEmail, fbFirstName, fbLastName);
						
						//kick off thread
						runningTask = new NetworkTask(context, RequestType.POST, getString(R.string.progress_connecting), json);
						runningTask.execute(Constants.REST_CREATE_PLAYER_URL);
					} catch (DesignByContractException e) {
						Logger.e(TAG,"handleFacebookMeResponse email=" + fbEmail+ " " + e.getLocalizedMessage());
						DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
					}
				} catch (FacebookError e) {
					Logger.e(TAG,"handleFacebookMeResponse.FacebookError=" + e.getLocalizedMessage());
					DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());  
				}
		    }
		  }
	 
	 private class handleFacebookFriendsResponseRunnable implements Runnable {
		 private Response response;	
		 private List<GraphUser> users;	
		 
		 public handleFacebookFriendsResponseRunnable(List<GraphUser> users, Response response){
		 		this.response = response;
		 		this.users = users;
		 	}
		 
		 
		    public void run() {

				try {
					if (users.size() > 0){
						PlayerService.saveFacebookFriendsFromJSONResponse(context, users);
					}
					else {
						Logger.e(TAG,"fbFriendsRequestListener. response from facebook empty");
					}
					
					Intent intent;
					
					 if (player.getTotalNumLocalGames() == 0){
	            		 intent = new Intent(context, com.riotapps.word.StartGame.class);
	            	 }
	            	 else {
	            		 intent = new Intent(context, com.riotapps.word.MainLanding.class);
	            		 intent.putExtra(Constants.EXTRA_GAME_LIST_PREFETCHED, true);
	            	 }
					
		     	    context.startActivity(intent);
		     	   spinner.dismiss();
					
				} catch (FacebookError e) {
					DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
				} catch (JSONException e) {
					DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
				}
			 
		    }
		  }

	 
    private void fetchFriends(){
    	Logger.d(TAG, "fetchFriends called");
    	
    	if (!fetchFriendsAlreadyCalled){
    		fetchFriendsAlreadyCalled = true;
    		
	    	Session session = Session.getActiveSession();
	    	Request request = Request.newMyFriendsRequest(session, new Request.GraphUserListCallback() {
	          // callback after Graph API response with user object
	          @Override
	          public void onCompleted(List<GraphUser> users, Response response) {
	            if (users != null) {
	            	Logger.e(TAG, "handleFacebookFriendsResponseRunnable about to be called");
	        		context.runOnUiThread(new handleFacebookFriendsResponseRunnable(users, response));
	            }
	          }
	        });
	        Request.executeBatchAsync(request);
    	}
    }
  

    private class NetworkTask extends AsyncNetworkRequest{
		
    	ConnectToFacebook context;
 
		public NetworkTask(ConnectToFacebook ctx, RequestType requestType,
				String shownOnProgressDialog, String jsonPost) {
			super(ctx, requestType, shownOnProgressDialog, jsonPost);
			this.context = ctx;
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
		            	 	//update local player context
		            	 Logger.d(TAG, "handleResponse about to call PlayerService.handleCreatePlayerResponse");
		            	 player = PlayerService.handleCreatePlayerResponse(this.context, result.getResult());
		            		
	            		 Logger.d(TAG, "handleResponse about to call fetchFriends");
	            		 //go get user's friends 
	            		 fetchFriends();
					     
		                 break;  

		             }//end of case 200 & 201 
		             case 401:
		            	 
		            	 String errorMessage;

		            	 try{
			            	 ErrorType errorType = ErrorService.translateError(context, result.getResult());
			            	 
			            	 switch (errorType){
			            	 	case INCORRECT_PASSWORD:
					            	errorMessage = context.getString(R.string.validation_incorrect_password);
			            	 		break; 
			            	 	case EMAIL_NOT_SUPPLIED:
			            	 		errorMessage = context.getString(R.string.validation_email_not_supplied);
			            	 		break;
			            	 	case NICKNAME_IN_USE:
			            	 		errorMessage = context.getString(R.string.validation_nickname_already_taken);
			            	 		break;
			            	 	case EMAIL_IN_USE:
			            	 		errorMessage = context.getString(R.string.validation_email_already_taken);
			            	 		break;
			            	 	case NICKNAME_NOT_SUPPLIED:
			            	 		errorMessage = context.getString(R.string.validation_nickname_not_supplied);
			            	 		break;
			            	 	case FB_USER_EMAIL_ALREADY_IN_USE:
			            	 		errorMessage = context.getString(R.string.validation_email_is_already_in_use);
			            	 		break;
			            	 	case UNAUTHORIZED:
			            	 		errorMessage = context.getString(R.string.validation_unauthorized);
			            	 		break;
			            	 	default:
			            	 		errorMessage = context.getString(R.string.validation_unspecified_error);
			            	 		break;		            	 		
			            	 }
		            	 
		            	 } catch (Exception e){
		            		 errorMessage =  (e.getMessage() == null ? "unknown error" : e.getMessage());
		            	 }
		            	 
		            	 DialogManager.SetupAlert(context, context.getString(R.string.sorry), errorMessage);
		            	 break;
		            	 
		             case 404:
		             //case Status code == 422
		            	 DialogManager.SetupAlert(context, context.getString(R.string.sorry), context.getString(R.string.validation_404_error), false, Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
		            	 break;
		             case 422: 
		             case 500:

		            	 DialogManager.SetupAlert(context, context.getString(R.string.oops), result.getStatusCode() + " " + result.getStatusReason(), 0);  
		         }  
		     }else if (exception instanceof ConnectTimeoutException ||  exception instanceof java.net.SocketTimeoutException) {
		    	 DialogManager.SetupAlert(context, context.getString(R.string.oops), context.getString(R.string.msg_connection_timeout), 0);
		     }else if(exception != null){  
		    	 DialogManager.SetupAlert(context, context.getString(R.string.oops), context.getString(R.string.msg_not_connected), 0);  

		     }  
		     else{  
		         Logger.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

		     }//end of else  
		}
		
 
	}
    
    private void handleInitialCallback(){
    	 Logger.d(TAG, "handleInitialCallback");
    	 if (!this.initialCallbackAlreadyCalled){
    		 this.initialCallbackAlreadyCalled = true;
	        Session session = Session.getActiveSession();
	            // make request to the /me API
	        if (session.isOpened()) {
	        	 Logger.d(TAG, "SessionStatusCallback Request.newMeRequest about to be called");
	              Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	                // callback after Graph API response with user object
	                @Override
	                public void onCompleted(GraphUser user, Response response) {
	                  if (user != null) {
	                  	Logger.d(TAG, "handleFacebookMeResponseRunnable about to be called");
	              		context.runOnUiThread(new handleFacebookMeResponseRunnable(user, response));
	                  }
	                }
	              });
	              Request.executeBatchAsync(request); 
	            }
	        else{
	        	Logger.d(TAG, "SessionStatusCallback session is closed");
	        	
	        	
	        	context.runOnUiThread(new handleDialogRunnable(context.getString(R.string.sorry), context.getString(R.string.welcome_facebook_login_issue)));
	        	 
	        	/* if (!session.isOpened() && !session.isClosed()) {
		        	 Logger.d(TAG, "handleInitialCallback connectToFacebook session is not open and not closed");
		        	 Logger.d(TAG, "handleInitialCallback connectToFacebook openForRead2 about to be called");
		        	// session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
		             session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback).setPermissions(Arrays.asList(Constants.FACEBOOK_PERMISSIONS)));
		         } else {*/
		        //	 Logger.d(TAG, "handleInitialCallback connectToFacebook openActiveSession about to be called");
		        //     Session.openActiveSession(context, true, statusCallback);
		         //}
	        }
    	 }
    }
    
}

