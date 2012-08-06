package com.riotapps.word;

import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.ui.GameThread;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends Activity {

	GameSurfaceView _gameSurfaceView;
	SurfaceView _surfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamesurface);
		//setContentView(new GameSurfaceView2(this));
		
	 	this._gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurface);
		//this._surfaceView = new GameSurfaceView(this); 
	}
	
 
		 
	 class GameSurfaceView2 extends SurfaceView  implements SurfaceHolder.Callback {

			GameThread2 _gameThread = null;
			SurfaceHolder surfaceHolder;
			private int _x = 20;
		    private int _y = 20;
			
			public GameSurfaceView2(Context context) {
				super(context);
				this.construct();
			}

			public GameSurfaceView2(Context context, AttributeSet attrs) {
				super(context, attrs);
				this.construct();
				// TODO Auto-generated constructor stub
			}

			private void construct() {
				 getHolder().addCallback(this);
				 _gameThread = new GameThread2(getHolder(), this);
				 setFocusable(true);	
			}
//			public GameSurfaceView(Context context, AttributeSet attrs, int defStyle) {
//				super(context, attrs, defStyle);
//				// TODO Auto-generated constructor stub
//			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				_gameThread.setRunning(true);
				_gameThread.start();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			    // simply copied from sample application LunarLander:
			    // we have to tell thread to shut down & wait for it to finish, or else
			    // it might touch the Surface after we return and explode
			    boolean retry = true;
			    _gameThread.setRunning(false);
			    while (retry) {
			        try {
			        	_gameThread.join();
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
				  _gameThread = new GameThread2(this, 500);
				  _gameThread.setRunning(true);
				  _gameThread.start();
				
			}

			public void onPause() {
				  //Kill the background Thread
				  boolean retry = true;
				  _gameThread.setRunning(false);
				   
				  while(retry){
				   try {
					   _gameThread.join();
				    retry = false; 
				   } catch (InterruptedException e) {
				    e.printStackTrace(); 
				   } 
				  }
				
			}
			
			 @Override
			 protected void onDraw(Canvas canvas) {
				// super.onDraw(canvas);
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
	 
	 public class GameThread2 extends Thread {
		 private SurfaceHolder _surfaceHolder;
		 volatile boolean _running = false;
		  
		 GameSurfaceView2 _parent;
		 long sleepTime;
		  
		 public GameThread2(GameSurfaceView2 sv, long st){
		  super();
		  _parent = sv;
		  sleepTime = st;
		 }
		 
		  public GameThread2(SurfaceHolder surfaceHolder, GameSurfaceView2 surfaceView) {
		    _surfaceHolder = surfaceHolder;
		    _parent = surfaceView;
		 }
		  
		 public void setRunning(boolean r){
		  _running = r;
		 }
		  
		 @Override
		 public void run() {
		  // TODO Auto-generated method stub
			 Canvas c;
			    while (_running) {
			        c = null;
			        try {
			            c = _surfaceHolder.lockCanvas(null);
			            synchronized (_surfaceHolder) {
			            	_parent.onDraw(c);
			            }
			        } finally {
			            // do this in a finally so that if an exception is thrown
			            // during the above, we don't leave the Surface in an
			            // inconsistent state
			            if (c != null) {
			                _surfaceHolder.unlockCanvasAndPost(c);
			            }
			        }
			    }
			 

		}
	 }
}
