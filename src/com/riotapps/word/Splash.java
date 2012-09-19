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
        
///do this check in separate thread
        
        
        NetworkConnectivity connection = new NetworkConnectivity(this);
        //are we connected to the web?
        boolean isConnected = connection.checkNetworkConnectivity();
        
        if (isConnected == false)  
        {
        	try {
				Thread.sleep(3000);
				isConnected = connection.checkNetworkConnectivity();
				
				if (isConnected == false) { 
				 	
					//change this to more specific dialog with button that goes to a page that allows offline usage
					DialogManager.SetupAlert(context, getString(R.string.oops), getString(R.string.msg_not_connected), true, 0);					
				}
        	} 
        	catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        
        if (isConnected == true) 
        {

        	
        	Intent goToMainLanding = new Intent(this, com.riotapps.word.TestLanding.class);
 	      	this.startActivity(goToMainLanding);
 	      	
//			SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);
			
//			String auth_token = settings.getString(Constants.USER_PREFS_AUTH_TOKEN, "");
//			String playerId = settings.getString(Constants.USER_PREFS_USER_ID, "");



	 	       
//			if (auth_token.length() > 0) {
//				//get player from rails server
//				PlayerService playerSvc = new PlayerService();
//				playerSvc.GetPlayerFromServer(context, playerId);
				
//			}
//			else{
//				Intent goToWelcomeActivity = new Intent(getApplicationContext(), Welcome.class);
//				startActivity(goToWelcomeActivity);
//				finish();
				
//			}
        }
    
     }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
    
    
   
}
 
