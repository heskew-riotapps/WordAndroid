package com.riotapps.word.hooks;

import java.util.Comparator;

public class FBFriendComparator  implements Comparator<FBFriend> {

	/*
	@Override
	public int compare(FBFriend fbFriend1, FBFriend fbFriend2) {
		  int value1 = fbFriend2.isRegisteredPlayer().compareTo(fbFriend1.isRegisteredPlayer());// .campus.compareTo(o2.campus);
	        if (value1 == 0) {
	            int value2 = fbFriend1.getName().compareTo(fbFriend2.getName());
	            if (value2 == 0) {
	                return value1;
	            } else {
	                return value2;
	            }
	        }
	        return value1;
		}
	*/
	@Override
	public int compare(FBFriend fbFriend1, FBFriend fbFriend2) {
		  return fbFriend1.getName().compareTo(fbFriend2.getName());

		}

}
 