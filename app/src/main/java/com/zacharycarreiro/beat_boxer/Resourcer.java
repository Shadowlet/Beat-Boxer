package com.zacharycarreiro.beat_boxer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Debug;

import java.util.HashMap;
import java.util.Map;

public class Resourcer {

    public static Map<String, Sprite> allBitmaps = new HashMap<>();
    public static HashMap<String, Integer> toLoad = new HashMap<>();


    // *** Prepares a list of images to turn into sprites
    public static void Preload() {
        toLoad.put("apple", R.drawable.apple);
        toLoad.put("gggg", R.drawable.gggg);
        toLoad.put("ggggg", R.drawable.ggggg);
        toLoad.put("punchingGuy", R.drawable.punchingguy);
        toLoad.put("punchingBag", R.drawable.punchingbag);
        toLoad.put("arrow", R.drawable.arrow);
        toLoad.put("meter", R.drawable.meter);

    }

    public static void Setup(Resources r) {
        Preload();


        allBitmaps.clear();
        //
        Bitmap bm;
        for (String Q : toLoad.keySet()) {
            bm = BitmapFactory.decodeResource(r, toLoad.get(Q));
            //
            // bm = Bitmap.createScaledBitmap(bm, bm.getWidth(), bm.getHeight(), false);
            //
            allBitmaps.put(Q, new Sprite(bm));
        }
        //
        toLoad.clear();

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


        Postload();
    }

    // *** Sets up the details for any loaded sprites
    public static void Postload() {
        Sprite Q;
        Sprite R;
        Sprite S;
        //
        Q = allBitmaps.get("apple");
        Q.Centralize();


        Q = allBitmaps.get("gggg");
        Q.Cornerize();
        Q.frameCount = 7;
        Q.frameAcross = 3;
        Q.frameWidth = 192;
        Q.frameHeight = 192;

        Q = allBitmaps.get("ggggg");
        Q.Centralize();
        Q.frameCount = 7;
        Q.frameAcross = 7;
        Q.frameWidth = 64;
        Q.frameHeight = 64;

        Q = allBitmaps.get("punchingBag");
        Q.offsetX = Q.GetWidth()/2;
        Q.offsetY = 0;



        S = allBitmaps.get("meter");

        R = allBitmaps.get("arrow");
        R.offsetX = S.GetWidth();



    }
}

