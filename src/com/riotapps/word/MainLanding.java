package com.riotapps.word;

import com.riotapps.word.hooks.PlayerService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainLanding extends Activity implements View.OnClickListener{

	TextView tvStartByNickname;
	Button bStart;
	Button bOptions;
	Button bBadges;
	ImageView ivContextPlayer;
	ImageView ivContextPlayerBadge;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlanding);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);

        //PlayerService playerSvc = new PlayerService();
        //Player player = PlayerService.getPlayerFromLocal();
        
       // Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	   // t.show();
        
	    bStart = (Button) findViewById(R.id.bStart);
	    bOptions = (Button) findViewById(R.id.bOptions);
	    bBadges = (Button) findViewById(R.id.bBadges);
	    
	    bStart.setOnClickListener(this);
		bOptions.setOnClickListener(this);
		bBadges.setOnClickListener(this);
		
		PlayerService.loadPlayerInHeader(this);
		
    }
    
    
    @Override 
    public void onClick(View v) {
    	Intent intent;
    	
    	switch(v.getId()){  
        case R.id.bStart:  
        	intent = new Intent(getApplicationContext(), StartGame.class);
			startActivity(intent);
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
        
