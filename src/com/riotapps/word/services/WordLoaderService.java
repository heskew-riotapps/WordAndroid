package com.riotapps.word.services;

import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Logger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WordLoaderService extends Service {
	private static final String TAG = WordLoaderService.class.getSimpleName();
   @Override
   public IBinder onBind(Intent intent) {
      return null;
   }

   @Override
   public void onCreate() {
      //code to execute when the service is first created
	   Logger.d(TAG, "onCreate called");
	 
   }

   @Override
   public void onDestroy() {
      //code to execute when the service is shutting down
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      //code to execute when the service is starting up
	   Logger.d(TAG, "onStartCommand called");
	   ApplicationContext appContext = (ApplicationContext)this.getApplicationContext();
	   try{
		   appContext.getWordService().loadAll();
	   }
	   catch (Exception e){
		   Logger.d(TAG, e.toString());
	   }
	   Logger.d(TAG, "all words loaded");
	   
	   this.stopSelf();
	   return START_STICKY;
   }
}
