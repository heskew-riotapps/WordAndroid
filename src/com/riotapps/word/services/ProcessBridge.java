package com.riotapps.word.services;

import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class ProcessBridge extends Service {
	private static final String TAG = ProcessBridge.class.getSimpleName();
	
	private GameListReceiver gameListReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (this.gameListReceiver != null){
			this.unregisterReceiver(this.gameListReceiver);
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		this.gameListReceiver = new GameListReceiver();
		this.registerReceiver(this.gameListReceiver,new IntentFilter(Constants.INTENT_GAME_LIST_REFRESHED_TO_BRIDGE));
	}
	
	
	//to allow second process to communicate with main process
	private class GameListReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String result = intent.getStringExtra(Constants.EXTRA_PLAYER_AUTH_RESULT);
			
			 Player player = PlayerService.handleAuthByTokenResponse(result);
    		 
    	   	 Logger.d(TAG, "GameListReceiver onReceive active games=" + player.getActiveGamesYourTurn().size() + " opp games=" + player.getActiveGamesOpponentTurn().size());

    		 GameService.updateLastGameListCheckTime();
    		 
    		 Intent targetIntent = new Intent(Constants.INTENT_GAME_LIST_REFRESHED);
 		     sendBroadcast(targetIntent);
		}
		
	}
	
}
