package com.riotapps.word;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
 
public class Tutorial2d extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new Panel(this));
    }
 
    class Panel extends SurfaceView implements SurfaceHolder.Callback {
        private TutorialThread _thread;
 
        public Panel(Context context) {
            super(context);
            getHolder().addCallback(this);
            _thread = new TutorialThread(getHolder(), this);
        }
 
        @Override
        public void onDraw(Canvas canvas) {
            Bitmap _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.bonus_3l);
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(_scratch, 10, 10, null);
        }
 
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
 
        }
 
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            _thread.setRunning(true);
            _thread.start();
        }
 
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // simply copied from sample application LunarLander:
            // we have to tell thread to shut down & wait for it to finish, or else
            // it might touch the Surface after we return and explode
            boolean retry = true;
            _thread.setRunning(false);
            while (retry) {
                try {
                    _thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // we will try it again and again...
                }
            }
        }
    }
 
    class TutorialThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private Panel _panel;
        private boolean _run = false;
 
        public TutorialThread(SurfaceHolder surfaceHolder, Panel panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }
 
        public void setRunning(boolean run) {
            _run = run;
        }
 
        @Override
        public void run() {
            Canvas c;
            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        _panel.onDraw(c);
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
