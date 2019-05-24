package com.zacharycarreiro.beat_boxer;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

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

    Menu_Title mainMenu;
    Holder_PunchMode punchMode;
    GameTimeline gameplayTimeline;

    public void Initialize() {
        mainMenu = new Menu_Title();
    }

    public void Update() {
        if (punchMode != null) {
            punchMode.Update();
        }
        //
        if (gameplayTimeline != null) {
            gameplayTimeline.Update();
        }
        //
        //

        Object[] list = Actor.actorList.toArray();
        for (Object o : list) {
            Actor a = (Actor)o;

            a.Process();
            //
            a.Update();
        }
        //
        //
        Inputter.Clear();
    }

    public void Draw(Canvas c, Paint p) {
        if (punchMode != null) {
            punchMode.Draw(c, p);
        }


        Object[] list = Actor.actorList.toArray();
        for (Object o : list) {
            Actor a = (Actor)o;

            a.Draw(c, p);
        }


        //
        if (gameplayTimeline != null) {
            gameplayTimeline.Draw(c, p);
        }
    }

    public void Setup(Activity a) {
        activity = a;
    }

    public void Begin_PunchMode() {
        mainMenu.Despawn();
        //
        punchMode = new Holder_PunchMode();
    }

    public void CollectGarbage() {
        for (Actor a : Actor.removeList) {
            a._Unregister();
        }
        //
        Actor.removeList.clear();
    }
}
