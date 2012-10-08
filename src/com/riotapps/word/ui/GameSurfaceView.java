package com.riotapps.word.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.riotapps.word.GameSurface;
import com.riotapps.word.R;
import com.riotapps.word.hooks.AlphabetService;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.TileLayout;
import com.riotapps.word.hooks.TileLayoutService;
import com.riotapps.word.utils.Constants;

import android.os.Message;
import android.util.Log;
import android.content.Context;
 
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Toast;

public class GameSurfaceView extends SurfaceView  implements SurfaceHolder.Callback {

	GameSurface parent;
	private static final String TAG = GameSurfaceView.class.getSimpleName();
	
	public void setParent(GameSurface parent){
		this.parent = parent;
	}
	
	GameSurfaceView me = this;
	Context context;
	GameThread gameThread = null;
	boolean isThreadRunning = false;
	SurfaceHolder surfaceHolder;
	Typeface bonusTypeface;
	Typeface letterTypeface;
	Typeface letterValueTypeface;
	private int currentX = 0;
    private int currentY = 20;
    private int fullWidth;
    private int fullHeight;
    private int fullViewTileWidth;
  //  private int top;
  //  private int left;
    private boolean isZoomed = false;
    private int excessWidth;
    private boolean isZoomAllowed = true; //if width of board greater than x disable zooming.  it means we are on a tablet and zooming not needed.
    private int activeTileWidth; 
    private long tapCheck = 0;
    private final float zoomMultiplier = 2.0f;
    private final int tileGap = 1;
    private static final int TRAY_TILE_GAP = 3;
    private int zoomedTileWidth;
    private int midpoint;
    private int fullViewTextSize;
    private int zoomedTextSize;
    private static final int TRAY_VERTICAL_MARGIN = 5;
    private static final int TRAY_TOP_BORDER_HEIGHT = 4;
    private static final int UPPER_GAP_BOTTOM_BORDER_HEIGHT = 4;
    private static final int LOWER_GAP_TOP_BORDER_HEIGHT = 4;
    private static final long SINGLE_TAP_DURATION_IN_NANOSECONDS = 300000000;
    private static final long DOUBLE_TAP_DURATION_IN_NANOSECONDS = 500000000;
    private static final long MOVE_STOPPED_DURATION_IN_MILLISECONDS = 200;
    private static final float MOVEMENT_TRIGGER_THRESHOLD = .05f;
    private static final int DECELERATION = 100;
    private float xVelocity = 0;
    private float yVelocity = 0;
    private int xPosition = 0;
    private int yPosition = 0;
    private int xDistance = 0;
    private int yDistance = 0;

    private static final float ANIMATION_TIMESTEP = .05f;
    private static final int NUMBER_OF_COORDINATES_TO_TRIGGER_MOMENTUM_SCROLLING = 3;
    private static final int NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED = 3;
    private long momentumScrollInterval = 20;
    
    private int bottomOfFullView;
    private int topGapHeight;
    private int bottomGapHeight;
    
    private int trayTileSize;
    private int draggingTileSize;
    private int trayTileLeftMargin;
    private int trayTop;
    //private int touchMotion = -1;
    private int outerZoomLeft;
    private int outerZoomTop; 
    private int fullViewTileMidpoint;
    private int zoomedTileMidpoint;
    private int trayTileMidpoint;
 //   private boolean isDrawn;
    private int scaleInProcess = 0;
    private boolean readyToDraw = false;
    private long dblTapCheck = 0;
    private boolean isMoving = false;
    private boolean isMomentum = false;
    private int previousY = 0;
    private int previousX = 0;
    private int previousTouchMotion = -3;
    private int currentTouchMotion = -3;
    private long previousTouchTime = 0;
  //  private long currentTouchTime = 0;
    private float currentSpeed = 0.0f;
    private int scoreboardHeight = 32;
    private int height;
    private SurfaceHolder holder;
    
  //  private Game game;
    private GameTile currentTile = null;
    private Bitmap trayBackground;
    private Bitmap logo;
    private boolean surfaceCreated = false;
    
	private Bitmap bgTray = BitmapFactory.decodeResource(getResources(), R.drawable.sbd_bg);
	private Bitmap bgTrayBaseScaled;
	private Bitmap bgTrayBaseDragging; 
	private boolean shuffleRedraw = false;
    
 
	public boolean isReadyToDraw() {
		return readyToDraw;
	}

	public void setReadyToDraw(boolean readyToDraw) {
		this.readyToDraw = readyToDraw;
	}

	List<Coordinate> coordinates = new ArrayList<Coordinate>();
	List<GameTile> tiles = new ArrayList<GameTile>();
	List<TrayTile> trayTiles = new ArrayList<TrayTile>();
    TileLayout defaultLayout;
    TileLayoutService layoutService;
    AlphabetService alphabetService;
   

 
//	public Game getGame() {
//		return game;
//	}

//	public void setGame(Game game) {
//		this.game = game;
//	}

	public GameSurfaceView(Context context) {
		super(context);
		//this.construct(context);
	 
	}

	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//this.construct(context);
		 
		
		// TODO Auto-generated constructor stub
	}

	public void construct(Context context) {
		Log.w(TAG, "construct called");
		
		this.context = context;
		this.layoutService = new TileLayoutService();
		this.defaultLayout = layoutService.GetDefaultLayout(context);
		
		this.alphabetService = new AlphabetService(context);
		//
		  this.setZOrderOnTop(true);
		 this.holder = getHolder();
		 this.holder.addCallback(this);
		 this.gameThread = new GameThread(holder, this);
		
		 setFocusable(true);
		 this.bonusTypeface = Typeface.createFromAsset(context.getAssets(), Constants.GAME_BOARD_FONT);
		 this.letterTypeface = Typeface.createFromAsset(context.getAssets(), Constants.GAME_LETTER_FONT); 
		 this.letterValueTypeface = Typeface.createFromAsset(context.getAssets(), Constants.GAME_LETTER_VALUE_FONT); 
		 this.holder.setFormat(PixelFormat.TRANSPARENT);// necessary
		 

		 
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
		   	    me.LoadTray();
		   	    me.LoadExtras();
		   	    Log.w(TAG, "run called");
		   	    
		  
		   	     LayoutParams lp = me.getLayoutParams();
			 	  lp.height = me.height;
			//	  // Apply to new dimension
			 	  me.setLayoutParams( lp );
		 
			 	   me.readyToDraw = true;
		        }

		     });
	}


	
	private void SetDerivedValues(){
		LayoutParams lp = me.getLayoutParams();
	 	 this.fullWidth = this.getWidth();
	 	 this.height = this.getHeight() - GameSurface.BUTTON_CONTROL_HEIGHT - GameSurface.SCOREBOARD_HEIGHT + 6;// lp.height; //getMeasuredHeight();
	 //	this.height = this.parent.getWindowHeight() - GameSurface.SCOREBOARD_HEIGHT - GameSurface.BUTTON_CONTROL_HEIGHT-100;
			this.trayTileSize = Math.round(this.fullWidth / 7.50f);	
			this.draggingTileSize  = Math.round(this.trayTileSize * 1.2f);
			if (this.draggingTileSize > 90){this.draggingTileSize = 90;}
			this.trayTileLeftMargin = Math.round(this.fullWidth - ((this.trayTileSize * 7) + (TRAY_TILE_GAP * 6))) / 2;
	 	this.trayTop = this.height - trayTileSize -  TRAY_VERTICAL_MARGIN; 
		this.bottomOfFullView = this.trayTop - TRAY_VERTICAL_MARGIN - TRAY_TOP_BORDER_HEIGHT - 1;
		this.topGapHeight = Math.round((this.bottomOfFullView - this.fullWidth) / 2);
		this.bottomGapHeight = this.bottomOfFullView - this.fullWidth -  this.topGapHeight;
	 	 
		this.fullViewTileWidth = Math.round(this.fullWidth/15) - this.tileGap; //-1 for the space between each tile
		this.excessWidth = this.fullWidth - ((this.fullViewTileWidth * 15) + (14 * this.tileGap));
		this.zoomedTileWidth = Math.round(this.fullViewTileWidth * this.zoomMultiplier);
		this.midpoint = Math.round(this.fullWidth / 2);
		this.outerZoomLeft = this.fullWidth - Math.round((this.zoomedTileWidth + 1) * 15); 
//		this.outerZoomTop = this.trayTop - TRAY_TOP_BORDER_HEIGHT - 1 - Math.round((this.zoomedTileWidth + 1) * 15); ///((this.fullViewTileWidth + 1) * 15) - Math.round((this.zoomedTileWidth + 1) * 15);  
		this.outerZoomTop = ((this.fullViewTileWidth + 1) * 15) - Math.round((this.zoomedTileWidth + 1) * 15) + this.topGapHeight + this.bottomGapHeight + 1;  

		this.fullViewTileMidpoint = Math.round(this.fullViewTileWidth / 2);
		this.zoomedTileMidpoint = Math.round(this.zoomedTileWidth / 2);	
		this.trayTileMidpoint = Math.round(this.trayTileSize / 2);	

		Bitmap bgTrayBase = BitmapFactory.decodeResource(getResources(), R.drawable.tray_tile_bg);
		this.bgTrayBaseScaled = Bitmap.createScaledBitmap(bgTrayBase, this.trayTileSize , this.trayTileSize, false);
		this.bgTrayBaseDragging = Bitmap.createScaledBitmap(bgTrayBase, this.draggingTileSize, this.draggingTileSize, false);
		this.trayBackground = Bitmap.createScaledBitmap(bgTray, this.fullWidth, this.trayTileSize + (TRAY_VERTICAL_MARGIN * 2), false);
		
		
		//Toast t = Toast.makeText(context, "Hello " +  this.height + " " + this.fullWidth + " " + getMeasuredHeight() , Toast.LENGTH_LONG);   
	    //t.show();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.w(TAG, "surfaceChanged called");
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.w(TAG, "surfaceCreated called");
		this.startThread();
		this.surfaceCreated = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.w(TAG, "surfaceDestroyed called");
	    this.stopThread();
	}
	
	public void onPause() {
		Log.w(TAG, "onPause called");
		this.gameThread.onPause();
	//	 this.stopThread();
	}
	
	public void onStop() {
		Log.w(TAG, "onStop called");
		 this.stopThread();
	}
	
//	public void onBackPressed() {
//		Log.w(TAG, "onBackPressed called");
//		 this.stopThread();
//	}
	
	public void onRestart() {
		 Log.w(TAG, "onRestart called");
		// if (this.surfaceCreated) {this.startThread();}
	//	this.gameThread = null;
		 this.gameThread = new GameThread(holder, this);
		 this.holder.setFormat(PixelFormat.TRANSPARENT);
		 this.startThread();
		 
		 me.readyToDraw = true;
	//	 this.startThread(); ///?????
	//	 if (this.surfaceCreated) {this.startThread();}
	}
	
	 public void onDestroy(){
		 Log.w(TAG, "onDestroy called");
		this.surfaceDestroyed(this.surfaceHolder); 
	 }
	
	 public void onWindowFocusChanged(){
		 Log.w(TAG, "onWindowFocusChanged called");
		 this.stopThread();
		 }
	 
	public void onResume() {
		Log.w(TAG, "onResume called");
		
		//make sure surface has been created first because onresume is initially called before surfacecreated and starting the 
		//thread then kills things (canvas is null in onDraw)
	 if (this.surfaceCreated) {this.startThread();}
	}
	
	
	private void startThread(){
		
	//	if (!mGameIsRunning) {
	 //       thread.start();
	 //       mGameIsRunning = true;
	 //   } else {
	//        thread.onResume();
	//    }
		
		 
		Log.w(TAG, "startThread called is thread alive " + this.gameThread.isAlive() + " isThreadRunning: " + this.isThreadRunning); 
		if (!this.isThreadRunning){ //() !=Thread.State.RUNNABLE) { 
			//if (!this.gameThread.isAlive()){
				this.gameThread.start();
				this.gameThread.setRunning(true);
				this.isThreadRunning = true;
			//}
		}
	//	else {
	//		this.gameThread.onResume();
	//	}
	}
	
	private void stopThread(){
		// simply copied from sample application LunarLander:
	    // we have to tell thread to shut down & wait for it to finish, or else
	    // it might touch the Surface after we return and explode
		Log.w(TAG, "stopThread called is thread alive " + this.gameThread.isAlive() + " isThreadRunning: " + this.isThreadRunning); 
		if (this.isThreadRunning){
	//		this.gameThread = null;
	//		this.isThreadRunning = false;
//		}
		
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
	    this.isThreadRunning = false;  
	    Log.w(TAG, "stopThread is thread alive " + this.gameThread.isAlive());
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
            	// if (this.dblTapCheck == 0){ this.dblTapCheck = currentTouchTime; }
            	// this.invalidate();
            	 this.isMoving = false; 
            	 
          
            	 this.previousX = this.currentX;
            	 this.previousY = this.currentY;
            	 this.coordinates.clear();
            	 this.isMomentum = false;
            	 currentTile = this.FindTileFromPositionInFullViewMode(this.currentX, this.currentY);
            	// this.currentTouchMotion = MotionEvent.ACTION_DOWN;
            	// return false; //??
            	  break;

             case MotionEvent.ACTION_UP:
            	 //includes a check to ignore double taps
            	 //  Log.w(getClass().getSimpleName() + "onTouchEvent ActionUP ", this.tapCheck + " " + currentTouchTime + " " + this.readyToDraw);
            	   
            	   
            	   this.readyToDraw = false;
            	   Log.w(TAG, "ACTION_UP tapcheck = " + this.tapCheck + " dbltapcheck = " + this.dblTapCheck + " diff = " + (this.tapCheck - this.dblTapCheck)); 
            	   
            	 if (this.tapCheck > 0 && currentTouchTime - this.tapCheck <= SINGLE_TAP_DURATION_IN_NANOSECONDS) { 
            	 // && (currentTouchTime - this.dblTapCheck >= 800000000 || this.dblTapCheck == 0)) {
            	//	 if (this.isMoving){
            	//		 //if we are coming out of a drag, up event just means drag is finished, nothing to do here, just move along
            	//		 this.isMoving = false;
             	
            	//	 }
            		// else {// if (this.currentTouchMotion == MotionEvent.ACTION_DOWN){ //action up should immediately follow and action down to be a tap
	            	
            		 //first make sure action up event is not associated with move event
            		 if (!this.isMoving) {  
            			  
            			 //then make sure to ignore double tap events
            			 if((this.tapCheck - this.dblTapCheck) >= (DOUBLE_TAP_DURATION_IN_NANOSECONDS - SINGLE_TAP_DURATION_IN_NANOSECONDS) || this.dblTapCheck == 0){
            				 this.isZoomed = !this.isZoomed;
            				 this.readyToDraw = true;
            				 this.dblTapCheck = currentTouchTime;
            			 }
            			 else {
            				 //restart double click check
            				 this.dblTapCheck = 0;
            			 }
            			 
            		 }
            	
            		// else {
            		//	 //if previous action was not a down, don't draw
            		//	 this.readyToDraw = false;
            		// }
            	 }
            	 else {
            		 //we are coming out of a move action here...let's determine if the momentum scroll should be triggered
        			 Log.w(TAG,"onTouchEvent: ACTION_UP number of coordinates" + this.coordinates.size());
    				 //if we are coming out of a move and we have at least 30 coordinates captured by move, let's trigger momentum scrolling
    				 if (this.coordinates.size() == NUMBER_OF_COORDINATES_TO_TRIGGER_MOMENTUM_SCROLLING){
    					 //if distance is too short that means the finger was in place and not moving and in this case
    					 //do not trigger momentum scroll
    					 
    					 int previousX = Math.round(this.coordinates.get(0).getxLocation());
    					 int currentX = Math.round(this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getxLocation());
    					 int previousY = Math.round(this.coordinates.get(0).getyLocation());
    					 int currentY = Math.round(this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getyLocation());
    					 
    					 Log.w(TAG, "onTouchEvent: ACTION_UP previousX=" + previousX + " currentX=" + currentX + " diff=" + (currentX - previousX)); 
    					 Log.w(TAG, "onTouchEvent: ACTION_UP previousY=" + previousX + " currentY=" + currentY + " diff=" + (currentY - previousY)); 
    					
    					 //time since last move action
    					 long timeSinceLastMoveInMilliseconds = (System.nanoTime() - this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getTimestamp()) / 1000000;
    					 
    					if (timeSinceLastMoveInMilliseconds < MOVE_STOPPED_DURATION_IN_MILLISECONDS) {
    				//	if (currentX + Math.abs(currentX - previousX) >= currentX * (1 + MOVEMENT_TRIGGER_THRESHOLD) &&
    				//		currentY + Math.abs(currentY - previousY) >= currentY * (1 + MOVEMENT_TRIGGER_THRESHOLD)) {	
 //   					if (this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getxLocation() <= Math.round(this.coordinates.get(0).getxLocation() * (1 + MOVEMENT_TRIGGER_THRESHOLD)) && 
  //  						this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getxLocation() >= Math.round(this.coordinates.get(0).getxLocation() * (1 - MOVEMENT_TRIGGER_THRESHOLD)) && 
   // 						this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getyLocation() <= Math.round(this.coordinates.get(0).getyLocation() * (1 + MOVEMENT_TRIGGER_THRESHOLD)) && 
   // 						this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getyLocation() >= Math.round(this.coordinates.get(0).getyLocation() * (1 - MOVEMENT_TRIGGER_THRESHOLD))){ 
    				   // if (Math.abs(this.coordinates.get(0).getxLocation()) >= Math.abs(this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getxLocation() * (1 + MOVEMENT_TRIGGER_THRESHOLD)) &&
    				   // 		Math.abs(this.coordinates.get(0).getyLocation()) >= Math.abs(this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getyLocation() * (1 + MOVEMENT_TRIGGER_THRESHOLD))){

	    					float speed = this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getTimestamp() - this.coordinates.get(0).getTimestamp();
	    					speed = speed / 1000000; //convert to milliseconds	  

						 
						    int xDisplacement = this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getxLocation() - this.coordinates.get(0).getxLocation();
					          
					        int yDisplacement = this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getyLocation() - this.coordinates.get(0).getyLocation();
					        
					        Log.w(TAG, "onTouchEvent: ACTION_UP speed " + speed);
					        Log.w(TAG, "onTouchEvent: ACTION_UP xDisplacement " + xDisplacement + " yDisplacement " + yDisplacement);
					        
					        this.xVelocity = (xDisplacement / ANIMATION_TIMESTEP) * speed;
					        this.yVelocity = (yDisplacement / ANIMATION_TIMESTEP) * speed;
					        this.xPosition = tiles.get(0).getxPositionZoomed();
					        this.yPosition = tiles.get(0).getyPositionZoomed();    
					        
					        this.xDistance =  this.coordinates.get(0).getxLocation() - this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getxLocation(); ///multiply by speed to adjust
					        this.yDistance =  this.coordinates.get(0).getyLocation() - this.coordinates.get(NUMBER_OF_COORDINATES_TO_DETERMINE_DIRECTION_AND_SPEED - 1).getyLocation(); ///multiply by speed to adjust;
						 
					        Log.w(TAG, "onTouchEvent: ACTION_UP xDistance=" + this.xDistance + "  yDistance=" + this.yDistance + " speed=" + speed); 
					        
					        this.isMomentum = true;
	    					this.readyToDraw = true;
    				    }
    					else {
    						 Log.w(TAG, "onTouchEvent: ACTION_UP threshold of movement not met to trigger momentum scrolling"); 
    					}
    				 }
        		 
        		 }
            	 // this.currentTouchMotion = MotionEvent.ACTION_UP;
            	 this.previousX = 0;
            	 this.previousY = 0;
            	 this.isMoving = false;
            	 this.tapCheck = 0;
            	 break;
             case MotionEvent.ACTION_MOVE:
            	 
            	// this.currentTouchMotion = MotionEvent.ACTION_MOVE;
            	 //Log.w(TAG, "ACTION_MOVE: x " + )
            	 
            	 this.tapCheck = 0;
            	 this.dblTapCheck = 0;
            	 this.isMoving = true;
            	 if (!this.isZoomed){
            		 this.readyToDraw = false;
            	 }
            	 //use absolute values here????
            	 //else if (this.currentTouchMotion == MotionEvent.ACTION_MOVE && this.previousX == this.currentX && this.previousY == this.currentY){
            	 else if (this.currentX <= Math.round(this.previousX * (1 + MOVEMENT_TRIGGER_THRESHOLD)) && 
            			  this.currentX >= Math.round(this.previousX * (1 - MOVEMENT_TRIGGER_THRESHOLD)) && 
            			  this.currentY <= Math.round(this.previousY * (1 + MOVEMENT_TRIGGER_THRESHOLD)) && 
            			  this.currentY >= Math.round(this.previousY * (1 - MOVEMENT_TRIGGER_THRESHOLD))){
            		 Log.w(TAG,"onTouchEvent minimum threshold not met");
            	 //else if (this.previousX == this.currentX && this.previousY == this.currentY){
            		 this.readyToDraw = false;
            	}
            	else {
            		 this.readyToDraw = true;
            		 
            		 //keep latest 30 coordinates in context, last 10 will be used to calculate momentum and direction for scrolling
            		 //hainvg 30 determines if action_up triggers momentum scrolling logic
            		 this.coordinates.add(new Coordinate(this.currentX, this.currentY, currentTouchTime));
            		 //quick loop to remove first in coordinates over 30, normally should only ever remove one, but just in case, we'll loop it
            		 while (this.coordinates.size() > NUMBER_OF_COORDINATES_TO_TRIGGER_MOMENTUM_SCROLLING){
            			 this.coordinates.remove(0);
            		 }
            		 Log.w(TAG,"onTouchEvent: coodinates size" + this.coordinates.size());
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
	 // Log.w(getClass().getSimpleName() + "onDraw ",this.currentTouchMotion + " " + this.tapCheck + " " +  this.isMoving  + " " + this.readyToDraw + " " + this.previousX + " " + this.previousY
	 //			 + " " + this.currentX + " " + this.currentY);
		 
		long currentTouchTime = System.nanoTime();
		
		if (!this.shuffleRedraw){
			//  if (this.touchMotion == MotionEvent.ACTION_MOVE ) {this.readyToDraw = false;} 
			 if (this.currentTouchMotion == MotionEvent.ACTION_DOWN){ this.readyToDraw = false;}  
			 
			//this will have to change if dragging a tile 
			 if (this.currentTouchMotion == MotionEvent.ACTION_MOVE && this.isZoomed == false) { this.readyToDraw = false; }
			 
			 //if we are in middle of action move but we are not moving (the finger is pressed but not moving, don't redraw
	 		 if (this.currentTouchMotion == MotionEvent.ACTION_MOVE && 
	 			this.previousTouchMotion == MotionEvent.ACTION_MOVE &&
	 			this.currentX <= Math.round(this.previousX * (1 + MOVEMENT_TRIGGER_THRESHOLD)) && 
	 			this.currentX >= Math.round(this.previousX * (1 - MOVEMENT_TRIGGER_THRESHOLD)) && 
	 			this.currentY <= Math.round(this.previousY * (1 + MOVEMENT_TRIGGER_THRESHOLD)) && 
	 			this.currentY >= Math.round(this.previousY * (1 - MOVEMENT_TRIGGER_THRESHOLD))){
	 			 Log.w(TAG,"onDraw minimum threshold not met");
	 			 	this.readyToDraw = false; 
	 			 }
		}
		 
		 if (this.readyToDraw == true){ 
 
			 // Log.w(getClass().getSimpleName() + "onDraw2 ",this.currentTouchMotion + " " + this.tapCheck + " " +  this.isMoving  + " " + this.readyToDraw + " " + this.previousX + " " + this.previousY
			//	 		 + " " + this.currentX + " " + this.currentY + " " + this.previousTouchMotion);
			 
			// if (this.touchMotion != MotionEvent.ACTION_MOVE ) { 
			// canvas.drawColor(0, Mode.CLEAR); ///clears out the previous drawing on the canvas
		 
			 //}
			 int tileFontSize;
			 this.readyToDraw = false;
			 
			 if (this.isZoomed == false || this.isZoomAllowed == false){
				 
				 this.drawFullBoard(canvas);
				
				
			 }
			 else {
				// if (this.touchMotion == MotionEvent.ACTION_UP) {
				//	 this.isZoomed = false; ///turn off zoom since we are handling now
				// }
				 if (this.shuffleRedraw){
					 this.drawBoardOnMove(canvas, 0, 0);
					
				 }
				 else if (this.isMomentum){
					 Log.w(TAG,"onDraw drawMomentumScroll about to be called");
					 this.drawMomentumScroll(canvas);
				 }
				 else if (this.currentTouchMotion == MotionEvent.ACTION_MOVE) {
					 Log.w(TAG,"onDraw drawBoardOnMove about to be called");
					 this.drawBoardOnMove(canvas, this.previousX - this.currentX, this.previousY - this.currentY);
				
				 }
				 else if (this.currentTouchMotion == MotionEvent.ACTION_UP){ 	
					
				// 	this.parent.updateHandler.sendMessage(this.parent.updateHandler.obtainMessage(GameSurface.MSG_SCOREBOARD_VISIBILITY, GONE, 0));
					 
			//		MarginLayoutParams  params = (MarginLayoutParams )this.getLayoutParams();
			//		params.setMargins(params.leftMargin, params.topMargin - 50, params.rightMargin, params.bottomMargin); //substitute parameters for left, top, right, bottom
			//		this.setLayoutParams(params); 
					canvas.drawColor(0, Mode.CLEAR);
					this.drawBoardZoomOnUp(canvas);
					this.readyToDraw = false;
	 
				  
				 }
			 }
			// this.previousTouchMotion = this.currentTouchMotion;
			// this.currentTouchMotion = ;
		    this.drawTray(canvas);
		    
		    if (this.shuffleRedraw){
		    	this.shuffleRedraw = false;
		    	this.readyToDraw = false;
		    }
		 } 
	 }
	 
	private void drawFullBoard(Canvas canvas){
		 canvas.drawColor(0, Mode.CLEAR);
		 this.drawUpperGap(canvas);
		 this.drawFullView(canvas);
		 this.drawLowerGap(canvas);	
	}
	
	private void drawBoardOnMove(Canvas canvas, int leftDiff, int topDiff){
			
		 this.readyToDraw = false;
		   
		 boolean setReadyToDraw = false;
	//	 int leftDiff = this.previousX - this.currentX ;
	//	 int topDiff =  this.previousY - this.currentY;
		 
		 //handle tile drag later, first drag the whole board around
		// if (this.currentX < this.outerZoomLeft || this.currentY < this.outerZoomTop) {
		//	 this.readyToDraw = false; 
		//	 newLeft = 
		// }
		 
		 Log.w(TAG,"drawBoardOnMove: leftDiff=" + leftDiff + " topDiff=" + topDiff + " prevX=" +  this.previousX  + " prevY=" + this.previousY + 
				 " currX="  +  this.currentX  + " currY=" + this.currentY + " outerZoomLeft=" 
				 + this.outerZoomLeft + " outerZoomTop=" + this.outerZoomTop);
		 
		  this.previousX = this.currentX;
		  this.previousY = this.currentY;
	 
		 
						
//		 if (this.currentTile.getPlacedText().length() > 0 ){
//			 //drag this letter, not the board
//		 }
//		 else {
			//drag/scroll entire board 
		 
		 
			 //if (tappedTile == null) { return; } ///do something here, this is causing the board to disappear when scrolled out of bounds
			 
			 //grab top left tile  
			 GameTile topLeftTile = this.tiles.get(0);
			 
			// Log.w(getClass().getSimpleName() + "onDraw ACTION_MOVE ",leftDiff + " " + topDiff + " " +  this.previousX  + " " + this.previousY + " "
			// + topLeftTile.getxPositionZoomed() + " " + topLeftTile.getyPositionZoomed() + " "
			// + this.outerZoomLeft + " " + this.outerZoomTop);

			 
			 //make sure it will be within outer left bounds
			 if (topLeftTile.getxPositionZoomed() - leftDiff < this.outerZoomLeft){
				 //only scroll to the edge of the left outer boundary
				 //leftDiff = leftDiff - (this.outerZoomLeft - topLeftTile.getxPositionZoomed() - leftDiff);
				 //leftDiff = this.outerZoomLeft - topLeftTile.getxPositionZoomed(); //topLeftTile.getxPositionZoomed() + this.outerZoomLeft; //leftDiff - (this.outerZoomLeft - topLeftTile.getxPositionZoomed());  
				 leftDiff = topLeftTile.getxPositionZoomed() - this.outerZoomLeft; //topLeftTile.getxPositionZoomed() + this.outerZoomLeft; //leftDiff - (this.outerZoomLeft - topLeftTile.getxPositionZoomed());  
					
				 
				 Log.w(TAG, "drawBoardOnMove: leftdiff(1)=" + leftDiff);
			 } 
			 else {
				 //make sure it will be within visible left bounds
				 if (topLeftTile.getxPositionZoomed() - leftDiff > 0) {
					 leftDiff = topLeftTile.getxPositionZoomed() - 0;//leftDiff - (1 - topLeftTile.getxPositionZoomed() - leftDiff);   
					 
					 Log.w(TAG, "drawBoardOnMove: leftdiff(2)=" + leftDiff);
				 }
				 else {
					 setReadyToDraw = true;
				 }
			 }
			 
			//grab top left tile and make sure it will be within outer top bounds
			 if (topLeftTile.getyPositionZoomed() - topDiff < this.outerZoomTop){ 
				 //only scroll to the edge of the top outer boundary
				 topDiff = this.outerZoomTop;// - topLeftTile.getyPositionZoomed();//topLeftTile.getyPositionZoomed() + this.outerZoomTop; //topDiff - (this.outerZoomTop - topLeftTile.getyPositionZoomed() - topDiff);
				 //topDiff = topDiff - (this.outerZoomTop - topLeftTile.getyPositionZoomed());
				 topDiff = topLeftTile.getyPositionZoomed() - this.outerZoomTop;
				 Log.w(TAG, "drawBoardOnMove: topdiff(1)=" + topDiff);
			 }
			 else { 
				 //make sure it will be within visible top bounds
				 if (topLeftTile.getyPositionZoomed() - topDiff > 0) {
					 topDiff = topLeftTile.getyPositionZoomed();// - 1;//topDiff - (1 - topLeftTile.getyPositionZoomed() - topDiff);
					 
					 Log.w(TAG, "drawBoardOnMove: topdiff(2)=" + topDiff);
				 }
				 else {
					 setReadyToDraw = true;
				 }
			 }

			
				// this.previousX = this.currentX;
				// this.previousY = this.currentY;
				 canvas.drawColor(0, Mode.CLEAR);
				 this.drawZoomedBoardByDiff(canvas, leftDiff, topDiff);	
				 if (setReadyToDraw){ this.readyToDraw = true; }
		  //   this.loadZoomedBoardByDiff(canvas, leftDiff, topDiff);	
		   //  if (setReadyToDraw){this.readyToDraw = true;}
//		}
		
	} 
	
	
	private void drawMomentumScroll(Canvas canvas){
	            // Free scrolling. Decelerate gradually.
		//grab velocity from speed of movement before action_up

		
		
		//for smooth scrolling you'd need to make some sort of method that takes a few points after scrolling
		//(i.e the first scroll point and the 10th) , subtract those and scroll by that number in a for each loop that makes it gradually slower
		//( ScrollAmount - turns - Friction ).
		//You could simulate this with a "recent axis changes" queue.

		//If you store say the last half a second of changes with the corresponding timestamps, you can then 
		//test if the queue is longer than a value N (ie if the user dragged it quicker than usual towards the end). 
		//You know the total distance traveled in the last half a second, the time, from those you can get a speed.

		//Scale the speed to something reasonable (say.. for 15px/.5sec, map to ~25px/sec) and apply a negative acceleration
		//(also appropiately scaled, for the example above, say -20px/sec) every couple of milliseconds 
		//(or as fast as your system can easily handle it, don't overstress it with this).

		//Then run a timer, updating the speed at each tick (speed+=accel*time_scale), then the position (position+=speed*time_scale). When the speed reaches 0 (or goes below it) kill the timer.

		try {
			Thread.sleep(this.momentumScrollInterval);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 //this.momentumScrollInterval = Math.round(this.momentumScrollInterval * 1.25f);
		
		this.xDistance -= (this.xDistance > 0 ? +1 : -1) * .02;
		this.yDistance -= (this.yDistance > 0 ? +1 : -1) * .02;
		
		
		 this.drawBoardOnMove(canvas, this.xDistance, this.yDistance);
	        
	        if (this.xDistance == 0 && this.yDistance == 0){
	        	this.readyToDraw = false;
	        }
	        
	        Log.w(TAG, "drawMomentumScroll xDistance=" + this.xDistance + " yDistance=" + this.yDistance );
	        
	        if (!this.readyToDraw){this.isMomentum = false;}
		
	}
	
	
	 
   private void drawBoardZoomOnUp(Canvas canvas){
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
	     this.drawZoomedBoard(canvas, tappedLeft, tappedTop); 
	      
	     //release the current tile context 
	     this.currentTile = null;  
   }
	
	private void drawZoomedBoardByDiff(Canvas canvas, int leftDiff, int topDiff) {
	    int x = 0; 
		for (GameTile tile : this.tiles) {
	    	if (x == 0){ 
	    	Log.w(TAG,"loadZoomedBoardByDiff before x=" + tile.getxPositionZoomed() + " after=" + (tile.getxPositionZoomed() - leftDiff));
	    	Log.w(TAG,"loadZoomedBoardByDiff before y=" + tile.getyPositionZoomed() + " after=" + (tile.getyPositionZoomed() - topDiff));
	    	}
			x += 1;
	     	 tile.setxPositionZoomed(tile.getxPositionZoomed() - leftDiff);
	     	 tile.setyPositionZoomed(tile.getyPositionZoomed() - topDiff);
	 		 
	 		 this.drawZoomedBoardGuts(canvas, tile);
 
	     }
	}
	 
	private void drawZoomedBoard(Canvas canvas, int leftBasisPoint, int topBasisPoint) {
	     for (GameTile tile : this.tiles) {
	     	 tile.setxPositionZoomed(leftBasisPoint + ((tile.getColumn() - 1) * this.zoomedTileWidth) + ((tile.getColumn() - 1) * this.tileGap));
	 		 tile.setyPositionZoomed(topBasisPoint + ((tile.getRow() - 1) * this.zoomedTileWidth) + ((tile.getRow() - 1) * this.tileGap));
	 		 
	 		 this.drawZoomedBoardGuts(canvas, tile);
	     }
	}
	
	private void drawZoomedBoardGuts(Canvas canvas, GameTile tile){
	 	 canvas.drawBitmap(tile.getOriginalBitmapZoomed(),tile.getxPositionZoomed(), tile.getyPositionZoomed(), null);
     	 
     	 if (tile.getCurrentText().length() > 0){
	     	 Paint p = new Paint();
	     	 p.setColor(Color.WHITE);
	     	 p.setTextSize(Math.round(this.zoomedTileWidth * .6));
		     p.setAntiAlias(true);
		     p.setTypeface(this.bonusTypeface);
		     Rect bounds = new Rect();
		     p.getTextBounds(tile.getCurrentText(), 0, tile.getCurrentText().length(), bounds);
		     int textLeft =  tile.getxPositionZoomed() + this.zoomedTileMidpoint - (Math.round(bounds.width() / 2));
		     int textTop =  tile.getyPositionZoomed() + this.zoomedTileMidpoint + (Math.round(bounds.height() / 2));
		     
		     canvas.drawText(tile.getCurrentText(), textLeft, textTop, p);
     	 }
	}
		
//	}
	 
	private void drawUpperGap(Canvas canvas){
		//3366dd
		
		Paint pGap = new Paint(); 
		pGap.setColor(Color.parseColor("#eec591"));  //grab color from drawable
	    
		pGap.setAntiAlias(true);
	  //   p.setTypeface(this.bonusTypeface);
	     Rect boundsGap = new Rect();
	     boundsGap.left = 0;
	     boundsGap.right = this.fullWidth;
	     boundsGap.top = 0;
	     boundsGap.bottom = this.topGapHeight;
		
	     canvas.drawRect(boundsGap, pGap);
	     
	     Paint p = new Paint();
     	 p.setColor(Color.WHITE);
     	 //p.setTextAlign(Paint.Align.LEFT);
     	 p.setTextSize(Math.round(this.topGapHeight * .4));
	     p.setAntiAlias(true);
	     p.setTypeface(this.bonusTypeface);
	     Rect bounds = new Rect();
	     String lastActionText = this.parent.getGame().getLastActionText();
	     p.getTextBounds(lastActionText, 0, lastActionText.length(), bounds);
	     int textLeft =  this.midpoint - (Math.round(bounds.width() / 2));
	     
	     //this is a hack because for some reason the vertical origin is going up in direction as opposed to down
	     int textTop =  Math.round(this.topGapHeight / 2) + (bounds.height() / 3); 
	     
	     ///account for line breaks for long strings
	     
	     canvas.drawText(lastActionText, textLeft, textTop, p);
	     //canvas.drawBitmap(this.logo, textLeft, 10, null); ///do not use 10,,,figure out math
	     //Yes. If you want to use the colour definition in the res/colors.xml file with the ID R.color.black, then you can't just use the ID. 
	     //If you want to get the actual colour value from the resources, use paint.setColor(getResources().getColor(R.color.black)); – Matt Gibson Dec 7 '11 at 20:49
	    
	}

	private void drawLowerGap(Canvas canvas){
		//3366dd
		
		Paint pGap = new Paint(); 
		pGap.setColor(Color.parseColor("#eec591"));  //grab color from drawable
	    
		pGap.setAntiAlias(true);
	  //   p.setTypeface(this.bonusTypeface);
	     Rect boundsGap = new Rect();
	     boundsGap.left = 0;
	     boundsGap.right = this.fullWidth;
	     boundsGap.top = this.topGapHeight + this.fullWidth;//this.trayTop - this.bottomGapHeight - TRAY_TOP_BORDER_HEIGHT - 1;
	     boundsGap.bottom = this.trayTop - 1;
		
	     canvas.drawRect(boundsGap, pGap);
	    // canvas.drawText("Junior", textLeft, 10, p);	     
	     canvas.drawBitmap(this.logo, this.midpoint - (Math.round(this.logo.getWidth() / 2)), this.topGapHeight + this.fullWidth + Math.round(this.bottomGapHeight / 2) - Math.round(this.logo.getHeight() / 2) , null); ///do not use 10,,,figure out math
	     
	     
	//     Paint pBorder = new Paint(); 
	//		pBorder.setColor(Color.parseColor("#eec591"));  //grab color from drawable
	//	    
	//	    pBorder.setAntiAlias(true);
	//	     Rect boundsBorder = new Rect();
	//	     boundsBorder.left = 0;
	//	     boundsBorder.right = this.fullWidth;
	//	     boundsBorder.top = this.topGapHeight  - LOWER_GAP_TOP_BORDER_HEIGHT;
	//	     boundsBorder.bottom = this.topGapHeight;
		    
	//	     canvas.drawRect(boundsBorder, pBorder);
	     
	///draw  thanks for playing if game is over '''to the right
		    
	}
	
	
	
	private void drawTray(Canvas canvas){
		Paint pBorder = new Paint(); 
		pBorder.setColor(Color.parseColor("#eec591"));
	    
	    pBorder.setAntiAlias(true);
	  //   p.setTypeface(this.bonusTypeface);
	     Rect boundsBorder = new Rect();
	     boundsBorder.left = 0;
	     boundsBorder.right = this.fullWidth;
	     boundsBorder.top = this.trayTop - TRAY_VERTICAL_MARGIN - TRAY_TOP_BORDER_HEIGHT;
	     boundsBorder.bottom = this.trayTop - TRAY_VERTICAL_MARGIN;
	     canvas.drawRect(boundsBorder, pBorder);
	     
		canvas.drawBitmap(this.trayBackground, 0, this.trayTop - TRAY_VERTICAL_MARGIN, null);
		
		for (TrayTile tile : this.trayTiles) {
			 canvas.drawBitmap(tile.getCurrentBitmap(),tile.getxPosition(), tile.getyPosition(), null);
			 if (tile.getCurrentLetter().length() > 0){
		     	 Paint pLetter = new Paint();
		     	 pLetter.setColor(Color.DKGRAY);
		     	 pLetter.setTextSize(Math.round(this.trayTileSize * .72));
		     	 pLetter.setAntiAlias(true);
		     	 pLetter.setTypeface(this.letterTypeface);
			     Rect boundsLetter = new Rect();
			     pLetter.getTextBounds(tile.getCurrentLetter(), 0, tile.getCurrentLetter().length(), boundsLetter);
			     
			     //find the midpoint and scoot over 5% to the left and 5% down
			     int textLeft =  tile.getxPosition() + this.trayTileMidpoint - Math.round(this.trayTileMidpoint * .08f) - (Math.round(boundsLetter.width() / 2));
			     int textTop =  tile.getyPosition() + this.trayTileMidpoint + Math.round(this.trayTileMidpoint * .08f) + (Math.round(boundsLetter.height() / 2));
			     
			     canvas.drawText(tile.getCurrentLetter(), textLeft, textTop, pLetter);
			     
			     Paint pValue = new Paint();
			     pValue.setColor(Color.BLACK);
			     pValue.setTextSize(Math.round(this.trayTileSize * .25f));
			     pValue.setAntiAlias(true);
			     
			     pValue.setTypeface(Typeface.SANS_SERIF);
			     Rect boundsValue = new Rect();
			     String value = Integer.toString(this.alphabetService.getLetterValue(tile.getCurrentLetter()));
			     pValue.getTextBounds(value, 0, value.length(), boundsValue);
			     
			     //find the midpoint and scoot over 5% to the left and 5% down
			     int textLeftValue =  tile.getxPosition() + this.trayTileSize - boundsValue.width() - Math.round(this.trayTileSize * .12f);
			     int textTopValue =  tile.getyPosition() + Math.round(this.trayTileSize * .15f) + (Math.round(boundsValue.height() / 2));
			     
			     canvas.drawText(value, textLeftValue, textTopValue, pValue);
			     
	     	 }
			 
		 }
		
	}
 
	public void shuffleTray(){
		
		Collections.shuffle(this.parent.getGameState().getTrayTiles());
		GameStateService.setGameState(this.context, this.parent.getGameState());

		this.LoadTray();
		this.shuffleRedraw = true;
		this.readyToDraw = true;
	}
	
	private void LoadExtras(){
		int height = Math.round(this.bottomGapHeight * .6F);
		Bitmap bgLogo = BitmapFactory.decodeResource(getResources(), R.drawable.wordsmash_logo7);
	 
		float factor = height / (float) bgLogo.getHeight();
		this.logo = Bitmap.createScaledBitmap(bgLogo, (int) (bgLogo.getWidth() * factor), height, false);  
	  
	} 
	
	private void LoadTray() {		

		//move bitmap stuff to shared location
	//	Bitmap bgTray = BitmapFactory.decodeResource(getResources(), R.drawable.sbd_bg);
	//	this.trayBackground = Bitmap.createScaledBitmap(bgTray, this.fullWidth, this.trayTileSize + (TRAY_VERTICAL_MARGIN * 2), false);
		 
		
	//	 Bitmap bgBase = BitmapFactory.decodeResource(getResources(), R.drawable.tray_tile_bg);
	//	 Bitmap bgTrayBaseScaled = Bitmap.createScaledBitmap(bgBase, this.trayTileSize , this.trayTileSize, false);
	//	 Bitmap bgTrayBaseDragging = Bitmap.createScaledBitmap(bgBase, this.draggingTileSize, this.draggingTileSize, false);
		
		 //load game letters into here (soon)
		 
		 this.trayTiles.clear();
		 
		 for(int y = 0; y < 7; y++){
			 TrayTile tile = new TrayTile();
			 tile.setId(y);
			 tile.setxPosition(this.trayTileLeftMargin + ((this.trayTileSize + TRAY_TILE_GAP) * tile.getId()));
			 tile.setyPosition(this.trayTop);
			 tile.setOriginalBitmap(this.bgTrayBaseScaled);
			 tile.setOriginalBitmapDragging(this.bgTrayBaseDragging);

			 //this will come from state object if it exists for this turn, this is temp
			 tile.setOriginalLetter(this.parent.getGameState().getTrayTiles().get(y).getLetter());
			 
			 this.trayTiles.add(tile);
		 }
	}
	
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
				 tile.setyPosition((y * this.fullViewTileWidth) + (y * this.tileGap) + this.topGapHeight);
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
	 

	 private void drawFullView(Canvas canvas){
        for (GameTile tile : this.tiles) { 
	    	 canvas.drawBitmap(tile.getOriginalBitmap(),tile.getxPosition(), tile.getyPosition(), null);
	    	 
	    	 if (tile.getCurrentText().length() > 0){
		    	 Paint p = new Paint(); 
		    	 p.setColor(Color.WHITE);
			     p.setTextSize(Math.round(this.fullViewTileWidth * .6));
			     p.setAntiAlias(true);
			     p.setTypeface(this.bonusTypeface);
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
