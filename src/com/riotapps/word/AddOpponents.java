package com.riotapps.word;

import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AddOpponents extends FragmentActivity implements View.OnClickListener{
	
	private static final String TAG = AddOpponents.class.getSimpleName();
	TextView tvStartByNickname;
	Player player;
	Context context = this;
	Game game;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addopponents);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);

        
        player = PlayerService.getPlayerFromLocal();
    	PlayerService.loadPlayerInHeader(this);
   
    	Intent i = getIntent();
    	this.game =  (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
    	
    	
      //  Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	  //  t.show();
    	  TextView tvTitle =(TextView)findViewById(R.id.tvTitle);
    	  TextView tvSubtitle =(TextView)findViewById(R.id.tvSubtitle);
    	  TextView tvOpponentsAlreadyAddedTitle =(TextView)findViewById(R.id.tvOpponentsAlreadyAddedTitle);
    	  if (this.game.getPlayerGames().size() < 3){
    		  tvTitle.setText(this.getString(R.string.add_opponents_add_x_more_title));
    		  tvSubtitle.setText(String.format(this.getString(R.string.add_opponents_add_x_more_subtitle), 3 - this.game.getPlayerGameOpponentsArray().length));
       	  }
    	  else {
    		  tvTitle.setText(this.getString(R.string.add_opponents_add_one_more_title));
    		  tvSubtitle.setText(this.getString(R.string.add_opponents_add_one_more_subtitle));
    	  }
    	  if (this.game.getPlayerGames().size() > 2){
    		  tvOpponentsAlreadyAddedTitle.setText(this.getString(R.string.add_opponents_add_x_more_title)); 		    
    	  }
    	  else{
    		  tvOpponentsAlreadyAddedTitle.setText(this.getString(R.string.add_opponents_already_added_one_title));
    	  }
    	  
    	   TextView tvFB =(TextView)findViewById(R.id.tvStartByFacebook);
          // TextView tvRandom =(TextView)findViewById(R.id.tvStartByRandom);
           
           TextView tvByOpponent =(TextView)findViewById(R.id.tvStartByOpponent);
           tvStartByNickname =(TextView)findViewById(R.id.tvStartByNickname);
           tvStartByNickname.setOnClickListener(this);
           
         
            //not ready for this option
        //    tvRandom.setVisibility(View.GONE);
           //if not in by facebook, hide this option
            if (!player.isFacebookUser()){
           	 tvFB.setVisibility(View.GONE);
            }
            else{
           	 tvFB.setOnClickListener(this);
            }
            
            if (this.player.getOpponents().size() == 0){
           	 tvByOpponent.setVisibility(View.GONE);
            }
            else {
           	 tvByOpponent.setOnClickListener(this);
            }
            
            ListView listPlayers = (ListView)findViewById(R.id.listPlayers);
        	PlayerArrayAdapter adapter = new PlayerArrayAdapter(this, this.game.getPlayerGameOpponentsArray());
        	listPlayers.setAdapter(adapter); 
    }
    
    
    @Override 
    public void onClick(View v) {
    	Intent intent;
    	switch(v.getId()){  
        case R.id.tvStartByNickname:  
          	intent = new Intent(this.context, FindPlayer.class);
          	intent.putExtra(Constants.EXTRA_GAME, this.game);
			this.context.startActivity(intent);
			break;
        case R.id.tvStartByFacebook:  
        	intent = new Intent(this.context, FindPlayer.class);
        	intent.putExtra(Constants.EXTRA_GAME, this.game);
			this.context.startActivity(intent);
			break;
	    case R.id.tvStartByOpponent:  
	    	intent = new Intent(this.context, FindPlayer.class);
			intent.putExtra(Constants.EXTRA_GAME, this.game);
			this.context.startActivity(intent);
			break;
    	}
    	
    }  
    
    private class PlayerArrayAdapter extends ArrayAdapter<PlayerGame> {
  	  private final FragmentActivity context;
  	  private final PlayerGame[] values;

  	  public PlayerArrayAdapter(FragmentActivity context, PlayerGame[] values) {
  	    super(context, R.layout.playerlistitem, values);
  	    this.context = context;
  	    this.values = values;
  	  }

  	  @Override
  	  public View getView(int position, View convertView, ViewGroup parent) {
  	    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  	    View rowView = inflater.inflate(R.layout.playerlistitem, parent, false);
  	    
  	   
  	    
  	    Player opponent =  this.values[position].getPlayer();
  	    TextView tvPlayerName = (TextView)rowView.findViewById(R.id.tvPlayerName);
	 	tvPlayerName.setText(opponent.getName());
	 	
	 	 Logger.w(TAG, "position=" + position + " opponent=" + opponent.getName());
	 	
		TextView tvPlayerWins = (TextView)rowView.findViewById(R.id.tvPlayerWins);
		tvPlayerWins.setText(String.format(this.context.getString(R.string.line_item_num_wins),opponent.getNumWins()));
	 	
	 	ImageFetcher imageLoader = new ImageFetcher(this.context, 34, 34, 0);
		imageLoader.setImageCache(ImageCache.findOrCreateCache(this.context, Constants.IMAGE_CACHE_DIR));
		ImageView ivPlayer = (ImageView) rowView.findViewById(R.id.ivPlayer);
		
		imageLoader.loadImage(opponent.getImageUrl(), ivPlayer); //default image
		
		int badgeId = getResources().getIdentifier("com.riotapps.word:drawable/" + opponent.getBadgeDrawable(), null, null);
		ImageView ivBadge = (ImageView)rowView.findViewById(R.id.ivBadge);	 
		ivBadge.setImageResource(badgeId);
	 
  	    return rowView;
  	  }
  }
}
        
