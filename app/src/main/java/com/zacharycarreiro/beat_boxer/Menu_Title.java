package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Menu_Title extends Actor {



    ButtonArea playButton;
    ButtonArea controlsButton;
    ButtonArea exitButton;


    int runningTime;
    int selection = -1;
    int pickTime;

    public Menu_Title() {
        int xx, yy;
        xx = 1484 -120;

        yy = (int)(Artist.screenHeight*(391/1080f));
        playButton = new ButtonArea("button_play", new Rect(10, 10, 10+312, 10+117), new Runnable() {
            @Override
            public void run() {
                GameManager.CreateInstance().mainMenu.MakeSelection(0);
            }
        });

        yy = (int)(Artist.screenHeight*(573/1080f));
        controlsButton = new ButtonArea("button_controls", new Rect(xx, yy, xx+312, yy+117), new Runnable() {
            @Override
            public void run() {

            }
        });

        yy = (int)(Artist.screenHeight*(753/1080f));
        exitButton = new ButtonArea("button_exit", new Rect(xx, yy, xx+312, yy+117), new Runnable() {
            @Override
            public void run() {

            }
        });
    }


    @Override
    public void Update() {
        super.Update();


        runningTime++;


        if (selection >= 0) {
            if (runningTime-pickTime >= Helper.SECOND*4) {
                GameManager.CreateInstance().Begin_PunchMode();
            }
        }
    }

    @Override
    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);


        // Artist.drawBitmap("title_backdrop", 0, x, y, 1*4, 1*4, 0, true);
        //
        if (selection >= 0) {
            float jist1 = Helper.Longevity(runningTime, pickTime, Helper.SECOND/3);
            p.setColor(Color.argb((int)(255 *(1-jist1)), 255, 255, 255));
            Artist.drawRect(0, 0, Artist.screenWidth, Artist.screenHeight);


            p.setColor(Color.argb((int)(255 *Helper.Longevity(runningTime, pickTime +Helper.SECOND*1, Helper.SECOND*1)), 0, 0, 0));
            Artist.drawRect(0, 0, Artist.screenWidth, Artist.screenHeight);
        }
    }


    @Override
    public void Discard() {
        super.Discard();


        playButton.Despawn();
        controlsButton.Despawn();
        exitButton.Despawn();
    }



    public void MakeSelection(int sel) {
        if (selection >= 0) return;

        selection = sel;
        pickTime = runningTime;


        playButton.visible = false;
        controlsButton.visible = false;
        exitButton.visible = false;

    }


}
