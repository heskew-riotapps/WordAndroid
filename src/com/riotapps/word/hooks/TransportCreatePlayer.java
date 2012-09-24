package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;

public class TransportCreatePlayer {
	
	@SerializedName("e_m")
	private String email;
	
	@SerializedName("p_w")
	private String password;
	
	@SerializedName("n_n")
	private String nickname;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
	
}
