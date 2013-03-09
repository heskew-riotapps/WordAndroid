package com.riotapps.word.services;

import com.riotapps.word.hooks.WordService;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class WordLoaderService extends Service {
	private static final String TAG = WordLoaderService.class.getSimpleName();
	
	public long runningTime = System.nanoTime();
	public long captureTime = System.nanoTime();
	private boolean wordsLoaded = false;
	private LoadWordsTask runningTask;
	private boolean isProcessing = false;
	
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
	   this.runningTask = null;
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      //code to execute when the service is starting up
	   Logger.d(TAG, "onStartCommand called");
	
	   if (!isProcessing){
		   this.isProcessing = true;
		   this.runningTask = new LoadWordsTask();
		   this.runningTask.execute("");
	   }
	  // this.stopSelf();
	   return START_STICKY;
   }
   
	private void captureTime(String text){
	     this.captureTime = System.nanoTime();
	     Logger.d(TAG, String.format("%1$s - time since last capture=%2$s", text, Utils.convertNanosecondsToMilliseconds(this.captureTime - this.runningTime)));
	     this.runningTime = this.captureTime;

	}
	

	 private class LoadWordsTask extends AsyncTask<String, Void, String> {

         @Override
         protected String doInBackground(String... params) {
        	   ApplicationContext appContext = (ApplicationContext)getApplicationContext();
        	   try{
        		  // appContext.getWordService().loadAll();
        		  // appContext.getWordService().loadList("a");
        		  // appContext.getWordService().isWordValid("aaa");
        		   
        		   WordService.isWordValid("aaa");
        			  
        		   captureTime("letter a - loaded");
        		   //Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   //appContext.getWordService().loadList("b");
        		   //appContext.getWordService().isWordValid("bbb");
        		   WordService.isWordValid("bbb");

        		   captureTime("letter b - loaded");
        		   //Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("c");
        		   appContext.getWordService().isWordValid("ccc");
        			 
        		   captureTime("letter c - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   
        		   appContext.getWordService().loadList("d");
        		   appContext.getWordService().isWordValid("ddd");
        			 
        		   captureTime("letter d - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("e");
        		   appContext.getWordService().isWordValid("eee");
        			 
        		   captureTime("letter e - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("f");
        		   appContext.getWordService().isWordValid("fff");
        			 
        		   captureTime("letter f - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("g");
        		   appContext.getWordService().isWordValid("ggg");
        			 
        		   captureTime("letter g - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("h");
        		   appContext.getWordService().isWordValid("hhh");
        			 
        		   captureTime("letter h - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("i");
        		   appContext.getWordService().isWordValid("iii");
        		   captureTime("letter i - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("j");
        		   appContext.getWordService().isWordValid("jjj");
        		   captureTime("letter j - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("k");
        		   appContext.getWordService().isWordValid("kkk");
        		   captureTime("letter k - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("l");
        		   appContext.getWordService().isWordValid("lll");
        		   captureTime("letter l - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("m");
        		   appContext.getWordService().isWordValid("mmm");
        		   captureTime("letter m - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("n");
        		   appContext.getWordService().isWordValid("nnn");
        		   captureTime("letter n - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("o");
        		   appContext.getWordService().isWordValid("ooo");
        		   captureTime("letter o - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("p");
        		   appContext.getWordService().isWordValid("ppp");
        		   captureTime("letter p - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("q");
        		   appContext.getWordService().isWordValid("qqq");
        		   captureTime("letter q - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("r");
        		   appContext.getWordService().isWordValid("rrr");
        		   captureTime("letter r - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("s");
        		   appContext.getWordService().isWordValid("sss");
        		   captureTime("letter s - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("t");
        		   appContext.getWordService().isWordValid("ttt");
        		   captureTime("letter t - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("u");
        		   appContext.getWordService().isWordValid("uuu");
        		   captureTime("letter u - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("v");
        		   appContext.getWordService().isWordValid("vvv");
        		   captureTime("letter v - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("w");
        		   appContext.getWordService().isWordValid("www");
        		   captureTime("letter w - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("x");
        		   appContext.getWordService().isWordValid("xxx");
        		   captureTime("letter x - loaded");
        		   Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        		   
        		   appContext.getWordService().loadList("y");
        		   appContext.getWordService().isWordValid("yyy");
        			captureTime("letter y - loaded");
        			Thread.sleep(Constants.WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS);
        			   
        			appContext.getWordService().loadList("z");
        			 appContext.getWordService().isWordValid("zzz");
        			captureTime("letter z - loaded");
        	   }
        	   catch (Exception e){
        		   Logger.d(TAG, e.toString());
        	   }
        	   Logger.d(TAG, "all words loaded");
               return "Executed";
         }      

         @Override
         protected void onPostExecute(String result) {
        	  
        	 stopSelf();
         }

         @Override
         protected void onPreExecute() {
         }

         @Override
         protected void onProgressUpdate(Void... values) {
         }
   }
}
