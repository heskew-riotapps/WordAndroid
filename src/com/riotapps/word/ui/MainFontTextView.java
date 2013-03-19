package com.riotapps.word.ui;

import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.Constants;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MainFontTextView extends TextView {

    public MainFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        //Typeface typeface = Typeface.createFromAsset(context.getAssets(), Constants.MAIN_FONT);
        setTypeface(ApplicationContext.getMainFontTypeface());
    }
}