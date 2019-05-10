package com.zacharycarreiro.beat_boxer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;


public class Meter extends Actor{

    @Override
    public void Update() {

    }


    @Override
    public void Draw(Canvas c, Paint p) {
        float size = 10;


        //Log.d("bitMeter","Hi" + );

        Bitmap Q = Resourcer.allBitmaps.get("arrow").bitmap;
        Bitmap R = Resourcer.allBitmaps.get("meter").bitmap;
        Artist.drawBitmap("arrow", 0, x, y, 1, 1, 0);
        Artist.drawBitmap("meter", 0, x, y+ Artist.screenHeight * 0.08f, 1, 1, 0);
    }



    @Override
    public void onCreate() {
        x = Artist.screenWidth * 0.05f;
        y = Artist.screenHeight * 0.8f;
    }

}
