package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
 

public class TransportFindByFB {


	@SerializedName("a_t")
	private String token;
	
	List<String> fb = new ArrayList<String>();

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<String> getFb() {
		return fb;
	}

	public void setFb(List<String> fb) {
		this.fb = fb;
	}

	public void addToFBList(String fb) {
		this.fb.add(fb);
	}
	
}
