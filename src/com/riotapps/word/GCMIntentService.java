package com.riotapps.word;

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;
import com.riotapps.word.utils.Logger;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String TAG = GCMIntentService.class.getSimpleName();
	
	//Called when the device tries to register or unregister, but GCM returned an error. 
	//Typically, there is nothing to be done other than evaluating the error 
	//(returned by errorId) and trying to fix the problem.
	@Override
	protected void onError(Context context, String regId) {
		// TODO Auto-generated method stub
		Logger.d(TAG, "onError called regId=" + regId);
		
	}

	//Called when your server sends a message to GCM, and GCM delivers it to the device. 
	//If the message has a payload, its contents are available as extras in the intent.
	@Override
	protected void onMessage(Context context, Intent intent) {
		Logger.d(TAG, "onMessage called");		
	}

	//Called after a registration intent is received, passes the registration ID 
	//assigned by GCM to that device/application pair as parameter. 
	//Typically, you should send the regid to your server so it can use it to send messages to this device.
	@Override
	protected void onRegistered(Context context, String regId) {
		Logger.d(TAG, "onRegistered called regId=" + regId);	
		
	}

	//Called after the device has been unregistered from GCM. 
	//Typically, you should send the regid to the server so it unregisters the device.
	@Override
	protected void onUnregistered(Context context, String regId) {
		Logger.d(TAG, "onUnregistered called regId=" + regId);
		
	}
	
	//Called when the device tries to register or unregister, but the GCM servers are unavailable. 
	//The GCM library will retry the operation using exponential backup,
	//unless this method is overridden and returns false. This method is optional and should be overridden 
	//only if you want to display the message to the user or cancel the retry attempts.
	@Override
	protected boolean onRecoverableError(Context context, String errorId){
		Logger.d(TAG, "onRecoverableError called errorId=" + errorId);
		return true;
	}
	
}
