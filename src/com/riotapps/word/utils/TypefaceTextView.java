package com.riotapps.word.utils;

 
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TypefaceTextView extends TextView {

    public TypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
      //  if (isInEditMode()) {
      //      return;
      //  }

     //   TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
      ////  String fontName = styledAttrs.getString(R.styleable.TypefacedTextView_typeface);
      //  styledAttrs.recycle();

      //  if (fontName != null) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), Constants.MAIN_FONT);
            setTypeface(typeface);
       // }
    }

}