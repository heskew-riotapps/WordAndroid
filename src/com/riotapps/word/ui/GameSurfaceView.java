package com.riotapps.word.ui;

import java.util.ArrayList;
import java.util.List;

import com.riotapps.word.R;
import com.riotapps.word.hooks.TileLayout;
import com.riotapps.word.hooks.TileLayoutService;
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
    private int fullWidth;
    private int smallTileWidth;
    private int top;
    private int left;
    private boolean _fullView;
    private int excessWidth;
    
    List<GameTile> list = new ArrayList<GameTile>();
    TileLayout layout;
    TileLayoutService layoutService;

 
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
		this.layoutService = new TileLayoutService();
		this.layout = layoutService.GetDefaultLayout(context);
		//
		this._fullView = true; 
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
		        	
		        me.fullWidth = me.getWidth();
		   		me.smallTileWidth = (int) Math.round(fullWidth/15) - 1; //-1 for the space between each tile
		   		me.top = me.getTop();
		   		me.left = me.getLeft();
		   		me.excessWidth = me.fullWidth - ((me.smallTileWidth * 15) + 14);
		   	 Toast t = Toast.makeText(me._context, String.valueOf(me.smallTileWidth)  + " " + String.valueOf(me.left)  + " " + String.valueOf(me.top) + " " + String.valueOf(me.fullWidth), Toast.LENGTH_LONG);  
			    t.show();            
		        }
		    });
		 

	}

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
		 int tileFontSize;
		 
		 if (this._fullView == true){
	        Bitmap _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.blank_tile);
	       Bitmap _scaled = Bitmap.createScaledBitmap(_scratch, smallTileWidth , smallTileWidth, false);
	      //  canvas.drawColor(Color.TRANSPARENT);
	  
	     //make sure full view is centered so grab remainder of 15 division 
	     //determine if font text can be used so that fewer images must be maintained
	     //use font size based on 80% of tile size
	       //keep array of tiles
	       
	     tileFontSize = (int) Math.round(this.smallTileWidth * .8);
	  //   canvas.drawColor(Color.GREEN);
	     this.temp(_scaled,canvas,0);
	     this.temp(_scaled,canvas,1);
	     this.temp(_scaled,canvas,2);
	     this.temp(_scaled,canvas,3);
	     this.temp(_scaled,canvas,4);
	     this.temp(_scaled,canvas,5);
	     this.temp(_scaled,canvas,6);
	     this.temp(_scaled,canvas,7);
	     this.temp(_scaled,canvas,8);
	     this.temp(_scaled,canvas,9);
	     this.temp(_scaled,canvas,10);
	     this.temp(_scaled,canvas,11);
	     this.temp(_scaled,canvas,12);
	     this.temp(_scaled,canvas,13);
	     this.temp(_scaled,canvas,14);
		 }
	       
	       // canvas.drawBitmap(_scaled, _x  - (_scaled.getWidth() / 2), _y - (_scaled.getWidth() / 2), null);
	       // canvas.drawBitmap(_scratch, _x + 22 - (_scratch.getWidth() / 2), _y - (_scaled.getWidth() / 2), null);
	        // Paint p = new Paint();
	       // p.setTextSize(24);
	       // p.setAntiAlias(true);
	       // p.setTypeface(_typeface);
	       // canvas.drawText("4L", 50, 50, p);
	//        canvas.d
		 
	 }
	 
	 private void temp(Bitmap bm, Canvas canvas, int x){
	      canvas.drawBitmap(bm,1  + (this.excessWidth / 2)   + (this.smallTileWidth * x) + x, 1, null);
	      canvas.drawBitmap(bm, 1  + (this.excessWidth / 2)  + (this.smallTileWidth * x) + x, 1 + smallTileWidth + 1, null);
	      canvas.drawBitmap(bm, 1  + (this.excessWidth / 2)  + (this.smallTileWidth * x) + x,1 + (smallTileWidth * 2) + 2, null);
	      canvas.drawBitmap(bm, 1 + (this.excessWidth / 2)  + (this.smallTileWidth * x) + x,1 + (smallTileWidth * 3) + 3, null);
	      canvas.drawBitmap(bm, 1  + (this.excessWidth / 2)  + (this.smallTileWidth * x) + x,1 + (smallTileWidth * 4) + 4, null);
	      canvas.drawBitmap(bm, 1 + (this.excessWidth / 2)  + (this.smallTileWidth * x) + x,1 + (smallTileWidth * 5) + 5, null);
		  canvas.drawBitmap(bm, 1 + (this.excessWidth / 2)   + (this.smallTileWidth * x) + x, 1 + (smallTileWidth * 6) + 6, null);
		  canvas.drawBitmap(bm, 1 + (this.excessWidth / 2)  + (this.smallTileWidth * x) + x, 1 + (smallTileWidth * 7) + 7, null);
		  canvas.drawBitmap(bm, 1 + (this.excessWidth / 2)   + (this.smallTileWidth * x) + x, 1 + (smallTileWidth * 8) + 8, null);
		  canvas.drawBitmap(bm, 1 + (this.excessWidth / 2)   + (this.smallTileWidth * x) + x, 1 + (smallTileWidth * 9) + 9, null);
		  canvas.drawBitmap(bm, 1 + (this.excessWidth / 2)   + (this.smallTileWidth * x) + x, 1 + (smallTileWidth * 10) + 10, null);
		  canvas.drawBitmap(bm, 1 + (this.excessWidth / 2) + (this.smallTileWidth * x) + x, 1 + (smallTileWidth * 11) + 11, null);
		  canvas.drawBitmap(bm, 1 + (this.excessWidth / 2)  + (this.smallTileWidth * x) + x, 1 + (smallTileWidth * 12) + 12, null);
          canvas.drawBitmap(bm, 1 + (this.excessWidth / 2)  + (this.smallTileWidth * x) + x, 1 + (smallTileWidth * 13) + 13, null);
		  canvas.drawBitmap(bm, 1 + (this.excessWidth / 2)  + (this.smallTileWidth * x) + x, 1 + (smallTileWidth * 14) + 14, null);
		  canvas.drawBitmap(bm, 1 + (this.excessWidth / 2)   + (this.smallTileWidth * x) + x, 1 + (smallTileWidth * 15) + 15, null);
	 
	 
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
