package com.riotapps.word.hooks;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class TransportAuthToken {
	@SerializedName("a_t")
	private String token;
	
	@SerializedName("a_a_d")
	private String lastAlertActivationDate;
	
	@SerializedName("c_g_d")
	private Date completedGameDate;
	
	@SerializedName("r_id")
	private String gcmRegistrationId;

	public TransportAuthToken(){}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCompletedGameDate() {
		return completedGameDate;
	}

	public void setCompletedGameDate(Date completedGameDate) {
		this.completedGameDate = completedGameDate;
	}
	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}

	public String getLastAlertActivationDate() {
		return lastAlertActivationDate;
	}

	public void setLastAlertActivationDate(String lastAlertActivationDate) {
		this.lastAlertActivationDate = lastAlertActivationDate;
	}
	
}
