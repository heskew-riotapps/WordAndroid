package com.riotapps.word.ui;

import com.riotapps.word.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView  implements SurfaceHolder.Callback {

	GameThread gameThread = null;
	SurfaceHolder surfaceHolder;
	private int _x = 20;
    private int _y = 20;
	
	public GameSurfaceView(Context context) {
		super(context);
		this.construct();
	}

	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.construct();
		// TODO Auto-generated constructor stub
	}

	private void construct() {
		 getHolder().addCallback(this);
		 gameThread = new GameThread(getHolder(), this);
		 setFocusable(true);	
	}
//	public GameSurfaceView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		// TODO Auto-generated constructor stub
//	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		gameThread.setRunning(true);
		gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	    // simply copied from sample application LunarLander:
	    // we have to tell thread to shut down & wait for it to finish, or else
	    // it might touch the Surface after we return and explode
	    boolean retry = true;
	    gameThread.setRunning(false);
	    while (retry) {
	        try {
	        	gameThread.join();
	            retry = false;
	        } catch (InterruptedException e) {
	            // we will try it again and again...
	        }
	    }
		
	}

	public void onResume() {
	//	  random = new Random();
		  surfaceHolder = getHolder();
		  getHolder().addCallback(this);
		   
		  //Create and start background Thread
		  gameThread = new GameThread(this, 500);
		  gameThread.setRunning(true);
		  gameThread.start();
		
	}

	public void onPause() {
		  //Kill the background Thread
		  boolean retry = true;
		  gameThread.setRunning(false);
		   
		  while(retry){
		   try {
		    gameThread.join();
		    retry = false; 
		   } catch (InterruptedException e) {
		    e.printStackTrace(); 
		   } 
		  }
		
	}
	
	 @Override
	 protected void onDraw(Canvas canvas) {
		 super.onDraw(canvas);
	        Bitmap _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.bonus_3l);
	        canvas.drawColor(Color.CYAN);
	        canvas.drawBitmap(_scratch, 10, 10, null);
	//        canvas.d
	 }
	 
	 @Override
	 public boolean onTouchEvent(MotionEvent event) {
	     _x = (int) event.getX();
	     _y = (int) event.getY();
	     return true;
	 }
	 
	 public void updateStates(){
	  //Dummy method() to handle the States
	 }
	 
	 public void updateSurfaceView(){
	  //The function run in background thread, not ui thread.
	   
	  Canvas canvas = null;
	    
	  try{
	   canvas = surfaceHolder.lockCanvas();
	 
	   synchronized (surfaceHolder) {
	    updateStates();
	    onDraw(canvas);
	   }
	  }
	  finally{
	   if(canvas != null){
	    surfaceHolder.unlockCanvasAndPost(canvas);
	   }
	  } 
	 }
	 
 

}
