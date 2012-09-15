package com.riotapps.word;

import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainLanding extends Activity implements View.OnClickListener{

	TextView tvStartByNickname;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlanding);
        
       // SharedPreferences settings = getSharedPreferences(Constants.USER_PREFS, 0);

        PlayerService playerSvc = new PlayerService();
        Player player = playerSvc.GetPlayerFromLocal();
        
        Toast t = Toast.makeText(this, "Hello " + player.getNickname(), Toast.LENGTH_LONG);  
	    t.show();
        
       
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
    	Intent goToActivity;
    	
    	switch(v.getId()){  
        case R.id.tvStartByNickname:  
        	goToActivity = new Intent(getApplicationContext(), FindPlayer.class);
			startActivity(goToActivity);
			break;
       	

    	}
    	
    }  
    
}
        
