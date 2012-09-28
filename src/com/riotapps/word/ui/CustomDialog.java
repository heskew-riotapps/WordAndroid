package com.riotapps.word.ui;

import com.riotapps.word.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomDialog {
	private Dialog dialog;
	private Button bOK;
	private Button bCancel;
	//private Context context;
	//private Boolean onCancelFinishActivity;
	
	public CustomDialog(Context context, String dialogTitle, String dialogText) {
		this(context, dialogTitle, dialogText, false);
	}

	public CustomDialog(Context context, String dialogTitle, String dialogText, Boolean onCancelClickFinishActivity) {
	    final Context ctx = context;
	    final boolean onCancelFinishActivity = onCancelClickFinishActivity;

		this.dialog = new Dialog(ctx, R.style.DialogStyle);
		this.dialog.setContentView(R.layout.twobuttondialog);
		
		TextView title = (TextView) dialog.findViewById(R.id.alert_title);
		title.setText(dialogTitle);
		TextView text = (TextView) dialog.findViewById(R.id.alert_text);
		text.setText(dialogText);

		bOK = (Button) dialog.findViewById(R.id.bOK);
		bCancel = (Button) dialog.findViewById(R.id.bCancel);
		
		
		bCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(onCancelFinishActivity){
	            	((Activity)ctx).finish();
	            }
			}
		});

		ImageView close = (ImageView) dialog.findViewById(R.id.img_close);
		//if button is clicked, close the custom dialog
		close.setOnClickListener(new View.OnClickListener() {
	 		@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(onCancelFinishActivity){
	            	((Activity)ctx).finish();
	            }
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
