package com.riotapps.word.hooks;

import android.content.Context;
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
		Toast t = Toast.makeText(ctx, "HandleCreatePlayerResponse", Toast.LENGTH_LONG);  
        t.show();  
	}
}
