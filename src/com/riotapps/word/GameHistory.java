package com.riotapps.word;

 
import java.util.Collections;

import com.riotapps.word.hooks.FBFriend;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.PlayedWord;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GameHistory extends FragmentActivity{
	private static final String TAG = GameHistory.class.getSimpleName();
	private Game game;
	private Player player;
	private ImageFetcher imageLoader;
	private ListView lvWords;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamehistory);
		 
		
	 	Intent i = getIntent();
	 	String gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
	 	this.game = GameService.getGameFromLocal(gameId); 
		
	    this.player = PlayerService.getPlayerFromLocal(); 
	 	GameService.loadScoreboard(this, this.game, this.player);
	 	
	 	 this.imageLoader = new ImageFetcher(this, Constants.DEFAULT_AVATAR_SIZE, Constants.DEFAULT_AVATAR_SIZE, 0);
	     this.imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
	 	
	 	this.loadList();
	}

 

@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		lvWords = null;
		super.onBackPressed();
	}



private void loadList(){
 
	
	Collections.reverse(this.game.getPlayedWords());
 
	PlayedWordArrayAdapter adapter = new PlayedWordArrayAdapter(this, this.game.getPlayedWords().toArray(new PlayedWord[this.game.getPlayedWords().size()]));

	this.lvWords = (ListView) findViewById(R.id.lvWords);
	this.lvWords.setAdapter(adapter); 
	
/*	lvWords.setOnItemClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
        	//this will eventually call wordnik for definition lookup
        	 
        }
    });
*/
}

private class PlayedWordArrayAdapter extends ArrayAdapter<PlayedWord> {
   	  private final GameHistory context;
   	  private final PlayedWord[] values;
   	  private final int wordCount;
   	  LayoutInflater inflater;
   	//  public ArrayList<Integer> selectedIds = new ArrayList<Integer>();

   	  public PlayedWordArrayAdapter(GameHistory context, PlayedWord[] values) {
   	    super(context, R.layout.gamehistoryitem, values);
    	    this.context = context;
    	    this.values = values;
    	    this.wordCount = values.length;
    	    
    	    this.inflater = (LayoutInflater) context
	    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	  }

    	  @Override
    	  public View getView(int position, View rowView, ViewGroup parent) {
    		 
    		  if ( rowView == null ) {
    			  rowView = inflater.inflate(R.layout.gamehistoryitem, parent, false);
    		  }
    		  
	    	  PlayedWord word = values[position];
	    	 
	    	   TextView tvWord = (TextView) rowView.findViewById(R.id.tvWord);
	    	   TextView tvTurnInfo = (TextView) rowView.findViewById(R.id.tvTurnInfo);
	    	   
	    	   Player player = context.game.getPlayerById(word.getPlayerId());
	    	   
	    	   ImageView ivPlayer = (ImageView)rowView.findViewById(R.id.ivPlayer);
	    	   imageLoader.loadImage(player.getImageUrl(), ivPlayer); 
	    	   
	    	   tvWord.setText(word.getWord());
	    	   tvTurnInfo.setText(String.format(this.context.getString(R.string.game_history_turn_info), player.getName(), word.getTurn(), word.getPointsScored()));
 
	    	   Logger.d(TAG, "adapter position=" + position + " count=" + this.wordCount); 
	    	//   LinearLayout llBottomBorder = (LinearLayout)rowView.findViewById(R.id.llBottomBorder);
	    	 
	    	    
	    	  /* if (position == this.wordCount - 1){ //last item
	    		   Logger.d(TAG, "position=wordCount");
		   			RelativeLayout rlLineItem = (RelativeLayout)rowView.findViewById(R.id.rlItem);
		   			int bgLineItem = context.getResources().getIdentifier("com.riotapps.word:drawable/text_selector_bottom", null, null);
		   			rlLineItem.setBackgroundResource(bgLineItem);
		   			//LinearLayout llBottomBorder = (LinearLayout)rowView.findViewById(R.id.llBottomBorder);
		   			llBottomBorder.setVisibility(View.INVISIBLE);
	    	   }
	    	   else{
	    		   llBottomBorder.setVisibility(View.VISIBLE);
	    	   }
	    	  */
	    	   
	    	   return rowView;
    	  } 
} 		  
 
    }