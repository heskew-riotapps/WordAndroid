package com.riotapps.word.ui;

import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ScoreboardButton extends Button{

	public ScoreboardButton(Context context, AttributeSet attrs){
		super(context, attrs);
		
		  //Typeface typeface = Typeface.createFromAsset(context.getAssets(), Constants.SCOREBOARD_BUTTON_FONT);
		setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
		
	}
}

