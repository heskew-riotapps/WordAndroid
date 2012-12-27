package com.riotapps.word.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectivity {

	Context ctx;

	public NetworkConnectivity(Context ctx){
		this.ctx = ctx;
	}

	/**=========================================================   
	  * method checkNetworkConnectivity()   
	  * =========================================================*/   
	public boolean checkNetworkConnectivity() {   

		//put thread sleep in here
		
	     ConnectivityManager connec = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);   
	     
	     
	   //  NetworkInfo activeNetworkInfo = connec.getActiveNetworkInfo();
	    // return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

	     
	     if (connec != null) {
            NetworkInfo[] info = connec.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                   // Log.w("INTERNET:",String.valueOf(i));
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    	return true;
                    }
                }
            }
	     }
	            
	     return false;       
	            
	            
	     
	     
	     
	     /*
	     boolean isMobileNetworkConnected = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();   

	     boolean isWiFiNetworkConnected = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();  

	     //try again in 2 seconds
	     if (!isMobileNetworkConnected && !isWiFiNetworkConnected){
	    	 try {
				Thread.sleep(Constants.NETWORK_CONNECTIVITY_CHECK_DURATION);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 connec = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);   

		     isMobileNetworkConnected = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();   

		     isWiFiNetworkConnected = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();  

	     }
	     
	    return (isMobileNetworkConnected || isWiFiNetworkConnected);  
	    */
	   
	 }//end method checkNetworkConnectivity

}
