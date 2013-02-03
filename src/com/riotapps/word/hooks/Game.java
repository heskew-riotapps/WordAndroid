package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.riotapps.word.R;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.Utils;

 
public class Game implements Parcelable, Comparable<Game> {
	private static final String TAG = Game.class.getSimpleName();
	public Game(){}
	
	private String id = "";
	
	@SerializedName("a_t") //mainly just for transport via json
	private String authToken = ""; 
	
	@SerializedName("played_words")
	private List<PlayedWord> playedWords = new ArrayList<PlayedWord>();
	
	@SerializedName("chats")
	private List<Chat> chats = new ArrayList<Chat>();
	
	@SerializedName("player_games")
	private List<PlayerGame> playerGames = new ArrayList<PlayerGame>();
	
	@SerializedName("played_tiles")
	private List<PlayedTile> playedTiles = new ArrayList<PlayedTile>();
	
	@SerializedName("l_t_a")
	private int lastTurnAction;
	
	@SerializedName("l_t_p")
	private String lastTurnPoints;
	
	@SerializedName("l_t_pl")
	private String lastTurnPlayerId;
	
	@SerializedName("l_t_d")
	private Date lastTurnDate;
	
	@SerializedName("ch_d")
	private Date lastChatDate;
	
	@SerializedName("r_v")
	private List<String> randomVowels;

	@SerializedName("r_c")
	private List<String> randomConsonants;
	
	private boolean showCompletionAlert;
	
	
	private Player _lastTurnPlayer;
	private PlayerGame _contextPlayerGame;
	
	
	
	public List<String> getRandomVowels() {
		return randomVowels;
	}

	public void setRandomVowels(List<String> randomVowels) {
		this.randomVowels = randomVowels;
	}

	public List<String> getRandomConsonants() {
		return randomConsonants;
	}

	public void setRandomConsonants(List<String> randomConsonants) {
		this.randomConsonants = randomConsonants;
	}

	public List<Chat> getChats() {
		return chats;
	}

	public void setChats(List<Chat> chats) {
		this.chats = chats;
	}
	
	//only used for new games, to add opponent to the player for "client-side joins"
	@SerializedName("opps")
	private List<Opponent> opponents_ = new ArrayList<Opponent>();

	public Date getLastChatDate() {
		return lastChatDate;
	}

	public void setLastChatDate(Date lastChatDate) {
		this.lastChatDate = lastChatDate;
	}

	
	public List<Opponent> getOpponents_() {
		return opponents_;
	}

	public void setOpponents_(List<Opponent> opponents_) {
		this.opponents_ = opponents_;
	}

	private Player getLastTurnPlayer(){
		if (this._lastTurnPlayer == null) {
			for (PlayerGame pg : this.getPlayerGames()){
				if (pg.getPlayer().getId().equals(this.lastTurnPlayerId)){
					this._lastTurnPlayer = pg.getPlayer();
				}
			}
		}
		return this._lastTurnPlayer;
	}
	
	public PlayerGame getContextPlayerGame(String contextPlayerId){
		if (this._contextPlayerGame == null) {
			for (PlayerGame pg : this.getPlayerGames()){
				if (pg.getPlayer().getId().equals(contextPlayerId)){
					this._contextPlayerGame = pg;
				}
			}
		}
		return this._contextPlayerGame;
	}
	
	public Player getPlayerById(String playerId){
		Player player = null;
	 
		for (PlayerGame pg : this.getPlayerGames()){
			if (pg.getPlayer().getId().equals(playerId)){
				player = pg.getPlayer();
				break;
			}
		}
 
		return player;
	}
	
//	@SerializedName("last_action_alert_text")
	//do not serialize
 //	private String lastActionText = "";
	
	public String getLastTurnPoints() {
		return lastTurnPoints;
	}

	public void setLastTurnPoints(String lastTurnPoints) {
		this.lastTurnPoints = lastTurnPoints;
	}
	
	

	public boolean isShowCompletionAlert() {
		return showCompletionAlert;
	}

	public void setShowCompletionAlert(boolean showCompletionAlert) {
		this.showCompletionAlert = showCompletionAlert;
	}

	@SerializedName("left")
	private int numLettersLeft = 0;
	
//	@SerializedName("d_c")
//	private String dupCheck = "";

	@SerializedName("cr_d")
	private Date createDate = new Date(0);  
	
	@SerializedName("ls_d")
	private long localStorageDate = 0;// System.nanoTime();  
	
	@SerializedName("ls_ltd")
	private long localStorageLastTurnDate = 0;// System.nanoTime();  
	
	@SerializedName("co_d")
	private Date completionDate = new Date(0); 

	@SerializedName("st")
	private int status = 0;   
	
	@SerializedName("t")
	private int turn = 0;  
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PlayedWord> getPlayedWords() {
		return playedWords;
	}
	
	public List<PlayedWord> getLastPlayedWords() {
		List<PlayedWord> words = new ArrayList<PlayedWord>();
		
		for(PlayedWord word : this.playedWords){
			//Logger.d(TAG, "getLastPlayedWords word=" + word.getWord());
			if (word.getTurn() == (this.getTurn() - 1)){
				words.add(word);
			}
		}
		return words;
	}

	public void setPlayedWords(List<PlayedWord> playedWords) {
		this.playedWords = playedWords;
	}

	public List<PlayerGame> getPlayerGames() {
		return playerGames;
	}

	public List<PlayedTile> getPlayedTiles() {
		return playedTiles;
	}

	public void setPlayedTiles(List<PlayedTile> playedTiles) {
		this.playedTiles = playedTiles;
	}
	
	

	public long getLocalStorageLastTurnDate() {
		return localStorageLastTurnDate;
	}

	public void setLocalStorageLastTurnDate(long localStorageLastTurnDate) {
		this.localStorageLastTurnDate = localStorageLastTurnDate;
	}

	public PlayerGame[] getPlayerGameArray(){
		PlayerGame[] ret = new PlayerGame[this.getPlayerGames().size()];
		  for(int i = 0;i < ret.length;i++){
		    ret[i] = this.getPlayerGames().get(i);}
		  return ret;
		}
	
	public List<PlayerGame> getOpponentPlayerGames(Player contextPlayer){ 
		//assume the context player is the first playergame
		List<PlayerGame> ret = new ArrayList<PlayerGame>();
		
		 for (PlayerGame pg : this.getPlayerGames()){ 
         	if (!pg.getPlayer().getId().equals(contextPlayer.getId())){
         		ret.add(pg);
         	}
		}
		  return ret;
	}
	
	public boolean isContextPlayerStarter(Player contextPlayer){ 
		if (this.getContextPlayerGame(contextPlayer.getId()).getPlayerOrder() == 1) {
			return true;
		}
		//for (PlayerGame pg : this.getPlayerGames()){ 
        // 	if (pg.getPlayerOrder() == 1 && pg.getPlayer().getId().equals(contextPlayer.getId())){
        // 		return true;
        // 	}
		//}
		return false;
	}
	
	public int getContextPlayerOrder(Player contextPlayer){
		try{
			return this.getContextPlayerGame(contextPlayer.getId()).getPlayerOrder();
		}
		catch (Exception e){
			return 0;
		}
//		for (PlayerGame pg : this.getPlayerGames()){ 
 //        	if (pg.getPlayer().getId().equals(contextPlayer.getId())){
 //        		return pg.getPlayerOrder();
 //        	}
//		}
//		return 0;
	}
	
	public boolean isContextPlayerTurn(Player contextPlayer){
		try{
			return this.getContextPlayerGame(contextPlayer.getId()).isTurn();
		}
		catch (Exception e){
			return false;
		}
//		for (PlayerGame pg : this.getPlayerGames()){ 
 //        	if (pg.getPlayer().getId().equals(contextPlayer.getId())){
 //        		return pg.getPlayerOrder();
 //        	}
//		}
//		return 0;
	}
	
	public int getContextPlayerTrayVersion(Player contextPlayer){ 
		try{
			return this.getContextPlayerGame(contextPlayer.getId()).getTrayVersion();
		}
		catch (Exception e){
			return 1;
		}
//		for (PlayerGame pg : this.getPlayerGames()){ 
 //        	if (pg.getPlayer().getId().equals(contextPlayer.getId())){
 //        		return pg.getTrayVersion();
  //       	}
//		}
//		return 1;
	}
	public PlayerGame getWinner(){ 
		 for (PlayerGame pg : this.getPlayerGames()){ 
         	if (pg.isWinner()){
         		return pg;
         	}
		}
		  return null;
	} 
	
	public List<Player> getOpponents(Player contextPlayer){ 
		//assume the context player is the first playergame
		List<Player> ret = new ArrayList<Player>();
		
		 for (PlayerGame pg : this.getPlayerGames()){ 
         	if (!pg.getPlayer().getId().equals(contextPlayer.getId()) && pg.getStatus() == 1){
         		ret.add(pg.getPlayer());
         	}
		}
		  return ret;
	} 
	
	public List<Player> getAllOpponents(Player contextPlayer){ 
		//assume the context player is the first playergame
		List<Player> ret = new ArrayList<Player>();
		
		 for (PlayerGame pg : this.getPlayerGames()){ 
         	if (!pg.getPlayer().getId().equals(contextPlayer.getId())){
         		ret.add(pg.getPlayer());
         	}
		}
		  return ret;
	}
	
	public int getNumActiveOpponents(){
		//refactor to take resigned/declined players out of the count
		return this.getPlayerGames().size() - 1;
	}
	
	public PlayerGame[] getPlayerGameOpponentsArray (){ 
		//assume the context player is the first playergame
		PlayerGame[] ret = new PlayerGame[this.getPlayerGames().size() - 1];
		
		  for(int i = 1;i < this.getPlayerGames().size();i++){
			  if (this.getPlayerGames().get(i).getPlayerOrder() > 1) {
				  ret[i - 1] = this.getPlayerGames().get(i);
			  }
		  }
		  return ret;
		}
	public void setPlayerGames(List<PlayerGame> playerGames) {
		this.playerGames = playerGames;
	}
 
	public int getNumLettersLeft() {
		return numLettersLeft;
	}

	public void setNumLettersLeft(int numLettersLeft) {
		this.numLettersLeft = numLettersLeft;
	}

 
	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getLocalStorageDate() {
		return localStorageDate;
	}
	
	public long getLocalStorageDateInMilliseconds() {
		return localStorageDate / 1000000;
	}

	public void setLocalStorageDate(long localStorageDate) {
		this.localStorageDate = localStorageDate;
	}
	
	public int getNumPlayerSlotsLeft(){
		return 4 - this.getPlayerGames().size(); 
	}

	public int getNumPlayers(){
		return this.getPlayerGames().size(); 
	}
	
	public List<Player> getUnregisteredFBPlayers(){
		List<Player> players = new ArrayList<Player>();
		
		for (PlayerGame pg : this.getPlayerGames()){	 
			if (pg.getPlayerId().length() == 0){		
				players.add(pg.getPlayer());
			}
		}
		return players;
	}
	
	public String getUnregisteredFBPlayersString(){
		String invited = "";
		
		for (Player player : this.getUnregisteredFBPlayers()){
		//Logger.d(TAG,"getInvitedFBPlayersString pg=" + pg.getPlayerId().length() + " " + pg.getPlayer().getFB());
			invited = invited + player.getFB() + ",";
		}
		return invited.length() == 0 ? "" : invited.substring(0,invited.length() - 1); //remove trailing comma
	}
	
	public boolean isCompleted(){
		return this.status == 3 || this.getStatus() == 4;
	}
	
	public int getLastTurnAction() {
		return lastTurnAction;
	}

	public void setLastTurnAction(int lastTurnAction) {
		this.lastTurnAction = lastTurnAction;
	}

	public String getLastTurnPlayerId() {
		return lastTurnPlayerId;
	}

	public void setLastTurnPlayerId(String lastTurnPlayerId) {
		this.lastTurnPlayerId = lastTurnPlayerId;
	}

	public Date getLastTurnDate() {
		return lastTurnDate;
	}

	public void setLastTurnDate(Date lastTurnDate) {
		this.lastTurnDate = lastTurnDate;
	}

	public String getLastActionText(Context context, String contextPlayerId){
	// LastTurn lastTurn = this.getLastTurn(contextPlayerId);
		
	//return "p";
		boolean isContext = this.isContextPlayerPerformedLastTurn(contextPlayerId);
		String opponentName = this.getLastTurnPlayer().getAbbreviatedName();
		PlayerGame contextPlayerGame = this.getContextPlayerGame(contextPlayerId);

		//Logger.d(TAG, "getLastActionText lastAction=" + this.lastTurnAction + " " + this.getLastAction().toString() + " isContext=" + isContext);

		if (this.getStatus() == 3) { //game over
			if (this.getNumActiveOpponents() + 1 == 2){
				PlayerGame singleOpponent = this.getOpponentPlayerGames(contextPlayerGame.getPlayer()).get(0);
				 if (contextPlayerGame.isWinner()){
					 return String.format(context.getString(R.string.game_surface_game_over_2_player_context),
							 contextPlayerGame.getScore(),
							 singleOpponent.getScore());
				 }
				 else if (contextPlayerGame.isDraw()){ 
					 return String.format(context.getString(R.string.game_surface_game_over_2_player_draw),
							 contextPlayerGame.getScore(),
							 contextPlayerGame.getScore());
				 }
				 else { 
					 return String.format(context.getString(R.string.game_surface_game_over_2_player),
							 singleOpponent.getScore(),
							 contextPlayerGame.getScore(),
							 singleOpponent.getPlayer().getName());
				 }
				 //handle draw
			 }
			 else{
				 if (contextPlayerGame.isWinner()){
					 return context.getString(R.string.game_surface_game_over_multi_player_context);
				 }
				 else if (contextPlayerGame.isDraw()){ 
					 return context.getString(R.string.game_surface_game_over_multi_player_draw);
				 }
				 else { 
					 return String.format(context.getString(R.string.game_surface_game_over_multi_player),
						this.getWinner().getPlayer().getName());
				 }
			 }
			
		}
		else if (this.getStatus() == 4) { //declined{
			return context.getString(R.string.game_last_action_declined);
		}
		else{
			switch (this.getLastAction()){
				case ONE_LETTER_SWAPPED:
					if (isContext){
						return context.getString(R.string.game_last_action_swapped_1_context);
					}
					else{
						return String.format(context.getString(R.string.game_last_action_swapped_1), opponentName);				
					}
				
				case TWO_LETTERS_SWAPPED:	
				case THREE_LETTERS_SWAPPED:
				case FOUR_LETTERS_SWAPPED:
				case FIVE_LETTERS_SWAPPED:
				case SIX_LETTERS_SWAPPED:
				case SEVEN_LETTERS_SWAPPED:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_swapped_context), this.getLastTurnAction());
						}
						else{
							return String.format(context.getString(R.string.game_last_action_swapped), opponentName, this.getLastTurnAction());				
						}
				case STARTED_GAME:
					if (isContext){
						return context.getString(R.string.game_last_action_started_context);
					}
					else{
						return String.format(context.getString(R.string.game_last_action_started), opponentName);				
					}
					
				case WORDS_PLAYED:
					
					List<PlayedWord> words = this.getLastPlayedWords();
					int numWordsPlayed = words.size();
			
					//for now limit display to 2
					switch (numWordsPlayed){
					case 1:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_word_played_context), this.getLastTurnPoints(), words.get(0).getWord());
						}
						else {
							return String.format(context.getString(R.string.game_last_action_word_played), this.getLastTurnPoints(), opponentName, words.get(0).getWord());						
						}
					default:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_2_words_played_context), this.getLastTurnPoints(), 
									words.get(0).getWord(), words.get(1).getWord());
						}
						else {
							return String.format(context.getString(R.string.game_last_action_2_words_played), this.getLastTurnPoints(), opponentName, 
									words.get(0).getWord(), words.get(1).getWord());						
						}
					/*case 3:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_3_words_played_context), this.getLastTurnPoints(), 
									words.get(0).getWord(), words.get(1).getWord(),
									words.get(2).getWord());
						}
						else {
							return String.format(context.getString(R.string.game_last_action_3_words_played), this.getLastTurnPoints(), opponentName, 
									words.get(0).getWord(), words.get(1).getWord(),
									words.get(2).getWord());						
						}
					case 4:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_4_words_played_context), this.getLastTurnPoints(), 
									words.get(0).getWord(), words.get(1).getWord(),
									words.get(2).getWord(), words.get(3).getWord());
						}
						else {
							return String.format(context.getString(R.string.game_last_action_4_words_played), this.getLastTurnPoints(), opponentName, 
									words.get(0).getWord(), words.get(1).getWord(),
									words.get(2).getWord(), words.get(3).getWord());						
						}
					case 5:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_5_words_played_context), this.getLastTurnPoints(),  
									words.get(0).getWord(), words.get(1).getWord(),
									words.get(2).getWord(), words.get(3).getWord(),
									words.get(4).getWord());
						}
						else {
							return String.format(context.getString(R.string.game_last_action_5_words_played), this.getLastTurnPoints(), opponentName, 
									words.get(0).getWord(), words.get(1).getWord(),
									words.get(2).getWord(), words.get(3).getWord(),
									words.get(4).getWord());						
						}
					case 6:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_6_words_played_context), this.getLastTurnPoints(),  
									words.get(0).getWord(), words.get(1).getWord(),
									words.get(2).getWord(), words.get(3).getWord(),
									words.get(4).getWord(), words.get(5).getWord());
						}
						else {
							return String.format(context.getString(R.string.game_last_action_6_words_played), this.getLastTurnPoints(), opponentName, 
									words.get(0).getWord(), words.get(1).getWord(),
									words.get(2).getWord(), words.get(3).getWord(),
									words.get(4).getWord(), words.get(5).getWord());						
						}
					default:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_more_than_6_words_played_context), this.getLastTurnPoints(),
									words.get(0).getWord(), words.get(1).getWord(),
									words.get(2).getWord(), words.get(3).getWord(),
									words.get(4).getWord(), words.get(5).getWord(), (numWordsPlayed - 6));
						}
						else {
							return String.format(context.getString(R.string.game_last_action_more_than_6_words_played), this.getLastTurnPoints(), opponentName, 
									words.get(0).getWord(), words.get(1).getWord(),
									words.get(2).getWord(), words.get(3).getWord(),
									words.get(4).getWord(), words.get(5).getWord(), (numWordsPlayed - 6));						
						}*/
					}
				case TURN_SKIPPED:
					if (isContext){
						return context.getString(R.string.game_last_action_skipped_context);
					}
					else{
						return String.format(context.getString(R.string.game_last_action_skipped), opponentName);				
					}		
				
				case RESIGNED:
					if (isContext){
						return context.getString(R.string.game_last_action_resigned_context);
					}
					else{
						return String.format(context.getString(R.string.game_last_action_resigned), opponentName);				
					}		
				
				case CANCELLED:
					return "cancelled"; ///probably not associated with game action, more of a pg status
					
				default:
					return context.getString(R.string.game_last_action_undetermined);
					
			}
		}
	
 	}
	
	public String getWinnerAlertText(Context context, PlayerGame contextPlayerGame){
		String message = "";
		
		if (this.getNumActiveOpponents() + 1 == 2){
			PlayerGame singleOpponent = this.getOpponentPlayerGames(contextPlayerGame.getPlayer()).get(0);
			 if (contextPlayerGame.isWinner()){
				 message = String.format(context.getString(R.string.game_alert_game_over_2_player_context),
						 contextPlayerGame.getScore(),
						 singleOpponent.getScore());
			 }
			 else if (contextPlayerGame.isDraw()){ 
				 message = String.format(context.getString(R.string.game_alert_game_over_2_player_draw),
						 contextPlayerGame.getScore(),
						 contextPlayerGame.getScore());
			 }
			 else { 
				 message = String.format(context.getString(R.string.game_alert_game_over_2_player),
						 singleOpponent.getScore(),
						 contextPlayerGame.getScore(),
						 singleOpponent.getPlayer().getName());
			 }
			 //handle draw
		 }
		 else{
			 if (contextPlayerGame.isWinner()){
				 message = context.getString(R.string.game_alert_game_over_multi_player_context);
			 }
			 else if (contextPlayerGame.isDraw()){ 
				 message = context.getString(R.string.game_alert_game_over_multi_player_draw);
			 }
			 else { 
				 message = String.format(context.getString(R.string.game_alert_game_over_multi_player),
					this.getWinner().getPlayer().getName());
			 }
		 }
		
		return message;
	}
	
	private boolean isContextPlayerPerformedLastTurn(String contextPlayerId){
		
		try{
		return this.lastTurnPlayerId.equals(contextPlayerId);
		}
		catch(Exception e){
			 Logger.d(TAG, "isContextPlayerPerformedLastTurn  this.lastTurnPlayerId=" +  this.lastTurnPlayerId == null ? "null" : this.lastTurnPlayerId  + " contextPlayerId= " + contextPlayerId == null ? "null" : contextPlayerId + " error=" + e.getMessage());
			
			return false;
		}
	}
	
	public String getLastActionTextForList(Context context, String contextPlayerId){
	//	LastTurn lastTurn = this.getLastTurn(contextPlayerId);
		
	// return "p";
	//	Logger.d(TAG, "getLastActionTextForList gameId=" + this.id + " status=" + this.getStatus() + " this.getLastAction()=" + this.getLastAction());
		
		String timeSince = Utils.getTimeSinceString(context, this.getLastTurnDate());
		boolean isContext = this.isContextPlayerPerformedLastTurn(contextPlayerId);
		String opponentName = this.getLastTurnPlayer().getAbbreviatedName();
		PlayerGame contextPlayerGame = this.getContextPlayerGame(contextPlayerId);
		
		if (this.getStatus() == 3) { //game over
			if (this.getNumActiveOpponents() + 1 == 2){
				PlayerGame singleOpponent = this.getOpponentPlayerGames(contextPlayerGame.getPlayer()).get(0);
				 if (contextPlayerGame.isWinner()){
					 return String.format(context.getString(R.string.game_list_game_over_2_player_context), timeSince,
							 contextPlayerGame.getScore(),
							 singleOpponent.getScore());
				 }
				 else if (contextPlayerGame.isDraw()){ 
					 return String.format(context.getString(R.string.game_list_game_over_2_player_draw), timeSince,
							 contextPlayerGame.getScore(),
							 contextPlayerGame.getScore());
				 }
				 else { 
					 return String.format(context.getString(R.string.game_list_game_over_2_player), timeSince,
							 singleOpponent.getScore(),
							 contextPlayerGame.getScore(),
							 singleOpponent.getPlayer().getName());
				 }
				 //handle draw
			 }
			 else{
		 
				 if (contextPlayerGame.isWinner()){
					 return String.format(context.getString(R.string.game_list_game_over_multi_player_context), timeSince);
				 }
				 else if (contextPlayerGame.isDraw()){ 
					 return String.format(context.getString(R.string.game_list_game_over_multi_player_draw), timeSince);
				 }
				 else { 
					 return String.format(context.getString(R.string.game_list_game_over_multi_player), timeSince,
						this.getWinner().getPlayer().getName());
				 }
	 
			 }
		}
		else if (this.getStatus() == 4) { //declined{
			return String.format(context.getString(R.string.game_last_action_list_declined), timeSince, opponentName);
		}
		else {
				switch (this.getLastAction()){
					case ONE_LETTER_SWAPPED:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_list_swapped_1_context), timeSince);
						}
						else{
							return String.format(context.getString(R.string.game_last_action_list_swapped_1), timeSince, opponentName);				
						}
					
					case TWO_LETTERS_SWAPPED:	
					case THREE_LETTERS_SWAPPED:
					case FOUR_LETTERS_SWAPPED:
					case FIVE_LETTERS_SWAPPED:
					case SIX_LETTERS_SWAPPED:
					case SEVEN_LETTERS_SWAPPED:
							if (isContext){
								return String.format(context.getString(R.string.game_last_action_list_swapped_context), timeSince, this.getLastTurnAction());
							}
							else{
								return String.format(context.getString(R.string.game_last_action_list_swapped), timeSince, opponentName, this.getLastTurnAction());				
							} 
					case STARTED_GAME:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_list_started_context), timeSince);
						}
						else{
							return String.format(context.getString(R.string.game_last_action_list_started), timeSince, opponentName);				
						}
						
					case WORDS_PLAYED:
						
						List<PlayedWord> words = this.getLastPlayedWords();
						int numWordsPlayed = words.size();
	
						Logger.d(TAG, "words played size =" + words.size());
						switch (numWordsPlayed){
						case 1:
							if (isContext){
								return String.format(context.getString(R.string.game_last_action_list_word_played_context), timeSince, words.get(0).getWord());
							}
							else {
								return String.format(context.getString(R.string.game_last_action_list_word_played), timeSince, opponentName, words.get(0).getWord());						
							}
						case 2:
							if (isContext){
								return String.format(context.getString(R.string.game_last_action_list_2_words_played_context), timeSince, 
										words.get(0).getWord(), words.get(1).getWord());
							}
							else {
								return String.format(context.getString(R.string.game_last_action_list_2_words_played), timeSince, opponentName, 
										words.get(0).getWord(), words.get(1).getWord());						
							}
						case 3:
							if (isContext){
								return String.format(context.getString(R.string.game_last_action_list_3_words_played_context), timeSince, 
										words.get(0).getWord(), words.get(1).getWord(),
										words.get(2).getWord());
							}
							else {
								return String.format(context.getString(R.string.game_last_action_list_3_words_played), timeSince, opponentName, 
										words.get(0).getWord(), words.get(1).getWord(),
										words.get(2).getWord());						
							}
						case 4:
							if (isContext){
								return String.format(context.getString(R.string.game_last_action_list_4_words_played_context), timeSince, 
										this.getLastPlayedWords().get(0).getWord(), this.getLastPlayedWords().get(1).getWord(),
										this.getLastPlayedWords().get(2).getWord(), this.getLastPlayedWords().get(3).getWord());
							}
							else {
								return String.format(context.getString(R.string.game_last_action_list_4_words_played), timeSince, opponentName, 
										words.get(0).getWord(), words.get(1).getWord(),
										words.get(2).getWord(), words.get(3).getWord());						
							}
						case 5:
							if (isContext){
								return String.format(context.getString(R.string.game_last_action_list_5_words_played_context), timeSince, 
										words.get(0).getWord(), words.get(1).getWord(),
										words.get(2).getWord(), words.get(3).getWord(),
										words.get(4).getWord());
							}
							else {
								return String.format(context.getString(R.string.game_last_action_list_5_words_played), timeSince, opponentName, 
										words.get(0).getWord(), words.get(1).getWord(),
										words.get(2).getWord(), words.get(3).getWord(),
										words.get(4).getWord());						
							}
						case 6:
							if (isContext){
								return String.format(context.getString(R.string.game_last_action_list_6_words_played_context), timeSince, 
										words.get(0).getWord(), words.get(1).getWord(),
										words.get(2).getWord(), words.get(3).getWord(),
										words.get(4).getWord(), words.get(5).getWord());
							}
							else {
								return String.format(context.getString(R.string.game_last_action_list_6_words_played), timeSince, opponentName, 
										words.get(0).getWord(), words.get(1).getWord(),
										words.get(2).getWord(), words.get(3).getWord(),
										words.get(4).getWord(), words.get(5).getWord());						
							}
						default:
							if (isContext){
								return String.format(context.getString(R.string.game_last_action_list_6_words_played_context), timeSince, 
										words.get(0).getWord(), words.get(1).getWord(),
										words.get(2).getWord(), words.get(3).getWord(),
										words.get(4).getWord(), words.get(5).getWord(), (numWordsPlayed - 6));
							}
							else {
								return String.format(context.getString(R.string.game_last_action_list_6_words_played), timeSince, opponentName, 
										words.get(0).getWord(), words.get(1).getWord(),
										words.get(2).getWord(), words.get(3).getWord(),
										words.get(4).getWord(), words.get(5).getWord(), (numWordsPlayed - 6));						
							}
						}
		
					case TURN_SKIPPED:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_list_skipped_context), timeSince);
						}
						else{
							return String.format(context.getString(R.string.game_last_action_list_skipped), timeSince, this.getLastTurnPlayer().getAbbreviatedName());				
						}		
					
					case RESIGNED:
						if (isContext){
							return String.format(context.getString(R.string.game_last_action_list_resigned_context), timeSince);
						}
						else{
							return String.format(context.getString(R.string.game_last_action_list_resigned), timeSince, this.getLastTurnPlayer().getAbbreviatedName());				
						}	
						
					case CANCELLED:
						return "cancelled"; ///probably not associated with game action, more of a pg status
						
					default:
						return context.getString(R.string.game_last_action_undetermined);
				}
			 }	
	}
	
	
	
	
	public enum LastAction{
		NO_TRANSLATION(0),
		ONE_LETTER_SWAPPED(1),
		TWO_LETTERS_SWAPPED(2),
		THREE_LETTERS_SWAPPED(3),
		FOUR_LETTERS_SWAPPED(4),
		FIVE_LETTERS_SWAPPED(5),
		SIX_LETTERS_SWAPPED(6),
		SEVEN_LETTERS_SWAPPED(7),
		STARTED_GAME(8),
		WORDS_PLAYED(9),
		TURN_SKIPPED(10),
		RESIGNED(11),
		CANCELLED(12),
		DECLINED(13);;	
		
		private final int value;
		private LastAction(int value) {
		    this.value = value;
		 }
		
	  public int value() {
		    return value;
		  }
		
	  private static TreeMap<Integer, LastAction> _map;
	  static {
		_map = new TreeMap<Integer, LastAction>();
	    for (LastAction num: LastAction.values()) {
	    	_map.put(new Integer(num.value()), num);
	    }
	    //no translation
	    if (_map.size() == 0){
	    	_map.put(new Integer(0), NO_TRANSLATION);
	    }
	  }
	  
	  public static LastAction lookup(int value) {
		  return _map.get(new Integer(value));
	  	}
	}
	
	public LastAction getLastAction(){
		return LastAction.lookup(this.lastTurnAction);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
 
		out.writeString(this.id);
 
		out.writeList(this.playedWords);
		out.writeList(this.playerGames);
		out.writeInt(this.numLettersLeft);
		//out.writeSerializable(this.createDate);
		//out.writeSerializable(this.completionDate);
		out.writeLong(this.createDate == null ? 0 : this.createDate.getTime());
		out.writeLong(this.completionDate == null ? 0 : this.completionDate.getTime());
		out.writeInt(this.status);
 
	}

	public static final Parcelable.Creator<Game> CREATOR
		    = new Parcelable.Creator<Game>() {
		public Game createFromParcel(Parcel in) {
		    return new Game(in);
		}
		
		public Game[] newArray(int size) {
		    return new Game[size];
		}
	};
	
	 private Game(Parcel in) {
	 
         this.id = in.readString();
         in.readList(this.playedWords,PlayedWord.class.getClassLoader());
         in.readList(this.playerGames,PlayerGame.class.getClassLoader());
 
         this.numLettersLeft = in.readInt();
 
         this.createDate = new Date(in.readLong());
 
         this.completionDate = new Date(in.readLong());
      
         this.status = in.readInt();
      
       	 
     }

	@Override
	public int compareTo(Game game) {
        if (this.getCompletionDate().after(game.getCompletionDate()))
            return -1;
        else if (this.getCompletionDate().equals(game.getCompletionDate()))
            return 0;
        else
            return 1;
    }
	
	
}
