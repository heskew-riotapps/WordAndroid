package com.riotapps.word.utils;

import com.riotapps.word.hooks.WordService;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;

public class ApplicationContext extends Application{

    private static Context context;
    private WordService wordService;

    public void onCreate(){
        super.onCreate();
        ApplicationContext.context = getApplicationContext();
        this.wordService = new WordService(getApplicationContext());
        
       // new Thread(new Runnable() {
       //     public void run() {
       //     	wordService.loadAll();
       //     }
       // }).start();
    }

    public static Context getAppContext() {
        return ApplicationContext.context;
    }

	public WordService getWordService() {
		return wordService;
	}

	public void setWordService(WordService wordService) {
		this.wordService = wordService;
	}
    
    
}
