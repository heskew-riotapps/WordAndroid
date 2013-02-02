package com.riotapps.word.hooks;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class TransportGameListCheck {
	@SerializedName("a_t")
	private String token;
	
	@SerializedName("l_rf_d")
	private Date lastRefreshDate;
	
	@SerializedName("c_g_d")
	private Date completedGameDate;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastRefreshDate() {
		return lastRefreshDate;
	}

	public void setLastRefreshDate(Date lastRefreshDate) {
		this.lastRefreshDate = lastRefreshDate;
	}

	public Date getCompletedGameDate() {
		return completedGameDate;
	}

	public void setCompletedGameDate(Date completedGameDate) {
		this.completedGameDate = completedGameDate;
	}
	
 
}
