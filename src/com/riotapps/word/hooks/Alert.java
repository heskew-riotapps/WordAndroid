package com.riotapps.word.hooks;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class Alert {
	private static final String TAG = Alert.class.getSimpleName();
	
	@SerializedName("t")
	private String text = "";
	
	@SerializedName("ti")
	private String title = "";
	
	private String id = "";
	
	@SerializedName("a_d")
	private String activationDateString; //string instead of date so that there will not be any date conversion issues.  want it to be the exact value from rails

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	 

	public String getActivationDateString() {
		return activationDateString;
	}

	public void setActivationDateString(String activationDateString) {
		this.activationDateString = activationDateString;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
