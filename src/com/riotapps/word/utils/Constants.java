package com.riotapps.word.utils;

public class Constants {

	/**============================================
	 * misc
	 *=============================================*/
 	public static final String MAIN_FONT = "fonts/FullDeceSans1.0.ttf";
 	

 	/**============================================
	 * storage
	 *=============================================*/
 	public static final String USER_PREFS = "user_";
 	public static final String USER_PREFS_USER_ID = "user_uid";
 	public static final String USER_PREFS_AUTH_TOKEN = "user_at";
 	public static final String USER_PREFS_EMAIL = "user_em";
 	public static final String USER_PREFS_PWD = "user_pw";
 	
 	
	/**=============================================
	 * creatingXMLParser for class
	 *==============================================*/
	public static final int XML_PARSER_FOR_USERS = 200;
	public static final int XML_PARSER_FOR_ERRORS = 201;

	/**=============================================
	 * mode of page
	 *==============================================*/
	public static final String MODE_OF_PAGE = "MODE_OF_PAGE";
	public static final int PAGE_ADD = 300;
	public static final int PAGE_EDIT = 301;	

	/**=============================================
	 * the web
	 *==============================================*/
	public static final String REST_URL_SITE = "http://10.0.2.2:3000/";
	public static final String FACEBOOK_API_ID = "314938401925933";
	public static final String REST_CREATE_PLAYER_URL = REST_URL_SITE + "players.json";
	public static final String REST_GET_PLAYER_URL = REST_URL_SITE + "players/%s.json";
	
	/**=============================================
	 * rails
	 *==============================================*/
	public static final String REST_METHOD = "_method";
	public static final String PUT_VERB = "PUT";
	public static final String DELETE_VERB = "DELETE";
}//end class Constants
 
