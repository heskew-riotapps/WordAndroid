package com.riotapps.word.ui;

import com.riotapps.word.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomButtonDialog extends AlertDialog{
	
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
		private int layoutId = R.layout.twobuttondialog;

	  
	 // public void SetText(String text){
	//	  this.dialogText = text;
	 // }
	
	//  public CustomButtonDialog(Context context) {  
	//        super(context);
	//    
	//    }

		public CustomButtonDialog(Context ctx, String dialogTitle, String dialogText, String okText, String cancelText) {
			this(ctx, dialogTitle, dialogText, false, okText, cancelText, null, null, R.layout.twobuttondialog);
		}
		
		public CustomButtonDialog(Context ctx, String dialogTitle, String dialogText, String okText, String cancelText, int layoutId ) {
			this(ctx, dialogTitle, dialogText, false, okText, cancelText, null, null, layoutId);
		}
		
		
		public CustomButtonDialog(Context ctx, String dialogTitle, String dialogText) {
			this(ctx, dialogTitle, dialogText, false, ctx.getString(R.string.ok), ctx.getString(R.string.cancel), null, null, R.layout.twobuttondialog);
		}
		
		public CustomButtonDialog(Context ctx, String dialogTitle, String dialogText, int layoutId ) {
			this(ctx, dialogTitle, dialogText, false, ctx.getString(R.string.ok), ctx.getString(R.string.cancel), null, null, layoutId);
		}
	 
		public CustomButtonDialog(Context ctx, String dialogTitle, String dialogText, Boolean onCancelClickFinishActivity, 
				String okText, String cancelText, View.OnClickListener onOK, View.OnClickListener onCancel, int layoutId) {
			super(ctx);
			
			this.context = ctx;
		    this.onCancelClickFinishActivity = onCancelClickFinishActivity;
			this.dialogTitle = dialogTitle;
			this.dialogText = dialogText;
			this.cancelText = cancelText;
			this.onCancel = onCancel;
			this.onOK = onOK;
			this.okText = okText;
			this.layoutId = layoutId;
		}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		this.setContentView(BUTTON1);
      //  this.setProgressStyle(R.style.CustomProgressStyle);
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	 
		LayoutInflater inflater = getLayoutInflater();//(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        this.layout = inflater.inflate(layoutId, 
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
		
		this.setCanceledOnTouchOutside(false);
		
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
		
		this.setContentView(this.layout);
	
	}

	public void setOnOKClickListener(View.OnClickListener onClick){
		
		this.onOK = onClick;
		//this.bOK.setOnClickListener(onClick);
	}
	
	public void setOnDismissListener(View.OnClickListener onClick){
		this.onCancel = onClick;
		//this.close.setOnClickListener(onClick);
		//this.bCancel.setOnClickListener(onClick);
	}
	  
}
