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




            GameManager gm = GameManager.CreateInstance();
            gm.Initialize();
            //
            gm.Setup((Activity)context);


            Initialize();
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


        public GameTimeline gameplayTimeline;

        public void Initialize() {
        /*
            // *** Entities, doesn't update/draw automatically
            gameplayTimeline = GameTimeline.CreateInstance();


            // *** Actors
            // new TestingGuy("gggg");
            Meter m = new Meter();
            m.myArrow = new Arrow();


            PunchingGuy g = new PunchingGuy();
            PunchingBag p = new PunchingBag();
            p.myGuy = g;
            p.myMeter = m;


        */


        }
        public void Update() {
            //gameplayTimeline.Update();
            //
            for (Actor a : Actor.actorList) {
                a.Process();
                //
                a.Update();
            }
            //
            //
            Inputter.Clear();
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
            canvas.drawColor(Color.argb(255, 0, 0, 0));//the background
            //
            //
            Artist.drawBitmap("grid2", 0, 0, 0, 1f, 1f, 0);
            //
            //
            paint.setColor(Color.argb(255,180,180,180));
            Artist.drawRect(0, 0, Artist.screenWidth, Artist.screenHeight/3);
            Artist.drawRect(0, Artist.screenHeight - Artist.screenHeight/3, Artist.screenWidth, Artist.screenHeight);
            //
            paint.setColor(Color.argb(255,0,0,0));
            Artist.drawRect(0, 0, Artist.screenWidth, Artist.screenHeight/7);
            Artist.drawRect(0, Artist.screenHeight - Artist.screenHeight/7, Artist.screenWidth, Artist.screenHeight);
            //
            for (Actor a : Actor.actorList) {
                a.Draw(canvas, paint);
            }









            paint.setColor(Color.argb(255,0,255,255));
            //Artist.drawRect(Artist.screenWidth * 0.5f, Artist.screenHeight * 0.8f, Artist.screenWidth * 0.5f +10, Artist.screenHeight * 0.8f +10);







            // gameplayTimeline.Draw(canvas, paint);
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
