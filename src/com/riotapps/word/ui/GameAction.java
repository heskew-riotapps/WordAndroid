package com.riotapps.word.ui;

import java.util.TreeMap;
 
public class GameAction {
	private int code;
	
	public enum GameActionType{
		NO_TRANSLATION(0),
		CANCEL_GAME(1),
		DECLINE_GAME(2),
		RESIGN(3),
		PLAY(4),
		SKIP(5),
		SWAP(6);
				
		private final int value;
		private GameActionType(int value) {
		    this.value = value;
		 }
		
	  public int value() {
		    return value;
		  }
		
	  private static TreeMap<Integer, GameActionType> _map;
	  static {
		_map = new TreeMap<Integer, GameActionType>();
	    for (GameActionType num: GameActionType.values()) {
	    	_map.put(new Integer(num.value()), num);
	    }
	    //no translation
	    if (_map.size() == 0){
	    	_map.put(new Integer(0), NO_TRANSLATION);
	    }
	  }
	  
	  public static GameActionType lookup(int value) {
		  return _map.get(new Integer(value));
	  	}
	}
	
	
	public GameActionType getType(){
		return GameActionType.lookup(this.code);
	}
}