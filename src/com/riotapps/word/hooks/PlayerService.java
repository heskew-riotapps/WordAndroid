package com.riotapps.word.hooks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
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
import com.riotapps.word.utils.Utils;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Enums.*;
import com.riotapps.word.utils.NetworkConnectivity;
import com.riotapps.word.utils.Validations;
import com.facebook.GraphUser;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

	public static String setupFBAccountUpdate(Context ctx, String nickname) throws DesignByContractException{
		Gson gson = new Gson(); 
	
		Player player = PlayerService.getPlayerFromLocal();
		NetworkConnectivity connection = new NetworkConnectivity(ctx);
		//are we connected to the web?
	 	Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
	 	//check funky characters in nickname [a-zA-Z0-9\-#\.\(\)\/%&\s]

		Check.Require(nickname.length() > 0, ctx.getString(R.string.validation_nickname_required));
		Check.Require(Validations.validateNickname(nickname.trim()) == true, ctx.getString(R.string.validation_nickname_invalid));
		
		TransportFBUpdateAccount updateAccount = new TransportFBUpdateAccount();
		updateAccount.setNickname(nickname);
		updateAccount.setToken(player.getAuthToken());
		
		return gson.toJson(updateAccount);
	}
	
	
//	public static void saveFacebookFriendsFromJSONResponseXXXXX(Context ctx, String response) throws FacebookError, JSONException{
//		JSONObject json;
//		 
//		json = Util.parseJson(response);
//		final JSONArray friends = json.getJSONArray("data");
//		
//		List<FBFriend> fbFriends = new ArrayList<FBFriend>();
//		
//		for(int i = 0 ; i < friends.length(); i++){
//			FBFriend fbFriend = new FBFriend();
//			
//			JSONObject row = friends.getJSONObject(i);
//			fbFriend.setId(row.getString("id"));
//			fbFriend.setName(row.getString("name"));
//			
//			fbFriends.add(fbFriend);
//		}
//		
//		FBFriends friendsList = new FBFriends();
//		friendsList.setFriends(fbFriends);
//		
//		Gson gson = new Gson(); 	
//	//	Type fooType = new TypeToken<Foo<Bar>>() {}.getType();
//	//	gson.toJson(foo, fooType);
//		String friendJSON = gson.toJson(friendsList, FBFriends.class);
//		
//		SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
//	    SharedPreferences.Editor editor = settings.edit();
//	        
//	  //  Logger.w(TAG, "saveFacebookFriendsFromJSONResponse fbFriends=" + friendJSON.length());
// 
//	    //String json1 = gson.toJson(friendJSON);
//	    editor.putString(Constants.USER_PREFS_FRIENDS_JSON, friendJSON);
//	    
//	 
//	    editor.commit(); 
//	    
//	    String friendsLocal = settings.getString(Constants.USER_PREFS_FRIENDS_JSON, Constants.EMPTY_JSON_ARRAY);
//	   // Logger.w(TAG, "saveFacebookFriendsFromJSONResponse friendsLocal=" + friendsLocal.length());
//	}
	
	public static void saveFacebookFriendsFromJSONResponse(Context ctx, List<GraphUser> users) throws FacebookError, JSONException{

		List<FBFriend> fbFriends = new ArrayList<FBFriend>();
		
		for(GraphUser user : users){
			FBFriend fbFriend = new FBFriend();

			fbFriend.setId(user.getId());
			fbFriend.setName(user.getName());
			
			fbFriends.add(fbFriend);
		}
		
		FBFriends friendsList = new FBFriends();
		friendsList.setFriends(fbFriends);
		
		Gson gson = new Gson(); 	
	//	Type fooType = new TypeToken<Foo<Bar>>() {}.getType();
	//	gson.toJson(foo, fooType);
		String friendJSON = gson.toJson(friendsList, FBFriends.class);
		
		SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
	    SharedPreferences.Editor editor = settings.edit();
	        
	  //  Logger.w(TAG, "saveFacebookFriendsFromJSONResponse fbFriends=" + friendJSON.length());
 
	    //String json1 = gson.toJson(friendJSON);
	    editor.putString(Constants.USER_PREFS_FRIENDS_JSON, friendJSON);
	    
	 
	    editor.commit(); 
	    
	    String friendsLocal = settings.getString(Constants.USER_PREFS_FRIENDS_JSON, Constants.EMPTY_JSON_ARRAY);
	   // Logger.w(TAG, "saveFacebookFriendsFromJSONResponse friendsLocal=" + friendsLocal.length());
	}
	
	public static String setupFindPlayersByFB(Context ctx) throws DesignByContractException{
		Gson gson = new Gson();
		NetworkConnectivity connection = new NetworkConnectivity(ctx);
		//are we connected to the web?
		Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
	 	
		Player player = PlayerService.getPlayerFromLocal();
	 	
	 	Check.Require(player.getAuthToken().length() > 0, ctx.getString(R.string.msg_not_connected));
	 	
		TransportFindByFB fbFriends = new TransportFindByFB();
		fbFriends.setToken(player.getAuthToken());
		

		 SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
	     String friendsLocal = settings.getString(Constants.USER_PREFS_FRIENDS_JSON, Constants.EMPTY_JSON_ARRAY);
		 
	   //  friendsLocal.
	     
	   //  Logger.d(TAG, "setupFindPlayersByFB friendsLocal=" + friendsLocal.length());  
	  //   Logger.d(TAG, "setupFindPlayersByFB friendsLocal=" + friendsLocal);//new StringBuffer(friendsLocal).reverse().toString());
 
	     FBFriends friends = gson.fromJson(friendsLocal, FBFriends.class);
	        
	  //   Logger.d(TAG, "setupFindPlayersByFB friends=" + friends.getFriends().size());
	     
	 //    for(FBFriend fb : friends.getFriends()){
	     for(FBFriend fb : friends.getFriends()){
	    	 fbFriends.addToFBList(fb.getId());
	     }
	 
		return gson.toJson(fbFriends);
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
		
		Check.Require(player != null, ctx.getString(R.string.msg_no_local_player));
	 	Check.Require(player.getAuthToken().length() > 0, ctx.getString(R.string.msg_no_local_player_token));
	 	
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
		context.getSharedPreferences(Constants.GAME_STATE, 0).edit().clear().commit();
		
		clearImageCache(context);
		
	}

	public static void logout(FragmentActivity context){
		clearLocalStorageAndCache(context);
		
        Intent intent = new Intent(context, com.riotapps.word.Welcome.class);
      	context.startActivity(intent);
		
	}
	
	
	public static String setupFindPlayerByNickname(Context ctx, String nickname) throws DesignByContractException{
		nickname = nickname.trim(); 
		NetworkConnectivity connection = new NetworkConnectivity(ctx);
		//are we connected to the web?
		Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
		Check.Require(nickname.length() > 0, ctx.getString(R.string.validation_nickname_required_for_search));
		//validate there are not  funky characters in the string
		
		String url = String.format(Constants.REST_FIND_PLAYER_BY_NICKNAME, nickname);
		
		return url;
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
	     
	    // Logger.w(TAG, "getPlayerFromLocal player=" + settings.getString(Constants.USER_PREFS_PLAYER_JSON, Constants.EMPTY_JSON));
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
	        
	   
	       // Logger.w(TAG, "handlePlayerResponse auth=" + player.getAuthToken() + " " + gson.toJson(player));
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
					if (pg.getPlayer().getId() == player.getId() && pg.isTurn()){
						yourTurn.add(game);
						isYourTurn = true;
						break;
					}
				}
				if (!isYourTurn){
					opponentTurn.add(game);
				}
	        }
			player.setActiveGamesOpponentTurn(opponentTurn);
			player.setActiveGamesYourTurn(yourTurn);
			
			//no need to duplicate the data that is in activeGamesYourTurn and activeGamesOpponentTurn
			//so let's clear this out
			player.getActiveGames().clear();
			
			//Logger.w(TAG, "handlePlayerResponse num active and opponent=" + player.getActiveGamesYourTurn().size() + " " + player.getActiveGamesOpponentTurn().size());

			long now =  Utils.convertNanosecondsToMilliseconds(System.nanoTime());
			
			editor.putLong(Constants.USER_PREFS_PLAYER_CHECK_TIME, now);
	        editor.putString(Constants.USER_PREFS_LATEST_COMPLETED_GAME_DATE, completedDate.toGMTString());
	        editor.putString(Constants.USER_PREFS_AUTH_TOKEN, player.getAuthToken());
	        editor.putString(Constants.USER_PREFS_USER_ID, player.getId());
	        editor.putString(Constants.USER_PREFS_PLAYER_JSON, gson.toJson(player));
	        editor.commit();  
	        
	        //Logger.w(TAG, "player=" + gson.toJson(player));
	        return player;

	}
	
	public static Player updateAuthToken(final Context ctx, String authToken){
		Gson gson = new Gson();
        SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();	        

        Player player = getPlayerFromLocal();
        player.setAuthToken(authToken);
  
        editor.putString(Constants.USER_PREFS_AUTH_TOKEN, player.getAuthToken());
        editor.putString(Constants.USER_PREFS_USER_ID, player.getId());
        editor.putString(Constants.USER_PREFS_PLAYER_JSON, gson.toJson(player));
        editor.commit();  
       
        Logger.d(TAG,"updateAuthToken");
        
        return player;
	}
	
//	@SuppressWarnings("unchecked")
	public static FBFriends findRegisteredFBFriendsResponse(final Context ctx, InputStream iStream){
		Gson gson = new Gson();
        
	    Reader reader = new InputStreamReader(iStream); //serverResponseObject.response.getEntity().getContent());
        
        Type type = new TypeToken<List<Player>>() {}.getType();
        List<Player> players = gson.fromJson(reader, type);
		
		SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();	
        
   	 	String friendsLocal = settings.getString(Constants.USER_PREFS_FRIENDS_JSON, Constants.EMPTY_JSON_ARRAY);
 
        FBFriends friends = gson.fromJson(friendsLocal, FBFriends.class);
        
  //   Logger.d(TAG, "setupFindPlayersByFB friends=" + friends.getFriends().size());
     
 //loop through friends with inner loop that tries to match friend with registered player coming back from server
     	for(FBFriend fb : friends.getFriends()){
     		for (Player player : players){
     			if (fb.getId().equals(player.getFB())){
     				fb.setPlayerId(player.getId());
     			}
     		}
     	}

     	//friends.getFriends().get(40).setPlayerId("123");
     	
     	Collections.sort(friends.getFriends(), new FBFriendComparator());
        
        editor.putLong(Constants.USER_PREFS_FRIENDS_LAST_REGISTERED_CHECK_TIME, Utils.convertNanosecondsToMilliseconds(System.nanoTime()));
   	    editor.putString(Constants.USER_PREFS_FRIENDS_JSON, gson.toJson(friends));
        
     	editor.commit();  
     	
     	return friends;
       
      //  Logger.d(TAG,"updateAuthToken");
        
       // return player;
	}
	
	public static FBFriends getLocalFBFriends(Context ctx){
		Gson gson = new Gson();
		
		SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
   	 	String friendsLocal = settings.getString(Constants.USER_PREFS_FRIENDS_JSON, Constants.EMPTY_JSON_ARRAY);
 
        return gson.fromJson(friendsLocal, FBFriends.class);
	}
	
	
	public static String getBadgeDrawable(int numWins){
		if (numWins == -1) {
			return Constants.BADGE_INVITED;
		}
		if (numWins == 0) {
			return Constants.BADGE_0;
		}
		if (numWins >= 1 && numWins <= 4) {
			return Constants.BADGE_1_4;
		}
		if (numWins >= 5 && numWins <= 9) {
			return Constants.BADGE_5_9;
		}
		if (numWins >= 10 && numWins <= 14) {
			return Constants.BADGE_10_14;
		}
		if (numWins >= 15 && numWins <= 19) {
			return Constants.BADGE_1_4;
		}
		if (numWins >= 1 && numWins <= 4) {
			return Constants.BADGE_15_19;
		}
		if (numWins >= 20 && numWins <= 24) {
			return Constants.BADGE_20_24;
		}
		if (numWins >= 25 && numWins <= 29) {
			return Constants.BADGE_25_29;
		}
		if (numWins >= 30 && numWins <= 39) {
			return Constants.BADGE_30_39;
		}
		if (numWins >= 40 && numWins <= 49) {
			return Constants.BADGE_40_49;
		}
		if (numWins >= 50 && numWins <= 59) {
			return Constants.BADGE_50_59;
		}
		if (numWins >= 60 && numWins <= 69) {
			return Constants.BADGE_60_69;
		}
		if (numWins >= 70 && numWins <= 79) {
			return Constants.BADGE_70_79;
		}
		if (numWins >= 80 && numWins <= 89) {
			return Constants.BADGE_80_89;
		}
		if (numWins >= 90 && numWins <= 99) {
			return Constants.BADGE_90_99;
		}
		if (numWins >= 100 && numWins <= 124) {
			return Constants.BADGE_100_124;
		}
		if (numWins >= 125 && numWins <= 149) {
			return Constants.BADGE_125_149;
		}
		if (numWins >= 150 && numWins <= 174) {
			return Constants.BADGE_150_174;
		}
		if (numWins >= 175 && numWins <= 199) {
			return Constants.BADGE_175_199;
		}
		if (numWins >= 200 && numWins <= 224) {
			return Constants.BADGE_200_224;
		}
		if (numWins >= 225 && numWins <= 249) {
			return Constants.BADGE_225_249;
		}
		if (numWins >= 250 && numWins <= 274) {
			return Constants.BADGE_250_274;
		}
		if (numWins >= 275 && numWins <= 299) {
			return Constants.BADGE_275_299;
		}
		if (numWins >= 300 && numWins <= 349) {
			return Constants.BADGE_300_349;
		}
		if (numWins >= 350 && numWins <= 399) {
			return Constants.BADGE_350_399;
		}
		if (numWins >= 400 && numWins <= 449) {
			return Constants.BADGE_400_449;
		}
		if (numWins >= 450 && numWins <= 499) {
			return Constants.BADGE_450_499;
		}
		if (numWins >= 500 && numWins <= 599) {
			return Constants.BADGE_500_599;
		}
		if (numWins >= 600 && numWins <= 699) {
			return Constants.BADGE_600_699;
		}
		if (numWins >= 700 && numWins <= 799) {
			return Constants.BADGE_700_799;
		}
		if (numWins >= 800 && numWins <= 899) {
			return Constants.BADGE_800_899;
		}
		if (numWins >= 900 && numWins <= 999) {
			return Constants.BADGE_900_999;
		}
		if (numWins >= 1000) { // && numWins <= 1249) {
			return Constants.BADGE_1000_1249;
		}
		if (numWins >= 1250 && numWins <= 1499) {
			return Constants.BADGE_1250_1499;
		}
		if (numWins >= 1500 && numWins <= 1749) {
			return Constants.BADGE_1500_1749;
		}
		if (numWins >= 1750 && numWins <= 1999) {
			return Constants.BADGE_1750_1999;
		}
		if (numWins >= 2000 && numWins <= 2499) {
			return Constants.BADGE_2000_2499;
		}
		if (numWins >= 2500 && numWins <= 2999) {
			return Constants.BADGE_2500_2999;
		}
		if (numWins >= 3000 && numWins <= 3999) {
			return Constants.BADGE_3000_3999;
		}
		if (numWins >= 4000 && numWins <= 4999) {
			return Constants.BADGE_4000_4999;
		}
		if (numWins >= 5000) {
			return Constants.BADGE_5000;
		}
		//fallthrough
		return Constants.BADGE_0;
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
