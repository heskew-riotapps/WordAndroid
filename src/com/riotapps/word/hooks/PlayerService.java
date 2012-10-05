package com.riotapps.word.hooks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.riotapps.word.FindPlayer;
import com.riotapps.word.R;
 
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.Check;
import com.riotapps.word.utils.IOHelper;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Enums.*;
import com.riotapps.word.utils.NetworkConnectivity;
import com.riotapps.word.utils.Validations;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
 
////make this class statisc
public class PlayerService {
	private static final String TAG = PlayerService.class.getSimpleName();

	Gson gson = new Gson();
	NetworkConnectivity connection = new NetworkConnectivity(ApplicationContext.getAppContext());
	
	public static void GetPlayerFromServer(Context ctx, String id){
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
	
	
	public static String setupConnectViaEmail(Context ctx, String email, String nickname, String password) throws DesignByContractException{
		Gson gson = new Gson();
	
		NetworkConnectivity connection = new NetworkConnectivity(ApplicationContext.getAppContext());
		//are we connected to the web?
	 	Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
	 	//check funky characters in nickname [a-zA-Z0-9\-#\.\(\)\/%&\s]
		Check.Require(email.length() > 0, ctx.getString(R.string.validation_email_required));
		Check.Require(nickname.length() > 0, ctx.getString(R.string.validation_nickname_required));
		Check.Require(password.length() > 0, ctx.getString(R.string.validation_password_required));
		Check.Require(password.length() >= 6, ctx.getString(R.string.validation_password_too_short));
		Check.Require(Validations.validateEmail(email.trim()) == true, ctx.getString(R.string.validation_email_invalid));
		Check.Require(Validations.validateNickname(nickname.trim()) == true, ctx.getString(R.string.validation_nickname_invalid));
		
		TransportCreatePlayer player = new TransportCreatePlayer();
		player.setEmail(email);
		player.setNickname(nickname);
		player.setPassword(password);
		
		return gson.toJson(player);
 
	}
	
	public static String setupConnectViaFB(Context ctx, String fb, String email, String firstName, String lastName) throws DesignByContractException{
		Gson gson = new Gson();
	
		NetworkConnectivity connection = new NetworkConnectivity(ApplicationContext.getAppContext());
		//are we connected to the web?
	 	Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
	 	//check funky characters in nickname [a-zA-Z0-9\-#\.\(\)\/%&\s]
		Check.Require(email.length() > 0, ctx.getString(R.string.validation_email_required));
		Check.Require(Validations.validateEmail(email.trim()) == true, ctx.getString(R.string.validation_email_invalid));
		
		TransportCreateFBPlayer player = new TransportCreateFBPlayer();
		player.setEmail(email);
		player.setFb(fb);
		player.setFirstName(firstName);
		player.setLastName(lastName);
		
		return gson.toJson(player);
	}
	
	
	public static String setupAuthTokenCheck(Context ctx, String authToken) throws DesignByContractException{
		Gson gson = new Gson();
	
		NetworkConnectivity connection = new NetworkConnectivity(ApplicationContext.getAppContext());
		//are we connected to the web?
	 	Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
	 
		Check.Require(authToken.length() > 0, ctx.getString(R.string.validation_auth_token_required));
		 
		SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
	    String completedDate = settings.getString(Constants.USER_PREFS_LATEST_COMPLETED_GAME_DATE, Constants.DEFAULT_COMPLETED_GAMES_DATE);
		TransportAuthToken player = new TransportAuthToken();
		player.setToken(authToken);
		player.setCompletedGameDate(new Date(completedDate));

		
		return gson.toJson(player);
	}
	
	
	public static String setupAccountUpdate(Context ctx, String email, String nickname) throws DesignByContractException{
		Gson gson = new Gson(); 
	
		Player player = PlayerService.getPlayerFromLocal();
		NetworkConnectivity connection = new NetworkConnectivity(ctx);
		//are we connected to the web?
	 	Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
	 	//check funky characters in nickname [a-zA-Z0-9\-#\.\(\)\/%&\s]
		Check.Require(email.length() > 0, ctx.getString(R.string.validation_email_required));
		Check.Require(nickname.length() > 0, ctx.getString(R.string.validation_nickname_required));
		Check.Require(Validations.validateEmail(email.trim()) == true, ctx.getString(R.string.validation_email_invalid));
		Check.Require(Validations.validateNickname(nickname.trim()) == true, ctx.getString(R.string.validation_nickname_invalid));
		
		TransportUpdateAccount updateAccount = new TransportUpdateAccount();
		updateAccount.setEmail(email);
		updateAccount.setNickname(nickname);
		updateAccount.setToken(player.getAuthToken());
		
		return gson.toJson(updateAccount);
	}
	
	public static void saveFacebookFriendsFromJSONResponse(Context ctx, String response) throws FacebookError, JSONException{
		JSONObject json;
		 
		json = Util.parseJson(response);
		final JSONArray friends = json.getJSONArray("data");
		
		List<FBFriend> fbFriends = new ArrayList<FBFriend>();
		
		for(int i = 0 ; i < friends.length(); i++){
			FBFriend fbFriend = new FBFriend();
			
			JSONObject row = friends.getJSONObject(i);
			fbFriend.setId(row.getString("id"));
			fbFriend.setId(row.getString("name"));
			
			fbFriends.add(fbFriend);
		}
		Gson gson = new Gson(); 
		
		String friendJSON = gson.toJson(fbFriends);
		
		 SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
	        SharedPreferences.Editor editor = settings.edit();
	        
	        Logger.w(TAG, "saveFacebookFriendsFromJSONResponse fbFriends=" + friendJSON);
 
	        editor.putString(Constants.USER_PREFS_FRIENDS_JSON, gson.toJson(friendJSON));
	        editor.commit();  
	}
	
	
	public static String setupPasswordChange(Context ctx, String password, String passwordConfirm) throws DesignByContractException{
		Gson gson = new Gson();
	
		NetworkConnectivity connection = new NetworkConnectivity(ctx);
		//are we connected to the web?
		Player player = PlayerService.getPlayerFromLocal();
		
		password = password.trim();
		passwordConfirm = passwordConfirm.trim();
	//	Logger.d(TAG, "setupPasswordChange pwd=" + password + " confirm=" + passwordConfirm);
		
	 	Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
	  
	 	Check.Require(player.getAuthToken().length() > 0, ctx.getString(R.string.validation_email_required));
		Check.Require(password.trim().length() > 0, ctx.getString(R.string.validation_new_password_not_provided));
		Check.Require(password.trim().length() >= 6, ctx.getString(R.string.validation_password_too_short));
		Check.Require(password.equals(passwordConfirm), ctx.getString(R.string.validation_password_confirmation_failed));
	 
		TransportPasswordChange pwdChg = new TransportPasswordChange();
		pwdChg.setToken(player.getAuthToken());
		pwdChg.setPassword(password);
		
		return gson.toJson(pwdChg);
 
	}
	
	public static String setupAuthTokenTransport(Context ctx) throws DesignByContractException{
		Gson gson = new Gson();
	
		NetworkConnectivity connection = new NetworkConnectivity(ctx);
		//are we connected to the web?
	 	Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
	
		Player player = PlayerService.getPlayerFromLocal();
	 	
	 	Check.Require(player.getAuthToken().length() > 0, ctx.getString(R.string.msg_not_connected));
	 	
		TransportAuthToken token = new TransportAuthToken();
		token.setToken(player.getAuthToken());
		
		return gson.toJson(token);
	}
	
	public static void clearImageCache(FragmentActivity context){

		ImageCache cache = ImageCache.findOrCreateCache(context, Constants.IMAGE_CACHE_DIR);
		cache.clearCaches();
		
	}
	
	public static void clearLocalStorageAndCache(FragmentActivity context){
		context.getSharedPreferences(Constants.USER_PREFS, 0).edit().clear().commit();
		
		clearImageCache(context);
		
	}

	public static void logout(FragmentActivity context){
		clearLocalStorageAndCache(context);
		
        Intent intent = new Intent(context, com.riotapps.word.Welcome.class);
      	context.startActivity(intent);
		
	}
	
	
	public String setupFindPlayerByNickname(Context ctx, String nickname) throws DesignByContractException{
		nickname = nickname.trim(); 
		NetworkConnectivity connection = new NetworkConnectivity(ctx);
		//are we connected to the web?
		Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
		Check.Require(nickname.length() > 0, ctx.getString(R.string.validation_nickname_required_for_search));
		//validate there are not  funky characters in the string
		
		String url = String.format(Constants.REST_FIND_PLAYER_BY_NICKNAME, nickname);
		
		return url;
		//ok lets call the server now
	 	//new AsyncNetworkRequest(ctx, RequestType.GET, ResponseHandlerType.FIND_PLAYER_BY_NICKNAME, ctx.getString(R.string.progress_searching), null).execute(url);
		
		//return player;
	}
	
	public static void loadPlayerInHeader(final FragmentActivity context){
		loadPlayerInHeader(context, true);
	}
	
	public static void loadPlayerInHeader(final FragmentActivity context, Boolean activateGravatarOnClick){
		 Player player = PlayerService.getPlayerFromLocal();
		ImageFetcher imageLoader = new ImageFetcher(context, 34, 34, 0);
		imageLoader.setImageCache(ImageCache.findOrCreateCache(context, Constants.IMAGE_CACHE_DIR));
		ImageView ivContextPlayer = (ImageView) context.findViewById(R.id.ivHeaderContextPlayer);
		//android.util.Logger.i(TAG, "FindPlayerResults: playerImage=" + player.getImageUrl());
		
		imageLoader.loadImage(player.getImageUrl(), ivContextPlayer); //default image
	 
		if (activateGravatarOnClick == true){
			if (player.isFacebookUser() == false){
		    	ivContextPlayer.setOnClickListener(new View.OnClickListener() {
			 		@Override
					public void onClick(View v) {
			 			Intent intent = new Intent(context, com.riotapps.word.Gravatar.class);
			      	    context.startActivity(intent);
			 		}
				});
			}
		} 
		
		ImageView ivContextPlayerBadge = (ImageView) context.findViewById(R.id.ivHeaderContextPlayerBadge);
		int contextPlayerBadgeId = context.getResources().getIdentifier("com.riotapps.word:drawable/" + player.getBadgeDrawable(), null, null);
		ivContextPlayerBadge.setImageResource(contextPlayerBadgeId);

		TextView tvHeaderContextPlayerName = (TextView) context.findViewById(R.id.tvHeaderContextPlayerName);
		tvHeaderContextPlayerName.setText(player.getNameWithMaxLength(25));
		
		TextView tvHeaderContextPlayerWins = (TextView) context.findViewById(R.id.tvHeaderContextPlayerWins); 
		tvHeaderContextPlayerWins.setText(String.format(context.getString(R.string.header_num_wins), player.getNumWins()));
	}
	
	
	public static Player getPlayerFromLocal(){
		 Gson gson = new Gson(); 
		 Type type = new TypeToken<Player>() {}.getType();
	     SharedPreferences settings = ApplicationContext.getAppContext().getSharedPreferences(Constants.USER_PREFS, 0);
	     
	     //Logger.w(TAG, "getPlayerFromLocal player=" + settings.getString(Constants.USER_PREFS_PLAYER_JSON, Constants.EMPTY_JSON));
	     Player player = gson.fromJson(settings.getString(Constants.USER_PREFS_PLAYER_JSON, Constants.EMPTY_JSON), type);
	     return player;
	}
	
	public static Player handleCreatePlayerResponse(final Context ctx, InputStream iStream){
	   return handlePlayerResponse(ctx, iStream);
	}
	
	public static Player handleChangePasswordResponse(final Context ctx, InputStream iStream){
       return handlePlayerResponse(ctx, iStream);
	}
	
	public static Player handleUpdateAccountResponse(final Context ctx, InputStream iStream){
	       return handlePlayerResponse(ctx, iStream);
	}

	public static Player handleAuthByTokenResponse(final Context ctx, InputStream iStream){
	       return handlePlayerResponse(ctx, iStream);
	}
	
	private static Player handlePlayerResponse(final Context ctx, InputStream iStream){
    	Gson gson = new Gson(); //wrap json return into a single call that takes a type
	        
         //Logger.w(TAG, "handlePlayerResponse incoming json=" + IOHelper.streamToString(iStream));
	        Reader reader = new InputStreamReader(iStream); //serverResponseObject.response.getEntity().getContent());
	        
	        Type type = new TypeToken<Player>() {}.getType();
	        Player player = gson.fromJson(reader, type);
	        
	        ///save player info to shared preferences
	        //userId and auth_token ...email and password should have been stored before this call
	        SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
	        SharedPreferences.Editor editor = settings.edit();
	        
	   
	        Logger.w(TAG, "handlePlayerResponse auth=" + player.getAuthToken() + " " + gson.toJson(player));
	        Date completedDate = new Date(settings.getString(Constants.USER_PREFS_LATEST_COMPLETED_GAME_DATE, Constants.DEFAULT_COMPLETED_GAMES_DATE));
	        
	        if (player.getCompletedGames().size() > 0) {
	        	//reset the rolling latest completion date to last completed game's date. this makes the response from the server as small as possible
	        	for (Game game : player.getCompletedGames()) {
	        		if (completedDate.before(game.getCompletionDate())){
	        			completedDate = game.getCompletionDate();
	        		}
	            }
	        }
	        
	        //manage the local completed games list, only keep 10 max in the list. roll off older games.
	        //do this before the player is stored locally
	        Player storedPlayer = getPlayerFromLocal();
	        if (storedPlayer != null){
	        	if (storedPlayer.getCompletedGames().size() + player.getCompletedGames().size() > Constants.NUM_LOCAL_COMPLETED_GAMES_TO_STORE){
	        		//more than x games are found in combined list. remove earliest to get the list down to x number
	        		List<Game> combinedGames = storedPlayer.getCompletedGames();
	        		for (Game game : player.getCompletedGames()) {
	        			combinedGames.add(game);
		            }
	        		
	        		Collections.sort(combinedGames);
	        		
	        		combinedGames.subList(Constants.NUM_LOCAL_COMPLETED_GAMES_TO_STORE + 1, combinedGames.size()).clear();
	        		player.setCompletedGames(combinedGames);
	        	}
	        }
	        
	        //now set activegames by turn
			//also set activeGamesYourTurn and OpponentTurn
	        List<Game> yourTurn = new ArrayList<Game>();
	        List<Game> opponentTurn = new ArrayList<Game>();
			for (Game game : player.getActiveGames()) {
				Boolean isYourTurn = false;
				for (PlayerGame pg : game.getPlayerGames()){
					if (pg.getPlayerId() == player.getId()){
						yourTurn.add(game);
						isYourTurn = true;
						break;
					}
				}
				if (!isYourTurn){
					opponentTurn.add(game);
				}
	        }
			
			//no need to duplicate the data that is in activeGamesYourTurn and activeGamesOpponentTurn
			//so let's clear this out
			player.getActiveGames().clear();


	        
	        
	        editor.putString(Constants.USER_PREFS_LATEST_COMPLETED_GAME_DATE, completedDate.toGMTString());
	        editor.putString(Constants.USER_PREFS_AUTH_TOKEN, player.getAuthToken());
	        editor.putString(Constants.USER_PREFS_USER_ID, player.getId());
	        editor.putString(Constants.USER_PREFS_PLAYER_JSON, gson.toJson(player));
	        editor.commit();  
	        
	        return player;

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
            Logger.w(getClass().getSimpleName(), "Error for HandleCreatePlayerResponse= ", e);
            
            DialogManager.SetupAlert(ApplicationContext.getAppContext(), "HandleCreatePlayerResponse", e.getMessage(), 0);
           // Toast t = Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);  //change this to real error handling
           // t.show(); 
         }
	 
	}
	
	public static Player handleFindPlayerByNicknameResponse(final Context ctx, InputStream iStream){
        try {
            
        	 Gson gson = new Gson(); //wrap json return into a single call that takes a type
 	        
 	        Reader reader = new InputStreamReader(iStream); //serverResponseObject.response.getEntity().getContent());
 	        
 	        Type type = new TypeToken<Player>() {}.getType();
 	        Player player = gson.fromJson(reader, type);
 	        return player;  
         } 
         catch (Exception e) {
            //getRequest.abort();
            Logger.w("PlayerService", "Error for HandleCreatePlayerResponse= ", e);
            
            DialogManager.SetupAlert(ApplicationContext.getAppContext(), "HandleCreatePlayerResponse", e.getMessage(), 0);
           // Toast t = Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);  //change this to real error handling
           // t.show(); 
         }
		return null;
	 
	}

	
	
}
