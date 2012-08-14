package com.riotapps.word.hooks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

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
import com.riotapps.word.utils.DialogManager;
import com.riotapps.word.utils.Enums.*;
import com.riotapps.word.utils.NetworkConnectivity;
import com.riotapps.word.utils.Validations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
 

public class PlayerService {
	
	Gson gson = new Gson();
	NetworkConnectivity connection = new NetworkConnectivity(ApplicationContext.getAppContext());
	
	public void GetPlayerFromServer(Context ctx, String id){
		//retrieve player from server
		//convert using gson
		//return player to caller
		String url = String.format(Constants.REST_GET_PLAYER_URL,id);
		new AsyncNetworkRequest(ctx, RequestType.GET, ResponseHandlerType.GET_PLAYER, ctx.getString(R.string.progress_syncing)).execute(url);
	}
	
	
	public Player SavePlayer(String id){
		//retrieve player from server
		//convert using gson
		//return player to caller
		
		//check validations here
		
		return new Player();
	}
	
	
	public void CreatePlayer(Context ctx, String email, String nickname, String password) throws DesignByContractException{

		//are we connected to the web?
		Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
		Check.Require(email.length() > 0, ctx.getString(R.string.validation_email_required));
		Check.Require(Validations.ValidateEmail(email) == true, ctx.getString(R.string.validation_email_invalid));
	 
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
		new AsyncNetworkRequest(ctx, RequestType.POST, ResponseHandlerType.CREATE_PLAYER, ctx.getString(R.string.progress_saving), json).execute(Constants.REST_CREATE_PLAYER_URL);
		
		//return player;
	}
	
	public Player GetPlayerFromLocal(){
		 Gson gson = new Gson(); 
		 Type type = new TypeToken<Player>() {}.getType();
	     SharedPreferences settings = ApplicationContext.getAppContext().getSharedPreferences(Constants.USER_PREFS, 0);
	     Player player = gson.fromJson(settings.getString(Constants.USER_PREFS_PLAYER_JSON, Constants.EMPTY_JSON), type);
	     return player;
	}
	
	public void HandleCreatePlayerResponse(final Context ctx, InputStream iStream){
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
            Log.w(getClass().getSimpleName(), "Error for HandleCreatePlayerResponse= ", e);
            
            Toast t = Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);  //change this to real error handling
            t.show(); 
         }
	 
	}
	
	public void HandleGetPlayerResponse(final Context ctx, InputStream iStream){
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
	 	        
 	        Intent goToMainLanding = new Intent(ctx, com.riotapps.word.TestLanding.class);
 	      	ctx.startActivity(goToMainLanding);
 	      	
 	       //redirect to game landing page
 	       
 	       //Toast t = Toast.makeText(ctx, response.getAuthToken(), Toast.LENGTH_LONG);  
 	       // t.show(); 
            
         } 
         catch (Exception e) {
            //getRequest.abort();
            Log.w(getClass().getSimpleName(), "Error for HandleCreatePlayerResponse= ", e);
            
            DialogManager.SetupAlert(ApplicationContext.getAppContext(), "HandleCreatePlayerResponse", e.getMessage());
           // Toast t = Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);  //change this to real error handling
           // t.show(); 
         }
	 
	}
}
