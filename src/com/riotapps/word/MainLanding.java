package com.riotapps.word;

import java.util.List;

import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.PlayerService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainLanding extends FragmentActivity implements View.OnClickListener{
	private static final String TAG = MainLanding.class.getSimpleName();
	TextView tvStartByNickname;
	Button bStart;
	Button bOptions;
	Button bBadges;
	ImageView ivContextPlayer;
	ImageView ivContextPlayerBadge;
	Context context = this;
	
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
		
		
		ListView listYourTurn = (ListView) findViewById(R.id.listYourTurn);
		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
		  "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
		  "Linux", "OS/2" };
		//go get games
		//check locally, if array(s) is empty hit the server
		//set up list views
		
		//do this in callback listener from network task, if array is empty
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third parameter - ID of the TextView to which the data is written
		// Fourth - the Array of data
		List<Game> games = GameService.getGamesFromLocal();
		if (games.size() > 0){
			 
			YourTurnArrayAdapter adapter = new YourTurnArrayAdapter(context, (Game[]) games.toArray());
	
			// Assign adapter to ListView
			listYourTurn.setAdapter(adapter); 
		}
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
    
    private class YourTurnArrayAdapter extends ArrayAdapter<Game> {
    	  private final Context context;
    	  private final Game[] values;

    	  public YourTurnArrayAdapter(Context context, Game[] values) {
    	    super(context, R.layout.gameyourturnlistitem, values);
    	    this.context = context;
    	    this.values = values;
    	  }

    	  @Override
    	  public View getView(int position, View convertView, ViewGroup parent) {
    	    LayoutInflater inflater = (LayoutInflater) context
    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	    View rowView = inflater.inflate(R.layout.gameyourturnlistitem, parent, false);
    	    ImageView ivOpponent1 = (ImageView) rowView.findViewById(R.id.ivOpponent1);
    	   // textView.setText(values[position]);
    	

    	    return rowView;
    	  }
    	} 
    
}
        
