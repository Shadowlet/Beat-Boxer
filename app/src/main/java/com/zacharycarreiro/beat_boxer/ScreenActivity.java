package com.zacharycarreiro.beat_boxer;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

public class ScreenActivity extends Activity {

    Canvas canvas;
    GamePlayView gameView;

    //Sound
    //initialize sound variables
    private SoundPool soundPool;
    int sample1 = -1;
    int sample2 = -1;
    int sample3 = -1;
    int sample4 = -1;

    //Used for getting display details like the number of pixels
    Display display;
    Point size;
    int screenWidth;
    int screenHeight;

    //!!Candidates for class!!!
    //Game objects
    int racketWidth;
    int racketHeight;
    Point racketPosition;

    Point ballPosition;
    int ballWidth;

    //for racket movement
    boolean racketIsMovingLeft;
    boolean racketIsMovingRight;

    //stats
    long lastFrameTime;
    int fps;
    int score;
    int lives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        gameView = new GamePlayView(this);
        setContentView(gameView);

        //Sound code
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            //Create objects of the 2 required classes
            AssetManager assetManager = getAssets();
            AssetFileDescriptor descriptor;

            //create our three fx in memory ready for use
            descriptor = assetManager.openFd("sample1.ogg");
            sample1 = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("sample2.ogg");
            sample2 = soundPool.load(descriptor, 0);


            descriptor = assetManager.openFd("sample3.ogg");
            sample3 = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("sample4.ogg");
            sample4 = soundPool.load(descriptor, 0);


        } catch (IOException e) {
            //catch exceptions here
        }


        //Get the screen size in pixels
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;


        //The game objects
        racketPosition = new Point();
        racketPosition.x = screenWidth / 2;
        racketPosition.y = screenHeight - 20;
        racketWidth = screenWidth / 8;
        racketHeight = 10;

        ballWidth = screenWidth / 35;
        ballPosition = new Point();
        ballPosition.x = screenWidth / 2;
        ballPosition.y = 1 + ballWidth;

        lives = 3;

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
            Resourcer.Setup(getResources());
            //
            Artist.Initialize(getWindowManager().getDefaultDisplay());


            Artist.SetScreenSize(1800, 1080);
            // Artist.SetScreenSize(100, 100);
            // Artist.SetScreenSize(32);



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


        public void Initialize() {
            new TestingGuy();
        }
        public void Update() {
            for (Actor a : Actor.actorList) {
                a.Update();
            }
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
            canvas.drawColor(Color.argb(255, 255, 0, 255));//the background
            //
            paint.setColor(Color.BLACK);
            Artist.drawRect(0, 0,
                    Artist.screenWidth, Artist.screenHeight);
            //
            for (Actor a : Actor.actorList) {
                a.Draw(canvas, paint);
            }

            /*
            //Draw the squash racket
            canvas.drawRect(racketPosition.x - (racketWidth / 2),
                    racketPosition.y - (racketHeight / 2), racketPosition.x + (racketWidth / 2),
                    racketPosition.y + racketHeight, paint);

            //Draw the ball
            canvas.drawRect(ballPosition.x, ballPosition.y,
                    ballPosition.x + ballWidth, ballPosition.y + ballWidth, paint);
            */


            paint.setColor(Color.argb(255, 255, 0, 0));
            paint.setTextSize(45);
            canvas.drawText("xR:" + Artist.screenWidth + " yR:" + Artist.screenHeight + " fps:" + fps, 20, 40, paint);
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
        public boolean onTouchEvent(MotionEvent motionEvent) {

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:

                    if (motionEvent.getX() >= screenWidth / 2) {
                        racketIsMovingRight = true;
                        racketIsMovingLeft = false;
                    } else {
                        racketIsMovingLeft = true;
                        racketIsMovingRight = false;
                    }

                    break;


                case MotionEvent.ACTION_UP:
                    racketIsMovingRight = false;
                    racketIsMovingLeft = false;
                    break;
            }
            return true;
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
