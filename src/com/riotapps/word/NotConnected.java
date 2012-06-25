package com.riotapps.word;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotConnected  extends Activity {
    /** Called when the activity is first created. */
	
	Button bTryAgain;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notconnected);
        
       bTryAgain = (Button) findViewById(R.id.bTryAgain);
        bTryAgain.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent splash = new Intent(getApplicationContext(), Splash.class);
				startActivity(splash);
				finish();
			}
		});
        
     }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}  
}
