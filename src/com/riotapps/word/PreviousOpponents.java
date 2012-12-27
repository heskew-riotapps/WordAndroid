package com.riotapps.word;

import java.util.ArrayList;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Opponent;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PreviousOpponents extends FragmentActivity implements View.OnClickListener{
	
	private static final String TAG = PreviousOpponents.class.getSimpleName();
	 
	Player player;
	PreviousOpponents context = this;
	Game game;
	int maxAvailable;
	ImageFetcher imageLoader;
	ArrayList<Integer> selectedIds = new ArrayList<Integer>();

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previousopponents);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);

        Logger.d(TAG, "PreviousOpponents onCreate called");
        
        this.imageLoader = new ImageFetcher(this, Constants.DEFAULT_AVATAR_SIZE, Constants.DEFAULT_AVATAR_SIZE, 0);
        this.imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
        
        player = PlayerService.getPlayerFromLocal();
    	PlayerService.loadPlayerInHeader(this);
   
    	Intent i = getIntent();
    	this.game =  (Game) i.getParcelableExtra(Constants.EXTRA_GAME);
    	//this.itemBGColor =  getResources().getIdentifier(Constants.DRAWABLE_LOCATION + Constants.CONTENT_AREA_BACKGROUND_COLOR, null, null);
    	//this.itemBGSelectedColor =  getResources().getIdentifier(Constants.DRAWABLE_LOCATION + Constants.CONTENT_AREA_BACKGROUND_SELECTED_COLOR, null, null);
    	
    	
      //  Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	  //  t.show();
    	  
    	Button bAdd = (Button)findViewById(R.id.bAdd); 
    	bAdd.setOnClickListener(this);
     
    	TextView tvSubtitle =(TextView)findViewById(R.id.tvSubtitle);
    	 
    	if (this.game.getNumPlayers() == 1 ){
    		tvSubtitle.setText(this.getString(R.string.choose_opponents_up_to_3_subtitle));
    		this.maxAvailable = 3;
       	}
    	else if(this.game.getNumPlayers() == 2) {
    		tvSubtitle.setText(this.getString(R.string.choose_opponents_up_to_2_subtitle));
    		this.maxAvailable = 2;
    	}
    	else {
    		tvSubtitle.setText(this.getString(R.string.choose_opponents_one_more_subtitle));
    		this.maxAvailable = 1;
    		bAdd.setText(this.getString(R.string.choose_previous_opponent_add_button_text));
    	}
     
    	this.loadList(this.player.getOpponents().toArray(new Opponent[this.player.getOpponents().size()])); 
    }
  
    
    @Override 
    public void onClick(View v) {
    
    	switch(v.getId()){  
	    	case R.id.bAdd:  
		    	this.addPlayers();
				break;
    	}
    	
    }  
    /*
    private Player findPlayer(String playerId){
    	for (Opponent opponent : this.player.getOpponents()){
    		if (opponent.getPlayer().getId().equals(playerId)){
    			return opponent.getPlayer();
    		}
    	}
    	
    	return null;
    }
    */
    private void addPlayers(){
		
		try 
		{
			if (this.selectedIds.size() == 0){
				DialogManager.SetupAlert(context, getString(R.string.oops), getString(R.string.choose_fb_friends_choose_1), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);
			}
			else if (this.selectedIds.size() > 3){
				DialogManager.SetupAlert(context, getString(R.string.oops), getString(R.string.validation_too_many_players), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);
			}
			else{
				
				for(int i = 0; i < this.selectedIds.size(); i++){
					//find opponent from opponent list
					this.game = GameService.addPlayerToGame(this, this.game, this.player.getOpponents().get(selectedIds.get(i)).getPlayer());
				}
	
				Intent intent = new Intent(this, com.riotapps.word.AddOpponents.class);
	       	    intent.putExtra(Constants.EXTRA_GAME, this.game);
	    	    this.startActivity(intent);
			}
		} 
		catch (DesignByContractException e) {
			DialogManager.SetupAlert(this, this.getString(R.string.oops), e.getMessage(), true);
		}
	}
    
      
    private void loadList(Opponent[] opponents){
    	OpponentArrayAdapter adapter = new OpponentArrayAdapter(context, opponents);
    
    	ListView lvPreviousOpponents = (ListView) findViewById(R.id.lvPreviousOpponents);
    	lvPreviousOpponents.setAdapter(adapter); 
    	
    	lvPreviousOpponents.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
    
            	  RelativeLayout rlItem = (RelativeLayout) view.findViewById(R.id.rlItem);
            	  
      		      Integer pos = new Integer(position);
      		      if(selectedIds.contains(pos)) {
      		           selectedIds.remove(pos);
      		          
      		           rlItem.setBackgroundColor(Color.parseColor(context.getString(R.color.content_area_background_color)));
      		          // rlItem.setBackgroundResource(itemBGColor);
      		           
      		       }
      		       else {
      		    	   if (maxAvailable == 1){
    		        	   //remove others first
      		    		   selectedIds.clear();
      		    		   ((OpponentArrayAdapter)parent.getAdapter()).notifyDataSetChanged();
    		        	   selectedIds.add(pos);
    		        	   rlItem.setBackgroundColor(Color.parseColor(context.getString(R.color.content_area_background_selected_color)));
    		    		  // rlItem.setBackgroundResource(itemBGSelectedColor);
    		           }
      		    	   else if (maxAvailable == 2 && selectedIds.size() == maxAvailable){
      		    		   Player opponentSelected = player.getOpponents().get(pos).getPlayer();
      		    		   Player opponent1 = player.getOpponents().get(selectedIds.get(0)).getPlayer();
      		    		   Player opponent2 = player.getOpponents().get(selectedIds.get(1)).getPlayer();
      		    		   
      		    		   DialogManager.SetupAlert(context, context.getString(R.string.sorry), 
      		    				   String.format(context.getString(R.string.choose_fb_friends_already_chosen_2, 
      		    						   						opponentSelected.getNameWithMaxLength(23), 
      		    						   						opponent1.getNameWithMaxLength(23),
      		    						   						opponent2.getNameWithMaxLength(23))));
      		    	   }
    		    	   else if (maxAvailable == 3 && selectedIds.size() == maxAvailable){
    		    		   Player opponentSelected = player.getOpponents().get(pos).getPlayer();
      		    		   Player opponent1 = player.getOpponents().get(selectedIds.get(0)).getPlayer();
      		    		   Player opponent2 = player.getOpponents().get(selectedIds.get(1)).getPlayer();
      		    		   Player opponent3 = player.getOpponents().get(selectedIds.get(2)).getPlayer();
 
      		    		   DialogManager.SetupAlert(context, context.getString(R.string.sorry), 
      		    				   String.format(context.getString(R.string.choose_fb_friends_already_chosen_3, 
   				   							opponentSelected.getNameWithMaxLength(23), 
					   						opponent1.getNameWithMaxLength(23),
					   						opponent2.getNameWithMaxLength(23),
					   						opponent3.getNameWithMaxLength(23))));
      		    	   }
      		    	   else {
      		    		   selectedIds.add(pos);
      		    		   rlItem.setBackgroundColor(Color.parseColor(context.getString(R.color.content_area_background_selected_color)));
      		    		 //  rlItem.setBackgroundResource(itemBGSelectedColor);
      		    	   }
      		       }
            }
        });

    }
    
    private class OpponentArrayAdapter extends ArrayAdapter<Opponent> {
	   	  private final PreviousOpponents context;
	   	  private final Opponent[] values;
	   	  LayoutInflater inflater;
	   	//  public ArrayList<Integer> selectedIds = new ArrayList<Integer>();
	
	   	  public OpponentArrayAdapter(PreviousOpponents context, Opponent[] values) {
	   	    super(context, R.layout.previousopponentitem, values);
	    	    this.context = context;
	    	    this.values = values;
	    	    
	    	    this.inflater = (LayoutInflater) context
		    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	  }
	
	    	  @Override
	    	  public View getView(int position, View rowView, ViewGroup parent) {
	    		 
	    		  if ( rowView == null ) {
	    			  rowView = inflater.inflate(R.layout.previousopponentitem, parent, false);
	    		  }
	    	    
	    	   // View rowView = inflater.inflate(R.layout.choosefbfrienditem, parent, false);
	    		  RelativeLayout rlItem = (RelativeLayout) rowView.findViewById(R.id.rlItem);
	    		  
	    		   rlItem.setBackgroundColor(selectedIds.contains(position) ? Color.parseColor(context.getString(R.color.content_area_background_selected_color)) : Color.parseColor(context.getString(R.color.content_area_background_color)));  
	    	    
	    		 // rlItem.setBackgroundResource(selectedIds.contains(position) ? itemBGSelectedColor : itemBGColor);
	    		  
		    	   Opponent opponent = values[position];
		    	 
		    	   TextView tvPlayerName = (TextView) rowView.findViewById(R.id.tvPlayerName);
		    	   tvPlayerName.setText(opponent.getPlayer().getNameWithMaxLength(23));
		    	   
		    	   ImageView ivPlayer = (ImageView)rowView.findViewById(R.id.ivPlayer);
		    	   imageLoader.loadImage(opponent.getPlayer().getImageUrl(), ivPlayer);  
		    	   
		    		
	    		   int badgeId = getResources().getIdentifier("com.riotapps.word:drawable/" + opponent.getPlayer().getBadgeDrawable(), null, null);
	    		   
		    		ImageView ivBadge = (ImageView)rowView.findViewById(R.id.ivPlayerBadge);

	   				Logger.d(TAG, "OpponentArrayAdapter friend drawable=" + opponent.getPlayer().getName() + " " + opponent.getPlayer().getBadgeDrawable() + " badge=" + badgeId + " " + (ivBadge == null));
	   				
		   			ivBadge.setImageResource(badgeId);
		   			
		   			TextView tvPlayerWins = (TextView)rowView.findViewById(R.id.tvPlayerWins);
					if (opponent.getPlayer().getNumWins() == 1){
						tvPlayerWins.setText(context.getString(R.string.line_item_1_win));
					}
					else{
						tvPlayerWins.setText(String.format(context.getString(R.string.line_item_num_wins),opponent.getPlayer().getNumWins())); 
					}
					
		    	   rowView.setTag(opponent.getPlayer().getId());
		    	   return rowView;
	    	  }

    }
}
        
