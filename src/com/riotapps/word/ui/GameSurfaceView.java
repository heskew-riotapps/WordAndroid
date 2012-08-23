package com.riotapps.word.ui;

import java.util.ArrayList;
import java.util.List;

import com.riotapps.word.R;
import com.riotapps.word.hooks.TileLayout;
import com.riotapps.word.hooks.TileLayoutService;
import com.riotapps.word.utils.Constants;
import android.util.Log;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class GameSurfaceView extends SurfaceView  implements SurfaceHolder.Callback {

	GameSurfaceView me = this;
	Context context;
	GameThread gameThread = null;
	SurfaceHolder surfaceHolder;
	Typeface letterTypeface;
	private int currentX = 0;
    private int currentY = 20;
    private int fullWidth;
    private int fullViewTileWidth;
  //  private int top;
  //  private int left;
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
    //private int touchMotion = -1;
    private int outerZoomLeft;
    private int outerZoomTop; 
    private int fullViewTileMidpoint;
    private int zoomedTileMidpoint;
 //   private boolean isDrawn;
    private int scaleInProcess = 0;
    private boolean readyToDraw = false;
    private long dblTapCheck = 0;
    private boolean isMoving = false;
    private int previousY = 0;
    private int previousX = 0;
    private int previousTouchMotion = -3;
    private int currentTouchMotion = -3;
    private long previousTouchTime = 0;
  //  private long currentTouchTime = 0;
    private float currentSpeed = 0.0f;
    
    private GameTile currentTile = null;
 
	public boolean isReadyToDraw() {
		return readyToDraw;
	}

	public void setReadyToDraw(boolean readyToDraw) {
		this.readyToDraw = readyToDraw;
	}

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
		this.context = context;
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
		 
		// this.isDrawn = false;
		 
		// this.setScrollContainer(true);
	///	 this.setVerticalScrollBarEnabled(true);
	//	 this.setHorizontalScrollBarEnabled(true);
		 		 
		 this.post(new Runnable() 
		    {   
		        @Override
		        public void run() {

		   		me.SetDerivedValues();
		   	    me.LoadTiles();
		   	    me.readyToDraw = true;
		        }
		    });
	}

	private void SetDerivedValues(){
		this.fullWidth = this.getWidth();
		this.fullViewTileWidth = Math.round(this.fullWidth/15) - this.tileGap; //-1 for the space between each tile
		this.excessWidth = this.fullWidth - ((this.fullViewTileWidth * 15) + (14 * this.tileGap));
		this.zoomedTileWidth = Math.round(this.fullViewTileWidth * this.zoomMultiplier);
		this.midpoint = Math.round(this.fullWidth / 2);
		this.outerZoomLeft = this.fullWidth - Math.round((this.zoomedTileWidth + 1) * 15); 
		this.outerZoomTop = ((this.fullViewTileWidth + 1) * 15) - Math.round((this.zoomedTileWidth + 1) * 15);  
		this.fullViewTileMidpoint = Math.round(this.fullViewTileWidth / 2);
		this.zoomedTileMidpoint = Math.round(this.zoomedTileWidth / 2);	
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

	 @Override
	 public boolean onTouchEvent(MotionEvent event) {
	     this.currentX = (int) event.getX();
	     this.currentY = (int) event.getY();
	     this.currentTouchMotion = event.getAction();
	     long currentTouchTime = System.nanoTime();
	     
 
          Log.w(getClass().getSimpleName() + "onTouchEvent", event.toString());

    	 
	     synchronized (this.gameThread.getSurfaceHolder()) {
             switch (event.getAction()) {
          

             case MotionEvent.ACTION_DOWN:
            
                 //where is the click, which object within view???
            	 //get tile from coordinates.  if tile is null, do nothing
            	 //for now act like this is a click/tap...
            	 this.readyToDraw = false;
            	 this.tapCheck = currentTouchTime;
            	 if (this.dblTapCheck == 0){ this.dblTapCheck = currentTouchTime; }
            	// this.invalidate();
            	 this.isMoving = false; 
            	 
          
            	 this.previousX = this.currentX;
            	 this.previousY = this.currentY;
            	 currentTile = this.FindTileFromPositionInFullViewMode(this.currentX, this.currentY);
            	// this.currentTouchMotion = MotionEvent.ACTION_DOWN;
            	// return false; //??
            	  break;
             case MotionEvent.ACTION_UP:
            	 //includes a check to ignore double taps
            	 //  Log.w(getClass().getSimpleName() + "onTouchEvent ActionUP ", this.tapCheck + " " + currentTouchTime + " " + this.readyToDraw);
            	   
            	   
            	   this.readyToDraw = false;
            	   
            	 if (this.tapCheck > 0 && currentTouchTime - this.tapCheck <= 300000000) {
            	 // && (currentTouchTime - this.dblTapCheck >= 800000000 || this.dblTapCheck == 0)) {
            	//	 if (this.isMoving){
            	//		 //if we are coming out of a drag, up event just means drag is finished, nothing to do here, just move along
            	//		 this.isMoving = false;
             	
            	//	 }
            		// else {// if (this.currentTouchMotion == MotionEvent.ACTION_DOWN){ //action up should immediately follow and action down to be a tap
	            	if (!this.isMoving) {
            		 this.isZoomed = !this.isZoomed;
	            		 this.readyToDraw = true;
	            		 this.dblTapCheck = 0;
            		 }
            		// else {
            		//	 //if previous action was not a down, don't draw
            		//	 this.readyToDraw = false;
            		// }
            	 }
            	 // this.currentTouchMotion = MotionEvent.ACTION_UP;
            	 this.previousX = 0;
            	 this.previousY = 0;
            	 this.isMoving = false;
            	 this.tapCheck = 0;
            	 break;
             case MotionEvent.ACTION_MOVE:
            	 
            	// this.currentTouchMotion = MotionEvent.ACTION_MOVE;
            	 
            	 this.tapCheck = 0;
            	 this.dblTapCheck = 0;
            	 this.isMoving = true;
            	 if (!this.isZoomed){
            		 this.readyToDraw = false;
            	 }
            	 else if (this.currentTouchMotion == MotionEvent.ACTION_MOVE && this.previousX == this.currentX && this.previousY == this.currentY){
            		 this.readyToDraw = false;
            	 }
            	 else {
            		 this.readyToDraw = true;
            	 }
            	// this.readyToDraw = false;
            	 if (this.currentX < this.outerZoomLeft || this.currentY < this.outerZoomTop) {
            		//calculate outerZoomRight and bottom
            		
            	 }
            	 //  Log.w(getClass().getSimpleName() + "onTouchEvent ACTION_MOVE ", this.previousX + " " + this.currentX + " " + this.readyToDraw);
            	 break; 
             default:
            	 this.readyToDraw = false;
             }
             this.previousTouchMotion = this.currentTouchMotion;
         	 this.previousTouchTime = currentTouchTime;
             return true;
         }

	 }
	 
	 @Override
	 protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
	  Log.w(getClass().getSimpleName() + "onDraw ",this.currentTouchMotion + " " + this.tapCheck + " " +  this.isMoving  + " " + this.readyToDraw + " " + this.previousX + " " + this.previousY
	 			 + " " + this.currentX + " " + this.currentY);
		 
		long currentTouchTime = System.nanoTime();
		
		//  if (this.touchMotion == MotionEvent.ACTION_MOVE ) {this.readyToDraw = false;} 
		 if (this.currentTouchMotion == MotionEvent.ACTION_DOWN){ this.readyToDraw = false;}  
		 
		//this will have to change if dragging a tile 
		 if (this.currentTouchMotion == MotionEvent.ACTION_MOVE && this.isZoomed == false) { this.readyToDraw = false; }
		 
		 //if we are in middle of action move but we are not moving (the finger is pressed but not moving, don't redraw
 		 if (this.currentTouchMotion == MotionEvent.ACTION_MOVE && this.previousTouchMotion == MotionEvent.ACTION_MOVE
 				 && this.previousX == this.currentX && this.previousY == this.currentY){ this.readyToDraw = false; }
	
		 
		 if (this.readyToDraw == true){ 
			 
			  Log.w(getClass().getSimpleName() + "onDraw2 ",this.currentTouchMotion + " " + this.tapCheck + " " +  this.isMoving  + " " + this.readyToDraw + " " + this.previousX + " " + this.previousY
				 		 + " " + this.currentX + " " + this.currentY + " " + this.previousTouchMotion);
			 
			// if (this.touchMotion != MotionEvent.ACTION_MOVE ) { 
			 canvas.drawColor(0, Mode.CLEAR); ///clears out the previous drawing on the canvas
			// canvas.drawColor(Color.YELLOW);
			 //}
			 int tileFontSize;
			 this.readyToDraw = false;
			 
			 if (this.isZoomed == false || this.isZoomAllowed == false){
				 this.drawFullView(canvas);
				
			 }
			 else {
				// if (this.touchMotion == MotionEvent.ACTION_UP) {
				//	 this.isZoomed = false; ///turn off zoom since we are handling now
				// }
				 if (this.currentTouchMotion == MotionEvent.ACTION_MOVE) {
					 
					 this.readyToDraw = true;
					   
					 int leftDiff = this.previousX - this.currentX ;
					 int topDiff =  this.previousY - this.currentY;
					 
					 //handle tile drag later, first drag the whole board around
					// if (this.currentX < this.outerZoomLeft || this.currentY < this.outerZoomTop) {
					//	 this.readyToDraw = false;
					//	 newLeft = 
					// }
					 
					 this.previousX = this.currentX;
					 this.previousY = this.currentY;
				 
					 
									
					 if (this.currentTile.getPlacedText().length() > 0 ){
						 //drag this letter, not the board
					 }
					 else {
						//drag/scroll entire board 
					 
					 
						 //if (tappedTile == null) { return; } ///do something here, this is causing the board to disappear when scrolled out of bounds
						 
						 //grab top left tile  
						 GameTile topLeftTile = this.tiles.get(0);
						 
						 Log.w(getClass().getSimpleName() + "onDraw ACTION_MOVE ",leftDiff + " " + topDiff + " " +  this.previousX  + " " + this.previousY + " "
						 + topLeftTile.getxPositionZoomed() + " " + topLeftTile.getyPositionZoomed() + " "
						 + this.outerZoomLeft + " " + this.outerZoomTop);

						 
						 //make sure it will be within outer left bounds
						 if (topLeftTile.getxPositionZoomed() - leftDiff < this.outerZoomLeft){
							 //only scroll to the edge of the left outer boundary
							 //leftDiff = leftDiff - (this.outerZoomLeft - topLeftTile.getxPositionZoomed() - leftDiff);
							 leftDiff = this.outerZoomLeft - topLeftTile.getxPositionZoomed(); //topLeftTile.getxPositionZoomed() + this.outerZoomLeft; //leftDiff - (this.outerZoomLeft - topLeftTile.getxPositionZoomed());  
							 Log.w(getClass().getSimpleName() + "onDraw ACTION_MOVE ", "222 " + leftDiff);
						 } 
						 else {
							 //make sure it will be within visible left bounds
							 if (topLeftTile.getxPositionZoomed() - leftDiff > 1) {
								 leftDiff = topLeftTile.getxPositionZoomed() - 1;//leftDiff - (1 - topLeftTile.getxPositionZoomed() - leftDiff);   
								 this.readyToDraw = false;
								 Log.w(getClass().getSimpleName() + "onDraw ACTION_MOVE ", "333 " + leftDiff);
							 }
						 }
						 
						//grab top left tile and make sure it will be within outer top bounds
						 if (topLeftTile.getyPositionZoomed() - topDiff < this.outerZoomTop){ 
							 //only scroll to the edge of the top outer boundary
							 topDiff = this.outerZoomTop - topLeftTile.getyPositionZoomed();//topLeftTile.getyPositionZoomed() + this.outerZoomTop; //topDiff - (this.outerZoomTop - topLeftTile.getyPositionZoomed() - topDiff);
							 //topDiff = topDiff - (this.outerZoomTop - topLeftTile.getyPositionZoomed());
							 Log.w(getClass().getSimpleName() + "onDraw ACTION_MOVE ", "444 " + topDiff);
						 }
						 else { 
							 //make sure it will be within visible top bounds
							 if (topLeftTile.getyPositionZoomed() - topDiff > 0) {
								 topDiff = topLeftTile.getyPositionZoomed();// - 1;//topDiff - (1 - topLeftTile.getyPositionZoomed() - topDiff);
								 this.readyToDraw = false;
								 Log.w(getClass().getSimpleName() + "onDraw ACTION_MOVE ", "555 " + topDiff);
							 }
						 }
					     this.loadZoomedBoardByDiff(canvas, leftDiff, topDiff);		
					}
				 }
				 
				if (this.currentTouchMotion == MotionEvent.ACTION_UP){ 
					// canvas.drawColor(Color.CYAN);
					 //only if in tapped mode
					 GameTile tappedTile = this.FindTileFromPositionInFullViewMode(this.currentX, this.currentY);    
					 
					 //check for specific tile action (as opposed to full board action) here
					 
					 if (tappedTile == null) { return; } //do something here
				 
				     //find the equivalent tapped top location in zoomed layout
				     int tappedTop = this.midpoint - (((tappedTile.getRow() - 1) * this.zoomedTileWidth) + Math.round(this.zoomedTileWidth / 2));
				     
				     //make sure we don't pass the upper top boundary (this upper boundary is calculated to ensure that bottom of board does
				     //not render too high)
				     if (tappedTop < this.outerZoomTop) {tappedTop = this.outerZoomTop;}
				     
				     //make sure we don't pass the visible top boundary (this is the visible top boundary of the surface view minus padding)
				     if (tappedTop > 0) {tappedTop = 0;}
				     
				     //find the equivalent tapped left location in zoomed layout
				     int tappedLeft = this.midpoint - (((tappedTile.getColumn() - 1) * this.zoomedTileWidth) + Math.round(this.zoomedTileWidth / 2));
				     
				    //make sure we don't pass the far left boundary (this far left boundary is calculated to ensure that right side of the board does
				     //not render too far to the left)
				     if (tappedLeft < this.outerZoomLeft) {tappedLeft = this.outerZoomLeft;}
				     
				     //make sure we don't pass the visible left boundary (this is the visible left boundary of the surface view minus padding)
				     if (tappedLeft > 1) {tappedLeft = 1;}
				     
				     //draw the board to the canvas
				     this.loadZoomedBoard(canvas, tappedLeft, tappedTop); 
				      
				     //release the current tile context 
				     this.currentTile = null;  
	 
				  
				 }
			 }
			// this.previousTouchMotion = this.currentTouchMotion;
			// this.currentTouchMotion = ;
		
		 } 
	 }
	 
	private void loadZoomedBoardByDiff(Canvas canvas, int leftDiff, int topDiff) {
	     for (GameTile tile : this.tiles) {
	     	 tile.setxPositionZoomed(tile.getxPositionZoomed() - leftDiff);
	     	 tile.setyPositionZoomed(tile.getyPositionZoomed() - topDiff);
	 		 
	 		 this.loadZoomedBoardGuts(canvas, tile);
	     }
	}
	 
	private void loadZoomedBoard(Canvas canvas, int leftBasisPoint, int topBasisPoint) {
	     for (GameTile tile : this.tiles) {
	     	 tile.setxPositionZoomed(leftBasisPoint + ((tile.getColumn() - 1) * this.zoomedTileWidth) + ((tile.getColumn() - 1) * this.tileGap));
	 		 tile.setyPositionZoomed(topBasisPoint + ((tile.getRow() - 1) * this.zoomedTileWidth) + ((tile.getRow() - 1) * this.tileGap));
	 		 
	 		 this.loadZoomedBoardGuts(canvas, tile);
	     }
	}
	
	private void loadZoomedBoardGuts(Canvas canvas, GameTile tile){
	 	 canvas.drawBitmap(tile.getOriginalBitmapZoomed(),tile.getxPositionZoomed(), tile.getyPositionZoomed(), null);
     	 
     	 if (tile.getCurrentText().length() > 0){
	     	 Paint p = new Paint();
	     	 p.setColor(Color.WHITE);
	     	 p.setTextSize(Math.round(this.zoomedTileWidth * .6));
		     p.setAntiAlias(true);
		     p.setTypeface(this.letterTypeface);
		     Rect bounds = new Rect();
		     p.getTextBounds(tile.getCurrentText(), 0, tile.getCurrentText().length(), bounds);
		     int textLeft =  tile.getxPositionZoomed() + this.zoomedTileMidpoint - (Math.round(bounds.width() / 2));
		     int textTop =  tile.getyPositionZoomed() + this.zoomedTileMidpoint + (Math.round(bounds.height() / 2));
		     
		     canvas.drawText(tile.getCurrentText(), textLeft, textTop, p);
     	 }
	}
		
//	}
	 
	
 
	 private void LoadTiles() {		  
		 
		 Bitmap bgBase = BitmapFactory.decodeResource(getResources(), R.drawable.blank_tile_bg);
		 Bitmap bgBaseScaled = Bitmap.createScaledBitmap(bgBase, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
		 Bitmap bgBaseZoomed = Bitmap.createScaledBitmap(bgBase, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
		 
		 Bitmap bg4L = BitmapFactory.decodeResource(getResources(), R.drawable.tile_4l_bg);
		 Bitmap bg4LScaled = Bitmap.createScaledBitmap(bg4L, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
		 Bitmap bg4LZoomed = Bitmap.createScaledBitmap(bg4L, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
		 
		 Bitmap bg3L = BitmapFactory.decodeResource(getResources(), R.drawable.tile_3l_bg);
		 Bitmap bg3LScaled = Bitmap.createScaledBitmap(bg3L, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
		 Bitmap bg3LZoomed = Bitmap.createScaledBitmap(bg3L, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
		 
		 Bitmap bg3W = BitmapFactory.decodeResource(getResources(), R.drawable.tile_3w_bg);
		 Bitmap bg3WScaled = Bitmap.createScaledBitmap(bg3W, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
		 Bitmap bg3WZoomed = Bitmap.createScaledBitmap(bg3W, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
		 
		 Bitmap bg2L = BitmapFactory.decodeResource(getResources(), R.drawable.tile_2l_bg);
		 Bitmap bg2LScaled = Bitmap.createScaledBitmap(bg2L, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
		 Bitmap bg2LZoomed = Bitmap.createScaledBitmap(bg2L, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
		 
		 Bitmap bg2W = BitmapFactory.decodeResource(getResources(), R.drawable.tile_2w_bg);
		 Bitmap bg2WScaled = Bitmap.createScaledBitmap(bg2W, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
		 Bitmap bg2WZoomed = Bitmap.createScaledBitmap(bg2W, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
		
		 Bitmap bgStarter = BitmapFactory.decodeResource(getResources(), R.drawable.tile_starter_bg);
		 Bitmap bgStarterScaled = Bitmap.createScaledBitmap(bgStarter, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
		 Bitmap bgStarterZoomed = Bitmap.createScaledBitmap(bgStarter, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
		 
		 //starter
	//	 Bitmap bg4L = BitmapFactory.decodeResource(getResources(), R.drawable.tile_4l_bg);
	//	 Bitmap bg4LScaled = Bitmap.createScaledBitmap(bg4L, this.fullViewTileWidth + 1 , this.fullViewTileWidth + 1, false);
	//	 Bitmap bg4LZoomed = Bitmap.createScaledBitmap(bg4L, this.zoomedTileWidth + 1, this.zoomedTileWidth + 1, false);
		 
		 //for each row load each column 15x15
		 //row = y
	     //column = x
		 int id = 1;
		 for(int y = 0; y < 15; y++){
			 for(int x = 0; x < 15; x++){
				 GameTile tile = new GameTile();
				 tile.setId(id);
				 id += 1;
				 tile.setxPosition((this.excessWidth / 2) + (x * this.fullViewTileWidth) + (x * this.tileGap));
				 tile.setyPosition((y * this.fullViewTileWidth) + (y * this.tileGap));
				 tile.setColumn(x + 1);
				 tile.setRow(y + 1);

				 //check game object for already played letter
				 //check defaultLayout for bonus tiles etc
				 switch (this.layoutService.GetDefaultTile(tile.getId(), this.defaultLayout)) {
				 case FourLetter:
					 tile.setOriginalBitmap(bg4LScaled); //this will change as default bonus and played tiles are incorporated
					 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(bg4LZoomed); }
					 tile.setOriginalText(this.context.getString(R.string.tile_4L));
					 break;
				 case ThreeWord:
					 tile.setOriginalBitmap(bg3WScaled); //this will change as default bonus and played tiles are incorporated
					 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(bg3WZoomed); }
					 tile.setOriginalText(this.context.getString(R.string.tile_3W));
					 break;
				 case ThreeLetter:
					 tile.setOriginalBitmap(bg3LScaled); //this will change as default bonus and played tiles are incorporated
					 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(bg3LZoomed); }
					 tile.setOriginalText(this.context.getString(R.string.tile_3L));
					 break;
				 case TwoWord:
					 tile.setOriginalBitmap(bg2WScaled); //this will change as default bonus and played tiles are incorporated
					 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(bg2WZoomed); }
					 tile.setOriginalText(this.context.getString(R.string.tile_2W));
					 break;
				 case TwoLetter:
					 tile.setOriginalBitmap(bg2LScaled); //this will change as default bonus and played tiles are incorporated
					 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(bg2LZoomed); }
					 tile.setOriginalText(this.context.getString(R.string.tile_2L));
					 break;
				 case Starter:
					 tile.setOriginalBitmap(bgStarterScaled); //this will change as default bonus and played tiles are incorporated
					 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(bgStarterZoomed); }
					 break;
				 case None:
					 tile.setOriginalBitmap(bgBaseScaled); //this will change as default bonus and played tiles are incorporated
					 if (this.isZoomAllowed == true){ tile.setOriginalBitmapZoomed(bgBaseZoomed); }
					 break;
				 }
				 
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
	 
	 public void onDestroy(){
		this.surfaceDestroyed(this.surfaceHolder); 
	 }

	 private void drawFullView(Canvas canvas){
        for (GameTile tile : this.tiles) { 
	    	 canvas.drawBitmap(tile.getOriginalBitmap(),tile.getxPosition(), tile.getyPosition(), null);
	    	 
	    	 if (tile.getCurrentText().length() > 0){
		    	 Paint p = new Paint(); 
		    	 p.setColor(Color.WHITE);
			     p.setTextSize(Math.round(this.fullViewTileWidth * .6));
			     p.setAntiAlias(true);
			     p.setTypeface(this.letterTypeface);
			     Rect bounds = new Rect();
			     p.getTextBounds(tile.getCurrentText(), 0, tile.getCurrentText().length(), bounds);
			     int textLeft =  tile.getxPosition() + this.fullViewTileMidpoint - (Math.round(bounds.width() / 2));
			     int textTop =  tile.getyPosition() + this.fullViewTileMidpoint + (Math.round(bounds.height() / 2));
			     
			     canvas.drawText(tile.getCurrentText(), textLeft, textTop, p);
	    	 }
	    }
        this.readyToDraw = false; 
	 }
	 
}
