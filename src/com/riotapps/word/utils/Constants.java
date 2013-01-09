package com.riotapps.word.utils;

public class Constants {

	/**============================================
	 * misc
	 *=============================================*/
 	public static final String MAIN_FONT =  "fonts/FullDeceSans1.0.ttf";
 	//public static final String GAME_BOARD_FONT = "fonts/Asap_Bold.ttf";//"fonts/banksia.ttf"; 
 	public static final String GAME_BOARD_FONT = "fonts/Asap_Bold.ttf";//"fonts/banksia.ttf"; //"fonts/Crushed.ttf";//"fonts/MILFCD_B.ttf";  //"fonts/Vegur_B_0.602.otf";
 	public static final String GAME_LETTER_FONT = "fonts/Asap_Bold.ttf"; //"fonts/Vegur_B_0.602.otf"; //Asap_Bold
 	public static final String GAME_LETTER_VALUE_FONT = "fonts/Asap_Regular.ttf"; //"fonts/Vegur_B_0.602.otf"; //Asap_Bold
 	public static final String SCOREBOARD_FONT = "fonts/Asap_Bold.ttf"; //"fonts/banksia.ttf"; "fonts/Crushed.ttf";//"fonts/Vegur_B_0.602.otf"; Asap_Bold
	public static final String SCOREBOARD_BUTTON_FONT = "fonts/Asap_Bold.ttf"; 
	public static final String EMPTY_JSON = "{}";
	public static final String EMPTY_JSON_ARRAY = "[]";
	public static final String DEFAULT_COMPLETED_GAMES_DATE = "10/6/2012";
 	public static final String WN_KEY = "48f31f368d20791114b01067e1d05b68ca177aabdbab4150b";
 	public static final String DRAWABLE_LOCATION = "com.riotapps.word:drawable/";
 	public static final String CONTENT_AREA_BACKGROUND_COLOR = "content_area_background_color";
 	public static final String CONTENT_AREA_BACKGROUND_SELECTED_COLOR = "content_area_background_selected_color";

 	public static final int SMASHER_BONUS_POINTS = 40;
 	public static final String LAYOUT_SCOPE_WORD = "W";
 	public static final String LAYOUT_SCOPE_LETTER = "L";
 	public static final String IMAGE_CACHE_DIR = "http";
 	public static final int DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS = 4000;
 	public static final long LOCAL_GAME_STORAGE_DURATION_IN_MILLISECONDS = 300000;//5 minutes in milliseconds
 	public static final long LOCAL_GAME_LIST_STORAGE_DURATION_IN_MILLISECONDS = 300000; //5 minutes in milliseconds
 	
 	public static final String GRAVATAR_URL = "http://www.gravatar.com/avatar/%s?r=pg&s=75&d=mm";
 	public static final String FACEBOOK_IMAGE_URL = "http://graph.facebook.com/%s/picture?r=1&type=square";		
 	public static final int DEFAULT_CONNECTION_TIMEOUT = 12000;
	public static final int DEFAULT_SOCKET_CONNECTION_TIMEOUT = 20000;
	public static final int INITIAL_CONNECTIVITY_THREAD_SLEEP = 4000;
	public static final int REGISTERED_FB_FRIENDS_CACHE_DURATION = 604800000; //a week of milliseconds
	public static final int SPLASH_ACTIVITY_TIMEOUT = 1000;
	public static final int NETWORK_CONNECTIVITY_CHECK_DURATION = 2000;
	
	
	public static final String FACEBOOK_PERMISSIONS = "email";
	public static final int NUM_LOCAL_COMPLETED_GAMES_TO_STORE = 10;
	public static final int DEFAULT_AVATAR_SIZE = 34;
	public static final int LARGE_AVATAR_SIZE = 54;
	public static final int BADGE_SIZE = 10;

	/**============================================
	 * intent extras
	 *=============================================*/
	public static final String EXTRA_GAME_LIST_PREFETCHED = "isGameListFetched";
 	public static final String EXTRA_GAME = "com.riotapps.word.hooks.Game";
	public static final String EXTRA_GAME_ID = "gameId";
	public static final String EXTRA_PLAYER = "com.riotapps.word.hooks.Player";
	 	
 	/**============================================
	 * storage
	 *=============================================*/
 	public static final String USER_PREFS = "user_";
 	public static final String USER_PREFS_USER_ID = "user_uid";
 	public static final String USER_PREFS_AUTH_TOKEN = "user_at";
 	public static final String USER_PREFS_EMAIL = "user_em";
 	public static final String USER_PREFS_PWD = "user_pw";
 	public static final String USER_PREFS_ACTIVE_GAMES = "a_games";
 	public static final String USER_PREFS_PLAYER_JSON = "player_json";
 	public static final String USER_PREFS_GAME_JSON = "game_json_%s";
 	public static final String USER_PREFS_FRIENDS_JSON = "friends_json";
 	public static final String USER_PREFS_FRIENDS_LAST_REGISTERED_CHECK_TIME = "friends_reg_check";
 	public static final String USER_PREFS_PLAYER_CHECK_TIME = "player_check";
 	public static final String USER_PREFS_GAME_LIST_CHECK_TIME = "game_list_check";
 	public static final String USER_PREFS_GCM_REGISTRATION_ID = "gcm";
	public static final String USER_PREFS_GAME_ALERT_CHECK = "game_alert_check_%s";
	public static final String USER_PREFS_GAME_CHAT_CHECK = "game_chat_check_%s";
 	public static final String USER_PREFS_LATEST_COMPLETED_GAME_DATE = "cg_date";
 	public static final String GAME_STATE = "game_state";
 	public static final String FB_TOKEN = "fb_token";
	public static final String FB_TOKEN_EXPIRES = "fb_token_expires";
 	
	/**=============================================
	 * creatingXMLParser for class
	 *==============================================*/
	//public static final int XML_PARSER_FOR_USERS = 200;
	//public static final int XML_PARSER_FOR_ERRORS = 201;

	/**=============================================
	 * mode of page
	 *==============================================*/
	//public static final String MODE_OF_PAGE = "MODE_OF_PAGE";
	///public static final int PAGE_ADD = 300;
	//public static final int PAGE_EDIT = 301; 	  

	/**=============================================
	 * the web
	 *==============================================*/  
	public static final boolean HIDE_ALL_ADS = false;   
	public static final String REST_URL_SITE = "http://smash.riotapps.com/en/";       
	// public static final String REST_URL_SITE = "http://10.0.2.2:3000/en/";     
	public static final String FACEBOOK_API_ID = "314938401925933"; 
	public static final String REST_CREATE_PLAYER_URL = REST_URL_SITE + "players.json";
	public static final String REST_GET_PLAYER_URL = REST_URL_SITE + "players/%s.json";
	public static final String REST_FIND_PLAYER_BY_NICKNAME = REST_URL_SITE + "players/find.json?n_n=%s";
	public static final String REST_AUTHENTICATE_PLAYER_BY_TOKEN = REST_URL_SITE + "players/auth_via_token.json";
	public static final String REST_GET_PLAYER_BY_TOKEN = REST_URL_SITE + "players/get_via_token.json";
	public static final String REST_PLAYER_LOGOUT = REST_URL_SITE + "players/log_out.json";
	public static final String REST_PLAYER_CHANGE_PASSWORD = REST_URL_SITE + "players/change_password.json";
	public static final String REST_PLAYER_UPDATE_ACCOUNT = REST_URL_SITE + "players/update_account.json";
	public static final String REST_PLAYER_UPDATE_FB_ACCOUNT = REST_URL_SITE + "players/update_fb_account.json";
	public static final String REST_PLAYER_GET_GAMES = REST_URL_SITE + "games/get_active_games.json";
	public static final String REST_PLAYER_GCM_REGISTER = REST_URL_SITE + "games/gcm_register.json";
	public static final String REST_CREATE_GAME_URL = REST_URL_SITE + "games.json";
	public static final String REST_GET_GAME_URL = REST_URL_SITE + "games/get.json";
	public static final String REST_FIND_REGISTERED_FB_FRIENDS = REST_URL_SITE + "players/find_all_by_fb";
	public static final String REST_GAME_CANCEL = REST_URL_SITE + "games/cancel";
	public static final String REST_GAME_PLAY = REST_URL_SITE + "games/play";
	public static final String REST_GAME_SKIP = REST_URL_SITE + "games/skip";
	public static final String REST_GAME_SWAP = REST_URL_SITE + "games/swap";
	public static final String REST_GAME_CHAT = REST_URL_SITE + "games/chat";
	public static final String REST_GAME_RESIGN = REST_URL_SITE + "games/resign";
	public static final String REST_GAME_DECLINE = REST_URL_SITE + "games/decline";
	
	/**=============================================
	 * rails
	 *==============================================*/
	public static final String REST_METHOD = "_method";
	public static final String PUT_VERB = "PUT";
	public static final String DELETE_VERB = "DELETE";
	
	
	/**=============================================
	 * badge drawables
	 *==============================================*/
	public static final String BADGE_INVITED = "badge_invited";
	public static final String BADGE_0 = "badge_0";
	public static final String BADGE_1_4 = "badge_1_4";
	public static final String BADGE_5_9 = "badge_5_9";
	public static final String BADGE_10_14 = "badge_10_14";
	public static final String BADGE_15_19 = "badge_15_19";
	public static final String BADGE_20_24 = "badge_20_24";
	public static final String BADGE_25_29 = "badge_25_29";
	public static final String BADGE_30_39 = "badge_30_39";
	public static final String BADGE_40_49 = "badge_40_49";
	public static final String BADGE_50_59 = "badge_50_59";
	public static final String BADGE_60_69 = "badge_60_69";
	public static final String BADGE_70_79 = "badge_70_79";
	public static final String BADGE_80_89 = "badge_80_89";
    public static final String BADGE_90_99 = "badge_90_99";
	public static final String BADGE_100_124 = "badge_100_124";
	public static final String BADGE_125_149 = "badge_125_149";
	public static final String BADGE_150_174 = "badge_150_174";
	public static final String BADGE_175_199 = "badge_175_199";
	public static final String BADGE_200_224 = "badge_200_224";
	public static final String BADGE_225_249 = "badge_225_249";
	public static final String BADGE_250_274 = "badge_250_274";
	public static final String BADGE_275_299 = "badge_275_299";
	public static final String BADGE_300_349 = "badge_300_349";
	public static final String BADGE_350_399 = "badge_350_399";
	public static final String BADGE_400_449 = "badge_400_449";
	public static final String BADGE_450_499 = "badge_450_499";
	public static final String BADGE_500_599 = "badge_500_599";
	public static final String BADGE_600_699 = "badge_600_699";
	public static final String BADGE_700_799 = "badge_700_799";
	public static final String BADGE_800_899 = "badge_800_899";
	public static final String BADGE_900_999 = "badge_900_999";
	public static final String BADGE_1000_1249 = "badge_1000_1249";
	public static final String BADGE_1250_1499 = "badge_1250_1499";
	public static final String BADGE_1500_1749 = "badge_1500_1749";
	public static final String BADGE_1750_1999 = "badge_1750_1999";
	public static final String BADGE_2000_2499 = "badge_2000_2449";
	public static final String BADGE_2500_2999 = "badge_2500_2999";
	public static final String BADGE_3000_3999 = "badge_3000_3999";
	public static final String BADGE_4000_4999 = "badge_4000_4999";
	public static final String BADGE_5000 = "badge_5000";
}//end class Constants
 
