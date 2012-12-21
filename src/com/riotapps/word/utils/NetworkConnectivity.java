package com.riotapps.word.utils;

import android.content.Context;
import android.net.ConnectivityManager;

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
	   
	 }//end method checkNetworkConnectivity

}
