package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.os.Parcel;
import android.os.Parcelable;

import com.riotapps.word.hooks.Error.ErrorType;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.Logger;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player implements Parcelable{
	private static final String TAG = Player.class.getSimpleName();
	
	public Player(){}
	
	private String id = "";
	
	@SerializedName("n_n")
	private String nickname = "";
	
	@SerializedName("f_n") 
	private String firstName = "";
	
	@SerializedName("l_n")
	private String lastName = "";
	
	@SerializedName("e_m")
	private String email = "";
	
	@SerializedName("n_c_g")
	private int numCompletedGames = 0;
	
	private String gravatar = "";
	
	private String password;
	
	private String fb = "";
	
	@SerializedName("a_t")
	private String authToken;
	
	@SerializedName("n_w")
	private int numWins = 0; //num wins
	
	@SerializedName("n_l")
	private int numLosses = 0; //num losses
	
	@SerializedName("n_d")
	private int numDraws = 0; //num draws

	private List<Opponent> opponents = new ArrayList<Opponent>();
	
	@SerializedName("a_games")
	private List<Game> activeGames= new ArrayList<Game>();

	@SerializedName("c_games")
	private List<Game> completedGames = new ArrayList<Game>();
	
	private List<Game> activeGamesYourTurn= new ArrayList<Game>();
	private List<Game> activeGamesOpponentTurn= new ArrayList<Game>();

	//private String badge_drawable = "";

	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname.trim();
	}
	public String getNickname() {
		return this.nickname;
	}
	public void setEmail(String email) {
		this.email = email.trim();
	}
	public String getEmail() {
		return this.email;
	}
	public void setPassword(String password) {
		this.password = password.trim();
	}
	public String getPassword() {
		return this.password;
	}
	public void setFB(String fb) {
		this.fb = fb.trim();
	}
	public String getFB() {
		return this.fb;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken.trim();
	}
	public String getAuthToken() {
		return this.authToken;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName.trim();
	}
	
	public String getlastName() {
		return lastName;
	}
	
	public void setlastName(String lastName) {
		this.lastName = lastName.trim();
	}
	
	public boolean isFacebookUser(){
		return this.fb.length() > 0;
	}
	
	public int getNumGames(){
		return this.numWins + this.numLosses + this.numDraws;
	}
	
	public String getNameWithMaxLength(int maxLength){
		return this.getName().length() > maxLength ? this.getAbbreviatedName() : this.getName();
	}
	
	public String getName(){
		if (this.isFacebookUser()){
			return this.firstName + " " + this.lastName;	
		}
		else{
			return this.nickname;
		}
		//if (this.nickname.length() > 0){return this.nickname;}
		
	}
	
	public String getAbbreviatedName(){
		//eventually check for fb friendship
		if (this.isFacebookUser()){
			return this.firstName + (this.lastName.length() > 0 ? " " + this.lastName.substring(0,1) + "." : "");	
		}
		else{
			return this.nickname;
		}
	}
		//if (this.nickname.length() > 0){return this.nickname;}
		//	}
	
	public String getImageUrl(){
		return this.fb.length() > 0 ? String.format(Constants.FACEBOOK_IMAGE_URL, this.fb) : String.format(Constants.GRAVATAR_URL, this.gravatar);
	}
	
	public int getNumWins() {
		return numWins;
	}
	public void setNumWins(int numWins) {
		this.numWins = numWins;
	}
	public int getNumLosses() {
		return numLosses;
	}
	public void setNumLosses(int numLosses) {
		this.numLosses = numLosses;
	}
	public int getNumDraws() {
		return numDraws;
	}
	public void setNumDraws(int numDraws) {
		this.numDraws = numDraws;
	}
	
	public String getGravatar() {
		return gravatar;
	}
	public void setGravatar(String gravatar) {
		this.gravatar = gravatar;
	}

	
	public int getNumCompletedGames() {
		return numCompletedGames;
	}
	public void setNumCompletedGames(int numCompletedGames) {
		this.numCompletedGames = numCompletedGames;
	}
	
	public List<Game> getActiveGames() {
		return activeGames;
	}
	public void setActiveGames(List<Game> activeGames) {
		this.activeGames = activeGames;
		
	}
	
	public List<Game> getCompletedGames() {
		return completedGames;
	}
	
	public void setCompletedGames(List<Game> completedGames) {
		this.completedGames = completedGames;
	}
	
	public int getTotalNumLocalGames(){
		return this.activeGamesYourTurn.size() + this.activeGamesOpponentTurn.size() + this.completedGames.size();
	}
	
	public List<Game> getActiveGamesYourTurn() {
		return activeGamesYourTurn;
	}
	
	public void setActiveGamesYourTurn(List<Game> activeGamesYourTurn) {
		this.activeGamesYourTurn = activeGamesYourTurn;
	}
	
	public List<Game> getActiveGamesOpponentTurn() {
		return activeGamesOpponentTurn;
	}
	
	public void setActiveGamesOpponentTurn(List<Game> activeGamesOpponentTurn) {
		this.activeGamesOpponentTurn = activeGamesOpponentTurn;
	}
	
	public List<Opponent> getOpponents() {
		return opponents;
	}
	
	public void setOpponents(List<Opponent> opponents) {
		this.opponents = opponents;
	}
	
	public String getBadgeDrawable(){
		return PlayerService.getBadgeDrawable(this.numWins);
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		
//		Logger.d(TAG, "parcelout");
		out.writeString(this.id);
//		Logger.d(TAG, "parcel out id=" + this.id);
		out.writeString(this.nickname);
//		Logger.d(TAG, "parcel out nickname=" + this.nickname);
		out.writeString(this.firstName);
//		Logger.d(TAG, "parcel out firstname=" + this.firstName);
		out.writeString(this.lastName);
//		Logger.d(TAG, "parcel out lastName=" + this.lastName);
		out.writeString(this.email);
//		Logger.d(TAG, "parcel out email=" + this.email);
	//	out.writeString(this.password);
		out.writeString(this.fb);
//		Logger.d(TAG, "parcel out fb=" + this.fb);
		out.writeString(this.authToken);
//		Logger.d(TAG, "parcel out authToken=" + this.authToken);
		out.writeInt(this.numWins);
		out.writeInt(this.numLosses);
		out.writeInt(this.numDraws);
	//	out.writeString(this.badge_drawable);
		out.writeString(this.gravatar);
//		Logger.d(TAG, "parcel out gravatar=" + this.gravatar);
	}
	
	public static final Parcelable.Creator<Player> CREATOR
    			= new Parcelable.Creator<Player>() {
			public Player createFromParcel(Parcel in) {
				return new Player(in);
			}

			public Player[] newArray(int size) {
				return new Player[size];
			}
	};

	private Player(Parcel in) {
		// same order as writeToParcel
	//	Logger.d(TAG, "parcelin");
		this.id = in.readString();
	//	Logger.d(TAG, "parcel in id=" + this.id);
		this.nickname = in.readString();
//		Logger.d(TAG, "parcel in nickname=" + this.nickname);
		this.firstName = in.readString();
//		Logger.d(TAG, "parcel in firstname=" + this.firstName);
		this.lastName = in.readString();
//		Logger.d(TAG, "parcel in lastName=" + this.lastName);
		this.email = in.readString();
//		Logger.d(TAG, "parcel in email=" + this.email);
//		this.password = in.readString();
	
		this.fb = in.readString();
//		Logger.d(TAG, "parcel in fb=" + this.fb);
		this.authToken = in.readString();
//		Logger.d(TAG, "parcel in authToken=" + this.authToken);
		this.numWins = in.readInt();
		this.numLosses = in.readInt();
		this.numDraws = in.readInt();
	//	this.badge_drawable = in.readString();
		this.gravatar = in.readString();
//		Logger.d(TAG, "parcel in gravatar=" + this.gravatar);
	}

	
}
