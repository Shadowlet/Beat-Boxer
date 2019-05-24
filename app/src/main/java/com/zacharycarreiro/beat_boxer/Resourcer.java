package com.zacharycarreiro.beat_boxer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Resourcer {

    public static Map<String, Sprite> allBitmaps = new HashMap<>();
    public static Map<String, Music> allMusics = new HashMap<>();
    public static HashMap<String, Integer> toLoadBitmaps = new HashMap<>();
    public static HashMap<String, Integer> toLoadMusics = new HashMap<>();


    // *** Prepares a list of images to turn into sprites
    public static void Preload() {
        toLoadBitmaps.put("apple", R.drawable.apple);
        toLoadBitmaps.put("gggg", R.drawable.gggg);
        toLoadBitmaps.put("ggggg", R.drawable.ggggg);
        toLoadBitmaps.put("grid", R.drawable.grid);
        toLoadBitmaps.put("grid2", R.drawable.grid2);

        toLoadBitmaps.put("punchingGuy", R.drawable.punchingguy);
        toLoadBitmaps.put("punchingBag", R.drawable.punchingbag);
        toLoadBitmaps.put("meter", R.drawable.meter);
        toLoadBitmaps.put("arrow", R.drawable.arrow);

        toLoadBitmaps.put("lukas_duck", R.drawable.lukas_duck);
        toLoadBitmaps.put("lukas_idle", R.drawable.lukas_idle);
        toLoadBitmaps.put("lukas_punch", R.drawable.lukas_punch);
        toLoadBitmaps.put("lukas_ready", R.drawable.lukas_ready);
        toLoadBitmaps.put("lukas_fly", R.drawable.lukas_fly);
        toLoadBitmaps.put("lukas_victory", R.drawable.lukas_victory);

        toLoadBitmaps.put("button_controls", R.drawable.button_controls);
        toLoadBitmaps.put("button_exit", R.drawable.button_exit);
        toLoadBitmaps.put("button_play", R.drawable.button_play);

        toLoadBitmaps.put("title_backdrop", R.drawable.title_page_scale); // title_page

        toLoadBitmaps.put("scene_treadmill", R.drawable.scene_threadmill);
        toLoadBitmaps.put("scene_weightlift", R.drawable.scene_weightlift);
        toLoadBitmaps.put("scene_weightrack", R.drawable.scene_weightrack);

        
        toLoadMusics.put("test", R.raw.test);
        toLoadMusics.put("emphasis", R.raw.emphasis);
        toLoadMusics.put("c_hc", R.raw.c_hc);

    }

    public static Resources resources;

    public static void Setup(Resources r) {
        resources = r;


        Preload();

        Bitmap mBitmapMeter = BitmapFactory.decodeResource(r,R.drawable.meter);
        Log.d("WIDTH", String.valueOf(mBitmapMeter.getWidth()));
        Log.d("HEIGHT", String.valueOf(mBitmapMeter.getHeight()));

        // ****************************************************************************************
        // [[ GRAPHICS/IMAGES ]]
        allBitmaps.clear();
        //
        Bitmap bm;
        for (String Q : toLoadBitmaps.keySet()) {
            bm = BitmapFactory.decodeResource(r, toLoadBitmaps.get(Q));
            //
            // bm = Bitmap.createScaledBitmap(bm, bm.getWidth(), bm.getHeight(), false);
            //
            allBitmaps.put(Q, new Sprite(bm));
        }
        //
        toLoadBitmaps.clear();

        /*
        //Initialize matrix objects ready for us in drawGame
        matrix90.postRotate(90);
        matrix180.postRotate(180);
        matrix270.postRotate(270);
        //And now the head flipper
        matrixHeadFlip.setScale(-1,1);
        matrixHeadFlip.postTranslate(headBitmap.getWidth(),0);

        //setup the first frame of the flower drawing
        flowerRectToBeDrawn = new Rect((flowerFrameNumber * frameWidth), 0,
                (flowerFrameNumber * frameWidth +frameWidth)-1, frameHeight);
        */



        // ****************************************************************************************
        // [[ AUDIO/MUSIC ]]
        allMusics.clear();
        //
        int mu;
        for (String Q : toLoadMusics.keySet()) {
            mu = toLoadMusics.get(Q);
            //
            Music music = new Music(mu);
            music.name = Q;
            //
            allMusics.put(Q, music);
        }
        //
        toLoadMusics.clear();



        Postload();
    }

    // *** Sets up the details for any loaded sprites
    public static void Postload() {
        Sprite Q;
        //
        Q = allBitmaps.get("apple");
        Q.Cornerize();


        Q = allBitmaps.get("gggg");
        Q.frameCount = 7;
        Q.frameAcross = 3;
        Q.frameWidth = 192;
        Q.frameHeight = 192;
        Q.Centralize();

        Q = allBitmaps.get("ggggg");
        Q.frameCount = 7;
        Q.frameAcross = 7;
        Q.frameWidth = 64;
        Q.frameHeight = 64;
        Q.Centralize();


        Q = allBitmaps.get("grid");
        Q.Cornerize();
        Q = allBitmaps.get("grid2");
        Q.Cornerize();

        Q = allBitmaps.get("meter");
        Q.Centralize();
        Q = allBitmaps.get("arrow");
        Q.Centralize();

        Q = allBitmaps.get("punchingBag");
        Q.Centralize();
        Q.offsetY = 0;

        Q = allBitmaps.get("punchingGuy");
        Q.Centralize();
        Q.offsetY = Q.GetHeight();

        Q = allBitmaps.get("lukas_punch");
        Q.frameCount = 5;
        Q.frameAcross = 5;
        Q.frameWidth = Q.GetWidth()/5;
        Q.Centralize();
        Q.offsetX = 0;
        Q.offsetY = Q.GetHeight()-32;
        Q = allBitmaps.get("lukas_duck");
        Q.frameCount = 5;
        Q.frameAcross = 5;
        Q.frameWidth = Q.GetWidth()/5;
        Q.Centralize();
        Q.offsetX = 0;
        Q.offsetY = Q.GetHeight()-32;
        Q = allBitmaps.get("lukas_ready");
        Q.Centralize();
        Q.offsetX = 0;
        Q.offsetY = Q.GetHeight()-32;
        Q = allBitmaps.get("lukas_idle");
        Q.frameCount = 4;
        Q.frameAcross = 4;
        Q.frameWidth = Q.GetWidth()/4;
        Q.Centralize();
        Q.offsetX = 0;
        Q.offsetY = Q.GetHeight()-32;
        Q = allBitmaps.get("lukas_fly");
        Q.Centralize();
        Q.offsetX = 0;
        Q.offsetY = Q.GetHeight()-32;
        Q = allBitmaps.get("lukas_victory");
        Q.Centralize();
        Q.offsetX = 0;
        Q.offsetY = Q.GetHeight()-32;


        Q = allBitmaps.get("scene_treadmill");
        Q.Centralize();
        Q = allBitmaps.get("scene_weightlift");
        Q.Centralize();
        Q = allBitmaps.get("scene_weightrack");
        Q.Centralize();


        Music P;
        P = allMusics.get("test");
        P.bitRate = 1411;
        P.barCount = 25;
        P._tempo = 140;
        P.beatDensity = 4;

        P = allMusics.get("emphasis");
        P.bitRate = 1411;
        P.barCount = 25;
        P._tempo = 140;
        P.beatDensity = 4;
        P.preDelay = 250;
        P.title = "Catch: Emphasis";


        P = allMusics.get("c_hc");
        P.bitRate = 1411;
        P.barCount = 64;
        P._tempo = 180;
        P.beatDensity = 16;
        P.preDelay = 250;
        P.walkmanned = R.raw.c_hc_walkman;
        P.title = "C_HC (clap)";
    }
}

