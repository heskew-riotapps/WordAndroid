package com.riotapps.word;

import java.util.ArrayList;
import java.util.List;

import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerGame;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StartGame extends FragmentActivity implements View.OnClickListener{
	
	private static final String TAG = StartGame.class.getSimpleName();
	TextView tvStartByNickname;
	Player player;
	Context context = this;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startgame);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);

        
        player = PlayerService.getPlayerFromLocal();
    	PlayerService.loadPlayerInHeader(this);
        
      //  Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	  //  t.show();
        
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
         	 intent = new Intent(this.context, FindPlayer.class);
    	     try {
				intent.putExtra(Constants.EXTRA_GAME, GameService.createGame(context, player));
				 this.context.startActivity(intent);
    	     } 
    	     catch (DesignByContractException e) {
				DialogManager.SetupAlert(this, this.getString(R.string.oops), e.getMessage(), false);
    	     }

			break;
	    case R.id.tvStartByOpponent:  
	    	 intent = new Intent(this.context, FindPlayer.class);
		     try {
					intent.putExtra(Constants.EXTRA_GAME, GameService.createGame(context, player));
					 this.context.startActivity(intent);
		     } 
		     catch (DesignByContractException e) {
					DialogManager.SetupAlert(this, this.getString(R.string.oops), e.getMessage(), false);
		     }

		break;
 
    	}
    	
    }  
    
    
}
        
