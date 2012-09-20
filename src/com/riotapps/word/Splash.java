package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.utils.*;
import com.riotapps.word.utils.Enums.RequestType;

public class Splash  extends Activity {
    /** Called when the activity is first created. */
	
    final Context context = this;
    Splash me = this;
    
    public void test(){}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
///do this check in separate thread
        
        
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
              me.checkInitialConnectivity();
        
            }
        };
        
        new Thread(runnable).start();
     }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
    
	
	private void checkInitialConnectivity(){
		  NetworkConnectivity connection = new NetworkConnectivity(this);
	        //are we connected to the web?
	        boolean isConnected = connection.checkNetworkConnectivity();
	        
	        if (isConnected == false)  
	        {
	        	try {
					Thread.sleep(Constants.INITIAL_CONNECTIVITY_THREAD_SLEEP);
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
	        	Intent intent = new Intent(this, com.riotapps.word.TestLanding.class);
	 	      	this.startActivity(intent); 	
	        }
	}

   
}
 
