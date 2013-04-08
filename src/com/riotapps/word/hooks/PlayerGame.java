package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.method.DateTimeKeyListener;

import com.google.gson.annotations.SerializedName;
import com.riotapps.word.hooks.Error.ErrorType;
import com.riotapps.word.utils.Logger;

public class PlayerGame implements Parcelable{
	private static final String TAG = PlayerGame.class.getSimpleName();
//	  key :player_id , ObjectId
//	  key :score,     Integer, :default => 0
//	  key :last_turn_date, Time
//	  key :last_alert_date, Time
//	  key :last_reminder_date, Time
//	  key :last_chatter_received_date, Time
//	  key :last_viewed_date, Time
//	  key :win_num,    Integer, :default => 0
//	  key :is_turn, Boolean, :default => false 
//	  key :is_winner, Boolean, :default => false 
//	  key :has_been_alerted_to_end_of_game, Boolean, :default => false 
	
	public PlayerGame(){}
	
	@SerializedName("player_id")
	private String playerId; 
	
	private Player player; 
	
	@SerializedName("sc")
	private int score = 0;
	
	@SerializedName("st")
	private int status = 0;
	
/*	@SerializedName("l_t")
	private int lastTurn;

	@SerializedName("l_t_a")
	private int lastTurnAction;
	
	@SerializedName("l_t_p")
	private int lastTurnPoints;
	
	@SerializedName("l_t_d")
	private Date lastTurnDate;
*/		
	@SerializedName("l_a_d")
	private Date lastAlertDate;

	@SerializedName("l_r_d")
	private Date lastReminderDate;
	
	@SerializedName("l_c_r_d")
	private Date lastChatterReceivedDate;
	
	@SerializedName("w_n")
	private int winNum = 0;
	
	@SerializedName("i_t")
	private boolean isTurn = false;
	
  	@SerializedName("a_e_g")
	private boolean hasBeenAlertedToEndOfGame = false;
	
	@SerializedName("o")
	private int playerOrder = 0;

	@SerializedName("t_v")
	private int trayVersion = 1;
	
	@SerializedName("t_l")
	private List<String> trayLetters = new ArrayList<String>();
	
	
	public List<String> getTrayLetters() {
		return trayLetters;
	}

	public void setTrayTiles(List<String> trayLetters) {
		this.trayLetters = trayLetters;
	}
	
	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	/*
 
	public Date getLastTurnDate() {
		return lastTurnDate;
	}

	public void setLastTurnDate(Date lastTurnDate) {
		this.lastTurnDate = lastTurnDate;
	}

	public Date getLastAlertDate() {
		return lastAlertDate;
	}

	public void setLastAlertDate(Date lastAlertDate) {
		this.lastAlertDate = lastAlertDate;
	}
*/
	public Date getLastReminderDate() {
		return lastReminderDate;
	}

	public void setLastReminderDate(Date lastReminderDate) {
		this.lastReminderDate = lastReminderDate;
	}

	public Date getLastChatterReceivedDate() {
		return lastChatterReceivedDate;
	}

	public void setLastChatterReceivedDate(Date lastChatterReceivedDate) {
		this.lastChatterReceivedDate = lastChatterReceivedDate;
	}

	public int getWinNum() {
		return winNum;
	}

	public void setWinNum(int winNum) {
		this.winNum = winNum;
	}

	public boolean isTurn() {
		return isTurn;
	}

	public void setTurn(boolean isTurn) {
		this.isTurn = isTurn;
	}

	public boolean isWinner() {
		return this.status == 4;
	}
	
	public boolean isDraw() {
		return this.status == 6;
	}

 
	public boolean isHasBeenAlertedToEndOfGame() {
		return hasBeenAlertedToEndOfGame;
	}

	public void setHasBeenAlertedToEndOfGame(boolean hasBeenAlertedToEndOfGame) {
		this.hasBeenAlertedToEndOfGame = hasBeenAlertedToEndOfGame;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public boolean isActive(){
		//declined and cancelled
		//NO_TRANSLATION(0), (no action yet)
		//ACTIVE(1),
		//CANCELLED(2),
		//DECLINED(3),
		//WON(4),
		//LOST(5),
		//DRAW(6),
		//RESIGNED(7) //a temp status, will be changed to LOST after the game is over????
		//DECLINED_BY_INVITEES(8),
		if (this.status == 1 || this.status == 4 || this.status == 5 || this.status == 6 || this.status == 7){
			return true;
		}
		return false;
	}

	public int getPlayerOrder() {
		return playerOrder;
	}

	public void setPlayerOrder(int playerOrder) {
		this.playerOrder = playerOrder;
	}
	
/*
	public int getLastTurn() {
		return lastTurn;
	}

	public void setLastTurn(int lastTurn) {
		this.lastTurn = lastTurn;
	}

	public int getLastTurnAction() {
		return lastTurnAction;
	}

	public void setLastTurnAction(int lastTurnAction) {
		this.lastTurnAction = lastTurnAction;
	}

	public int getLastTurnPoints() {
		return lastTurnPoints;
	}

	public void setLastTurnPoints(int lastTurnPoints) {
		this.lastTurnPoints = lastTurnPoints;
	}
*/
	public int getTrayVersion() {
		return trayVersion;
	}

	public void setTrayVersion(int trayVersion) {
		this.trayVersion = trayVersion;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
//		Logger.d(TAG, "parcel out");
		out.writeString(this.playerId); 
		out.writeParcelable(player, flags);
//		Logger.d(TAG, "parcel out playerId=" + this.player.getId());
		out.writeInt(this.score);
	//	out.writeLong(this.lastTurnDate == null ? 0 : this.lastTurnDate.getTime());
		out.writeLong(this.lastAlertDate == null ? 0 : this.lastAlertDate.getTime());
		out.writeLong(this.lastReminderDate == null ? 0 : this.lastReminderDate.getTime());
		out.writeLong(this.lastChatterReceivedDate == null ? 0 : this.lastChatterReceivedDate.getTime());
		out.writeInt(this.winNum);
		out.writeByte((byte) (this.isTurn ? 1 : 0));
	 
		out.writeByte((byte) (this.hasBeenAlertedToEndOfGame ? 1 : 0)); 
		out.writeInt(this.playerOrder);
 		out.writeList(this.trayLetters);
 	//	out.writeInt(this.lastTurn);
 	//	out.writeInt(this.lastTurnAction);
 	 	out.writeInt(this.status);
 		out.writeInt(this.trayVersion);
 		
	}
	
	public static final Parcelable.Creator<PlayerGame> CREATOR
			= new Parcelable.Creator<PlayerGame>() {
			public PlayerGame createFromParcel(Parcel in) {
				return new PlayerGame(in);
			}

			public PlayerGame[] newArray(int size) {
				return new PlayerGame[size];
			}
	};
	
	private PlayerGame(Parcel in) {
//		Logger.d(TAG, "parcel in");
		// same order as writeToParcel
	 	this.playerId = in.readString();
	 	this.player = in.readParcelable(Player.class.getClassLoader());
	// 	Logger.d(TAG, "parcel in playerId=" + this.playerId);
	 	this.score = in.readInt();
	// 	this.lastTurnDate = new Date();
	// 	this.lastTurnDate.setTime(in.readLong());
		this.lastAlertDate = new Date();
	 	this.lastAlertDate.setTime(in.readLong()); 	
	 	this.lastReminderDate = new Date();
	 	this.lastReminderDate.setTime(in.readLong());
	 	this.lastChatterReceivedDate = new Date();
	 	this.lastChatterReceivedDate.setTime(in.readLong()); 
	  	this.winNum = in.readInt();
	  	this.isTurn = in.readByte() == 1;
	 
	    this.hasBeenAlertedToEndOfGame  = in.readByte() == 1;
	    this.playerOrder = in.readInt();
	    this.trayLetters = new ArrayList<String>();
	    in.readStringList(this.trayLetters);
//	    this.lastTurn = in.readInt();
//	    this.lastTurnAction = in.readInt();
	    this.status = in.readInt();
	    this.trayVersion = in.readInt();
	  //  Logger.d(TAG, "parcel in playerOrder=" + this.playerOrder);
	
	}
/*	
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
		CANCELLED(12);	
		
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
	
	*/
}
