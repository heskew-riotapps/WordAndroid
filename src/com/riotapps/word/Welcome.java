package com.riotapps.word;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome  extends Activity {
    /** Called when the activity is first created. */
	
	Button bFB;
	Button bNative;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        bNative = (Button) findViewById(R.id.btnNative);
        bNative.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goToNextActivity = new Intent(getApplicationContext(), JoinNative.class);
				startActivity(goToNextActivity);
			}
		});
        
    }
}

