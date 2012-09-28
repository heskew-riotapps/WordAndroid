package com.riotapps.word;

import com.riotapps.word.hooks.PlayerService;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class FullRules extends Activity{
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fullrules);
	        
	        TextView t2 = (TextView) findViewById(R.id.fullRules2);
	        t2.setMovementMethod(LinkMovementMethod.getInstance());
	        
	        TextView fullRulesDictionary2 = (TextView) findViewById(R.id.fullRulesDictionary2);
	        fullRulesDictionary2.setMovementMethod(LinkMovementMethod.getInstance());

			PlayerService.loadPlayerInHeader(this);  
	 }
	    
}
