package com.zacharycarreiro.beat_boxer;

import android.app.Activity;
import android.content.Context;

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



    public void Initialize() {

    }

    public void Setup(Activity a) {
        activity = a;


    }
}
