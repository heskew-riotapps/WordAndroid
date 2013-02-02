package com.riotapps.word.ui;

import com.riotapps.word.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomDialog{
	private Dialog dialog;
	private AlertDialog alert;
	private Button bOK;
	private Button bCancel;
	private ImageView close;
	//private Context context;
	//private Boolean onCancelFinishActivity;
	
	public CustomDialog(Context context, String dialogTitle, String dialogText, String okText, String cancelText) {
		this(context, dialogTitle, dialogText, false, okText, cancelText);
	}
	
	public CustomDialog(Context context, String dialogTitle, String dialogText) {
		this(context, dialogTitle, dialogText, false, context.getString(R.string.ok), context.getString(R.string.cancel));
	}
 
	public CustomDialog(Context context, String dialogTitle, String dialogText, Boolean onCancelClickFinishActivity, String okText, String cancelText) {
		
		final Context ctx = context;
	    final boolean onCancelFinishActivity = onCancelClickFinishActivity;

	   
		this.dialog = new Dialog(ctx, R.style.DialogStyle);
		this.dialog.setContentView(R.layout.twobuttondialog);
		
		TextView title = (TextView) dialog.findViewById(R.id.alert_title);
		title.setText(dialogTitle);
		TextView text = (TextView) dialog.findViewById(R.id.alert_text);
		text.setText(dialogText);

		bOK = (Button) dialog.findViewById(R.id.bOK);
		bOK.setText(okText);
		bCancel = (Button) dialog.findViewById(R.id.bCancel);
		bCancel.setText(cancelText);
		
		bCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(onCancelFinishActivity){
	            	((Activity)ctx).finish();
	            }
			}
		});

		
		this.close = (ImageView) dialog.findViewById(R.id.img_close);
		//if button is clicked, close the custom dialog
		this.close.setOnClickListener(new View.OnClickListener() {
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
	
	public void setOnDismissListener(OnClickListener onClick){
		this.close.setOnClickListener(onClick);
		this.bCancel.setOnClickListener(onClick);
	}
	
	public boolean isShowing(){
		return this.dialog.isShowing();
	}
	
	public void setOnCancelListener(OnCancelListener onCancel){
		this.dialog.setOnCancelListener(onCancel);
 
	}

}
