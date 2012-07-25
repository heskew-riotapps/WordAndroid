package com.riotapps.word;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Badges extends Activity implements View.OnClickListener{
	
	final Context context = this;
	Button bBack;
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.badges);
	        
	        bBack = (Button) findViewById(R.id.bBack);
	        bBack.setOnClickListener(this);
	 }
	 
	  @Override 
	    public void onClick(View v) {
	    	switch(v.getId()){  
	        case R.id.bBack:  
	        	((Activity)context).finish();
				break;
	    	}
      }  

}
