package com.riotapps.word;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import com.facebook.FacebookActivity;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.facebook.android.*;
 
import com.google.gson.Gson;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Enums.RequestType;
import com.riotapps.word.facebook.friendsRequestListener;
import com.riotapps.word.facebook.meRequestListener;
import com.riotapps.word.hooks.ErrorService;
import com.riotapps.word.hooks.FBFriend;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.Error.ErrorType;

import com.facebook.GraphUser;
import com.facebook.LoggingBehaviors;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.LoginButton;
import com.facebook.Settings;
 
public class Welcome  extends FacebookActivity implements View.OnClickListener{
	private static final String TAG = Welcome.class.getSimpleName();
	Facebook facebook = new Facebook(Constants.FACEBOOK_API_ID);
	AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
	
	 Session.StatusCallback statusCallback = new SessionStatusCallback();
	
	Player player;
    final Welcome context = this;	
    
    NetworkTask runningTask = null;
  
	TextView txtFB;
	TextView txtNative;
	private SharedPreferences settings;
	private Bundle savedInstanceState;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.welcome);
        
 
        txtFB = (TextView) findViewById(R.id.byFacebook);
        txtFB.setOnClickListener(this);
        txtNative = (TextView) findViewById(R.id.byEmail);
        txtNative.setOnClickListener(this);   
        
        LoginButton login_button = (LoginButton) findViewById(R.id.login_button);
        login_button.setReadPermissions(Arrays.asList(Constants.FACEBOOK_PERMISSIONS));
        
 
      //  handler = new Handler() {
       //     public void handleMessage(Message msg) {
       //         // process incoming messages here
       //     	
       //     	//handleMessageFromHandler(msg);
        //    }
       // };
        
    }

    
    
    @Override 
    public void onClick(View v) {
    	switch(v.getId()){  
        case R.id.byFacebook:  
        	this.connectToFacebook();
        //	this.openSession();
        //	this.routeToFacebook();
        	break;
        case R.id.byEmail:  
			Intent intent = new Intent(this, JoinNative.class);
			startActivity(intent);
			break;
//            Toast.makeText(context, "Button 2 pressed ", Toast.LENGTH_SHORT).show();  
  //          break;  
        }  
      }
    
    private void connectToFacebook(){
    	 Settings.addLoggingBehavior(LoggingBehaviors.INCLUDE_ACCESS_TOKENS);

    	 Logger.d(TAG, "connectToFacebook getActiveSession about to be called");
         Session session = Session.getActiveSession();
         if (session == null) {
             if (this.savedInstanceState != null) {
                 session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
             }
             if (session == null) {
                 session = new Session(this);
             }
        	 Logger.d(TAG, "connectToFacebook setActiveSession about to be called");

             Session.setActiveSession(session);
             if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
            	 
            	 Logger.d(TAG, "connectToFacebook openForRead about to be called");
                 session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback).setPermissions(Arrays.asList(Constants.FACEBOOK_PERMISSIONS)));
             }
         }
    	
    	
       //  Session session = Session.getActiveSession();
         if (!session.isOpened() && !session.isClosed()) {
        	 Logger.d(TAG, "connectToFacebook session is not open and not closed");
             session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback).setPermissions(Arrays.asList(Constants.FACEBOOK_PERMISSIONS)));
         } else {
        	 Logger.d(TAG, "connectToFacebook openActiveSession about to be called");
             Session.openActiveSession(this, true, statusCallback);
         }
     }
    
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    @Override
    public void onResume() {    
        super.onResume();
        facebook.extendAccessTokenIfNeeded(this, null);
    }
    
    
	 //private void handleMessageFromHandler(Message msg){
	    
 //   @Override
 //   protected void onSessionStateChange(SessionState state, Exception exception) {
 //     // user has either logged in or not ...
 //     if (state.isOpened()) {
 //     // make request to the /me API
 //       Request request = Request.newMeRequest(this.getSession(), new Request.GraphUserCallback() {
 //         // callback after Graph API response with user object
 //         @Override
 //         public void onCompleted(GraphUser user, Response response) {
 //           if (user != null) {
 //           	Logger.e(TAG, "handleFacebookMeResponseRunnable about to be called");
 //       		context.runOnUiThread(new handleFacebookMeResponseRunnable(user, response));
 //           }
 //         }
 //     
 //       });
 //       Request.executeBatchAsync(request);
 //     }
 //   }	 
		 
	 //}
    
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
    	if (this.runningTask != null){
    		this.runningTask.cancel(true);
    	}
		super.onPause();
	}


//	private void handleFacebookMeRequest(){
 //   	Logger.e(TAG, "handleFacebookMeRequest facebook.getAccessToken()=" + facebook.getAccessToken());
 //  	 	SharedPreferences.Editor editor = settings.edit();
  //      editor.putString(Constants.FB_TOKEN, facebook.getAccessToken());
 //       editor.putLong(Constants.FB_TOKEN_EXPIRES, facebook.getAccessExpires());
 //       editor.commit();
 //    
 //       // get information about the currently logged in user
 //       mAsyncRunner.request("me", new fbMeRequestListener());
 //   	
 //   }
    
    
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
					//JSONObject json_fb = Util.parseJson(this.response);
					fbId = user.getId(); //json_fb.getString("id");
					fbFirstName = user.getFirstName(); // json_fb.getString("first_name");
					fbLastName = user.getLastName(); // json_fb.getString("last_name");
					fbEmail = response.getGraphObject().getProperty("email").toString(); // json_fb.getString("email");
					
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
				} //catch (JSONException e) {
			//		Logger.e(TAG,"handleFacebookMeResponse.JSONException=" + e.getLocalizedMessage());
			//		
			//		DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());  
			//	}
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
		    	//Logger.w(TAG, "handleFacebookMeResponse response=" + response);
		    	//Logger.w(TAG,"fbFriendsRequestListener.onComplete.JSONException=" + response);
				JSONObject json;
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
					
		     	  //  Intent intent = new Intent(context, com.riotapps.word.MainLanding.class);
		     	  //  intent.putExtra(Constants.EXTRA_GAME_LIST_PREFETCHED, true);
		     	    context.startActivity(intent);
					
				} catch (FacebookError e) {
					DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
				}
			 
		    }
		  }

    
 //   private void routeToFacebook() {
 //   	settings = this.getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE); //getPreferences(MODE_PRIVATE);
 //       String access_token = settings.getString(Constants.FB_TOKEN, null);
 //       
 //       //Logger.d(TAG, "routeToFacebook token=" + access_token);
 //       long expires = settings.getLong(Constants.FB_TOKEN_EXPIRES, 0);
 //       if(access_token != null) {
 //           facebook.setAccessToken(access_token);
  //      }
  //      if(expires != 0) {
  //          facebook.setAccessExpires(expires);
  //      }
  //      
  //      /*
  //       * Only call authorize if the access_token has expired.
  //       */
   //     if(!facebook.isSessionValid()) {
  //      	Logger.w(TAG, "facebook.authorize about to be called");
  //      	
  //      	// Looper.getMainLooper().prepare();
  //      	// new fbWork().execute(facebook);
  //      	// Looper.loop();
  //      	
  //      	facebook.authorize(context, new String[] { Constants.FACEBOOK_PERMISSIONS },
//			    	  new PostAuthorizeDialogListener() );
 //       	
  //      //	Thread t = new Thread(new Runnable() {
  //      //	             public void run() {
  //      //	            	 
 //       //	           	 
//		 //   	 facebook.authorize(context, new String[] { Constants.FACEBOOK_PERMISSIONS },
//		//	    	  new PostAuthorizeDialogListener() );
 //       //	    }
 //       //	});
 //       //	Looper.prepare(); 
 //       //	t.start();
//        //	Looper.loop();
        
             
     
   
 //       }
 //       else{
//
 //       	 mAsyncRunner.request("me", new fbMeRequestListener());
 //       	//handleFacebookMeRequest();
 //       }
 //   }
   
    public class PostAuthorizeDialogListener implements DialogListener 
    {
    	@Override
        public void onComplete(Bundle values) {
       	// Logger.e(TAG, "facebook.authorize..onComplete:");
       	// getFriends();
       	 Logger.w(TAG, "handleFacebookMeRequest about to be called");
    //   	 handleFacebookMeRequest();

        }

        @Override
        public void onFacebookError(FacebookError e) {
       	 DialogManager.SetupAlert(context, context.getString(R.string.facebook), e.getLocalizedMessage());
       	 Logger.e(TAG,"facebook.authorize..onFacebookError=" + e.getLocalizedMessage());
       	 //DialogManager.SetupAlert(context, "fbDialogListener", dialogMessage)
       	 
        }

        @Override
        public void onError(DialogError e) {
       	 Logger.e(TAG,"facebook.authorize..DialogError=" + e.getLocalizedMessage());
       	 DialogManager.SetupAlert(context, context.getString(R.string.facebook), e.getLocalizedMessage());
        }
        @Override
        public void onCancel() {
       	 Logger.e(TAG,"facebook.authorize..onCancel called");
       	 
        }
    }
    
  //  private class fbMeRequestListener implements RequestListener {

  //  	@Override
  //  	public void onComplete(String response, Object state) {
  //          
   // 		// get the logged-in user's friends
   // 		//save user to server...
   // 		//Logger.e(TAG, "fbMeRequestListener.onComplete response=" + response);
   // 		Logger.e(TAG, "handleFacebookMeResponseRunnable about to be called");
   // 		context.runOnUiThread(new handleFacebookMeResponseRunnable(response));
//		
 //    	}
//
 //   	@Override
  //  	public void onIOException(IOException e, Object state) {
  //  		// TODO Auto-generated method stub
  //  		 Logger.e(TAG,"fbMeRequestListener onIOException=" + e.getMessage());
 //   	}
//
 //   	@Override
 //   	public void onFileNotFoundException(FileNotFoundException e, Object state) {
 //   		// TODO Auto-generated method stub
  //  		 Logger.e(TAG,"fbMeRequestListener onFileNotFoundException=" + e.getMessage());
   // 	}

    //	@Override
   // 	public void onMalformedURLException(MalformedURLException e, Object state) {
  //  		// TODO Auto-generated method stub
  //  		 Logger.e(TAG,"fbMeRequestListener onMalformedURLException=" + e.getMessage());
  //  	}

   // 	@Override
 //   	public void onFacebookError(FacebookError e, Object state) {
 //   		// TODO Auto-generated method stub
 //   		Logger.e(TAG,"fbMeRequestListener onFacebookError=" + e.getMessage());
 //   	}
//
 //   }
    private void fetchFriends(){
    	Session session = Session.getActiveSession();
    	Request request = Request.newMyFriendsRequest(session, new Request.GraphUserListCallback() {
          // callback after Graph API response with user object
          @Override
          public void onCompleted(List<GraphUser> users, Response response) {
            if (users != null) {
            	Logger.e(TAG, "handleFacebookMeResponseRunnable about to be called");
        		context.runOnUiThread(new handleFacebookFriendsResponseRunnable(users, response));
            }
          }
        });
        Request.executeBatchAsync(request);
    }
    
    
 //   private class fbFriendsRequestListener implements RequestListener {
//
 //   	@Override
//    	public void onComplete(String response, Object state) {
//    		//route user to main landing
//    		//save fb friends locally, determine the structure
//    /*		{
//    			   "data": [
//    			      {
//    			         "name": "Will Richardson",
//    			         "id": "4914132"
//    			      },
//    			      {
//    			         "name": "Kevin Fielding",
//    			         "id": "4945016"
//    			      }
//    		}
//    */		
//     		Logger.e(TAG, "fbFriendsRequestListener.onComplete response=" + response);
//    		//Logger.e(TAG,"fbFriendsRequestListener onComplete=");
//    		context.runOnUiThread(new handleFacebookFriendsResponseRunnable(response));
//    
//   	}
//
//    	@Override
//    	public void onIOException(IOException e, Object state) {
//    		// TODO Auto-generated method stub
//    		Logger.e(TAG,"fbFriendsRequestListener IOException=" + e.getMessage());
//    	}
//
//    	@Override
//    	public void onFileNotFoundException(FileNotFoundException e, Object state) {
//    		// TODO Auto-generated method stub
//    		Logger.e(TAG,"fbFriendsRequestListener onFileNotFoundException=" + e.getMessage());
//    	}
//
//    	@Override
 //   	public void onMalformedURLException(MalformedURLException e, Object state) {
//    		// TODO Auto-generated method stub
//    		Logger.e(TAG,"fbFriendsRequestListener onMalformedURLException=" + e.getMessage());
//    	}
//
//    	@Override
//    	public void onFacebookError(FacebookError e, Object state) {
//    		// TODO Auto-generated method stub
//    		Logger.e(TAG,"fbFriendsRequestListener onFacebookError=" + e.getMessage());
//    	}
//
//    }

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
			//Logger.e(TAG,"NetworkTask onPostExecute ");
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
		            		player = PlayerService.handleCreatePlayerResponse(this.context, iStream);
		            	 
		            		//go get user's friends 
		            		 fetchFriends();
		            		 
		            		//Logger.e(TAG, "mAsyncRunner.request(me/friends) about to be called");
		            	
		            		 
		            		 
		            		//  mAsyncRunner.request("me/friends", new fbFriendsRequestListener());
					     
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
        }
    }
    
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Logger.d(Welcome.TAG, "statusCallback appId" + session.getApplicationId() );
            handleInitialCallback();
        }
    }
}

