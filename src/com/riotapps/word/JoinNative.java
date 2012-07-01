package com.riotapps.word;

import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.DesignByContractException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class JoinNative extends Activity implements View.OnClickListener{
	    /** Called when the activity is first created. */
		
	    final Context context = this;		
		Button bCancel; 
		Button bSave;
		EditText tEmail;
		EditText tNickname;
		EditText tPassword;
		
		//EditText 
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.joinnative);
	        
	        bCancel = (Button) findViewById(R.id.bCancel);
	        bSave = (Button) findViewById(R.id.bSave);
	        
	        bCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();      
				}
			});
	        
	        
	        bSave.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PlayerService playerSvc = new PlayerService();
					
					EditText tEmail = (EditText) findViewById(R.id.tEmail);
					EditText tNickname = (EditText) findViewById(R.id.tNickname);
					EditText tPassword = (EditText) findViewById(R.id.tPassword);
					
					try{
						playerSvc.PutPlayer(tEmail.getText().toString(), 
								tNickname.getText().toString(), 
								tPassword.getText().toString());
					} 
					catch (DesignByContractException dbEx) {
					 	DialogManager.SetupOKDialog(context, getString(R.string.oops), dbEx.getMessage());					}
					catch (Exception ex) {
						//log this, figure out logging
					 	DialogManager.SetupOKDialog(context, getString(R.string.error_title), getString(R.string.error_message));						
					}
				 
				}
			});
	    }

	    @Override 
	    public void onClick(View v) {
	        // do something when the button is clicked
	      }
	    
	}
