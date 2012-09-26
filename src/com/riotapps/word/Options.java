package com.riotapps.word;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Options extends Activity implements View.OnClickListener{
	 final Context context = this;	
		TextView tvSettings;
		TextView tvQuickRules;
		TextView tvFullRules;

			
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.options);

	        tvSettings = (TextView) findViewById(R.id.tvSettings);
	        tvSettings.setOnClickListener(this);
	        tvQuickRules = (TextView) findViewById(R.id.tvQuickRules);
	        tvQuickRules.setOnClickListener(this); 
	        tvFullRules = (TextView) findViewById(R.id.tvFullRules);
	        tvFullRules.setOnClickListener(this); 
	       
	    }
	
	    @Override 
	    public void onClick(View v) {
	    	Intent intent;
	    	
	    	switch(v.getId()){  
	        case R.id.tvSettings:  
	        	intent = new Intent(this, Settings.class);
				startActivity(intent);
				break;
	       	case R.id.tvQuickRules:  
	       		intent = new Intent(this, QuickRules.class);
        		startActivity(intent);
        		break;
	       	
	       	case R.id.tvFullRules:  
	       		intent = new Intent(this, FullRules.class);
        		startActivity(intent);
        		break;

	    	}
	    	
        }  
	 }

