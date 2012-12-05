package com.riotapps.word.ui;

import java.util.List;

import com.riotapps.word.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
 

public class SwapDialog {
	private Dialog dialog;
	private Button bOK;
	private Button bCancel;
	//private Context context;
	//private Boolean onCancelFinishActivity;
	
 
	public SwapDialog(Context context, List<String> letters) {
	    final Context ctx = context;

		this.dialog = new Dialog(ctx, R.style.DialogStyle);
		this.dialog.setContentView(R.layout.swapdialog);
		
		 //loop through letters, filling the views

		bOK = (Button) dialog.findViewById(R.id.bOK);

		
		bCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		ImageView close = (ImageView) dialog.findViewById(R.id.img_close);
		//if button is clicked, close the custom dialog
		close.setOnClickListener(new View.OnClickListener() {
	 		@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	
	public void show(){
		this.dialog.show();	
	}
	
	public void dismiss(){
		this.dialog.dismiss();	
	}
	
	public void setOnOKClickListener(OnClickListener onClick){
		this.bOK.setOnClickListener(onClick);
	}

}
