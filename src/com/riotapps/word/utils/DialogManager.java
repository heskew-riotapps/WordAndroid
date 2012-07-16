package com.riotapps.word.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.riotapps.word.R;

public class DialogManager {
	public static void SetupAlert(Context context, String dialogTitle, String dialogMessage){
		SetupAlert(context, dialogTitle, dialogMessage, context.getString(R.string.ok, ""));
	}
	
	public static void SetupAlert(Context context, String dialogTitle, String dialogMessage, String okText){
    	final Dialog dialog = new Dialog(context, R.style.DialogStyle);
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
//		Button dialogButton = (Button) dialog.findViewById(R.id.bAlertOk);
//		//if button is clicked, close the custom dialog
//		dialogButton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});

		dialog.show();	

    }
}


