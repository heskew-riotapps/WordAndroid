package com.riotapps.word.hooks;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;

import com.facebook.android.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.ui.GameTile;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.FileUtils;

public class TileLayoutService {

	public enum eDefaultTile {
		None,
		FourLetter,
		ThreeLetter,
		ThreeWord,
		TwoLetter,
		TwoWord,
		Starter
	}
	
	public TileLayout GetDefaultLayout(Context context){
		 Gson gson = new Gson();
		 Type type = new TypeToken<TileLayout>() {}.getType();
	       
		return gson.fromJson(FileUtils.ReadRawTextFile(context, R.raw.tile_layout), type);
	}
	
	public eDefaultTile GetDefaultTile(int id, TileLayout layout){
		
		for(TileLayout.StarterTile x : layout.StarterTiles) {
	        if(x.getId() == id){return eDefaultTile.Starter;}
	    }
		
		for(TileLayout.BonusTile x : layout.BonusTiles) {
	        if(x.getId() == id){
	        	if (x.getMultiplier() == 4) {return eDefaultTile.FourLetter;}
	        	if (x.getMultiplier() == 3 && x.getScope().equals(Constants.LAYOUT_SCOPE_WORD)) {return eDefaultTile.ThreeWord;}	        	
	        	if (x.getMultiplier() == 3 && x.getScope().equals(Constants.LAYOUT_SCOPE_LETTER)) {return eDefaultTile.ThreeLetter;}
	        	if (x.getMultiplier() == 2 && x.getScope().equals(Constants.LAYOUT_SCOPE_WORD)) {return eDefaultTile.TwoWord;}
	        	if (x.getMultiplier() == 2 && x.getScope().equals(Constants.LAYOUT_SCOPE_LETTER)) {return eDefaultTile.TwoLetter;}
	        }
	    }
		return eDefaultTile.None;
	}
	
}
