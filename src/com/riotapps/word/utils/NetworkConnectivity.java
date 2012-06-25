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

	     ConnectivityManager connec = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);   

	     boolean isMobileNetworkConnected = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();   

	     boolean isWiFiNetworkConnected = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();  

	     return (isMobileNetworkConnected || isWiFiNetworkConnected);  

	 }//end method checkNetworkConnectivity

}
