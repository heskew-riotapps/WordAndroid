package com.riotapps.word.utils;

import com.riotapps.word.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

public class CustomProgressDialog extends ProgressDialog{
	
	  public CustomProgressDialog(Context context) {  
	        super(context);

	       this.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.custom_progress));    
	       
	        this.setProgressStyle(R.style.CustomProgressStyle);
	    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        this.setProgressStyle(R.style.CustomProgressStyle);
	}  
	  
	  
}
