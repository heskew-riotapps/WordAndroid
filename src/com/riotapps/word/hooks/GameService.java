package com.riotapps.word.hooks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.riotapps.word.R;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.Check;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.Utils;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.IOHelper;
import com.riotapps.word.utils.Enums.*;
import com.riotapps.word.utils.NetworkConnectivity;
import com.riotapps.word.utils.Validations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
 
 
public class GameService {
	private static final String TAG = GameService.class.getSimpleName();
	
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
	
	public static void updateLastGameListCheckTime(Context context){
		
		SharedPreferences settings = context.getSharedPreferences(Constants.USER_PREFS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(Constants.USER_PREFS_GAME_LIST_CHECK_TIME, Utils.convertNanosecondsToMilliseconds(System.nanoTime()));
		editor.commit();
	}
	
	public static void clearLastGameListCheckTime(Context context){
		
		SharedPreferences settings = context.getSharedPreferences(Constants.USER_PREFS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(Constants.USER_PREFS_GAME_LIST_CHECK_TIME, 0);
		editor.commit();
	}
	
	public static long getLastGameListCheckTime(Context context){ 
		SharedPreferences settings = context.getSharedPreferences(Constants.USER_PREFS, 0);
		return settings.getLong(Constants.USER_PREFS_GAME_LIST_CHECK_TIME, 0);
	}
		
	public static String setupStartGame(Context ctx, Game game) throws DesignByContractException{
		 
		Logger.d(TAG, "setupStartGame");
		Gson gson = new Gson();
		
		NetworkConnectivity connection = new NetworkConnectivity(ApplicationContext.getAppContext());
		//are we connected to the web?
	 	Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
		Check.Require(game.getPlayerGames().size() > 1 && game.getPlayerGames().size() < 5, ctx.getString(R.string.validation_must_have_between_2_and_4_players));
		
		Player player = PlayerService.getPlayerFromLocal();
		
		TransportCreateGame newGame = new TransportCreateGame();
		newGame.setToken(player.getAuthToken());
		
		for(PlayerGame pg : game.getPlayerGames()){
			String name = "";
			if (pg.getPlayerId().length() == 0) {
				name = pg.getPlayer().getName();
			}
			newGame.addToPlayerGames(pg.getPlayerId(),pg.getPlayerOrder(), pg.getPlayer().getFB(), name);
		}
		
		return gson.toJson(newGame);
	}
	
	
	public static String setupGetGame(Context ctx, String gameId) throws DesignByContractException{
		 
		Logger.d(TAG, "setupGetGame");
		Gson gson = new Gson();
		
		NetworkConnectivity connection = new NetworkConnectivity(ApplicationContext.getAppContext());
		//are we connected to the web?
	 	Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
	 	
		Player player = PlayerService.getPlayerFromLocal();
		
		TransportGetGame game = new TransportGetGame();
		game.setToken(player.getAuthToken());
		game.setGameId(gameId);
		
		return gson.toJson(game);
	}
//	public static void CreateGame(Context ctx, String email, String nickname, String password, Class<?> goToClass) throws DesignByContractException{
//
//		Gson gson = new Gson();
//		NetworkConnectivity connection = new NetworkConnectivity(ApplicationContext.getAppContext());
//		//are we connected to the web?
//		Check.Require(connection.checkNetworkConnectivity() == true, ctx.getString(R.string.msg_not_connected));
//		Check.Require(email.length() > 0, ctx.getString(R.string.validation_email_required));
//		Check.Require(Validations.validateEmail(email) == true, ctx.getString(R.string.validation_email_invalid));
//	 
//		Player player = new Player();
//		player.setEmail(email);
//		player.setNickname(nickname);
//		player.setPassword(password);
//		
//		String json = gson.toJson(player);
//		
//	//	  String shownOnProgressDialog = "progress test";//ctx.getString(R.string.progressDialogMessageSplashScreenRetrievingUserListing);
//		   
//		SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
//		SharedPreferences.Editor editor = settings.edit();
//		editor.putString(Constants.USER_PREFS_PWD, player.getPassword());
//		editor.putString(Constants.USER_PREFS_EMAIL, player.getEmail());
//		editor.commit();
//		
//		//ok lets call the server now
//		new AsyncNetworkRequest(ctx, RequestType.POST, ResponseHandlerType.CREATE_PLAYER, ctx.getString(R.string.progress_saving), json, goToClass).execute(Constants.REST_CREATE_PLAYER_URL);
		
//		//return player;
//	}
	 
	public static void putGameToLocal(Context ctx,Game game){
		Gson gson = new Gson(); 
	    SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    
	    game.setLocalStorageDate(System.nanoTime());
	    Logger.w(TAG, "game=" + gson.toJson(game));
	   
	    editor.putString(String.format(Constants.USER_PREFS_GAME_JSON, game.getId()), gson.toJson(game));
	    editor.commit(); 
	}
	
	
	public static Game getGameFromLocal(String gameId){
		 Gson gson = new Gson(); 
		 Type type = new TypeToken<Game>() {}.getType();
	     SharedPreferences settings = ApplicationContext.getAppContext().getSharedPreferences(Constants.USER_PREFS, 0);
	     Game game = gson.fromJson(settings.getString(String.format(Constants.USER_PREFS_GAME_JSON, gameId), Constants.EMPTY_JSON), type);
	     return game;
	}
	
//	public static void HandleCreateGameResponse(final Context ctx, InputStream iStream){
 //       try {
 //           
 //       	 Gson gson = new Gson(); //wrap json return into a single call that takes a type
 	        
// 	        Reader reader = new InputStreamReader(iStream); //serverResponseObject.response.getEntity().getContent());
// 	        
// 	        Type type = new TypeToken<Player>() {}.getType();
// 	        Player player = gson.fromJson(reader, type);
// 	        
// 	        ///save player info to shared preferences
// 	        //userId and auth_token ...email and password should have been stored before this call
// 	       SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
// 	       SharedPreferences.Editor editor = settings.edit();
// 	       editor.putString(Constants.USER_PREFS_AUTH_TOKEN, player.getAuthToken());
// 	       editor.putString(Constants.USER_PREFS_USER_ID, player.getId());
// 	      editor.putString(Constants.USER_PREFS_PLAYER_JSON, gson.toJson(player));
// 	       editor.commit();  
// 	        
// 	       Intent goToMainLanding = new Intent(ctx, com.riotapps.word.MainLanding.class);
//	       ctx.startActivity(goToMainLanding);
// 	       //Intent goToGamesLanding = new Intent(ctx, com.riotapps.word.GamesLanding.class);
//		   //ctx.startActivity(goToGamesLanding);
 //	       //redirect to game landing page
// 	       
// 	       //Toast t = Toast.makeText(ctx, response.getAuthToken(), Toast.LENGTH_LONG);  
// 	       // t.show(); 
 //           
  //       } 
 //        catch (Exception e) {
  ///          //getRequest.abort();
  //          Log.w("HandleCreateGameResponse", "Error for HandleCreateGameResponse= ", e);
  //          
 //           Toast t = Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);  //change this to real error handling
 //           t.show(); 
  //       }
	 
//	}
	
//	public static void HandleGetGameResponse(final Context ctx, InputStream iStream, Class<?> goToClass)
//	{
 //       try {
  //          
 //       	 Gson gson = new Gson(); //wrap json return into a single call that takes a type
 //	        
 //	        Reader reader = new InputStreamReader(iStream); //serverResponseObject.response.getEntity().getContent());
 //	        
 //	        Type type = new TypeToken<Game>() {}.getType();
 //	        Game game = gson.fromJson(reader, type);
 //	       
 //	        //save game data to  	        
 //	       // String s = IOHelper.streamToString(iStream);
 //	        
 //	        //save game data to 
 //	        
 //	        ///save player info to shared preferences
 //	        //userId and auth_token ...email and password should have been stored before this call
 //	       // SharedPreferences settings = ctx.getSharedPreferences(Constants.USER_PREFS, 0);
 //	       // SharedPreferences.Editor editor = settings.edit();
 //	       // editor.putString(Constants.USER_PREFS_AUTH_TOKEN, player.getAuthToken());
 //	       // editor.putString(Constants.USER_PREFS_USER_ID, player.getId());
 //	       // editor.putString(Constants.USER_PREFS_PLAYER_JSON, gson.toJson(player));
 //	       // editor.commit();  
//	 	        
// 	        Intent intent = new Intent(ctx, goToClass);
// 	      //  intent.putExtra("gameId", game.getId());
// 	      //	intent.putExtra("game", s);
// 	      	intent.putExtra(Constants.EXTRA_GAME, game);
// 	        ctx.startActivity(intent);
// 	      	
 //	       
 //	      	
 //	       //redirect to game landing page
 //	       
 //	       //Toast t = Toast.makeText(ctx, response.getAuthToken(), Toast.LENGTH_LONG);  
 //	       // t.show(); 
  //           
   //      } 
  //       catch (Exception e) {
   //         //getRequest.abort();
  //          //Log.w(getClass().getSimpleName(), "Error for HandleCreatePlayerResponse= ", e);
  //          
   ///         DialogManager.SetupAlert(ApplicationContext.getAppContext(), "HandleGetGameResponse", e.getMessage(), 0);
  //         // Toast t = Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);  //change this to real error handling
  //         // t.show(); 
   //      }
	 
//	}

	public static Game handleCreateGameResponse(final Context ctx, InputStream iStream){
		return handleGameResponse(ctx, iStream); 
	}
	
	public static Game handleGetGameResponse(final Context ctx, InputStream iStream){
        return handleGameResponse(ctx, iStream); 
	}
	
	private static Game handleGameResponse(final Context ctx, InputStream iStream){
		 Gson gson = new Gson(); //wrap json return into a single call that takes a type
    	 
   	  Logger.d(TAG, "handleCreateGameResponse");
        
         Reader reader = new InputStreamReader(iStream); //serverResponseObject.response.getEntity().getContent());

         Type type = new TypeToken<Game>() {}.getType();
         Game game = gson.fromJson(reader, type);
        
        Logger.d(TAG, "game authtoken=" + game.getAuthToken()); 
	         
         PlayerService.updateAuthToken(ctx, game.getAuthToken());
         
         //should this be saved locally??????
         //perhaps save it locally if it's the context player's turn, since the only thing that can
         //change is chat in this state
         
         return game;  
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
		
	 	Check.Require(!contextPlayer.getId().equals(player.getId()), ctx.getString(R.string.validation_cannot_add_yourself));
	 	Check.Require(!contextPlayer.getId().equals(player.getId()), ctx.getString(R.string.validation_cannot_add_yourself)); 
	 	 for (Player p : game.getOpponents(contextPlayer)){
	 		Check.Require(player.getFB().length() > 0 || !p.getId().equals(player.getId()), String.format(ctx.getString(R.string.validation_opponent_already_added), p.getName())); 
		 }

    	PlayerGame pg = new PlayerGame();
    	pg.setPlayerId(player.getId());
    	pg.setPlayer(player);
    	pg.setPlayerOrder(game.getPlayerGames().size() + 1);
    	game.getPlayerGames().add(pg);
    	
    	return game;
	}
	
	public static PlayerGame loadScoreboard(final FragmentActivity context, Game game, Player player){
		 //determine length of name and font size if too long (maybe)
		 //always use abbreviated name when 3 or more players
		 
		 //find context user in list.  context user will always be display in top left of scoreboard with
		 //other players in the assigned game order following
		 //for instance if contextUser is #3 in order, he will still be in top left corner and
		 // #4 will be under him (or #1 if there are only 3 players in the game) and
		 //#1 will be in top right and #2 will be in bottom right
		 //if there are only 3 players, the bottom right will always be empty
		 //if there are only 2 players the right column will be hidden 
		PlayerGame contextPlayerGame = new PlayerGame();
		
		 int contextPlayerIndex = -1;
		 int contextPlayerTurnOrder = -1;
		 int playerIndex2 = -1;
		 int playerIndex3 = -1;
		 int playerIndex4 = -1;
		 
		 String activePlayer; 
		 int playerGameCount = game.getPlayerGames().size();
		 
		 //Log.d(TAG,"num players=" + playerGameCount); 
		 
		 //loop through once to find context player
		 for(int x = 0; x < playerGameCount; x++){ 
			 
			// Log.d(TAG,"x=" + x);
		 //for (PlayerGame pg : this.game.getPlayerGames()) {
		    PlayerGame pg = game.getPlayerGames().get(x);	
		  //  Log.d(TAG,"player=" + pg.getPlayer().getId());
			 if (pg.getPlayer().getId() != null && pg.getPlayer().getId().equals(player.getId())) {
				// Log.d(TAG,"contextplayermatch=" + pg.getPlayer().getId());
		    		contextPlayerIndex = x;
		    		contextPlayerTurnOrder = pg.getPlayerOrder();
		    		contextPlayerGame = pg;
		     }
			// Log.d(TAG,"about to check pg.isturn");
			 if (pg.isTurn()){
				 Log.d(TAG,"pg.isTurn=true");
				 activePlayer = pg.getPlayer().getId();
			 }
		  }
		 
		// Log.d(TAG,"context playerid=" + contextPlayerIndex);
		 
		 if (contextPlayerIndex == -1){ 
			 Log.w(TAG,"context player not found.  this is a problem that requires game to be removed from user's device.");
		 }
		 
		 if (playerGameCount > 2){
			 //loop through again to fill out the order of the other players for placement in the scoreboard 
			 //i'm sure there is a 2 line formula that will shrink this code but i'm tired and settling for switch statements
			 for(int x = 0; x < playerGameCount; x++){
				
			    PlayerGame pg = game.getPlayerGames().get(x);
			    if (x != contextPlayerIndex){
			    	//we already know where the context player (the player logged in right now) is
			    	//let's find the order to display the others
				    int orderDiff = contextPlayerTurnOrder - pg.getPlayerOrder();
				    //if (contextPlayerTurnOrder == 1 && pg.getPlayerOrder() == 2)
				    switch (contextPlayerTurnOrder){
					    case 1:
					    	//1 3  <-- 4 players
					    	//2 4
					    	//1 3  <-- 3 players
					    	//2  
					    	switch (pg.getPlayerOrder()){
						    	case 2:
						    		playerIndex2 = x;
						    		break;
						    	case 3:
						    		playerIndex3 = x;
						    		break;
						    	case 4:
						    		playerIndex4 = x;
						    		break;
					    	}
					    	break;
					    case 2:
					    	//2 4  <-- 4 players
					    	//3 1
					    	//2 1  <-- 3 players
					    	//3  
					    	switch (pg.getPlayerOrder()){
						    	case 3:
						    		playerIndex2 = x;
						    		break;
						    	case 4:
						    		playerIndex3 = x;
						    		break;
						    	case 1:
						    		if (playerGameCount == 3){
						    			playerIndex3 = x;
						    		}
						    		else {
						    			playerIndex4 = x;
						    		}
						    		break;
						    	}
					    	break;
					    case 3:
					    	//3 2  <-- 3 players
					    	//1
					    	//3 1  <-- 4 players
					    	//4 2
					    	switch (pg.getPlayerOrder()){
						    	case 1:
						    		if (playerGameCount == 3){
						    			playerIndex2 = x;
						    		}
						    		else {
						    			playerIndex3 = x;
						    		}
						    		break;
						    	case 4:
						    		playerIndex2 = x;
						    		break;
						    	case 2:
						    		if (playerGameCount == 3){
						    			playerIndex3 = x;
						    		}
						    		else {
						    			playerIndex4 = x;
						    		}
						    		break;
						    	}
					    	break;
					    case 4:
					    	//4 2  <-- 4 players (can never be 3 players by definition)
					    	//1 3
					    	switch (pg.getPlayerOrder()){
					    		case 1:
						    		playerIndex2 = x;
						    		break;
						    	case 2:
						    		playerIndex3 = x;
						    		break;
						    	case 3:
						    		playerIndex4 = x;
						    		break;
						    	}
					    	break;
				    }
			    }
			 }
		 }
		 else{
			 playerIndex2 = (contextPlayerIndex == 0 ? 1 : 0);
		 }
		 
		 TextView tvContextPlayer = (TextView)context.findViewById(R.id.tvPlayer_1);
		 TextView tvContextPlayerScore = (TextView)context.findViewById(R.id.tvPlayerScore_1);
		 ImageView ivContextPlayerBadge = (ImageView)context.findViewById(R.id.ivPlayerBadge_1);
		 ImageView ivContextPlayerTurn = (ImageView)context.findViewById(R.id.ivPlayerTurn_1);
		 
		 TextView tvPlayer2 = (TextView)context.findViewById(R.id.tvPlayer_2);
		 TextView tvPlayerScore2 = (TextView)context.findViewById(R.id.tvPlayerScore_2);
		 ImageView ivPlayerBadge2 = (ImageView)context.findViewById(R.id.ivPlayerBadge_2);
		 ImageView ivPlayerTurn2 = (ImageView)context.findViewById(R.id.ivPlayerTurn_2);

		 TextView tvPlayer3 = (TextView)context.findViewById(R.id.tvPlayer_3);
		 TextView tvPlayerScore3 = (TextView)context.findViewById(R.id.tvPlayerScore_3);
		 ImageView ivPlayerBadge3 = (ImageView)context.findViewById(R.id.ivPlayerBadge_3);
		 ImageView ivPlayerTurn3 = (ImageView)context.findViewById(R.id.ivPlayerTurn_3);
		 
		 TextView tvPlayer4 = (TextView)context.findViewById(R.id.tvPlayer_4);
		 TextView tvPlayerScore4 = (TextView)context.findViewById(R.id.tvPlayerScore_4);
		 ImageView ivPlayerBadge4 = (ImageView)context.findViewById(R.id.ivPlayerBadge_4);
		 ImageView ivPlayerTurn4 = (ImageView)context.findViewById(R.id.ivPlayerTurn_4);
		 
		 //position 1
		 String contextPlayer = game.getPlayerGames().get(contextPlayerIndex).getPlayer().getName();
		 if (contextPlayer.length() > 25 || playerGameCount > 2){contextPlayer = game.getPlayerGames().get(contextPlayerIndex).getPlayer().getAbbreviatedName();}
		 tvContextPlayer.setText(contextPlayer);
		 tvContextPlayerScore.setText(Integer.toString(game.getPlayerGames().get(contextPlayerIndex).getScore()));
		 int contextPlayerBadgeId = context.getResources().getIdentifier("com.riotapps.word:drawable/" + game.getPlayerGames().get(contextPlayerIndex).getPlayer().getBadgeDrawable(), null, null);
		 ivContextPlayerBadge.setImageResource(contextPlayerBadgeId);
		 if (game.getPlayerGames().get(contextPlayerIndex).isTurn() == false){ivContextPlayerTurn.setVisibility(View.INVISIBLE);}

		 //position 2
		 String player2 = game.getPlayerGames().get(playerIndex2).getPlayer().getName();
		 if (player2.length() > 25 || playerGameCount > 2){player2 = game.getPlayerGames().get(playerIndex2).getPlayer().getAbbreviatedName();}
		 tvPlayer2.setText(player2);
		 tvPlayerScore2.setText(Integer.toString(game.getPlayerGames().get(playerIndex2).getScore()));
		 int playerBadgeId2 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + game.getPlayerGames().get(playerIndex2).getPlayer().getBadgeDrawable(), null, null);
		 ivPlayerBadge2.setImageResource(playerBadgeId2);
		 if (game.getPlayerGames().get(playerIndex2).isTurn() == false){ivPlayerTurn2.setVisibility(View.INVISIBLE);}

		 //if neither context player and player 2 are currently taking their turn, 
		 //collapse the turn image column on the left
		 if (game.getPlayerGames().get(playerIndex2).isTurn() == false &&
				 game.getPlayerGames().get(contextPlayerIndex).isTurn() == false) {
			 ivContextPlayerTurn.setVisibility(View.GONE);
			 ivPlayerTurn2.setVisibility(View.GONE);
		 }
		 
		  
		 if (playerGameCount == 3){
			 //hide 4th player
			 tvPlayer4.setVisibility(View.INVISIBLE);
			 tvPlayerScore4.setVisibility(View.INVISIBLE);
			 ivPlayerBadge4.setVisibility(View.INVISIBLE);
			 ivPlayerTurn4.setVisibility(View.INVISIBLE);	
			 
			 //position 3
			 String player3 = game.getPlayerGames().get(playerIndex3).getPlayer().getName();
			 if (player3.length() > 25 || playerGameCount > 2){player3 = game.getPlayerGames().get(playerIndex3).getPlayer().getAbbreviatedName();}
			 tvPlayer3.setText(player3);
			 tvPlayerScore3.setText(Integer.toString(game.getPlayerGames().get(playerIndex3).getScore()));
			 int playerBadgeId3 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + game.getPlayerGames().get(playerIndex3).getPlayer().getBadgeDrawable(), null, null);
			 ivPlayerBadge3.setImageResource(playerBadgeId3);
			 if (game.getPlayerGames().get(playerIndex3).isTurn() == false){ivPlayerTurn3.setVisibility(View.INVISIBLE);}

		 }
		 else if (playerGameCount == 2)
		 {
			 //hide second column if there are only 2 players
			 tvPlayer3.setVisibility(View.GONE);
			 tvPlayerScore3.setVisibility(View.GONE);
			 ivPlayerBadge3.setVisibility(View.GONE);
			 ivPlayerTurn3.setVisibility(View.GONE);
			 
			 tvPlayer4.setVisibility(View.GONE);
			 tvPlayerScore4.setVisibility(View.GONE);
			 ivPlayerBadge4.setVisibility(View.GONE);
			 ivPlayerTurn4.setVisibility(View.GONE);	 
		 }
		 else if (playerGameCount == 4){
			 
			 //position 3
			 String player3 = game.getPlayerGames().get(playerIndex3).getPlayer().getName();
			 if (player3.length() > 25 || playerGameCount > 2){player3 = game.getPlayerGames().get(playerIndex3).getPlayer().getAbbreviatedName();}
			 tvPlayer3.setText(player3);
			 tvPlayerScore3.setText(Integer.toString(game.getPlayerGames().get(playerIndex3).getScore()));
			 int playerBadgeId3 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + game.getPlayerGames().get(playerIndex3).getPlayer().getBadgeDrawable(), null, null);
			 ivPlayerBadge3.setImageResource(playerBadgeId3);
			 if (game.getPlayerGames().get(playerIndex3).isTurn() == false){ivPlayerTurn3.setVisibility(View.INVISIBLE);}

			 //position 4
			 String player4 = game.getPlayerGames().get(playerIndex4).getPlayer().getName();
			 if (player4.length() > 25 || playerGameCount > 2){player4 = game.getPlayerGames().get(playerIndex4).getPlayer().getAbbreviatedName();}
			 tvPlayer4.setText(player4);
			 tvPlayerScore4.setText(Integer.toString(game.getPlayerGames().get(playerIndex4).getScore()));
			 int playerBadgeId4 = context.getResources().getIdentifier("com.riotapps.word:drawable/" + game.getPlayerGames().get(playerIndex4).getPlayer().getBadgeDrawable(), null, null);
			 ivPlayerBadge4.setImageResource(playerBadgeId4);
			 if (game.getPlayerGames().get(playerIndex4).isTurn() == false){ivPlayerTurn4.setVisibility(View.INVISIBLE);}
		 }
		 
		 TextView tvLettersLeft = (TextView)context.findViewById(R.id.tvLettersLeft);
		 if (game.getNumLettersLeft() == 1){
			 tvLettersLeft.setText(R.string.scoreboard_1_letter_left);
		 }
		 else{
			 tvLettersLeft.setText(String.format(context.getString(R.string.scoreboard_letters_left), game.getNumLettersLeft()));
		 }
		 //this.tvNumPoints.setText(String.format(context.getString(R.string.scoreboard_num_points), "0"));
		 
		 return contextPlayerGame;
	 }
	
}
