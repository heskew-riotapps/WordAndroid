package com.riotapps.word.hooks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import com.riotapps.word.utils.Constants;
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
	
	@SerializedName("alerts")
	private List<Alert> latestAlerts = new ArrayList(); //the latest communication alert 


	@SerializedName("game_")
	private Game notificationGame = null; //the game associated with notification via gcmIntent
	
	@SerializedName("o_n_i_a")
	private boolean noInterstitialAdsOption = false; //a paid upgrade to not display interstitial ads 

	private List<Opponent> opponents = new ArrayList<Opponent>();
		
	//used as the transport from server, using separate property just in case..
	//don't want opponents property to be overridden by accident
	@SerializedName("opps")
	private List<Opponent> opponents_ = null; //new ArrayList<Opponent>();
	
	@SerializedName("a_games")
	private List<Game> activeGames= new ArrayList<Game>();

	@SerializedName("c_games")
	private List<Game> completedGames = new ArrayList<Game>();
	
	@SerializedName("l_rf_d") 
	private Date lastRefreshDate = null; //new GregorianCalendar("10/6/2012"); //last time a game status changed that the player was involved in
	
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
		this.nickname = nickname;
	}
	public String getNickname() {
		return  (nickname == null ? "" : nickname.trim());
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return (email == null ? "" : email.trim());
	}
	public void setPassword(String password) {
		this.password = password.trim();
	}
	public String getPassword() {
		return this.password;
	}
	public void setFB(String fb) {
		this.fb = fb;
	}
	public String getFB() {
		return (fb == null ? "" : fb.trim());
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken.trim();
	}
	public String getAuthToken() {
		return this.authToken;
	}
	
	public String getFirstName() {
		return (firstName == null ? "" : firstName.trim());
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return (lastName == null ? "" : lastName.trim());
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public boolean isFacebookUser(){
		return this.getFB().length() > 0;
	}
	
	public int getNumGames(){
		return this.numWins + this.numLosses + this.numDraws;
	}
	
	public String getNameWithMaxLength(int maxLength){
		return this.getName().length() > maxLength ? this.getAbbreviatedName() : this.getName();
	}
	
	public String getName(){
		if (this.isFacebookUser()){
			return this.getFirstName() + " " + this.getLastName();	
		}
		else{
			return this.getNickname();
		}
		//if (this.nickname.length() > 0){return this.nickname;}
	}
	
	public String getFirstNameOrNickname(){
		if (this.isFacebookUser()){
			return this.getFirstName();	
		}
		else{
			return this.getNickname();
		}
		//if (this.nickname.length() > 0){return this.nickname;}
	}
	
 
	public List<Alert> getLatestAlerts() {
		return latestAlerts;
	}
	public void setLatestAlerts(List<Alert> latestAlerts) {
		this.latestAlerts = latestAlerts;
	}
	
	public Game getNotificationGame() {
		return notificationGame;
	}
	public void setNotificationGame(Game notificationGame) {
		this.notificationGame = notificationGame;
	}
	public Date getLastRefreshDate() {
		if (lastRefreshDate == null){
			String mytime="20121006102400";
	        SimpleDateFormat dateFormat = new SimpleDateFormat(
	                "yyyymmddhhmmss");

	        try {
				lastRefreshDate = dateFormat.parse(mytime);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lastRefreshDate;
	}
	public void setLastRefreshDate(Date lastRefreshDate) {
		this.lastRefreshDate = lastRefreshDate;
	}
	public boolean isNoInterstitialAdsOption() {
		return noInterstitialAdsOption;
	}
	
	public void setNoInterstitialAdsOption(boolean noInterstitialAdsOption) {
		this.noInterstitialAdsOption = noInterstitialAdsOption;
	}
	
	public String getAbbreviatedName(){
		//eventually check for fb friendship
		if (this.isFacebookUser()){
			return this.getFirstName() + (this.getLastName().length() > 0 ? " " + this.getLastName().substring(0,1) + "." : "");	
		}
		else{
			return this.getNickname();
		}
	}
	
	public String getShortName(){
		String[] parts = this.getName().split(" ");
		
		//keep first name (before first space, initialize everything else
		StringBuilder sb = new StringBuilder();
		for (int x = 0;x < parts.length; x++){
			if (x == 0) {
				sb.append(parts[x]);
			}
			else{
				sb.append(" ");
				sb.append(parts[x].substring(0, 1));
				sb.append(".");
			}
		}
		return sb.toString();
	}
		//if (this.nickname.length() > 0){return this.nickname;}
		//	}
	
	public String getImageUrl(){
		return this.isFacebookUser() ? String.format(Constants.FACEBOOK_IMAGE_URL, this.fb) : String.format(Constants.GRAVATAR_URL, this.gravatar);
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
	
	
	
	public List<Opponent> getOpponents_() {
		return opponents_;
	}
	public void setOpponents_(List<Opponent> opponents_) {
		this.opponents_ = opponents_;
	}
	public Game getGameFromLists(String gameId){
		if (this.getActiveGamesYourTurn().size() > 0){
			for(Game game : this.getActiveGamesYourTurn()){
				if (game.getId().equals(gameId)){
					return game;
				}
			}
		}
		if (this.getActiveGamesOpponentTurn().size() > 0){
			for(Game game : this.getActiveGamesOpponentTurn()){
				if (game.getId().equals(gameId)){
					return game;
				}
			}
		}
		if (this.getCompletedGames().size() > 0){
			for(Game game : this.getCompletedGames()){
				if (game.getId().equals(gameId)){
					return game;
				}
			}
		}
		return null;
	}
	
	public long getLastPlayedDateFromGameList(String gameId){
		if (this.getActiveGamesYourTurn().size() > 0){
			for(Game game : this.getActiveGamesYourTurn()){
				if (game.getId().equals(gameId)){
					return game.getLastTurnDate().getTime();
				}
			}
		}
		if (this.getActiveGamesOpponentTurn().size() > 0){
			for(Game game : this.getActiveGamesOpponentTurn()){
				if (game.getId().equals(gameId)){
					return game.getLastTurnDate().getTime();
				}
			}
		}
		if (this.getCompletedGames().size() > 0){
			for(Game game : this.getCompletedGames()){
				if (game.getId().equals(gameId)){
					return game.getLastTurnDate().getTime();
				}
			}
		}
		return 0;
	}
	
	
	public void setActiveGamesOpponentTurn(List<Game> activeGamesOpponentTurn) {
		this.activeGamesOpponentTurn = activeGamesOpponentTurn;
	}
	
	public List<Opponent> getOpponents() {
		return opponents;
	}
	
	//these are opponents that have at least one completed game with the context player
	public List<Opponent> getOfficialOpponents() {
		List<Opponent> opponents = new ArrayList<Opponent>();
		for (Opponent o : this.getOpponents()){
			if (o.getStatus() == 2 || o.getStatus() == 1){ //1 will go away soon
				opponents.add(o);
			}
		}
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
