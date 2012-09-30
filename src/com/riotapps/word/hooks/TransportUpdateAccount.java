package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;

public class TransportUpdateAccount {
	
	@SerializedName("e_m")
	private String email;
	
	@SerializedName("n_n")
	private String nickname;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.trim();
	}
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname.trim();
	}
	
	
	
}
