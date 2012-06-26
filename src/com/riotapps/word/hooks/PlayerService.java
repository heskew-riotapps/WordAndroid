package com.riotapps.word.hooks;

import android.R;

import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.PreconditionException;
import com.riotapps.word.utils.Check;
 

public class PlayerService {

	public Player GetPlayer(String id){
		//retrieve player from server
		//convert using gson
		//return player to caller
		return new Player();
	}
	
	
	public Player SavePlayer(String id){
		//retrieve player from server
		//convert using gson
		//return player to caller
		
		//check validations here
		
		return new Player();
	}
	
	
	public Player PutPlayer(String email, String nickname, String password) throws DesignByContractException{
		//retrieve player from server
		//convert using gson
		//return player to caller
		Player player = new Player();
		
		player.setEmail(email);
	//	player.se
		
		Check.Require(player.getEmail().length() > 0,  R.string.validation_email_required);
	 
		
		
		//check validations here
		
		return new Player();
	}
}
