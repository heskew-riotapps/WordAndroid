package com.riotapps.word.hooks;

import com.google.gson.annotations.SerializedName;

public class TransportCreateFBPlayer {
	
 
	private String fb;
	
	@SerializedName("l_n")
	private String lastName;
	
	@SerializedName("f_n")
	private String firstName;

	@SerializedName("e_m")
	private String email;

	@SerializedName("r_id")
	private String gcmRegistrationId;
	
	public String getFb() {
		return fb;
	}

	public void setFb(String fb) {
		this.fb = fb;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}
	
}
