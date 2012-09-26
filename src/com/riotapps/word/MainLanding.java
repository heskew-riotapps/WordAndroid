package com.riotapps.word;

import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        Player player = PlayerService.getPlayerFromLocal();
        
        Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	    t.show();
        
	    bStart = (Button) findViewById(R.id.bStart);
	    bOptions = (Button) findViewById(R.id.bOptions);
	    bBadges = (Button) findViewById(R.id.bBadges);
	    
	    bStart.setOnClickListener(this);
		bOptions.setOnClickListener(this);
		bBadges.setOnClickListener(this);
		
		ImageFetcher imageLoader = new ImageFetcher(this, 30, 30, 6);
		imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
		ImageView ivContextPlayer = (ImageView) findViewById(R.id.ivHeaderContextPlayer);
		//android.util.Log.i(TAG, "FindPlayerResults: playerImage=" + player.getImageUrl());
		
		imageLoader.loadImage(player.getImageUrl(), ivContextPlayer); //default image
		
		ImageView ivContextPlayerBadge = (ImageView) findViewById(R.id.ivHeaderContextPlayerBadge);
		int contextPlayerBadgeId = getResources().getIdentifier("com.riotapps.word:drawable/" + player.getBadgeDrawable(), null, null);
		ivContextPlayerBadge.setImageResource(contextPlayerBadgeId);

		TextView tvHeaderContextPlayerName = (TextView) findViewById(R.id.tvHeaderContextPlayerName);
		tvHeaderContextPlayerName.setText(player.getAbbreviatedName());
		
		TextView tvHeaderContextPlayerWins = (TextView) findViewById(R.id.tvHeaderContextPlayerWins);
		tvHeaderContextPlayerName.setText(player.getAbbreviatedName());
 
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
        
