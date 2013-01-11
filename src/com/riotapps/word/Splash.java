package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.google.android.gcm.GCMRegistrar;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.*;
import com.riotapps.word.utils.Enums.RequestType;

public class Splash  extends FragmentActivity {
   
	private static final String TAG = Splash.class.getSimpleName();

    final Context context = this;
    Splash me = this;
    Handler handler;
    long startTime = System.nanoTime();
    NetworkTask runningTask = null;
    
    public void test(){}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
      
        Logger.d(TAG, "onCreate started");
        
       // Intent i = new Intent(this, WordLoaderService.class);
       // this.startService(new Intent(this, WordLoaderService.class));
     
        try{
	        GCMRegistrar.checkDevice(this);
	        GCMRegistrar.checkManifest(this);
	        final String regId = GCMRegistrar.getRegistrationId(this);
	        if (regId.equals("")) {
	          GCMRegistrar.register(this, this.getString(R.string.gcm_sender_id));
	        } else {
	          Logger.d(TAG, "onCreated Already registered");
	        }
        } catch(Exception e){
        	 Logger.d(TAG, "onCreated GCMRegistrar error=" + e.toString());
        }
        this.handlePreProcessing();
     }
    
    private void handlePreProcessing(){
		SharedPreferences settings = this.getSharedPreferences(Constants.USER_PREFS, 0);
	    String storedToken = settings.getString(Constants.USER_PREFS_AUTH_TOKEN, "");
	       
	    Logger.d(TAG, "handlePreProcessing called.");
	    
	    if (storedToken.length() > 0){
	    	String json = "";
			try {
				json = PlayerService.setupAuthTokenCheck(this, storedToken);
			} catch (DesignByContractException e) {
				//this should never happen unless there is some tampering
				 DialogManager.SetupAlert(context, getString(R.string.oops), e.getLocalizedMessage(), true, 0);
			}
 
			this.runningTask = new NetworkTask(this, RequestType.POST, json);
			this.runningTask.execute(Constants.REST_AUTHENTICATE_PLAYER_BY_TOKEN);
	    }
	    else{
	    	 Logger.w(TAG, "about to execute CheckConnectivityTask, no auth token");
	    	new CheckConnectivityTask().execute("");
	     
	    }
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (this.runningTask != null){
	  		this.runningTask.cancel(true);
	  	}
		finish();
	}
	
	  
	private class CheckConnectivityTask extends AsyncTask<String, Void, Boolean> {

		 @Override
		    protected void onPostExecute(Boolean result) {
	    	 	Logger.w(TAG, " CheckConnectivityTask onPostExecute");

	    	 	processConnectivityResults(result);
		    }

			@Override
			protected Boolean doInBackground(String... arg0) {
				 Logger.w(TAG, " CheckConnectivityTask doInBackground");
				return checkInitialConnectivity();
			}
	  }

	
	private Boolean checkInitialConnectivity(){
		  NetworkConnectivity connection = new NetworkConnectivity(context);
	        //are we connected to the web?
	        boolean isConnected = connection.checkNetworkConnectivity();
	        
	        if (isConnected == false)  
	        {
	        	try {
					Thread.sleep(Constants.INITIAL_CONNECTIVITY_THREAD_SLEEP);
					isConnected = connection.checkNetworkConnectivity();
					
		        } 
	        	catch (InterruptedException e1) {
	        		isConnected = false;
					e1.printStackTrace();
				}
	        }
	        else{
	        	 long currentTime = System.nanoTime();
	        	 
            	 //default time in which to leave splash up//watch out for negative values
	        	 long timeDiff = Utils.convertNanosecondsToMilliseconds(currentTime -  this.startTime);
            	 if (timeDiff < Constants.SPLASH_ACTIVITY_TIMEOUT){
            		 try {
            			// Logger.w(TAG, " checkInitialConnectivity OK , about to sleep" + Utils.convertNanosecondsToMilliseconds(currentTime -  this.startTime)  + " milliseconds" ); 
						Thread.sleep(Constants.SPLASH_ACTIVITY_TIMEOUT - timeDiff);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	 }
	        }
	        
	       return isConnected;
	}
	
	private void processConnectivityResults(Boolean connected){
	   	 Logger.w(TAG, " processTaskResults" );  
		 if (connected == true) 
	        {
	        	Intent intent = new Intent(this, com.riotapps.word.Welcome.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	 	      	this.startActivity(intent); 	
	        }
		 else{
			 DialogManager.SetupAlert(context, getString(R.string.oops), getString(R.string.msg_not_connected), true, 0);
		 }
	}

	
	
	private class NetworkTask extends AsyncNetworkRequest{

		Splash context;
		
		public NetworkTask(Splash ctx, RequestType requestType,
				String jsonPost) {
			super(ctx, requestType, "", jsonPost);
			this.context = ctx;
		    Logger.d(TAG, "NetworkTask called with jsonPost=" + jsonPost);
		 
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
		    	 long currentTime = System.nanoTime();
		    	 long timeDiff = 0;
		    	 Intent intent;
		    	 
		         switch(result.getStatusCode()){  
		             case 200:  
		            	// try{
		            		 Player player = PlayerService.handleAuthByTokenResponse(this.context, result.getResult());
	
			            	 //default time in which to leave splash up
		            		 timeDiff = Utils.convertNanosecondsToMilliseconds(currentTime -  context.startTime);
			            	 if (timeDiff < Constants.SPLASH_ACTIVITY_TIMEOUT){
			            		 try {
									Thread.sleep(Constants.SPLASH_ACTIVITY_TIMEOUT - timeDiff);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									Logger.d(TAG, "Thread sleep error=" + e.toString());
								}
			            	 }
		            		 
			     
			            	 if (player.getTotalNumLocalGames() == 0){
			            		 intent = new Intent(this.context, com.riotapps.word.StartGame.class);
			            	 }
			            	 else {
			            		 intent = new Intent(this.context, com.riotapps.word.MainLanding.class);
			            		 intent.putExtra(Constants.EXTRA_GAME_LIST_PREFETCHED, true);
			            	 }
		            		// Intent intent = new Intent( this, com.riotapps.word.MainLanding.class);
		            		// intent.putExtra(Constants.EXTRA_GAME_LIST_PREFETCHED, true);
				     	     context.startActivity(intent);
		            	// }
		            //	 catch(Exception e){
		           // /		 Logger.w(TAG, e.getStackTrace());
		           // 		 DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage(), true, 0); 
		            //	 }
		               break;
		             case 401:    
		                //unauthorized
		            	 //clear local storage and send to login
		            	 PlayerService.clearLocalStorageAndCache(context);
		            	 
		            
		            	 
		            	 //default time in which to leave splash up
		            	 timeDiff = Utils.convertNanosecondsToMilliseconds(currentTime -  context.startTime);
		            	 if (timeDiff < Constants.SPLASH_ACTIVITY_TIMEOUT){
		            		 try {
		            			 Thread.sleep(Constants.SPLASH_ACTIVITY_TIMEOUT - timeDiff);
							} catch (InterruptedException e) {
								Logger.w(TAG, "Thread.sleep timeDiff=" + timeDiff + " " + e.getMessage());
								e.printStackTrace();
							}
		            	 }
		            	 intent = new Intent( context, com.riotapps.word.Welcome.class);
		            	 context.startActivity(intent);
			     	     break;  

		             case 404:
		            	 DialogManager.SetupAlert(context, context.getString(R.string.sorry), context.getString(R.string.server_404_error), true, 0);
		            	 break;
		             case 422: 
		             case 500:

		            	 DialogManager.SetupAlert(context, context.getString(R.string.oops), result.getStatusCode() + " " + result.getStatusReason(), true, 0);  
		            	 break;
		         }  
		     }else if (exception instanceof ConnectTimeoutException) {
		    	 DialogManager.SetupAlert(context, context.getString(R.string.oops), context.getString(R.string.msg_connection_timeout), true, 0);
		     }else if(exception != null){  
		    	 DialogManager.SetupAlert(context, context.getString(R.string.oops), context.getString(R.string.msg_not_connected), true, 0);  

		     }  
		     else{  
		         Logger.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

		     }//end of else  
		}
		
	}
	
	
}
	
 
