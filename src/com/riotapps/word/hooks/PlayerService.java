package com.riotapps.word.hooks;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.riotapps.word.R;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.Check;
import com.riotapps.word.utils.Enums.*;
import com.riotapps.word.utils.NetworkConnectivity;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Validations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
 

public class PlayerService {
	
	Gson gson = new Gson();
	NetworkConnectivity connection = new NetworkConnectivity(ApplicationContext.getAppContext());
	
	public Player GetPlayer(String id){
		//retrieve player from server
		//convert using gson
		//return player to caller
		return new Player();
	}
	
	
	public Player SavePlayer(String id){
		//retrieve player from server
		//convert using gson
		//return player to caller
		
		//check validations here
		
		return new Player();
	}
	
	
	public Player CreatePlayer(Context ctx, String email, String nickname, String password) throws DesignByContractException{
		//retrieve player from server
		//convert using gson
		//return player to caller
		//Player player = new Player();
	       
		
		//player.setEmail(email);
		//are we connected to the web?
		Check.Require(connection.checkNetworkConnectivity() == true, ApplicationContext.getAppContext().getString(R.string.msg_not_connected));
		Check.Require(email.length() > 0, ApplicationContext.getAppContext().getString(R.string.validation_email_required));
		Check.Require(Validations.ValidateEmail(email) == true, ApplicationContext.getAppContext().getString(R.string.validation_email_invalid));
	 
		Player player = new Player();
		player.setEmail(email);
		player.setNickname(nickname);
		player.setPassword(password);
		
		String json = gson.toJson(player);
		//ok lets call the server now
		
	//	  String shownOnProgressDialog = "progress test";//ctx.getString(R.string.progressDialogMessageSplashScreenRetrievingUserListing);
		  
		  new AsyncNetworkRequest(ctx, RequestType.POST, ResponseHandlerType.CREATE_PLAYER, "abcd", json).execute(Constants.REST_CREATE_PLAYER_URL);
		
		
		//check validations here
		
		return player;
	}
	
	public void HandleCreatePlayerResponse(final Context ctx, ServerResponse serverResponseObject){
  
	        try {
	            
	        	 Gson gson = new Gson(); //wrap json return into a single call that takes a type
	 	        
	 	        Reader reader = new InputStreamReader(serverResponseObject.response.getEntity().getContent());
	 	        
	 	        Type type = new TypeToken<Player>() {}.getType();
	 	        Player response = gson.fromJson(reader, type);
	 	        
	 	       Toast t = Toast.makeText(ctx, response.getAuthToken(), Toast.LENGTH_LONG);  
	 	        t.show(); 
	            
	         } 
	         catch (IOException e) {
	            //getRequest.abort();
	            Log.w(getClass().getSimpleName(), "Error for HandleCreatePlayerResponse ", e);
	            
	            Toast t = Toast.makeText(ctx, "oops", Toast.LENGTH_LONG);  //change this to real error handling
	            t.show(); 
	         }
	        catch (Exception e) {
	            //getRequest.abort();
	            Log.w(getClass().getSimpleName(), "Error for HandleCreatePlayerResponse= ", e);
	            
	            Toast t = Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);  //change this to real error handling
	            t.show(); 
	         }
		 
	}
}
