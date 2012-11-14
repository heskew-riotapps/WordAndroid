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
import com.riotapps.word.ui.RowCol;

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
	
	public static eDefaultTile getDefaultTile(int id, TileLayout layout){
		
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
	
	public static RowCol getRowCol(int tileId){
		//0 = 1,1
        //14 = 1,15
        //15 = 2,1
        //16 = 2,2
        //32 = 3,3
        //44 = 3,15 

        int row = Math.round((tileId / 15) + 1);
        int col = Math.round(tileId - ((row - 1) * 15) + 1);

        return new RowCol(row, col);
	}
	
	
	 public static int getTileIdAbove(int tileId)
     {
         //if already on the top row, can't get any lower
         if (tileId < 15) { return 255; }
         return tileId - 15;
     }

     public static int getTileIdBelow(int tileId)
     {
         //if already on the bottom row, can't get any lower
         if (tileId > 209) { return 255; }
         return tileId + 15;
     }
    
     public static int getTileIdToTheRight(int tileId)
     {
         //if already on the far right, can't get any farther right
         if (((tileId + 1) % 15) == 0){ return 255; }
         return tileId + 1;
     }

     public static int getTileIdToTheLeft(int tileId)
     {
         //if already on the far left, can't get any farther left
         if (tileId == 0) { return 255; }
         if ((tileId % 15) == 0) { return 255; }
         return tileId - 1;
     }
     
     public static int getLetterMultiplier(int tileId, TileLayout layout)
     {
    	 switch(getDefaultTile(tileId, layout)){
    		 case FourLetter:
    			 return 4;
    		 case ThreeLetter:
    			 return 3;
    		 case TwoLetter:
    			 return 2;
    		 default:
    			 return 1;
    	 }
    		
     }

     public static int GetWordMultiplier(int tileId, TileLayout layout)
     {
    	 switch(getDefaultTile(tileId, layout)){
			 case ThreeWord:
				 return 3;
			 case TwoWord:
				 return 2;
			 default:
				 return 1;
    	 }
		
     }
	
}
