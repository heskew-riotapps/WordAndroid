package com.riotapps.word;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GamePlay extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);
        
//        Display display = getWindowManager().getDefaultDisplay();
///        Point size = new Point();
 //       display.getSize(size);
  //      int width = size.x;
   //     int height = size.y;
        Display display = getWindowManager().getDefaultDisplay(); 
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();  // deprecated
        
		
     //   Toast t = Toast.makeText(this, width, Toast.LENGTH_LONG);
     //   t.show();
 		TableLayout gameBoardWrapper = (TableLayout) findViewById(R.id.gameBoardWrapper);
 	//	LayoutParams params=
		//		  new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, width);
		gameBoardWrapper.setLayoutParams( new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, width));
		//gameBoardWrapper.set
		
		//TableRow tr = new TableRow(this);
		//tr.setl
		gameBoardWrapper.setOnClickListener(new View.OnClickListener() {            
	        public void onClick(View view) {  
//	                    Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_SHORT).show();

	                    Display display = getWindowManager().getDefaultDisplay(); 
	                    int width = display.getWidth();  // deprecated
	                    int height = display.getHeight();  // deprecated

	                    
	             		TableLayout gameBoardWrapper = (TableLayout) findViewById(R.id.gameBoardWrapper);
	             		RelativeLayout.LayoutParams params=  new RelativeLayout.LayoutParams(width * 5, width * 5);
	             		params.setMargins(-50, -50, -50, -50);
	             			//		  new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, width);
	             		
	             			gameBoardWrapper.setLayoutParams(params);
	                  
	        }
	     });  
      }

}
