package com.riotapps.word;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
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

public class Welcome  extends FragmentActivity implements View.OnClickListener{
	private static final String TAG = Welcome.class.getSimpleName();
	Facebook facebook = new Facebook(Constants.FACEBOOK_API_ID);
	AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
	
    final Welcome context = this;	
    
    protected Handler handler;
  
	TextView txtFB;
	TextView txtNative;
	private SharedPreferences settings;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
 
        txtFB = (TextView) findViewById(R.id.byFacebook);
        txtFB.setOnClickListener(this);
        txtNative = (TextView) findViewById(R.id.byEmail);
        txtNative.setOnClickListener(this);    
        handler = new Handler() {
            public void handleMessage(Message msg) {
                // process incoming messages here
            	
            	//handleMessageFromHandler(msg);
            }
        };
        
    }
    
    
   
    
    @Override 
    public void onClick(View v) {
    	switch(v.getId()){  
        case R.id.byFacebook:  
        	this.routeToFacebook();
        	break;
        case R.id.byEmail:  
			Intent intent = new Intent(this, JoinNative.class);
			startActivity(intent);
			break;
//            Toast.makeText(context, "Button 2 pressed ", Toast.LENGTH_SHORT).show();  
  //          break;  
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
	    
		 
		 
	 //}
    
	 private class handleFriendsRunnable implements Runnable {
		    public void run() {
		    	Logger.w(TAG, "handleFriendsRunnable");
		    	 SharedPreferences.Editor editor = settings.edit();
		         editor.putString(Constants.FB_TOKEN, facebook.getAccessToken());
		         editor.putLong(Constants.FB_TOKEN_EXPIRES, facebook.getAccessExpires());
		         editor.commit();
		      
		         // get information about the currently logged in user
		         mAsyncRunner.request("me", new fbMeRequestListener());
		         }
		  }
    
    private void routeToFacebook() {
    	settings = getPreferences(MODE_PRIVATE);
        String access_token = settings.getString(Constants.FB_TOKEN, null);
        long expires = settings.getLong(Constants.FB_TOKEN_EXPIRES, 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!facebook.isSessionValid()) {
    	 facebook.authorize(this, new String[] { Constants.FACEBOOK_PERMISSIONS },
	    	  new DialogListener() {
	             @Override
	             public void onComplete(Bundle values) {
	            	 Logger.w(TAG, "facebook.authorize..onComplete:");
	            	// getFriends();
	            	 handler.post(new handleFriendsRunnable());
	            	 //redirect to authorization if these errors occur
	                 //  User revoked access to your app:
	               //  {"error":{"type":"OAuthException","message":"Error validating access token: User 1053947411 has not authorized application 157111564357680."}}

	               //  OR when password changed:
	               //  {"error":{"type":"OAuthException","message":"Error validating access token: The session is invalid because the user logged out."}}
	                 
	             }

	             @Override
	             public void onFacebookError(FacebookError error) {
	            	 
	            	 Logger.d(TAG,"onFacebookError=" + error.getLocalizedMessage());
	            	 //DialogManager.SetupAlert(context, "fbDialogListener", dialogMessage)
	            	 
	             }

	             @Override
	             public void onError(DialogError e) {
	            	 Logger.d(TAG,"DialogError=" + e.getLocalizedMessage());
	             }
	             @Override
	             public void onCancel() {}
	         });
        }
        else{
        	 handler.post(new handleFriendsRunnable());
        	//getFriends();
        }
 
    }
    
    
    private void getFriends(){
    	Logger.w(TAG, "getFriends");
    	 SharedPreferences.Editor editor = settings.edit();
         editor.putString(Constants.FB_TOKEN, facebook.getAccessToken());
         editor.putLong(Constants.FB_TOKEN_EXPIRES, facebook.getAccessExpires());
         editor.commit();
      
         // get information about the currently logged in user
         mAsyncRunner.request("me", new fbMeRequestListener());
    }
    
    private void handleFacebookMeResponse(String response){
    	String fbId; 
		String fbFirstName; 
		String fbLastName; 
		String fbEmail; 
		try {
			JSONObject json_fb = Util.parseJson(response);
			fbId = json_fb.getString("id");
			fbFirstName = json_fb.getString("first_name");
			fbLastName = json_fb.getString("last_name");
			fbEmail = json_fb.getString("email");
			
			
			try { 
				String json = PlayerService.setupConnectViaFB(context, fbId, fbEmail, fbFirstName, fbLastName);
				
				//kick off thread
				new NetworkTask(context, RequestType.POST, getString(R.string.progress_updating), json).execute(Constants.REST_CREATE_PLAYER_URL);
			} catch (DesignByContractException e) {
				Logger.w(TAG,"handleFacebookMeResponse email=" + fbEmail+ " " + e.getLocalizedMessage());
				//DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());  
			}
		} catch (FacebookError e) {
			Logger.w(TAG,"handleFacebookMeResponse.FacebookError=" + e.getLocalizedMessage());
			//DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());  
		} catch (JSONException e) {
			Logger.w(TAG,"handleFacebookMeResponse.JSONException=" + e.getLocalizedMessage());
			// TODO Auto-generated catch block
			//DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());  
		}
    }
    
    
    private class fbMeRequestListener implements RequestListener {

    	@Override
    	public void onComplete(String response, Object state) {
            
    		// get the logged-in user's friends
    		//save user to server...
    		Logger.d(TAG, "fbMeRequestListener.onComplete response=" + response);
    		
    		handleFacebookMeResponse(response);
    		
  //  		String fbId; 
  //  		String fbFirstName; 
  //  		String fbLastName; 
  //  		String fbEmail; 
  //  		try {
//				JSONObject json_fb = Util.parseJson(response);
//				fbId = json_fb.getString("id");
//				fbFirstName = json_fb.getString("first_name");
//				fbLastName = json_fb.getString("last_name");
//				fbEmail = json_fb.getString("email");
//				
//				
//				try { 
//					String json = PlayerService.setupConnectViaFB(context, fbId, fbEmail, fbFirstName, fbLastName);
//					
//					//kick off thread
//					new NetworkTask(context, RequestType.POST, getString(R.string.progress_updating), json).execute(Constants.REST_CREATE_PLAYER_URL);
//				} catch (DesignByContractException e) {
//					Logger.w(TAG,"onComplete.DesignByContractException email=" + fbEmail+ " " + e.getLocalizedMessage());
//					//DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());  
//				}
//			} catch (FacebookError e) {
//				Logger.w(TAG,"onComplete.FacebookError=" + e.getLocalizedMessage());
//				//DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());  
//			} catch (JSONException e) {
//				Logger.w(TAG,"onComplete.JSONException=" + e.getLocalizedMessage());
//				// TODO Auto-generated catch block
//				//DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());  
//			}
    		
			
		
		
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
    
    private class fbFriendsRequestListener implements RequestListener {

    	@Override
    	public void onComplete(String response, Object state) {
    		//route user to main landing
    		//save fb friends locally, determine the structure
    /*		{
    			   "data": [
    			      {
    			         "name": "Will Richardson",
    			         "id": "4914132"
    			      },
    			      {
    			         "name": "Kevin Fielding",
    			         "id": "4945016"
    			      }
    		}
    */		
    		Logger.w(TAG,"fbFriendsRequestListener.onComplete.JSONException=" + response);
    		JSONObject json;
			try {
				PlayerService.saveFacebookFriendsFromJSONResponse(context, response);
	 
	     	    Intent intent = new Intent(context, com.riotapps.word.MainLanding.class);
	     	    context.startActivity(intent);
				
			} catch (FacebookError e) {
				DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());  
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getLocalizedMessage());  
			}
    		
            
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
		         
		         Log.i(Welcome.TAG, "StatusCode: " + statusCode);

		         switch(statusCode){  
		             case 200:  
		             case 201: {   
		            	 	//update local player context
		            		Player player = PlayerService.handleCreatePlayerResponse(this.context, iStream);
		            		 
		            		//go get user's friends
   	            		    mAsyncRunner.request("me/friends", new fbFriendsRequestListener());
					     
		                 break;  

		             }//end of case 200 & 201 
		             case 401:
		            	 ErrorType errorType = ErrorService.translateError(context, iStream);
		            	 
		            	 String errorMessage;
		            	 
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
		            	 	case UNAUTHORIZED:
		            	 		errorMessage = context.getString(R.string.validation_unauthorized);
		            	 		break;
		            	 	default:
		            	 		errorMessage = context.getString(R.string.validation_unspecified_error);
		            	 		break;		            	 		
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
		         Log.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

		     }//end of else  
		}
		
 
	}
}

