package com.riotapps.word;

import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.Logger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartGame extends FragmentActivity implements View.OnClickListener{
	
	private static final String TAG = StartGame.class.getSimpleName();
	TextView tvStartByNickname;
	Player player;
	Context context = this;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startgame);
         
        player = PlayerService.getPlayerFromLocal();
    	PlayerService.loadPlayerInHeader(this);
        
      //  Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	  //  t.show();
        
    	   TextView tvFB =(TextView)findViewById(R.id.tvStartByFacebook);
          // TextView tvRandom =(TextView)findViewById(R.id.tvStartByRandom);
           
           TextView tvByOpponent =(TextView)findViewById(R.id.tvStartByOpponent);
           tvStartByNickname = (TextView)findViewById(R.id.tvStartByNickname);
           tvStartByNickname.setOnClickListener(this);
           LinearLayout llButtons = (LinearLayout)findViewById(R.id.llButtons);
           Button bBadges = (Button)findViewById(R.id.bBadges);
           Button bOptions = (Button)findViewById(R.id.bOptions);
           
            //not ready for this option
        //    tvRandom.setVisibility(View.GONE);
           //if not in by facebook, hide this option
            if (player.getTotalNumLocalGames() == 0){
            	bBadges.setOnClickListener(this);
            	bOptions.setOnClickListener(this);
            }
            else{
            	//only show first time around, since main landing is skipped
            	llButtons.setVisibility(View.GONE);
            }
           
            if (!player.isFacebookUser()){
           	 tvFB.setVisibility(View.GONE);
            }
            else{
           	 tvFB.setOnClickListener(this);
            }
            Logger.d(TAG, "onCreate opponents=" + this.player.getOpponents().size());
            
            if (this.player.getOfficialOpponents().size() == 0){
           	 tvByOpponent.setVisibility(View.GONE);
            }
            else {
           	 tvByOpponent.setOnClickListener(this);
            }
    
     
    }
    
    
    @Override 
    public void onClick(View v) {
    	Intent intent;
    	switch(v.getId()){  
        case R.id.tvStartByNickname:  
          	 intent = new Intent(this.context, FindPlayer.class);
     	     try {
				intent.putExtra(Constants.EXTRA_GAME, GameService.createGame(context, player));
				 this.context.startActivity(intent);
     	     } 
     	     catch (DesignByContractException e) {
				DialogManager.SetupAlert(this, this.getString(R.string.oops), e.getMessage(), false);
     	     }
 
			break;
        case R.id.tvStartByFacebook:  
         	 intent = new Intent(this.context, ChooseFBFriends.class);
    	     try {
				intent.putExtra(Constants.EXTRA_GAME, GameService.createGame(context, player));
				 this.context.startActivity(intent);
    	     } 
    	     catch (DesignByContractException e) {
				DialogManager.SetupAlert(this, this.getString(R.string.oops), e.getMessage(), false);
    	     }

			break;
	    case R.id.tvStartByOpponent:  
	    	 intent = new Intent(this.context, PreviousOpponents.class);
		     try {
					intent.putExtra(Constants.EXTRA_GAME, GameService.createGame(context, player));
					 this.context.startActivity(intent);
		     } 
		     catch (DesignByContractException e) {
					DialogManager.SetupAlert(this, this.getString(R.string.oops), e.getMessage(), false);
		     }
		     break;
	    case R.id.bBadges:  
        	intent = new Intent(getApplicationContext(), Badges.class);
			startActivity(intent);
			break; 
        case R.id.bOptions:  
        	intent = new Intent(getApplicationContext(), Options.class);
			startActivity(intent);
			break;
 
    	}
    	
    }  
    
    
}
        
