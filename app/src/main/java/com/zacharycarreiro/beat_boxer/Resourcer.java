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


        toLoadMusics.put("test", R.raw.test);
    }

    public static Resources resources;

    public static void Setup(Resources r) {
        resources = r;


        Preload();


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
            allMusics.put(Q, new Music(mu));
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



        Music P;
        P = allMusics.get("test");
        P.bitRate = 1411;
        P.barCount = 17;
        P._tempo = 140;
        P.beatDensity = 4;
    }
}

