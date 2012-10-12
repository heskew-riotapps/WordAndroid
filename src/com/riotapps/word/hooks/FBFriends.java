package com.riotapps.word.hooks;

import java.util.ArrayList;
import java.util.List;

public class FBFriends {
	private List<FBFriend> friends = new ArrayList<FBFriend>();

	public List<FBFriend> getFriends() {
		return friends;
	}

	public void setFriends(List<FBFriend> friends) {
		this.friends = friends;
	}
	
	public FBFriend[] getArray(){
		return (FBFriend[]) friends.toArray();
	}

}
