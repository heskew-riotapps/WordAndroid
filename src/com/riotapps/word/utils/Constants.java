package com.riotapps.word.utils;

public class Constants {

	/**============================================
	 * misc
	 *=============================================*/
 	public static final String MAIN_FONT =  "fonts/FullDeceSans1.0.ttf";
 	//public static final String GAME_BOARD_FONT = "fonts/Asap_Bold.ttf";//"fonts/banksia.ttf"; 
 	public static final String GAME_BOARD_FONT = "fonts/Asap_Bold.ttf";//"fonts/banksia.ttf"; //"fonts/Crushed.ttf";//"fonts/MILFCD_B.ttf";  //"fonts/Vegur_B_0.602.otf";
 	public static final String GAME_LETTER_FONT = "fonts/CherryCreamSoda.ttf"; //"fonts/Vegur_B_0.602.otf"; //Asap_Bold
 	public static final String GAME_LETTER_VALUE_FONT = "fonts/Asap_Regular.ttf"; //"fonts/Vegur_B_0.602.otf"; //Asap_Bold
 	public static final String SCOREBOARD_FONT = "fonts/Asap_Bold.ttf"; //"fonts/banksia.ttf"; "fonts/Crushed.ttf";//"fonts/Vegur_B_0.602.otf"; Asap_Bold
	public static final String SCOREBOARD_BUTTON_FONT = "fonts/Asap_Bold.ttf"; 
	public static final String EMPTY_JSON = "{}";
 	public static final String WN_KEY = "48f31f368d20791114b01067e1d05b68ca177aabdbab4150b";
 	
 	public static final String LAYOUT_SCOPE_WORD = "W";
 	public static final String LAYOUT_SCOPE_LETTER = "L";
 	public static final String IMAGE_CACHE_DIR = "images";
 	public static final int DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS = 3000;
 	
 	public static final String GRAVATAR_URL = "http://www.gravatar.com/avatar/%s?d=mm&r=pg&s=50";
 	public static final String FACEBOOK_IMAGE_URL = "http://graph.facebook.com/%s/picture?r=1&type=square";			
 			

	/**============================================
	 * intent extras
	 *=============================================*/
 	public static final String EXTRA_GAME = "game";
	public static final String EXTRA_GAME_ID = "gameId";
	public static final String EXTRA_PLAYER = "player";
	 	
 	/**============================================
	 * storage
	 *=============================================*/
 	public static final String USER_PREFS = "user_";
 	public static final String USER_PREFS_USER_ID = "user_uid";
 	public static final String USER_PREFS_AUTH_TOKEN = "user_at";
 	public static final String USER_PREFS_EMAIL = "user_em";
 	public static final String USER_PREFS_PWD = "user_pw";
 	public static final String USER_PREFS_PLAYER_JSON = "player_json";
 	public static final String GAME_STATE = "game_state";
 	
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
	//public static final String REST_URL_SITE = "http://smash.riotapps.com/en/";
	public static final String REST_URL_SITE = "http://10.0.2.2:3000/en/";
	public static final String FACEBOOK_API_ID = "314938401925933";
	public static final String REST_CREATE_PLAYER_URL = REST_URL_SITE + "players.json";
	public static final String REST_GET_PLAYER_URL = REST_URL_SITE + "players/%s.json";
	public static final String REST_FIND_PLAYER_BY_NICKNAME = REST_URL_SITE + "players/find.json?n_n=%s";
	
	/**=============================================
	 * rails
	 *==============================================*/
	public static final String REST_METHOD = "_method";
	public static final String PUT_VERB = "PUT";
	public static final String DELETE_VERB = "DELETE";
	
	
	/**=============================================
	 * badge drawables
	 *==============================================*/
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
 
