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
        		   appContext.getWordService().isWordValid("aaa");     			  
        		   captureTime("letter a - loaded");
        		   
        		   appContext.getWordService().isWordValid("bbb");
        		   captureTime("letter b - loaded");
        		   
        		   appContext.getWordService().isWordValid("ccc");     			  
        		   captureTime("letter c - loaded");
        		   
        		   appContext.getWordService().isWordValid("ddd");
        		   captureTime("letter d - loaded");
        		   
        		   appContext.getWordService().isWordValid("eee");     			  
        		   captureTime("letter e - loaded");
        		   
        		   appContext.getWordService().isWordValid("fff");
        		   captureTime("letter f - loaded");
        		   
        		   appContext.getWordService().isWordValid("ggg");     			  
        		   captureTime("letter g - loaded");
        		   
        		   appContext.getWordService().isWordValid("hhh");
        		   captureTime("letter h - loaded");
        		   
        		   appContext.getWordService().isWordValid("iii");     			  
        		   captureTime("letter i - loaded");
        		   
        		   appContext.getWordService().isWordValid("jjj");
        		   captureTime("letter j - loaded");
        		   
        		   appContext.getWordService().isWordValid("kkk");     			  
        		   captureTime("letter k - loaded");
        		   
        		   appContext.getWordService().isWordValid("lll");
        		   captureTime("letter l - loaded");
        		   
        		   appContext.getWordService().isWordValid("mmm");     			  
        		   captureTime("letter m - loaded");
        		   
        		   appContext.getWordService().isWordValid("nnn");
        		   captureTime("letter n - loaded");
        		   
        		   appContext.getWordService().isWordValid("ooo");     			  
        		   captureTime("letter o - loaded");
        		   
        		   appContext.getWordService().isWordValid("ppp");
        		   captureTime("letter p - loaded");
        		   
        		   appContext.getWordService().isWordValid("qqq");     			  
        		   captureTime("letter q - loaded");
        		   
        		   appContext.getWordService().isWordValid("rrr");
        		   captureTime("letter r - loaded");
        		   
        		   appContext.getWordService().isWordValid("sss");     			  
        		   captureTime("letter s - loaded");
        		   
        		   appContext.getWordService().isWordValid("ttt");
        		   captureTime("letter t - loaded");
        		   
        		   appContext.getWordService().isWordValid("uuu");     			  
        		   captureTime("letter u - loaded");
        		   
        		   appContext.getWordService().isWordValid("vvv");
        		   captureTime("letter v - loaded");
         		   
        		   appContext.getWordService().isWordValid("www");
        		   captureTime("letter w - loaded");
        		   
        		   appContext.getWordService().isWordValid("xxx");     			  
        		   captureTime("letter x- loaded");
        		   
        		   appContext.getWordService().isWordValid("yyy");
        		   captureTime("letter y - loaded");
        		   
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
