package com.riotapps.word.ui;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
	 private SurfaceHolder _surfaceHolder;
	 volatile boolean _running = false;
	  
	 GameSurfaceView _parent;
	 long sleepTime;
	  
	 public GameThread(GameSurfaceView sv, long st){
	  super();
	  _parent = sv;
	  sleepTime = st;
	 }
	 
	  public GameThread(SurfaceHolder surfaceHolder, GameSurfaceView surfaceView) {
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
