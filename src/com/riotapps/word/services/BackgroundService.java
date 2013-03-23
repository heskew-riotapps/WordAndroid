package com.riotapps.word.services;

import org.apache.http.conn.ConnectTimeoutException;

import com.google.android.gcm.GCMRegistrar;
import com.riotapps.word.R;
import com.riotapps.word.Splash;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.WordService;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.NetworkTaskResult;
import com.riotapps.word.utils.Utils;
import com.riotapps.word.utils.Enums.RequestType;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class BackgroundService extends Service {
	private static final String TAG = BackgroundService.class.getSimpleName();
	
	public long runningTime = System.nanoTime();
	public long captureTime = System.nanoTime();
	private ApplicationContext context;
	private Player player;
	private NetworkTask task;
	private boolean isProcessed = false;
//	private MainTask runningTask;
	private LoadWordsTask wordLoaderTask;
	private String storedToken = null;
	//private Thread wordListThread;
	private boolean isWordLoaderCompleted = false;
	private boolean isGameFetcherCompleted = false;
	private String completedDate;
	private String lastAlertActivationDate;
	private String registrationId;
	 
   @Override
   public IBinder onBind(Intent intent) {
      return null;
   }

   @Override
   public void onCreate() {
      //code to execute when the service is first created
	   Logger.d(TAG, "onCreate called");
	 
   }

   @Override
   public void onDestroy() {
      //code to execute when the service is shutting down
	   this.player = null;
	   this.task = null;
	   //this.runningTask = null;
	  // this.wordLoaderTask = null;
	   
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      //code to execute when the service is starting up
	   Logger.d(TAG, "onStartCommand called this.isProcessed=" + this.isProcessed);
	  // if (!this.isProcessed){
		   //this.player = PlayerService.getPlayerFromLocal();
		   this.storedToken = intent.getStringExtra(Constants.EXTRA_PLAYER_TOKEN);  //PlayerService.getAuthTokenFromLocal();
		   this.lastAlertActivationDate = intent.getStringExtra(Constants.EXTRA_PLAYER_LAST_ALERT_ACTIVATION_DATE); 
		   this.completedDate = intent.getStringExtra(Constants.EXTRA_PLAYER_COMPLETED_DATE); 
		   this.registrationId = intent.getStringExtra(Constants.EXTRA_PLAYER_GCM_RID); 
		   
		   Logger.d(TAG, "onStartCommand storedToken=" + this.storedToken);
		   Logger.d(TAG, "onStartCommand lastAlertActivationDate=" + this.lastAlertActivationDate);
		   Logger.d(TAG, "onStartCommand completedDate=" + this.completedDate);
		   
		   context = (ApplicationContext) ApplicationContext.getAppContext(); // (ApplicationContext)this.getApplicationContext();
		 
		  // this.runningTask = new MainTask();
		  // this.runningTask.execute("");
		   
		  // this.wordLoaderTask = new LoadWordsTask();
		  // this.wordLoaderTask.execute("");
		   
		   if (storedToken.length() > 0){
			   try {
					Thread.sleep(Constants.BACKGROUND_GAME_LIST_DELAY_IN_MILLISECONDS);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   this.getGameList();
		   }
	  // }
	   
	 
	   return START_STICKY;
   }
   
   private void getGCMRegistration(){
	   
//       this.captureTime("GCMRegistrar starting");
       try{
       	    Logger.w(TAG, "GCMRegistrar.checkDevice about to be called");
	        GCMRegistrar.checkDevice(this);
	        GCMRegistrar.checkManifest(this);
	        final String regId = GCMRegistrar.getRegistrationId(this);
	        if (regId.equals("")) {
	        	GCMRegistrar.register(this, this.getString(R.string.gcm_sender_id));
	        } else {
	        	Logger.w(TAG, "onCreated gcm already registered regId=" + regId);
	  			PlayerService.updateRegistrationId(context, regId);
	        }
       } catch(Exception e){
       	 Logger.w(TAG, "onCreated GCMRegistrar error=" + e.toString());
       }
  //     this.captureTime("GCMRegistrar ended");
   }
   
   private class MainTask extends AsyncTask<String, Void, String> {

       @Override
       protected String doInBackground(String... params) {
      	  // ApplicationContext appContext = (ApplicationContext)getApplicationContext();
      	  
      	  getGCMRegistration();	
      	   return "";
       }      

       @Override
       protected void onPostExecute(String result) {
       
       }

 }
   
   
   private void getGameList(){
	   try { 
		   //set up timer in background service to pull in game list every 10 minutes
		   //just do full auth here to get opponents and games
		   
		   //wait a few seconds for main landing to load
		/*
		   
		   try {
			Thread.sleep(Constants.GAME_LIST_CHECK_INTERVAL_IN_MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		   
  		 Logger.d(TAG, "getGameList");
				String json = PlayerService.setupAuthTokenCheck(context, this.storedToken, this.completedDate, 
							this.lastAlertActivationDate, this.registrationId);
				//this will bring back the players games too
				this.task = new NetworkTask(RequestType.POST, json, false);
				this.task.execute(Constants.REST_AUTHENTICATE_PLAYER_BY_TOKEN);
 			} catch (DesignByContractException e) {
 				//this should never happen unless there is some tampering
 				Logger.d(TAG, "getGameList error=" + e.getLocalizedMessage());
 				//DialogManager.SetupAlert(context, getString(R.string.oops), e.getLocalizedMessage(), true, 0);
 			}
   }
   

	private void captureTime(String text){
		ApplicationContext.captureTime(TAG, text);
	    // this.captureTime = System.nanoTime();
	    // Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", text, Utils.convertNanosecondsToMilliseconds(this.captureTime - this.runningTime)));
	    // this.runningTime = this.captureTime;

	}
   
   private class NetworkTask extends AsyncNetworkRequest{

		//ApplicationContext context;
 
		public NetworkTask(RequestType requestType, String jsonPost, boolean fetchGame) {
			super(null, requestType, "", jsonPost);
			//this.context = ctx;

		}

		@Override
		protected void onPostExecute(NetworkTaskResult result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			BackgroundService.this.handleResponse(result);
		}
	}
	
	private void handleResponse(NetworkTaskResult result){
	     Exception exception = result.getException();   

	     Logger.d(TAG, "handleResponse statusCode=" + result.getStatusCode());
	     
	     if(result.getResult() != null){  
	         switch(result.getStatusCode()){  
	             case 200:  
	            	 try{
	            		 	isProcessed = true;
	            		//	 Logger.d(TAG, "handleAuthByTokenResponse after timer");
		            	//	 Player player = PlayerService.handleAuthByTokenResponse(result.getResult());
		            	//	 
		            	 //  	 Logger.d(TAG, "handleResponse active games=" + player.getActiveGamesYourTurn().size() + " opp games=" + player.getActiveGamesOpponentTurn().size());

		            	//	 GameService.updateLastGameListCheckTime();
		            		 
	            		 	//send intent to process bridge
	            		 	
	            		 	
		            			Intent intent = new Intent(Constants.INTENT_GAME_LIST_REFRESHED_TO_BRIDGE);
		            			intent.putExtra(Constants.EXTRA_PLAYER_AUTH_RESULT, result.getResult());
		            		    this.sendBroadcast(intent);
	            		 
	            		 }
	            	 catch(Exception e){
	            		// e.getStackTrace().toString()
	            		 Logger.w(TAG, "status code=200 " + e.toString());
	            		 String err = (e.getMessage()==null)?"status code=200, unknown error":e.getMessage();
	            		 Logger.w(TAG, err);
	            		 
	            	 }
	               break;
	             case 401:    
	                //unauthorized
	            	 //clear local storage and send to login
	            //	 PlayerService.clearLocalStorageAndCache(context);

	            //	 Intent intent = new Intent( context, com.riotapps.word.Welcome.class);
		     	 //    this.startActivity(intent);
		     	     break;  

	             case 404:
	            	 //no updates, do nothing
	
	            	 break;
	             case 422: 
	             case 500:
	            	 Logger.w(TAG, "status code=500 " +  result.getStatusCode() + " " + result.getStatusReason());
	            	// DialogManager.SetupAlert(context, this.getString(R.string.oops), result.getStatusCode() + " " + result.getStatusReason(), true, 0);  
	            	 break;
	         }  
	     }else if (exception instanceof ConnectTimeoutException ||  exception instanceof java.net.SocketTimeoutException) {
	    	 Logger.w(TAG, this.getString(R.string.msg_connection_timeout));
	    	// DialogManager.SetupAlert(context, this.getString(R.string.oops), this.getString(R.string.msg_connection_timeout), true, 0);
	     }else if(exception != null){  
	    	// DialogManager.SetupAlert(context, this.getString(R.string.oops), this.getString(R.string.msg_not_connected), true, 0);  
	    	 Logger.w(TAG, this.getString(R.string.msg_not_connected));
	     }  
	     else{  
	         Logger.d("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

	     }//end of else  
	     
	     this.task = null;
	     this.isGameFetcherCompleted = true;
	     this.handleCompleted();
	}
	
	 private class LoadWordsTask extends AsyncTask<String, Void, String> {

         @Override
         protected String doInBackground(String... params) {
        	   
        	  processWordList();
               return "Executed";
         }      

         @Override
         protected void onPostExecute(String result) {
        	  
        	 stopWordLoaderTask();
         }

   }

 
	private void processWordList(){
		   ApplicationContext appContext = (ApplicationContext)getApplicationContext();
    	   try{

    		   appContext.getWordService().isWordValid("aaa");     			  
    		   captureTime("letter a - loaded");
    		   
    		   appContext.getWordService().isWordValid("bbb");
    		   captureTime("letter b - loaded");
    		   
    		   appContext.getWordService().isWordValid("ccc");     			  
    		   captureTime("letter c - loaded");
    		   
    		   appContext.getWordService().isWordValid("ddd");
    		   captureTime("letter d - loaded");
    		   
    		   appContext.getWordService().isWordValid("eee");     			  
    		   captureTime("letter e - loaded");
    		   
    		   appContext.getWordService().isWordValid("fff");
    		   captureTime("letter f - loaded");
    		   
    		   appContext.getWordService().isWordValid("ggg");     			  
    		   captureTime("letter g - loaded");
    		   
    		   appContext.getWordService().isWordValid("hhh");
    		   captureTime("letter h - loaded");
    		   
    		   appContext.getWordService().isWordValid("iii");     			  
    		   captureTime("letter i - loaded");
    		   
    		   appContext.getWordService().isWordValid("jjj");
    		   captureTime("letter j - loaded");
    		   
    		   appContext.getWordService().isWordValid("kkk");     			  
    		   captureTime("letter k - loaded");
    		   
    		   appContext.getWordService().isWordValid("lll");
    		   captureTime("letter l - loaded");
    		   
    		   appContext.getWordService().isWordValid("mmm");     			  
    		   captureTime("letter m - loaded");
    		   
    		   appContext.getWordService().isWordValid("nnn");
    		   captureTime("letter n - loaded");
    		   
    		   appContext.getWordService().isWordValid("ooo");     			  
    		   captureTime("letter o - loaded");
    		   
    		   appContext.getWordService().isWordValid("ppp");
    		   captureTime("letter p - loaded");
    		   
    		   appContext.getWordService().isWordValid("qqq");     			  
    		   captureTime("letter q - loaded");
    		   
    		   appContext.getWordService().isWordValid("rrr");
    		   captureTime("letter r - loaded");
    		   
    		   appContext.getWordService().isWordValid("sss");     			  
    		   captureTime("letter s - loaded");
    		   
    		   appContext.getWordService().isWordValid("ttt");
    		   captureTime("letter t - loaded");
    		   
    		   appContext.getWordService().isWordValid("uuu");     			  
    		   captureTime("letter u - loaded");
    		   
    		   appContext.getWordService().isWordValid("vvv");
    		   captureTime("letter v - loaded");
     		   
    		   appContext.getWordService().isWordValid("www");
    		   captureTime("letter w - loaded");
    		   
    		   appContext.getWordService().isWordValid("xxx");     			  
    		   captureTime("letter x- loaded");
    		   
    		   appContext.getWordService().isWordValid("yyy");
    		   captureTime("letter y - loaded");
    		   
    		   appContext.getWordService().isWordValid("zzz");     			  
    		   captureTime("letter z - loaded");  		   
    	   
    		  
    	   }
    	   catch (Exception e){
    		   Logger.d(TAG, e.toString());
    	   }
    	   Logger.d(TAG, "all words loaded");
	
	 
	}
	
	private void stopWordLoaderTask(){
		this.wordLoaderTask = null;
		this.isWordLoaderCompleted = true;
		this.handleCompleted();
	}
	
	private void handleCompleted(){
		if (this.isGameFetcherCompleted && this.isWordLoaderCompleted){
			this.stopSelf();
		}
	}
}
