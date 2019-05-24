package com.zacharycarreiro.beat_boxer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Holder_PunchMode extends Entity {


    public Holder_PunchMode() {
        GameManager gm = GameManager.CreateInstance();

        // *** Entities, doesn't update/draw automatically
        gm.gameplayTimeline = GameTimeline.CreateInstance();


        // *** Actors
        // new TestingGuy("gggg");
        Meter m = new Meter();
        m.myArrow = new Arrow();


        PunchingGuy g = new PunchingGuy();
        PunchingBag p = new PunchingBag();
        p.myGuy = g;
        p.myMeter = m;
        //
        g.x = p.x - 64 * 6.5f;
        g.y = p.y + 64 * 11;

        // -------------------------------------------------------------
        // [Game Manager Stuff]
        gm.obj_meter = m;
    }



    public void Draw(Canvas c, Paint p) {
        super.Draw(c, p);



        c.drawColor(Color.argb(255, 255, 255, 255));//the background
        //
        //
        // Artist.drawBitmap("grid2", 0, 0, 0, 1f, 1f, 0);


        p.setColor(Color.argb(255,180,180,180));
        Artist.drawRect(0, 0, Artist.screenWidth, Artist.screenHeight/3);
        Artist.drawRect(0, Artist.screenHeight - Artist.screenHeight/3, Artist.screenWidth, Artist.screenHeight);
        //
        p.setColor(Color.argb(255,0,0,0));
        Artist.drawRect(0, 0, Artist.screenWidth, Artist.screenHeight/7);
        Artist.drawRect(0, Artist.screenHeight - Artist.screenHeight/7, Artist.screenWidth, Artist.screenHeight);


/*
        int xx, yy;
        yy = (int)(Artist.screenHeight*(5/8f));
        Artist.drawBitmap("scene_treadmill", 0, Artist.screenWidth *(1/10f), yy, 2f, 2f, 0);
        Artist.drawBitmap("scene_treadmill", 0, Artist.screenWidth *(0/10f), yy, 2f, 2f, 0);
        Artist.drawBitmap("scene_treadmill", 0, Artist.screenWidth *(-1/10f), yy, 2f, 2f, 0);
        //
        Artist.drawBitmap("scene_weightrack", 0, Artist.screenWidth *(2/10f), yy-16, 2f, 2f, 0);
        Artist.drawBitmap("scene_weightrack", 0, Artist.screenWidth *(3/10f), yy-16, 2f, 2f, 0);
        //
        Artist.drawBitmap("scene_weightlift", 0, Artist.screenWidth *(5/10f), yy, 2f, 2f, 0);
        Artist.drawBitmap("scene_weightlift", 0, Artist.screenWidth *(6.2f/10f), yy, 2f, 2f, 0);
        Artist.drawBitmap("scene_weightlift", 0, Artist.screenWidth *(7.4f/10f), yy, 2f, 2f, 0);
*/
    }

}