package com.riotapps.word.services;

import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WordLoaderService extends Service {
	private static final String TAG = WordLoaderService.class.getSimpleName();
	
	public long runningTime = System.nanoTime();
	public long captureTime = System.nanoTime();
	private boolean wordsLoaded = false;
	
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
		   appContext.getWordService().isWordValid("aaa");
			  
		   this.captureTime("letter a - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("b");
		   appContext.getWordService().isWordValid("bbb");
		   this.captureTime("letter b - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("c");
		   appContext.getWordService().isWordValid("ccc");
			 
		   this.captureTime("letter c - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   
		   appContext.getWordService().loadList("d");
		   appContext.getWordService().isWordValid("ddd");
			 
		   this.captureTime("letter d - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("e");
		   appContext.getWordService().isWordValid("eee");
			 
		   this.captureTime("letter e - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("f");
		   appContext.getWordService().isWordValid("fff");
			 
		   this.captureTime("letter f - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("g");
		   appContext.getWordService().isWordValid("ggg");
			 
		   this.captureTime("letter g - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("h");
		   appContext.getWordService().isWordValid("hhh");
			 
		   this.captureTime("letter h - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("i");
		   appContext.getWordService().isWordValid("iii");
		   this.captureTime("letter i - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("j");
		   appContext.getWordService().isWordValid("jjj");
		   this.captureTime("letter j - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("k");
		   appContext.getWordService().isWordValid("kkk");
		   this.captureTime("letter k - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("l");
		   appContext.getWordService().isWordValid("lll");
		   this.captureTime("letter l - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("m");
		   appContext.getWordService().isWordValid("mmm");
		   this.captureTime("letter m - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("n");
		   appContext.getWordService().isWordValid("nnn");
		   this.captureTime("letter n - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("o");
		   appContext.getWordService().isWordValid("ooo");
		   this.captureTime("letter o - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("p");
		   appContext.getWordService().isWordValid("ppp");
		   this.captureTime("letter p - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("q");
		   appContext.getWordService().isWordValid("qqq");
		   this.captureTime("letter q - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("r");
		   appContext.getWordService().isWordValid("rrr");
		   this.captureTime("letter r - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("s");
		   appContext.getWordService().isWordValid("sss");
		   this.captureTime("letter s - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("t");
		   appContext.getWordService().isWordValid("ttt");
		   this.captureTime("letter t - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("u");
		   appContext.getWordService().isWordValid("uuu");
		   this.captureTime("letter u - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("v");
		   appContext.getWordService().isWordValid("vvv");
		   this.captureTime("letter v - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("w");
		   appContext.getWordService().isWordValid("www");
		   this.captureTime("letter w - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("x");
		   appContext.getWordService().isWordValid("xxx");
		   this.captureTime("letter x - loaded");
		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
		   
		   appContext.getWordService().loadList("y");
		   appContext.getWordService().isWordValid("yyy");
			this.captureTime("letter y - loaded");
			Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
			   
			appContext.getWordService().loadList("z");
			 appContext.getWordService().isWordValid("zzz");
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
