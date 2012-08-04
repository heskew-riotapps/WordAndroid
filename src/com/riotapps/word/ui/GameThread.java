package com.riotapps.word.ui;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
	 private SurfaceHolder _surfaceHolder;
	 volatile boolean running = false;
	  
	 GameSurfaceView parent;
	 long sleepTime;
	  
	 public GameThread(GameSurfaceView sv, long st){
	  super();
	  parent = sv;
	  sleepTime = st;
	 }
	 
	  public GameThread(SurfaceHolder surfaceHolder, GameSurfaceView surfaceView) {
	        _surfaceHolder = surfaceHolder;
	        parent = surfaceView;
	    }
	  
	 public void setRunning(boolean r){
	  running = r;
	 }
	  
	 @Override
	 public void run() {
	  // TODO Auto-generated method stub
		 Canvas c;
		    while (running) {
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
		 
		 
	  //while(running){
	 
	   //try {
	   // sleep(sleepTime);
	   // parent.updateSurfaceView();
	  // } catch (InterruptedException e) {
	  //  // TODO Auto-generated catch block
	  //  e.printStackTrace();
	  // }
	 
	 // }
	 }
	 
	}
