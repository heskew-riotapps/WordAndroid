package com.riotapps.word;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.facebook.android.*;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.facebook.friendsRequestListener;
import com.riotapps.word.facebook.meRequestListener;

public class Welcome  extends Activity implements View.OnClickListener{
 
	Facebook facebook = new Facebook(Constants.FACEBOOK_API_ID);
	AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
	
    final Context context = this;	
	TextView txtFB;
	TextView txtNative;
	private SharedPreferences mPrefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
 
     //   txtFB = (TextView) findViewById(R.id.byFacebook);
     //   txtFB.setOnClickListener(this);
        txtNative = (TextView) findViewById(R.id.byEmail);
        txtNative.setOnClickListener(this);    
      
        
    }
    
    @Override 
    public void onClick(View v) {
    	switch(v.getId()){  
        case R.id.byFacebook:  
        	this.routeToFacebook();
        	break;
        case R.id.byEmail:  
			Intent intent = new Intent(this, JoinNative.class);
			startActivity(intent);
			break;
//            Toast.makeText(context, "Button 2 pressed ", Toast.LENGTH_SHORT).show();  
  //          break;  
        }  
      }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    private void routeToFacebook() {
    	mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        /*
         * Only call authorize if the access_token has expired.
         */
        if(!facebook.isSessionValid()) {
    	 facebook.authorize(this, new String[] { "email" },
	    	  new DialogListener() {
	             @Override
	             public void onComplete(Bundle values) {
	            	 SharedPreferences.Editor editor = mPrefs.edit();
	                 editor.putString("access_token", facebook.getAccessToken());
	                 editor.putLong("access_expires", facebook.getAccessExpires());
	                 editor.commit();
	               //redirect to authorization if these errors occur
	                 //  User revoked access to your app:
	               //  {"error":{"type":"OAuthException","message":"Error validating access token: User 1053947411 has not authorized application 157111564357680."}}

	               //  OR when password changed:
	               //  {"error":{"type":"OAuthException","message":"Error validating access token: The session is invalid because the user logged out."}}
	                 
	                 // get information about the currently logged in user
	                 mAsyncRunner.request("me", new meRequestListener());

	                 // get the posts made by the "platform" page
	               //  mAsyncRunner.request("platform/posts", new pageRequestListener());

	                 // get the logged-in user's friends
	                 mAsyncRunner.request("me/friends", new friendsRequestListener());
	                 
	             }

	             @Override
	             public void onFacebookError(FacebookError error) {}

	             @Override
	             public void onError(DialogError e) {}

	             @Override
	             public void onCancel() {}
	         });
        }
 
    }
}

