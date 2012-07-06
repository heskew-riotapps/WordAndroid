package com.riotapps.word;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.riotapps.word.utils.*;

public class Splash  extends Activity {
    /** Called when the activity is first created. */
	
    final Context context = this;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        

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
					DialogManager.SetupOKDialog(context, getString(R.string.oops), getString(R.string.msg_not_connected));					

				 	
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
        
        if (isConnected == true)
        {
	        Thread setup = new Thread(){
	        	public void run(){
	        		try {
	        			sleep(3000);
	        		}
	        		catch (InterruptedException e){
	        			e.printStackTrace();
	        		}
	        		finally {
	        		//	Intent goToFirstActivity = new Intent(getApplicationContext(), Welcome.class);
	    			//	startActivity(goToFirstActivity);
	        		}
	        		
	        	}
	        };
       setup.start();
        }
    
     }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
    
    
   
}
 
