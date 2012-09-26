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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class StartGame extends Activity implements View.OnClickListener{

	TextView tvStartByNickname;
	Player contextPlayer;
	Context context = this;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startgame);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);

        PlayerService playerSvc = new PlayerService();
        contextPlayer = playerSvc.getPlayerFromLocal();
        
      //  Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	  //  t.show();
        
       
        TextView tvFB =(TextView)findViewById(R.id.startByFacebook);
        TextView tvByOpponent =(TextView)findViewById(R.id.startByOpponent);
        tvStartByNickname =(TextView)findViewById(R.id.tvStartByNickname);
        
         tvStartByNickname.setOnClickListener(this);
        
        //if not in by facebook, hide this option
      //  tvFB.setVisibility(View.GONE);
        //if num complete games = zero, hide this option
      //  tvByOpponent.setVisibility(View.GONE);
        
    }
    
    
    @Override 
    public void onClick(View v) {

    	switch(v.getId()){  
        case R.id.tvStartByNickname:  
          	 Intent intent = new Intent(this.context, FindPlayer.class);
     	     try {
				intent.putExtra(Constants.EXTRA_GAME, GameService.createGame(context, contextPlayer));
				 this.context.startActivity(intent);
     	     } 
     	     catch (DesignByContractException e) {
				DialogManager.SetupAlert(this, this.getString(R.string.oops), e.getMessage(), false);
     	     }
 
			break;
    	}
    	
    }  
    
    
}
        
