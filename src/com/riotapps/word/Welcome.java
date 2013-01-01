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
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.Logger;
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
 
public class Welcome  extends FragmentActivity implements View.OnClickListener{
	private static final String TAG = Welcome.class.getSimpleName();

	// Session.StatusCallback statusCallback = new SessionStatusCallback();
	
	Player player;
    final Welcome context = this;	
    private UiLifecycleHelper uiHelper;
    
    NetworkTask runningTask = null;
  
	TextView txtFB;
	TextView txtNative;
	private SharedPreferences settings;
	private Bundle savedInstanceState;
	private boolean fetchFriendsAlreadyCalled = false;
	private boolean initialCallbackAlreadyCalled = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.welcome);
        
        Logger.d(TAG, "Welcome onCreate called");
        
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        
        txtFB = (TextView) findViewById(R.id.byFacebook);
        txtFB.setOnClickListener(this);
        txtNative = (TextView) findViewById(R.id.byEmail);
        txtNative.setOnClickListener(this);   
        
   /*     
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
                session.openForRead(new Session.OpenRequest(this).setCallback(callback));
            }
        }
        */
    }

  //  @Override
  //  public void onStart() {
  //      super.onStart();
  //      Logger.d(TAG, "onStart called");
  //    //  Session session = Session.getActiveSession();
  //    //  if (session != null){
  //    //  	session.addCallback(statusCallback);
  //    //  }
  //      try{
  //      	Session.getActiveSession().addCallback(statusCallback);
  //      }
  //      catch (Exception e){
  //      	Logger.d(TAG, "onStart error=" + e.getMessage());
  //      }
  //  }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	   // super.onSessionStateChange(state, exception);
    	Logger.d(TAG, "onSessionStateChange called - enum=" + state.toString());
	    if (state.isOpened()) {
	        // Session open
	    	Logger.d(TAG, "onSessionStateChange called state.isOpened");
	    	handleInitialCallback();
	    } else if (state.isClosed()) {
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
 		if (this.runningTask != null){
    		this.runningTask.cancel(true);
    	}
 	    super.onResume();
 	   Session session = Session.getActiveSession();
 	    if (session != null &&
 	           (session.isOpened() || session.isClosed()) ) {
 	        onSessionStateChange(session, session.getState(), null);
 	    }
 	    
        Logger.d(TAG, "onResume called  uiHelper.onResume() about to be called");
 	    uiHelper.onResume();
 	}   

 	@Override
 	public void onPause() {
 	    super.onPause();
 	     Logger.d(TAG, "onPause called  uiHelper.onPause() about to be called");
 	    uiHelper.onPause();
 	}   

 	@Override
 	public void onActivityResult(int requestCode, int resultCode, Intent data) {
 	    super.onActivityResult(requestCode, resultCode, data);
 	   Logger.d(TAG, "onActivityResult called  uiHelper.onActivityResult() about to be called");
 	    uiHelper.onActivityResult(requestCode, resultCode, data);
 	}   

 	@Override
 	public void onDestroy() {
 	//	Session session = Session.getActiveSession();
 	//    if (session != null &&
 	//           (session.isOpened() || session.isClosed()) ) {
 	//        onSessionStateChange(session, session.getState(), null);
 	//    }
 	//    else{
 	    super.onDestroy();
 	   Logger.d(TAG, "onDestroy called  uiHelper.onDestroy() about to be called");
 	    uiHelper.onDestroy();
 	//    }
 	}   

 	@Override
 	protected void onSaveInstanceState(Bundle outState) {
 	    super.onSaveInstanceState(outState);
 	   Logger.d(TAG, "onSaveInstanceState called  uiHelper.onSaveInstanceState() about to be called");
 	    uiHelper.onSaveInstanceState(outState);
 	}   

 	
  //  @Override
  //  public void onStop() {
   //     super.onStop();
    //    Logger.d(TAG, "onStop called");
      //  Session session = Session.getActiveSession();
      //  if (session != null){
      //  	Session.getActiveSession().removeCallback(statusCallback);
      //  }
     //   uiHelper.onStop();
       /* try{
        	Session.getActiveSession().removeCallback(statusCallback);
        }
        catch (Exception e){
        	Logger.d(TAG, "onStop error=" + e.getMessage());
        }*/
   // }

  //  @Override
  // protected void onSaveInstanceState(Bundle outState) {
  //       super.onSaveInstanceState(outState);
  //       Logger.d(TAG, "onSaveInstanceState called");
     //    Session session = Session.getActiveSession();
      //   if (session != null){
      //  	 Session.saveSession(session, outState);
       //  }
   //      try{ 
   //     	 Session session = Session.getActiveSession();
   //          Session.saveSession(session, outState);
   //      }
   //      catch(Exception e){
   //      	Logger.d(TAG, "onSaveInstanceState error=" + e.getMessage());
   //      }
   // }
    
  //  @Override
 //   public void onActivityResult(int requestCode, int resultCode, Intent data) {
 //       super.onActivityResult(requestCode, resultCode, data);
 //       Logger.d(TAG, "onActivityResult called");
        //Session session = Session.getActiveSession();
  //      try{ 
  //      	Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
   //     }
   //     catch(Exception e){
   //     	Logger.d(TAG, "onActivityResult error=" + e.getMessage());
   //     }
        
       // if (session != null){
       // 	Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
       // }
        
        //super.onActivityResult(requestCode, resultCode, data);
        //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
   // }
    
    @Override 
    public void onClick(View v) {
    	switch(v.getId()){  
        case R.id.byFacebook:  
        	this.connectToFacebook();
        	break;
        case R.id.byEmail:  
			Intent intent = new Intent(this, JoinNative.class);
			startActivity(intent);
			break;
        }  
      }
    
    private void connectToFacebook(){
    	//first connect to facebook
    	//then retrieve user information from facebook (id, name, email)
    	//then save the user information to rails server
    	//then retrieve friend list from facebook
    	//then save the friend list locally
    	//then redirect to landing page
    	try{
    	/*  Session session = Session.getActiveSession();
          if (!session.isOpened() && !session.isClosed()) {
        	  Logger.d(TAG, "connectToFacebook session is not open and not closed");
	        	 Logger.d(TAG, "connectToFacebook openForRead2 about to be called");
              session.openForRead(new Session.OpenRequest(this).setCallback(callback).setPermissions(Arrays.asList(Constants.FACEBOOK_PERMISSIONS)));
          } else {
        	  Logger.d(TAG, "connectToFacebook openActiveSession about to be called...permissions already set?");
              Session.openActiveSession(this, true, callback);
          }
    	*/
    	
	      com.facebook.Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
	
	  	 Logger.d(TAG, "connectToFacebook getActiveSession about to be called");
	         Session session = Session.getActiveSession();
	         if (session == null) {
	             if (this.savedInstanceState != null) {
	            	 Logger.d(TAG, "connectToFacebook restoreSession about to be called");
	
	                 session = Session.restoreSession(this, null, callback, savedInstanceState);
	             }
	             if (session == null) {
	            	 Logger.d(TAG, "connectToFacebook session constructor about to be called");
	
	                 session = new Session(this);
	             }
	        	 Logger.d(TAG, "connectToFacebook SetActiveSession about to be called");
	
	             Session.setActiveSession(session);
	             if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
	            	 
	            	 Logger.d(TAG, "connectToFacebook openForRead about to be called");
	                 session.openForRead(new Session.OpenRequest(context).setCallback(callback).setPermissions(Arrays.asList(Constants.FACEBOOK_PERMISSIONS)));
	             }
	         }
	  	
	         Logger.d(TAG, "session state=" + session.getState().toString());
	         session = Session.getActiveSession();
	       //  session = Session.getActiveSession();
	       //  Session session = Session.getActiveSession();
	         if (session.isOpened()){
	        	 this.handleInitialCallback();
	         }
	         else if (!session.isOpened() && !session.isClosed()) {
	        	 Logger.d(TAG, "connectToFacebook session is not open and not closed");
	        	 Logger.d(TAG, "connectToFacebook openForRead2 about to be called");
	        	// session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
	             session.openForRead(new Session.OpenRequest(context).setCallback(callback).setPermissions(Arrays.asList(Constants.FACEBOOK_PERMISSIONS)));
	         } else {
	        	 Logger.d(TAG, "connectToFacebook openActiveSession about to be called");
	             Session.openActiveSession(context, true, callback);
	         }
	    	 
    	}
    	catch (FacebookException fbEx){
    		DialogManager.SetupAlert(context, context.getString(R.string.sorry), fbEx.getMessage());
    	}
     }
    
    
  //  @Override
  //  public void onActivityResult(int requestCode, int resultCode, Intent data) {
  //      super.onActivityResult(requestCode, resultCode, data);
//
 //       facebook.authorizeCallback(requestCode, resultCode, data);
 //   }
    
  //  @Override
  //  public void onResume() {    
  //      super.onResume();
  //     // facebook.extendAccessTokenIfNeeded(this, null);
  //  }
    
    
  //  @Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
 //   	if (this.runningTask != null){
 //   		this.runningTask.cancel(true);
 //   	}
//		super.onPause();
//	}

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
		 		this.response = response;
		 		this.user = user;
		 	}
		 		 
		    public void run() {
		    	 Logger.w(TAG, "handleFacebookMeResponseRunnable");
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
		
    	Welcome context;
 
		public NetworkTask(Welcome ctx, RequestType requestType,
				String shownOnProgressDialog, String jsonPost) {
			super(ctx, requestType, shownOnProgressDialog, jsonPost);
			this.context = ctx;
		}

		@Override
		protected void onPostExecute(ServerResponse serverResponseObject) {
			// TODO Auto-generated method stub
			super.onPostExecute(serverResponseObject);
			 Logger.d(TAG,"NetworkTask onPostExecute ");
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
		             Logger.e("in ResponseHandler -> in handleResponse() -> in if(response !=null) -> in catch ","IllegalStateException " + e);  
		         } catch (IOException e) {  
		             Logger.e("in ResponseHandler -> in handleResponse() -> in if(response !=null) -> in catch ","IOException " + e);  
		         }  

		         int statusCode = response.getStatusLine().getStatusCode();  
		         
		        // Logger.e(Welcome.TAG, "StatusCode: " + statusCode);

		         switch(statusCode){  
		             case 200:  
		             case 201: {   
		            	 	//update local player context
		            	 Logger.d(TAG, "handleResponse about to call PlayerService.handleCreatePlayerResponse");
		            	 player = PlayerService.handleCreatePlayerResponse(this.context, iStream);
		            		
	            		 Logger.d(TAG, "handleResponse about to call fetchFriends");
	            		 //go get user's friends 
	            		 fetchFriends();
					     
		                 break;  

		             }//end of case 200 & 201 
		             case 401:
		            	 
		            	 String errorMessage;

		            	 try{
			            	 ErrorType errorType = ErrorService.translateError(context, iStream);
			            	 
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

		            	 DialogManager.SetupAlert(context, context.getString(R.string.oops), statusCode + " " + response.getStatusLine().getReasonPhrase(), 0);  
		         }  
		     }else if (exception instanceof ConnectTimeoutException) {
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
    
 //   private class SessionStatusCallback implements Session.StatusCallback {
 //       @Override
 //       public void call(Session session, SessionState state, Exception exception) {
 //           Logger.d(Welcome.TAG, "statusCallback appId" + session.getApplicationId() );
 //          
 //           handleInitialCallback();
 /*           if (exception == null) {
	            if (session.isOpened()) {
	            	 handleInitialCallback();
	            } 
	            else if (session.isClosed()){
	            	Logger.d(TAG, "SessionStatusCallback sessio isClosed==true");    	
	            }
	        }
            else{
            	Logger.d(TAG, "SessionStatusCallback login failed");
                //login failed...handle this somehow
            }
            */
  //      }
  //  }
}

