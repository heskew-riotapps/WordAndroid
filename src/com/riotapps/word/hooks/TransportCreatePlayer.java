package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;

public class TransportCreatePlayer {
	
	@SerializedName("e_m")
	private String email;
	
	@SerializedName("p_w")
	private String password;
	
	@SerializedName("n_n")
	private String nickname;

	@SerializedName("r_id")
	private String gcmRegistrationId;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password.trim();
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname.trim();
	}
	
	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}
	
}
