package com.zacharycarreiro.beat_boxer;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameManager {

    private static GameManager _instance = null;
    public static GameManager CreateInstance() {
        if (_instance == null) {
            _instance = new GameManager();
        }

        return _instance;
    }

    private GameManager() {}


    public Activity activity;


    public Meter obj_meter;

    GameTimeline gameplayTimeline;

    public void Initialize() {
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

        // -------------------------------------------------------------
        // [Game Manager Stuff]
        obj_meter = m;
    }

    public void Update() {
        gameplayTimeline.Update();
        //
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

    public void Draw(Canvas c, Paint p) {
        c.drawColor(Color.argb(255, 0, 0, 0));//the background
        //
        //
        Artist.drawBitmap("grid2", 0, 0, 0, 1f, 1f, 0);
        //
        //
        p.setColor(Color.argb(255,180,180,180));
        Artist.drawRect(0, 0, Artist.screenWidth, Artist.screenHeight/3);
        Artist.drawRect(0, Artist.screenHeight - Artist.screenHeight/3, Artist.screenWidth, Artist.screenHeight);
        //
        p.setColor(Color.argb(255,0,0,0));
        Artist.drawRect(0, 0, Artist.screenWidth, Artist.screenHeight/7);
        Artist.drawRect(0, Artist.screenHeight - Artist.screenHeight/7, Artist.screenWidth, Artist.screenHeight);
        //
        for (Actor a : Actor.actorList) {
            a.Draw(c, p);
        }



        p.setColor(Color.argb(255,0,255,255));
        //Artist.drawRect(Artist.screenWidth * 0.5f, Artist.screenHeight * 0.8f, Artist.screenWidth * 0.5f +10, Artist.screenHeight * 0.8f +10);


        gameplayTimeline.Draw(c, p);
    }

    public void Setup(Activity a) {
        activity = a;
        //
        gameplayTimeline.Initialize();
    }
}
