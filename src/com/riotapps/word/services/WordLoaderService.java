package com.riotapps.word.services;

import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WordLoaderService extends Service {
	private static final String TAG = WordLoaderService.class.getSimpleName();
	
	public long runningTime = System.nanoTime();
	public long captureTime = System.nanoTime();
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
		  // appContext.getWordService().loadAll();
		   appContext.getWordService().loadList("a");
		   this.captureTime("letter a - loaded");
		   appContext.getWordService().loadList("b");
		   this.captureTime("letter b - loaded");
		   appContext.getWordService().loadList("c");
		   this.captureTime("letter c - loaded");
		   appContext.getWordService().loadList("d");
		   this.captureTime("letter d - loaded");
		   appContext.getWordService().loadList("e");
		   this.captureTime("letter e - loaded");
		   appContext.getWordService().loadList("f");
		   this.captureTime("letter f - loaded");
		   appContext.getWordService().loadList("g");
		   this.captureTime("letter g - loaded");
		   appContext.getWordService().loadList("h");
		   this.captureTime("letter h - loaded");
		   appContext.getWordService().loadList("i");
		   this.captureTime("letter i - loaded");
		   appContext.getWordService().loadList("j");
		   this.captureTime("letter j - loaded");
		   appContext.getWordService().loadList("k");
		   this.captureTime("letter k - loaded");
		   appContext.getWordService().loadList("l");
		   this.captureTime("letter l - loaded");
		   appContext.getWordService().loadList("m");
		   this.captureTime("letter m - loaded");
		   appContext.getWordService().loadList("n");
		   this.captureTime("letter n - loaded");
		   appContext.getWordService().loadList("o");
		   this.captureTime("letter o - loaded");
		   appContext.getWordService().loadList("p");
		   this.captureTime("letter p - loaded");
		   appContext.getWordService().loadList("q");
		   this.captureTime("letter q - loaded");
		   appContext.getWordService().loadList("r");
		   this.captureTime("letter r - loaded");
		   appContext.getWordService().loadList("s");
		   this.captureTime("letter s - loaded");
		   appContext.getWordService().loadList("t");
		   this.captureTime("letter t - loaded");
		   appContext.getWordService().loadList("u");
		   this.captureTime("letter u - loaded");
		   appContext.getWordService().loadList("v");
		   this.captureTime("letter v - loaded");
		   appContext.getWordService().loadList("w");
		   this.captureTime("letter w - loaded");
		   appContext.getWordService().loadList("x");
		   this.captureTime("letter x - loaded");
			appContext.getWordService().loadList("y");
			this.captureTime("letter y - loaded");
			appContext.getWordService().loadList("z");
			this.captureTime("letter z - loaded");
	   }
	   catch (Exception e){
		   Logger.d(TAG, e.toString());
	   }
	   Logger.d(TAG, "all words loaded");
	   
	   this.stopSelf();
	   return START_STICKY;
   }
   
	private void captureTime(String text){
	     this.captureTime = System.nanoTime();
	     Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", text, Utils.convertNanosecondsToMilliseconds(this.captureTime - this.runningTime)));
	     this.runningTime = this.captureTime;

	}
}
