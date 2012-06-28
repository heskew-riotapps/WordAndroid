package com.riotapps.word;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.riotapps.word.R;

public class DialogManager {
	public static void SetupOKDialog(Context context, String dialogTitle, String dialogMessage){
    	final Dialog dialog = new Dialog(context, R.style.DialogStyle);
		dialog.setContentView(R.layout.dialog);
 
		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
		text.setText(dialogMessage);

		TextView title = (TextView) dialog.findViewById(R.id.dialog_title);
		title.setText(dialogTitle);

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
}

