package com.riotapps.word;

 
import com.google.ads.*;
import com.google.ads.AdRequest.ErrorCode;
import com.riotapps.word.utils.Logger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
 

public class InterstitialAd extends FragmentActivity  implements AdListener{
	private static final String TAG = InterstitialAd.class.getSimpleName();
	private final Context context = this;
	 private com.google.ads.InterstitialAd interstitial;

	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.interstitialad);
	        
	        // Create the interstitial
	        interstitial = new  com.google.ads.InterstitialAd(this, "a150e6e3f4832b4");

	        // Create ad request
	        AdRequest adRequest = new AdRequest();

	        // Begin loading your interstitial
	        interstitial.loadAd(adRequest);

	        // Set Ad Listener to use the callbacks below
	        interstitial.setAdListener(this);
	      }
 

	@Override
	public void onReceiveAd(Ad ad) {
	  Logger.d(TAG, "Received ad");
	  if (ad == interstitial) {
	    interstitial.show();
	  }
	}


	@Override
	public void onDismissScreen(Ad arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		// TODO Auto-generated method stub
		 Logger.d(TAG, "failed to receive ad");
		 try {
			this.finish();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void onLeaveApplication(Ad arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPresentScreen(Ad arg0) {
		// TODO Auto-generated method stub
		
	}

}
