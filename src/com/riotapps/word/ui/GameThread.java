package com.riotapps.word.ui;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
	 private SurfaceHolder _surfaceHolder;
	 volatile boolean _running = false;
	  
	 GameSurfaceView parent;
	 long sleepTime;
	  
//	 public GameThread(GameSurfaceView sv, long st){
//	  super();
//	  parent = sv;
//	  sleepTime = st;
//	 }
	 
	  public GameThread(SurfaceHolder surfaceHolder, GameSurfaceView surfaceView) {
	    _surfaceHolder = surfaceHolder;
	    parent = surfaceView;
	 }
	  
	  public SurfaceHolder getSurfaceHolder() {
		    return _surfaceHolder;
	  }
	  
	  
	 public void setRunning(boolean r){
	  _running = r;
	 }
	  
	 @Override
	 public void run() {
		 Canvas c;
		    while (_running) {
		    	if (parent.isReadyToDraw()) {
		        c = null;
		        try {
		            c = _surfaceHolder.lockCanvas(null);
		            synchronized (_surfaceHolder) {
		            	 parent.onDraw(c); 
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
