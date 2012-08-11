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
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class GameSurfaceView extends SurfaceView  implements SurfaceHolder.Callback {

	GameSurfaceView me = this;
	Context _context;
	GameThread gameThread = null;
	SurfaceHolder surfaceHolder;
	Typeface letterTypeface;
	private int currentX = 0;
    private int currentY = 20;
    private int fullWidth;
    private int fullViewTileWidth;
    private int top;
    private int left;
    private boolean isZoomed = false;
    private int excessWidth;
    private boolean isZoomAllowed = true; //if width of board greater than x disable zooming.  it means we are on a tablet and zooming not needed.
    private int activeTileWidth; 
    private long tapCheck = 0;
    private float zoomMultiplier = 2.0f;
    private int tileGap = 1;
    private int zoomedTileWidth;
    private int midpoint;
    private int fullViewTextSize;
    private int zoomedTextSize;
    private int touchMotion;
    private int outerZoomLeft;
    private int outerZoomTop; 
    private int fullViewTileMidpoint;
 
    
    List<GameTile> tiles = new ArrayList<GameTile>();
    TileLayout defaultLayout;
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
		this.defaultLayout = layoutService.GetDefaultLayout(context);
		//
		this.setZOrderOnTop(true);
		 SurfaceHolder holder = getHolder();
		 holder.addCallback(this);
		 gameThread = new GameThread(holder, this);
		 setFocusable(true);
		 this.letterTypeface = Typeface.createFromAsset(context.getAssets(), Constants.GAME_BOARD_FONT);
		  
		 holder.setFormat(PixelFormat.TRANSPARENT);// necessary
		 
		
		 
		 
		 this.post(new Runnable() 
		    {   
		        @Override
		        public void run() {
		        	
		        me.fullWidth = me.getWidth();
		   		me.SetDerivedValues();
		   	    me.LoadTiles();
		   	 Toast t = Toast.makeText(me._context, String.valueOf(me.fullViewTileWidth)  + " " + String.valueOf(me.excessWidth)  + " "  + String.valueOf(me.fullWidth), Toast.LENGTH_LONG);  
			    t.show();            
		        }
		    });
	

	}

	private void SetDerivedValues(){
		this.fullViewTileWidth = Math.round(this.fullWidth/15) - this.tileGap; //-1 for the space between each tile
   		//me.top = me.getTop();
   		//me.left = me.getLeft();
		this.excessWidth = this.fullWidth - ((this.fullViewTileWidth * 15) + (14 * this.tileGap));
		this.zoomedTileWidth = Math.round(this.fullViewTileWidth * this.zoomMultiplier);
		this.midpoint = Math.round(this.fullWidth / 2);
		this.outerZoomLeft = this.fullWidth - Math.round((this.zoomedTileWidth + 1) * 15); 
		this.outerZoomTop = ((this.fullViewTileWidth + 1) * 15) - Math.round((this.zoomedTileWidth + 1) * 15);  
		this.fullViewTileMidpoint = Math.round(this.fullViewTileWidth / 2);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.gameThread.setRunning(true);
		this.gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	    // simply copied from sample application LunarLander:
	    // we have to tell thread to shut down & wait for it to finish, or else
	    // it might touch the Surface after we return and explode
	    boolean retry = true;
	    this.gameThread.setRunning(false);
	    while (retry) {
	        try {
	        	this.gameThread.join();
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
		// super.onDraw(canvas);
		// this.setLayoutParams(params)
		 //canvas.co
		 
		 if (this.touchMotion == MotionEvent.ACTION_MOVE && this.isZoomed == false) { return; }
		 
		 canvas.drawColor(0, Mode.CLEAR);
		 int tileFontSize;
		 Bitmap _scratch;
		 Bitmap _scaled;
		 int left = 0;
		 int top = 0;
		 canvas.drawColor(Color.YELLOW);
		 if (this.isZoomed == false || this.isZoomAllowed == false){
			 this.activeTileWidth = this.fullViewTileWidth;
	        _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.blank_tile);
	        _scaled = Bitmap.createScaledBitmap(_scratch, fullViewTileWidth , fullViewTileWidth, false);
	      //  canvas.drawColor(Color.TRANSPARENT);
	        left = 1 + (this.excessWidth / 2);  
	        top = 1;
	        
	        for (GameTile tile : this.tiles) { 
		    	 canvas.drawBitmap(tile.getOriginalBitmap(),tile.getxPosition(), tile.getyPosition(), null);
		    	 
		    	 Paint p = new Paint();
			     p.setTextSize(this.fullViewTileWidth - 10);
			     p.setAntiAlias(true);
			     p.setTypeface(this.letterTypeface);
			     Rect bounds = new Rect();
			     p.getTextBounds("4L", 0, "4L".length(), bounds);
			     int textLeft =  tile.getxPosition() + this.fullViewTileMidpoint - (Math.round(bounds.width() / 2));
			     int textTop =  tile.getyPosition() + this.fullViewTileMidpoint - (Math.round(bounds.height() / 2));
			     
			     canvas.drawText("4L", textLeft, textTop, p);
		    }
		 }
		 else {
			 
			 if (this.touchMotion == MotionEvent.ACTION_MOVE) {
				 
				 if (this.currentX < this.outerZoomLeft || this.currentY < this.outerZoomTop) {return;}
				 
				 //check for tray movement...
				 //determine if whole board should be scrolled or should a tile be moved
				 //can partial view be moved if just the tile is moving???
				 
			 }
			// canvas.drawColor(Color.CYAN);
			 //only if in tapped mode
			 GameTile tappedTile = this.FindTileFromPositionInFullViewMode(this.currentX, this.currentY);
			 
			 //this.activeTileWidth = Math.round(this.fullViewTileWidth * this.zoomMultiplier);  //do this in oncreate/calculate
			// int midpoint = Math.round(fullWidth / 2);
		 
			 
			// _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.blank_tile);
		   //  _scaled = Bitmap.createScaledBitmap(_scratch, activeTileWidth , activeTileWidth, false);
			 //calculate left and top based on pointer
		     
		  //   left = this.currentX;
		   //  top = this.currentY;
		     
		    // int outerZoomBottom = this.fullWidth + (Math.round(this.zoomMultiplier * this.fullWidth) / 2);
		     
		     
		   //  int innerZoomLeft = 1;
		    // int innerZoomRight = this.fullWidth;
		    // int innerZoomTop = 1; //this will vary depending on whether or not top scoreboard slides away
		   //  int innerZoomBottom = this.fullWidth;
		     
		     int tappedTop = this.midpoint - ((tappedTile.getRow() * this.zoomedTileWidth) + Math.round(this.zoomedTileWidth / 2));
		     if (tappedTop < this.outerZoomTop) {tappedTop = this.outerZoomTop;}
		     if (tappedTop > 1) {tappedTop = 1;}
		     
		     int tappedLeft = this.midpoint - ((tappedTile.getColumn() * this.zoomedTileWidth) + Math.round(this.zoomedTileWidth / 2));
		     if (tappedLeft < this.outerZoomLeft) {tappedLeft = this.outerZoomLeft;}
		     if (tappedLeft > 1) {tappedLeft = 1;}
		     
		     for (GameTile tile : this.tiles) {
		    	 tile.setxPositionZoomed(tappedLeft + ((tile.getColumn() - 1) * this.zoomedTileWidth) + ((tile.getColumn() - 1) * this.tileGap));
				 tile.setyPositionZoomed(tappedTop + ((tile.getRow() - 1) * this.zoomedTileWidth) + ((tile.getRow() - 1) * this.tileGap));
			
		    	// int loopTop = ((tile.getRow() - 1) * this.zoomedTileWidth) + ((tile.getRow() - 1) * this.tileGap);
		    //	 int loopLeft = ((tile.getColumn() - 1) * this.zoomedTileWidth) + ((tile.getRow() - 1) * this.tileGap);
		    //	 tile.setxPositionZoomed(loopLeft);
		    //	 tile.setyPositionZoomed(loopTop);
		    	 canvas.drawBitmap(tile.getOriginalBitmapZoomed(),tile.getxPositionZoomed(), tile.getyPositionZoomed(), null);
		    	 //tile.setZoomedx and to determine which tile was clicked
		     }
		     
		     //so just determine where the tap was and move that position to the middle
		     //if any of the above boundaries are breached, simply use the breached boundary position instead
		     
		     //left needs to be centered, top needs to be centered in zoomed view
		 }
     	 
    	// Toast t = Toast.makeText(me._context, "width: " + String.valueOf(activeTileWidth), Toast.LENGTH_LONG);  
		 //   t.show(); 
	     //make sure full view is centered so grab remainder of 15 division 
	     //determine if font text can be used so that fewer images must be maintained
	     //use font size based on 80% of tile size
	       //keep array of tiles
	       
	  ////   tileFontSize = (int) Math.round(this.activeTileWidth * .8);
	  //   canvas.drawColor(Color.GREEN);
	//     for (int x = 0; x<15 ;x++){
	  //  	 this.temp(_scaled,canvas,x, left, top);
	    	 
	   //  }
	     
	 //    for (GameTile tile : this.tiles) { 
	  //  	 canvas.drawBitmap(tile.getOriginalBitmap(),tile.getxPosition(), tile.getyPosition(), null);
	   // 	}
	     
 
		 
	 
	       
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
	     this.currentX = (int) event.getX();
	     this.currentY = (int) event.getY();
	     this.touchMotion = event.getAction();
	     //return true;
	     
	     synchronized (this.gameThread.getSurfaceHolder()) {
             switch (event.getAction()) {
             
             case MotionEvent.ACTION_DOWN:
            
                 //where is the click, which object within view???
            	 //get tile from coordinates.  if tile is null, do nothing
            	 //for now act like this is a click/tap...
            	 
            	 this.tapCheck = System.nanoTime();
            	// this.invalidate();
            	 
            	 break;
             case MotionEvent.ACTION_UP:
            	 if (this.tapCheck > 0 && System.nanoTime() - this.tapCheck <= 300000000) {
            		 this.isZoomed = !this.isZoomed;
            	 }
            	 this.tapCheck = 0;
            	 break;
             case MotionEvent.ACTION_MOVE:
            	 this.tapCheck = 0;
            	 break; 
             }
             
             return true;
         }

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
	 
 
	 private void LoadTiles() {
		 
		 Bitmap baseBG = BitmapFactory.decodeResource(getResources(), R.drawable.blank_tile_bg);
		 Bitmap baseBGScaled = Bitmap.createScaledBitmap(baseBG, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
	
		 Bitmap baseBGZoomed = BitmapFactory.decodeResource(getResources(), R.drawable.blank_tile_bg);  //changed this to zoomed version
		 Bitmap baseBGScaleZoomed = Bitmap.createScaledBitmap(baseBGZoomed, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
		 
		 //for each row load each column 15x15
		 //row = y
	     //column = x
		 for(int y = 0; y < 15; y++){
			 for(int x = 0; x < 15; x++){
				 GameTile tile = new GameTile();
				 tile.setxPosition((this.excessWidth / 2) + (x * this.fullViewTileWidth) + (x * this.tileGap));
				 tile.setyPosition(1 + (y * this.fullViewTileWidth) + (y * this.tileGap));
				 tile.setColumn(x + 1);
				 tile.setRow(y + 1);
				 tile.setOriginalBitmap(baseBGScaled); //this will change as default bonus and played tiles are incorporated
				 if (this.isZoomAllowed == true){
					 tile.setOriginalBitmapZoomed(baseBGScaleZoomed);
				 }
				 //check defaultLayout for bonus tiles etc
				 this.tiles.add(tile);
			 }
		 }
	 }
	 
	 private GameTile FindTileFromPositionInFullViewMode(int xPosition, int yPosition){
		 for (GameTile tile : this.tiles) { 
	    	 if (xPosition >= tile.getxPosition() && xPosition <= tile.getxPosition() + this.fullViewTileWidth + this.tileGap &&
	    	 		 yPosition >= tile.getyPosition() && yPosition <= tile.getyPosition() + this.fullViewTileWidth + this.tileGap){
	    		 return tile;
	    	 }
	    	}
		 return null;
	 }

}
