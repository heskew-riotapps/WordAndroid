package com.riotapps.word;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.*;

public class Splash  extends Activity {
    /** Called when the activity is first created. */
	
    final Context context = this;
    
    public void test(){}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        

        NetworkConnectivity connection = new NetworkConnectivity(this);
        //are we connected to the web?
        boolean isConnected = connection.checkNetworkConnectivity();
    
        com.riotapps.word.utils.DialogManager.SetupAlert(context, getString(R.string.oops), "Yoohoo!!!");	
        
        if (isConnected == false)  
        {
        	try {
				Thread.sleep(3000);
				isConnected = connection.checkNetworkConnectivity();
				
				if (isConnected == false) { 
				 	
					//change this to more specific dialog with button that goes to a page that allows offline usage
					DialogManager.SetupAlert(context, getString(R.string.oops), getString(R.string.msg_not_connected));					

				 	
					//Intent notConnected = new Intent(getApplicationContext(), NotConnected.class);
					//startActivity(notConnected);
					//finish();
				}
        	} 
        	catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        
        if (isConnected == false)////change back
        {
	        //Thread setup = new Thread(){
	        //	public void run(){
	        	//	try {
	        	//		sleep(3000);
	        	//	}
	        	//	catch (InterruptedException e){
	        	//		e.printStackTrace();
	        	//	}
	        	//	finally {
	        			SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);
	        			
	        			String auth_token = settings.getString(Constants.USER_PREFS_AUTH_TOKEN, "");
	        			String playerId = settings.getString(Constants.USER_PREFS_USER_ID, "");



	        	 	       
	        			if (auth_token.length() > 0) {
	        				//get player from rails server
	        				PlayerService playerSvc = new PlayerService();
	        				playerSvc.GetPlayerFromServer(context, playerId);
	        				

	        			//	Intent goToMainActivity = new Intent(getApplicationContext(), MainLanding.class);
	        			//	startActivity(goToMainActivity);
	        			//	finish();
    					}
    					else{
    						Intent goToWelcomeActivity = new Intent(getApplicationContext(), Welcome.class);
	        				startActivity(goToWelcomeActivity);
	        				finish();
    						
    					}
	        		//}
	        		
	        	//}
	       // };
      // setup.start();
        }
    
     }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
    
    
   
}
 
