package com.riotapps.word.ui;

import com.riotapps.word.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomButtonDialogView extends View {

	private Button bOK;
	private Button bCancel;
	private ImageView close;
	private Context context;
	private String dialogTitle;
	private String dialogText;
	private String okText;
	private String cancelText;
	private View layout;
	private boolean onCancelClickFinishActivity;
	private View.OnClickListener onCancel = null;
	private View.OnClickListener onOK = null;
	
	public CustomButtonDialogView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomButtonDialogView(Context ctx, String dialogTitle, String dialogText, String okText, String cancelText) {
		this(ctx, dialogTitle, dialogText, false, okText, cancelText, null, null);
	}
	
	public CustomButtonDialogView(Context ctx, String dialogTitle, String dialogText) {
		this(ctx, dialogTitle, dialogText, false, ctx.getString(R.string.ok), ctx.getString(R.string.cancel), null, null);
	}
 
	public CustomButtonDialogView(Context ctx, String dialogTitle, String dialogText, Boolean onCancelClickFinishActivity, 
			String okText, String cancelText, View.OnClickListener onOK, View.OnClickListener onCancel) {
		super(ctx);
		
	/*	this.context = ctx;
	    this.onCancelClickFinishActivity = onCancelClickFinishActivity;
		this.dialogTitle = dialogTitle;
		this.dialogText = dialogText;
		this.cancelText = cancelText;
		this.onCancel = onCancel;
		this.onOK = onOK;
		this.okText = okText;
		
		LayoutInflater inflater = getLayoutInflater();//(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        this.layout = inflater.inflate(R.layout.twobuttondialog, 
                                        (ViewGroup) findViewById(R.id.progress_root));

		TextView title = (TextView) layout.findViewById(R.id.alert_title);  
		title.setText(this.dialogTitle);
		TextView text = (TextView) layout.findViewById(R.id.alert_text);
		text.setText(this.dialogText);

		this.bOK = (Button) layout.findViewById(R.id.bOK);
		this.bOK.setText(okText);
		
		this.bOK.setOnClickListener(this.onOK);
		
		this.bCancel = (Button) layout.findViewById(R.id.bCancel);
		this.bCancel.setText(cancelText);
		
		this.close = (ImageView) layout.findViewById(R.id.img_close);
		
//		this.setCanceledOnTouchOutside(false);
		
		if (this.onCancel == null){
			this.bCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
					if(onCancelClickFinishActivity){
		            	((Activity)context).finish();
		            }
				}
			});

			
			
			//if button is clicked, close the custom dialog
			this.close.setOnClickListener(new View.OnClickListener() {
		 		@Override
				public void onClick(View v) {
					dismiss();
					if(onCancelClickFinishActivity){
		            	((Activity)context).finish();
		            }
				}
			});
		}
		else {
			this.close.setOnClickListener(onCancel);
			this.bCancel.setOnClickListener(onCancel);
		}
		//this.layout.setLayoutParams(new LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		this.setView(this.layout);
		*/
		
	}
}
