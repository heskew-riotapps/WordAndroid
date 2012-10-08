package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.riotapps.word.utils.Logger;

 
public class Game implements Parcelable, Comparable<Game> {
	private static final String TAG = Game.class.getSimpleName();
	public Game(){}
	
	private String id = "";
	
	@SerializedName("a_t") //mainly just for transport via json
	private String authToken = ""; 
	
	@SerializedName("played_words")
	private List<PlayedWord> playedWords = new ArrayList<PlayedWord>();
	
	@SerializedName("player_games")
	private List<PlayerGame> playerGames = new ArrayList<PlayerGame>();
	
//	@SerializedName("last_action_alert_text")
	//do not serialize
 //	private String lastActionText = "";
	
	@SerializedName("left")
	private int numLettersLeft = 0;
	
//	@SerializedName("d_c")
//	private String dupCheck = "";

	@SerializedName("cr_d")
	private Date createDate = new Date(0);  
	
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

	public void setPlayedWords(List<PlayedWord> playedWords) {
		this.playedWords = playedWords;
	}

	public List<PlayerGame> getPlayerGames() {
		return playerGames;
	}

	public PlayerGame[] getPlayerGameArray(){
		PlayerGame[] ret = new PlayerGame[this.getPlayerGames().size()];
		  for(int i = 0;i < ret.length;i++){
		    ret[i] = this.getPlayerGames().get(i);}
		  return ret;
		}
	
	public List<Player> getOpponents(Player contextPlayer){ 
		//assume the context player is the first playergame
		List<Player> ret = new ArrayList<Player>();
		
		 for (PlayerGame pg : this.getPlayerGames()){ 
         	if (!pg.getPlayer().getId().equals(contextPlayer.getId())){
         		ret.add(pg.getPlayer());
         	}
		}
		  return ret;
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

	public String getLastActionText(){
		//find player that played last turn (this.turn - 1)
		
		
		if (this.turn == 1){
			//find player who played first turn
			
		}
		else {
			//find player who played last turn
			
			
		
		}
		
	}
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Logger.d(TAG, "parcel out");
		out.writeString(this.id);
		Logger.d(TAG, "parcel out id=" + this.id);
		out.writeList(this.playedWords);
		out.writeList(this.playerGames);
		out.writeInt(this.numLettersLeft);
		//out.writeSerializable(this.createDate);
		//out.writeSerializable(this.completionDate);
		out.writeLong(this.createDate == null ? 0 : this.createDate.getTime());
		out.writeLong(this.completionDate == null ? 0 : this.completionDate.getTime());
		out.writeInt(this.status);
		Logger.d(TAG, "parcel out status=" + this.status);
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
		 Logger.d(TAG, "parcel in");
         this.id = in.readString();
         in.readList(this.playedWords,PlayedWord.class.getClassLoader());
         in.readList(this.playerGames,PlayerGame.class.getClassLoader());
         Logger.d(TAG, "parcel in this.playerGames.size()" + this.playerGames.size());
         this.numLettersLeft = in.readInt();

         Logger.d(TAG, "parcel in numLettersLeft=" + numLettersLeft);
       //  this.createDate = in.readSerializable(); 

         this.createDate = new Date(in.readLong());
         
         Logger.d(TAG, "parcel in createDate=" + createDate.toString());
         this.completionDate = new Date(in.readLong());
         Logger.d(TAG, "parcel in completionDate=" + completionDate.toString());
         this.status = in.readInt();
         Logger.d(TAG, "parcel in status=" + status);
       	 
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
