package com.zacharycarreiro.beat_boxer;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;


/*
Changes:
- Viewport (allows for "screen shakes" by VERY briefly "zooming in")
- Higher beat resolution (it is now possible to make 1/16th beats, not just 1/4th)
	*** Fully backwards-compatible-- NP
- New "types" of beats (Easy = forgiving, Hard = kills you)
 */

public class ScreenActivity extends Activity {

    Canvas canvas;
    GamePlayView gameView;

    //Used for getting display details like the number of pixels
    Display display;
    Point size;

    //stats
    long lastFrameTime;
    int fps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        gameView = new GamePlayView(this);
        setContentView(gameView);

        //Get the screen size in pixels
        display = getWindowManager().getDefaultDisplay();

        size = new Point();
        display.getSize(size);
    }

    class GamePlayView extends SurfaceView implements Runnable {
        Thread ourThread = null;
        SurfaceHolder ourHolder;
        volatile boolean gameIsOn;
        Paint paint;

        public GameManager gm;

        public GamePlayView(Context context) {
            super(context);
            ourHolder = getHolder();
            paint = new Paint();
            //
            Artist.Initialize(getWindowManager().getDefaultDisplay(), getResources().getDisplayMetrics());
            //
            Resourcer.Setup(getResources());


            Artist.SetScreenSize(1800, 1080);
            // Artist.SetScreenSize(100, 100);
            // Artist.SetScreenSize(32);




            gm = GameManager.CreateInstance();
            gm.Initialize();
            //
            gm.Setup((Activity)context);

            Artist.viewport.width = 1000;
        }

        @Override
        public void run() {
            while (gameIsOn) {
                Update();
                _Draw();
                //
                controlFPS();
            }
        }



        public void Update() {
            gm.Update();
        }

        private void _Draw() {
            if (ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                //
                //
                Artist.Setup(canvas, paint);
                //
                Draw();
                //
                //
                ourHolder.unlockCanvasAndPost(canvas);
            }

        }
        public void Draw() {
            gm.Draw(canvas, paint);
            //
            //
            paint.setColor(Color.argb(255, 255, 0, 0));
            paint.setTextSize(45);
            canvas.drawText(Helper.debugMessage + "\nfps:" + fps, 20, 40, paint);
        }


        public int runTime = 0;
        public void controlFPS() {
            runTime++;

            long timeThisFrame = (System.currentTimeMillis() - lastFrameTime);
            long timeToSleep = 15 - timeThisFrame;
            if (timeThisFrame > 0) {
                if (runTime % 60 == 0) {
                    fps = (int) (1000 / timeThisFrame);
                }
            }
            if (timeToSleep > 0) {

                try {
                    ourThread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                }

            }

            lastFrameTime = System.currentTimeMillis();
        }


        public void Pause() {
            gameIsOn = false;
            try {
                ourThread.join();
            } catch (InterruptedException e) {
            }

        }

        public void Resume() {
            gameIsOn = true;
            ourThread = new Thread(this);
            ourThread.start();
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return Inputter.Read(event);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        while (true) {
            gameView.Pause();
            break;
        }

        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        gameView.Resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.Pause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            gameView.Pause();
            finish();
            return true;
        }
        return false;
    }
}
