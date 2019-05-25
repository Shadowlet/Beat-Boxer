package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.SystemClock;
import android.view.MotionEvent;

public class Menu_Title extends Actor {



    ButtonArea playButton;
    ButtonArea controlsButton;
    ButtonArea exitButton;
    SongToggleButton songButton;


    int runningTime;
    int selection = -1;
    int pickTime;

    int songIndex = 0;


    long startTime;
    long pickMoment;


    public Menu_Title() {
        startTime = SystemClock.uptimeMillis();


        int xx, yy;
        xx = 1484 -120;

        yy = (int)(Artist.screenHeight*(391/1080f));
        playButton = new ButtonArea("button_play", new Rect(xx, yy, xx+312, yy+117), new Runnable() {
            @Override
            public void run() {
                GameManager.CreateInstance().mainMenu.MakeSelection(0);
            }
        });

        yy = (int)(Artist.screenHeight*(573/1080f));
        controlsButton = new ButtonArea("button_controls", new Rect(xx, yy, xx+312, yy+117), new Runnable() {
            @Override
            public void run() {
                showingControls = true;
                //
                playButton.visible = false;
                controlsButton.visible = false;
                exitButton.visible = false;
                songButton.visible = false;
            }
        });

        yy = (int)(Artist.screenHeight*(753/1080f));
        exitButton = new ButtonArea("button_exit", new Rect(xx, yy, xx+312, yy+117), new Runnable() {
            @Override
            public void run() {
                // System.exit(0);

                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });



        yy = (int)(Artist.screenHeight*(253/1080f));
        songButton = new SongToggleButton(xx, yy, new Runnable() {
            @Override
            public void run() {
                songIndex = (songIndex + 1) % Databaser.CreateInstance().GamePlaylist.length;
                //
                UpdateSongInfo();
            }
        });
        UpdateSongInfo();
    }


    @Override
    public void Update() {
        super.Update();


        runningTime++;
        long pickedTime = (SystemClock.uptimeMillis()-pickMoment);


        if (selection >= 0) {
            if (pickedTime >= 1000*4) {
                GameManager.CreateInstance().Begin_PunchMode(songIndex);
            }
        }
        else {
            if (!Sounder.Music_IsPlaying()) {
                Sounder.Music_Play(R.raw.rival, 0);
            }
        }


        if (showingControls) {
            if (Inputter.check(MotionEvent.ACTION_DOWN, null)) {
                showingControls = false;
                //
                playButton.visible = true;
                controlsButton.visible = true;
                exitButton.visible = true;
                songButton.visible = true;
            }
        }
    }

    public boolean showingControls = false;

    @Override
    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);


        long pickedTime = (SystemClock.uptimeMillis()-pickMoment);

        /*
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        //
        Artist.drawBitmap("title_backdrop", 0, x, y, 1, 1, 0, true);
        //
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        */
        c.drawColor(Color.DKGRAY);
        Artist.drawBitmap("title_backdrop", 0, x, y, 1, 1, 0, true);


        if (showingControls) {
            Artist.drawBitmap("game_controls", 0, Artist.screenWidth/2, Artist.screenHeight/2, 2, 2, 0, true);
        }
        else {
            if (selection < 0) {
                Artist.drawBitmap("title_text", 0, Artist.screenWidth * (1 / 10f), Artist.screenHeight * ((float) ((1 + 0.3f * Math.sin(Math.PI * (pickedTime / 1000f))) / 10f)), 1, 1, 0, true);
            } else {
                float jist1 = Helper.Longevity(pickedTime, 0, 1000 / 3);
                p.setColor(Color.argb((int) (255 * (1 - jist1)), 255, 255, 255));
                Artist.drawRect(0, 0, Artist.screenWidth, Artist.screenHeight);


                p.setColor(Color.argb((int) (255 * Helper.Longevity(pickedTime, 0 + 1000 * 1, 1000 * 1)), 0, 0, 0));
                Artist.drawRect(0, 0, Artist.screenWidth, Artist.screenHeight);
            }
        }
    }


    @Override
    public void Discard() {
        super.Discard();


        GameManager.CreateInstance().mainMenu = null;

        playButton.Despawn();
        controlsButton.Despawn();
        exitButton.Despawn();
    }



    public void MakeSelection(int sel) {
        if (selection >= 0) return;

        selection = sel;
        pickTime = runningTime;
        pickMoment = SystemClock.uptimeMillis();


        playButton.visible = false;
        controlsButton.visible = false;
        exitButton.visible = false;
        songButton.visible = false;


        Sounder.Music_Stop();
        //
        Sounder.Sound_Play("menu_select");
    }


    public void UpdateSongInfo() {
        songButton.text = Resourcer.allMusics.get(Databaser.CreateInstance().GamePlaylist[songIndex]).title;
    }


}
