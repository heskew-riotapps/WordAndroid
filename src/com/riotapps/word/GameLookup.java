package com.riotapps.word;

import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.CustomProgressDialog;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.jeremybrooks.knicker.AccountApi;
import net.jeremybrooks.knicker.KnickerException;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.Knicker.PartOfSpeech;
import net.jeremybrooks.knicker.Knicker.SourceDictionary;
import net.jeremybrooks.knicker.dto.Definition;
import net.jeremybrooks.knicker.dto.TokenStatus;


public class GameLookup extends FragmentActivity  implements View.OnClickListener {
	private static final String TAG = GameLookup.class.getSimpleName();
	private Game game;
	private Player player;
	private ImageFetcher imageLoader;
	private Context context = this;
	private WordnikLookup task;
	private LinearLayout llDefs;
	private TextView tvNotFound;
	private CustomProgressDialog spinner;
	private String word;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamelookup);
		 
		
	 	Intent i = getIntent();
	 	String gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
	 	this.word = i.getStringExtra(Constants.EXTRA_WORD_LOOKUP);
	 	
	 	this.game = GameService.getGameFromLocal(gameId); 
		
	    this.player = PlayerService.getPlayerFromLocal(); 
	 	GameService.loadScoreboard(this, this.game, this.player);
	 	
	 	 this.imageLoader = new ImageFetcher(this, Constants.DEFAULT_AVATAR_SIZE, Constants.DEFAULT_AVATAR_SIZE, 0);
	     this.imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
	 	
	    // Toast toast = Toast.makeText(this, word, 1000);
	    // toast.show();
	     
	     TextView tvWord = (TextView)this.findViewById(R.id.tvWord);
	     tvWord.setText(word);
	     
	     ImageView ivWordnik = (ImageView)this.findViewById(R.id.ivWordnik);
	     ivWordnik.setOnClickListener(this);
	 
	     this.llDefs = (LinearLayout)this.findViewById(R.id.llDefs);
	     this.tvNotFound = (TextView)this.findViewById(R.id.tvNotFound);
	     //tvNotFound.setText(context.getString(R.string.lookup_definition_not_found));
	     
	     System.setProperty("WORDNIK_API_KEY", this.getString(R.string.wordnik_apiKey));
	 
	     this.spinner = new CustomProgressDialog(this);
		 this.spinner.setMessage(this.getString(R.string.progress_looking_up));
		 this.spinner.show();
		 
	     task = new WordnikLookup(word.toLowerCase());
	     task.execute("");
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if (this.spinner != null){
			this.spinner.dismiss();
		}
	}


	private class WordnikLookup extends AsyncTask<String, Void, List<Definition>> {
		 private String word;
		
		  public WordnikLookup(String word){
			this.word = word;
		  }
		
	      @Override
	      protected List<Definition> doInBackground(String... params) {

	    	  List<Definition> def = new ArrayList<Definition>();
			try {
				//Logger.d(TAG, "WordnikLookup definitions about to be called");
				def = WordApi.definitions(word, 60, null, false, null, true, false );
				/*	(String word, int limit,
							   Set<PartOfSpeech> partOfSpeech, boolean includeRelated,
							   Set<SourceDictionary> sourceDictionaries,
							   boolean useCanonical, boolean includeTags)
				*/	 
			} catch (KnickerException e) {
				// TODO Auto-generated catch block
				Logger.d(TAG, "Wordnik lookup error=" + e.toString());
			}
	    	  
	    	  return def;
	      }      

	      @Override
	      protected void onPostExecute(List<Definition> results) {  
	    	  int i = 1;
	    	//  Logger.d(TAG, "wordnik onPostExecute result.size=" + result.size());
	    	  fillDefinitionsView(results);
	    	 
	      }

	      @Override
	      protected void onPreExecute() {
	      }

	      @Override
	      protected void onProgressUpdate(Void... values) {
	      }
	 }

	 public void fillDefinitionsView(List<Definition> results){
		 int i = 1;
		 
		 if (results.size() > 0) {
   		  tvNotFound.setVisibility(View.GONE);
	   	  //if no results....write "no definition found" message
		    	  for (Definition d : results) {
		    		 // Toast toast = Toast.makeText(context, (i++) + ") " + d.getPartOfSpeech() + ": " + d.getText(), 1000);
		    		 // toast.show();
		    		  llDefs.addView(getDefinitionView(i, d));
			          i += 1;
		    	  }   
		    	  llDefs.setVisibility(View.VISIBLE);
	   	  }
	   	  else {
	   		  tvNotFound.setVisibility(View.VISIBLE);
	   		  llDefs.setVisibility(View.GONE);	
	   		  tvNotFound.setText(context.getString(R.string.lookup_definition_not_found));
	   	  }
		 
		 if (spinner != null){
				spinner.dismiss();
		 }
	 }
	
	  public View getDefinitionView(int num, Definition definition ) {

	  		View view = LayoutInflater.from(this).inflate(R.layout.gamelookupitem,null);
	  
		 	TextView tvNum = (TextView)view.findViewById(R.id.tvNum);
		 //TextView tvPartOfSpeech = (TextView)view.findViewById(R.id.tvPartOfSpeech);
		 	TextView tvDefinition = (TextView)view.findViewById(R.id.tvDefinition);
		 	TextView tvAttribution = (TextView)view.findViewById(R.id.tvAttribution);
		 	
		 	tvNum.setText(String.format(this.getString(R.string.lookup_definition_num), num));
		 	tvDefinition.setText(String.format(this.getString(R.string.lookup_definition), definition.getPartOfSpeech(), definition.getText()));
		 	//tvDefinition.setText(definition.getText());
		 	tvAttribution.setText(definition.getAttributionText());
		 
	  	    return view;
	  	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){  
        case R.id.ivWordnik:  
        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(Constants.WORDNIK_WORD_URL, this.word.toLowerCase())));
	   		startActivity(browserIntent);
        	break;
		}
		
	}
	
}