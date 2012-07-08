package com.riotapps.word.hooks;

public class Player {
	private String id;
	private String nickname;
	private String email;
	private String password;
	private String fb = "";
	private String auth_token;
	private int numWins = 0;
	private int numLosses = 0;
	private int numDraws = 0;

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
	public void setAuthToken(String auth_token) {
		this.auth_token = auth_token.trim();
	}
	public String getAuthToken() {
		return this.auth_token;
	}
	
	public boolean isFacebookUser(){
		return this.fb.length() > 0;
	}
	
	public int getNumGames(){
		return this.numWins + this.numLosses + this.numDraws;
	}

}
