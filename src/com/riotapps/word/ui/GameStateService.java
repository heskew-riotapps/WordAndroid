package com.riotapps.word.ui;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.android.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.FileUtils;

public class GameStateService {

	public static GameState getGameState(Context context, String gameId){
		SharedPreferences settings = context.getSharedPreferences(Constants.GAME_STATE, 0);
	    String gameStatejson = settings.getString(gameId, "");  
	    
	    GameState gameState;
	    
	    if (gameStatejson.length() == 0){
	    	gameState = new GameState();
			gameState.setGameId(gameId);
	    	return gameState;
	    }

	    Gson gson = new Gson();
		Type type = new TypeToken<GameState>() {}.getType();
		gameState = gson.fromJson(gameStatejson, type);
		
		//just in case
		gameState.setGameId(gameId);
		 return gameState;
	}
	
	public static GameState clearGameState(Context context, String gameId){
		 Gson gson = new Gson();
		 GameState gameState = new GameState();
		 gameState.setGameId(gameId);
		 
		 SharedPreferences settings = context.getSharedPreferences(Constants.GAME_STATE, 0);
		 SharedPreferences.Editor editor = settings.edit();
		 editor.putString(gameId, gson.toJson(gameState));
		 editor.commit();
		 
		 return gameState;
	}
	
	public static void removeGameState(Context context, String gameId){
		 Gson gson = new Gson();
		 GameState gameState = new GameState();
		 gameState.setGameId(gameId);
		 
		 SharedPreferences settings = context.getSharedPreferences(Constants.GAME_STATE, 0);
		 SharedPreferences.Editor editor = settings.edit();
		 editor.remove(gameId);
		 editor.commit();

	}
	
	public static void setGameState(Context context, GameState gameState){
		
		if (gameState != null && gameState.getGameId().length() > 0){
			 Gson gson = new Gson();
			 
			 SharedPreferences settings = context.getSharedPreferences(Constants.GAME_STATE, 0);
			 SharedPreferences.Editor editor = settings.edit();
			 editor.putString(gameState.getGameId(), gson.toJson(gameState));
			 editor.commit();
		}
	}
}
