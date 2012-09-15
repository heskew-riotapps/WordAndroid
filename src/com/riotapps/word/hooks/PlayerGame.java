package com.riotapps.word.hooks;

import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

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
	
	private int score;
	
	@SerializedName("l_t_d")
	private Date lastTurnDate;
	
	@SerializedName("l_a_d")
	private Date lastAlertDate;
	
	@SerializedName("l_r_d")
	private Date lastReminderDate;
	
	@SerializedName("l_c_r_d")
	private Date lastChatterReceivedDate;
	
	@SerializedName("w_n")
	private int winNum;
	
	@SerializedName("i_t")
	private boolean isTurn;
	
	@SerializedName("i_w")
	private boolean isWinner;
	
	@SerializedName("a_e_g")
	private boolean hasBeenAlertedToEndOfGame;
	
	@SerializedName("o")
	private int playerOrder;

	
	@SerializedName("tray_tiles")
	private List<TrayTile> trayTiles;
	
	public List<TrayTile> getTrayTiles() {
		return trayTiles;
	}

	public void setTrayTiles(List<TrayTile> trayTiles) {
		this.trayTiles = trayTiles;
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
		out.writeInt(this.score);
		out.writeLong(this.lastTurnDate.getTime());
		out.writeLong(this.lastAlertDate.getTime());
		out.writeLong(this.lastReminderDate.getTime());
		out.writeLong(this.lastChatterReceivedDate.getTime());
		out.writeInt(this.winNum);
		out.writeByte((byte) (this.isTurn ? 1 : 0));
		out.writeByte((byte) (this.isWinner ? 1 : 0));
		out.writeByte((byte) (this.hasBeenAlertedToEndOfGame ? 1 : 0)); 
		out.writeInt(this.playerOrder);
	//	out.writeParcelable(Player, parcelableFlags)
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
	//	this.playerId = in.readString();
	//	this.score = in.readInt();
	//	this.lastTurnDate = in.readValue()
	//	this.lastAlertDate);
	//	this.lastReminderDate);
	//	this.lastChatterReceivedDate);
	//	this.winNum = in.readString();
	//	this.isTurn);
	//	this.isWinner);
	//	this.hasBeenAlertedToEndOfGame);
	//	myBoolean = in.readByte() == 1; 
		
	}
}
