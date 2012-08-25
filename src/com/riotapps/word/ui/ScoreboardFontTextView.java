package com.riotapps.word.ui;

import com.riotapps.word.utils.Constants;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class ScoreboardFontTextView extends TextView {

    public ScoreboardFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), Constants.SCOREBOARD_FONT);
        setTypeface(typeface);
    }
}