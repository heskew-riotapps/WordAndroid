package com.riotapps.word.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class TypefaceButton extends Button{

	public TypefaceButton(Context context, AttributeSet attrs){
		super(context, attrs);
		
		  Typeface typeface = Typeface.createFromAsset(context.getAssets(), Constants.MAIN_FONT);
          setTypeface(typeface);
		
	}
}
