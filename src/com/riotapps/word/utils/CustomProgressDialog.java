package com.riotapps.word.utils;

import com.riotapps.word.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class CustomProgressDialog extends ProgressDialog{
	
	  public CustomProgressDialog(Context context) {  
	        super(context);
	       // this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
 
	     //   this.setContentView(R.layout.progress);
	    
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
	        View layout = inflater.inflate(R.layout.progress, 
	                                        (ViewGroup) findViewById(R.id.progress_root));
	     
	        
	     	TextView text = (TextView) layout.findViewById(R.id.dialog_title);
	 		text.setText("TTTTTTTTTT");
	  //     this.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.custom_progress));    
	       
	      //  this.setProgressStyle(R.style.CustomProgressStyle); 
	 		this.setView(layout);
	}  
	  
	  
}
