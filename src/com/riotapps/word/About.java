package com.riotapps.word;

import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.Constants;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
 

public class About extends FragmentActivity implements View.OnClickListener{
	
	private final Context context = this;
	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.about);
	        
	        PlayerService.loadPlayerInHeader(this);
	        
	        TextView tvSupportText = (TextView)this.findViewById(R.id.tvSupportText);
	        TextView tvSupportLink = (TextView)this.findViewById(R.id.tvSupportLink);
	        TextView tvVersion = (TextView)this.findViewById(R.id.tvVersion);
	        TextView tvBuildNumber = (TextView)this.findViewById(R.id.tvBuildNumber);
	        
	        tvBuildNumber.setText(String.format(this.getString(R.string.about_build_number), this.getString(R.string.build)));
	        tvVersion.setText(String.format(this.getString(R.string.about_version), this.getString(R.string.version)));	        
	
	        tvSupportLink.setOnClickListener(this);
	        tvSupportText.setOnClickListener(this);
	 }


	@Override
	public void onClick(View v) {
		switch(v.getId()){  
        case R.id.tvSupportLink:  
        case R.id.tvSupportText:  
        	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.SUPPORT_SITE_URL));
	   		startActivity(browserIntent);
			break;
		}
		
	}


	@Override
	protected void onStart() {
		 
		super.onStart();
		 EasyTracker.getInstance().activityStart(this);
	}


	@Override
	protected void onStop() {
	 
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	
	
}
