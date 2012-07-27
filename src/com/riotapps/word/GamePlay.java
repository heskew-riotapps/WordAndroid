package com.riotapps.word;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;

public class GamePlay extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);
        
    	DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int vpHeight = displaymetrics.heightPixels;
		int vpWidth = displaymetrics.widthPixels;
		
		TableLayout gameBoardWrapper = (TableLayout) findViewById(R.id.gameBoardWrapper);
		TableLayout.LayoutParams params=
				  new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, 300);
		gameBoardWrapper.setLayoutParams(params);
		
      }

}
