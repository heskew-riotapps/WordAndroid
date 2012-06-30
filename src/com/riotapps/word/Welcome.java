package com.riotapps.word;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Welcome  extends Activity {
    /** Called when the activity is first created. */
	
	Button bFB;
	Button bNative;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        

        TextView tvWelcome =(TextView)findViewById(R.id.welcome_msg);
        TextView tvSubWelcome =(TextView)findViewById(R.id.welcome_sub_msg);
        TextView tvFB =(TextView)findViewById(R.id.byFacebook);
        TextView tvNative =(TextView)findViewById(R.id.byEmail);
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/FullDeceSans1.0.ttf");

        tvWelcome.setTypeface(face);
        tvSubWelcome.setTypeface(face);
        tvFB.setTypeface(face);
        tvNative.setTypeface(face);
        

        
     //   bNative = (Button) findViewById(R.id.btnNative);
     //   bNative.setOnClickListener(new View.OnClickListener() {
			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent goToNextActivity = new Intent(getApplicationContext(), JoinNative.class);
//				startActivity(goToNextActivity);
//			}
//		});
        
    }
}

