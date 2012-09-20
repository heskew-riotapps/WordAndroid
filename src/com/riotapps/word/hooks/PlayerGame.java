package com.riotapps.word.hooks;

import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.method.DateTimeKeyListener;

import com.google.gson.annotations.SerializedName;

public class PlayerGame implements Parcelable{

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
	
	private int score = 0;
	
	@SerializedName("l_t_d")
	private Date lastTurnDate;
	
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
	
	@SerializedName("i_w")
	private boolean isWinner = false;
	
	@SerializedName("a_e_g")
	private boolean hasBeenAlertedToEndOfGame = false;
	
	@SerializedName("o")
	private int playerOrder = 0;

	
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
		return isWinner;
	}

	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
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
	
	

	public int getPlayerOrder() {
		return playerOrder;
	}

	public void setPlayerOrder(int playerOrder) {
		this.playerOrder = playerOrder;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		 
		out.writeString(this.playerId); 
		out.writeParcelable(player, flags);
		out.writeInt(this.score);
		out.writeLong(this.lastTurnDate == null ? 0 : this.lastTurnDate.getTime());
		out.writeLong(this.lastAlertDate == null ? 0 : this.lastAlertDate.getTime());
		out.writeLong(this.lastReminderDate == null ? 0 : this.lastReminderDate.getTime());
		out.writeLong(this.lastChatterReceivedDate == null ? 0 : this.lastChatterReceivedDate.getTime());
		out.writeInt(this.winNum);
		out.writeByte((byte) (this.isTurn ? 1 : 0));
		out.writeByte((byte) (this.isWinner ? 1 : 0));
		out.writeByte((byte) (this.hasBeenAlertedToEndOfGame ? 1 : 0)); 
		out.writeInt(this.playerOrder);
 		out.writeList(this.trayLetters);		
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
		// same order as writeToParcel
	 	this.playerId = in.readString();
	 	this.player = in.readParcelable(Player.class.getClassLoader());
	 	this.score = in.readInt();
	 	this.lastTurnDate = new Date();
	 	this.lastTurnDate.setTime(in.readLong());
		this.lastAlertDate = new Date();
	 	this.lastAlertDate.setTime(in.readLong()); 	
	 	this.lastReminderDate = new Date();
	 	this.lastReminderDate.setTime(in.readLong());
	 	this.lastChatterReceivedDate = new Date();
	 	this.lastChatterReceivedDate.setTime(in.readLong()); 
	  	this.winNum = in.readInt();
	  	this.isTurn = in.readByte() == 1;
	 	this.isWinner  = in.readByte() == 1;
	    this.hasBeenAlertedToEndOfGame  = in.readByte() == 1;
	    this.playerOrder = in.readInt();
	    this.trayLetters = null;
	    in.readStringList(this.trayLetters);
	
	}
}
