package com.riotapps.word.hooks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.riotapps.word.R;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.Check;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.IOHelper;
import com.riotapps.word.utils.Enums.*;
import com.riotapps.word.utils.NetworkConnectivity;
import com.riotapps.word.utils.Validations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
 
 
public class GameService {
	
	
	public static void GetPlayerGameFromServer(Context ctx, String id, Class<?> goToClass){
		//retrieve player from server
		//convert using gson
		//return player to caller
		String url = String.format(Constants.REST_GET_PLAYER_URL,id);
		new AsyncNetworkRequest(ctx, RequestType.GET, ResponseHandlerType.GET_GAME, ctx.getString(R.string.progress_syncing)).execute(url);
	}
	
	
	public Game SaveGame(String id){
		//retrieve player from server
		//convert using gson
		//return player to caller
		
		//check validations here
		
		return new Game();
	}
	
//	public static List<Game> getGamesFromLocal(){
//		 Gson gson = new Gson(); 
//		 Type type = new TypeToken<List<Game>>() {}.getType();
//	     SharedPreferences settings = ApplicationContext.getAppContext().getSharedPreferences(Constants.USER_PREFS, 0);
//	     List<Game> games = gson.fromJson(settings.getString(Constants.USER_PREFS_ACTIVE_GAMES, Constants.EMPTY_JSON_ARRAY), type);
//	     return games;
//	}
	
	
	public static void CreateGame(Context ctx, String email, String nickname, String password, Class<?> goToClass) throws DesignByContractException{

		Gson gson = new Gson();
		NetworkConnectivity connection = new NetworkConnectivity(ApplicationContext.getAppContext());
		//are we connected to the web?
		Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
		Check.Require(email.length() > 0, ctx.getString(R.string.validation_email_required));
		Check.Require(Validations.validateEmail(email) == true, ctx.getString(R.string.validation_email_invalid));
	 
		Player player = new Player();
		player.setEmail(email);
		player.setNickname(nickname);
		player.setPassword(password);
		
		String json = gson.toJson(player);
		
	//	  String shownOnProgressDialog = "progress test";//ctx.getString(R.string.progressDialogMessageSplashScreenRetrievingUserListing);
		  
		SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(Constants.USER_PREFS_PWD, player.getPassword());
		editor.putString(Constants.USER_PREFS_EMAIL, player.getEmail());
		editor.commit();
		
		//ok lets call the server now
		new AsyncNetworkRequest(ctx, RequestType.POST, ResponseHandlerType.CREATE_PLAYER, ctx.getString(R.string.progress_saving), json, goToClass).execute(Constants.REST_CREATE_PLAYER_URL);
		
		//return player;
	}
	
	public static Player GetGameFromLocal(){
		 Gson gson = new Gson(); 
		 Type type = new TypeToken<Player>() {}.getType();
	     SharedPreferences settings = ApplicationContext.getAppContext().getSharedPreferences(Constants.USER_PREFS, 0);
	     Player player = gson.fromJson(settings.getString(Constants.USER_PREFS_PLAYER_JSON, Constants.EMPTY_JSON), type);
	     return player;
	}
	
	public static void HandleCreateGameResponse(final Context ctx, InputStream iStream){
        try {
            
        	 Gson gson = new Gson(); //wrap json return into a single call that takes a type
 	        
 	        Reader reader = new InputStreamReader(iStream); //serverResponseObject.response.getEntity().getContent());
 	        
 	        Type type = new TypeToken<Player>() {}.getType();
 	        Player player = gson.fromJson(reader, type);
 	        
 	        ///save player info to shared preferences
 	        //userId and auth_token ...email and password should have been stored before this call
 	       SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
 	       SharedPreferences.Editor editor = settings.edit();
 	       editor.putString(Constants.USER_PREFS_AUTH_TOKEN, player.getAuthToken());
 	       editor.putString(Constants.USER_PREFS_USER_ID, player.getId());
 	      editor.putString(Constants.USER_PREFS_PLAYER_JSON, gson.toJson(player));
 	       editor.commit();  
 	        
 	       Intent goToMainLanding = new Intent(ctx, com.riotapps.word.MainLanding.class);
	       ctx.startActivity(goToMainLanding);
 	       //Intent goToGamesLanding = new Intent(ctx, com.riotapps.word.GamesLanding.class);
		   //ctx.startActivity(goToGamesLanding);
 	       //redirect to game landing page
 	       
 	       //Toast t = Toast.makeText(ctx, response.getAuthToken(), Toast.LENGTH_LONG);  
 	       // t.show(); 
            
         } 
         catch (Exception e) {
            //getRequest.abort();
            Log.w("HandleCreateGameResponse", "Error for HandleCreateGameResponse= ", e);
            
            Toast t = Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);  //change this to real error handling
            t.show(); 
         }
	 
	}
	
	public static void HandleGetGameResponse(final Context ctx, InputStream iStream, Class<?> goToClass)
	{
        try {
            
        	 Gson gson = new Gson(); //wrap json return into a single call that takes a type
 	        
 	        Reader reader = new InputStreamReader(iStream); //serverResponseObject.response.getEntity().getContent());
 	        
 	        Type type = new TypeToken<Game>() {}.getType();
 	        Game game = gson.fromJson(reader, type);
 	       
 	        //save game data to  	        
 	       // String s = IOHelper.streamToString(iStream);
 	        
 	        //save game data to 
 	        
 	        ///save player info to shared preferences
 	        //userId and auth_token ...email and password should have been stored before this call
 	       // SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
 	       // SharedPreferences.Editor editor = settings.edit();
 	       // editor.putString(Constants.USER_PREFS_AUTH_TOKEN, player.getAuthToken());
 	       // editor.putString(Constants.USER_PREFS_USER_ID, player.getId());
 	       // editor.putString(Constants.USER_PREFS_PLAYER_JSON, gson.toJson(player));
 	       // editor.commit();  
	 	        
 	        Intent intent = new Intent(ctx, goToClass);
 	      //  intent.putExtra("gameId", game.getId());
 	      //	intent.putExtra("game", s);
 	      	intent.putExtra(Constants.EXTRA_GAME, game);
 	        ctx.startActivity(intent);
 	      	
 	       
 	      	
 	       //redirect to game landing page
 	       
 	       //Toast t = Toast.makeText(ctx, response.getAuthToken(), Toast.LENGTH_LONG);  
 	       // t.show(); 
            
         } 
         catch (Exception e) {
            //getRequest.abort();
            //Log.w(getClass().getSimpleName(), "Error for HandleCreatePlayerResponse= ", e);
            
            DialogManager.SetupAlert(ApplicationContext.getAppContext(), "HandleGetGameResponse", e.getMessage(), 0);
           // Toast t = Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);  //change this to real error handling
           // t.show(); 
         }
	 
	}
	
	public static Game createGame(Context ctx, Player contextPlayer) throws DesignByContractException{
		
		Check.Require(PlayerService.getPlayerFromLocal().getId().equals(contextPlayer.getId()), ctx.getString(R.string.validation_incorrect_context_player));
    	
		Game game = new Game();
    	
    	PlayerGame pg = new PlayerGame();
    	pg.setPlayerId(contextPlayer.getId());
    	pg.setPlayer(contextPlayer);
    	pg.setPlayerOrder(1);
    	
    	List<PlayerGame> pgs = new ArrayList<PlayerGame>();
    	pgs.add(pg);
    	game.setPlayerGames(pgs);
    	
    	return game;
	}
	
	public static Game addPlayerToGame(Context ctx, Game game, Player player) throws DesignByContractException{

		Check.Require(game.getPlayerGames().size() < 5, ctx.getString(R.string.validation_too_many_players));
		
		Player contextPlayer = PlayerService.getPlayerFromLocal();
		
	//	Check.Require(!contextPlayer.getId().equals(player.getId()), ctx.getString(R.string.validation_cannot_add_yourself));
		
    	PlayerGame pg = new PlayerGame();
    	pg.setPlayerId(player.getId());
    	pg.setPlayer(player);
    	pg.setPlayerOrder(game.getPlayerGames().size() + 1);
    	game.getPlayerGames().add(pg);
    	
    	return game;
	}
}
