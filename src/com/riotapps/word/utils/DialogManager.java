package com.riotapps.word.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.riotapps.word.R;

public class DialogManager {
	public static void SetupAlert(Context context, String dialogTitle, String dialogMessage, int closeAfterMilliseconds){
		SetupAlert(context, dialogTitle, dialogMessage, context.getString(R.string.ok, ""), false, closeAfterMilliseconds);
	}

	public static void SetupAlert(Context context, String dialogTitle, String dialogMessage, boolean onOkClickFinishActivity, int closeAfterMilliseconds){
		SetupAlert(context, dialogTitle, dialogMessage, context.getString(R.string.ok, ""), onOkClickFinishActivity, closeAfterMilliseconds);
	}
	
	public static void SetupAlert(Context context, String dialogTitle, String dialogMessage, String okText, boolean onOkClickFinishActivity, int closeAfterMilliseconds){
    	final Dialog dialog = new Dialog(context, R.style.DialogStyle);
    	final boolean onClickFinishActivity = onOkClickFinishActivity;
    	final Context ctx = context;
    	final Timer t = new Timer();
		dialog.setContentView(R.layout.alert);
 
		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.alert_text);
		text.setText(dialogMessage);

		TextView title = (TextView) dialog.findViewById(R.id.alert_title);
		title.setText(dialogTitle);

		TextView ok = (TextView) dialog.findViewById(R.id.alert_ok);
		ok.setText(okText);

		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				t.cancel();
				if(onClickFinishActivity){
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
				t.cancel();
				if(onClickFinishActivity){
	            	((Activity)ctx).finish();
	            }
			}
		});
		

		dialog.show();	

		if (closeAfterMilliseconds > 0){
	            t.schedule(new TimerTask() {
	                public void run() {
	                	dialog.dismiss(); // when the task active then close the dialog
	                    t.cancel(); // also just stop the timer thread, otherwise, you may receive a crash report
	                }
	            }, closeAfterMilliseconds);
			}

		
    }
}


