package com.riotapps.word.ui;

import com.riotapps.word.R;
import com.riotapps.word.utils.Constants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class GameSurfaceView extends SurfaceView  implements SurfaceHolder.Callback {

	GameSurfaceView me = this;
	Context _context;
	GameThread _gameThread = null;
	SurfaceHolder surfaceHolder;
	Typeface _typeface;
	private int _x = 20;
    private int _y = 20;
    private int _fullWidth;
    private int _smallTileWidth;
    private int _top;
    private int _left;
	
	public GameSurfaceView(Context context) {
		super(context);
		this.construct(context);
	}

	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.construct(context);
		
		// TODO Auto-generated constructor stub
	}

	private void construct(Context context) {
		this._context = context;
		this.setZOrderOnTop(true);
		 SurfaceHolder holder = getHolder();
		 holder.addCallback(this);
		 _gameThread = new GameThread(holder, this);
		 setFocusable(true);
		 _typeface = Typeface.createFromAsset(context.getAssets(), Constants.MAIN_FONT);
		  
		 holder.setFormat(PixelFormat.TRANSPARENT);// necessary
		 
		 
		 this.post(new Runnable() 
		    {   
		        @Override
		        public void run() {
		        	
		        	me._fullWidth = me.getWidth();
		   		 me._smallTileWidth = (int) Math.round(_fullWidth/15) - 1; //-1 for the space between each tile
		   		 me._top = me.getTop();
		   		 me._left = me.getLeft();
		           // Log.i("In onCreate", "" + hello.getHeight());
		            
		            Toast t = Toast.makeText(me._context, String.valueOf(me._fullWidth) + " " +  String.valueOf(me._smallTileWidth), Toast.LENGTH_LONG);  
				    t.show();
		        }
		    });
		 
		 
		// this.setBackgroundColor(Color.TRANSPARENT);
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
		this._gameThread.setRunning(true);
		this._gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	    // simply copied from sample application LunarLander:
	    // we have to tell thread to shut down & wait for it to finish, or else
	    // it might touch the Surface after we return and explode
	    boolean retry = true;
	    this._gameThread.setRunning(false);
	    while (retry) {
	        try {
	        	this._gameThread.join();
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
		  _gameThread = new GameThread(this, 500);
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
		// this.setLayoutParams(params)
		 //canvas.co
	        Bitmap _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.blank_tile);
	       Bitmap _scaled = Bitmap.createScaledBitmap(_scratch, _smallTileWidth , _smallTileWidth, false);
	      //  canvas.drawColor(Color.TRANSPARENT);
	       canvas.drawBitmap(_scaled, this._left, this._top, null);
	      canvas.drawBitmap(_scaled, this._left, this._top + _smallTileWidth + 1, null);
	      canvas.drawBitmap(_scaled, this._left, this._top +  (_smallTileWidth * 2) + 2, null);
	      canvas.drawBitmap(_scaled, this._left, this._top + (_smallTileWidth * 3) + 3, null);
	       canvas.drawBitmap(_scaled, this._left, this._top + (_smallTileWidth * 4) + 4, null);
	     canvas.drawBitmap(_scaled, this._left, this._top + (_smallTileWidth * 5) + 5, null);
	       
	      canvas.drawBitmap(_scaled,this._left + this._top + _smallTileWidth + 1, _smallTileWidth + 1, null);
	       canvas.drawBitmap(_scaled,this._left + this._top +  _smallTileWidth + 1, (_smallTileWidth * 2) + 2, null);
	       canvas.drawBitmap(_scaled,this._left + this._top +  _smallTileWidth + 1, (_smallTileWidth * 3) + 3, null);
	       canvas.drawBitmap(_scaled,this._left + this._top +  _smallTileWidth + 1, (_smallTileWidth * 4) + 4, null);
	       canvas.drawBitmap(_scaled,this._left + this._top +  _smallTileWidth + 1, (_smallTileWidth * 5) + 5, null);
	       
	       // canvas.drawBitmap(_scaled, _x  - (_scaled.getWidth() / 2), _y - (_scaled.getWidth() / 2), null);
	       // canvas.drawBitmap(_scratch, _x + 22 - (_scratch.getWidth() / 2), _y - (_scaled.getWidth() / 2), null);
	       // Paint p = new Paint();
	       // p.setTextSize(24);
	       // p.setAntiAlias(true);
	       // p.setTypeface(_typeface);
	       // canvas.drawText("4L", 50, 50, p);
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
