package com.riotapps.word;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gcm.GCMBaseIntentService;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String TAG = GCMIntentService.class.getSimpleName();
	
	public String gameId = "";
	
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
		Logger.d(TAG, "onMessage called. data=" + intent.getStringExtra("data")  + "gameId=" + intent.getStringExtra(Constants.EXTRA_GCM_GAME_ID) + " msg=" + intent.getStringExtra(Constants.EXTRA_GCM_MESSAGE));		
		
	    this.gameId = intent.getStringExtra(Constants.EXTRA_GCM_GAME_ID);
		this.sendMessage(context, intent.getStringExtra(Constants.EXTRA_GCM_GAME_ID), intent.getStringExtra(Constants.EXTRA_GCM_MESSAGE));
	}

	//Called after a registration intent is received, passes the registration ID 
	//assigned by GCM to that device/application pair as parameter. 
	//Typically, you should send the regid to your server so it can use it to send messages to this device.
	@Override
	protected void onRegistered(Context context, String regId) {
		Logger.d(TAG, "onRegistered called regId=" + regId);	
		PlayerService.updateRegistrationId(context, regId);
	}

	//Called after the device has been unregistered from GCM. 
	//Typically, you should send the regid to the server so it unregisters the device.
	@Override
	protected void onUnregistered(Context context, String regId) {
		Logger.d(TAG, "onUnregistered called regId=" + regId);
		PlayerService.updateRegistrationId(context, "");
		
	}
	
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     
    public class LocalBinder extends Binder {
    	GCMIntentService getService() {
            return GCMIntentService.this;
        }
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

	
	//Called when the device tries to register or unregister, but the GCM servers are unavailable. 
	//The GCM library will retry the operation using exponential backup,
	//unless this method is overridden and returns false. This method is optional and should be overridden 
	//only if you want to display the message to the user or cancel the retry attempts.
	@Override
	protected boolean onRecoverableError(Context context, String errorId){
		Logger.d(TAG, "onRecoverableError called errorId=" + errorId);
		return true;
	}
	
	*/ 
	private void sendMessage(Context context, String gameId, String message){
		//"message_text"
		//only send message if user is connected to wordsmash
	
		
		SharedPreferences settings = this.getSharedPreferences(Constants.USER_PREFS, Context.MODE_MULTI_PROCESS);
	    String storedToken = settings.getString(Constants.USER_PREFS_AUTH_TOKEN, "");
	       	    
	    if (storedToken.length() > 0){
			if (message == null){
				message = "message is null again";
				gameId = "123";
			}
			
			if (message != null){
				NotificationCompat.Builder mBuilder =
				        new NotificationCompat.Builder(this)
				        .setSmallIcon(R.drawable.status_icon)
				        .setContentTitle(context.getString(R.string.app_name))
				        .setLargeIcon( BitmapFactory.decodeResource(getResources(), R.drawable.icon_launcher))
				        .setContentText(message);
				// Creates an explicit intent for an Activity in your app
				Intent resultIntent = new Intent(this, Splash.class);
				resultIntent.putExtra(Constants.EXTRA_GAME_ID, gameId);
				
				// The stack builder object will contain an artificial back stack for the
				// started Activity.
				// This ensures that navigating backward from the Activity leads out of
				// your application to the Home screen.
				TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
				// Adds the back stack for the Intent (but not the Intent itself)
				stackBuilder.addParentStack(Splash.class);
				// Adds the Intent that starts the Activity to the top of the stack
				stackBuilder.addNextIntent(resultIntent);
				PendingIntent resultPendingIntent =
				        stackBuilder.getPendingIntent(
				            0,
				            PendingIntent.FLAG_UPDATE_CURRENT
				        );
				mBuilder.setContentIntent(resultPendingIntent);
				NotificationManager mNotificationManager =
				    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				// mId allows you to update the notification later on.
				mNotificationManager.notify(1, mBuilder.build());
			}
	    }
	    
		
		Intent intent = new Intent(Constants.INTENT_GCM_MESSAGE_RECEIVED);
		intent.putExtra(Constants.EXTRA_GAME_ID, gameId);
	    this.sendBroadcast(intent);
	}
	
}
