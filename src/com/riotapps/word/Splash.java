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
import android.os.Handler;
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
    Handler handler;
    
    public void test(){}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
     //   handler = new Handler();
        
///do this check in separate thread
        
        
        new CheckConnectivityTask().execute("");
     }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	private class CheckConnectivityTask extends AsyncTask<String, Void, Boolean> {

	
	  
		 @Override
		    protected void onPostExecute(Boolean result) {
			 processTaskResults(result);
		    }

			@Override
			protected Boolean doInBackground(String... arg0) {
			 
				return checkInitialConnectivity();
			}
	  }

	
	private Boolean checkInitialConnectivity(){
		  NetworkConnectivity connection = new NetworkConnectivity(this);
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
	        
	       return isConnected;
	}
	
	private void processTaskResults(Boolean connected){
		 if (connected == true) 
	        {
	        	Intent intent = new Intent(this, com.riotapps.word.TestLanding.class);
	 	      	this.startActivity(intent); 	
	        }
		 else{
			 DialogManager.SetupAlert(context, getString(R.string.oops), getString(R.string.msg_not_connected), true, 0);
		 }
	}

   
}
 
