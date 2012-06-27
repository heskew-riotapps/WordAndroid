package com.riotapps.word;

import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.ApplicationContext;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class JoinNative extends Activity {
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
					// TODO Auto-generated method stub
					PlayerService playerSvc = new PlayerService();
					
					tEmail = (EditText) findViewById(R.id.tEmail);
					
				 	playerSvc.PutPlayer(tEmail.getText().toString());
				 	
				 	
				 	
				 	final Dialog dialog = new Dialog(context, R.style.DialogStyle);
					//dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.dialog);
			 
		 
					// set the custom dialog components - text, image and button
					TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
					text.setText("Android custom dialog example!");

					TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
					title.setText("title");
				//	ImageView image = (ImageView) dialog.findViewById(R.id.image);
				//	image.setImageResource(R.drawable.ic_launcher);
		 
					ImageView close = (ImageView) dialog.findViewById(R.id.img_close);
					//if button is clicked, close the custom dialog
					close.setOnClickListener(new View.OnClickListener() {
				 		@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					
					Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
					//if button is clicked, close the custom dialog
					dialogButton.setOnClickListener(new View.OnClickListener() {
				 		@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
		 
					dialog.show();
				 	
					
				}
			});
	    }

	//	@Override
	//	protected Dialog onCreateDialog(int id) {
	//		// TODO Auto-generated method stub
	//		return super.onCreateDialog(id);
	//	}
	}
