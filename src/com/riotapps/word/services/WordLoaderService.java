package com.riotapps.word.services;

import com.riotapps.word.hooks.GameService;
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
        	//   ApplicationContext appContext = (ApplicationContext)getApplicationContext();
        	   try{
        		   WordService.isWordValid("aaa"); 
        		  // appContext.getWordService().isWordValid("aaa");     			  
        		   captureTime("letter a - loaded");
        		   
        		   WordService.isWordValid("bbb");
        		   //appContext.getWordService().isWordValid("bbb");
        		   captureTime("letter b - loaded");
        		   
        		   WordService.isWordValid("ccc");
        		  // appContext.getWordService().isWordValid("ccc");     			  
        		   captureTime("letter c - loaded");
        		   
        		   WordService.isWordValid("ddd");
        		 //  appContext.getWordService().isWordValid("ddd");
        		   captureTime("letter d - loaded");
        		   
        		   WordService.isWordValid("eee");
        		 //  appContext.getWordService().isWordValid("eee");     			  
        		   captureTime("letter e - loaded");
        		   
        		   WordService.isWordValid("fff");
        		//  appContext.getWordService().isWordValid("fff");
        		   captureTime("letter f - loaded");
        		   
        		   WordService.isWordValid("gg");
        		//   appContext.getWordService().isWordValid("ggg");     			  
        		   captureTime("letter g - loaded");
        		   
        		   WordService.isWordValid("hhh");
        		   captureTime("letter h - loaded");
        		   
        		   WordService.isWordValid("iii");
        		 //  appContext.getWordService().isWordValid("iii");     			  
        		   captureTime("letter i - loaded");
        		   
        		   WordService.isWordValid("jjj");
        		  // appContext.getWordService().isWordValid("jjj");
        		   captureTime("letter j - loaded");
        		   
        		   WordService.isWordValid("kkk");
        		//   appContext.getWordService().isWordValid("kkk");     			  
        		   captureTime("letter k - loaded");
        		   
        		   WordService.isWordValid("lll");
        		//   appContext.getWordService().isWordValid("lll");
        		   captureTime("letter l - loaded");
        		   
        		   WordService.isWordValid("mmm");
        		 ///  appContext.getWordService().isWordValid("mmm");     			  
        		   captureTime("letter m - loaded");
        		 
        		   WordService.isWordValid("nnn");
        		 //  appContext.getWordService().isWordValid("nnn");
        		   captureTime("letter n - loaded");
        		   
        		   WordService.isWordValid("ooo");
        		 //  appContext.getWordService().isWordValid("ooo");     			  
        		   captureTime("letter o - loaded");
        		   
        		   WordService.isWordValid("ppp");
        		   //appContext.getWordService().isWordValid("ppp");
        		   captureTime("letter p - loaded");
        		   
        		   WordService.isWordValid("qqq");
        		   //appContext.getWordService().isWordValid("qqq");     			  
        		   captureTime("letter q - loaded");
        		   
        		   WordService.isWordValid("rrr");
        		 ///  appContext.getWordService().isWordValid("rrr");
        		   captureTime("letter r - loaded");
        		   
        		   WordService.isWordValid("sss");
        		//   appContext.getWordService().isWordValid("sss");     			  
        		   captureTime("letter s - loaded");
        		   
        		   WordService.isWordValid("ttt");
        		  // appContext.getWordService().isWordValid("ttt");
        		   captureTime("letter t - loaded");
        		   
        		   WordService.isWordValid("uuu");
        		 // appContext.getWordService().isWordValid("uuu");     			  
        		   captureTime("letter u - loaded");
        		   
        		   WordService.isWordValid("vvv");
        		///   appContext.getWordService().isWordValid("vvv");
        		   captureTime("letter v - loaded");
         		   
        		   WordService.isWordValid("www");
        		//   appContext.getWordService().isWordValid("www");
        		   captureTime("letter w - loaded");
        		   
        		   WordService.isWordValid("xxx");
        		///  appContext.getWordService().isWordValid("xxx");     			  
        		   captureTime("letter x- loaded");
        		   
        		   WordService.isWordValid("yyy");
        	///	   appContext.getWordService().isWordValid("yyy");
        		   captureTime("letter y - loaded");
        		
        		   WordService.isWordValid("zzz");
        		///   appContext.getWordService().isWordValid("zzz");     			  
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
